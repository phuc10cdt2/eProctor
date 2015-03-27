package ApplicationLayer;

import java.util.ArrayList;
import java.util.Vector;

import DatabaseEntity.ExamRecord;
import DatabaseEntity.Proctor;
import DatabaseEntity.Student;
import DatabaseEntity.User;
import DatabaseManager.DatabaseManager;

public class ServerService {
	int msgCode;
	boolean check;
	Vector msgContent;
	DatabaseManager dbMng;
	public ServerService(DatabaseManager dbMng)
	{
		this.dbMng =dbMng;
	}
	public Message serveService(Message receivedMsg, String ipAddress)
	{
		Message sendMsg=  new Message();
		this.msgCode = receivedMsg.getMessageCode();
		System.out.println("Received: " + msgCode);
		this.msgContent = receivedMsg.getContent();
		switch (msgCode)
		{
			case Message.LOGIN_STUDENT: 
					System.out.println("Authenticating Student login");
					System.out.println("Username:" + ((User)msgContent.get(0)).getUsername());
					System.out.println("Password:" + ((User)msgContent.get(0)).getPassword());
					studentLoginService((User)msgContent.get(0), null);
					sendMsg.setContent(msgContent);
                    break;
			case Message.LOGIN_PROCTOR:
					System.out.println("Authenticating Proctor login");
					System.out.println("Username:" + ((User)msgContent.get(0)).getUsername());
					System.out.println("Password:" + ((User)msgContent.get(0)).getPassword());
					proctorLoginService((User)msgContent.get(0),ipAddress);
					System.out.println("Number of ExamRecord belong to Proctor: " +((User)msgContent.get(0)).getExamRecord().size());

					sendMsg.setContent(msgContent);
					break;
			case Message.EDIT_PROFILE_STUDENT:
					System.out.println("Editing student profile");
					updateStudentProfileService((Student)msgContent.get(0));
					sendMsg.setContent(msgContent);
                    break;
			case Message.EDIT_PROFILE_PROCTOR:
					System.out.println("Editing proctor profile");
					updateProctorProfileService((Proctor)msgContent.get(0));
					sendMsg.setContent(msgContent);
                    break;
                            
			case Message.BOOK_EXAM:
					System.out.println("Booking exam");
					System.out.println("Book exam for RecordID: "+ ((ExamRecord) msgContent.get(0)).getRecordID());
					System.out.println("Book exam for course: "+ ((ExamRecord) msgContent.get(0)).getCourseCode());
					System.out.println("Book exam for studentID: "+ ((ExamRecord) msgContent.get(0)).getStudentMatric());
					System.out.println("Book exam for date: "+ ((ExamRecord) msgContent.get(0)).getExamDate());
					bookExamService((ExamRecord)msgContent.get(0));
					msgContent = null;
					sendMsg.setContent(msgContent);
					break;
			case Message.GET_NEW_INVIGILATION_BOOK_LIST:
					ArrayList<ExamRecord> list = new ArrayList<ExamRecord>();
					System.out.println("Getting studentlist for proctor to book:");
					getListOfCandidate(list);
					msgContent.add(list);
					System.out.println("Number of unbooked student: " + list.size());
					sendMsg.setContent(msgContent);
					break;
			case Message.NEW_INVIGILATION_BOOK:
					System.out.println("Booking Invigilate");
					System.out.println("Book Invigilation for RecordID: "+ ((ExamRecord) msgContent.get(0)).getRecordID());
					System.out.println("Book Invigilation for course: "+ ((ExamRecord) msgContent.get(0)).getCourseCode());
					System.out.println("Book Invigilation for studentID: "+ ((ExamRecord) msgContent.get(0)).getStudentMatric());
					System.out.println("Book Invigilation for proctorID: "+ ((ExamRecord) msgContent.get(0)).getProctorID());
					System.out.println("Book Invigilation for date: "+ ((ExamRecord) msgContent.get(0)).getExamDate());
					bookInvigilation((ExamRecord)msgContent.get(0));
					msgContent = null;
					sendMsg.setContent(msgContent);
					break;
			case Message.EDIT_EXAM:
					editExamService((ExamRecord)msgContent.get(0));
					sendMsg.setContent(msgContent);
                    break;
			case Message.DELETE_EXAM:
					System.out.println("Delete Exam RecordID: "+ ((ExamRecord)msgContent.get(0)).getRecordID());
					System.out.println("Delete Exam StudentID: "+ ((ExamRecord)msgContent.get(0)).getStudentMatric());
					System.out.println("Delete Exam ExamDate: "+ ((ExamRecord)msgContent.get(0)).getExamDate());
					System.out.println("Delete Exam Course: "+ ((ExamRecord)msgContent.get(0)).getCourseCode());
					deleteExamRecord((ExamRecord)msgContent.get(0));
					sendMsg.setContent(msgContent);
                    break;
			case Message.CANCEL_INVIGILATION_BOOK:
					System.out.println("Delete Invigilation RecordID: "+ ((ExamRecord)msgContent.get(0)).getRecordID());
					System.out.println("Delete Invigilation StudentID: "+ ((ExamRecord)msgContent.get(0)).getStudentMatric());
					System.out.println("Delete Invigilation ExamDate: "+ ((ExamRecord)msgContent.get(0)).getExamDate());
					System.out.println("Delete Invigilation Course: "+ ((ExamRecord)msgContent.get(0)).getCourseCode());
					cancelInvigilate((ExamRecord)msgContent.get(0));
					sendMsg.setContent(msgContent);
					break;
            case Message.GET_PROCTOR_IP:
                    System.out.println("Get proctor ID of exam record " + msgContent.get(0));
                    ExamRecord record = (ExamRecord)msgContent.get(0);
                    String proctorIP = getProctorIP(record);
                    System.out.println("Proctor IP: " + proctorIP);
                    msgContent = new Vector();
                    msgContent.add(proctorIP);
                    sendMsg.setContent(msgContent);
                    break;
            case Message.STUDENT_LOG_OUT:
            		System.out.println("Student logging out");
            		studentLogout((User)msgContent.get(0));
            		msgContent = null;
            		sendMsg.setContent(msgContent);
            		break;
            case Message.PROCTOR_LOG_OUT:
        			System.out.println("Proctor logging out");
        			proctorLogout((User)msgContent.get(0));
        			msgContent = null;
        			sendMsg.setContent(msgContent);
        			break;
            case Message.START_EXAM:
            		System.out.println("sending request to NTU server");
            		MakeRequest rq = new MakeRequest();
            		String s = rq.sendRequest();
            		if (s==null) msgCode=-1;
            		System.out.println("Link : "+ s);
            		msgContent.add(s);
        			sendMsg.setContent(msgContent);
        			break;
			default:
		}
		sendMsg.setMessageCode(msgCode);
		return sendMsg;
	}
	
