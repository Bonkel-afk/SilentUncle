package org.btc.com.silentuncle.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.btc.com.silentuncle.events.MainPageEvent;
import org.btc.com.silentuncle.events.StageReadyEvent;
import org.btc.com.silentuncle.events.TrayIconEvent;
import org.btc.com.silentuncle.services.LocaleService;
import org.btc.com.silentuncle.view.TrayIconView;
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

    private final String applicationTitle;
    public static ApplicationContext applicationContext;
    private final TrayIconView trayIconView;
    private final LocaleService localeService;

    @FXML
    private Label label;
    @FXML
    private Button settingsButton;
    @FXML
    private ComboBox<String> languageComboBox;

    private final ResourceBundle resourceBundle;


    ApplicationController(ApplicationContext applicationContext, TrayIconView trayIconView, LocaleService localeService, @Qualifier("messageSource") MessageSource messageSource, ResourceBundle resourceBundle) {
        ApplicationController.applicationContext = applicationContext;
        this.trayIconView = trayIconView;
        this.localeService = localeService;
        this.resourceBundle = resourceBundle;
        this.applicationTitle = bundle.getString("applicationName");

        bundle = ResourceBundle.getBundle("i18n.message", LocaleService.getLocale());

    }
    @FXML
    public void initialize() {
        //noinspection StatementWithEmptyBody
        if(languageComboBox==null){
            //this
        }else{
        languageComboBox.getItems().addAll("Deutsch", "English");
        languageComboBox.setValue("English");  // Standardauswahl
        }

    }


    @EventListener
    public Stage onApplicationEvent(StageReadyEvent event) {
        try {
            Stage stage = event.getStage();
            trayIconView.setupTray(stage);
            FXMLLoader fxmlLoader = new FXMLLoader();       fxmlLoader.setLocation(ApplicationController.class.getResource("/main.fxml"));
            fxmlLoader.setResources(bundle);
            return getStage(stage, fxmlLoader);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }
    @EventListener
    public Stage onMainPageEvent(MainPageEvent event) {
        try {
            Stage stage = event.getStage();
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(TrayIconView.class.getResource("/main.fxml"));
            fxmlLoader.setResources(bundle);
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
            fxmlLoader.setResources(bundle);
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
                label.setText(bundle.getString("settingsLabel"));
            }
            if (settingsButton != null) {
                settingsButton.setText(bundle.getString("settingsButton"));
            }


    }

    private Stage getStage(Stage stage, FXMLLoader fxmlLoader) throws IOException {
        fxmlLoader.setControllerFactory(applicationContext::getBean);
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root, 600, 400);
        stage.setScene(scene);
        stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResource("/silentunclelogo.png")).toExternalForm()));
        stage.setTitle(this.applicationTitle);
        stage.setResizable(false);
        stage.setAlwaysOnTop(true);
        stage.show();
        return stage;
    }


}
