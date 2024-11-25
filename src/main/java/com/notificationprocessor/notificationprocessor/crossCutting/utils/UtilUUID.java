package com.notificationprocessor.notificationprocessor.crossCutting.utils;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public class UtilUUID {
   private final static String UUID_STRING = "ffffffff-ffff-ffff-ffff-ffffffffffff";
   private final static UUID UUID_DEFAULT_VALUE = UUID.fromString(UUID_STRING);


    private UtilUUID() {

    }

    public static String getUuidString() {
        return UUID_STRING;
    }

    public static  UUID getUuidDefaultValue() {
        return UUID_DEFAULT_VALUE;
    }

    public static UUID newUuid(JpaRepository repository){
        boolean alreadyExist;
        UUID nuevoUuid;
        do {
            nuevoUuid = UUID.randomUUID();
            if (repository.findById(nuevoUuid).isPresent()){
                alreadyExist = true;
            }else {
                alreadyExist = false;
            }
        }while (alreadyExist);
        return nuevoUuid;
    }
}
