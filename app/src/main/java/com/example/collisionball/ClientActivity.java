package com.example.collisionball;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

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

    //基本属性
    public static int port=MainActivity.port;
    public static String ip;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);
        Intent intent = getIntent();
        ip=intent.getStringExtra("transIp");
        Log.i("client", "客户端获取ip:"+ip);
        ClientView clientView = new ClientView(this);
        setContentView(clientView);
        clientView.clientstart();
    }
}