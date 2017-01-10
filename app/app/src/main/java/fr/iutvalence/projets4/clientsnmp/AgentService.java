package fr.iutvalence.projets4.clientsnmp;

/***
 Copyright (c) 2008-2012 CommonsWare, LLC
 Licensed under the Apache License, Version 2.0 (the "License"); you may not
 use this file except in compliance with the License. You may obtain	a copy
 of the License at http://www.apache.org/licenses/LICENSE-2.0. Unless required
 by applicable law or agreed to in writing, software distributed under the
 License is distributed on an "AS IS" BASIS,	WITHOUT	WARRANTIES OR CONDITIONS
 OF ANY KIND, either express or implied. See the License for the specific
 language governing permissions and limitations under the License.

 From _The Busy Coder's Guide to Android Development_
 http://commonsware.com/Android
 */
        import android.app.Notification;
        import android.app.NotificationManager;
        import android.app.PendingIntent;
        import android.app.Service;
        import android.content.Intent;
        import android.os.Build;
        import android.os.IBinder;
        import android.util.Log;

public class AgentService extends Service {
    public static final String EXTRA_PLAYLIST="EXTRA_PLAYLIST";
    public static final String EXTRA_SHUFFLE="EXTRA_SHUFFLE";
    private boolean isPlaying=false;

    private int NOTIFICATION_ID=11;

    Notification notification;
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String playlist=intent.getStringExtra(EXTRA_PLAYLIST);
        boolean useShuffle=intent.getBooleanExtra(EXTRA_SHUFFLE, false);

        play(playlist, useShuffle);

        return(START_NOT_STICKY);
    }

    @Override
    public void onDestroy() {
        stop();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return(null);
    }

    NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

    private void play(String playlist, boolean useShuffle) {
        if (!isPlaying) {
            Log.w(getClass().getName(), "Got to play()!");
            isPlaying=true;

            Intent i=new Intent(this, ServiceActivity.class);
            Notification.Builder builder = new Notification.Builder(this);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|
                    Intent.FLAG_ACTIVITY_SINGLE_TOP);

            PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, i, 0);

            builder.setAutoCancel(false);
            builder.setTicker("this is ticker text");
            builder.setContentTitle("Service is running");
            builder.setContentText("Click");
            builder.setSmallIcon(R.drawable.common_google_signin_btn_icon_dark_disabled); // ICONE DE NOTIF
            builder.setContentIntent(pendingIntent);
            builder.setOngoing(true);

            if (Build.VERSION.SDK_INT < 16) {
                notification = builder.getNotification();
            }else {
               notification = builder.build();
            }



            startForeground(NOTIFICATION_ID, notification);
        }
    }

    private void stop() {
        if (isPlaying) {
            Log.w(getClass().getName(), "Got to stop()!");
            isPlaying=false;
            stopForeground(true);
        }
    }
}