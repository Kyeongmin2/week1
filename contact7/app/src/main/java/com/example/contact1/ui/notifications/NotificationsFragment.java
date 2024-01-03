package com.example.contact1.ui.notifications;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.fragment.app.FragmentTransaction;

import com.example.contact1.R;

public class NotificationsFragment extends Fragment {

    private AlarmFragment alarmFragment;
    private TimerFragment timerFragment;
    private StopwatchFragment stopwatchFragment;

    public NotificationsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notifications, container, false);

        Button alarmButton = view.findViewById(R.id.btnAlarm);
        Button timerButton = view.findViewById(R.id.btnTimer);
        Button stopwatchButton = view.findViewById(R.id.btnStopwatch);

        alarmFragment = new AlarmFragment();
        timerFragment = new TimerFragment();
        stopwatchFragment = new StopwatchFragment();

        // 알람 버튼을 누르면 AlarmFragment로 교체
        alarmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(alarmFragment);
            }
        });

        // 타이머 버튼을 누르면 TimerFragment로 교체
        timerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(timerFragment);
            }
        });

        // 스톱워치 버튼을 누르면 StopwatchFragment로 교체
        stopwatchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(stopwatchFragment);
            }
        });

        return view;
    }



    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container_notifications, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}