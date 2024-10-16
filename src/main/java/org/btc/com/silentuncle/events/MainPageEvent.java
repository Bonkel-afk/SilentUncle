package org.btc.com.silentuncle.events;

import javafx.stage.Stage;
import org.springframework.context.ApplicationEvent;

public class MainPageEvent extends ApplicationEvent {
    public MainPageEvent(Stage stage) {
        super(stage);
    }
    public Stage getStage() {
        return ((Stage) getSource());
    }
}
