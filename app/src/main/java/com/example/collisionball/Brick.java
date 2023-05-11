package com.example.collisionball;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Brick {
    private float x;
    private float y;

    private float l;
    private float h;

    //private boolean overleft=false;
    //private boolean overright=false;

    public Brick(float x,float y,float l,float h){
        this.x=x;
        this.y=y;
        this.l=l;
        this.h=h;
    }

    public void paint(Canvas canvas, Paint paint){
        paint.setColor(Color.BLUE);
        canvas.drawRect(getleft(),gettop(),getright(),getbottom(),paint);
    }

    //获取属性
    public float getleft(){
        return x-l/2;
    }

    public float gettop(){
        return y-h/2;
    }

    public float getright(){
        return x+l/2;
    }

    public float getbottom(){
        return y+h/2;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    //重置属性
    public void reset(float x){
        this.x=x;
    }
}
