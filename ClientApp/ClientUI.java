import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.awt.event.KeyEvent;
import java.awt.event.KeyAdapter;


public class ClientUI extends JFrame {

    public JTextField sendMsg = new JTextField();
    public JTextArea console = new JTextArea();
    public JButton send;
    public JButton connect;
    public JButton startServer;
    public GridBagConstraints lim;
    public ExecutorService threads = Executors.newFixedThreadPool(2);
    public ClientThreadData data = new ClientThreadData();
    

    public ClientUI(){
        Toolkit tool = Toolkit.getDefaultToolkit();
        Dimension screenSize = tool.getScreenSize();
        setSize((int)screenSize.getWidth()/2,(int)screenSize.getHeight()/2);
        setMinimumSize(new Dimension((int)screenSize.getWidth()/2,(int)screenSize.getHeight()/2));
        setTitle("Remote Connect");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        JPanel display = new JPanel();
        getContentPane().add(display);
        display.setLayout(new BoxLayout(display, BoxLayout.Y_AXIS));
        //display.setLayout(new GridBagLayout());
        
        data = new ClientThreadData();

        //Define UI Components
        sendMsg = new JTextField(50);
        sendMsg.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if(data.connected){
                    String read = sendMsg.getText();
                    sendMsg.setText("");
                    
                    sendLogic(read);
                    
                }else{
                   writeToConsole("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\nPlease try connecting first before sending a message"); 
                }
            }
        });
        
        //Console display setup
        console = new JTextArea();
        console.setColumns(50);
        console.setRows(10);
        //console.setPreferredSize(new Dimension(50,5));
        console.setLineWrap(true);
        console.setWrapStyleWord(true);
        console.setBackground(Color.WHITE);
        console.setText("Hello! This is the client console, which allows you to run scripts from your computer to my server. \n - If you just wanna boot up the server press Start Server. \n - If you somehow lose connection just press Connect \n - You can send me messages with the textfield and button bellow");
        console.append("\n--------------------------------------------------------------------------------------------------------------------------------");

        JScrollPane pConsole = new JScrollPane(console);
        Dimension scrollDim = new Dimension(50,300);
        pConsole.setPreferredSize(scrollDim);
        console.setEditable(false);
        
        //Setup the buttons
        send = new JButton("Send!");
        send.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(data.connected){
                    String read = sendMsg.getText();
                    sendMsg.setText("");
                    
                    sendLogic(read);
                    
                }else{
                   writeToConsole("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\nPlease try connecting first before sending a message"); 
                }
            }

        });

        
        connect = new JButton("Re/Connect");
        connect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                connectToServer();
            }
        });

        startServer = new JButton("Start Server");
        startServer.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                
                connectToServer();
                if(data.connected){
                    writeToConsole("Sending starting script for server the current conection will be closed.\n After a few moments (Give it 1 to 5 min) please check if the server is running");
                    sendLogic("run run.bat");

                }else{
                    writeToConsole("The server may be on or unavailabe, check if it's alredy on or contact your admin");
                }
            }
        });
        //sendMsg.setText("   ");
        lim = new GridBagConstraints();
        lim.insets = new Insets(2,2,5,5);
        
        pConsole.setMinimumSize( scrollDim );
        //pConsole.add(console);

        display.add(pConsole);
        JPanel aux = new JPanel();
        aux.setLayout(new FlowLayout());
        aux.add(sendMsg);
        aux.add(send);
        JPanel aux2 = new JPanel();
        aux2.setLayout(new FlowLayout());
        aux2.add(startServer);
        aux2.add(connect);

        display.add(aux);
        display.add(aux2);
        
        
        
        
    }

    public static void main(String[] args){
        
        
        ClientUI cui = new ClientUI();
        cui.data.setClientUI(cui.console);
        
        cui.setVisible(true);

    }

    public void connectToServer(){
        if(data.connected){
            //data.closingConnection();
            data.closingConnection();
            
        }
        if(data.startConnextion("localhost", 25565)){
            writeToConsole("Established a succesful connection\n********************************************************************************************");
            threads.execute(new client(1,this.data));
            
        }else{
            writeToConsole("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\nFailed to establish connection please try again");
        }
    
    }

    public void sendLogic(String msg){
        
            
            if(data.connected){
                writeToConsole("me >> "+msg);
                try{
                    data.dataout.writeUTF(msg);
                    data.dataout.flush();
                }catch(IOException e){
                    writeToConsole("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\nEncountered an error while sending a message");
                    data.closingConnection();
                    data.endThreads();
                }
                if(msg.equals("stop")){
                    data.closingConnection();
                    data.endThreads();
                }
            } 
        
    }

    public void writeToConsole(String msg){
        console.append("\n"+msg);
        console.setCaretPosition(console.getDocument().getLength());
        repaint();
    }

    
     

}