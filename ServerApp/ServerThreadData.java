import java.io.*;
import java.lang.*;
import java.net.*;
import jdk.net.Sockets;

public class ServerThreadData {
    
    public Socket s;
    public DataInputStream datain;
    public DataOutputStream dataout;
    public boolean waitForMain = false;
    public  String aux;
    public  String ans;
    public Process run = null;
    public boolean killThreads = false;
    public boolean stopService = false;
    public boolean ansUpdated = false;
    public BufferedReader br ;
    
    public synchronized void closeConnection(){
        try{
            
                if(datain != null) datain.close();
                if(dataout != null) dataout.close();
                if(s != null)s.close();
                
                waitForMain = true;
            
            System.out.println("Closed the socket succesfully!\n-------------------------\n");
        }catch(IOException e){
            System.out.println("Problems closing socket");
        }
        while(waitForMain){
            try{
                wait();
            }catch(InterruptedException e){

            }
        }
    }

    public synchronized void killProcess(){
        System.out.println("Start by killing threads and stopping sockets");
        killThreads = true;
        waitForMain = true;
        ansUpdated = true;
        safeClose();
        notifyAll();
        //System.exit(0);
    }

    public synchronized void safeClose(){
        try{
            System.out.println("Closing Streams...");
            if(datain != null) datain.close();
            if(dataout != null) dataout.close();
            if(s != null)s.close();
            
        }catch(IOException e){
            System.out.println("aaaaaaaaa");
        }
    }

    public synchronized void stopServer(){
        stopService = true;
        killProcess();
    }
    
    public synchronized void waitMainFunc(){
        while(!waitForMain){
            try{
                wait();
            }catch(InterruptedException e){
                System.out.println("An error ocurred to sleeping main");
            }
        }
    }

    public synchronized void readAns(){
        while(!ansUpdated){
            try{
                wait();
            }catch(InterruptedException e){
                System.out.println("An error ocurred to sleeping main");
            }
        }
        
    }

    public synchronized void updateAns(String readC){
        ans = readC;
        ansUpdated = true;
        notifyAll();
    }

    public synchronized void setupServerSocketService(Socket newSok){
        System.out.println("Starting Serverside socket...");
                try{
                    s = newSok;
                    //ss.setSoTimeout(3000);
                    
                    //s.setSoTimeout(3000);
                    datain = new DataInputStream(s.getInputStream());
                    dataout = new DataOutputStream(s.getOutputStream());
                    
                    aux = ""; ans ="joined";
                    waitForMain = false;
                    notifyAll();
                }catch(IOException e){
                    System.out.println("An error has occured when booting server socket");
                    
                    
                }
    }

    public ServerThreadData(){
        ans = "";
        br = new BufferedReader(new InputStreamReader(System.in));
    }

}