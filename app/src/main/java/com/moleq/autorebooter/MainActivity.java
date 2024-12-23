package com.moleq.autorebooter;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

/**
 * @author jzheng
 * @date 2024/12/18
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnReboot = findViewById(R.id.btn_open_as_setting);
        btnReboot.setOnClickListener(v -> {
            Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
            startActivity(intent);
        });

        Button btnSetRootTime = findViewById(R.id.btn_set_reboot_time);
        btnSetRootTime.setOnClickListener(v -> setRebootTime());
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void setRebootTime() {
        TimePicker timePicker = findViewById(R.id.tp_time);

        Calendar rebootTime = Calendar.getInstance();
        rebootTime.set(Calendar.HOUR_OF_DAY, timePicker.getHour());
        rebootTime.set(Calendar.MINUTE, timePicker.getMinute());
        rebootTime.set(Calendar.SECOND, 0);

        if (rebootTime.before(Calendar.getInstance())) {
            rebootTime.add(Calendar.DATE, 1);
        }

        Intent intent = new Intent(this, RequestRebootReceiver.class);
        PendingIntent sender;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            sender = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT | PendingIntent.FLAG_MUTABLE);
        } else {
            sender = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        }

        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        am.set(AlarmManager.RTC_WAKEUP, rebootTime.getTimeInMillis(), sender);
        Toast.makeText(this, "Set Reboot Time Success.", Toast.LENGTH_LONG).show();
    }

}