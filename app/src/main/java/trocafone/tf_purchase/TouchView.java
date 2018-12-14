package trocafone.tf_purchase;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.lang.reflect.Array;
import java.util.ArrayList;


public class TouchView extends View {

    private float x;
    private float y;


    Paint drawPaint;
    Paint drawPaintGreen;
    private Path path = new Path();
    private int[] location = new int[2];
    int tileSize = 80;
    boolean drawSquare = false;
    int h_global;
    int l_global;
    int init_x = 0;
    int init_y = 0;

    ArrayList<Point> arrayPoints = new ArrayList<Point>();

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

        drawPaintGreen = new Paint(Paint.DITHER_FLAG);
        drawPaintGreen.setAntiAlias(true);
        drawPaintGreen.setColor(Color.parseColor("#008000"));
        drawPaintGreen.setStyle(Paint.Style.FILL);
        drawPaintGreen.setStrokeJoin(Paint.Join.ROUND);
        drawPaintGreen.setStrokeWidth(3);
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

        for (int i = 0; i<arrayPoints.size(); i++) {
            if ((arrayPoints.get(i).x <= tileSize || arrayPoints.get(i).x >= (getRight()-tileSize )) || (arrayPoints.get(i).y <= tileSize || arrayPoints.get(i).y >= getBottom()-tileSize)) {
                canvas.drawRect(arrayPoints.get(i).x * tileSize, arrayPoints.get(i).y * tileSize, arrayPoints.get(i).x * tileSize + tileSize, arrayPoints.get(i).y * tileSize + tileSize, drawPaintGreen);
            }
        }

        if (drawSquare) {
            init_x = h_global;
            init_y = l_global;
            int i, j;
            i = Math.floorDiv(h_global, tileSize);
            j = Math.floorDiv(l_global, tileSize);
            canvas.drawRect( i * tileSize, j * tileSize, i * tileSize + tileSize, j * tileSize + tileSize, drawPaintGreen);
            arrayPoints.add(new Point(i, j));
        }

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
                    drawSquare = true;
                    h_global = h;
                    l_global = l;
                } else {
                    drawSquare = false;
                }

                break;
            default:
                return false;
        }
        invalidate();
        return true;
    }
}