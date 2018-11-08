package hr.danijelbabic.alarmclockapp.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import hr.danijelbabic.alarmclockapp.services.RingtoneService;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        //creating an intent to the RingtoneService
        //kreiranje intenta za RingtoneService
        Intent service1 = new Intent(context, RingtoneService.class);

        //starting the RingtoneService
        //započinjanje RingtoneService-a
        context.startService(service1);

    }
}
