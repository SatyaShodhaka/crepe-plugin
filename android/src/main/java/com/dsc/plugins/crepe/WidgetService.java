package com.dsc.plugins.crepe;

import static com.dsc.plugins.crepe.DemonstrationUtil.initiateDemonstration;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import edu.nd.crepe.R;
import edu.nd.crepe.ui.dialog.GraphQueryCallback;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;

public class WidgetService extends Service {
    int LAYOUT_FLAG;
    View mFloatingView;
    WindowManager windowManager;
    float height, width;
    Context c = WidgetService.this;
    FullScreenOverlayManager fullScreenOverlayManager;

    GraphQueryCallback graphQueryCallback;

    public IBinder onBind(Intent intent){
        return null;
    }

    public int onStartCommand(Intent intent, int flags, int startId) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_PHONE;
        }

        this.graphQueryCallback = (GraphQueryCallback) intent.getSerializableExtra("graphQueryCallback");

        // inflate widget layout
        mFloatingView = LayoutInflater.from(c).inflate(R.layout.demonstration_float_widget, null);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                LAYOUT_FLAG,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        // initialize position
        layoutParams.gravity = Gravity.TOP | Gravity.RIGHT;
        layoutParams.x = 0;
        layoutParams.y = 100;

        windowManager = (WindowManager)getSystemService(WINDOW_SERVICE);
        WidgetDisplay widgetDisplay = new WidgetDisplay(layoutParams, mFloatingView, windowManager);
        widgetDisplay.showWidget();
        height = windowManager.getDefaultDisplay().getHeight();
        width = windowManager.getDefaultDisplay().getHeight();

        // initialize fullScreenOverlayManager
        fullScreenOverlayManager = new FullScreenOverlayManager(c, windowManager, getResources().getDisplayMetrics(), this.graphQueryCallback);


        // initialize callback for the data received from the demonstration

        FloatingActionButton closeFltBtn = (FloatingActionButton) mFloatingView.findViewById(R.id.floating_close);
        FloatingActionButton drawFltBtn = (FloatingActionButton) mFloatingView.findViewById(R.id.floating_draw_frame);

        closeFltBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // turn off the overlay before leaving the app
                if(fullScreenOverlayManager.getShowingOverlay()) {
                    fullScreenOverlayManager.disableOverlay();
                }

                stopSelf();
                Intent launchIntent = c.getPackageManager().getLaunchIntentForPackage("edu.nd.crepe");
                if (launchIntent != null) {
                    c.startActivity(launchIntent);
                } else {
                    Toast.makeText(c, "There is no package available in android", Toast.LENGTH_LONG).show();
                }

                widgetDisplay.removeWidget();
            }
        });

        drawFltBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                initiateDemonstration(c, fullScreenOverlayManager, widgetDisplay);
                Toast.makeText(c, "Please tap on the data to collect", Toast.LENGTH_LONG).show();
            }
        });

        // drag movement
