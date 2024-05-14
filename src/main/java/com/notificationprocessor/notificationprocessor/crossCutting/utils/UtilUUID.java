package com.notificationprocessor.notificationprocessor.crossCutting.utils;

import org.springframework.data.jpa.repository.JpaRepository;

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
