package com.example.control;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class fragment_third extends Fragment {

    public fragment_third() throws JSchException {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_third, container, false);
    }
    JSch jsch=new JSch();
    final Session session=jsch.getSession("rogue","10.147.17.234",22);
    final ExecutorService sshrunner= Executors.newFixedThreadPool(1);
    public void onViewCreated(@NonNull View view,Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        final TextView textView=(TextView) view.findViewById(R.id.viewtext);

        textView.setMovementMethod(new ScrollingMovementMethod());
        final Properties config=new Properties();
        config.put("StrictHostKeyChecking", "no");
        final Toast sshconnected=Toast.makeText(getActivity().getApplicationContext(),"SSH Connected",Toast.LENGTH_SHORT);
        sshrunner.submit(new Runnable() {
            @Override
            public void run() {
                session.setPassword("passwordd");

                try {
                    session.setConfig(config);
                    session.setConfig("PreferredAuthentications", "password");
                    session.connect(5000);
                    System.out.println("connected "+session.isConnected());
                    if(session.isConnected()){
                        sshconnected.show();
                    }
                } catch (JSchException e) {
                    e.printStackTrace();
                }
            }
        });
        FloatingActionButton send=(FloatingActionButton) view.findViewById(R.id.sshsend);
        final FloatingActionButton file=(FloatingActionButton) view.findViewById(R.id.sshfile);
        final EditText cmd=(EditText) view.findViewById(R.id.sshcmd);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               sshrunner.submit(new Runnable() {
                   @Override
                   public void run() {
                       try {
                           ChannelExec exec=(ChannelExec) session.openChannel("exec");
                           String command=cmd.getText().toString();
                           cmd.setText("");
                           exec.setCommand(command);
                           exec.setErrStream(System.err);
                           InputStream in=exec.getInputStream();
                           exec.connect(8000);

                           byte[] tmp = new byte[1024];
                           while (true) {
                               while (in.available() > 0) {
                                   int i = in.read(tmp, 0, 1024);
                                   if (i < 0) break;
                                   textView.append(new String(tmp, 0, i));
                               }
                               if (exec.isClosed()) {
                                   if (in.available() > 0) continue;
                                   System.out.println("exit-status: "
                                           + exec.getExitStatus());
                                   break;
                               }
                               try {
                                   Thread.sleep(1000);
                               } catch (Exception ee) {
                               }
                           }


                       } catch (JSchException | IOException e) {
                           e.printStackTrace();
                       }
                   }
               });
            }
        });
        final Toast fileget=Toast.makeText(getActivity().getApplicationContext(),"Got File",Toast.LENGTH_SHORT);
        file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               sshrunner.submit(new Runnable() {
                   @Override
                   public void run() {
                       String both = cmd.getText().toString();
                       String toget = both;
                       String dest = "/storage/emulated/0/Documents/";
                       try {
                           ChannelSftp sftp=(ChannelSftp) session.openChannel("sftp");
                           String command = "scp -o -r StrictHostKeyChecking=no rogue@Rogue:" + toget + " " + dest;
                           sftp.connect(8000);
                           sftp.get(toget,dest);
                           fileget.show();
                               try {
                                   Thread.sleep(1000);
                               } catch (Exception ee) {
                               }
                           } catch (JSchException | SftpException jSchException) {
                           jSchException.printStackTrace();
                       }


                   }
               });
            }
        });

    }


}