package com.mobioetech.keralathodoppam.keralathodoppam;

import com.google.firebase.database.FirebaseDatabase;


public class KeralathodoppamDBUtil {

    private static FirebaseDatabase KeralathodoppamDBInstance ;

    public static FirebaseDatabase getInstance()
    {
        if(KeralathodoppamDBInstance == null)
        {
            KeralathodoppamDBInstance = FirebaseDatabase.getInstance();
            KeralathodoppamDBInstance.setPersistenceEnabled(true);
        }
        return KeralathodoppamDBInstance;
    }

    private KeralathodoppamDBUtil() {
    }
}
