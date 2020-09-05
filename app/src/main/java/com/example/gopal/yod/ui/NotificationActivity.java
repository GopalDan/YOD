package com.example.gopal.yod.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.gopal.yod.NotificationReceiver;
import com.example.gopal.yod.R;

import java.util.Calendar;

public class NotificationActivity extends AppCompatActivity {

    private static final String NOTIFICATION_ENABLED = "NOTIFICATION_ENABLED";

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        final Button button = findViewById(R.id.set_notification_button);
        final SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        final boolean isDailyNotificationEnabled = sharedPreferences.getBoolean(NOTIFICATION_ENABLED, false);
        setTitle("Word Reminder");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isDailyNotificationEnabled) {
                    Toast.makeText(NotificationActivity.this, "You will get one word daily!", Toast.LENGTH_SHORT).show();
                    notification();
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean(NOTIFICATION_ENABLED, true);
                    editor.apply();
                }else
                    Toast.makeText(NotificationActivity.this, "Already Enabled!", Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void notification(){

        AlarmManager alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        Intent notifyIntent = new Intent(this, NotificationReceiver.class);
//        notifyIntent.setFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        PendingIntent pendingIntent = PendingIntent.getBroadcast
                (this, 0, notifyIntent, 0);
        //            code for starting on specific time
        Calendar firingCal = setFiringTimeAndGetCalenderInstance(15, 38, 0);
        long intendedTime = firingCal.getTimeInMillis();

//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                // Wakes up the device in Doze Mode
//                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, intendedTime,
//                        1000 * 60 * 60 * 24, pendingIntent);
//            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                // Wakes up the device in Idle Mode
//                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, intendedTime,
//                        1000 * 60 * 60 * 24, pendingIntent);
//            } else {
//                // Old APIs
//                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, intendedTime,
//                        1000 * 60 * 60 * 24, pendingIntent);
//            }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Wakes up the device in Doze Mode
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC, intendedTime, pendingIntent);
//            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, intendedTime, AlarmManager.INTERVAL_DAY, pendingIntent);

        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // Wakes up the device in Idle Mode
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, intendedTime, pendingIntent);
//            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, intendedTime, AlarmManager.INTERVAL_DAY, pendingIntent);

        } else {
            // Old APIs
            alarmManager.set(AlarmManager.RTC, intendedTime, pendingIntent);
//            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, intendedTime, AlarmManager.INTERVAL_DAY, pendingIntent);

        }
    }

    public  Calendar setFiringTimeAndGetCalenderInstance(int hour, int min, int sec) {
        Calendar firingCal = Calendar.getInstance();
        firingCal.set(Calendar.HOUR_OF_DAY, hour); // At the hour you wanna fire
        firingCal.set(Calendar.MINUTE, min); // Particular minute
        firingCal.set(Calendar.SECOND, sec); // particular second
        return firingCal;
    }

}