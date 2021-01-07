package com.example.control;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.jcraft.jsch.JSchException;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.PublicKey;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView bottom=findViewById(R.id.bottomnav);
         final MenuItem  item=bottom.getMenu().findItem(1);

        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

        final int[] created = {0};
        getSupportFragmentManager().beginTransaction().replace(R.id.constraintLayout,new FirstFragment(),"first").commit();
        bottom.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment selected=null;
                final Fragment first=getSupportFragmentManager().findFragmentByTag("first");
                final Fragment second=getSupportFragmentManager().findFragmentByTag("second");
                final Fragment third=getSupportFragmentManager().findFragmentByTag("third");
                switch(menuItem.getItemId()){

                    case R.id.nav_home:


                        selected=new FirstFragment();
                        if(first==null){
                            System.out.println("replaced\n");
                            getSupportFragmentManager().beginTransaction().setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out).hide(second).commit();
                            getSupportFragmentManager().beginTransaction().setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out).add(R.id.constraintLayout,selected,"first").commit();
                        }
                        else {
                            System.out.println("done\n");
                            if(third!=null){
                            getSupportFragmentManager().beginTransaction().setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out).hide(third).commit();}
                            if(second!=null){
                             getSupportFragmentManager().beginTransaction().setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out).hide(second).commit();}
                            getSupportFragmentManager().beginTransaction().setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out).show(first).commit();

                        }

                        break;

                    case R.id.nav_command:


                        selected=new SecondFragment();


                                if (second == null) {

                                    System.out.println("replaced\n");

                                    getSupportFragmentManager().beginTransaction().setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out).add(R.id.constraintLayout, selected, "second").commit();

                                } else {
                                    System.out.println("done\n");
                                    getSupportFragmentManager().beginTransaction().setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out).hide(first).commit();
                                    if (third != null) {
                                        getSupportFragmentManager().beginTransaction().setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out).hide(third).commit();
                                    }
                                    getSupportFragmentManager().beginTransaction().setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out).show(second).commit();


                            }

                        break;

                    case R.id.nav_ssh:
                        try {
                            selected=new fragment_third();
                        } catch (JSchException e) {
                            e.printStackTrace();
                        }
                        if(third==null){
                            System.out.println("replaced\n");
                            getSupportFragmentManager().beginTransaction().setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out).add(R.id.constraintLayout,selected,"third").commit();

                        }
                        else {
                            System.out.println("done\n");
                            getSupportFragmentManager().beginTransaction().setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out).hide(first).commit();
                           if(second!=null){
                            getSupportFragmentManager().beginTransaction().setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out).hide(second).commit();}
                            getSupportFragmentManager().beginTransaction().setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out).show(third).commit();

                        }
                }



                return true;
            }
        });


    }





}