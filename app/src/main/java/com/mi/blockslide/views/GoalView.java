package com.mi.blockslide.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.mi.blockslide.GameController;
import com.mi.blockslide.R;
import com.mi.blockslide.model.Goal;

/**
 * Created by quinn on 7/28/14.
 */
public class GoalView extends GamePieceView {

    private final Paint paint;
    private final Paint whitePaint;
    private final Context mContext;
    int color = 0;
    public GameController gc;
    public Goal goal;

    public GoalView(Context context) {
        this(context, null, 0);
        // TODO Auto-generated constructor stub
    }

    public GoalView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        // TODO Auto-generated constructor stub
    }

    public GoalView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        paint = new Paint();
        whitePaint = new Paint();
        whitePaint.setColor(Color.WHITE);
        // load_paint = new Paint();
        mContext = context;
    }


    public void setGamePieces(Goal goal, GameController gc){
        this.goal = goal;
        this.gc = gc;
            switch(this.goal.color){
            case 0:
                this.setImageDrawable(mContext.getResources().getDrawable(R.drawable.bluemouse));
                break;
            case 1:
                this.setImageDrawable(mContext.getResources().getDrawable(R.drawable.redmouse));
                break;
            case 2:
                this.setImageDrawable(mContext.getResources().getDrawable(R.drawable.greenmouse));
                break;



        }
    }

    @Override
    public void onMeasure(int widthSpec, int heightSpec){
        int parentWidth = MeasureSpec.getSize(widthSpec);
        int parentHeight = MeasureSpec.getSize(heightSpec);
        int width = parentWidth/gc.level.cols;
        int height = parentHeight/gc.level.rows;
        this.setMeasuredDimension(width, height);
        this.setX(goal.location[0]*width);
        this.setY(goal.location[1]*height);
    }

    @Override
    public void onAttachedToWindow(){

    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // canvas.drawRect(play_end, 0, load_end, height, load_paint);
    }

}