//        closeFltBtn.setOnTouchListener(new View.OnTouchListener() {
//            int initialX, initialY;
//            float initialTouchX, initialTouchY;
//            long startClickTime;
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//                switch(motionEvent.getAction()){
//                    case MotionEvent.ACTION_DOWN:
//                        startClickTime = Calendar.getInstance().getTimeInMillis();
//                        mFloatingView.setVisibility(View.VISIBLE);
//                        initialX = layoutParams.x;
//                        initialY = layoutParams.y;
//                        // touch position
//                        initialTouchX = motionEvent.getRawX();
//                        initialTouchY = motionEvent.getRawY();
//                        return true;
//                    case MotionEvent.ACTION_UP:
//                        long clickDuration = Calendar.getInstance().getTimeInMillis() - startClickTime;
//                        mFloatingView.setVisibility(View.VISIBLE);
//                        layoutParams.x = initialX + (int)(initialTouchX - motionEvent.getRawX());
//                        layoutParams.y = initialY + (int)(motionEvent.getRawY() - initialTouchY);
//                        return true;
//                    case MotionEvent.ACTION_MOVE:
//                        mFloatingView.setVisibility(View.VISIBLE);
//                        layoutParams.x = initialX + (int)(initialTouchX - motionEvent.getRawX());
//                        layoutParams.y = initialY + (int)(motionEvent.getRawY() - initialTouchY);
//                        windowManager.updateViewLayout(mFloatingView,layoutParams);
//                        return true;
//                }
//                return false;
//            }
//        });
//
//        drawFltBtn.setOnTouchListener(new View.OnTouchListener() {
//            int initialX, initialY;
//            float initialTouchX, initialTouchY;
//            long startClickTime;
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//                switch(motionEvent.getAction()){
//                    case MotionEvent.ACTION_DOWN:
//                        startClickTime = Calendar.getInstance().getTimeInMillis();
//                        mFloatingView.setVisibility(View.VISIBLE);
//                        initialX = layoutParams.x;
//                        initialY = layoutParams.y;
//                        // touch position
//                        initialTouchX = motionEvent.getRawX();
//                        initialTouchY = motionEvent.getRawY();
//                        return true;
//                    case MotionEvent.ACTION_UP:
//                        long clickDuration = Calendar.getInstance().getTimeInMillis() - startClickTime;
//                        mFloatingView.setVisibility(View.VISIBLE);
//                        layoutParams.x = initialX + (int)(initialTouchX - motionEvent.getRawX());
//                        layoutParams.y = initialY + (int)(motionEvent.getRawY() - initialTouchY);
//                        return true;
//                    case MotionEvent.ACTION_MOVE:
//                        mFloatingView.setVisibility(View.VISIBLE);
//                        layoutParams.x = initialX + (int)(initialTouchX - motionEvent.getRawX());
//                        layoutParams.y = initialY + (int)(motionEvent.getRawY() - initialTouchY);
//                        windowManager.updateViewLayout(mFloatingView,layoutParams);
//                        return true;
//                }
//                return false;
//            }
//        });

        mFloatingView.setOnTouchListener(new View.OnTouchListener() {
            int initialX, initialY;
            float initialTouchX, initialTouchY;
            long startClickTime;
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch(motionEvent.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        startClickTime = Calendar.getInstance().getTimeInMillis();
                        mFloatingView.setVisibility(View.VISIBLE);
                        initialX = layoutParams.x;
                        initialY = layoutParams.y;
                        // touch position
                        initialTouchX = motionEvent.getRawX();
                        initialTouchY = motionEvent.getRawY();
                        return true;
                    case MotionEvent.ACTION_UP:
                        long clickDuration = Calendar.getInstance().getTimeInMillis() - startClickTime;
                        mFloatingView.setVisibility(View.VISIBLE);
                        layoutParams.x = initialX + (int)(initialTouchX - motionEvent.getRawX());
                        layoutParams.y = initialY + (int)(motionEvent.getRawY() - initialTouchY);
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        mFloatingView.setVisibility(View.VISIBLE);
                        layoutParams.x = initialX + (int)(initialTouchX - motionEvent.getRawX());
                        layoutParams.y = initialY + (int)(motionEvent.getRawY() - initialTouchY);
                        windowManager.updateViewLayout(mFloatingView,layoutParams);
                        return true;
                }
                return false;
            }
        });

        return START_STICKY;
    }

    // remove widget
    public void onDestroy() {
        super.onDestroy();
        if (mFloatingView != null){
            windowManager.removeView(mFloatingView);

        }
    }


    private void setVisibility(Boolean clicked, FloatingActionButton btn) {
        // if the fab icon is clicked, show the small buttons
        if(!clicked) {
            btn.setVisibility(View.VISIBLE);
        } else {
            // if the fab icon is clicked to be closed, set the visibilities to invisible
            btn.setVisibility(View.INVISIBLE);
        }
    }

    public void registerCallback(GraphQueryCallback dataGraphQueryCallback) {
        graphQueryCallback = dataGraphQueryCallback;
    }


}
