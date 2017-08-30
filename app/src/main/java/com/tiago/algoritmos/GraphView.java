package com.tiago.algoritmos;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.support.v4.util.Pair;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class GraphView extends View
{
    //private Bitmap bitmap;
    private Canvas canvas;
    private Path path, path2, pathHint;
    private Paint paint, paint2, paintHint;
    private Context context;
    private double mx, my;
    private boolean isMoving = false;
    private List<Pair<Double, Double>> vertexes;

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
        paintHint = new Paint();
        pathHint = new Path();
        paintHint.setAntiAlias(true);
        paintHint.setColor(getResources().getColor(R.color.colorPrimary));
        paintHint.setStyle(Paint.Style.STROKE);
        paintHint.setStrokeJoin(Paint.Join.ROUND);
        paintHint.setStrokeWidth(2f);
        vertexes = new ArrayList<>();

        //canvas.setBitmap(bitmap);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPath(path, paint);
        canvas.drawPath(path2, paint2);
        canvas.drawPath(pathHint, paintHint);
    }

    /*@Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        canvas = new Canvas();
        canvas.setBitmap(bitmap);

    }*/

    private void drawVertex(double x, double y)
    {
        //Paint paint2 = new Paint();
        //paint2.setAntiAlias(true);
        //paint.setColor(getResources().getColor(R.color.colorAccent));
        //paint2.setStyle(Paint.Style.STROKE);
        //paint2.setStrokeJoin(Paint.Join.ROUND);
        //paint.setStrokeWidth(10f);
        //path2.moveTo(mx, my);
        //path2.lineTo(x, y);
        //path.quadTo(mx, my, x, y);
        //canvas.drawPath(path, paint2);
        //}
        path.addCircle((float) x, (float) y, 25f, Path.Direction.CCW);
    }

    /*private void moveTouch(double x, double y)
    {
        double dx = Math.abs(x - mx);
        double dy = Math.abs(y - my);
        if(dx >= 5 || dy >= 5)
        {
            path.quadTo(mx, my, (x+mx)/2f, (y+my)/2f);
            mx = x;
            my = y;
        }
    }*/

    /*private void upTouch()
    {
        path.lineTo(mx, my);
    }*/

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        double x = event.getX();
        double y = event.getY();

        if(event.getAction() == MotionEvent.ACTION_DOWN)
        {
            if (vertexExists(x, y, 80f))
            {
                Pair<Double, Double> p = getNearestVertex(x, y);
                pathHint.moveTo(p.first.floatValue(), p.second.floatValue());
                path2.moveTo(p.first.floatValue(), p.second.floatValue());
                mx = p.first.floatValue();
                my = p.second.floatValue();
                return true;
            }
            vertexes.add(new Pair<>(x, y));
            path2.moveTo((float) x, (float)y);
            pathHint.reset();
            pathHint.moveTo((float)x, (float)y);
            drawVertex(x, y);
            mx = x;
            my = y;
            Log.d("debug", "down");
            invalidate();
        }
        else if(event.getAction() == MotionEvent.ACTION_MOVE)
        {
            pathHint.reset();
            pathHint.moveTo((float) mx, (float) my);
            pathHint.lineTo((float) x, (float) y);
            if(vertexExists(x, y, 80f))
            {
                pathHint.reset();
                Pair<Double, Double> p = getNearestVertex(x, y);
                path2.moveTo((float) mx, (float) my);
                path2.lineTo(p.first.floatValue(), p.second.floatValue());
                pathHint.moveTo((float) mx, (float) my);
                pathHint.lineTo(p.first.floatValue(), p.second.floatValue());
                TextView textView = new TextView(getContext());
                textView.setTypeface(Typeface.DEFAULT_BOLD);
                textView.setPadding(8, 8, 8, 8);
                textView.setX((float) ((mx+p.first.floatValue())/2f));
                textView.setY((float) ((my+p.second.floatValue())/2f));
                ((ViewGroup)this.getParent()).addView(textView);
                textView.setText("3");
                textView.setTextColor(getResources().getColor(R.color.colorAccent));
                /*textView.animate()
                        .x((float) ((mx+p.first.floatValue())/2f))
                        .y((float) ((my+p.second.floatValue())/2f))
                        .setDuration(500);*/
                mx = p.first;
                my = p.second;
                //pathHint.reset();
            }
            Log.d("debug", "move");
            invalidate();
        }
        else if(event.getAction() == MotionEvent.ACTION_UP)
        {
            /*if(vertexExists(x, y, 10f))
            {
                Pair<Double, Double> p = getNearestVertex(x, y);
                path2.moveTo((float) mx, (float) my);
                path2.lineTo(p.first.floatValue(), p.second.floatValue());
                TextView textView = new TextView(getContext());
                //textView.setX((float) mx);
                //textView.setY((float) my);
                ((ViewGroup)this.getParent()).addView(textView);
                textView.setText("3");
                textView.setTextColor(Color.BLACK);
                textView.animate()
                        .x((float) ((mx+p.first.floatValue())/2f))
                        .y((float) ((my+p.second.floatValue())/2f))
                        .setDuration(500);
            }*/
            pathHint.reset();
            Log.d("debug", "move");
            invalidate();
        }
        return true;
    }

    private boolean vertexExists(double x, double y, double tolerance)
    {
        double dx, dy, dist;
        for(Pair<Double, Double> p : vertexes)
        {
            dx = Math.abs(x - p.first);
            dy = Math.abs(y - p.second);
            dist = Math.sqrt(Math.pow(dx, 2)+Math.pow(dy, 2));
            Log.d("debug", dist+"======");
            if(dist < tolerance)
                return true;
        }
        return false;
    }

    private Pair<Double, Double> getNearestVertex(double x, double y)
    {
        double dx, dy;
        double val, min = Double.MAX_VALUE;
        Pair<Double, Double> pair = null;
        for(Pair<Double, Double> p : vertexes)
        {
            dx = Math.abs(x - p.first);
            dy = Math.abs(y - p.second);
            val = Math.sqrt(Math.pow(dx, 2)+Math.pow(dy, 2));
            if(val < min)
            {
                min = val;
                pair = p;
                Log.d("debug", val+",  x = "+p.first);
            }
        }
        return pair;
    }

    private double distance(Pair<Double, Double> p, double x, double y)
    {
        double dx, dy;
        dx = Math.abs(x - p.first);
        dy = Math.abs(y - p.second);
        return Math.sqrt(Math.pow(dx, 2)+Math.pow(dy, 2));
    }

    public void resetLines()
    {
        path2.reset();
        invalidate();
    }

}





















