package com.flinc.core;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

public class KeyboardButtonBackspaceView extends View {

    int myColor = 0xFF86888A;

    public KeyboardButtonBackspaceView(Context context) {
        super(context);
    }

    public KeyboardButtonBackspaceView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public KeyboardButtonBackspaceView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public KeyboardButtonBackspaceView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
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
        path.moveTo((float) (width*0.35), (float) (height*0.5));
        path.lineTo((float) (width*0.45), (float) (height*0.3));
        path.lineTo((float) (width*0.45), (float) (height*0.7));

        path.moveTo((float) (width*0.45), (float) (height*0.3));
        path.lineTo((float) (width*0.65), (float) (height*0.3));
        path.lineTo((float) (width*0.65), (float) (height*0.7));
        path.lineTo((float) (width*0.45), (float) (height*0.7));


        path.close();

        // рисование path
        p.setColor(myColor);
        canvas.drawPath(path, p);
    }
}
