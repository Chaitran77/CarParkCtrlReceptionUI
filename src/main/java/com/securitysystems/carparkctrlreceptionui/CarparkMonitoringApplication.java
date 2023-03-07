package com.securitysystems.carparkctrlreceptionui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.Optional;
import java.util.Timer;
import java.util.concurrent.TimeUnit;

public class CarparkMonitoringApplication extends Application {

//    https://edencoding.com/contemporary-ui-java/
//    SOURCE: JavaFX CSS Reference Guide @ https://openjfx.io/javadoc/19/javafx.graphics/javafx/scene/doc-files/cssref.html
//    SOURCE: Making window without titlebar movable: https://github.com/edencoding/javafx-ui/blob/master/simple-ui/src/main/java/com/edencoding/App.java
//    SOURCE: Curated list of JavaFX frameworks: https://github.com/mhrimaz/AwesomeJavaFX/blob/master/README.md#frameworks
//    chose JFX for ability to create a contemporary, minimal look due to CSS layer and for modularisation to separate functional design, layout and design

	// login credentials are stored in memory since persistent logins are not desirable in the client

	private double xOffset;
	private double yOffset;

	private Timer logRetrieveTimer;
	@Override
	public void start(Stage stage) throws IOException {

		loginSequence(5);

		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/carpark-monitoring-view/carpark-monitoring-view.fxml"));

		Scene scene = new Scene(fxmlLoader.load(), 1080, 720);

		// allow the window to be dragged by the titlebar
		Node titlebar = scene.lookup("#titlebar");
		titlebar.setOnMousePressed(event -> {
			xOffset = event.getSceneX();
			yOffset = event.getSceneY();
			event.consume(); // prevent the event from being interpreted by anything else
		});

		titlebar.setOnMouseDragged(event -> {
			stage.setX(event.getScreenX() - xOffset);
			stage.setY(event.getScreenY() - yOffset);
			event.consume();
		});

		// give custom window controls normal functionality
		Button minimiseButton = (Button) scene.lookup("#minimiseButton");
		minimiseButton.setOnAction(event -> stage.setIconified(true));

		Button maximiseButton = (Button) scene.lookup("#maximiseButton");
		maximiseButton.setOnAction(event -> stage.setMaximized(!stage.isMaximized())); // set to opposite of isMaximised() - avoids use of an if (){} which would need isMaximised() anyway

		Button exitButton = (Button) scene.lookup("#exitButton");
		exitButton.setOnAction(event -> stage.close());
		/*  used Java's lambda syntax ((parameter) -> statement) to avoid creating unnecessary anonymous classes (and improve legibility) as original code below:
			exitButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					stage.close();
				}
			});
		*/

		GridPane monitoringViewGridpane = (GridPane)scene.lookup("#monitoring-view-gridpane");
		// width of each image is 40% of the gridpane's width ((100%-20%)/2). EventsContainer's width is 20%.
		// image preserveRatios are set to true so heightProperty left unset.
		// used documentation https://openjfx.io/javadoc/11/javafx.graphics/javafx/scene/image/ImageView.html#fitWidthProperty()
		((ImageView) scene.lookup("#entry-snapshot-view")).fitWidthProperty().bind(monitoringViewGridpane.widthProperty().multiply(.4));
		((ImageView) scene.lookup("#exit-snapshot-view")).fitWidthProperty().bind(monitoringViewGridpane.widthProperty().multiply(.4));


		// search view (tab) follows same grid layout, so widths can be bound to monitoringViewGridpane
		((ImageView) scene.lookup("#search-entry-snapshot-view")).fitWidthProperty().bind(monitoringViewGridpane.widthProperty().multiply(.4));
		((ImageView) scene.lookup("#search-exit-snapshot-view")).fitWidthProperty().bind(monitoringViewGridpane.widthProperty().multiply(.4));

		((CarparkMonitoringController)fxmlLoader.getController()).setApplicationScene(scene);

		stage.setTitle("Carpark Monitoring and Control Client");
		stage.initStyle(StageStyle.UNDECORATED); // remove system's window styling
		stage.setScene(scene);
		stage.show();

		logRetrieveTimer = new Timer(); // below: Passing scene which elements can be derived from to avoid having many parameters
		logRetrieveTimer.schedule(new LoadLatestLogsTask(scene), 30, TimeUnit.SECONDS.toMillis(40));
	}

