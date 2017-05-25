package saurabhshrivas.csenitrr.com.intest;

import android.app.Application;

import com.firebase.client.Firebase;

/**
 * Created by sony on 24-05-2017.
 */
public class InTest extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
    }
}
