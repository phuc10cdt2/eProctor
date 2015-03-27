/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package util;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javafx.scene.control.Dialogs;
import javafx.scene.image.ImageView;
import net.coobird.thumbnailator.Thumbnails;
import ApplicationLayer.SerializedImage;

import com.github.sarxos.webcam.Webcam;

/**
 *
 * @author phongnt
 */
public class WebcamUtil extends Thread implements VideoManager{
    Communicator comm;
    ImageView imageView;
    private final long interval = 500;
    
    int count;
    public WebcamUtil(Communicator communicator, ImageView imageView){
        this.comm=communicator;
        this.imageView=imageView;
    }
    
    //
    public WebcamUtil(Communicator communicator){
        this.comm=communicator;
    }
    //

    @Override
    public void run()
    {
    	Webcam webcam = Webcam.getDefault();
    	webcam.open();
        count =0;
        BufferedImage webcamImg = null;
        
        System.out.println("inside start");
        while(true)
        {
            try{
                System.gc();
                webcamImg = webcam.getImage();
                resize(webcamImg, 280 , 210);
                SerializedImage simg = new SerializedImage(webcamImg);
                comm.sendObject(simg);
                count++;
                System.out.println(count);
                simg.clear();
                comm.flushOut();
                try{
                    Thread.currentThread().sleep(100);
                }catch(InterruptedException e) {
                    System.out.println("Can not sleep thread");
                }
            }
            catch(IOException e){
                Dialogs.showErrorDialog(null, "Message can not be sent");
            }
        }
    }
    public static BufferedImage resize(BufferedImage img, int newW, int newH) throws IOException {
  	  return Thumbnails.of(img).size(newW, newH).asBufferedImage();
  	}
}
