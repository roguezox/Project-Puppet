package com.example.control;
import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Switch;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;




public class FirstFragment extends Fragment {
    Animation animopen;
    Animation animclose;
    Animation clipopen;
    Animation clipclose;







   public static final Socket[] socket = new Socket[1];
   public static final Socket[] socketmain=new Socket[1];
    FloatingActionButton main;
    FloatingActionButton clipboard;
    FloatingActionButton turnoff;
    FloatingActionButton mute;
    FloatingActionButton lock;
    final ExecutorService test= Executors.newFixedThreadPool(1);
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {


        return inflater.inflate(R.layout.fragment_first, container, false);
    }
    private boolean clicked=false;
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        createNotificationChannel();
        final BottomNavigationView view1=view.findViewById(R.id.bottomnav);

        final NotificationCompat.Builder builder=new NotificationCompat.Builder(getActivity(),"M_CH_ID")
                .setSmallIcon(R.mipmap.ic_convect)
                .setContentTitle("Controller")
                .setContentText("Connected to remote Host: 10.147.17.234")
                .setPriority(NotificationCompat.PRIORITY_HIGH);





        animopen= AnimationUtils.loadAnimation(getContext(),R.anim.mainbutton_open);
        animclose= AnimationUtils.loadAnimation(getContext(),R.anim.mainbutton_close);
        clipopen= AnimationUtils.loadAnimation(getContext(),R.anim.clipboard);
        clipclose= AnimationUtils.loadAnimation(getContext(),R.anim.clipboard_close);

