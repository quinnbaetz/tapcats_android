package com.mi.blockslide;

import com.mi.blockslide.model.Arrow;
import com.mi.blockslide.model.Block;
import com.mi.blockslide.model.Goal;
import com.mi.blockslide.model.Level;
import com.mi.blockslide.model.Space;
import com.mi.blockslide.views.ArrowView;
import com.mi.blockslide.views.BlockView;
import com.mi.blockslide.views.GoalView;

/**
 * Created by quinn on 7/28/14.
 */
public class GameController {

    private final BlockActivity activity;

    public GameController(Level level, BlockActivity activity){
        this.level = level;
        this.board = new Space[level.cols][level.rows];
        for(int x = 0; x < level.cols; x++) {
            for (int y = 0; y < level.rows; y++) {
                this.board[x][y] = new Space();
            }
        }
        this.activity = activity;
    }

    public Level level;
    public Space[][]  board;


    public void addBlock(BlockView blockView){

        Block block = blockView.block;
        Logger.Log(block.toString());
        Logger.Log(board.toString());
        Logger.Log(block.location.toString());
        board [block.location[0]][block.location[1]].blockView = blockView;
    }
    public void addArrow(ArrowView arrowView){
        Arrow arrow = arrowView.arrow;
        board [arrow.location[0]][arrow.location[1]].arrowView = arrowView;
    }
    public void addGoal(GoalView goalView){
        Goal goal = goalView.goal;

        board [goal.location[0]][goal.location[1]].goalView = goalView;
    }




    public boolean didWin(){
        for(Goal goal : level.goals){
            BlockView blockView = board [goal.location[0]][goal.location[1]].blockView;
            if(blockView == null || blockView.block.color != goal.color){
                return false;
            }
        }
        return true;

    }
    public boolean checkValid(BlockView blockView, int direction){
        if(blockView.block.path.length <= blockView.position){
            return false;
        }
        return blockView.block.path[blockView.position] == direction;
    }


    public boolean move(BlockView blockView, int direction){
        return move(blockView, direction, true);
    }

    public boolean move(BlockView blockView, int direction, boolean checkWin){

        boolean win = backendMove(blockView, direction, checkWin);
        blockView.stopAnimation();

        if(checkWin && !win){
            blockView.animateToPosition(false, true);
            return false;
        }
        if(checkWin && win){
            blockView.animateToPosition(true, false);
            return true;
        }

        blockView.animateToPosition(false, false);
        return false;

    }

    private boolean backendMove(BlockView blockView, int direction, boolean checkWin){
        boolean lose = false;
        if(!checkValid(blockView, direction)){
            lose = true;
        }

        int x = blockView.block.location[0];
        int y = blockView.block.location[1];
        int newX = x;
        int newY = y;
        switch(direction){
            case AppConsts.UP:
                newY--;
                break;
            case AppConsts.RIGHT:
                newX++;
                break;
            case AppConsts.DOWN:
                newY++;
                break;
            case AppConsts.LEFT:
                newX--;
                break;
        }




        if(newX >= 0 && newY >= 0 && newX < board.length && newY < board[newX].length) {
            if (board[newX][newY].blockView != null) {
                move(board[newX][newY].blockView, direction, false);
            }
            if (board[newX][newY].arrowView != null) {
                blockView.block.direction = board[newX][newY].arrowView.arrow.direction;
                blockView.invalidate();
            }

            board[newX][newY].blockView = blockView;
        }
        if(x >= 0 && y >= 0 && x < board.length && y < board[x].length) {
            board[x][y].blockView = null;
        }
        //if(board[newX][newY].goalView != null){

        //}


        blockView.block.location = new int[]{newX, newY};

        blockView.position++;

        if(lose){
            return false;
        }
        if(checkWin && didWin()){
            return true;
        }else{
            return false;
        }
    }

    public void lose(){
        activity.lose();
    }

    public void nextLevel(){
        activity.nextLevel();
    }



    public void showHint() {
        for(int x = 0; x < level.cols; x++) {
            for (int y = 0; y < level.rows; y++) {
                BlockView blockView = this.board[x][y].blockView;
                if( blockView != null && blockView.position < blockView.block.path.length &&
                        blockView.block.path[blockView.position] == blockView.block.direction) {
                    blockView.flash();
                }
            }
        }
    }
}
