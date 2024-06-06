package com.hfc.minesweeper.landmine.player;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.hfc.minesweeper.R;
import com.hfc.minesweeper.landmine.tools.MineManager;
import com.hfc.minesweeper.landmine.view.MineAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class AIPlayer {
    RecyclerView recyclerView ;
    int boardsize;
    int surplusMines;
    int totalMines;
    Activity activity;
    public AIPlayer(Activity activity) {
        boardsize = MineManager.getinstance().getM()* MineManager.getinstance().getN();
        surplusMines = totalMines = MineManager.getinstance().getMines();
        this.activity= activity;
    }

    public void radomClick(){
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Random random = new Random();
                View view = recyclerView.getChildAt(random.nextInt(boardsize));
                ImageView imageView = view.findViewById(R.id.iamge);
                imageView.performClick();
                startPlay();
            }
        });
    }
    Set<Integer> clickList = new HashSet<>();
    Set<Integer> longclickList = new HashSet<>();
    public void needclick(int i ,int j){
        int position =  MineManager.getinstance().getM()*i+j;
        clickList.add(position);
       // Log.e( "click: ","doclick" );
    }

    public void needclick(int position){
        clickList.add(position);
    }
    public void needlongClick(int i ,int j){
        int position =  MineManager.getinstance().getM()*i+j;
        longclickList.add(position);
        // Log.e( "click: ","doclick" );
    }
    public void needlongClick(int position){
        longclickList.add(position);
    }
    public int ijToPosition(int i, int j){
        int position =  MineManager.getinstance().getM()*i+j;
        return position;
    }

    public String intListToString(List<Integer> aroundList){
        Collections.sort(aroundList);
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < aroundList.size(); i++) {
            stringBuilder.append(aroundList.get(i)).append(",");
        }
        return stringBuilder.toString();
    }
    public void  doClickAndLongClick(){
        for(int a :clickList){
            clickAtPositon(a);
        }
        for(int a :longclickList){
            longClickAtPositon(a);
        }
        clickList.clear();
        longclickList.clear();
        Log.e( "doClickAndLongClick: ","do in main thread" );
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        startPlay();
    }
    public void longClickAtPositon(int position){
        View view = recyclerView.getChildAt(position);
        ImageView imageView = view.findViewById(R.id.iamge);
        imageView.performLongClick();
        surplusMines--;
    }
    public void clickAtPositon(int position){
        View view = recyclerView.getChildAt(position);
        ImageView imageView = view.findViewById(R.id.iamge);
        imageView.performClick();
    }
    public void startPlay() {
     MineAdapter.isAiPlay = true;
     new Thread(new Runnable() {
         @Override
         public void run() {
             if (surplusMines==0){
                 activity.runOnUiThread(new Runnable() {
                     @Override
                     public void run() {
                         AlertDialog.Builder alertDialog = new AlertDialog.Builder(((Context) activity));
                         alertDialog.setMessage(" u are win");
                         alertDialog.create().show();

                     }
                 });
             }
             if (geBlock11()==0){
                 radomClick();
                 Log.e( "run: ","2222222" );
             }else if (geBlock11()==-1){
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(((Context) activity));
                        alertDialog.setMessage(" u are failed");
                        alertDialog.create().show();

                    }
                });
             }else {
                 Log.e( "run: ","1111111" );
                 try {
                     checkOnce();
                 } catch (Exception e) {
                     e.printStackTrace();
                 }
             }
         }
     }).start();

    }

    /**
     * 简单算法
     * 在数字周围的空格等于数字那么就是雷flag
     * 在数字周围的flag数量等于数字那么周围未点击的格子就是没有雷的
     */
    public void checkOnce(){
        for (int i = 0; i <MineManager.getinstance().getN() ; i++) {
            for (int j = 0; j < MineManager.getinstance().getM(); j++) {
                if (numSurroundWithNormal(MineManager.getinstance().getDisplays(),i,j)){

                }
            }
        }
        float minProbability[]=new float[1];
        int minProbabilityPosition[]=new int[1];
        minProbability[0] =Float.MAX_VALUE;
        if (longclickList.size()==0&&clickList.size()==0){
           tankSolove(minProbability,minProbabilityPosition);
        }
        //如果还是没有选项则选择一个概率最小的点击
        if (longclickList.size()==0&&clickList.size()==0){
            Log.e("checkOnce: ","tankSolove55" +minProbabilityPosition[0]);
            needclick(minProbabilityPosition[0]);
        }
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                doClickAndLongClick();
            }
        });
    }
    Map<String,Integer> hashMap = new HashMap<>();

    /**
     * 算法原理：
     * 判断所有数字周围的空格中有几个雷
     * 假如6789这些格子中有3个雷
     * 如果678有3个雷那么9这个格子就能点击时没有雷的
     * 假如678有2个雷那么9这个格子就是雷flag
     * 如果67有一个雷那么89就是雷
     */
    private void tankSolove(float[] minProbability,int [] minProbabilityPosition) {
        int [][] displayes = MineManager.getinstance().getDisplays();

        for (int i = 0; i <MineManager.getinstance().getN() ; i++) {
            for (int j = 0; j < MineManager.getinstance().getM(); j++) {
              if (!(checkValidate(i,j)&&displayes[i][j] > 0&&displayes[i][j]<9)){
                 continue;
              }
              final int[] minesArround = {displayes[i][j]};
              final List<Integer> aroundList = new ArrayList<>();
              doArroundBlockLisener(i, j, new AroundLisener() {
                  @Override
                  public void doarroundLisener(int i1, int j1) {
                      //计算剩余地雷数
                      if (displayes[i1][j1]==12){
                          minesArround[0]--;
                      }else if (displayes[i1][j1]==0){
                          aroundList.add(ijToPosition(i1,j1));
                      }
                  }
              });
              if (aroundList.size()==0){
                  continue;
              }
              float min =((float) minesArround[0])/ ((float) aroundList.size());
              if (min!=0){
                  minProbability[0]= Math.min(min,minProbability[0]);
              }
              //  Log.e("tankSolove11: ", minesArround[0]/aroundList.size()+","+minProbability[0] );
              if (minProbability[0]==min){
                  minProbabilityPosition[0]=aroundList.get(0);
              }
              String aroundListStr = intListToString(aroundList);
              for(String key :hashMap.keySet()){
                  if (key.contains(aroundListStr)||aroundListStr.contains(key)){

                      if (Math.abs(minesArround[0]-hashMap.get(key))==0){
                          Log.e("tankSolove5: ",aroundListStr+","+key+","+displayes[i][j]);
                          if (key.length()>aroundListStr.length()){
                             String[] strs= key.replace(aroundListStr,"").split(",");
                              Log.e("tankSolove1: ",Arrays.toString(strs) );
                              for (int k = 0; k < strs.length; k++) {
                                  if (!strs[k].equals("")){
                                     needclick( Integer.valueOf(strs[k]));
                                  }
                              }
                          }else {
                              String[] strs= aroundListStr.replace(key,"").split(",");
                              Log.e("tankSolove2: ",Arrays.toString(strs) );
                              for (int k = 0; k < strs.length; k++) {
                                  if (!strs[k].equals("")){
                                      needclick( Integer.valueOf(strs[k]));
                                  }
                              }
                          }
                      }else {
                          int num = Math.abs(minesArround[0]-hashMap.get(key));
                          Log.e("tankSolove6: ",aroundListStr+","+key+","+displayes[i][j]);
                          if (key.length()>aroundListStr.length()){
                              String[] strs= key.replace(aroundListStr,"").split(",");
                              Log.e("tankSolove3: ",Arrays.toString(strs) );
                                if (num==strs.length){
                                    for (int k = 0; k < strs.length; k++) {
                                        if (!strs[k].equals("")){
                                            needlongClick( Integer.valueOf(strs[k]));
                                        }
                                    }
                                }
                          }else {
                              String[] strs= aroundListStr.replace(key,"").split(",");
                              Log.e("tankSolove4: ",Arrays.toString(strs) );
                              if (num==strs.length){
                                  for (int k = 0; k < strs.length; k++) {
                                      if (!strs[k].equals("")){
                                          needlongClick( Integer.valueOf(strs[k]));
                                      }
                                  }
                              }
                          }
                      }
                  }
              }
              hashMap.put(aroundListStr,minesArround[0]);
            }
        }
    }

    private int geBlock11() {
        int count =0 ;
        int [][]displays = MineManager.getinstance().getDisplays();
        for (int i = 0; i <MineManager.getinstance().getN() ; i++) {
            for (int j = 0; j < MineManager.getinstance().getM(); j++) {
                if (displays[i][j]==11){
                    count++;
                }
                if (displays[i][j]==10){
                   return -1;
                }
            }
        }
        return count;
    }
    private int getUnclickBlock() {
        int count =0 ;
        int [][]displays = MineManager.getinstance().getDisplays();
        for (int i = 0; i <MineManager.getinstance().getN() ; i++) {
            for (int j = 0; j < MineManager.getinstance().getM(); j++) {
               if (displays[i][j]==0){
                   count++;
               }
            }
        }
        return count;
    }

    public boolean numSurroundWithNormal(int [][] displays,int i,int j){
        int unclickNums[] = new int[1];
        int flagNums[] = new int[1];
        Log.e( "numSurroundWithNormal: ",i+","+j+","+displays[i][j] );
        if (checkValidate(i,j)&&displays[i][j] > 0&&displays[i][j]<9){
            int i1=i-1;
            int j1=j;
            doSurround(i1,j1,displays,unclickNums,flagNums);
            i1=i-1;
            j1=j-1;
            doSurround(i1,j1,displays,unclickNums,flagNums);
            i1=i-1;
            j1=j+1;
            doSurround(i1,j1,displays,unclickNums,flagNums);
            i1=i+1;
            j1=j;
            doSurround(i1,j1,displays,unclickNums,flagNums);
            i1= i+1;
            j1= j+1;
            doSurround(i1,j1,displays,unclickNums,flagNums);
            i1= i+1;
            j1= j-1;
            doSurround(i1,j1,displays,unclickNums,flagNums);
            i1= i;
            j1= j+1;
            doSurround(i1,j1,displays,unclickNums,flagNums);
            i1= i;
            j1= j-1;
            doSurround(i1,j1,displays,unclickNums,flagNums);

            doSurroundClick(i,j,displays,unclickNums,flagNums);
                if (unclickNums[0]>0){
                    return true;
                }

        }
        return false;
    }
    interface  AroundLisener{
       void doarroundLisener(int i1, int j1);
    }

    public void doArroundBlockLisener(int i, int j,AroundLisener aroundLisener){
        int i1=i-1;
        int j1=j;
        if (checkValidate(i1,j1))
        aroundLisener.doarroundLisener(i1,j1);
        i1=i-1;
        j1=j-1;
        if (checkValidate(i1,j1))
        aroundLisener.doarroundLisener(i1,j1);
        i1=i-1;
        j1=j+1;
        if (checkValidate(i1,j1))
        aroundLisener.doarroundLisener(i1,j1);
        i1=i+1;
        j1=j;
        if (checkValidate(i1,j1))
        aroundLisener.doarroundLisener(i1,j1);
        i1= i+1;
        j1= j+1;
        if (checkValidate(i1,j1))
        aroundLisener.doarroundLisener(i1,j1);
        i1= i+1;
        j1= j-1;
        if (checkValidate(i1,j1))
        aroundLisener.doarroundLisener(i1,j1);
        i1= i;
        j1= j+1;
        if (checkValidate(i1,j1))
        aroundLisener.doarroundLisener(i1,j1);
        i1= i;
        j1= j-1;
        if (checkValidate(i1,j1))
        aroundLisener.doarroundLisener(i1,j1);
    }
    private void doSurroundClick(int i, int j, int[][] displays, int[] unclickNums, int[] flagNums) {

        int displayValue = displays[i][j];
        //周围的未点击的和数字一样择标记为旗子
        if (displayValue ==unclickNums[0]){
            doArroundBlockLisener(i, j, new AroundLisener() {
                @Override
                public void doarroundLisener(int i1, int j1) {
                    if (displays[i1][j1]==0){
                        needlongClick(i1,j1);
                    }
                }
            });
        }
        if (flagNums[0]==displayValue&&unclickNums[0]>flagNums[0]){
            doArroundBlockLisener(i, j, new AroundLisener() {
                @Override
                public void doarroundLisener(int i1, int j1) {
                    if (displays[i1][j1]==0){
                        needclick(i1,j1);
                    }
                }
            });
        }
    }

    private void doSurround(int i1,int j1,int [][]displays, int[]unclickNums,int []flagNums) {
        if (checkValidate(i1,j1)){
            if (displays[i1][j1] == 0||displays[i1][j1] == 12){
                unclickNums[0]++;
                if (displays[i1][j1] == 12){
                    flagNums[0]++;
                }
            }
        }
    }

    private boolean checkValidate(int i, int j) {
        return MineManager.getinstance().checkValidate(i,j);
    }

    public void setView(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }
}
