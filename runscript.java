import java.io.*;
import java.lang.*;
public class runscript{
    
    public static void main(String args[]){
        System.out.println(System.getProperty("user.dir"));
        Process run = null;
        //El /c le indica que corra el comando. Start al parecer es necesario
        //Ahopra hay que experimentar conthreads y sockests para probarlo
        String[] cmmd = {"cmd","/c","start" ,System.getProperty("user.dir")+"\\script_test.sh"};
        try{
            run =  Runtime.getRuntime().exec(cmmd,null,null);
            System.out.println(run.isAlive());
        }catch(IOException ex){
            System.out.println("Hubo un error");
        }
        System.out.println(run.isAlive());
    }

}