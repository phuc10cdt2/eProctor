/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package util;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.imageio.ImageIO;

import ApplicationLayer.Message;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import javafx.application.Platform;
import javafx.scene.control.Dialogs;

/**
 *
 * @author phongnt
 */
public class BaseCommunicator implements Communicator {
    protected String host;
    protected int port;
    
    protected Socket client = null;
    protected ObjectInputStream in = null;
    protected ObjectOutputStream out = null;

    public Socket getClient() {
            return client;
    }

    public void setClient(Socket client) {
            this.client = client;
    }

    public ObjectOutputStream getOut() {
            return out;
    }

    public void setOut(ObjectOutputStream out) {
            this.out = out;
    }

    public void setIn(ObjectInputStream in) {
            this.in = in;
    }

    public ObjectInputStream getIn() {
            return in;
    }
    
    public BaseCommunicator(String host, int port){
        if (connect(host, port) == 1) {
                System.out.println("Connected to server");
        } else {
                System.out.println("Cannot connect to server");
        }
    }
    
    public BaseCommunicator(ObjectInputStream in, ObjectOutputStream out)
    {
    	this.in = in;
    	this.out = out;
    }
    
    @Override
    public int sendMessage(Message msg){
        try{
            out.writeObject(msg);
            return 0;
        }
        catch(IOException e){
            System.out.println("cannot write");
            e.printStackTrace();
        }
        return -1;
    }
    @Override
    public Message getMessage(){
        try{
            Message msg = (Message)in.readObject();
            return msg;
        }
        catch(ClassNotFoundException e){
            e.printStackTrace();
            
        }
        catch(IOException e){
            e.printStackTrace();
//            Dialogs.showErrorDialog(null, "Client disconnected");
        }
        return null;
    }
    @Override
    public int sendObject(Object o){
        try{
            out.writeObject(o);
            return 0;
        }
        catch(IOException e){
            Dialogs.showErrorDialog(null, "Could not write object to output stream");
        }
        return -1;
    }
    @Override
    public Object getObject(){
        try{
            Object o = in.readObject();
            return o;
        }
        catch(ClassNotFoundException e){
            Dialogs.showErrorDialog(null, "Could not read object from input stream");
        }
        catch(IOException e){
//            Platform.runLater(new Runnable() {
//                    @Override
//                    public void run() {
//                        Dialogs.showErrorDialog(null, "Student.fxml could not be loaded.");
//                    }	
//                });  
            //Dialogs.showErrorDialog(null, "Client disconnected");
            System.out.println("Student disconnected");
        }
        return null;
    }
    
   public BufferedImage getImage()
   {
	   try {
		   return ImageIO.read(in);
	   }catch(IOException e)
	   {
		   Dialogs.showErrorDialog(null, "Could not read image");
	   }
	   return null;
   }
   
   public void sendImage(BufferedImage img)
   {
       try{
        ImageIO.write(img, "jpg",out);
       }
      catch(Exception e)
      {
          Dialogs.showErrorDialog(null, "Could not send image");
      }
   }
    
    private int connect(String host, int port) {
        this.host=host;
        this.port=port;
        try {
                client = new Socket(host, port);
                out = new ObjectOutputStream((client.getOutputStream()));
                in = new ObjectInputStream((client.getInputStream()));
                return 1;
        } catch (UnknownHostException e) {
                Dialogs.showErrorDialog(null, "UnknownHostException on Client");
        } catch (IOException e) {
                Dialogs.showErrorDialog(null, "Could not read from Client");
        }
        return -1;
    }
    @Override
    public void flushOut(){
        try{
            out.flush();
        }
        catch(IOException e){
            Dialogs.showErrorDialog(null, "Output stream can not be flushed");
        }
    }
    
 
    private int disconnect() {
        try {
            client.close();
            return 1;
        } catch (IOException e) {
            Dialogs.showErrorDialog(null, "Could not close Client");
        }
        return -1;
    }
    
    public void close(){
        try{
            client.close();
        }
        catch(IOException e){
            //
        }
    }
}
