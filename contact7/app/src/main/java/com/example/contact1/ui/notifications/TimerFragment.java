package com.example.contact1.ui.notifications;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.contact1.R;

import java.util.Timer;
import java.util.TimerTask;

public class TimerFragment extends Fragment {

    LinearLayout timeCountSettingLV, timeCountLV;
    EditText hourET, minuteET, secondET;
    TextView hourTV, minuteTV, secondTV, finishTV;
    Button startBtn, stopBtn, resetBtn;
    int hour, minute, second;
    Timer timer;
    long remainingTime = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_timer, container, false);

        timeCountSettingLV = view.findViewById(R.id.timeCountSettingLV);
        timeCountLV = view.findViewById(R.id.timeCountLV);

        hourET = view.findViewById(R.id.hourET);
        minuteET = view.findViewById(R.id.minuteET);
        secondET = view.findViewById(R.id.secondET);

        hourTV = view.findViewById(R.id.hourTV);
        minuteTV = view.findViewById(R.id.minuteTV);
        secondTV = view.findViewById(R.id.secondTV);
        finishTV = view.findViewById(R.id.finishTV);

        startBtn = view.findViewById(R.id.startBtn);
        stopBtn = view.findViewById(R.id.stopBtn);
        resetBtn = view.findViewById(R.id.resetBtn);
        showButtons(false);
        // 시작버튼 이벤트 처리
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startTimer();
            }
        });
        stopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopTimer();
            }
        });

        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetTimer();
            }
        });

        return view;
    }
    private void showResetUI() {
        timeCountSettingLV.setVisibility(View.VISIBLE);
        timeCountLV.setVisibility(View.GONE);

        hourET.setText("");
        minuteET.setText("");
        secondET.setText("");
    }
    // 알람을 울리기 위한 메서드
    private void triggerNotification() {
        String CHANNEL_ID = "timer_channel";
        CharSequence name = "Timer Channel";
        String description = "Channel for Timer Notifications";
        int importance = NotificationManager.IMPORTANCE_HIGH;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system
            NotificationManager notificationManager = requireActivity().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        Intent intent = new Intent(requireContext(), requireContext().getClass());
        PendingIntent pendingIntent = PendingIntent.getActivity(requireContext(), 0, intent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(requireContext(), CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("타이머 완료")
                .setContentText("타이머가 완료되었습니다.")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(requireContext());
        notificationManager.notify(1, builder.build());

        resetTimer();
    }
    private void startTimer() {

        // 기존에 정지된 시간이 있다면 해당 시간을 사용
        if (remainingTime > 0) {
            continueTimer();
            return;
        }
        showButtons(true);

        String hourStr = hourET.getText().toString().trim();
        String minuteStr = minuteET.getText().toString().trim();
        String secondStr = secondET.getText().toString().trim();

        // 입력값이 비어있는 경우 0으로 처리
        if (hourStr.isEmpty()) {
            hourStr = "0";
        }
        if (minuteStr.isEmpty()) {
            minuteStr = "0";
        }
        if (secondStr.isEmpty()) {
            secondStr = "0";
        }

        // 시간 값 파싱
        hour = Integer.parseInt(hourStr);
        minute = Integer.parseInt(minuteStr);
        second = Integer.parseInt(secondStr);

        timeCountSettingLV.setVisibility(View.GONE);
        timeCountLV.setVisibility(View.VISIBLE);
        // 이전과 동일한 타이머 시작 로직
        timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                // 반복실행할 구문

                // UI 업데이트는 메인 쓰레드에서 해야 합니다.
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        // 시간 감소 로직 수정
                        if (second > 0) {
                            second--;
                        } else {
                            if (minute > 0) {
                                minute--;
                                second = 59;
                            } else {
                                if (hour > 0) {
                                    hour--;
                                    minute = 59;
                                    second = 59;
                                } else {
                                    timer.cancel(); // 타이머 종료
                                    // 알람 울리기
                                    triggerNotification();
                                    return;
                                }
                            }
                        }

                        // 시, 분, 초가 10이하(한자리수) 라면
                        // 숫자 앞에 0을 붙인다 ( 8 -> 08 )
                        secondTV.setText((second <= 9) ? "0" + second : Integer.toString(second));
                        minuteTV.setText((minute <= 9) ? "0" + minute : Integer.toString(minute));
                        hourTV.setText((hour <= 9) ? "0" + hour : Integer.toString(hour));
                    }
                });
            }
        };
        timer.schedule(timerTask, 0, 1000);
    }
    private void continueTimer() {
        showButtons(true);
        timeCountSettingLV.setVisibility(View.GONE);
        timeCountLV.setVisibility(View.VISIBLE);

        timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        if (remainingTime > 0) {
                            remainingTime -= 1000;
                        } else {
                            timer.cancel();
                            triggerNotification();
                            return;
                        }

                        second = (int) (remainingTime / 1000) % 60;
                        minute = (int) ((remainingTime / (1000 * 60)) % 60);
                        hour = (int) ((remainingTime / (1000 * 60 * 60)) % 24);

                        secondTV.setText((second <= 9) ? "0" + second : Integer.toString(second));
                        minuteTV.setText((minute <= 9) ? "0" + minute : Integer.toString(minute));
                        hourTV.setText((hour <= 9) ? "0" + hour : Integer.toString(hour));
                    }
                });
            }
        };
        timer.schedule(timerTask, 0, 1000);
    }
    private void stopTimer() {
        showButtons(true);
        if (timer != null) {
            timer.cancel(); // 타이머 정지
        }
        remainingTime = hour * 3600000 + minute * 60000 + second * 1000;
    }
    private void resetTimer() {
        // 타이머 정지 후 UI 초기화

        stopTimer();
        remainingTime=0;
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                showResetUI();
            }
        });
        showButtons(false);
    }
    private void showButtons(boolean show) {
        if (show) {
            stopBtn.setVisibility(View.VISIBLE);
            resetBtn.setVisibility(View.VISIBLE);
        } else {
            stopBtn.setVisibility(View.GONE);
            resetBtn.setVisibility(View.GONE);
        }
    }
}