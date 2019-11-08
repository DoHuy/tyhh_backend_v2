package com.stadio.cms.i18n;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

/**
 * Created by Andy on 11/10/2017.
 */
@Component
public class MessageLocaleService implements IMessageService
{
    @Autowired
    private MessageSource messageSource;

    private Locale locale = LocaleContextHolder.getLocale();

    @Override
    public String getMessage(String key) {
        return messageSource.getMessage(key,null, locale);
    }

    @Override
    public String getMessage(String key, List<String> agrs) {
        return messageSource.getMessage(key,agrs.toArray(),locale);
    }

    @Override
    public String getMessage(String key, String[] agrs) {
        return messageSource.getMessage(key,agrs,locale);
    }

}
