package com.hfc.minesweeper.landmine.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.hfc.minesweeper.R;
import com.hfc.minesweeper.landmine.MineApplication;
import com.hfc.minesweeper.landmine.model.data.Block;
import com.hfc.minesweeper.landmine.model.data.Block2;
import com.hfc.minesweeper.landmine.model.interfaces.BitmapFactoryB;

public class BlockBitmapFactory implements BitmapFactoryB {

    private static BlockBitmapFactory instance;
    private static Bitmap[] bitmaps = new Bitmap[16];
    private static Bitmap bitmap;

    public static BlockBitmapFactory getInstance(){

        if (instance == null){
            instance = new BlockBitmapFactory();
            bitmap =BitmapFactory.decodeResource(MineApplication.app.getResources(),R.mipmap.mine_410);
        }

        return instance;
    }


    @Override
    public Bitmap getBitmap(Block block) {
        if (bitmaps[block.getIndex()] == null){
            bitmaps[block.getIndex()] = Bitmap.createBitmap(bitmap,0,
                    block.getIndex()*bitmap.getWidth(),bitmap.getWidth(),bitmap.getWidth());
        }
        return bitmaps[block.getIndex()];
    }

    @Override
    public Bitmap getBitmap(Block2 block) {
        if (bitmaps[block.getIndex()] == null){
            int index2=15-block.getIndex();
            if (index2>=0&&index2<=10||index2==11||index2==12||index2==13){

                int id = MineApplication.app.getResources().getIdentifier("mine_"+index2,
                        "mipmap",MineApplication.app.getPackageName());
                if (index2==10){
                    id =R.mipmap.mine;
                }
                if (index2==0){
                    id =R.drawable.mine_normol;
                }
                if (index2==11){
                    id =R.mipmap.mine_0;
                }
                if (index2==12){
                    id =R.mipmap.mine_flag;
                }
                if (index2==13){
                    id =R.mipmap.mine_flag2;
                }
                bitmaps[block.getIndex()]  =BitmapFactory.decodeResource(MineApplication.app.getResources(),id);

            }

           /* else {
                //裁剪有误用原图 ---- 丢弃的代码
                bitmaps[block.getIndex()] = Bitmap.createBitmap(bitmap, 0,
                        block.getIndex() * bitmap.getWidth(), bitmap.getWidth(), bitmap.getWidth());
            }*/
        }
        return bitmaps[block.getIndex()];
    }

    @Override
    public Drawable getDrawable(Block block) {
        return null;
    }
}
