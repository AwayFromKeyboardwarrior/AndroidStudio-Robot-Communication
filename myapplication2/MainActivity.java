package com.example.krjikim.myapplication2;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;


public class MainActivity extends AppCompatActivity {
    FrameLayout container;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 상태바를 안보이도록 합니다.
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // 화면 켜진 상태를 유지합니다.
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.activity_main);
        Button button_Connection = (Button) findViewById(R.id.button_ConnectionID);
        button_Connection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, SocketCreation_class.class);
                startActivity(i);
            }
        });

        Button button_Move_Sensor = (Button) findViewById(R.id.button_MoveID_Sensor);
        button_Move_Sensor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, RobotMove_Sensor.class);
                startActivity(i);
            }
        });

        Button button_Move_Touch = (Button) findViewById(R.id.button_MoveID_Touch);
        button_Move_Touch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, RobotMove_Screen.class);
                startActivity(i);
            }
        });

        Button button_Camera = (Button) findViewById(R.id.button_CameraID);
        button_Camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, CameraAppearing.class);
                startActivity(i);
            }
        });

    }

}