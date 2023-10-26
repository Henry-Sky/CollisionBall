package com.example.sockettest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class MainActivity extends AppCompatActivity {

    private String ip;
    private String port="10000";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //获取输入框ip内容
        EditText editxt=(EditText)findViewById(R.id.editText);

        //服务端按钮
        Button serbtn=(Button) findViewById(R.id.serBtn);
        serbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,ServerActivity.class);
                ip = getip();//设置ip为服务端ip
                intent.putExtra("transIp",ip);
                intent.putExtra("transPort",port);
                startActivity(intent);
            }
        });

        //客户端按钮
        Button clibtn=(Button) findViewById(R.id.cliBtn);
        clibtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ip = editxt.getText().toString();
                if(!ip.equals("")){
                    Intent intent = new Intent(MainActivity.this,ClientActivity.class);
                    intent.putExtra("transIp",ip);
                    intent.putExtra("transPort",port);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(MainActivity.this,"输入ip地址不能为空！",Toast.LENGTH_SHORT).show();
                }
            }
        });

        //获取ip地址按钮
        Button ipBtn =(Button) findViewById(R.id.ipBtn);
        ipBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editxt.setText(getip());
            }
        });
    }

    public String getip(){
        String str="none";
        try{
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();){
                NetworkInterface face = en.nextElement();
                for (Enumeration<InetAddress> enAddr = face.getInetAddresses(); enAddr.hasMoreElements();){
                    InetAddress addr =enAddr.nextElement();
                    if(!addr.isLoopbackAddress()){
                        str = addr.getHostAddress();
                        if ("192".equals(str.substring(0, 3))) {
                            return str;
                        }
                    }
                }
            }
        }catch (SocketException e){
            e.printStackTrace();
        }
        return str;
    }
}