//This is the helper class

package com.company;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    public  static void main(String[] args) throws IOException {
        ExecutorService serverstart= Executors.newFixedThreadPool(2);
        ServerSocket servsocket=new ServerSocket(1330);
        Socket socket= servsocket.accept();
        DataInputStream dis=new DataInputStream(new BufferedInputStream(socket.getInputStream()));
        System.out.print("This is maintainer helper class\n");

        boolean connectivity=connectivity(socket);
        final Process p  = Runtime.getRuntime().exec
                ("java -jar /home/rogue/Documents/control/control.jar");
        if(connectivity){
            serverstart.submit(new Runnable() {
                @Override
                public void run() {
                    while(true){
                        String terminator= null;
                        try {
                            terminator = dis.readUTF();
                            System.out.print(terminator);

                            if (terminator.equals("over\n")) {
                                p.destroy();
                                servsocket.close();
                                socket.close();
                                servermaintainer.main(args);




                                break;
                            }
                        }
                        catch (IOException e) {
                            e.printStackTrace();
                        }
                    }


                }
            });
            serverstart.submit(new Runnable() {
                @Override
                public void run() {

                    try {
                        String line;

                        BufferedReader input =
                                new BufferedReader
                                        (new InputStreamReader(p.getInputStream()));
                        while ((line = input.readLine()) != null) {
                            System.out.println(line);
                        }
                        input.close();
                    }
                    catch (Exception err) {
                        err.printStackTrace();
                    }
                }
            });


        }
    }
    public static boolean connectivity(Socket socket) throws IOException {
        DataInputStream dis=new DataInputStream(new BufferedInputStream(socket.getInputStream()));
        DataOutputStream dos=new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
        String connectinit=dis.readUTF();
        if(connectinit.equals("hello\n")){
            dos.writeUTF("hi\n");
            dos.flush();
        }

        return true;
    }

}
