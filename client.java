import java.net.*;
import java.io.*;

class client{
    public static void main(String[] args)throws IOException{
        Socket sok = new Socket("localhost",3333);
        DataInputStream datain = new DataInputStream(sok.getInputStream());
        DataOutputStream dataout = new DataOutputStream(sok.getOutputStream());
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String aux = "", ans ="";
        while(!ans.equals("stop") && !aux.equals("stop")){
            
            ans = br.readLine();
            dataout.writeUTF(ans);
            dataout.flush();
            aux = datain.readUTF();
            System.out.println("Server::> "+aux);
        }
        datain.close();
        dataout.close();
        sok.close();
    }

}

