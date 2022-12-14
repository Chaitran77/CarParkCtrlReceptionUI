module com.securitysystems.carparkctrlreceptionui {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;

    opens com.securitysystems.carparkctrlreceptionui to javafx.fxml;
    exports com.securitysystems.carparkctrlreceptionui;
}