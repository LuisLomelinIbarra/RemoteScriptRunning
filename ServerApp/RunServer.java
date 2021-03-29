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

class RunServer /*implements Runnable*/{
    
    
    private static boolean isFirstRun = true;
    private static Process run = null;
    
    /*public void run(){
        try{
            do{
                aux = datain.readUTF();
                System.out.println("Client::> "+aux);
                if(aux.equals("run script")){
                    runSubRutine("script_test.bat");
                }
                System.out.print(">> ");
            }while(!ans.equals("stop") && !aux.equals("stop"));
            System.exit(0);
        }catch(IOException e){
            System.out.println("An ioerror ocurrued");
        }
        
    }*/

    
    /*private static Thread isScriptAlive = new Thread(){
        @Override
        public void run(){
            //Check if the script that is beign run is done
            while(!isScriptDone){
                isScriptDone = !run.isAlive();
            };
            System.out.println("----------------------------------\nfinished runing the script!!");
        }
    };*/

   public static void main(String args[])throws IOException{
        System.out.println("Starting Serverside Client...\n**********************************");
        //The shared info between all threads are stored in this class
        ServerThreadData data = new ServerThreadData();
        //Create the thread that handles inbound information
        //Thread tin = new Thread(new SocketThread(data,1));
        //Create Thread that handles outbound information 
        //Thread tout = new Thread(new SocketThread(data,2));
        System.out.println("Server side is now active!\n\n----------------------------");
        ExecutorService serverThreads = Executors.newFixedThreadPool(5);
        
        //tout.start();
        ServerSocket ss;
        Socket s;
        serverThreads.execute(new Thread(new SocketThread(data,3)));
        do{
            data.killThreads = false;
            data.ansUpdated = false;
            
            ss = new ServerSocket(25565);
            //s.setSoTimeout(timeout);
            s = ss.accept();
            data.setupServerSocketService(s);
            serverThreads.execute(new Thread(new SocketThread(data,1)));
            serverThreads.execute(new Thread(new SocketThread(data,2)));

            data.waitMainFunc();
            ss.close();
            data.safeClose();
            data.ansUpdated = true;
            
            
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
       // try{
            System.out.println("Starting ending process");
            
            /*tout.interrupt();
            //tin.interrupt();
            tout.join();
            System.out.println("Joined tout");
            tin.interrupt();
            tin.join();
            System.out.println("Joined tin");*
        }catch(InterruptedException e){
            System.out.println("Failed to join the communication threads");
        }*/
    }


    public static void runSubRutine(String scriptFile){
        //System.out.println(System.getProperty("user.dir"));
        System.out.println("Running the script...");
        
        //El /c le indica que corra el comando. Start al parecer es necesario
        //Ahopra hay que experimentar conthreads y sockests para probarlo
        String[] cmmd = {"cmd","/c","start","/wait" ,System.getProperty("user.dir")+"\\"+scriptFile};
        try{
            run =  Runtime.getRuntime().exec(cmmd,null,null);
            run.waitFor();
            System.out.println("--------------\nFinished running the script");
            
        }catch(IOException ex){
            System.out.println("Hubo un error");
        }catch (InterruptedException e){
            System.out.println("Hubo un error");
        }
        

    }

    

}