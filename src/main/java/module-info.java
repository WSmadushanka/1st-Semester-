module lk.ijse.gdse71.nl.nltecnologies {
    requires javafx.controls;
    requires javafx.fxml;
    requires lombok;
    requires java.sql;
    requires com.jfoenix;
    requires com.gluonhq.maps;
    requires java.mail;
    requires net.sf.jasperreports.core;
    requires com.google.zxing;
    requires com.google.zxing.javase;
    requires java.desktop;
    requires org.controlsfx.controls;
    requires webcam.capture;

    // Open DTO package for reflection
    opens lk.ijse.gdse71.nl.nltecnologies.dto.tm to javafx.base;

    // Open controller package for FXML loading
    opens lk.ijse.gdse71.nl.nltecnologies.controller to javafx.fxml;

    // Export main package for access
    exports lk.ijse.gdse71.nl.nltecnologies;

    // Ensure resources are accessible to JasperReports
    opens report to net.sf.jasperreports.core;
}
