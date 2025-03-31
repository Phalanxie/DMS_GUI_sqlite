module com.swdprojects.romkaicode.dmsgui {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires java.sql;
    requires jdk.compiler;

    opens com.swdprojects.romkaicode.dmsgui to javafx.fxml;
    exports com.swdprojects.romkaicode.dmsgui;
    exports com.swdprojects.romkaicode.dmsgui.model;
    opens com.swdprojects.romkaicode.dmsgui.model to javafx.fxml;
    exports com.swdprojects.romkaicode.dmsgui.controller;
    opens com.swdprojects.romkaicode.dmsgui.controller to javafx.fxml;
}