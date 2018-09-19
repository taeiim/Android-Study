package com.sesong.badthreadexam;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    // 스레드 객체
    private Thread mThread;
    // 카운팅
    private int mCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    // 스레드 시작
    public void startThread(View view) {
        if (mThread == null) {
            // 스레드 초기화 및 시작
            mThread = new Thread("My Thread") {
                @Override
                public void run() {
                    for (int i = 0; i < 100; i++) {
                        try {
                            mCount++;
                            // 1초마다 쉬기
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            // 인터럽트되면 오래 걸리는 처리 종료
                            break;
                        }
                        // 1초마다 로그 남기기
                        Log.d("My Thread", "스레드 동작 중" + mCount);
                    }
                }
            };
            mThread.start();
        }
    }

    // 스레드 종료
    public void stopThread(View view) {
        if (mThread != null) {
            mThread.interrupt();
            mThread = null;
            mCount = 0;
        }
    }
}
