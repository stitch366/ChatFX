package chatfx;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Sydney
 */
public class ChatServer extends Thread{
    private ServerSocket server;
    private ArrayList<Client> clients = new ArrayList<Client>();
    private boolean running = true;
    
    public ChatServer(int socketno) throws IOException{
        this.server = new ServerSocket(socketno);
        setName("Server-Thread");
    }
    /**
     * @param args the command line arguments
     */
    public void end() throws IOException{
        running = false;
        server.close();
    }
    @Override 
    public void run(){
        Socket chan;
        Client client;
        while(running){
            try {
                 chan = server.accept();
                 if(chan != null){
                    client = new Client(chan);
                    clients.add(client);
                   (new Reciver(client)).start();
                 }
            }catch(SocketException ex){
                /*Exists purely bcause the end function needs to cause this Error to get the Thread to Stop waiting for a client After the Sever user leaves*/
            } catch (IOException ex) {
                Logger.getLogger(ChatServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public class Client {
        private Socket chan;
        private boolean open = true;
        private ObjectInputStream from;
        private ObjectOutputStream to;
        Client(Socket c) throws IOException{
            this.chan = c;
            this.to = new ObjectOutputStream(this.chan.getOutputStream());
            this.from = new ObjectInputStream(this.chan.getInputStream());
        }
        public void close(){
            this.open = false;
            try {
                chan.close();
            } catch (IOException ex) {
                Logger.getLogger(ChatServer.class.getName()).log(Level.SEVERE, null, ex);
            }
            clients.remove(this);
        }
        public void send(String msg) throws IOException{
            to.writeObject(msg);
        }
        public boolean isopen(){
            return this.open;
        }
        public String recive() throws IOException, ClassNotFoundException{
            return (String)from.readObject();
        }
    }
    public class Sender extends Thread{
        private String msg;
        public Sender(String m){
            this.msg =m;
            setName("Sender");
        }
        @Override
        public void run(){
            //System.out.println(this.msg);
            for(Client c: clients){
                try {
                    c.send(this.msg);
                } catch (IOException ex) {
                    c.close();
                }
            }
        }
        
        
    }
    public class Reciver extends Thread{
        private Client client;
        private String cname;
        private boolean flag = true;
        public Reciver(Client chan){
            this.client = chan;
            setName("Server-Reciver");
        }
        @Override
        public void run(){
            String msg;
            try {
                this.cname = client.recive();
            } catch (IOException ex) {
                Logger.getLogger(ChatServer.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ChatServer.class.getName()).log(Level.SEVERE, null, ex);
            }
            (new Sender(this.cname+" has joined the chat.")).start();
            while(this.flag){
                msg = "";
                try {
                    msg = client.recive();
                } catch (Exception ex) {
                    msg="";
                }
                if("end".equals(msg.toLowerCase())|| "".equals(msg)){
                    msg=this.cname+" has left the chat.";
                    this.flag = false;
                    client.close();
                }
                else{
                    msg = this.cname+": "+msg;
                }
                (new Sender(msg)).start();
            }
        }
    }
}
