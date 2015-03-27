/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ApplicationLayer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Vector;

/**
 *
 * @author Phuc
 */
public class Message implements Serializable {
    public static final int NEGATIVE = -1;
    public static final int ACKNOWLEDGE = 0;
    public static final int LOGIN_STUDENT = 1;
    public static final int LOGIN_PROCTOR = 2;
    public static final int EDIT_PROFILE_STUDENT = 3;
    public static final int EDIT_PROFILE_PROCTOR = 4;
    public static final int CANCEL_INVIGILATION_BOOK = 5;
    public static final int NEW_INVIGILATION_BOOK = 6;
    public static final int GET_NEW_INVIGILATION_BOOK_LIST = 7;
    public static final int BOOK_EXAM = 9;
    public static final int EDIT_EXAM =10;
    public static final int DELETE_EXAM =11;
    public static final int GET_PROCTOR_IP = 12;
    public static final int GET_AUTHORISED_STATUS = 13;
    public static final int ALREADY_LOGGED_IN =14;
    public static final int STUDENT_LOG_OUT =15;
    public static final int PROCTOR_LOG_OUT =16;
    public static final int START_EXAM = 30;
    public static final int SET_AUTHORIZE = 17;
    private int messageCode = 0;
    private Vector content = null;
    
    public Message(){
        this.messageCode = 0;
        content = new Vector();
    }
    
    public Message(int messageCode){
        this.messageCode = messageCode;
        content = new Vector();
    }
    
    public int getMessageCode(){
        return this.messageCode;
    }
    
    public void setMessageCode(int messageCode){
        this.messageCode=messageCode;
    }
    
    public Vector getContent() {
        return content;
    }
    
    public void addContent(Object o) {
        content.add(o);
    }
    
    public void setContent(Vector v)
    {
    	this.content=v;
    }
    
    public void removeContent(int index){
        content.remove(index);
    }
    
    public void waitMessage(ObjectInputStream in){
        try{
            Message msg = (Message)in.readObject();
            this.content=msg.getContent();
            this.messageCode=msg.getMessageCode();
        }
        catch(ClassNotFoundException e){
            e.printStackTrace();
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
    public void sendMessage(ObjectOutputStream out){
        try{
        	 
            out.writeObject(this);
            out.flush();
        }
        catch(IOException e){
        	System.out.println("cannot write");
            e.printStackTrace();
        }
        
    }
    
}
