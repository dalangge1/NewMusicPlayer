package com.example.newmusicplayer.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;

import com.example.newmusicplayer.R;
import com.example.newmusicplayer.utils.ThreadPool;

public class WelcomeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try{
                    Thread.sleep(500);
                    Intent mainIntent = new Intent(WelcomeActivity.this, LoginActivity.class);
                    startActivity(mainIntent);
                    WelcomeActivity.this.finish();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        };

        ThreadPool.getInstance().execute(runnable);

    }
}
