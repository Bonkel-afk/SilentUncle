package org.btc.com.silentuncle.controller;
/*
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.btc.com.silentuncle.events.StageReadyEvent;
import org.btc.com.silentuncle.view.TrayIconView;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.testfx.framework.junit5.ApplicationTest;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ApplicationControllerTest extends ApplicationTest {

    private final Stage stage = Mockito.mock(Stage.class);

    private final TrayIconView trayIconView = Mockito.mock(TrayIconView.class);

    private final ApplicationContext applicationContext = Mockito.mock(ApplicationContext.class);

    private final Resource fxml = Mockito.mock(Resource.class);


    private final URI uri = Paths.get("src/main/resources/main.fxml").toUri();
    private final ApplicationController applicationController = new ApplicationController("Test", applicationContext, trayIconView);


    @Test
    void onApplicationEvent() throws IOException {

        StageReadyEvent event = Mockito.mock(StageReadyEvent.class);
        when(event.getStage()).thenReturn(stage);
        when(this.fxml.getURL()).thenReturn(uri.toURL());

        Stage result = applicationController.onApplicationEvent(event);

        verify(trayIconView).setupTray(stage);
        verify(stage).setScene(any(Scene.class));
        verify(stage).setTitle(anyString());
        verify(stage).show();

        assertNotNull(result);
        assertEquals(stage, result);

    }


}
*/