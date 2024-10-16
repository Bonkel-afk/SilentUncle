package org.btc.com.silentuncle;

import javafx.application.Application;
import javafx.application.HostServices;
import javafx.application.Platform;
import javafx.stage.Stage;
import org.btc.com.silentuncle.events.StageReadyEvent;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.GenericApplicationContext;

public class JavaFxApplication extends Application {

    private ConfigurableApplicationContext context;

    @Override
    public void init() {

        ApplicationContextInitializer<GenericApplicationContext> initializer = applicationContext -> {
            applicationContext.registerBean(Application.class, () -> JavaFxApplication.this);
            applicationContext.registerBean(Parameters.class, this::getParameters);
            applicationContext.registerBean(HostServices.class, this::getHostServices);
        };

        this.context = new SpringApplicationBuilder().sources(SpringApplication.class).headless(false).initializers(initializer).run(getParameters().getRaw().toArray(new String[0]));
    }

    @Override
    public void start(Stage primaryStage) {
        this.context.publishEvent(new StageReadyEvent(primaryStage));

    }

    @Override
    public void stop() {
        this.context.close();
        Platform.exit();
    }
}
