package com.example.krjikim.myapplication2;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import static com.example.krjikim.myapplication2.Constant.NS2S;
import static com.example.krjikim.myapplication2.Constant.RAD2DGR;


public class RobotMove_Sensor extends AppCompatActivity {

    //Gyro sensor variables
    Sensor sensor_Gyro;
    float X_Gyro = 0;
    float Y_Gyro = 0;
    float Z_Gyro = 0;
    TextView textX_Gyro;
    TextView textY_Gyro;
    TextView textZ_Gyro;

    //Magnetic sensor variables
    Sensor sensor_Mag;
    TextView textX_Mag;
    TextView textY_Mag;
    TextView textZ_Mag;
    float X_Mag = 0;
    float Y_Mag = 0;
    float Z_Mag = 0;
    float X_Mag_Cal = 0;
    float Y_Mag_Cal = 0;
    float Z_Mag_Cal = 0;

    //Roll and Pitch
    static double pitch = 0;
    static double roll = 0;
    static double yaw = 0;

    //timestamp and dt
    private double timestamp;
    private double dt;


    SensorManager sensorManager;

    static SocketCommunication socketCommunication = new SocketCommunication();
    ToggleButton button_Connection4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_robot_move_sensor);
        // 상태바를 안보이도록 합니다.
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // 화면 켜진 상태를 유지합니다.
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        textX_Gyro = (TextView) findViewById(R.id.textView1);
        textY_Gyro = (TextView) findViewById(R.id.textView2);
        textZ_Gyro = (TextView) findViewById(R.id.textView3);
        textX_Mag = (TextView) findViewById(R.id.textView4);
        textY_Mag = (TextView) findViewById(R.id.textView5);
        textZ_Mag = (TextView) findViewById(R.id.textView6);

/*
        Button button_Connection1 = (Button) findViewById(R.id.button_Connect);
        button_Connection1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    socketCommunication.SocketConnection(socketCommunication.ipaddress, socketCommunication.portnumber);
                    if (socketCommunication.SocketStatusChk()) {
                        Toast.makeText(v.getContext(), "Connected", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(v.getContext(), "Connection Fail", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(v.getContext(), "Connection Fail", Toast.LENGTH_LONG).show();
                }
            }
        });*/

        Button button_Connection2 = (Button) findViewById(R.id.button_GyroOn);
        button_Connection2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
                sensor_Gyro = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
                Toast.makeText(v.getContext(), "Gyro Sensor Activated", Toast.LENGTH_LONG).show();
                SensorEventListener gyroscopeSensorListener = new SensorEventListener() {
                    @Override
                    public void onSensorChanged(SensorEvent event) {

                        double gyroX = event.values[0];
                        double gyroY = event.values[1];
                        double gyroZ = event.values[2];

                        Sensor mySensor = event.sensor;
                        if (mySensor.getType() == Sensor.TYPE_GYROSCOPE) {
                            X_Gyro = event.values[0];
                            Y_Gyro = event.values[1];
                            Z_Gyro = event.values[2];

                            dt = (event.timestamp - timestamp) * NS2S;
                            timestamp = event.timestamp;

                            if (dt - timestamp * NS2S != 0) {
                                textX_Gyro.setText("X : " + pitch * RAD2DGR);
                                textY_Gyro.setText("Y : " + roll * RAD2DGR);
                                textZ_Gyro.setText("Z : " + yaw * RAD2DGR);
                                pitch = pitch + gyroY * dt;
                                roll = roll + gyroX * dt;
                                yaw = yaw + gyroZ * dt;
                            }
                        }
                    }

                    @Override
                    public void onAccuracyChanged(Sensor sensor, int i) {
                    }
                };
                sensorManager.registerListener(gyroscopeSensorListener,
                        sensor_Gyro, SensorManager.SENSOR_DELAY_NORMAL);
            }
        });

        Button button_Connection3 = (Button) findViewById(R.id.button_GyroCal);
        button_Connection3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pitch = 0;
                roll = 0;
                yaw = 0;
                Toast.makeText(v.getContext(), "Gyro Sensor initialized", Toast.LENGTH_LONG).show();
            }
        });


        button_Connection4 = (ToggleButton) findViewById(R.id.button_Robot_SynchronizeOn);
        button_Connection4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (socketCommunication.SocketStatusChk()) {
                        Thread socketsend = new Thread(new socketsend_Gyroscope());
                        socketsend.start();
                        Toast.makeText(getApplicationContext(), "Robot Connection Successful", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Robot Connection Fail", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Robot Disconnection Successful", Toast.LENGTH_LONG).show();
                }
            }
        });
        Button button_Connection5 = (Button) findViewById(R.id.button_MagOn);
        button_Connection5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
                sensor_Mag = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD_UNCALIBRATED);
                Toast.makeText(v.getContext(), "Magnetic Sensor Activated", Toast.LENGTH_LONG).show();
                SensorEventListener magnetSensorListener = new SensorEventListener() {
                    @Override
                    public void onSensorChanged(SensorEvent event) {
                        if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD_UNCALIBRATED) {
                            // set value on the screen
                            X_Mag = event.values[0];
                            Y_Mag = event.values[1];
                            Z_Mag = event.values[2];

                            textX_Mag.setText("X : " + (int) (X_Mag + X_Mag_Cal) + " rad/s");
                            textY_Mag.setText("Y : " + (int) (Y_Mag + Y_Mag_Cal) + " rad/s");
                            textZ_Mag.setText("Z : " + (int) (Z_Mag + Z_Mag_Cal) + " rad/s");
                        }
                    }

                    @Override
                    public void onAccuracyChanged(Sensor sensor, int accuracy) {
                    }
                };
                sensorManager.registerListener(magnetSensorListener,
                        sensor_Mag, SensorManager.SENSOR_DELAY_NORMAL);
            }
        });

        Button button_Connection6 = (Button) findViewById(R.id.button_MagCal);
        button_Connection6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                X_Mag_Cal = -X_Mag;
                Y_Mag_Cal = -Y_Mag;
                Z_Mag_Cal = -Z_Mag;
                Toast.makeText(v.getContext(), "Magnetic Sensor initialized", Toast.LENGTH_LONG).show();
            }
        });

        Button button_Connection7 = (Button) findViewById(R.id.button_Cancel);
        button_Connection7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Toast.makeText(v.getContext(), "Cancelled", Toast.LENGTH_LONG).show();
            }
        });
    }

    class socketsend_Gyroscope implements Runnable {
        @Override
        public void run() {
            while (button_Connection4.isChecked()) {
                try {
                    long pollrate;;
                    socketCommunication.socketsendingThread = new Thread(new SocketCommunication.SocketSending_Thread((-yaw) * RAD2DGR + "," + (-roll) * RAD2DGR + "," + pitch * RAD2DGR + ",Orientation,\n\0d"));
                    socketCommunication.socketsendingThread.start();
                    final EditText edittext = (EditText) findViewById(R.id.editText_pollrate);
                    if (edittext.getText() == null) {
                        pollrate = 1000;
                        Toast.makeText(getApplicationContext(), "Set the number factor!", Toast.LENGTH_LONG).show();
                    } else {
                        pollrate = Integer.parseInt(edittext.getText().toString());
                    }
                    Thread.sleep(pollrate);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}