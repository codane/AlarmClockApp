package hr.danijelbabic.alarmclockapp.services;

import android.app.Service;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;

public class RingtoneService extends Service {

    private Ringtone ringtone;


    public RingtoneService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Uri alarmTone = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        ringtone = RingtoneManager.getRingtone(this, alarmTone);
        ringtone.play();

        // if service stops it will not automatically restart
        //ako servis završi, neće se automatski ponovno pokrenuti
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        ringtone.stop();

    }
}
