 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Client.Proctor;

import ApplicationLayer.SerializedImage;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javafx.application.Platform;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.Dialogs;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import util.BaseCommunicator;
import util.Communicator;

/**
 *
 * @author phongnt
 */
public class pServerThread extends Thread{
    private Socket socket = null;
    private ImageView screen;
    public pServerThread(Socket socket, ImageView screen){
        this.socket=socket;
        this.screen=screen;
    }
    
    @Override
    public void run() {
        try{
            System.out.println("Student connected");
            ObjectOutputStream out= new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in=new ObjectInputStream(socket.getInputStream());
            final Communicator comm = new BaseCommunicator(in, out);
            javafx.scene.image.Image screen_img;
            javafx.scene.image.Image webcam_img;
            while(true){
            	SerializedImage si = (SerializedImage)comm.getObject();
                System.out.println("received image");
                final BufferedImage image_bi = si.getScreen();
                Platform.runLater(new Runnable() {
                        @Override
                        public void run(){
                            
                            if(image_bi!=null){
                           Image screen_img = SwingFXUtils.toFXImage(image_bi, null);
                           screen.setImage(screen_img); 
                        }
                    }
                });
            }        }
        catch(IOException e){
            System.out.println("connection terminated");
//            Platform.runLater(new Runnable() {
//                @Override
//                public void run() {
//                    Dialogs.showErrorDialog(null, "Could not make a connection");
//                }	
//            });            
        }
        finally{
            try{
                System.out.println("student disconnected");
                Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                           screen.setImage(null);
                        }	
                    });  
                
                socket.close();
            }
            catch(IOException e){
                Dialogs.showErrorDialog(null, "Error closing socket");
            }
        }
       // Update/Query the FX classes here
    }

    
//    @Override
//    public void run(){
//        try{
//            System.out.println("Student connected");
//            ObjectInputStream in=new ObjectInputStream(socket.getInputStream());
//            ObjectOutputStream out= new ObjectOutputStream(socket.getOutputStream());
//            Communicator comm = new BaseCommunicator(in, out);
//            javafx.scene.image.Image screen_img;
//            javafx.scene.image.Image webcam_img;
//            while(true){
//            	SerializedImage si = (SerializedImage)comm.getObject();
//                System.out.println("received image");
//                System.out.println(si);
//                
//            	BufferedImage screen_bi = si.getScreen();
//                System.out.println(screen_bi);
////                BufferedImage webcam_bi = si.getWebcam();
////                System.out.println(webcam_bi);
//                if(screen_bi!=null){
//            		screen_img = SwingFXUtils.toFXImage(screen_bi, null);
//                        screen.setImage(screen_img);
//                }
////                if(webcam_bi!=null){
////                        webcam_img = SwingFXUtils.toFXImage(webcam_bi, null);
////                        webcam.setImage(webcam_img);
////                }
//            }
//            //Communication ends
//        }
//        catch(IOException e){
//            Dialogs.showErrorDialog(null, "Could not make a connection");
//        }
//        finally{
//            try{
//                socket.close();
//            }
//            catch(IOException e){
//                Dialogs.showErrorDialog(null, "Error closing socket");
//            }
//        }
//    }
}
