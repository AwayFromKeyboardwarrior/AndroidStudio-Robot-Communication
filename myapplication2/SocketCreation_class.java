package com.example.krjikim.myapplication2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

public class SocketCreation_class extends AppCompatActivity {

    static SocketCommunication socketCommunication = new SocketCommunication();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 상태바를 안보이도록 합니다.
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // 화면 켜진 상태를 유지합니다.
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.activity_connection_check);

        Button button_connect = (Button) findViewById(R.id.button_ConnectID);
        button_connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    final EditText edittext=(EditText) findViewById(R.id.textIP_ID);
                    final EditText edittext1=(EditText) findViewById(R.id.textPort_ID);
                    socketCommunication.ipaddress = edittext.getText().toString();
                    socketCommunication.portnumber = Integer.parseInt(edittext1.getText().toString());
                    socketCommunication.SocketConnection(socketCommunication.ipaddress,socketCommunication.portnumber);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        Button button_Disconnect = (Button) findViewById(R.id.button_DisconnectID);
        button_Disconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                   // SocketConnection.SocketDisConnection();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        Button button_Back = (Button) findViewById(R.id.button_BackID);
        button_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }




}
