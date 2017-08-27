package com.tiago.algoritmos;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class GraphView extends View
{
    private Bitmap bitmap;
    private Canvas canvas;
    private Path path;
    private Paint paint;
    private Context context;
    private float mx, my;

    public GraphView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.setDrawingCacheEnabled(true);
        this.context = context;
        path = new Path();
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(getResources().getColor(R.color.colorPrimary));
        //paint.setStyle(Paint.Style.STROKE);
        //paint.setStrokeJoin(Paint.Join.ROUND);
        //paint.setStrokeWidth(60f);
        bitmap = Bitmap.createBitmap(50, 50, Bitmap.Config.ARGB_8888);
        canvas = new Canvas();
        canvas.setBitmap(bitmap);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPath(path, paint);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        canvas = new Canvas();
        canvas.setBitmap(bitmap);

    }

    private void onStartTouch(float x, float y)
    {
        path.addCircle(x, y, 35f, Path.Direction.CCW);
        path.moveTo(x, y);
        mx = x;
        my = y;
    }

    private void moveTouch(float x, float y)
    {
        float dx = Math.abs(x - mx);
        float dy = Math.abs(y - my);
        if(dx >= 5 || dy >= 5)
        {
            path.quadTo(mx, my, (x+mx)/2f, (y+my)/2f);
            mx = x;
            my = y;
        }
    }

    private void upTouch()
    {
        path.lineTo(mx, my);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        if(event.getAction() == MotionEvent.ACTION_DOWN)
        {
            onStartTouch(x, y);
            invalidate();
        }
        /*switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                onStartTouch(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                moveTouch(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                upTouch();
                invalidate();
                break;

        }*/
        return true;
    }

}





















