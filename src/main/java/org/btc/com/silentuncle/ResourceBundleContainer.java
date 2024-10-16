package org.btc.com.silentuncle;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ResourceBundle;
@Component
public class ResourceBundleContainer {
    @Getter
    @Setter
    @Autowired
    private ResourceBundle resourceBundle;
    public ResourceBundleContainer( ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
    }
}
