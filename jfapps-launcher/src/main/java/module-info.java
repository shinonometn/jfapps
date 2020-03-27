module jfapps.launcher {
    requires javafx.base;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.controls;

//    requires lombok;

    requires org.apache.commons.lang3;
    requires slf4j.api;

    requires jfapps.base;

    exports com.hyd.jfapps.launcher;
}