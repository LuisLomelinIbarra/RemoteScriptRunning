
import java.io.*;
import java.lang.*;
import java.net.*;

import javax.swing.JTextArea;

public class ClientThreadData {
    public Socket s;
    public DataInputStream datain;
    public DataOutputStream dataout;
    //public boolean threadKill = false;
    public boolean connected = false;

    public String aux;
    public String ans;
    public JTextArea ui;

    public synchronized boolean startConnextion(String add, int port){
        try{
            s =  new Socket(add,port);
            datain = new DataInputStream(s.getInputStream());
            dataout = new DataOutputStream(s.getOutputStream());
            connected = true;
        }catch(IOException e ){
            System.out.println("Error in connecting");
            connected = false;
            return false;
        }
        return true;
    }

    public synchronized String closingConnection(){
        String res = "Succesfully closed the conection with the handler app...";
        try{
           if(datain != null) datain.close();
           if(dataout != null) dataout.close();
           if(s != null) s.close();
           connected = false;
           
        }catch(IOException e){
            res = "An error occured while closing the connection";
        }
        return res;
    }

    public synchronized void updateConsole(String msg){
      
        ui.append("\n"+msg);
        ui.setCaretPosition(ui.getDocument().getLength());
    } 

    public synchronized void setClientUI(JTextArea t){
        ui = t;
    }

    public synchronized void endThreads(){
        connected = false;
        notifyAll();
    }


}