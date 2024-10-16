package org.btc.com.silentuncle.view;

import com.dustinredmond.fxtrayicon.FXTrayIcon;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;
import org.btc.com.silentuncle.controller.ApplicationController;
import org.btc.com.silentuncle.events.MainPageEvent;
import org.btc.com.silentuncle.events.TrayIconEvent;
import org.btc.com.silentuncle.services.LocaleService;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class TrayIconView {

    private FXTrayIcon trayIcon;
    private final MessageSource messageSource;
    private Stage stageInternal = null;

    public TrayIconView( MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public void setupTray(Stage stage) {
        stageInternal = stage;
        trayIcon = new FXTrayIcon(stage, Objects.requireNonNull(getClass().getResource("/silentunclelogo.png")));
        // Create menu items and add them to the trayIcon
        createAndAddMenuItems();

        trayIcon.setTooltip(messageSource.getMessage("applicationName", null, LocaleService.getLocale()));
        trayIcon.show();

        trayIcon.showInfoMessage(messageSource.getMessage("startTextTitle", null, LocaleService.getLocale()), messageSource.getMessage("startMessageText", null, LocaleService.getLocale()));
    }

    private void createAndAddMenuItems() {
        MenuItem titleItem= new MenuItem(messageSource.getMessage("applicationName", null, LocaleService.getLocale()));
        titleItem.setOnAction(e->ApplicationController.applicationContext.publishEvent(new MainPageEvent(stageInternal)));
        trayIcon.addMenuItem(titleItem);

        trayIcon.addSeparator();

        MenuItem fire = new MenuItem(messageSource.getMessage("fire", null, LocaleService.getLocale()));
        fire.setOnAction(e -> displayFireAlarm());
        trayIcon.addMenuItem(fire);

        MenuItem mobbing = new MenuItem(messageSource.getMessage("customer", null, LocaleService.getLocale()));
        mobbing.setOnAction(e -> displayBullyAlarm());
        trayIcon.addMenuItem(mobbing);

        MenuItem animal = new MenuItem(messageSource.getMessage("animal", null, LocaleService.getLocale()));
        animal.setOnAction(e -> displayAnimalIncidentAlarm());
        trayIcon.addMenuItem(animal);
        trayIcon.addSeparator();

        MenuItem settings = new MenuItem(messageSource.getMessage("settings", null, LocaleService.getLocale()));
        settings.setOnAction(e -> ApplicationController.applicationContext.publishEvent(new TrayIconEvent(stageInternal)));
        trayIcon.addMenuItem(settings);

        MenuItem exit = new MenuItem(messageSource.getMessage("quit", null, LocaleService.getLocale()));
        exit.setOnAction(e -> System.exit(0));
        trayIcon.addMenuItem(exit);
    }

    public void updateTrayIconTexts() {
        // Update the tooltip and menu items with the new texts
        trayIcon.setTooltip(messageSource.getMessage("applicationName", null, LocaleService.getLocale()));
        // Clear and re-add menu items
        trayIcon.clear();
        createAndAddMenuItems();
    }

    public void displayFireAlarm() {
        trayIcon.showErrorMessage("Feuer", "Feuer ist ausgebrochen. Sofort Raus!!!");
    }

    public void displayBullyAlarm() {
        trayIcon.showInfoMessage("Aggressiver Vorfall", "Ein Kunde verhält sich aggressiv am Empfang bitte um Hilfe");
    }

    public void displayAnimalIncidentAlarm() {
        trayIcon.showInfoMessage("Tier Alarm", "Tier Alarm im Behandlungsraum XY bitte um Unterstützung");
    }
}
