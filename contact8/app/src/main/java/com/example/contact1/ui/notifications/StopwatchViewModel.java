package com.example.contact1.ui.notifications;

import androidx.lifecycle.ViewModel;

public class StopwatchViewModel extends ViewModel {
    private long pauseTime = 0L;
    private boolean isRunning = false;

    public long getPauseTime() {
        return pauseTime;
    }

    public void setPauseTime(long pauseTime) {
        this.pauseTime = pauseTime;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void setRunning(boolean running) {
        isRunning = running;
    }
}