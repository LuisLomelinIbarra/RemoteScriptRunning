/*
    Class: RunServer
    Description:
    This is the server side client for running a script remotely
    It will search for a client and will respond to ciertan commands
 */ 

import java.io.*;
import java.lang.*;
import java.net.*;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import jdk.net.Sockets;

public class RunServer {
    

   public static void main(String args[])throws IOException{
        System.out.println("Starting Serverside Client...\n**********************************");
        //The shared info between all threads are stored in this class
        ServerThreadData data = new ServerThreadData();
        
        System.out.println("Server side is now active!\n\n----------------------------");
        ExecutorService serverThreads = Executors.newFixedThreadPool(2);
        
        ServerSocket ss;
        Socket s;
        serverThreads.execute(new Thread(new SocketThread(data,2)));
        
        do{
            data.killThreads = false;

            ss = new ServerSocket(25565);
            
            s = ss.accept();
            data.setupServerSocketService(s);
            
            serverThreads.execute(new Thread(new SocketThread(data,1)));
            

            data.waitMainFunc();
            System.out.println("Closing input and output threads for restart....");
            data.safeClose();
            ss.close();
              
            System.out.println("Testing if run is alive");
            if(data.run != null && data.run.isAlive()){
                try{
                    data.run.waitFor();
                    System.out.println("Script finished");
                }catch(InterruptedException e){
                    System.out.println("Having trubles wating for the script");
                }
            }
            
        }while(!data.stopService);
        serverThreads.shutdownNow();
      
            System.out.println("Starting ending process");
            
        
    }


}