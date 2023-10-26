package com.example.sockettest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ClientActivity extends AppCompatActivity {

    //基础属性
    private String ip;//为服务端ip
    private String port;
    private int PORT;
    private boolean flag;//用于判断按钮是否按下
    private String msg;

    //线程
    private ExecutorService clientpool=null;

    //通信类
    private Socket socket;

    //界面
    private Button clisend;
    private EditText clienttxt;
    private TextView clientview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);
        Intent intent = getIntent();
        ip=intent.getStringExtra("transIp");
        port=intent.getStringExtra("transPort");
        PORT=Integer.parseInt(port);
        Log.i("client", "传递IP:"+ip+"\n传递端口："+PORT);
        //配置界面
        clisend=(Button) findViewById(R.id.cliSend);
        clienttxt=(EditText)findViewById(R.id.clientTxt);
        clientview=(TextView)findViewById(R.id.clientView);
        //监听发送按钮
        clisend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                msg=clienttxt.getText().toString();
                flag=true;
            }
        });
        //创建线程
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                startClient();
            }
        },0);
    }

    public void startClient(){
                try{
                    Log.i("client", "ip:"+ip+";port:"+PORT);
                    socket = new Socket(ip,PORT);
                    Log.i("client", "连接成功");
                    //开始通信线程(发信息和收消息各一个线程)
                    clientpool=Executors.newCachedThreadPool();
                    clientpool.execute(new Getmsg(socket));
                    clientpool.execute(new Sendmsg(socket));
                } catch (Exception e) {
                    e.printStackTrace();
                }
    }

    class Getmsg implements Runnable {
        private Socket socket;
        private String receiveMsg;
        private BufferedReader in = null;

        private Getmsg(Socket socket) throws IOException {
            this.socket=socket;
            in = new BufferedReader(new InputStreamReader(socket.getInputStream(),"UTF-8"));
        }

        @Override
        public void run() {
            while (true) {                                   //循环接收、读取 Client 端发送过来的信息
                try {
                    if ((receiveMsg = in.readLine()) != null) {
                        clientview.setText(receiveMsg);
                        Log.i("client", "收到消息:" + receiveMsg);
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
            out = new PrintWriter(new BufferedWriter(new OutputStreamWriter( socket.getOutputStream(), "UTF-8")), true);
            out.println("成功连接服务器"+"（客户端发送）");
        }

        @Override
        public void run() {
            while(true){
                if(flag){
                    clientview.setText(msg);
                    out.println(msg);
                    flag=false;
                }

            }
        }
    }
}