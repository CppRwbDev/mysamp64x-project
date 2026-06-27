package com.flinc.core;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class KeyboardButtonSpaceView extends androidx.appcompat.widget.AppCompatButton {

    int myColor = 0xFF86888A;

    public KeyboardButtonSpaceView(@NonNull Context context) {
        super(context);
    }

    public KeyboardButtonSpaceView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public KeyboardButtonSpaceView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        Path path = new Path();
        Paint p = new Paint();

        int width = getWidth();
        int height = getHeight();

        // очистка path
        path.reset();

        path.moveTo(0, (float) (height*0.25));
        path.lineTo(width, (float) (height*0.25));
        path.lineTo(width, (float) (height*(1-0.25)));
        path.lineTo(0, (float) (height*(1-0.25)));

        path.close();

        // рисование path
        p.setColor(myColor);
        canvas.drawPath(path, p);
        super.onDraw(canvas);
    }
}
