package com.app.D1App;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;

/**
 * Created by kartikeya on 11/11/2018.
 */

public class Tiendas extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);
//        MultiDex.install(this);
    }
}
