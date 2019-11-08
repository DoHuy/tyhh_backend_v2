package com.stadio.model.enu;


/**
 * Created by Andy on 02/15/2018.
 */
public enum MobileScreen
{
    HOME,
    EXAM_LIST,
    PROFILE,
    MESSAGE,
    EXAM_DETAILS,
    EXAM_ONLINE_REMIND,
    TABLE_POINT,
    EXAM_ONLINE_WAITING;

    public static boolean equals(String vk)
    {
        for (MobileScreen mk: values())
        {
            if (mk.name().equals(vk))
            {
                return true;
            }
        }
        return false;
    }

    public static MobileScreen find(String vk)
    {
        for (MobileScreen mk: values())
        {
            if (mk.name().equals(vk))
            {
                return mk;
            }
        }
        return null;
    }
}

