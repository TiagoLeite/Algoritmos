package com.tiago.algoritmos;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class BarView extends View
{
    private Paint paint;
    private Rect rect;

    public BarView(Context context) {
        super(context);
    }

    public BarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        paint.setARGB(0xFF, 0xFF, 0, 0);
        rect = new Rect(10, 10, 80, 80);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawRect(rect, paint);
    }
}













