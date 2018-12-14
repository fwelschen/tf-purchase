package trocafone.tf_purchase;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.lang.reflect.Array;


public class TouchView extends View {

    private float x;
    private float y;

    Paint drawPaint;
    private Path path = new Path();
    private int[] location = new int[2];
    int tileSize = 40;


    public TouchView(Context context) {
        super(context);
    }

    public TouchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        drawPaint = new Paint(Paint.DITHER_FLAG);
        drawPaint.setAntiAlias(true);
        drawPaint.setColor(Color.parseColor("#5D4037"));
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setStrokeJoin(Paint.Join.ROUND);
        drawPaint.setStrokeWidth(3);
        setWillNotDraw(false);
    }

    @Override
    protected void onSizeChanged(int w, int h, int width, int height) {
        super.onSizeChanged(w, h, width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int right, bottom;
        right =  getRight();
        bottom =  getBottom();

        super.onDraw(canvas);
        canvas.drawPath(path, drawPaint);



        // Pido disculpas por esto, ojala no me echen
        // drawing left vertical rectangles
        for (int i=0 ; i<bottom; i = i + tileSize){
            canvas.drawRect(0, i, tileSize, i+tileSize, drawPaint);
        }

        // drawing top horizontal rectangles
        for (int i = tileSize; i<right; i = i + tileSize){
            canvas.drawRect(i, 0, i+tileSize, tileSize, drawPaint);
        }

        // drawing bottom horizontal rectangles
        for (int i = tileSize; i<right; i = i + tileSize){
            canvas.drawRect(i, bottom-tileSize, i+tileSize, bottom, drawPaint);
        }

        // drawing right vertical rectangles
        for (int i = 0; i<bottom-tileSize; i = i + tileSize){
            canvas.drawRect(right-tileSize, i, right, i+tileSize, drawPaint);
        }

        // getLocationOnScreen(location);
       // int x = location[0];
       // int y = location[1];

        //canvas.drawRect(5, 5, 700, 1100, drawPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        x = event.getX();
        y = event.getY();
        int h,l;
        h = (int) event.getX();
        l = (int) event.getY();
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                location[0] = h;
                location[1] = l;

                path.moveTo(x, y);
                return true;
            case MotionEvent.ACTION_MOVE:
                path.lineTo(x, y);
                location[0] = h;
                location[1] = l;

                if ((h <= tileSize || h >= (getRight()-tileSize )) || (l <= tileSize || l >= getBottom()-tileSize) ){
                    drawPaint.setColor(Color.parseColor("#000DFD"));
                }

                break;
            default:
                return false;
        }
        invalidate();
        return true;
    }
}