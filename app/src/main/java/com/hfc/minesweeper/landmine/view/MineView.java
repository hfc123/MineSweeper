package com.hfc.minesweeper.landmine.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

import androidx.annotation.Nullable;

public class MineView extends ImageView {
    public MineView(Context context) {
        super(context);
        init();
    }

    private void init() {
        setOnClickListener(view -> {

        });
    }

    public MineView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MineView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public MineView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void setOnClickListener(@Nullable OnClickListener l) {
        super.setOnClickListener(l);
    }
}
