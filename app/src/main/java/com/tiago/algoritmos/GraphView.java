package com.tiago.algoritmos;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class GraphView extends View
{
    //private Bitmap bitmap;
    private Canvas canvas;
    private Path path;
    private Path path2;
    private Paint paint, paint2;
    private Context context;
    private boolean firstClick = true;
    private float mx, my;

    public GraphView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.setDrawingCacheEnabled(true);
        this.context = context;
        path = new Path();
        path2 = new Path();
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(getResources().getColor(R.color.colorPrimary));
        //paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        //bitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
        canvas = new Canvas();
        paint2 = new Paint();
        paint2.setAntiAlias(true);
        paint2.setColor(getResources().getColor(R.color.colorPrimary));
        paint2.setStyle(Paint.Style.STROKE);
        paint2.setStrokeJoin(Paint.Join.ROUND);
        paint2.setStrokeWidth(10f);

        //canvas.setBitmap(bitmap);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPath(path, paint);
        canvas.drawPath(path2, paint2);
    }

    /*@Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        canvas = new Canvas();
        canvas.setBitmap(bitmap);

    }*/

    private void onStartTouch(float x, float y)
    {
        if(firstClick)
            firstClick = false;
        else
        {
            //Paint paint2 = new Paint();
            //paint2.setAntiAlias(true);
            //paint.setColor(getResources().getColor(R.color.colorAccent));
            //paint2.setStyle(Paint.Style.STROKE);
            //paint2.setStrokeJoin(Paint.Join.ROUND);
            //paint.setStrokeWidth(10f);
            path2.moveTo(mx, my);
            path2.lineTo(x, y);
            //path.quadTo(mx, my, x, y);
            //canvas.drawPath(path, paint2);
        }
        mx = x;
        my = y;
        path.addCircle(x, y, 25f, Path.Direction.CCW);
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





















