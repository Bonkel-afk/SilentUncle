package org.btc.com.silentuncle.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.btc.com.silentuncle.ResourceBundleContainer;
import org.btc.com.silentuncle.events.IncomingAlarmEvent;
import org.btc.com.silentuncle.events.MainPageEvent;
import org.btc.com.silentuncle.events.StageReadyEvent;
import org.btc.com.silentuncle.events.TrayIconEvent;
import org.btc.com.silentuncle.services.AlarmService;
import org.btc.com.silentuncle.services.LocaleService;
import org.btc.com.silentuncle.services.LogService;
import org.btc.com.silentuncle.view.TrayIconView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Locale;
import java.util.Objects;


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
    private ComboBox<String> roleComboBox;

    @FXML
    private VBox adminPasswordArea;
    @FXML
    private PasswordField adminPasswordField;
    @FXML
    private VBox adminLogArea;
    @FXML
    private TextArea debugTextArea;

    @FXML
    private Label userLabel;

    @FXML
    private Label chooseLanguageLabel;

    @FXML
    private Label chooseRoleLabel;

    @FXML
    private Label adminPasswordLabel;

    @FXML
    private Label alarmTypeLabel;
    @FXML
    private Label alarmMessageLabel;
    @FXML
    private Label alarmSenderLabel;
    @FXML
    private Label pcNameLabel;

    @FXML
    private Button settingsButton;

    @Autowired
    private ResourceBundleContainer resourceBundleContainer;
    @Autowired
    private LocaleService localeService;
    @Autowired
    private AlarmService alarmService;
    @Autowired
    private LogService logService;



    ApplicationController(ApplicationContext applicationContext, TrayIconView trayIconView) {
        ApplicationController.applicationContext = applicationContext;
        this.trayIconView = trayIconView;


    }

    @FXML
    public void initialize() {
        var bundle = resourceBundleContainer.getResourceBundle();
        if (languageComboBox != null) {
            languageComboBox.getItems().clear();
            languageComboBox.getItems().addAll("Deutsch", "English");
            if (LocaleService.getLocale().equals(Locale.GERMAN)) {
                languageComboBox.setValue("Deutsch");
            } else {
                languageComboBox.setValue("English");
            }
        }

        if (roleComboBox != null) {
            roleComboBox.getItems().clear();
            roleComboBox.getItems().addAll(
                    bundle.getString("role_doctor"),
                    bundle.getString("role_office"),
                    bundle.getString("role_admin")
            );

            String currentRole = alarmService.getUserRole();
            roleComboBox.setValue(bundle.getString("role_" + currentRole.toLowerCase()));
            
            roleComboBox.setOnAction(event -> {
                String selectedLocalizedRole = roleComboBox.getValue();
                if (bundle.getString("role_admin").equals(selectedLocalizedRole)) {
                    adminPasswordArea.setVisible(true);
                    adminPasswordArea.setManaged(true);
                } else {
                    adminPasswordArea.setVisible(false);
                    adminPasswordArea.setManaged(false);
                }
            });

            // Initialen Zustand setzen
            if ("ADMIN".equals(alarmService.getUserRole())) {
                if (adminPasswordArea != null) {
                    adminPasswordArea.setVisible(true);
                    adminPasswordArea.setManaged(true);
                }
            }
        }

        if (userLabel != null) {
            userLabel.setText(System.getProperty("user.name"));
        }

        if (adminLogArea != null) {
            boolean isAdmin = "ADMIN".equals(alarmService.getUserRole());
            adminLogArea.setVisible(isAdmin);
            adminLogArea.setManaged(isAdmin);
            if (isAdmin) {
                refreshLogs();
            }
        }
    }

    private void refreshLogs() {
        if (debugTextArea != null) {
            Platform.runLater(() -> {
                debugTextArea.clear();
                logService.readLogs().forEach(line -> debugTextArea.appendText(line + "\n"));
                debugTextArea.setScrollTop(Double.MAX_VALUE);
            });
        }
    }

    private void logToUI(String message) {
        logService.log(message);
        refreshLogs();
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
        var bundle = resourceBundleContainer.getResourceBundle();
        logToUI("Sende Tier-Alarm...");
        alarmService.broadcastAlarm("animal", bundle.getString("animalAlarmMessage"));
    }

    @FXML
    private void handleFeuerAlarm() {
        var bundle = resourceBundleContainer.getResourceBundle();
        logToUI("Sende Feuer-Alarm...");
        alarmService.broadcastAlarm("fire", bundle.getString("fireAlarmMessage"));
    }

    @FXML
    private void handleOpenSettings() {
        applicationContext.publishEvent(new TrayIconEvent((Stage) trayIconView.getStage()));
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
            return stage;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;

    }

    @FXML
    public void handleApplySettings() {
        System.out.println("[DEBUG] handleApplySettings aufgerufen");
        var bundle = resourceBundleContainer.getResourceBundle();
        
        String selectedLocalizedRole = roleComboBox.getValue();
        if (bundle.getString("role_admin").equals(selectedLocalizedRole)) {
            String password = adminPasswordField.getText();
            if (!"admin123".equals(password)) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle(bundle.getString("accessDenied"));
                alert.setHeaderText(bundle.getString("wrongPassword"));
                alert.setContentText(bundle.getString("passwordPrompt"));
                alert.showAndWait();
                return;
            }
        }

        changeLanguage();
        updateUserRole();
        // Kehre zur Homepage zurück
        applicationContext.publishEvent(new MainPageEvent((Stage) trayIconView.getStage()));
    }

    private void updateUserRole() {
        if (roleComboBox != null) {
            var bundle = resourceBundleContainer.getResourceBundle();
            String selectedLocalizedRole = roleComboBox.getValue();
            String roleToSet = "DOCTOR";
            if (bundle.getString("role_office").equals(selectedLocalizedRole)) {
                roleToSet = "OFFICE";
            } else if (bundle.getString("role_admin").equals(selectedLocalizedRole)) {
                roleToSet = "ADMIN";
            }
            
            System.out.println("[DEBUG] Setze Rolle auf: " + roleToSet);
            alarmService.setUserRole(roleToSet);
        }
    }

    private void changeLanguage() {
        if (languageComboBox == null) return;
        
        String selectedLanguage = languageComboBox.getValue();
        System.out.println("[DEBUG] Setze Sprache auf: " + selectedLanguage);

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
        var bundle = resourceBundleContainer.getResourceBundle();
        if (label != null) {
            label.setText(bundle.getString("settingsLabel"));
        }
        if (settingsButton != null) {
            settingsButton.setText(bundle.getString("settingsButton"));
        }
        if (chooseLanguageLabel != null) {
            chooseLanguageLabel.setText(bundle.getString("chooseLanguage"));
        }
        if (chooseRoleLabel != null) {
            chooseRoleLabel.setText(bundle.getString("chooseRole"));
        }
        if (adminPasswordLabel != null) {
            adminPasswordLabel.setText(bundle.getString("adminPasswordLabel"));
        }
    }

    @EventListener
    public void onIncomingAlarm(IncomingAlarmEvent event) {
        logToUI("Alarm empfangen: " + event.getType() + " von " + event.getSender() + " (" + event.getPcName() + ")");
        Platform.runLater(() -> {
            try {
                Stage stage = (Stage) trayIconView.getStage();
                if (stage == null) {
                    System.err.println("Stage ist null, kann Alarm nicht anzeigen.");
                    return;
                }
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/alarm_received.fxml"));
                fxmlLoader.setResources(resourceBundleContainer.getResourceBundle());
                fxmlLoader.setControllerFactory(applicationContext::getBean);
                
                Parent root = fxmlLoader.load();
                
                ApplicationController controller = fxmlLoader.getController();
                controller.setAlarmData(event.getType(), event.getMessage(), event.getSender(), event.getPcName(), event.getSenderRole());

                Scene scene = new Scene(root, 1280, 720);
                stage.setScene(scene);
                stage.setAlwaysOnTop(true);
                stage.show();
                stage.setIconified(false);
                stage.toFront();
                stage.requestFocus();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void setAlarmData(String type, String message, String sender, String pcName, String senderRole) {
        var bundle = resourceBundleContainer.getResourceBundle();
        if (alarmTypeLabel != null) {
            String titleKey = type.toLowerCase() + "AlarmTitle";
            try {
                alarmTypeLabel.setText(bundle.getString(titleKey));
            } catch (Exception e) {
                alarmTypeLabel.setText(type.toUpperCase() + " ALARM");
            }
        }
        if (alarmMessageLabel != null) {
             String localizedRole = "";
             try {
                 localizedRole = bundle.getString("role_" + senderRole.toLowerCase());
             } catch (Exception e) {
                 localizedRole = senderRole;
             }
             String roleSuffix = senderRole != null ? " (" + localizedRole + ")" : "";
             alarmMessageLabel.setText(message + roleSuffix);
        }
        if (alarmSenderLabel != null) alarmSenderLabel.setText(sender);
        if (pcNameLabel != null) pcNameLabel.setText(pcName);
    }

    @FXML
    private void handleAlarmAck() {
        applicationContext.publishEvent(new MainPageEvent((Stage) trayIconView.getStage()));
    }

    private Stage getStage(Stage stage, FXMLLoader fxmlLoader) throws IOException {
        fxmlLoader.setControllerFactory(applicationContext::getBean);
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root, 1280, 720);
        stage.setScene(scene);
        stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResource("/silentunclelogo.png")).toExternalForm()));
        stage.setTitle(resourceBundleContainer.getResourceBundle().getString("applicationName"));
        stage.setResizable(false);
        stage.setAlwaysOnTop(true);
        stage.show();
        return stage;
    }

}
