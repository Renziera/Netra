package com.interpixel.netra;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.GestureDetectorCompat;
import androidx.viewpager.widget.ViewPager;

public class NetraViewPager extends ViewPager {

    private GestureDetectorCompat detector;
    private NetraGestureListener listener = new NetraGestureListener();
    private FlingListener flingListener;

    public NetraViewPager(@NonNull Context context) {
        super(context);
        detector = new GestureDetectorCompat(context, listener);
    }

    public NetraViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        detector = new GestureDetectorCompat(context, listener);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        detector.onTouchEvent(ev);
        return super.onTouchEvent(ev);
    }

    public void setFlingListener(FlingListener flingListener) {
        this.flingListener = flingListener;
    }

    class NetraGestureListener extends GestureDetector.SimpleOnGestureListener{
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if(Math.abs(e1.getX() - e2.getX()) > 250){
                return super.onFling(e1, e2, velocityX, velocityY);
            }

            float diffY = e1.getY() - e2.getY();
            boolean isUp = diffY > 0;
            diffY = Math.abs(diffY);
            if(diffY < 250){
                return super.onFling(e1, e2, velocityX, velocityY);
            }

            if(isUp){
                flingListener.menuNext();
            }else{
                flingListener.menuPrev();
            }

            return false;
        }
    }
}
