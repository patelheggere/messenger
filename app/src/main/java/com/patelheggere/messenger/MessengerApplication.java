package com.patelheggere.messenger;

import android.app.Application;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class MessengerApplication extends Application {
    private static MessengerApplication mInstance;
    private static DatabaseReference databaseReference;
    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        FirebaseApp.initializeApp(this);
        if(FirebaseApp.getInstance()!=null){
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        }

        // ApiClient.intialise();
       /* if(isDeve()) {
            ApiClient.setBaseUrl(AppConstants.appBaseUrlDev);
        }
        else
        {
            ApiClient.setBaseUrl(AppConstants.appBaseUrl);

        }*/

    }
    public static synchronized DatabaseReference getFireBaseRef()
    {
        //FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        if(BuildConfig.DEBUG) {
            databaseReference = FirebaseDatabase.getInstance().getReference().child("test");
        }
        else {
            databaseReference = FirebaseDatabase.getInstance().getReference().child("prod");
        }
        return databaseReference;
    }

    public static synchronized MessengerApplication getInstance() {
        return mInstance;
    }

}