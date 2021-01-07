package com.example.control;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.example.control.FirstFragment.socketmain;

public class mutedd extends Service {
    public mutedd() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        ExecutorService clip= Executors.newFixedThreadPool(1);
        final Toast muted=Toast.makeText(getApplicationContext(),"Muted",Toast.LENGTH_SHORT);
        clip.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println(FirstFragment.socket[0].isConnected());
                    DataOutputStream dos=new DataOutputStream(new BufferedOutputStream(socketmain[0].getOutputStream()));
                    DataInputStream dis=new DataInputStream(new BufferedInputStream(socketmain[0].getInputStream()));
                    System.out.println("hello\n");
                    dos.writeUTF("amixer -q -D pulse sset Master toggle\n");
                    dos.flush();
                    String back=dis.readUTF();
                    if(back.equals("done\n")){
                        muted.show();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        throw new UnsupportedOperationException("Not yet implemented");
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Let it continue running until it is stopped.
        ExecutorService clip= Executors.newFixedThreadPool(1);
        final Toast muted=Toast.makeText(getApplicationContext(),"Muted",Toast.LENGTH_SHORT);
        clip.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println(FirstFragment.socket[0].isConnected());
                    DataOutputStream dos=new DataOutputStream(new BufferedOutputStream(socketmain[0].getOutputStream()));
                    DataInputStream dis=new DataInputStream(new BufferedInputStream(socketmain[0].getInputStream()));
                    System.out.println("hello\n");
                    dos.writeUTF("amixer -q -D pulse sset Master toggle\n");
                    dos.flush();
                    String back=dis.readUTF();
                    if(back.equals("done\n")){
                        muted.show();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}