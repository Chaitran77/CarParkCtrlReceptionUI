package com.securitysystems.carparkctrlreceptionui;

import javafx.event.EventHandler;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

public class IntegerInputField extends TextField {
	public IntegerInputField() {
		this.addEventFilter(KeyEvent.KEY_TYPED, new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				if (!event.getCharacter().matches("[0-9]")) {
					event.consume(); // stops event from bubbling up the TextField's normal input handler which would add the text to the TextField.
				}
			}
		});
	}

}
