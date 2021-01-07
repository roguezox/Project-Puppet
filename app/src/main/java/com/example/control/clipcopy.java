package com.example.control;

import android.app.Service;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
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

public class clipcopy extends Service {
    public clipcopy() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        ExecutorService clip= Executors.newFixedThreadPool(1);
        final Toast clipdone=Toast.makeText(getApplicationContext(),"Got Clipboard",Toast.LENGTH_SHORT);
        clip.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println(FirstFragment.socket[0].isConnected());
                    DataOutputStream dos=new DataOutputStream(new BufferedOutputStream(socketmain[0].getOutputStream()));
                    DataInputStream dis=new DataInputStream(new BufferedInputStream(socketmain[0].getInputStream()));
                    System.out.println("hello\n");
                    dos.writeUTF("send clipboard\n");
                    dos.flush();
                    String back=dis.readUTF();
                    ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText(back, back);
                    clipboard.setPrimaryClip(clip);
                    if(!back.equals(null)){
                        clipdone.show();
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
        final Toast clipdone=Toast.makeText(getApplicationContext(),"Got Clipboard",Toast.LENGTH_SHORT);
        clip.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println(FirstFragment.socket[0].isConnected());
                    DataOutputStream dos=new DataOutputStream(new BufferedOutputStream(socketmain[0].getOutputStream()));
                    DataInputStream dis=new DataInputStream(new BufferedInputStream(socketmain[0].getInputStream()));
                    System.out.println("hello\n");
                    dos.writeUTF("send clipboard\n");
                    dos.flush();
                    String back=dis.readUTF();
                    ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText(back, back);
                    clipboard.setPrimaryClip(clip);
                    if(!back.equals(null)){
                        clipdone.show();
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