/*
    Class: RunServer
    Description:
    This is the server side client for running a script remotely
    It will search for a client and will respond to ciertan commands
 */ 

import java.io.*;
import java.lang.*;
import java.net.*;

import jdk.net.Sockets;

class RunServer{
    private static ServerSocket ss;
    private static Socket s;
    private static DataInputStream datain;
    private static DataOutputStream dataout;
    private static BufferedReader br ;
    private static String aux;
    private static String ans;
    private static boolean isScriptDone = true;
    private static Process run = null;


    private static Thread t= new Thread(){
        @Override
        public void run(){
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
            
        }
    };

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
        System.out.println("Starting Serverside socket...");
        ss = new ServerSocket(25565);
        s = ss.accept();
        datain = new DataInputStream(s.getInputStream());
        dataout = new DataOutputStream(s.getOutputStream());
         br = new BufferedReader(new InputStreamReader(System.in));
        aux = ""; ans ="";
        System.out.println("Server side is now active!\nSocket is open\n----------------------------");
    
        t.start();
        while(!ans.equals("stop") && !aux.equals("stop")){
            
            
            System.out.print(">> ");
            ans = br.readLine();
            dataout.writeUTF(ans);
            dataout.flush();
        }
        datain.close();
        dataout.close();
        ss.close();
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