	public static void loginSequence(int loginAttemptsRemaining) { // use of remaining eliminates use of maxAttempts parameter

		if (loginAttemptsRemaining == 0) {
			Alert attemptsErrorAlert = new Alert(Alert.AlertType.ERROR, "You have tried to login with incorrect credentials too many times.\nPlease contact your administrator to continue.",  new ButtonType("Okay"));
			attemptsErrorAlert.showAndWait();
			return;
		}

		Optional<String[]> result = loginDialog(loginAttemptsRemaining).showAndWait();
		if (result.isPresent()) {
			// for clarity
			String username = result.get()[0];
			String password = result.get()[1];
			String loginSalt;
			try {
				loginSalt = HttpRequester.getLoginSalt(result.get()[0]);
				System.out.println(loginSalt);
				String hashedPassword = Hasher.getHashedString(password + loginSalt); // loginSalt is appended to password
				System.out.println(hashedPassword);

				try {
					// throws a custom Exception to indicate incorrect credentials. IOException will be handled (differently) by outer catch block.
					HttpRequester.login(username, hashedPassword);
				} catch (Exception exception) {
					// incorrect creds
					Alert errorAlert = new Alert(Alert.AlertType.WARNING, "Incorrect username and/or password.\n\n" + exception.getMessage(), new ButtonType("Try again"));
					errorAlert.showAndWait();
					loginSequence(loginAttemptsRemaining-1);
				}

			} catch (IOException exception) {
				// unable to connect to server
				Alert IOErrorAlert = new Alert(Alert.AlertType.WARNING, "Unable to login due to either a network or server error.\n\n" + exception.getMessage(), new ButtonType("Close"));
				IOErrorAlert.showAndWait();
				loginSequence(loginAttemptsRemaining);
			}
		}
	}

	public static Dialog<String[]> loginDialog(int loginAttemptsRemaining) {
		Dialog<String[]> loginDialog = new Dialog<>();
		GridPane container = new GridPane();
		container.setVgap(20);

		Label headingLabel = new Label("Login to carpark management client");
		headingLabel.setFont(new Font(20));
		Label usernameLabel = new Label("Username: ");
		Label passwordLabel = new Label("Password: ");
		TextField usernameInput = new TextField();
		TextField passwordInput = new PasswordField();
		usernameInput.setPromptText("Type your username...");
		passwordInput.setPromptText("Type your password...");
		Label attemptsRemainingLabel = new Label("Attempts remaining: " + loginAttemptsRemaining);

		container.add(headingLabel, 0, 0, 2, 1);
		container.add(usernameLabel, 0, 1);
		container.add(passwordLabel, 0, 2);
		container.add(usernameInput, 1, 1);
		container.add(passwordInput, 1, 2);
		container.add(attemptsRemainingLabel, 1, 3);

		// no cancel button since cannot proceed without logging in (correct creds)
		loginDialog.getDialogPane().getButtonTypes().add(new ButtonType("Confirm", ButtonBar.ButtonData.OK_DONE));

		// type of dialogButton is inferred through lambda syntax
		loginDialog.setResultConverter((dialogButton) -> { // since the confirm button is the only available button, no need to check which button was pressed
			return new String[]{usernameInput.getText(), passwordInput.getText()};
		});

		loginDialog.getDialogPane().setContent(container);
		return loginDialog;
	}

	public static void main(String[] args) {
		launch();
	}

	@Override
	public void stop() {
		logRetrieveTimer.cancel();
	}
}