package com.example.adoptpet;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

public class OnSwipeTouchListener implements View.OnTouchListener {
        private final GestureDetector gestureDetector;
        Context context;
        OnSwipeTouchListener(Context ctx, View mainView) {
            gestureDetector = new GestureDetector(ctx, new GestureListener());
            mainView.setOnTouchListener(this);
            context = ctx;
        }
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            return gestureDetector.onTouchEvent(event);
        }
        public class GestureListener extends
                GestureDetector.SimpleOnGestureListener {
            private static final int SWIPE_THRESHOLD = 100;
            private static final int SWIPE_VELOCITY_THRESHOLD = 100;
            @Override
            public boolean onDown(MotionEvent e) {
                return true;
            }
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                boolean result = false;
                    float diffY = e2.getY() - e1.getY();
                    float diffX = e2.getX() - e1.getX();
                    if (Math.abs(diffX) > Math.abs(diffY))
                    {
                        if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD)
                        {
                            if (diffX > 0)
                            {
                                onSwipeRight();
                            }
                            else {
                                onSwipeLeft();
                            }
                            result = true;
                        }
                    }
                return result;
            }
        }
        void onSwipeRight() {
            Toast.makeText(context, "Swiped Right", Toast.LENGTH_SHORT).show();
            if(context instanceof AddPetActivity) //TODO : can be interface
                ((AddPetActivity) context).swipeRight();
            if(context instanceof HomeActivity)
                ((HomeActivity) context).swipeRight();
        }
        void onSwipeLeft() {
            Toast.makeText(context, "Swiped Left", Toast.LENGTH_SHORT).show();
            if(context instanceof MyPetsActivity) //TODO : can be interface
                ((AddPetActivity) context).swipeLeft();
            if(context instanceof AddPetActivity)
                ((AddPetActivity) context).swipeLeft();
        }
    }

