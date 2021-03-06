package com.ethos.conwaysgameoflife;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by john.white on 2/17/16.
 */
public class Game extends Observable{

    Drawable mBlackCell;
    Drawable mWhiteCell;

    Context mContext;

    final int mRows;
    final int mColumns;
    final int mTotalCells;

    ArrayList<Cell> mCellList;
    ArrayList<Boolean> mNewStateList;
    ArrayList<Integer> mNeighborList;
    int totalFlips = 0;
    boolean reSeed = false;

    Handler gameLoop = new Handler();
    Runnable mGameRunnable = new Runnable() {
        @Override
        public void run() {
            long before = System.currentTimeMillis();
            initiateStateChange();
            long after = System.currentTimeMillis();
            Log.i("Game", "Speed: " + Long.toString(after - before));
        }
    };


    public Game(Context appContext, int rows, int columns, ArrayList<Cell> cells, Observer activity){

        mContext = appContext;
        this.mRows = rows;
        this.mColumns = columns;
        mTotalCells = rows * columns;
        mBlackCell = mContext.getDrawable(R.drawable.black_cell);
        mWhiteCell = mContext.getDrawable(R.drawable.white_cell);
        mCellList = cells;
        mNewStateList = new ArrayList<>();
        mNeighborList = new ArrayList<>(Collections.nCopies(mTotalCells, 0));
        for(int i = 0; i < mCellList.size(); i++){
            if(mCellList.get(i).isAlive()){
                mCellList.get(i).setBackground(mBlackCell);
            }else
                mCellList.get(i).setBackground(mWhiteCell);

            mNewStateList.add(false);
        }

        addObserver(activity);

    }

    private boolean isCellAlive(int location){
        return mCellList.get(location).isAlive();
    }

    private boolean verifyCellState(int location){

        if(isCellAlive(location)){
            if(mNeighborList.get(location) < 2) {
                mNewStateList.set(location, false);
                return true;
            }
            else if(mNeighborList.get(location) == 2 || mNeighborList.get(location) == 3) {
                mNewStateList.set(location, true);
                return false;
            }
            else if(mNeighborList.get(location) > 3) {
                mNewStateList.set(location, false);
                return true;
            }
        }else {
            if (mNeighborList.get(location) == 3) {
                mNewStateList.set(location, true);
                return true;
            } else {
                mNewStateList.set(location, false);
                return false;
            }
        }
        return false;
    }

    private void generateNewState(){
//        for (int i =0; i < mCellList.size(); i++) {
//            if(verifyCellState(i))
//                totalFlips++;
//        }

        for(int location = 0; location < mCellList.size(); location++){
            if(mCellList.get(location).isAlive()) {
                if (location == 0) {
                    mNeighborList.set(location + 1, (mNeighborList.get(location+1)+1));
                    mNeighborList.set(location + mColumns, (mNeighborList.get(location + mColumns)+1));
                    mNeighborList.set(location + mColumns + 1, (mNeighborList.get(location + mColumns + 1)+1));
                } else if (location == mColumns) {
                    mNeighborList.set(location - 1, mNeighborList.get(location - 1)+1);
                    mNeighborList.set(location + mColumns - 1, mNeighborList.get(location + mColumns - 1)+1);
                    mNeighborList.set(location + mColumns, mNeighborList.get(location + mColumns)+1);
                } else if (location - mColumns < 0) {
                    mNeighborList.set(location + 1, mNeighborList.get(location + 1)+1);
                    mNeighborList.set(location - 1, mNeighborList.get(location - 1)+1);
                    mNeighborList.set(location + mColumns - 1, mNeighborList.get(location + mColumns - 1)+1);
                    mNeighborList.set(location + mColumns, mNeighborList.get(location + mColumns)+1);
                    mNeighborList.set(location + mColumns + 1, mNeighborList.get(location + mColumns + 1)+1);
                } else if (location == mTotalCells - 1) {
                    mNeighborList.set( location - 1, mNeighborList.get(location - 1)+1);
                    mNeighborList.set( location - mColumns - 1, mNeighborList.get(location - mColumns - 1)+1);
                    mNeighborList.set( location - mColumns, mNeighborList.get(location - mColumns)+1);
                } else if (location == (mTotalCells - mColumns)) {
                    mNeighborList.set( location + 1, mNeighborList.get(location + 1)+1);
                    mNeighborList.set( location - mColumns, mNeighborList.get(location - mColumns)+1);
                    mNeighborList.set( location - mColumns + 1, mNeighborList.get(location - mColumns + 1)+1);
                } else if (location % mColumns == 0) {
                    mNeighborList.set( location + 1, mNeighborList.get(location + 1)+1);
                    mNeighborList.set( location + mColumns, mNeighborList.get(location + mColumns)+1);
                    mNeighborList.set( location + mColumns + 1, mNeighborList.get(location + mColumns + 1)+1);
                    mNeighborList.set( location - mColumns, mNeighborList.get(location - mColumns)+1);
                    mNeighborList.set( location - mColumns + 1, mNeighborList.get(location - mColumns + 1)+1);
                } else if (location % mColumns == mColumns - 1) {
                    mNeighborList.set( location - 1, mNeighborList.get(location - 1)+1);
                    mNeighborList.set( location + mColumns - 1, mNeighborList.get(location + mColumns - 1)+1);
                    mNeighborList.set( location + mColumns, mNeighborList.get(location + mColumns)+1);
                    mNeighborList.set( location - mColumns - 1, mNeighborList.get(location - mColumns - 1)+1);
                    mNeighborList.set( location - mColumns, mNeighborList.get(location - mColumns)+1);
                } else if (location + mColumns > mTotalCells) {
                    mNeighborList.set( location + 1, mNeighborList.get(location + 1)+1);
                    mNeighborList.set( location - 1, mNeighborList.get(location - 1)+1);
                    mNeighborList.set( location - mColumns - 1, mNeighborList.get(location - mColumns - 1)+1);
                    mNeighborList.set( location - mColumns, mNeighborList.get(location - mColumns)+1);
                    mNeighborList.set( location - mColumns + 1, mNeighborList.get(location - mColumns + 1)+1);
                } else {
                    mNeighborList.set( location + 1, mNeighborList.get(location + 1)+1);
                    mNeighborList.set( location - 1, mNeighborList.get(location - 1)+1);
                    mNeighborList.set( location + mColumns - 1, mNeighborList.get(location + mColumns - 1)+1);
                    mNeighborList.set( location + mColumns, mNeighborList.get(location + mColumns)+1);
                    mNeighborList.set( location + mColumns + 1, mNeighborList.get(location + mColumns + 1)+1);
                    mNeighborList.set( location - mColumns - 1, mNeighborList.get(location - mColumns - 1)+1);
                    mNeighborList.set( location - mColumns, mNeighborList.get(location - mColumns)+1);
                    mNeighborList.set( location - mColumns + 1, mNeighborList.get(location - mColumns + 1)+1);
                }
            }
        }
        for(int i = 0; i < mCellList.size(); i++){
            if(verifyCellState(i)){
                totalFlips ++;
            }
        }
        mNeighborList = new ArrayList<>(Collections.nCopies(mNeighborList.size(), 0));
    }

