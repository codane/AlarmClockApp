package hr.danijelbabic.alarmclockapp;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

import hr.danijelbabic.alarmclockapp.receivers.AlarmReceiver;
import hr.danijelbabic.alarmclockapp.services.RingtoneService;

public class MainActivity extends AppCompatActivity {


    private TimePicker alarmTimePicker;
    private AlarmManager alarmManager;
    private Button btnSet;
    private Button btnStop;
    private TextView tvAlarm;
    private Intent alarmIntent;
    private PendingIntent pendingIntent;
    private NotificationCompat.Builder notification;
    private NotificationManager myNotificationManager;
    private static final int ID = 2422;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initAlarm();
        initWidgets();
        setupListeners();

        //getting preferences/ dohvaćanje postavki
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        //storing boolean from SharedPreferences into a boolean variable
        boolean cbTimeFormat = sharedPreferences.getBoolean("time_format", false);

        // we set the 24 hour time format if it is selected in the menu
        if (cbTimeFormat == true) {
            alarmTimePicker.setIs24HourView(true);
        }

        notification = new NotificationCompat.Builder(this);
        notification.setAutoCancel(true);

    }

    public void initAlarm() {
        alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);

        //creating an intent to the AlarmReceiver
        //kreiranje intenta za AlarmReceiver
        alarmIntent = new Intent(this, AlarmReceiver.class);

        //creating a pending intent which delays the intent until the seleced time
        //kreiranje pending intenta koji odgađa intent do odabranog vremena
        pendingIntent = PendingIntent.getBroadcast(MainActivity.this , 0,
                alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private void initWidgets() {
        alarmTimePicker = (TimePicker)findViewById(R.id.timePicker);
        tvAlarm = (TextView) findViewById(R.id.tvAlarm);
        btnSet = (Button) findViewById(R.id.btnSet);
        btnStop = (Button) findViewById(R.id.btnStop);
    }

    private void setupListeners() {
        btnSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //creating an instance of the calendar/ kreiranje instance kalendara
                //setting the calendar instance with the time selected on TimePicker
                //postavljanje instance kalendara na vrijeme odabrano na TimePicker-u
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, alarmTimePicker.getCurrentHour());
                calendar.set(Calendar.MINUTE, alarmTimePicker.getCurrentMinute());

                //getting int values of selected hour and minute
                //pretvaranje odabranih sati i minuta u int vrijednosti
                int hourSelected = alarmTimePicker.getCurrentHour();
                int minuteSelected = alarmTimePicker.getCurrentMinute();

                //converting int values into strings
                //pretvaranje int vrjednosti u stringove
                String hourSelectedString = String.valueOf(hourSelected);
                String minuteSelectedString = String.valueOf(minuteSelected);

                //checking if minutes selected are less than 10; if so adding 0
                //provjera jesu li odabrane minute manje od 10; ako da, dodavanje 0
                if (minuteSelected < 10) {
                    minuteSelectedString = "0" + minuteSelectedString;
                }

                //checing if selected time is less than the current time; if is, adding 24 hours
                //provjera je li odabrano vrijeme manje od trenutnog; ako je, dodavanje 24 sata
                long time = calendar.getTimeInMillis();
                if (time < System.currentTimeMillis()){
                    time = time + (1000 * 60 * 60 * 24);
                }

                tvAlarm.setText("Alarm set on: " + hourSelectedString + ":"
                        + minuteSelectedString);

                //setting the alarm manager
                //postavljanje alarm managera
                alarmManager.set(AlarmManager.RTC_WAKEUP, time, pendingIntent);

                //creating the notification/ kreiranje notifikacije
                Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
                PendingIntent myPendingIntent = PendingIntent.getActivity(getApplicationContext(), 0,
                        myIntent, PendingIntent.FLAG_UPDATE_CURRENT);


                notification.setSmallIcon(R.drawable.ic_alarm_black_24dp);
                notification.setContentTitle("Alarm!!");
                notification.setTicker("Alarm!!");
                notification.setContentText(hourSelectedString + ":" + minuteSelectedString);
                notification.setContentIntent(myPendingIntent);

                myNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                myNotificationManager.notify(ID, notification.build());


            }
        });
        btnStop = (Button) findViewById(R.id.btnStop);
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvAlarm.setText("Alarm stopped!");

                //creating an intent to the RingtoneService and stopping that service
                //kreiranje intenta za RingtoneService i zaustavljanje tog servisa
                Intent i = new Intent(MainActivity.this, RingtoneService.class);
                stopService(i);

                alarmIntent = new Intent(MainActivity.this, AlarmReceiver.class);

                pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, alarmIntent, 0);
                alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

                //cancel the alarm manager
                //otkazivanje alarm managera
                alarmManager.cancel(pendingIntent);
                myNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                myNotificationManager.cancel(ID);

            }
        });
    }

    //creating menu
    //kreiranje menu-a
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.settings) {
            Intent i = new Intent(getApplicationContext(), MyPreferenceActivity.class);
            startActivity(i);

            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
