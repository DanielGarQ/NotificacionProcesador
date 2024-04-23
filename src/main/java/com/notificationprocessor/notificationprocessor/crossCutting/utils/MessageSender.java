package com.notificationprocessor.notificationprocessor.crossCutting.utils;

public interface MessageSender<T>{
    void execute(T message, String idMessage);
}
