package com.example.collisionball;

import android.app.Application;
import android.content.Context;
import android.view.WindowManager;

public class Screen extends Application {

    public Context context;
    public static float width;
    public static float height;

    //相对标尺
    public static float hx;

    public void onCreate() {
        super.onCreate();
        context=getApplicationContext();
        WindowManager w= (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        width=w.getDefaultDisplay().getWidth();
        height=w.getDefaultDisplay().getHeight();
        //定义标尺:hx
        hx=width/108;
    }
}
