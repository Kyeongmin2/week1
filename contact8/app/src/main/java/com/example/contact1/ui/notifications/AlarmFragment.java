package com.example.contact1.ui.notifications;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.contact1.R;
import com.example.contact1.databinding.FragmentAlarmBinding;
import com.example.contact1.ui.notifications.AlarmReceiver;
import com.example.contact1.MainActivity;
import com.example.contact1.databinding.FragmentNotificationsBinding;
import com.example.contact1.ui.notifications.ShowAlarmsActivity;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.util.Calendar;

public class AlarmFragment extends Fragment {

    private FragmentAlarmBinding binding;
    private MaterialTimePicker timePicker;
    private Calendar calendar;
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    private int alarmRequestCode = 0;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentAlarmBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        createNotificationChannel();

        binding.selectTime.setOnClickListener(view -> showTimePicker());

        binding.setAlarm.setOnClickListener(view -> setAlarm());

        binding.setAlarm.setOnClickListener(view -> setAlarm());

        Button showAlarmsButton = root.findViewById(R.id.showAlarmsButton);
        showAlarmsButton.setOnClickListener(view -> showAlarms());

        return root;

    }


    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "akchannel";
            String desc = "Channel for Alarm Manager";
            int imp = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("androidknowledge", name, imp);
            channel.setDescription(desc);

            NotificationManager notificationManager = requireActivity().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void showTimePicker() {
        timePicker = new MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_12H)
                .setHour(12)
                .setMinute(0)
                .setTitleText("시간을 고르시요")
                .build();

        timePicker.show(requireActivity().getSupportFragmentManager(), "androidknowledge");
        timePicker.addOnPositiveButtonClickListener(view -> updateSelectedTime());
    }

    private void updateSelectedTime() {
        if (timePicker.getHour() > 12) {
            binding.selectTime.setText(String.format("%02d", timePicker.getHour() - 12) + ":" +
                    String.format("%02d", timePicker.getMinute()) + "PM");
        } else {
            binding.selectTime.setText(timePicker.getHour() + ":" + String.format("%02d", timePicker.getMinute()) + "AM");
        }

        calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, timePicker.getHour());
        calendar.set(Calendar.MINUTE, timePicker.getMinute());
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
    }

    private void setAlarm() {
        if (calendar == null) {
            Toast.makeText(requireActivity(), "시간을 설정해 주세요", Toast.LENGTH_SHORT).show();
            return;
        }

        alarmManager = (AlarmManager) requireActivity().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(requireActivity(), AlarmReceiver.class);
        alarmRequestCode++;

        // 유일한 requestCode를 생성하기 위해 현재 시간을 추가
        alarmRequestCode += System.currentTimeMillis() % 1000;

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        intent.putExtra("alarmSound", alarmSound.toString());

        pendingIntent = PendingIntent.getBroadcast(
                requireActivity(),
                alarmRequestCode,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        if (calendar.getTimeInMillis() <= System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }

        int alarmType = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) ?
                AlarmManager.RTC_WAKEUP : AlarmManager.RTC;
        alarmManager.setExact(alarmType, calendar.getTimeInMillis(), pendingIntent);

        // 알람 목록에 추가
        Intent showAlarmsIntent = new Intent(requireActivity(), ShowAlarmsActivity.class);
        String alarmTimeInfo = String.format("%02d:%02d", timePicker.getHour(), timePicker.getMinute());
        showAlarmsIntent.putExtra("newAlarm", alarmTimeInfo);
        startActivity(showAlarmsIntent);

        Toast.makeText(requireActivity(), "알람이 설정되었습니다.", Toast.LENGTH_SHORT).show();
    }

    private void showAlarms() {
        Intent intent = new Intent(requireActivity(), ShowAlarmsActivity.class);
        startActivity(intent);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}