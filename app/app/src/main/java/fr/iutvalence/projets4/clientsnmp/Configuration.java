package fr.iutvalence.projets4.clientsnmp;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by ethis on 12/01/17.
 */

public class Configuration {

    private static Context context = null;

    public static void initConfiguration(Context context)
    {
        Configuration.context = context;
    }

    public static Context getContext() {
        return Configuration.context;
    }

    public static String getConfigValue(String name) {
        if(context == null)
        {
            Log.e("CONFIG", "Please init the configuration class with the context !");
        }
        else {
            Resources resources = context.getResources();

            try {
                InputStream rawResource = resources.openRawResource(R.raw.config);
                Properties properties = new Properties();
                properties.load(rawResource);
                return properties.getProperty(name);
            } catch (IOException e) {
                Log.e("CONFIG", "Failed to open config file.");
            }
        }
        return null;
    }
}
