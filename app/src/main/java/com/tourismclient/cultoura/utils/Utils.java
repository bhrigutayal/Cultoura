package com.tourismclient.cultoura.utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.annotation.NonNull;

public class Utils {

    private static Utils utils;

    private Utils() {
    }

    public static Utils getInstance() {
        if (utils == null) {
            utils = new Utils();
        }
        return utils;
    }

    public boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }








    public boolean isEmpty(String value) {
        //TODO: Replace this with your own logic
        return value.equals("");
    }
    public static void triggerRebirth(Context context) {
        PackageManager packageManager = context.getPackageManager();
        Intent intent = packageManager.getLaunchIntentForPackage(context.getPackageName());
        ComponentName componentName = intent.getComponent();
        Intent mainIntent = Intent.makeRestartActivityTask(componentName);
        context.startActivity(mainIntent);
        Runtime.getRuntime().exit(0);
    }

    public void logout(Context context) {
        //TODO implement this function
    }
}
