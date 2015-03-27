/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ApplicationLayer;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;

/**
 *
 * @author phongnt
 */
public class SerializedImage implements Serializable {
    transient List<BufferedImage> images;
    //flag = 0:normal
    //flag = 1:verify
    //flag = 2: warning
//    private int flag;
    public SerializedImage(BufferedImage screen, int flag){
        this.images=new ArrayList();
        this.images.add(screen);
//        this.flag = flag;
    }
    public SerializedImage(BufferedImage screen){
        this.images=new ArrayList();
        this.images.add(screen);
//        this.flag = 0;
    }

//    public int getFlag() {
//        return flag;
//    }
//
//    public void setFlag(int flag) {
//        this.flag = flag;
//    }

    public BufferedImage getScreen(){
        return images.get(0);
    }
    public BufferedImage getWebcam(){
        return images.get(1);
    }
    
    public void setScreen(BufferedImage screen){
        images.set(0, screen);
    }
    
    public void setWebcam(BufferedImage webcam){
        images.set(1, webcam);
    }
    
    public int getSize(){
        return images.size();
    }
    
    public void clear(){
        images.clear();
        System.gc();
    }


    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.writeInt(images.size()); // how many images are serialized?
        for (BufferedImage eachImage : images) {
            ImageIO.write(eachImage, "gif", out); // png is lossless
            System.out.println("wrote 1");
        }
//        out.writeInt(flag);
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        final int imageCount = in.readInt();
        images = new ArrayList<BufferedImage>(imageCount);
        for (int i=0; i<imageCount; i++) {
            images.add(ImageIO.read(in));
        }
//        flag = in.readInt();
    }

}
