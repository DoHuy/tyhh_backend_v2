package com.stadio.mediation.i18n;

import java.util.List;

/**
 * Created by Andy on 11/10/2017.
 */
public interface IMessageService
{
    String getMessage(String key);

    String getMessage(String key, List<String> agrs);

    String getMessage(String key, String[] agrs);
}
