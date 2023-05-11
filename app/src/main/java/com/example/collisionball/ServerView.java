package com.example.collisionball;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;

public class ServerView extends View implements Runnable{
    //基本属性
    Context context;
    Paint paint;
    Canvas canvas;
    public static float hx;

    //游戏界面
    public GameView gameview;
    private static float width;
    private static float height;

    //新的线程
    Thread thread=new Thread(this);

    //触摸位置
    private float touch_x;
    private float touch_y;

    //球
    public Ball ball;
    private float ball_x;
    private float ball_y;

    //我方的砖
    public Brick brick;
    private float brick_x;
    private float brick_y;

    //对方的砖
    public Brick opbrick;
    private float opbrick_x;
    private float opbrick_y;

    //通信
    private Socket socket;
    private ServerSocket serverSocket;
    boolean flag =false;

    public ServerView(Context context) {
        super(context);
        this.context=context;
        width=Screen.width;
        height=Screen.height;
        hx=Screen.hx;
        //屏幕默认触摸位置
        touch_x=width/2;
        touch_y=height/2;
        //初始化
        init();
        //开始线程
        thread.start();
    }

    private void init(){
        //实例化画布和画笔
        this.canvas=new Canvas();
        this.paint=new Paint();
        gameview=new GameView();
        //砖的实例化
        brick_x=(GameView.getleft()+GameView.getright())/2;
        brick_y=(GameView.getbottom()-hx*5);
        brick=new Brick(brick_x,brick_y,200,50);

        //对方的砖实例化
        opbrick_x=(GameView.getleft()+GameView.getright())/2;
        opbrick_y=(GameView.gettop()+hx*5);
        opbrick=new Brick(opbrick_x,opbrick_y,200,50);

        //球的实例化
        ball_x=(GameView.getleft()+GameView.getright())/2;
        ball_y=(GameView.gettop()+GameView.getbottom())/2;
        ball=new Ball(ball_x,ball_y);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        gameview.paint(canvas,paint);
        brick.paint(canvas,paint);
        opbrick.paint(canvas,paint);
        ball.paint(canvas,paint);
    }


    public boolean onTouchEvent(MotionEvent event) {
        touch_x=event.getX();
        touch_y=event.getY();
        //改变砖的位置
        brick.reset(touch_x);
        if(flag) {
            sendMes("brick|" + touch_x);
        }
        Log.i("brick", "X:"+X+",Y:"+Y);
        //重新绘图
        this.invalidate();
        return true;
    }

    public void serverstart(){
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    serverSocket = new ServerSocket(ServerActivity.port);
                    socket = serverSocket.accept();
                    Log.i("server", "服务端已完成通信");
                    sendMes("连接成功！(服务端发送)");
                    flag=true;
                    new MyThread().start();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        },0);
    }

    @Override
    public void run() {
        Log.i("client", "进入run函数");
        while (true) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            ball.move(brick);
            postInvalidate();
        }
    }

    class MyThread extends Thread {
        @Override
        public void run() {
            BufferedReader br = null;
            InputStreamReader isr = null;
            try {
                while (true) {
                    // 睡眠一段时间，不必每毫秒都执行
                    sleep(5);
                    isr = new InputStreamReader(socket.getInputStream());
                    br = new BufferedReader(isr);
                    // 是否接受到了消息
                    if (br.ready()) {
                        String cmd = br.readLine();
                        // 分割信息
                        String[] array = cmd.split("\\|");
                        String msgType = array[0];
                        switch (msgType) {
                            // 一定是服务器接受到这个消息
                            //Integer.parseInt([String])
                            case "ball":
                                ball.reset(Float.parseFloat(array[1]),Float.parseFloat(array[2]),
                                        Float.parseFloat(array[3]),Float.parseFloat(array[4]));
                                break;
                            case "brick":
                                opbrick.reset(Float.parseFloat(array[1]));
                                break;
                            default:
                                Log.i("client", "接收消息"+ msgType);
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void sendMes(final String s) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    PrintWriter pw;
                    pw = new PrintWriter(socket.getOutputStream());
                    pw.println(s);
                    pw.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
