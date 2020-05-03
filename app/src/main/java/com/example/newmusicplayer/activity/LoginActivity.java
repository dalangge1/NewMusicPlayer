package com.example.newmusicplayer.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.newmusicplayer.animation.AnimationUtils;
import com.example.newmusicplayer.R;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private Button buttonLogin;
    private Button buttonRegister2;
    private Button buttonRegister1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        buttonLogin = findViewById(R.id.buttonLogin);
        buttonRegister1 = findViewById(R.id.buttonRegister1);
        buttonRegister2 = findViewById(R.id.buttonRegister2);


        buttonLogin.setOnClickListener(this);
        buttonRegister1.setOnClickListener(this);
        buttonRegister2.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        AnimationUtils.buttonClickAnimation(v);
        switch (v.getId()){
            case R.id.buttonLogin:
                Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                startActivity(intent);
                LoginActivity.this.finish();
                break;
        }
    }
}
