package com.tiago.algoritmos;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class GraphView extends View
{
    private Paint paint;
    private PointF pointA, pointB;

    public GraphView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setColor(Color.BLUE);
        paint.setStrokeWidth(20);
        canvas.drawLine(pointA.x, pointA.y, pointB.x, pointB.y, paint);
    }


    public void setPointA(PointF pointA) {
        this.pointA = pointA;
    }

    public void setPointB(PointF pointB) {
        this.pointB = pointB;
    }

    public void draw()
    {
        invalidate();
        requestLayout();
    }
}





















