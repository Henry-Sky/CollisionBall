package com.example.collisionball;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

public class Ball {
    //坐标
    private float x;
    private float y;

    //运动属性
    private double speed;
    private double v_x;
    private double v_y;

    private float r;
    //相对标尺
    private static float hx;


    public Ball(float x,float y){
        hx=Screen.hx;
        this.x=x;
        this.y=y;
        this.r=hx*4;
        this.v_x=hx/2;
        this.v_y=hx/2;
        this.speed=2.5;
    }

    public void paint(Canvas canvas, Paint paint){
        paint.setColor(Color.YELLOW);
        canvas.drawCircle(this.x,this.y,r,paint);
    }

    public void move(Brick brick) {
        this.x= (float) (this.x+v_x*speed);
        this.y= (float) (this.y+v_y*speed);
        //碰撞检测
        ballhitbrick(brick);
        ballhitgameview();
        ingameview();
        Log.i("ball", "球的坐标:"+this.x+";"+this.y);
    }

    //碰撞检测
    private void ballhitgameview(){
        if(y<GameView.getbottom()&&y+r>GameView.getbottom()){
            v_y=-v_y;
        }
        else if(y>GameView.gettop()&&y-r<GameView.gettop()){
            v_y=-v_y;
        }
        else if(x<GameView.getright()&&x+r>GameView.getright()){
            v_x=-v_x;
        }
        else if(x>GameView.getleft()&&x-r<GameView.getleft()){
            v_x=-v_x;
        }
    }
    private void ballhitbrick(Brick brick){
        if(y<brick.gettop()&&y+r>brick.gettop()&&x>brick.getleft()&&x<brick.getright()){
            v_y=-v_y;
        }
        else if(x<brick.getleft()&&x+r>brick.getleft()&&y<brick.getbottom()&&y>brick.gettop()){
            v_x=-v_x;
        }
        else if(x> brick.getright()&&x-r<brick.getright()&&y<brick.getbottom()&&y>brick.gettop()){
            v_x=-v_x;
        }
        else if(y> brick.getbottom()&&y-r< brick.getbottom()&&x>brick.getleft()&&x<brick.getright()){
            v_y=-v_y;
        }
    }
    private void ingameview(){
        if(x<GameView.getleft()){
            x=GameView.getleft()+r;
            v_x=-v_x;
        }
        else if(x>GameView.getright()){
            x=GameView.getright()-r;
            v_x=-v_x;
        }
        else if(y<GameView.gettop()){
            y=GameView.gettop()+r;
            v_y=-v_y;
        }
        else if(y>GameView.getbottom()){
            y=GameView.getbottom()-r;
            v_y=-v_y;
        }
    }

    //获取属性
    public float getX() {
        return x;
    }
    public double getV_x() {
        return v_x;
    }
    public float getY() {
        return y;
    }
    public double getV_y() {
        return v_y;
    }
    public float getR(){
        return r;
    }

    //重置属性
    public void reset(float x,float y,double v_x,double v_y){
        this.x=x;
        this.y=y;
        this.v_x=v_x;
        this.v_y=v_y;
    }

    public  void speed(double speed){
        this.speed=speed;
    }
}
