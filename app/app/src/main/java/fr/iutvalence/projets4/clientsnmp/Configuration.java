package fr.iutvalence.projets4.clientsnmp;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Class who represents the service's configuration
 * Created by ethis on 12/01/17.
 */
public class Configuration {

    /**
     * The service's context (used to calculate some values into the MIB)
     */
    private static Context context = null;

    /**
     * Set the the service's context into the Configuration class
     * @param context of the application (for example "this" into MainActivity)
     */
    public static void setContext(Context context)
    {
        Configuration.context = context;
    }

    /**
     * Get the service's context
     * @return the Context of the service
     */
    public static Context getContext() {
        return Configuration.context;
    }

    /**
     * Get the value corresponding to the given name in the configuration file (/res/raw/config.properties)
     * @param name the String corresponding to the name of the configuration data needed
     * @return the value (String) corresponding to the given name
     */
    public static String getConfigValue(String name) {
        if(context == null)
        {
            Log.e("CONFIG", "Please set the service's context to the configuration !");
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
