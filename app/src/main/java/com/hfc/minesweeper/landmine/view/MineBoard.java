package com.hfc.minesweeper.landmine.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MineBoard extends RecyclerView {
    public MineBoard(Context context) {
        super(context);
    }

    public MineBoard(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MineBoard(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void init(Context context){
        setLayoutManager(new GridLayoutManager(context,10));

    }
}
