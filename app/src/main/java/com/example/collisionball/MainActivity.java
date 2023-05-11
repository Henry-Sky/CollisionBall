package com.example.collisionball;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    //基本属性
    private String ip;
    public static int port=10000;
    private String cntpsd="";

    //控件
    private Button btn_create;
    private Button btn_join;
    private EditText edi_ip;
    private TextView viw_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //绑定控件
        btn_create=findViewById(R.id.btn_create);
        btn_join=findViewById(R.id.btn_join);
        edi_ip=findViewById(R.id.edi_ip);
        viw_name=findViewById(R.id.viw_name);
        //连接代码
        ip=getip();
        cntpsd=iptopsd(ip);
        edi_ip.setText(cntpsd);
        //创建游戏
        btn_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,ServerActivity.class);
                ip=getip();
                intent.putExtra("transIp",ip);
                Log.i("main", "传给服务端的ip："+ip);
                startActivity(intent);
                finish();
            }
        });
        //加入游戏
        btn_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,ClientActivity.class);
                //获取服务端ip
                ip=psdtoip(edi_ip.getText().toString());
                intent.putExtra("transIp",ip);
                Log.i("main", "传给客户端的ip："+ip);
                startActivity(intent);
                finish();
            }
        });
    }

    private void JumpServer(){

    }

    //连接代码和ip间互相转换
    public String iptopsd(String ip){
        String password="";
        for(int i=8;i<ip.length();i++)
        {
            if(ip.charAt(i)=='.'){
                password+='Z';
            }
            else{
                password+=ip.charAt(i);
            }
        }
        return password;
    }

    public String psdtoip(String psd){
        String ip="192.168.";
        for(int i=0;i<psd.length();i++){
            if(psd.charAt(i)=='Z'){
                ip+='.';
            }
            else{
                ip+=psd.charAt(i);
            }
        }
        return ip;
    }

    //获取ip地址
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