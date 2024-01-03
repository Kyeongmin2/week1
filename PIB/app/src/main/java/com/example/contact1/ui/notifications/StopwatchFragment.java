package com.example.contact1.ui.notifications;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.os.SystemClock;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.contact1.R;
import com.example.contact1.databinding.ActivityMainBinding;
import com.example.contact1.databinding.FragmentStopwatchBinding;

public class StopwatchFragment extends Fragment {

    private StopwatchViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // 레이아웃 인플레이션을 통해 뷰 생성
        FragmentStopwatchBinding binding = FragmentStopwatchBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 뷰 바인딩 초기화
        FragmentStopwatchBinding binding = FragmentStopwatchBinding.bind(view);

        viewModel = new ViewModelProvider(this).get(StopwatchViewModel.class);

        binding.chronometer.setBase(SystemClock.elapsedRealtime() + viewModel.getPauseTime());

        if (viewModel.isRunning()) {
            binding.chronometer.start();
            binding.startButton.setEnabled(false);
            binding.resetButton.setEnabled(true);
            binding.stopButton.setEnabled(true);
        }

        binding.startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.chronometer.setBase(SystemClock.elapsedRealtime() + viewModel.getPauseTime());
                binding.chronometer.start();

                // 버튼 표시 여부 조정
                binding.startButton.setEnabled(false);
                binding.resetButton.setEnabled(true);
                binding.stopButton.setEnabled(true);

                viewModel.setRunning(true);
            }
        });

        binding.stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.setPauseTime(binding.chronometer.getBase() - SystemClock.elapsedRealtime());
                binding.chronometer.stop();
                binding.startButton.setEnabled(true);
                binding.resetButton.setEnabled(true);
                binding.stopButton.setEnabled(false);

                viewModel.setRunning(false);
            }
        });

        binding.resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.setPauseTime(0L);
                binding.chronometer.setBase(SystemClock.elapsedRealtime());
                binding.chronometer.stop();
                binding.startButton.setEnabled(true);
                binding.resetButton.setEnabled(false);
                binding.stopButton.setEnabled(false);

                viewModel.setRunning(false);
            }
        });
    }
}