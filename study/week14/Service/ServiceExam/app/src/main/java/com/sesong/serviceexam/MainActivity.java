package com.sesong.serviceexam;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private MyService mService;
    private boolean mBound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // 서비스에 바인딩
        Intent intent = new Intent(this, MyService.class);
        bindService(intent, mConnection, BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        // 서비스와 연결 해제
        if (mBound) {
            unbindService(mConnection);
            mBound = false;
        }
    }

    /**
     * bindService() 를 통해 서비스와 연결될 때의 콜백 정의
     */
    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // MyBinder와 연결될 것이며 IBinder 타입으로 넘어오는 것을 캐스팅하여 사용
            MyService.MyBinder binder = (MyService.MyBinder) service;
            mService = binder.getService();
            mBound = true;
        }
        @Override
        public void onServiceDisconnected(ComponentName name) {
            // 예기치 않은 종료
        }
    };

    public void onStartService(View view) {
        Intent intent = new Intent(this, MyService.class);
        startService(intent);
    }

    public void onStopService(View view) {
        Intent intent = new Intent(this, MyService.class);
        stopService(intent);
    }

    public void onStartIntentService(View view) {
        Intent intent = new Intent(this, MyIntentService.class);
        startService(intent);
    }

    public void onStartForegroundService(View view) {
        Intent intent = new Intent(this, MyService.class);
        intent.setAction("startForeground");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent);
        } else {
            startService(intent);
        }
    }

    public void getCountValue(View view) {
        if (mBound){
            Toast.makeText(this, "카운팅 : " + mService.getCount(), Toast.LENGTH_SHORT).show();
        }
    }
}
