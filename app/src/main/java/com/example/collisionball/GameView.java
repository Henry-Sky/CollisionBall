package com.example.collisionball;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class GameView {

    private static float width;
    private static float height;
    //相对标尺
    private static float hx;

    //相对游戏界面尺寸
    public static float gamewidth;
    public static float gameheight;

    public GameView(){
        width=Screen.width;
        height=Screen.height;
        hx=Screen.hx;
        gamewidth=width-hx*2;
        gameheight= (float) (1.8*(width-(hx*2)));
    }

    public void paint(Canvas canvas, Paint paint){
        paint.setColor(Color.GRAY);
        canvas.drawRect((float) (hx), (float) (hx), (float) (width-(hx)), (float) (gameheight+hx),paint);
    }

    public static float getleft(){
        return hx;
    }

    public static float gettop(){
        return hx;
    }

    public static float getright(){
        return width-(hx);
    }

    public static float getbottom(){
        return gameheight+hx;
    }
}
