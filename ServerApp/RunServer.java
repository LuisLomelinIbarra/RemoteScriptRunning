/*
    Class: RunServer
    Description:
    This is the server side client for running a script remotely
    It will search for a client and will respond to ciertan commands
 */ 

import java.io.*;
import java.lang.*;
import java.net.*;
import java.nio.Buffer;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import jdk.net.Sockets;

public class RunServer {
   public static int port = 0;

   public static void main(String args[])throws IOException{
        System.out.println("Starting Serverside Client...\n**********************************");
        //The shared info between all threads are stored in this class
        ServerThreadData data = new ServerThreadData();
        
        System.out.println("Server side is now active!\n\n----------------------------");
        ExecutorService serverThreads = Executors.newFixedThreadPool(2);
          
        
        
        String logName = DateTimeFormatter.ISO_LOCAL_DATE.format(LocalDateTime.now())+" log.txt";
        ServerSocket ss;
        Socket s;
        data.setLogFile(logName);
        System.out.println(logName);
        serverThreads.execute(new Thread(new SocketThread(data,2)));
        if(readPortStatusFile()){
            System.out.println("ServerProperties file found!\nLoading properties");
        }
        do{
            data.killThreads = false;

            ss = new ServerSocket(port);
            
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


    public static boolean readPortStatusFile(){
        boolean foundFile = true;
        File target = new File("portProperties.txt");
        try{    
            if( target.isFile() && target.length() != 0){
                BufferedReader readFile = new BufferedReader(new FileReader(target));
                String sPort = readFile.readLine();
                if(sPort.matches("SERVERPORT=\\d{1,5}")){
                sPort = sPort.substring(sPort.indexOf('=')+1);
                
                    port = Integer.parseInt(sPort);
                    System.out.println("Setting port to: "+Integer.toString(port));
                }
            }else{
                port = 25565;
                BufferedWriter ptf = new BufferedWriter(new FileWriter("portProperties.txt",true));
                ptf.write("SERVERPORT="+Integer.toString(port));
                ptf.close();
                foundFile = false;
            }
        }catch(IOException e){

        }
        return foundFile;
    }

}