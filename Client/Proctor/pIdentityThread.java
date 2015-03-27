/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Client.Proctor;

import ApplicationLayer.Message;
import DatabaseEntity.ExamRecord;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import util.BaseCommunicator;
import util.Communicator;

/**
 *
 * @author phongnt
 */
public class pIdentityThread extends Thread{
    Socket socket;
    Communicator serverComm;
    ObjectOutputStream out;
    ObjectInputStream in;
    public pIdentityThread(Socket socket, Communicator serverComm){
        this.socket = socket;
        this.serverComm = serverComm;
        try{
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
        }
        catch(IOException e){
            //
        }
    }
    @Override
    public void run(){
        Communicator comm= new BaseCommunicator(in, out);
        System.out.println("created communicator");
        Message msg = comm.getMessage();
        System.out.println("receive std identity");
        
        System.out.println("sent to to server");
        comm.sendMessage(msg);
        System.out.println("sent back to student");
        while(true)
            try{
            Thread.sleep(4000);
            }
            catch(InterruptedException e){
            ExamRecord examRecord = (ExamRecord)msg.getContent().get(0);
            serverComm.sendMessage(msg);
        }
    }
}
