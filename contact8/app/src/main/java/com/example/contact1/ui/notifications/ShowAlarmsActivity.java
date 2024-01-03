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
import java.util.Objects;
import java.util.Set;

public class ShowAlarmsActivity extends AppCompatActivity {

    private Set<String> alarmListSet;
    private ListView listViewAlarms;
    private ArrayAdapter<String> alarmsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_alarms);

        initViews();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        alarmListSet = preferences.getStringSet("alarmList", new HashSet<>());

        Intent intent = getIntent();
        if (intent.hasExtra("newAlarm")) {
            String newAlarm = Objects.requireNonNull(intent.getStringExtra("newAlarm"));
            int requestCode = extractRequestCode(newAlarm); // requestCode를 추출
            addAlarmToList(newAlarm, requestCode);
        }


        listViewAlarms.setOnItemClickListener((adapterView, view, position, id) -> cancelAlarmForListItem(position));

        handleAlarmRing();
        showAlarmsInListView();
    }

    private void initViews() {
        listViewAlarms = findViewById(R.id.listViewAlarms);
        alarmsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        listViewAlarms.setAdapter(alarmsAdapter);
    }

    private void showAlarmsInListView() {
        alarmsAdapter.clear();
        alarmsAdapter.addAll(alarmListSet);
        alarmsAdapter.notifyDataSetChanged();
    }

    private void handleAlarmRing() {
        Intent intent = getIntent();
        if (intent != null && intent.getBooleanExtra("alarmRinging", false)) {
            String ringingAlarm = intent.getStringExtra("ringingAlarm");
            removeAlarmFromList(ringingAlarm);
        }
    }

    private void removeAlarmFromList(String alarmToRemove) {
        if (alarmListSet != null) {
            alarmListSet.remove(alarmToRemove);
            saveAlarmListToPreferences();
            showAlarmsInListView();
        }
    }

    private void addAlarmToList(String newAlarm, int requestCode) {
        alarmListSet.add(newAlarm);
        saveAlarmListToPreferences();
        showAlarmsInListView();
    }

    private void cancelAlarmForListItem(int position) {
        String alarmInfo = alarmsAdapter.getItem(position);
        int requestCode = extractRequestCode(alarmInfo);

        Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), requestCode, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);

        String alarmToRemove = alarmsAdapter.getItem(position);
        alarmListSet.remove(alarmToRemove);

        saveAlarmListToPreferences();
        showAlarmsInListView();
    }
    private int extractRequestCode(String alarmInfo) {
        int openingBracketIndex = alarmInfo.lastIndexOf("(");
        int closingBracketIndex = alarmInfo.lastIndexOf(")");

        if (openingBracketIndex >= 0 && closingBracketIndex > openingBracketIndex) {
            String requestCodeString = alarmInfo.substring(openingBracketIndex + 1, closingBracketIndex);
            return Integer.parseInt(requestCodeString.trim());
        } else {
            // 괄호가 없거나 하나도 없을 경우 예외 처리 또는 기본값 반환
            return -1; // 또는 다른 적절한 기본값 설정
        }
    }

    private void saveAlarmListToPreferences() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putStringSet("alarmList", alarmListSet);
        editor.apply();
    }
}