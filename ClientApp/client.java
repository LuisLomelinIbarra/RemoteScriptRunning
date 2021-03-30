/*
    Note to self; find a good way to handle sockets when one closes (either by sending a goodbye message that starts o handle the closing)
    Also check how to well sinchornize the threads so that it doe snot leave you waiting till a new line
*/ 

import java.net.*;
import java.io.*;



public class client implements Runnable{
    ClientThreadData data;
    int type;
    /*private static Thread t = new Thread(){
        @Override
        public void run(){
            try{
                do{
                    aux = datain.readUTF();
                    System.out.println("Server::> "+aux); 
                    System.out.print(">> ");
                }while(!ans.equals("stop") && !aux.equals("stop"));
                datain.close();
                dataout.close();
                sok.close();
                System.exit(0);
            }catch(IOException e){
                System.out.println("An ioerror ocurrued; cannot connect with server side");
            }
            
        }
    };;*/

    public client(int ty, ClientThreadData d){
        type = ty;
        data = d;
    }
    public void run(){
        switch(type){
            case 1:{
                String aux = "";
                do{
                    try{
                        
                            aux = data.datain.readUTF();
                            System.out.println("READ: "+aux);
                            if(aux.equals("stop")){
                                aux = data.closingConnection();
                                data.updateConsole(aux);
                                data.endThreads();
                            }else{
                                aux = "Server::> " + aux;
                            }
                            data.updateConsole(aux);
                            
                        
                        
                    }catch(IOException e){
                        System.out.println("An ioerror ocurrued; cannot connect with server side");
                    }
                }while(data.connected);
                System.out.println("Exited reading thread");
                break;
            }
            
        }
        
    }

   /* public static void main(String[] args)throws IOException{
        int resetCount = 0;
        System.out.println("Starting clientside socket...");
        do{
            try{
                sok = new Socket("localhost",25565);
                datain = new DataInputStream(sok.getInputStream());
                dataout = new DataOutputStream(sok.getOutputStream());
                
                
                System.out.println("Establishing connection...");
                
                
                
                
                datain.close();
                dataout.close();
                sok.close();
            }catch(IOException e){
                System.out.println("Connection lost with host trying to reset it\nRetrying.....");
                

            }
            resetCount++;
        }while(resetCount <= 3&&!ans.equals("stop") && !aux.equals("stop"));
        if(resetCount >= 3){
            System.out.println("\n!!!!!!!!!!!!!!!!!\nFailed to restablish Connection");
        }else{
            System.out.println("Stopping the commnication.....");
        }
        
    }*/

}

