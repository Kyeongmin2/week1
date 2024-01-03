package com.example.contact1.ui.notifications;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.contact1.databinding.FragmentAlarmBinding;
import com.example.contact1.ui.notifications.AlarmReceiver;
import com.example.contact1.MainActivity;
import com.example.contact1.databinding.FragmentNotificationsBinding;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.util.Calendar;

public class AlarmFragment extends Fragment {

    private FragmentAlarmBinding binding;

    private MaterialTimePicker timePicker;
    private Calendar calendar;
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAlarmBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        createNotificationChannel();

        binding.selectTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timePicker = new MaterialTimePicker.Builder()
                        .setTimeFormat(TimeFormat.CLOCK_12H)
                        .setHour(12)
                        .setMinute(0)
                        .setTitleText("시간을 고르시요")
                        .build();
                timePicker.show(getActivity().getSupportFragmentManager(), "androidknowledge");
                timePicker.addOnPositiveButtonClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (timePicker.getHour() > 12) {
                            binding.selectTime.setText(String.format("%02d", timePicker.getHour() - 12) + ":" + String.format("%02d", timePicker.getMinute()) + "PM");
                        } else {
                            binding.selectTime.setText(timePicker.getHour() + ":" + String.format("%02d", timePicker.getMinute()) + "AM");
                        }
                        calendar = Calendar.getInstance();
                        calendar.set(Calendar.HOUR_OF_DAY, timePicker.getHour());
                        calendar.set(Calendar.MINUTE, timePicker.getMinute());
                        calendar.set(Calendar.SECOND, 0);
                        calendar.set(Calendar.MILLISECOND, 0);
                    }
                });
            }
        });

        binding.setAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 시간이 설정되지 않은 경우 메시지 표시
                if (calendar == null) {
                    Toast.makeText(getActivity(), "시간을 설정해 주세요", Toast.LENGTH_SHORT).show();
                    return;
                }

                alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
                Intent intent = new Intent(getActivity(), AlarmReceiver.class);
                pendingIntent = PendingIntent.getBroadcast(getActivity(), 0, intent, 0);

                if (calendar.getTimeInMillis() <= System.currentTimeMillis()) {
                    calendar.add(Calendar.DAY_OF_YEAR, 1);
                }

                // setInexactRepeating 대신 setExact 사용
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                } else {
                    alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                }

                // 알람 목록에 추가
                Intent showAlarmsIntent = new Intent(getActivity(), ShowAlarmsActivity.class);

                // 여기에 알람 시간 등의 정보를 전달
                String alarmTimeInfo = String.format("%02d:%02d", timePicker.getHour(), timePicker.getMinute());
                showAlarmsIntent.putExtra("newAlarm", alarmTimeInfo);

                startActivity(showAlarmsIntent);

                Toast.makeText(getActivity(), "알람이 설정되었습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        binding.showAlarmsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 알람 목록을 보여주는 액티비티 호출
                Intent intent = new Intent(getActivity(), ShowAlarmsActivity.class);
                startActivity(intent);
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "akchannel";
            String desc = "Channel for Alarm Manager";
            int imp = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("androidknowledge", name, imp);
            channel.setDescription(desc);

            NotificationManager notificationManager = getActivity().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

}