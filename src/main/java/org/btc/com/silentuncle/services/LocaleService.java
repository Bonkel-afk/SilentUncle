package org.btc.com.silentuncle.services;

import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class LocaleService {
    @Getter
    private static Locale locale = Locale.ENGLISH;  // Standard Locale

    public void setLocale(Locale locale) {
        LocaleService.locale = locale;
    }

}
