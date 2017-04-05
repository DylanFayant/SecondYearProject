package fr.iutvalence.projets4.clientsnmp.AndroidDebugInterface;

import android.app.ActivityManager;

import com.jaredrummler.android.processes.AndroidProcesses;
import com.jaredrummler.android.processes.models.AndroidAppProcess;
import java.util.Arrays;
import java.util.List;

import fr.iutvalence.projets4.clientsnmp.Configuration;

/**
 * This class get service to monitor in the file /app-app/res/raw/config.properties
 * and return the status of this service and them in a table of string
 * Created by Thundermist on 13/01/17.
 */

public class ListServiceToSurvey  {

    //List<AndroidAppProcess> processForeground = AndroidProcesses.getRunningForegroundApps(Configuration.getContext());
    List<ActivityManager.RunningAppProcessInfo> processes = AndroidProcesses.getRunningAppProcessInfo(Configuration.getContext());
    String servicesToWatch = Configuration.getConfigValue("srvc_must_be_open");
    List<String> listOfProcessesToWatch = Arrays.asList(servicesToWatch.split(";"));

    /**
     * Transform "listOfProcessesToWatch" (List<String>) in String[]
     * @return Table of string who contain service to monitor
     */
    int i;
    public String[] getListOfProcessToSurvey()
    {
        i=0;
        String[] ListOfProcessToWatch = new String[listOfProcessesToWatch.size()];
        for(String aService : listOfProcessesToWatch) {
            ListOfProcessToWatch[i] = String.valueOf(aService);
            i++;
        }
        return ListOfProcessToWatch;
    }

    /**
     * Check if all services to monitor was open or not
     * @return Table of String who indicate if service was open or not
     */
    public String[] getProcessStatus()
    {
        i=0;
        String test;
        String[] ListStatusProcessToWatch = new String[listOfProcessesToWatch.size()];
        for(String aService : listOfProcessesToWatch) {
            test = "Stop";
            for (ActivityManager.RunningAppProcessInfo aProcess : processes)
            {
                if (aProcess.processName.equals(aService)) {
                    test = "Launch";
                }
            }
            ListStatusProcessToWatch[i] = test;
            i++;
        }
        return ListStatusProcessToWatch;
    }
}
