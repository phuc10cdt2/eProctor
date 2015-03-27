package ApplicationLayer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import util.BaseCommunicator;
import DatabaseEntity.Proctor;
import DatabaseEntity.Student;
import DatabaseManager.DatabaseManager;
import javafx.application.Platform;
import javafx.scene.control.Dialogs;

public class ClientThread extends Thread{
	private ObjectOutputStream outputObject;
	private ObjectInputStream inputObject;
	private Socket clientSocket;
	private static InsideServerService insideServerService;
	private static OutSideServerService outsideServerService;
	private Message receivedMessage,sendMessage;
	private DatabaseManager manager;
	private BaseCommunicator comm;
        private String ipAddress;
	public ClientThread(Socket clientSocket,DatabaseManager manager)
	{
		this.manager = manager;
		this.insideServerService = new InsideServerService(manager);
		this.outsideServerService = new OutSideServerService(manager);
		this.receivedMessage = null;
		this.sendMessage = new Message(); 
		this.clientSocket = clientSocket;
                ipAddress = clientSocket.getInetAddress().getHostAddress();
		try{
			this.outputObject= new ObjectOutputStream(this.clientSocket.getOutputStream());
			this.inputObject = new ObjectInputStream(this.clientSocket.getInputStream());
			comm = new BaseCommunicator(inputObject, outputObject);
			
		}catch (Exception e)
		{
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            Dialogs.showErrorDialog(null, "Can not connect to client ");
                        }	
                    });     
		}
	}
	public void run()
	{
		while(true){
		try{
			System.out.println("get in");
			this.receivedMessage = comm.getMessage();
			if(receivedMessage.getMessageCode()<30) sendMessage = insideServerService.serveService(receivedMessage, ipAddress);
			else sendMessage = outsideServerService.serveService(receivedMessage, null);
			comm.sendMessage(sendMessage);
			if (clientSocket==null) break;
		}catch(Exception e)
		{
			try {
				clientSocket.close();
				System.out.println("Client Thread : Terminated due to socket closed");
				break;
			} catch (IOException e1) {
				// TODO Auto-generated catch block
                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        Dialogs.showErrorDialog(null, "can not close client socket");
                                    }	
                                });   
				e1.printStackTrace();
			}
			System.out.println("cannot recieve messages from:" + this.clientSocket.getInetAddress().getHostAddress());
		}
	}
	}
	
}
