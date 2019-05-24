package com.example.krjikim.myapplication2;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.v4.view.VelocityTrackerCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.VelocityTracker;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.Date;

public class RobotMove_Screen extends AppCompatActivity implements SurfaceHolder.Callback {
    String text;
    TextView textMsg;
    VelocityTracker velocityTracker = null;
    boolean xy = false;
    boolean yz = false;
    boolean zx = false;
    boolean acc = false;

    SurfaceView surfaceView;
    SurfaceHolder holder;
    Paint paint = null;
    Canvas canvas = null;

    static SocketCommunication socketCommunication = new SocketCommunication();

    public RobotMove_Screen() {
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_robot_move_screen);


        surfaceView = (SurfaceView) findViewById(R.id.surfaceView_SwipeTouchID);
        holder = surfaceView.getHolder();
        holder.addCallback(RobotMove_Screen.this);
        surfaceView.setBackgroundColor(Color.WHITE);
        //surfaceView.setZOrderOnTop(true);
        surfaceView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
        surfaceView.setVisibility(View.INVISIBLE);

        surfaceView.setOnTouchListener(new View.OnTouchListener() {
            long startTime = 0, endTime = 0;
            float oldX = 0, oldY = 0, newX = 0, newY = 0;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getActionMasked();
                int index = event.getActionIndex();
                int pointerId = event.getPointerId(index);
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    oldX = event.getX();
                    oldY = event.getY();
                    Date startDate = new Date();
                    startTime = startDate.getTime();

                    if (velocityTracker == null) {
                        velocityTracker = VelocityTracker.obtain();
                    } else {
                        velocityTracker.clear();
                    }
                    velocityTracker.addMovement(event);
                    if (pointerId == 0) {
                        textMsg.setText("X-velocity (pixel/s): 0" + "/ Y-velocity (pixel/s): 0");
                    }

                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    //long timerTime = getTime between two event down to Up
                    newX = event.getX();
                    newY = event.getY();
                    Date endDate = new Date();
                    endTime = endDate.getTime();
                    double timeDiff = endTime - startTime;

                    double diffX = (newX - oldX);
                    double diffY = (newY - oldY);


                    final EditText edittext = (EditText) findViewById(R.id.editText_nFactor);
                    double distX;
                    double distY;
                    if (edittext.getText() == null) {
                        distX = diffX * (100 / timeDiff);
                        distY = diffY * (100 / timeDiff);
                        Toast.makeText(v.getContext(), "Set the number factor!", Toast.LENGTH_LONG).show();
                    } else {
                        double nFactor = Integer.parseInt(edittext.getText().toString());
                        distX = diffX * (nFactor / timeDiff);
                        distY = diffY * (nFactor / timeDiff);
                    }


                    if ((xy == true) && (yz == false) && (zx == false)) {
                        socketCommunication.socketsendingThread = new Thread(new SocketCommunication.SocketSending_Thread(distY + "," + (distX) + "," + "0" + ",Position"+getAcc()+",\n\0d"));
                        socketCommunication.socketsendingThread.start();
                        Toast.makeText(v.getContext(), "X-Y Move Successful!", Toast.LENGTH_LONG).show();
                    } else if ((xy == false) && (yz == true) && (zx == false)) {
                        socketCommunication.socketsendingThread = new Thread(new SocketCommunication.SocketSending_Thread("0" + "," + distX + "," + (-distY) + ",Position"+getAcc()+",\n\0d"));
                        socketCommunication.socketsendingThread.start();
                        Toast.makeText(v.getContext(), "Y-Z Move Successful!", Toast.LENGTH_LONG).show();
                    } else if ((xy == false) && (yz == false) && (zx == true)) {
                        socketCommunication.socketsendingThread = new Thread(new SocketCommunication.SocketSending_Thread((-distX) + "," + "0" + "," + (-distY) + ",Position"+getAcc()+",\n\0d"));
                        socketCommunication.socketsendingThread.start();
                        Toast.makeText(v.getContext(), "Z-X Move Successful!", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(v.getContext(), "button error. Msg not sent!", Toast.LENGTH_LONG).show();
                    }

                } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    velocityTracker.addMovement(event);
                    velocityTracker.computeCurrentVelocity(1000);
                    //1000 provides pixels per second

                    float xVelocity = VelocityTrackerCompat.getXVelocity(
                            velocityTracker, pointerId);

                    float yVelocity = VelocityTrackerCompat.getYVelocity(
                            velocityTracker, pointerId);

                    if (pointerId == 0) {
                        textMsg.setText("X-velocity (pixel/s): " + xVelocity + " / Y-velocity (pixel/s): " + yVelocity);
                    }
                } else if (event.getAction() == MotionEvent.ACTION_CANCEL) {
                    velocityTracker.recycle();
                    velocityTracker = null;
                }
                return true;
            }
        });


        ToggleButton button_Connection0 = (ToggleButton) findViewById(R.id.button_Robot_Swipe_Accumulation);
        button_Connection0.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    acc = true;
                } else {
                    acc = false;
                }
            }
        });

        ToggleButton button_Connection1 = (ToggleButton) findViewById(R.id.button_Robot_SwipeMove);
        button_Connection1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    surfaceView.setVisibility(View.VISIBLE);
                    textMsg = (TextView) findViewById(R.id.textView_SwipeID);
                } else {
                    surfaceView.setVisibility(View.INVISIBLE);
                    textMsg.setVisibility(View.INVISIBLE);
                }
            }
        });

        ToggleButton button_Connection2 = (ToggleButton) findViewById(R.id.button_SwipeXY);
        button_Connection2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    xy = true;
                } else {
                    xy = false;
                }
            }
        });

        ToggleButton button_Connection3 = (ToggleButton) findViewById(R.id.button_SwipeYZ);
        button_Connection3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    yz = true;
                } else {
                    yz = false;
                }
            }
        });

        ToggleButton button_Connection4 = (ToggleButton) findViewById(R.id.button_SwipeZX);
        button_Connection4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    zx = true;
                } else {
                    zx = false;
                }
            }
        });

        Button button_Connection5 = (Button) findViewById(R.id.button_Cancel);
        button_Connection5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Toast.makeText(v.getContext(), "Cancelled", Toast.LENGTH_LONG).show();
            }
        });
    }

    public String getAcc() {
        if (acc) {
            final EditText edittext = (EditText) findViewById(R.id.editText_AccIndex);
            return ",Acc,"+edittext.getText();
        } else {
            return "";
        }
    }

    @Override
    public void surfaceCreated(final SurfaceHolder holder) {

       /*
        // Get and lock canvas object from surfaceHolder.
        Canvas canvas = holder.lockCanvas(); //캔버스 객체 등록

        Paint surfaceBackground = new Paint(); //서피스배경 객체 등록
        // Set the surfaceview background color.
        surfaceBackground.setColor(Color.CYAN); //배경색 설정
        // Draw the surfaceview background color.
        canvas.drawRect(0, 0, surfaceView.getWidth(), surfaceView.getHeight(), surfaceBackground);

        // Draw the circle.
        //canvas.drawCircle(circleX, circleY, 100, paint);

        // Unlock the canvas object and post the new draw.
        //surfaceHolder.unlockCanvasAndPost(canvas);
        */
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    void setPaint(Paint paint) {
        this.paint = paint;
    }

    Paint getPaint() {
        return paint;
    }

}
