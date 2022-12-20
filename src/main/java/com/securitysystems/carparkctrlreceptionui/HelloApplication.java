package com.securitysystems.carparkctrlreceptionui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class HelloApplication extends Application {

//    https://edencoding.com/contemporary-ui-java/
//    SOURCE: JavaFX CSS Reference Guide @ https://openjfx.io/javadoc/19/javafx.graphics/javafx/scene/doc-files/cssref.html
//    SOURCE: Making window without titlebar movable: https://github.com/edencoding/javafx-ui/blob/master/simple-ui/src/main/java/com/edencoding/App.java
//    SOURCE: Curated list of JavaFX frameworks: https://github.com/mhrimaz/AwesomeJavaFX/blob/master/README.md#frameworks
//    choosing JFX for ability to create a contemporary, minimal look due to CSS layer and for modularisation to separate functional design, layout and design

    private double xOffset;
    private double yOffset;
    @Override
    public void start(Stage stage) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/carpark-monitoring-view/carpark-monitoring-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1080, 720);
        Node titlebar = scene.lookup("#titlebar");
        titlebar.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
            event.consume(); // prevent the event from being interpreted by anything elsee
        });

        titlebar.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);
            event.consume();
        });
        stage.setTitle("Hello!");
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}