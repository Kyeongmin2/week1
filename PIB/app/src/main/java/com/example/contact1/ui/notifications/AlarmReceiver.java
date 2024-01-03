package com.example.contact1.ui.notifications;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.preference.PreferenceManager;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.contact1.R;

import java.util.HashSet;
import java.util.Set;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        String alarmSoundUri = intent.getStringExtra("alarmSound");
        if (alarmSoundUri != null) {
            Ringtone ringtone = RingtoneManager.getRingtone(context, Uri.parse(alarmSoundUri));
            ringtone.play();
        }
        String ringingAlarm = intent.getStringExtra("ringingAlarm");

        // 알림 생성 및 표시
        showNotification(context);

        // 알람 목록에서 해당 알람 삭제
        removeAlarmFromList(context, ringingAlarm);
    }

    private void removeAlarmFromList(Context context, String alarmToRemove) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        Set<String> alarmListSet = preferences.getStringSet("alarmList", new HashSet<>());

        if (alarmListSet != null) {
            // 알람 목록에서 해당 알람 제거
            alarmListSet.remove(alarmToRemove);

            // 변경된 알람 목록 저장
            saveAlarmListToPreferences(context, alarmListSet);

        }
    }

    private void saveAlarmListToPreferences(Context context, Set<String> alarmListSet) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putStringSet("alarmList", alarmListSet);
        editor.apply();
    }

    private void showNotification(Context context) {
        // 알림 생성 및 표시
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "androidknowledge")
                .setSmallIcon(R.drawable.ic_notifications_black_24dp)
                .setContentTitle("Reminder")
                .setContentText("일어날 시간")
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        Intent nextActivity = new Intent(context, AlarmActivity.class);
        nextActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, nextActivity, 0);
        builder.setContentIntent(pendingIntent);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(123, builder.build());
    }
}