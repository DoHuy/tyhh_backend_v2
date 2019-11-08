package com.stadio.common.utils;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Andy on 01/20/2018.
 */
public class RandomUtils
{
    public static int number(int min, int max)
    {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }
}
