package dev.coc12.identityvtraitmanager;

import android.os.Handler;
import android.widget.TextView;

public class CountdownCoolTime implements Runnable {

    private int openingCount;
    private int count;
    private TextView countdownText;
    private final Handler handler;

    public CountdownCoolTime(int openingCount, TextView countdownText) {
        setOpeningCount(openingCount);
        setCountdownText(countdownText);
        handler = new Handler();
    }

    public void startCountdown(int time) {
        count = time;
        startRunnable();
    }

    public void startOpeningCountdown() {
        count = openingCount;
        startRunnable();
    }

    private void startRunnable() {
        handler.removeCallbacks(this);
        handler.post(this);
    }

    @Override
    public void run() {
        countdownText.setText(String.valueOf(count));
        count--;
        if (count < 0) {
            countdownText.setText("");
            return;
        }
        handler.postDelayed(this, Constants.COUNTDOWN_INTERVAL);
    }

    public void setOpeningCount(int openingCount) {
        this.openingCount = openingCount;
    }

    public void setCountdownText(TextView countdownText) {
        this.countdownText = countdownText;
    }
}
