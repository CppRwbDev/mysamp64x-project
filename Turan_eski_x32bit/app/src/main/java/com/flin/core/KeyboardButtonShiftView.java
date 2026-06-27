package com.flinc.core;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class KeyboardButtonShiftView extends View implements View.OnTouchListener {

    int colorArrow = 0xFF86888A;

    public KeyboardButtonShiftView(Context context) {
        super(context);

        goInit();
    }

    public KeyboardButtonShiftView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        goInit();
    }

    public KeyboardButtonShiftView(Context context, AttributeSet attrs) {
        super(context, attrs);

        goInit();
    }

    public void goInit(){
        this.setOnTouchListener(this);
    }

    public boolean onTouch(View v, MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            this.setBackgroundColor(0xFFF59132);
        }
        if(event.getAction() == MotionEvent.ACTION_UP){
            this.setBackgroundColor(Color.TRANSPARENT);
        }
        return false;
    }

    public void setColorArrow(int color){
        colorArrow = color;
        this.invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Path path = new Path();
        Paint p = new Paint();

        int width = getWidth();
        int height = getHeight();

        // очистка path
        path.reset();

        // угол
        path.moveTo((float) (width*0.5), (float) (height*0.3));
        path.lineTo((float) (width*0.35), (float) (height*0.5));
        path.lineTo((float) (width*0.65), (float) (height*0.5));

        path.moveTo((float) (width*(0.5-((0.5-0.35)/2))), (float) (height*0.5));
        path.lineTo((float) (width*(0.5-((0.5-0.35)/2))), (float) (height*0.7));
        path.lineTo((float) (width*(0.5+((0.65-0.5)/2))), (float) (height*0.7));
        path.lineTo((float) (width*(0.5+((0.65-0.5)/2))), (float) (height*0.5));


        path.close();

        // рисование path
        p.setColor(colorArrow);
        canvas.drawPath(path, p);

    }
}
