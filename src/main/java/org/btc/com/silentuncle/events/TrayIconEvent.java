package org.btc.com.silentuncle.events;

import javafx.stage.Stage;
import org.springframework.context.ApplicationEvent;

public class TrayIconEvent extends ApplicationEvent {
    public TrayIconEvent(Stage stage) {
        super(stage);
    }
    public Stage getStage() {
        return ((Stage) getSource());
    }
}
