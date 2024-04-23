package com.notificationprocessor.notificationprocessor.crossCutting.utils;

import java.util.UUID;

public class UtilUUID {
   private final static String uuidString = "ffffffff-ffff-ffff-ffff-ffffffffffff";
   private final static UUID uuidDefaultValue = UUID.fromString(uuidString);


    private UtilUUID() {

    }

    public static String getUuidString() {
        return uuidString;
    }

    public static  UUID getUuidDefaultValue() {
        return uuidDefaultValue;
    }
}
