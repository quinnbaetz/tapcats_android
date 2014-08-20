package com.mi.blockslide.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;

import com.mi.blockslide.AppConsts;
import com.mi.blockslide.GameController;
import com.mi.blockslide.R;
import com.mi.blockslide.model.Arrow;

/**
 * Created by quinn on 7/28/14.
 */
public class ArrowView extends GamePieceView {

    private final Paint paint;
    private final Paint blackPaint;
    int color = 0;
    public Arrow arrow;
    private int insideRectangleOffset = 10;
    GameController gc;

    public ArrowView(Context context) {
        this(context, null, 0);
        // TODO Auto-generated constructor stub
    }

    public ArrowView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        // TODO Auto-generated constructor stub
    }

    public ArrowView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        paint = new Paint();
        blackPaint = new Paint();
        blackPaint.setColor(Color.BLACK);

        this.setImageDrawable(context.getResources().getDrawable(R.drawable.fish));
        // load_paint = new Paint();
    }


    public void setGamePieces(Arrow arrow, GameController gc){
        this.arrow = arrow;
        this.gc = gc;

    }

    @Override
    public void onMeasure(int widthSpec, int heightSpec){
        int parentWidth = MeasureSpec.getSize(widthSpec);
        int parentHeight = MeasureSpec.getSize(heightSpec);
        int width = parentWidth/gc.level.cols;
        int height = parentHeight/gc.level.rows;
        this.setMeasuredDimension(width, height);
        this.setX(arrow.location[0]*width);
        this.setY(arrow.location[1]*height);
    }

    @Override
    public void onAttachedToWindow(){

    }


    @Override
    protected void onDraw(Canvas canvas) {

        //drawArrow(canvas, arrow.direction);

        Path path = new Path();
        switch (arrow.direction){
            case AppConsts.UP:
                canvas.rotate(270, getWidth()/2, getHeight()/2);

                break;
            case AppConsts.RIGHT:
                break;
            case AppConsts.DOWN:
                canvas.rotate(90, getWidth()/2, getHeight()/2);
                break;
            case AppConsts.LEFT:
                canvas.rotate(180, getWidth()/2, getHeight()/2);
                break;

        }

        super.onDraw(canvas);

        // canvas.drawRect(play_end, 0, load_end, height, load_paint);
    }

}
