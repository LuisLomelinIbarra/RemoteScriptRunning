/*
    Class: RunServer
    Description:
    This is the server side client for running a script remotely
    It will search for a client and will respond to ciertan commands
 */ 

import java.io.*;
import java.lang.*;
import java.net.*;

class RunServer{
    
   public static void main(String args[])throws IOException{
        ServerSocket ss = new ServerSocket(3333);
        Socket s = ss.accept();
        DataInputStream datain = new DataInputStream(s.getInputStream());
        DataOutputStream dataout = new DataOutputStream(s.getOutputStream());
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String aux = "", ans ="";
        System.out.println("Server side is now active!\nSocket is open\n----------------------------");
        while(!ans.equals("stop") && !aux.equals("stop")){
            aux = datain.readUTF();
            System.out.println("Client::> "+aux);
            if(aux.equals("run script")){
                runSubRutine("script_test.bat");
            }
            ans = br.readLine();
            dataout.writeUTF(ans);
            dataout.flush();
        }
        datain.close();
        dataout.close();
        ss.close();
    }


    public static void runSubRutine(String scriptFile){
        System.out.println(System.getProperty("user.dir"));
        Process run = null;
        //El /c le indica que corra el comando. Start al parecer es necesario
        //Ahopra hay que experimentar conthreads y sockests para probarlo
        String[] cmmd = {"cmd","/c","start" ,System.getProperty("user.dir")+"\\script_test.bat"};
        try{
            run =  Runtime.getRuntime().exec(cmmd,null,null);
            System.out.println(run.isAlive());
        }catch(IOException ex){
            System.out.println("Hubo un error");
        }
        System.out.println(run.isAlive());

    }

}