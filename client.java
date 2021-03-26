/*
    Note to self; find a good way to handle sockets when one closes (either by sending a goodbye message that starts o handle the closing)
    Also check how to well sinchornize the threads so that it doe snot leave you waiting till a new line
*/ 

import java.net.*;
import java.io.*;

class client{
    private static Socket sok;
    private static DataInputStream datain;
    private static DataOutputStream dataout;
    private static BufferedReader br ;
    private static String aux;
    private static String ans;
    private static Thread t;
    public static void main(String[] args)throws IOException{
        int resetCount = 0;
        System.out.println("Starting clientside socket...");
        do{
            try{
                sok = new Socket("localhost",25565);
                datain = new DataInputStream(sok.getInputStream());
                dataout = new DataOutputStream(sok.getOutputStream());
                br = new BufferedReader(new InputStreamReader(System.in));
                aux = ""; ans ="";
                System.out.println("Establishing connection...");
                t = new Thread(){
                    @Override
                    public void run(){
                        try{
                            do{
                                aux = datain.readUTF();
                                System.out.println("Server::> "+aux); 
                                System.out.print(">> ");
                            }while(!ans.equals("stop") && !aux.equals("stop"));
                            System.exit(0);
                        }catch(IOException e){
                            System.out.println("An ioerror ocurrued; cannot connect with server side");
                        }
                        
                    }
                };
                t.start();
                while(!ans.equals("stop") && !aux.equals("stop")){
                    System.out.print(">> ");
                    ans = br.readLine();
                    dataout.writeUTF(ans);
                    dataout.flush();
                    
                }
                
                datain.close();
                dataout.close();
                sok.close();
            }catch(Exception e){
                System.out.println("Connection lost with host trying to reset it\nRetrying.....");
                

            }
            resetCount++;
        }while(resetCount <= 3);
        if(resetCount >= 3){
            System.out.println("\n!!!!!!!!!!!!!!!!!\nFailed to restablish Connection");
        }else{
            System.out.println("Stopping the commnication.....");
        }
        
    }

}

