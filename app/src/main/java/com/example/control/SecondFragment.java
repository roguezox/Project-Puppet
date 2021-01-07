package com.example.control;

import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SecondFragment extends Fragment {

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_second, container, false);
    }
    public static final Socket[] socket = new Socket[1];
    final ExecutorService test= Executors.newFixedThreadPool(1);

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final TimerTask[] task = new TimerTask[1];
        final Timer timer=new Timer();
        test.submit(new Runnable() {
            @Override
            public void run() {
                if(FirstFragment.socket[0].isConnected()){
                    try{
                        socket[0] = new Socket("10.147.17.234", 4443);
                        socket[0].setTcpNoDelay(true);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        final EditText cmd=(EditText) view.findViewById(R.id.cmdprmt);
        FloatingActionButton send=(FloatingActionButton) view.findViewById(R.id.send);
        final Toast executed=Toast.makeText(getActivity().getApplicationContext(),"Cmd Executed",Toast.LENGTH_SHORT);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                test.submit(new Runnable() {
                    @Override
                    public void run() {

                        String command=cmd.getText().toString();
                        System.out.println(command);

                        try {

                            System.out.println(socket[0].isConnected());
                            DataOutputStream dos=new DataOutputStream(new BufferedOutputStream(socket[0].getOutputStream()));
                            DataInputStream dis=new DataInputStream(new BufferedInputStream(socket[0].getInputStream()));
                            dos.writeUTF(command+"\n");
                            dos.flush();
                            String back=dis.readUTF();
                            if(back.equals("over\n")){
                                System.out.println("done");
                                executed.show();
                            }
                            else {
                                System.out.println("error");
                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                });
            }
        });



    }
    public static boolean isOnline() {
        boolean b = true;
        try{
            InetSocketAddress sa = new InetSocketAddress("10.147.17.234", 4443);
            Socket ss = new Socket();
            ss.connect(sa, 4000);
            ss.close();
        }catch(Exception e) {
            b = false;
        }
        return b;
    }
}