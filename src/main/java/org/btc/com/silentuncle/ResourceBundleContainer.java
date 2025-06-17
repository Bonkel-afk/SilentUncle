package org.btc.com.silentuncle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.ResourceBundle;

@Component
public class ResourceBundleContainer {

    private final ResourceBundle resourceBundle;

    // Konstruktor-Injektion für bessere Testbarkeit und saubere Abhängigkeiten
    @Autowired
    public ResourceBundleContainer(ResourceBundle resourceBundle) {
        this.resourceBundle = Objects.requireNonNull(resourceBundle, "ResourceBundle darf nicht null sein");
    }

    public ResourceBundle getResourceBundle() {
        return resourceBundle;
    }
}
