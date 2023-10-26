package com.example.sockettest;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerActivity extends AppCompatActivity {

    //基本属性
    private String ip;//为服务端ip
    private String port;
    private int PORT;
    private boolean flag;//用于判断按钮是否按下
    private String msg;

    //线程
    private ExecutorService serverpool=null;

    //通信类
    private ServerSocket serverSocket;
    private Socket socket;

    //界面
    private TextView serverview;
    private EditText servertxt;
    private Button sersend;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server);
        Intent intent = getIntent();
        ip=intent.getStringExtra("transIp");
        port=intent.getStringExtra("transPort");
        PORT=Integer.parseInt(port);
        Log.i("server", "传递IP:"+ip+"\n传递端口："+PORT);
        //配置界面
        sersend=(Button)findViewById(R.id.serSend);
        servertxt=(EditText)findViewById(R.id.serverTxt);
        serverview=(TextView)findViewById(R.id.serverView);
        //监听发送按钮
        sersend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                msg=servertxt.getText().toString();
                flag=true;
            }
        });
        //创建线程
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                startServer();
            }
        },0);
    }

    public void startServer(){
                try{
                    Log.i("server", "ip:"+ip+";port:"+PORT);
                    serverSocket = new ServerSocket(PORT);
                    socket = serverSocket.accept();
                    Log.i("server", "连接成功!");
                    //开始通信线程(发信息和收消息各一个线程)
                    serverpool=Executors.newCachedThreadPool();
                    serverpool.execute(new Getmsg(socket));
                    serverpool.execute(new Sendmsg(socket));
                } catch (Exception e) {
                    e.printStackTrace();
                }
    }
    class Getmsg implements Runnable {
        private Socket socket;
        private String receiveMsg;
        private String sendMsg;
        private BufferedReader in = null;

        private Getmsg(Socket socket) throws IOException {
            serverview=(TextView)findViewById(R.id.serverView);
            Log.i("server", "准备通信");
            this.socket=socket;
            in = new BufferedReader(new InputStreamReader(socket.getInputStream(),"UTF-8"));
        }

        @Override
        public void run() {
            Log.i("server", "等待接收消息");
            while (true) {
                try {
                    if((receiveMsg=in.readLine())!=null){
                        serverview.setText(receiveMsg);
                        Log.i("server", "收到消息:"+receiveMsg);
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    class Sendmsg implements Runnable{
        private Socket socket;
        private PrintWriter out = null;

        private Sendmsg(Socket socket) throws IOException {
            this.socket=socket;
            //初始化输出流
            out = new PrintWriter(new BufferedWriter(new OutputStreamWriter( socket.getOutputStream(), "UTF-8")), true);
            out.println("成功连接服务器"+"（服务器发送）");
            Log.i("server", "已发送连接消息");
        }
        //发送输出流
        @Override
        public void run() {
            while(true){
                if(flag){
                    serverview.setText(msg);
                    out.println(msg);
                    flag=false;
                }
            }
        }
    }
}