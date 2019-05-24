package com.example.krjikim.myapplication2;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v4.view.VelocityTrackerCompat;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.VelocityTracker;
import android.view.View;
import android.widget.TextView;

public class surfaceView_screen extends SurfaceView implements SurfaceHolder.Callback, View.OnTouchListener {
    Context mContext;
    SurfaceView surfaceView;
    SurfaceHolder mHolder;
    TextView textVelocityX0, textVelocityY0,textVelocityX1, textVelocityY1;
    VelocityTracker velocityTracker = null;

    public surfaceView_screen(Context context) {
        super(context);
        mContext = context;
        mHolder = getHolder();
        mHolder.addCallback(this);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        Canvas canvas = null;
        canvas = mHolder.lockCanvas(null);
        Paint paint = null;
        canvas.drawCircle(100,200,300,paint );
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int action = event.getActionMasked();
        int index = event.getActionIndex();
        int pointerId = event.getPointerId(index);

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                if (velocityTracker == null) {
                    velocityTracker = VelocityTracker.obtain();
                } else {
                    velocityTracker.clear();
                }
                velocityTracker.addMovement(event);

                if (pointerId == 0) {
                    textVelocityX0.setText("X-velocity (pixel/s): 0");
                    textVelocityY0.setText("Y-velocity (pixel/s): 0");
                } else if (pointerId == 1) {
                    textVelocityX1.setText("X-velocity (pixel/s): 0");
                    textVelocityY1.setText("Y-velocity (pixel/s): 0");
                }

                break;
            case MotionEvent.ACTION_MOVE:
                velocityTracker.addMovement(event);
                velocityTracker.computeCurrentVelocity(1000);
                //1000 provides pixels per second

                float xVelocity = VelocityTrackerCompat.getXVelocity(
                        velocityTracker, pointerId);

                float yVelocity = VelocityTrackerCompat.getYVelocity(
                        velocityTracker, pointerId);

                if (pointerId == 0) {
                    textVelocityX0.setText("X-velocity (pixel/s): " + xVelocity);
                    textVelocityY0.setText("Y-velocity (pixel/s): " + yVelocity);
                } else if (pointerId == 1) {
                    textVelocityX1.setText("X-velocity (pixel/s): " + xVelocity);
                    textVelocityY1.setText("Y-velocity (pixel/s): " + yVelocity);
                }

                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                velocityTracker.recycle();
                break;
        }

        return true;
    }

    @Override
    public boolean onTouch(View v, MotionEvent motionEvent) {
        float x = motionEvent.getX();

        float y = motionEvent.getY();

       // customSurfaceView.setCircleX(x);

       // customSurfaceView.setCircleY(y);
        return false;
    }
}
