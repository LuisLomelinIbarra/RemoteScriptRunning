
import java.net.*;
import java.io.*;
import java.util.concurrent.TimeUnit;
public class SocketThread implements Runnable{
    public BufferedReader br; 
    ServerThreadData data;
    int type;
    public SocketThread(ServerThreadData newD, int newType){
        this.data = newD;
        this.type = newType;
        br = new BufferedReader(new InputStreamReader(System.in));
    }

    @Override
    public void run(){
 
            
                switch(type){
                    case 1:{
                        
                            
                            do{
                                
                                if(data.datain != null){
                                    try{
                                        data.aux = data.datain.readUTF();
                                        System.out.println("Client::> "+data.aux);
                                        data.printToFile(data.getDate()+" ::: "+data.clientName+"::> "+data.aux);
                                        if(data.aux.matches("\\s*run \\w+\\.\\w+")){
                                            
                                                data.dataout.writeUTF("Running the script;\n Closing the conection...");
                                                data.dataout.writeUTF("stop");
                                                String scriptFile = data.aux.substring(data.aux.indexOf('r'));
                                                scriptFile = scriptFile.substring(scriptFile.indexOf(' ')+1);
                                                runSubRutine(scriptFile);
                                                data.aux = "";
                                            
                                            
                                            data.safeClose();
                                            
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
                        
                            String readC ="";
                            do{
                                try{
                                    System.out.print(">> ");
                                    //System.out.println(data.waitForMain);
                                    readC = br.readLine();
                                    

                                    //System.out.println(data.waitForMain);
                                    
                                    if(readC.equals("stop")){
                                        System.out.println("Stopping the service");
                                        if(data.connected) data.dataout.writeUTF("stop");
                                        data.stopServer();
                                        System.exit(0);
                                    }else{
                                        if(data.connected){
                                            data.dataout.writeUTF(readC);
                                            data.dataout.flush();
                                            System.out.println("Sent to client!");
                                            System.out.println(":::::::: "+readC);
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