/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cardsagainsthumanity;

import java.io.*;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author thoma_000
 */
public class ComClient implements Runnable{
    private Socket socket               =null;
    private Thread thread               =null;
    private DataInputStream input       =null;
    private DataOutputStream output     =null;
    private ComClientThread client      =null;
    
    public ComClient(String serverName, int serverPort) throws IOException{
        socket = new Socket(serverName, serverPort);
                start();
    }
        
    public void sendGameRequest(String status,String oauth){
        try {
            output.writeUTF(status+oauth);
            output.flush();
        } catch (IOException ex) {
            Logger.getLogger(ComClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void sendCardUpdate(String status, String oauth, String cardNum){
        try {
            output.writeUTF(status+oauth+":CardNum:"+cardNum);
            output.flush();
        } catch (IOException ex) {
            Logger.getLogger(ComClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    public void handle(String msg){
        if(msg.equals("terminatedCommunication")){
            System.out.println("Connection is terminated");
            stop();
        }else{
            System.out.println(msg);
        }
    }
    
    public void start() throws IOException{
        input = new DataInputStream(System.in);
        output = new DataOutputStream(socket.getOutputStream());
        if(thread==null){
            client = new ComClientThread(this,socket);
            thread = new Thread((Runnable) this);
            thread.start();
        }
    }
    
    public void stop(){
        if(thread!=null){
            thread.stop();
            thread = null;
        }
        try{
            if(input!=null) input.close();
            if(output!=null) output.close();
            if(socket!=null) socket.close();
        }catch(IOException ioe){
            client.close();
            client.stop();
        }
    }

    @Override
    public void run() {
        while(thread!=null){
            try{
                output.writeUTF(input.readLine());
                output.flush();
            }catch(IOException e){
                stop();
            }
        }
    }
    
    
}