    private void propogateNewState(){
        for(int i = 0; i < mCellList.size(); i++){
            if(mNewStateList.get(i))
                turnOnCell(i);
            else
                turnOffCell(i);

            if(totalFlips < mCellList.size()*.02){
                reSeed = true;
                if((int)(Math.random()*100)%5 == 0)
                    turnOnCell(i);
            }
        }
        if(reSeed){
            Bundle b = new Bundle();
            b.putBoolean("counter", false);
            setChanged();
            notifyObservers(b);
            clearChanged();
            reSeed = false;
        }
        totalFlips = 0;
    }

    private void initiateStateChange(){
        generateNewState();
        propogateNewState();
        Bundle b = new Bundle();
        b.putBoolean("counter", true);
        setChanged();
        notifyObservers(b);
        clearChanged();
        gameLoop.postDelayed(mGameRunnable, 500);
    }

    private void turnOffCell(int location){
        mCellList.get(location).setAlive(false, mWhiteCell);
    }

    private void turnOnCell(int location){
        mCellList.get(location).setAlive(true, mBlackCell);
    }

    private int tallyLivingNeighbors(int location){
        int neighbors = 0;

        int[] neighborList;
        if(location == 0){
            neighborList = new int[3];
            neighborList[0] = location+1;
            neighborList[1] = location + mColumns;
            neighborList[2] = location + mColumns+1;
        }else if(location == mColumns){
            neighborList = new int[3];
            neighborList[0] = location-1;
            neighborList[1] = location + mColumns-1;
            neighborList[2] = location + mColumns;
        }else if(location - mColumns < 0) {
            neighborList = new int[5];
            neighborList[0] = location + 1;
            neighborList[1] = location - 1;
            neighborList[2] = location + mColumns - 1;
            neighborList[3] = location + mColumns;
            neighborList[4] = location + mColumns + 1;
        }else if(location == mTotalCells -1) {
            neighborList = new int[3];
            neighborList[0] = location - 1;
            neighborList[1] = location - mColumns - 1;
            neighborList[2] = location - mColumns;
        }else if(location == (mTotalCells - mColumns)){
            neighborList = new int[3];
            neighborList[0] = location + 1;
            neighborList[1] = location - mColumns;
            neighborList[2] = location - mColumns + 1;
        }else if(location % mColumns == 0) {
            neighborList = new int[5];
            neighborList[0] = location + 1;
            neighborList[1] = location + mColumns;
            neighborList[2] = location + mColumns + 1;
            neighborList[3] = location - mColumns;
            neighborList[4] = location - mColumns + 1;
        }else if(location % mColumns == mColumns-1){
            neighborList = new int[5];
            neighborList[0] = location - 1;
            neighborList[1] = location + mColumns - 1;
            neighborList[2] = location + mColumns;
            neighborList[3] = location - mColumns - 1;
            neighborList[4] = location - mColumns;
        }else if(location + mColumns > mTotalCells){
            neighborList = new int[5];
            neighborList[0] = location+1;
            neighborList[1] = location-1;
            neighborList[2] = location - mColumns-1;
            neighborList[3] = location - mColumns;
            neighborList[4] = location - mColumns+1;
        }else {
            neighborList = new int[8];
            neighborList[0] = location + 1;
            neighborList[1] = location - 1;
            neighborList[2] = location + mColumns - 1;
            neighborList[3] = location + mColumns;
            neighborList[4] = location + mColumns + 1;
            neighborList[5] = location - mColumns - 1;
            neighborList[6] = location - mColumns;
            neighborList[7] = location - mColumns + 1;
        }

        for(int i = 0; i < neighborList.length; i++){
            if(isCellAlive(neighborList[i]))
                neighbors += 1;
        }
        return neighbors;


    }

    public void startGame(){
        gameLoop.post(mGameRunnable);
    }

    public void stopGame(){
        gameLoop.removeCallbacksAndMessages(null);
    }

    public void tearDown(){
        deleteObservers();
    }

}
