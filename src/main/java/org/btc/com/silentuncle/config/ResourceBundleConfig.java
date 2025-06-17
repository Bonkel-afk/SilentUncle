package org.btc.com.silentuncle.config;

import org.btc.com.silentuncle.services.LocaleService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ResourceBundle;

@Configuration
public class ResourceBundleConfig {

    @Bean
    public ResourceBundle resourceBundle(){
        return ResourceBundle.getBundle("i18n.message", LocaleService.getLocale());
    }
}
