import java.io.*;
import java.nio.Buffer;
import java.lang.*;
import java.net.*;
import jdk.net.Sockets;
import java.time.format.DateTimeFormatter;  
import java.time.LocalDateTime;

public class ServerThreadData {
    
    public Socket s;
    public DataInputStream datain;
    public DataOutputStream dataout;
    public boolean waitForMain = false;
    public  String aux;
    public  String clientName;
    public Process run = null;
    public boolean killThreads = false;
    public boolean stopService = false;
    public boolean connected = false;
    public String log;
    public BufferedWriter ptf;
    
    
    

    public synchronized void killProcess(){
        System.out.println("Start by killing threads and stopping sockets");
        killThreads = true;
        waitForMain = true;
        connected = false;
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
            connected = false;
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

    

    

    public synchronized void setupServerSocketService(Socket newSok){
        System.out.println("Starting Serverside socket...");
                try{
                    s = newSok;
                    //ss.setSoTimeout(3000);
                    clientName = s.getInetAddress().getHostName();
                    //s.setSoTimeout(3000);
                    datain = new DataInputStream(s.getInputStream());
                    dataout = new DataOutputStream(s.getOutputStream());
                    
                    aux = "";
                    waitForMain = false;
                    connected = true;
                    notifyAll();
                }catch(IOException e){
                    System.out.println("An error has occured when booting server socket");
                    
                    
                }
    }

    

    public synchronized void printToFile ( String content) throws IOException{
        
        
        ptf = new BufferedWriter(new FileWriter(log,true));
        ptf.write("\n"+content);
        ptf.close();
        
    }

    public String getDate(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
        LocalDateTime now = LocalDateTime.now();  
        return dtf.format(now);  
    }

    public synchronized void setLogFile(String fileName){
        log = fileName;
        
    }

}