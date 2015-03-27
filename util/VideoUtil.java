/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package util;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javafx.scene.image.ImageView;

import javax.swing.ImageIcon;

import net.coobird.thumbnailator.Thumbnails;
import ApplicationLayer.SerializedImage;
import java.awt.AWTException;
import javafx.scene.control.Dialogs;

/**
 *
 * @author phongnt
 */
public class VideoUtil extends Thread implements VideoManager{
    Communicator comm;
    ImageView imageView;
    private final long interval = 500;
    
    int count;
    public VideoUtil(Communicator communicator, ImageView imageView){
        this.comm=communicator;
        this.imageView=imageView;
    }
    
    //
    public VideoUtil(Communicator communicator){
        this.comm=communicator;
    }
    //

    @Override
    public void run()
    {
        count =0;
        BufferedImage screen = null;
        Robot robot = null;
        try{
            robot = new Robot();
        }catch(AWTException e) {
            Dialogs.showErrorDialog(null, "New robot could not be created");
        }
        Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
        Rectangle rect = new Rectangle(0, 0, size.width, size.height);
        System.out.println("inside start");
        while(true)
        {
            try{
                screen = robot.createScreenCapture(rect);
                resize(screen, 280 , 210);
                SerializedImage simg = new SerializedImage(screen);
                comm.sendObject(simg);
                simg = null;
                System.gc();
                count++;
                System.out.println(count);
                comm.flushOut();
                try{
                    Thread.currentThread().sleep(2000);
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