	private void studentLoginService(User u, String ipAddress)
	{	
		check = this.dbMng.studentLogin(u, ipAddress);
		if (u.getStatus()){
			msgCode= Message.ALREADY_LOGGED_IN;
			msgContent = null;
		}
		if (!check) msgCode=-1;
	}
	private void proctorLoginService(User u, String ipAddress)
	{
		
		check = this.dbMng.proctorLogin(u, ipAddress);
                
		if (u.getStatus()){
			msgCode= Message.ALREADY_LOGGED_IN;
			msgContent = null;
		}
		
		if (!check) msgCode=-1;
	}
	private void bookExamService(ExamRecord examRecord)
	{
		check =this.dbMng.bookExam(examRecord);
		if (!check) msgCode=-1;
		else System.out.println("Book Exam Successfully");
	}
	
	private void updateStudentProfileService(Student u)
	{
		check =this.dbMng.updateStudentProfile(u);
		if (!check) msgCode=-1;
	}
	
	private void updateProctorProfileService(Proctor u)
	{
		check =this.dbMng.updateProctorProfile(u);
		if (!check) msgCode=-1;
	}
	
	private void deleteExamRecord(ExamRecord examRecord)
	{
		check =this.dbMng.deleteExam(examRecord);
		if (!check) msgCode=-1;
		else System.out.println("Delete Exam Successfully");
	}
	
	private void editExamService(ExamRecord examRecord)
	{
		check =this.dbMng.bookInvigilation(examRecord);
		if (!check) msgCode=-1;
		else System.out.println("Edit Invigilation Successfully");
	}
	
	private void getListOfCandidate(ArrayList<ExamRecord> list)
	{
		check = this.dbMng.getListOfCandidate(list);
		if (!check) msgCode=-1;
		else System.out.println("Get list of candidates Successfully");
	}
	
	private void bookInvigilation(ExamRecord examRecord)
	{
		check = this.dbMng.bookInvigilation(examRecord);
		if (!check) msgCode=-1;
		else System.out.println("Book Invigilation Successfully");
	}

	private void cancelInvigilate(ExamRecord examRecord)
	{
		check =this.dbMng.cancelInvigilation(examRecord);
		if (!check) msgCode=-1;
		else System.out.println("Cancel Invigilation Successfully");
	}
	
	private void studentLogout(User user)
	{
		check = this.dbMng.logOutStudent(user);
		if (!check) msgCode=-1;
		else System.out.println("Logout Successfully");
	}
	
	private void proctorLogout(User user)
	{
		check = this.dbMng.logOutProctor(user);
		if (!check) msgCode=-1;
		else System.out.println("Logout Successfully");
	}
        
        private String getProctorIP(ExamRecord record)
        {
            String ip = this.dbMng.getProctorIP(record);
            System.out.println(ip.length());
            if(ip == null || ip.length() == 0)
            {
                msgCode = -1;
            }
            else
                msgCode = Message.GET_PROCTOR_IP;
            return ip;
        }

}
