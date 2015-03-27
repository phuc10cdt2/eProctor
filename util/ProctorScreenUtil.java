/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package util;

import ApplicationLayer.SerializedImage;
import java.awt.image.BufferedImage;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 *
 * @author phongnt
 */
public class ProctorScreenUtil extends Thread{
    Communicator comm;
    ImageView screenView;
    public ProctorScreenUtil(Communicator comm, ImageView screenView){
        this.comm=comm;
    }
    @Override
    public void run(){
        while(true){
            try{
                SerializedImage si = (SerializedImage)comm.getObject();
                System.out.println("received image");
                final BufferedImage image_bi = si.getScreen();
                Platform.runLater(new Runnable() {
                    @Override
                    public void run(){ 
                        if(image_bi!=null){
                            Image screen_img = SwingFXUtils.toFXImage(image_bi, null);
                            screenView.setImage(screen_img); 
                        }
                    }
                });
            }
            catch(Exception e){

            }
        }
    }
}
