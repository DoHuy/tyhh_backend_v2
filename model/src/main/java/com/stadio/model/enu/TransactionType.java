package com.stadio.model.enu;

/**
 * Created by Andy on 03/02/2018.
 */
public enum TransactionType
{
    //decrease balance
    FAST_PRACTICE(10),
    EXAM(11),
    COURSE(12),
    CATEGORY(13),

    //increase balance
    CARD(21),
    SMS(22),
    BANKING(23),
    CONFIG(24),
    RECHARGE_CARD(25);

    private final int code;

    private static TransactionType[] allValues = values();

    private TransactionType(int code) {
        this.code = code;
    }

    public int toInt() {
        return code;
    }

    public static TransactionType fromInt(Integer code) {
        return allValues[code];
    }
}
