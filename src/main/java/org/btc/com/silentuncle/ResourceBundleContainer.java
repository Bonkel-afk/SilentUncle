package org.btc.com.silentuncle;

import org.btc.com.silentuncle.services.LocaleService;
import org.springframework.stereotype.Component;

import java.util.ResourceBundle;

@Component
public class ResourceBundleContainer {

    public ResourceBundle getResourceBundle() {
        return ResourceBundle.getBundle("i18n.message", LocaleService.getLocale());
    }
}
