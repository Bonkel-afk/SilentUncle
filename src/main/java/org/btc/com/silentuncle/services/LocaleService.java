package org.btc.com.silentuncle.services;

import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class LocaleService {

    private final SettingsService settingsService;

    public LocaleService(SettingsService settingsService) {
        this.settingsService = settingsService;
        String lang = settingsService.getSetting("user.language", "en");
        locale = lang.equals("de") ? Locale.GERMAN : Locale.ENGLISH;
    }

    // Standard-Locale
    private static Locale locale = Locale.ENGLISH;

    // Getter für das Locale
    public static Locale getLocale() {
        return locale;
    }

    // Setter für das Locale
    public void setLocale(Locale locale) {
        if (locale == null) {
            throw new IllegalArgumentException("Locale darf nicht null sein");
        }
        LocaleService.locale = locale;
        settingsService.setSetting("user.language", locale.getLanguage());
    }
}
