package com.stadio.model.enu;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by Andy on 11/10/2017.
 */
public enum QuestionType
{
    THEORY,
    PRACTICE;

    private static final List<QuestionType> VALUES = Collections.unmodifiableList(Arrays.asList(values()));
    private static final int SIZE = VALUES.size();
    private static final Random RANDOM = new Random();

    public static QuestionType random()
    {
        return VALUES.get(RANDOM.nextInt(SIZE));
    }
}