        main=(FloatingActionButton) view.findViewById(R.id.mainbutton);
        clipboard=(FloatingActionButton) view.findViewById(R.id.clipboard);
        turnoff=(FloatingActionButton) view.findViewById(R.id.turnoff);
        mute=(FloatingActionButton) view.findViewById(R.id.mute);
        lock=(FloatingActionButton) view.findViewById(R.id.lock);
        @SuppressLint("UseSwitchCompatOrMaterialCode") final Switch connect=view.findViewById(R.id.connect);
        final Toast muted=Toast.makeText(getActivity().getApplicationContext(),"Muted",Toast.LENGTH_SHORT);
        final Toast locked=Toast.makeText(getActivity().getApplicationContext(),"locked",Toast.LENGTH_SHORT);
        final Toast clipdone=Toast.makeText(getActivity().getApplicationContext(),"Got Clipboard",Toast.LENGTH_SHORT);
        main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainclicked();
            }
        });

        clipboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                test.submit(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            System.out.println(socket[0].isConnected());
                            DataOutputStream dos=new DataOutputStream(new BufferedOutputStream(socketmain[0].getOutputStream()));
                            DataInputStream dis=new DataInputStream(new BufferedInputStream(socketmain[0].getInputStream()));
                            System.out.println("hello\n");
                            dos.writeUTF("send clipboard\n");
                            dos.flush();
                            String back=dis.readUTF();
                            ClipboardManager clipboard = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
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
            }
        });
        turnoff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               test.submit(new Runnable() {
                   @Override
                   public void run() {
                       try{
                           System.out.println(socket[0].isConnected());
                           DataOutputStream dos=new DataOutputStream(new BufferedOutputStream(socketmain[0].getOutputStream()));
                           DataInputStream dis=new DataInputStream(new BufferedInputStream(socketmain[0].getInputStream()));
                           System.out.println("hello\n");
                           dos.writeUTF("systemctl poweroff\n");
                           dos.flush();
                       } catch (IOException e) {
                           e.printStackTrace();
                       }
                   }
               });
            }
        });
        mute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                test.submit(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            System.out.println(socket[0].isConnected());
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

            }
        });
        lock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                test.submit(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            System.out.println(socket[0].isConnected());
                            DataOutputStream dos=new DataOutputStream(new BufferedOutputStream(socketmain[0].getOutputStream()));
                            DataInputStream dis=new DataInputStream(new BufferedInputStream(socketmain[0].getInputStream()));
                            dos.writeUTF("xdotool key Super+l\n");
                            dos.flush();
                            String back=dis.readUTF();
                            if(back.equals("done\n")){
                                locked.show();
                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                });
            }
        });
        final Toast toast=Toast.makeText(getActivity().getApplicationContext(),"Connected",Toast.LENGTH_SHORT);
        final Toast toastnot=Toast.makeText(getActivity().getApplicationContext(),"Host is Down",Toast.LENGTH_SHORT);
        Intent intent=new Intent(getActivity(),clipcopy.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent=PendingIntent.getService(getActivity(),0,intent,PendingIntent.FLAG_UPDATE_CURRENT);

        Intent intent2=new Intent(getActivity(), mutedd.class);
        intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent2=PendingIntent.getService(getActivity(),4,intent2,PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setOngoing(true);
        builder.addAction(R.mipmap.ic_convect,"Clipboard",pendingIntent);

        builder.addAction(R.mipmap.ic_convect,"Mute",pendingIntent2);


        final NotificationManager manager=(NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);

        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(connect.isChecked()){


                    test.submit(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                socket[0]=new Socket("10.147.17.234",1330);
                                socket[0].setTcpNoDelay(true);
                                socket[0].setKeepAlive(true);

                               System.out.println(socket[0].isConnected());
                                DataOutputStream dos=new DataOutputStream(new BufferedOutputStream(socket[0].getOutputStream()));
                                DataInputStream in=new DataInputStream(new BufferedInputStream(socket[0].getInputStream()));
                                BufferedReader reader=new BufferedReader(new InputStreamReader(System.in));
                                String message="text";

                                dos.writeUTF("hello\n");
                                dos.flush();
                                String enc=in.readUTF();

                                if(enc.equals("hi\n")){
                                    Thread.sleep(5000);
                                    System.out.println(enc);
                                    socketmain[0]=new Socket("10.147.17.234",4443);
                                    socketmain[0].setTcpNoDelay(true);
                                    socketmain[0].setKeepAlive(true);
                                    if(socketmain[0].isConnected()){
                                        manager.notify(1,builder.build());
                                    toast.show();}
                                }
                                else {
                                   toastnot.show();

                                }




                            } catch (IOException | InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    });


                }
                else {
                    test.submit(new Runnable() {
                        @Override
                        public void run() {
                            try {

                                DataOutputStream dos=new DataOutputStream(new BufferedOutputStream(socket[0].getOutputStream()));
                                DataInputStream dis=new DataInputStream(new BufferedInputStream(socket[0].getInputStream()));
                                dos.writeUTF("over\n");

                                dos.flush();



                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
        });
    }
    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.app_name);
            String description = getString(R.string.connected);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("M_CH_ID", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getActivity().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }



    public void mainclicked(){
        setvisib(clicked);
        setanim(clicked);
        clicked= !clicked;
    }
    @SuppressLint("RestrictedApi")
    public void setvisib(boolean bool){
        if(!bool){
            clipboard.setVisibility(View.VISIBLE);
            turnoff.setVisibility(View.VISIBLE);
            lock.setVisibility(View.VISIBLE);
            mute.setVisibility(View.VISIBLE);
        }
        else {
            clipboard.setVisibility(View.INVISIBLE);
            turnoff.setVisibility(View.INVISIBLE);
            lock.setVisibility(View.INVISIBLE);
            mute.setVisibility(View.INVISIBLE);
        }
    }
    public void setanim(boolean bool){
        if(!bool){
            main.startAnimation(animopen);
            lock.startAnimation(clipopen);
            mute.startAnimation(clipopen);
            turnoff.startAnimation(clipopen);
            clipboard.startAnimation(clipopen);
        }
        else {
            main.startAnimation(animclose);
            lock.startAnimation(clipclose);
            mute.startAnimation(clipclose);
            turnoff.startAnimation(clipclose);
            clipboard.startAnimation(clipclose);
        }
    }
}