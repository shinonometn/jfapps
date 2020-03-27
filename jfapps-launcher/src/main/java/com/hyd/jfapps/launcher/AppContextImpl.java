package com.hyd.jfapps.launcher;

import com.hyd.jfapps.appbase.AppContext;
import javafx.application.HostServices;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class AppContextImpl implements AppContext {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private Image icon;

    private String configFilePath;

    private Properties appProperties = new Properties();

    private HostServices hostServices;

    private Stage primaryStage;

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    @Override
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public void setHostServices(HostServices hostServices) {
        this.hostServices = hostServices;
    }

    @Override
    public HostServices getHostServices() {
        return hostServices;
    }

    public void setIcon(Image icon) {
        this.icon = icon;
    }

    @Override
    public Image getIcon() {
        return icon;
    }

    @Override
    public void setConfiguration(String s, String s1) {
        appProperties.setProperty(s, s1);

        try (OutputStream os = Files.newOutputStream(Paths.get(configFilePath))) {
            appProperties.store(os, null);
        } catch (IOException e) {
            log.error("", e);
        }
    }

    @Override
    public String getConfiguration(String s) {
        return appProperties.getProperty(s);
    }

    public void setConfigFilePath(String configFilePath) {
        this.configFilePath = configFilePath;
    }

    public void loadProperties() throws IOException {
        Path path = Paths.get(configFilePath);
        Files.createDirectories(path.getParent());

        if (Files.exists(path)) {
            try (InputStream inStream = Files.newInputStream(path)) {
                appProperties.load(inStream);
            }
        } else {
            Files.createFile(path);
        }
    }
}
