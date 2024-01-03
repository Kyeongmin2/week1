package com.example.contact1.ui.notifications;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.contact1.R;

import java.util.HashSet;
import java.util.Set;

public class ShowAlarmsActivity extends AppCompatActivity {

    private Set<String> alarmListSet; // 알람 목록을 저장하는 Set

    private ListView listViewAlarms;
    private ArrayAdapter<String> alarmsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_alarms);

        listViewAlarms = findViewById(R.id.listViewAlarms);
        alarmsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        listViewAlarms.setAdapter(alarmsAdapter);


        // SharedPreferences에서 알람 목록을 불러오기
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        alarmListSet = preferences.getStringSet("alarmList", new HashSet<>());

        // Intent에서 새로운 알람 정보 받아와서 ListView에 추가
        Intent intent = getIntent();
        if (intent.hasExtra("newAlarm")) {
            String newAlarmInfo = intent.getStringExtra("newAlarm");
            addAlarmToList(newAlarmInfo);
        }
        listViewAlarms.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                // 클릭한 항목의 알람 취소 버튼을 처리
                cancelAlarmForListItem(position);
            }
        });

        // 알람이 울렸을 때의 동작 설정
        handleAlarmRing();

        // 초기 알람 목록 표시
        showAlarmsInListView();
    }

    // 알람 목록을 새로고침하는 메서드
    private void clearAlarmList() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove("alarmList");
        editor.putBoolean("isFirstRun", false);
        editor.apply();
    }

    private void showAlarmsInListView() {
        alarmsAdapter.clear(); // 기존 데이터 지우지 않고 새로운 알람만 추가
        alarmsAdapter.addAll(alarmListSet);
        alarmsAdapter.notifyDataSetChanged(); // 변경 사항 알림
    }

    private void handleAlarmRing() {
        // AlarmReceiver에서 전달한 알람이 울렸는지 확인
        Intent intent = getIntent();
        if (intent != null && intent.getBooleanExtra("alarmRinging", false)) {
            // 알람이 울렸을 때의 동작 수행
            String ringingAlarm = intent.getStringExtra("ringingAlarm");
            removeAlarmFromList(ringingAlarm);
        }
    }

    private void removeAlarmFromList(String alarmToRemove) {
        if (alarmListSet != null) {  // null 체크 추가
            alarmListSet.remove(alarmToRemove);

            // SharedPreferences에 알람 목록 저장
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            SharedPreferences.Editor editor = preferences.edit();
            editor.putStringSet("alarmList", alarmListSet);
            editor.apply();

            // 알람 목록 갱신
            showAlarmsInListView();
        }
    }
    private void addAlarmToList(String newAlarm) {
        alarmListSet.add(newAlarm);

        // SharedPreferences에 알람 목록 저장
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putStringSet("alarmList", alarmListSet);
        editor.apply();

        // 알람 목록 갱신
        showAlarmsInListView();
    }
    public void cancelAlarmForListItem(int position) {
        Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), position, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);

        String alarmToRemove = alarmsAdapter.getItem(position);

        // 알람 목록에서 해당 알람 제거
        alarmListSet.remove(alarmToRemove);

        // SharedPreferences에 알람 목록 저장
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putStringSet("alarmList", alarmListSet);
        editor.apply();

        // 알람 목록 갱신
        showAlarmsInListView();
    }
}