package com.hfc.minesweeper.landmine.model.interfaces;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import com.hfc.minesweeper.landmine.model.data.Block;
import com.hfc.minesweeper.landmine.model.data.Block2;

public interface BitmapFactoryB {

    Bitmap getBitmap(Block block);

    Bitmap getBitmap(Block2 block);

    Drawable getDrawable(Block block);

}
