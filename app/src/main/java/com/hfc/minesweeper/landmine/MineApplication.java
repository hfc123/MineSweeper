package com.hfc.minesweeper.landmine;

import android.app.Application;

public class MineApplication extends Application {

    public static Application app;

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
    }
}
