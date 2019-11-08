package com.stadio.common.enu;

/**
 * Created by Andy on 02/11/2018.
 */
public enum FolderName
{
    IMAGES("images"),
    DOCUMENTS("documents"),
    USERS("users"),
    LOGS("logs");

    private String value;

    FolderName(String value)
    {
        this.value = value;
    }


}
