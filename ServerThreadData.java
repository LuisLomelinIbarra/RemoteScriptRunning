import java.io.*;
import java.lang.*;
import java.net.*;
import jdk.net.Sockets;

public class ServerThreadData {
    public ServerSocket ss;
    public Socket s;
    public DataInputStream datain;
    public DataOutputStream dataout;
    public boolean waitForMain = false;
    public  String aux;
    public  String ans;
    public Process run = null;
    public boolean needReset = false;
    public BufferedReader br ;
    public synchronized void closeConnection(){
        try{
            datain.close();
            dataout.close();
            s.close();
            ss.close();
            needReset = true;
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

    public synchronized void setupServerSocketService(){
        System.out.println("Starting Serverside socket...");
                try{
                    ss = new ServerSocket(25565);
                    s = ss.accept();
                    datain = new DataInputStream(s.getInputStream());
                    dataout = new DataOutputStream(s.getOutputStream());
                    
                    aux = ""; ans ="joined";
                    waitForMain = false;
                    needReset = false;
                    notifyAll();
                }catch(IOException e){
                    System.out.println("An error has occured when booting server socket");
                }
    }

    public ServerThreadData(){
        /*System.out.println("Starting Serverside socket...");
        try{
            ss = new ServerSocket(25565);
            s = ss.accept();
            datain = new DataInputStream(s.getInputStream());
            dataout = new DataOutputStream(s.getOutputStream());
            
            aux = ""; ans ="joined";
            waitForMain = true;
            
        }catch(IOException e){
            System.out.println("An error has occured when booting server socket");
        }*/
        br = new BufferedReader(new InputStreamReader(System.in));
    }

}