/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package util;

import java.awt.image.BufferedImage;

import ApplicationLayer.Message;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 *
 * @author phongnt
 */
public interface Communicator {
    public static final int DEFAULT_PORT_SERVER = 8088;
    public static final int DEFAULT_PORT_PROCTOR = 8000;
    public void close();
    public ObjectOutputStream getOut();
    public ObjectInputStream getIn();
    public int sendObject(Object o);
    public Object getObject();
    public int sendMessage(Message msg);
    public Message getMessage();
    public void flushOut();
    public BufferedImage getImage();
    public void sendImage(BufferedImage img);
}
