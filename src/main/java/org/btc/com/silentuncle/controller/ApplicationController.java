package org.btc.com.silentuncle.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.btc.com.silentuncle.ResourceBundleContainer;
import org.btc.com.silentuncle.events.MainPageEvent;
import org.btc.com.silentuncle.events.StageReadyEvent;
import org.btc.com.silentuncle.events.TrayIconEvent;
import org.btc.com.silentuncle.services.LocaleService;
import org.btc.com.silentuncle.view.TrayIconView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;


@SuppressWarnings("CallToPrintStackTrace")
@Component
public class ApplicationController {

    public static ApplicationContext applicationContext;
    private final TrayIconView trayIconView;

    @FXML
    private Label label;
    @FXML
    private ComboBox<String> languageComboBox;

    @FXML
    private ImageView logoImageView;

    @FXML
    private Label userLabel;

    @FXML
    private Button settingsButton;

    @FXML
    private Button tierAlarmButton;

    @FXML
    private Button feuerAlarmButton;

    @Autowired
    private ResourceBundleContainer resourceBundleContainer;
    @Autowired
    private LocaleService localeService;


    ApplicationController(ApplicationContext applicationContext, TrayIconView trayIconView) {
        ApplicationController.applicationContext = applicationContext;
        this.trayIconView = trayIconView;


    }

    @FXML
    public void initialize() {
        //noinspection StatementWithEmptyBody
        if (languageComboBox == null) {
            //this
        } else {
            languageComboBox.getItems().addAll("Deutsch", "English");
            languageComboBox.setValue("English");  // Standardauswahl
        }
        userLabel.setText(System.getProperty("user.name"));
    }


    @EventListener
    public Stage onApplicationEvent(StageReadyEvent event) {
        try {
            Stage stage = event.getStage();
            trayIconView.setupTray(stage);
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(ApplicationController.class.getResource("/main.fxml"));
            fxmlLoader.setResources(resourceBundleContainer.getResourceBundle());
            return getStage(stage, fxmlLoader);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }
    @FXML
    private void handleTierAlarm() {
        System.out.println("Tier Alarm ausgelöst!");
        trayIconView.displayAnimalIncidentAlarm();
    }

    @FXML
    private void handleFeuerAlarm() {
        System.out.println("Feuer Alarm ausgelöst!");
        trayIconView.displayFireAlarm();
    }

    @FXML
    private void handleSettings() {
        System.out.println("Einstellungen geöffnet.");
    }

    @EventListener
    public Stage onMainPageEvent(MainPageEvent event) {
        try {
            Stage stage = event.getStage();
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(TrayIconView.class.getResource("/main.fxml"));
            fxmlLoader.setResources(resourceBundleContainer.getResourceBundle());
            getStage(stage, fxmlLoader);
            return stage;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;

    }

    @EventListener
    public Stage onTrayIconEvent(TrayIconEvent event) {
        try {
            Stage stage = event.getStage();
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(TrayIconView.class.getResource("/settings.fxml"));
            fxmlLoader.setResources(resourceBundleContainer.getResourceBundle());
            getStage(stage, fxmlLoader);
            settingsButton.setOnAction(e -> changeLanguage());
            return stage;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;

    }

    private void changeLanguage() {
        String selectedLanguage = languageComboBox.getValue();

        // Ändere die Locale basierend auf der Auswahl
        if (selectedLanguage.equals("Deutsch")) {
            localeService.setLocale(Locale.GERMAN);
        } else {
            localeService.setLocale(Locale.ENGLISH);
        }
        updateTexts();
        trayIconView.updateTrayIconTexts();
    }

    private void updateTexts() {
        if (label != null) {
            label.setText(resourceBundleContainer.getResourceBundle().getString("settingsLabel"));
        }
        if (settingsButton != null) {
            settingsButton.setText(resourceBundleContainer.getResourceBundle().getString("settingsButton"));
        }


    }

    private Stage getStage(Stage stage, FXMLLoader fxmlLoader) throws IOException {
        fxmlLoader.setControllerFactory(applicationContext::getBean);
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root, 600, 400);
        stage.setScene(scene);
        stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResource("/silentunclelogo.png")).toExternalForm()));
        stage.setTitle(resourceBundleContainer.getResourceBundle().getString("applicationName"));
        stage.setResizable(false);
        stage.setAlwaysOnTop(true);
        stage.show();
        return stage;
    }


}
