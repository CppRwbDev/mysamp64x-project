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

public class KeyboardButtonEnterView extends View {

    int myColor = 0xFF86888A;

    public KeyboardButtonEnterView(Context context) {
        super(context);
    }

    public KeyboardButtonEnterView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public KeyboardButtonEnterView(Context context, AttributeSet attrs) {
        super(context, attrs);
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
        path.moveTo((float) (width*0.3), (float) (height*0.3));
        path.lineTo((float) (width*0.7), (float) (height*0.5));
        path.lineTo((float) (width*0.3), (float) (height*0.7));
        path.close();

        // рисование path
        p.setColor(myColor);
        canvas.drawPath(path, p);

    }
}
