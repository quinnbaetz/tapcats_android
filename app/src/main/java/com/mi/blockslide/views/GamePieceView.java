package com.mi.blockslide.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import com.mi.blockslide.AppConsts;
import com.mi.blockslide.model.Level;

/**
 * Created by quinn on 7/28/14.
 */
public class GamePieceView extends ImageView {

    private final Paint paint;
    private final Paint blackPaint;


    private Level level;

    public GamePieceView(Context context) {
        this(context, null, 0);
        // TODO Auto-generated constructor stub
    }

    public GamePieceView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        // TODO Auto-generated constructor stub
    }

    public GamePieceView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        paint = new Paint();
        blackPaint = new Paint();
        blackPaint.setColor(Color.BLACK);
        // load_paint = new Paint();
    }

    public void drawArrow(Canvas canvas, int direction, Paint paint){
        Path path = new Path();
        switch (direction){
            case AppConsts.UP:
                break;
            case AppConsts.RIGHT:
                canvas.rotate(90, getWidth()/2, getHeight()/2);
                break;
            case AppConsts.DOWN:
                canvas.rotate(180, getWidth()/2, getHeight()/2);
                break;
            case AppConsts.LEFT:
                canvas.rotate(270, getWidth()/2, getHeight()/2);
                break;

        }

        path.moveTo(getWidth()/2, getHeight()/2-getHeight()/8);
        path.lineTo(getWidth()/2+getWidth()/8, getHeight()/2+getHeight()/8);
        path.lineTo(getWidth()/2-getWidth()/8, getHeight()/2+getHeight()/8);
        path.close();
        canvas.drawPath(path, paint);
    }
    public void drawArrow(Canvas canvas, int direction){
        drawArrow(canvas, direction, blackPaint);
    }

    @Override
    public void onMeasure(int widthSpec, int heightSpec){
        int parentWidth = MeasureSpec.getSize(widthSpec);
        int parentHeight = MeasureSpec.getSize(heightSpec);
        int width = parentWidth/level.cols;
        int height = parentHeight/level.rows;
        this.setMeasuredDimension(width, height);
    }

    @Override
    public void onAttachedToWindow(){

    }


}
