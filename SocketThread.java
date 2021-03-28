
import java.net.*;
import java.io.*;

public class SocketThread implements Runnable{
    
    ServerThreadData data;
    int type;
    public SocketThread(ServerThreadData newD, int newType){
        this.data = newD;
        this.type = newType;
    }

    @Override
    public void run(){
        try{
            switch(type){
                case 1:{
                    do{
                        data.aux = data.datain.readUTF();
                        System.out.println("Client::> "+data.aux);
                        if(data.aux.equals("run script")){
                            data.dataout.writeUTF("Running the script;\n Closing the conection...");
                            data.dataout.writeUTF("stop");
                            runSubRutine("script_test.bat");
                            data.aux = "";
                            data.closeConnection();
                            
                        }else{
                            System.out.print(">> ");
                        }

                    }while(!data.ans.equals("stop"));
                    break;
                }
                case 2:{
                    while(!data.ans.equals("stop") ){
                
                        try{
                            System.out.print(">> ");
                            data.ans = data.br.readLine();
                            //System.out.println(data.waitForMain);
                            if(!data.waitForMain){
                                data.dataout.writeUTF(data.ans);
                                data.dataout.flush();
                            }
                        }catch(IOException e){
                            System.out.println("error a la hora de leer");
                        }
                        
                    }
                    break;
                }
                default:{

                }
            
            }
            
        }catch(IOException e){
            System.out.println("An ioerror ocurrued");
        }
        
    }
    
    //Function that handles the run of the script
    public void runSubRutine(String scriptFile){
        //System.out.println(System.getProperty("user.dir"));
        System.out.println("Running the script...");
        
        //El /c le indica que corra el comando. Start al parecer es necesario
        //Ahopra hay que experimentar conthreads y sockests para probarlo
        String[] cmmd = {"cmd","/c","start","/wait" ,System.getProperty("user.dir")+"\\"+scriptFile};
        try{
            data.run =  Runtime.getRuntime().exec(cmmd,null,null);
            
              
            
        }catch(IOException ex){
            System.out.println("Hubo un error");
        }
        

    }
}