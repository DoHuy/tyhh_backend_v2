package com.stadio.model.enu;

public enum  TransactionApproveStatus {

    DENY(0),
    APPROVED(1),
    WAITING(2);

    private final int code;

    private static TransactionApproveStatus[] allValues = values();

    private TransactionApproveStatus(int code) {
        this.code = code;
    }

    public int toInt() {
        return code;
    }

    public static TransactionApproveStatus fromInt(Integer code) {
        return allValues[code];
    }

}
