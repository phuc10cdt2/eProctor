package ApplicationLayer;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Vector;

import DatabaseEntity.ExamRecord;
import DatabaseManager.DatabaseManager;

public class OutSideServerService {
	int msgCode;
	Vector msgContent;
	DatabaseManager dbMng;
	boolean check;
	public OutSideServerService(DatabaseManager mng)
	{
		this.dbMng = mng;
	}
	public Message serveService(Message receivedMsg, String ipAddress)
	{
		Message sendMsg=  new Message();
		this.msgCode = receivedMsg.getMessageCode();
		System.out.println("Received: " + msgCode);
		this.msgContent = receivedMsg.getContent();
		switch (msgCode)
		{
			case Message.START_EXAM:
				System.out.println("sending request to NTU server");
				String s = sendRequest(((ExamRecord)msgContent.get(0)));
				System.out.println("Link : "+ s);
				msgContent.add(s);
				sendMsg.setContent(msgContent);
				break;
				default:
		}
		sendMsg.setMessageCode(msgCode);
		return sendMsg;
	}
	
	private String sendRequest(ExamRecord record)
	{
		check = this.dbMng.checkAuthorized(record);
		if (!check) {
				msgCode=-1;
				return null;
		}
			
		 try { 
       	  String url = "http://172.22.228.210:8888/NTUServer.php";
 
     		URL obj = new URL(url);
     		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
    		con.setRequestMethod("POST");
     		con.setDoOutput(true);
     		int responseCode = con.getResponseCode();
     		System.out.println(con.getHeaderField("cz2001"));
     		
     		if(record.getCourseCode().equals("cz3001")) return con.getHeaderField("cz3001");
     		if(record.getCourseCode().equals("cz2001")) return con.getHeaderField("cz2001");
     		if(record.getCourseCode().equals("cz1001")) return con.getHeaderField("cz1001");
       } catch (Exception e) {
           e.printStackTrace();
       }
		 return null;
	}
	
}
