
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
        //try{
            
                switch(type){
                    case 1:{
                        
                            
                            do{
                                
                                if(data.datain != null){
                                    try{
                                        data.aux = data.datain.readUTF();
                                        System.out.println("Client::> "+data.aux);
                                        if(data.aux.matches("\\s*run \\w+\\.\\w+")){
                                            
                                                data.dataout.writeUTF("Running the script;\n Closing the conection...");
                                                data.dataout.writeUTF("stop");
                                                String scriptFile = data.aux.substring(data.aux.indexOf('r'));
                                                scriptFile = scriptFile.substring(scriptFile.indexOf(' ')+1);
                                                runSubRutine(scriptFile);
                                                data.aux = "";
                                            
                                            
                                            data.closeConnection();
                                            
                                        }else{
                                            System.out.print(">> ");
                                        }
                                
                                    }catch(IOException e){
                                        System.out.println("An ioerror ocurrued");
                                        data.killProcess();
                                    }
                                }  
                            }while(!data.killThreads);
                            System.out.println("Closing inbound thread");
                            
                        break;
                    }
                    case 2:{
                        while(!data.killThreads ){
                        try{
                                //Read ans
                                data.readAns();
                                if(data.dataout != null){
                                    if(!data.waitForMain && data.dataout != null && !data.killThreads){
                                        data.dataout.writeUTF(data.ans);
                                        data.dataout.flush();
                                        data.ansUpdated =  false;
                                    }
                                }
                                
                            }catch(IOException e){
                                System.out.println("error a la hora de leer");
                            }
                            
                        }
                        System.out.println("Closing reading thread");
                        
                        break;
                    }

                    case 3:{
                        
                            String readC ="";
                            do{
                                try{
                                    System.out.print(">> ");
                                    readC = data.br.readLine();
                                    //System.out.println(data.waitForMain);
                                    if(!data.killThreads){
                                        if(readC.equals("stop")){
                                            System.out.println("Stopping the service");
                                            data.stopServer();
                                            System.exit(0);
                                        }else{
                                            data.updateAns(readC);
                                        }
                                    }
                                }catch(IOException e){
                                    System.out.println("Error in reading the console");
                                }
                            }while(!data.stopService);
                        
                        break;
                    }
                    default:{

                    }
                
                }
            
            
        /*}catch(IOException e){
            System.out.println("An ioerror ocurrued");
            data.killProcess();
        }*/
        
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