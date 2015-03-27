/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ApplicationLayer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import util.BaseCommunicator;
import DatabaseManager.DatabaseManager;
import javafx.application.Platform;
import javafx.scene.control.Dialogs;

/**
 *
 * @author Phuc
 */
public class Server {
	private static DatabaseManager manager;
	private static ServerSocket serverSocket;

    public static void main(String[] args) {
    	init();
        //manager.getProctorIP(3);
    	try {
    		establishConnect();
		} catch (IOException e) {
		}
       
    }
    
    private static void init()
    {
    	manager = new DatabaseManager();
    	//serverService = new ServerService(manager);
    	
    }
    private static void establishConnect() throws IOException
    {
    	try {
    		serverSocket = new ServerSocket(8008);
    		System.out.println("Listening on port :8008");
    	}catch (Exception e)
    	{
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    System.out.println("Cannot open port on server");
                }	
            });   		
    		
    	}
    	while (true)
    	{
	    	try{  		
	    		Socket clientSocket = serverSocket.accept();
	    		System.out.println("Connected to client :" +clientSocket.getInetAddress().getHostAddress());
	    		ClientThread clientThread = new ClientThread(clientSocket,manager);
	    		clientThread.start();
	    
	    		
	    	
	    	}catch (Exception e)
	    	{
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            System.out.println("Cannot connect to server");
                        }	
                    }); 
	    	}
    	}    	
    }
    
}
