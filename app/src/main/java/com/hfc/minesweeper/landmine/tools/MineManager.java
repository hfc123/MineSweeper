package com.hfc.minesweeper.landmine.tools;

import android.util.Log;
import android.view.View;

import java.security.InvalidKeyException;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class MineManager {
    static MineManager mineManager;
    int n;
    int m;
    int mines;
    int level = 1;

    int[][] mineses = new int[n][m];
    int[][] displays = new int[n][m];
    public static MineManager getinstance() {

        if (mineManager==null){
            mineManager = new MineManager();
        }

        return mineManager;
    }

    public int getLevel() {
        return level;
    }
    /**
     * 1. 初级 (Beginner) level 1
     * 网格尺寸: 9 x 9
     * 地雷数量: 10
     * 2. 中级 (Intermediate) level 2
     * 网格尺寸: 16 x 16
     * 地雷数量: 40
     * 3. 高级 (Expert) level 3
     * 网格尺寸: 30 x 16
     * 地雷数量: 99
     */
    public void setLevel(int level) throws InvalidKeyException {
        this.level = level;
        switch (level){
            case 1:
                setMines(9,9,10);
                break;
            case 2:
                setMines(16,16,40);
                break;
            case 3:
                setMines(30,16,99);
                break;
            default:
                throw new InvalidKeyException("error type");
        }
    }
    //10设置为地雷
    public int[][] createMinesData() {

        Random random = new Random();
        //生成的雷用set集合收集防止重复
        Set<String> sets = new HashSet<>();
        //随机生成雷
        while (sets.size()< mines){
          int n1 = random.nextInt(n);
          int m1 = random.nextInt(m);
          StringBuilder stringBuilder = new StringBuilder();
          stringBuilder.append(n1).append(m1);
            if (sets.add(stringBuilder.toString())){
                mineses[n1][m1] = 10;
            }
        }
        //计算雷旁边的数字o(1)遍历一遍
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                int arroundMines = 0;
                if (checkValidate(i,j)&&mineses[i][j] == 10){
                    continue;
                }
                if (checkValidate(i-1,j)&&mineses[i-1][j] == 10){
                    arroundMines++;
                }
                if (checkValidate(i-1,j-1)&&mineses[i-1][j-1] == 10){
                    arroundMines++;
                }
                if (checkValidate(i-1,j+1)&&mineses[i-1][j+1] == 10){
                    arroundMines++;
                }
                if (checkValidate(i+1,j)&&mineses[i+1][j] == 10){
                    arroundMines++;
                }
                if (checkValidate(i+1,j+1)&&mineses[i+1][j+1] == 10){
                    arroundMines++;
                }
                if (checkValidate(i+1,j-1)&&mineses[i+1][j-1] == 10){
                    arroundMines++;
                }
                if (checkValidate(i,j+1)&&mineses[i][j+1] == 10){
                    arroundMines++;
                }
                if (checkValidate(i,j-1)&&mineses[i][j-1] == 10){
                    arroundMines++;
                }
                mineses[i][j] = arroundMines;
            }
        }
        return mineses;
    }

    public boolean checkValidate(int i, int j) {
        if (i<0||j<0){
            return false;
        }
        if (i>=n||j>=m){
            return false;
        }
        return true;
    }

    public int[][] createDisplayData() {

        return displays = new int[n][m];
    }

    private void setMines(int n, int m,int mineSize) {
        this.n = n;
        this.m = m;
        this.mines = mineSize;
        mineses = new int[n][m];
        displays = new int[n][m];
    }

    public void ondisplayClick(int i, int j, View.OnClickListener onClickListener){

        if (checkValidate(i,j)&&mineses[i][j]==0&&displays[i][j]==0){
            Log.e("ondisplayClick: ", i+","+j+","+displays[i][j]+","+mineses[i][j]);
            displays[i][j] = 11;
            onClickListener.onClick(null);
            ondisplayClick( i,  j+1,  onClickListener);
            ondisplayClick( i,  j-1,  onClickListener);
            ondisplayClick( i+1,  j-1,  onClickListener);
            ondisplayClick( i+1,  j,  onClickListener);
            ondisplayClick( i-1,  j-1,  onClickListener);
            ondisplayClick( i-1,  j,  onClickListener);
            ondisplayClick( i-1,  j+1,  onClickListener);
            ondisplayClick( i+1,  j+1,  onClickListener);
        }else if (checkValidate(i,j)&&mineses[i][j]!=0){
            displays[i][j]=mineses[i][j];
            onClickListener.onClick(null);
        }
    }

    public int getN() {
        return n;
    }

    public void setN(int n) {
        this.n = n;
    }

    public int getM() {
        return m;
    }

    public void setM(int m) {
        this.m = m;
    }

    public int[][] getMineses() {
        return mineses;
    }

    public void setMineses(int[][] mineses) {
        this.mineses = mineses;
    }

    public int[][] getDisplays() {
        return displays;
    }

    public void setDisplays(int[][] displays) {
        this.displays = displays;
    }

    public int getMines() {
        return mines;
    }

    public void setMines(int mines) {
        this.mines = mines;
    }
}
