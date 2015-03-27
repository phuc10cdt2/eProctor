/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Client.Proctor;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.List;
import javafx.application.Platform;
import javafx.scene.control.Dialogs;
import javafx.scene.image.ImageView;
import util.Communicator;

/**
 *
 * @author phongnt
 */
public class pServer extends Thread{
    boolean listening = false;
    Communicator communicator;
    ExamViewController examViewController;
    private int port;
    public pServer(int port){
        this.port=port;
    }
    @Override
    public void run(){
        listening = true;
        try{
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Listening on port "+port);
            System.out.println(examViewController);
            
            while(listening){
                List<ImageView> imageView = examViewController.getImageView();
                ImageView screen = imageView.get(0);
                ImageView webcam = imageView.get(1);
                Thread t = new pIdentityThread(serverSocket.accept(),communicator);
                examViewController.setComm(t);
                t.start();
                System.out.println("1 student connected");
               new pServerThread(serverSocket.accept(), screen).start();
               new pServerThread(serverSocket.accept(), webcam).start();
            }
        }
        catch(IOException e){                            
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    Dialogs.showErrorDialog(null, "Could not make a connection");
                }	
            });
        }
    }
    
    public void setController(ExamViewController examViewController){
        this.examViewController = examViewController;
    }
    
    public void setCommunicator(Communicator communicator){
        this.communicator=communicator;
    }
    
}
