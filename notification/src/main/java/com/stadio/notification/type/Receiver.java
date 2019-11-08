package com.stadio.notification.type;

public interface Receiver
{
    void receiveMessage(String message) throws Exception;
}