/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package DatabaseManager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import DatabaseEntity.Proctor;
import DatabaseEntity.User;
import javafx.application.Platform;
import javafx.scene.control.Dialogs;

/**
 * 
 * @author Phuc
 */
public class ProctorManager implements AccountInterface {

	@Override
	public boolean updateProfile(User userAccount) {
		Proctor proctor = (Proctor) userAccount;
		PreparedStatement update = null;
		try {
			update = DatabaseManager.connection
					.prepareStatement("UPDATE "
							+ Table.listOfTable.PROCTOR
							+ " SET first_name = ?, last_name = ?, date_of_birth = ?, username = ?, password = ?, ip_address = ? WHERE proctor_id = ?");
			update.setString(1, proctor.getFirstName());
			update.setString(2, proctor.getLastName());
			update.setDate(3, Table.convertDate(proctor.getDateOfBirth()));
			update.setString(4, proctor.getUsername());
			update.setString(5, proctor.getPassword());
			update.setString(6, proctor.getProctorIP());
			update.setString(7, proctor.getProctorID());
			update.executeUpdate();
			System.out.println("Successfully Updated profile");
			return true;
		} catch (SQLException ex) {
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                Dialogs.showInformationDialog(null, "Database error");
                            }	
                        });
			Logger.getLogger(StudentManager.class.getName()).log(Level.SEVERE,
					null, ex);
			ex.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean authenticateAccount(User userAccount, String ipAddress) {
		int valid;
                boolean check = false;
		PreparedStatement getInfo = null;
		ResultSet result = null;
		Proctor proctor = (Proctor) userAccount;
		boolean getRecord;

		try {
			getInfo = DatabaseManager.connection
					.prepareStatement("SELECT * from "
							+ Table.listOfTable.PROCTOR + " where username =? ");
			getInfo.setString(1, proctor.getUsername());
			result = getInfo.executeQuery();
			while (result.next()) {
				// check password
				if (result.getString("password").equals(proctor.getPassword())) {
					PreparedStatement pst = DatabaseManager.connection
							.prepareStatement("UPDATE "
									+ Table.listOfTable.PROCTOR
									+ " SET ip_address = ? WHERE username = ?");
					pst.setString(1, ipAddress);
					pst.setString(2, proctor.getUsername());
					pst.executeUpdate();
					proctor.setFirstName(result.getString("first_name"));
					proctor.setLastName(result.getString("last_name"));
					proctor.setProctorID(result.getString("proctor_id"));
					proctor.setDateOfBirth(result.getDate("date_of_birth"));
					proctor.setProctorIP(ipAddress);
					getRecord = DatabaseManager.examRecordMgr.getExamRecord(
							proctor, 1);
                                        valid = checkStatus(proctor);
                                        if(valid==1)
                                        {
                                        	proctor.setStatus(true);
                                            check = true;
                                            
                                        }
                                        else if(valid == 0)
                                        {
                                            setStatusOnline(proctor);
                  
                                            check = true;
                                        }
                                        else
                                            check = false;
				
				} else
					break;
			}
			userAccount = (User) proctor;
                        return check;
		} catch (SQLException e) {
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                Dialogs.showInformationDialog(null, "Database error");
                            }	
                        });
			e.printStackTrace();
			System.out.println("Can not query student's info");
		}
                return false;
	}
    @Override
    public boolean setStatusOnline(User user) {
        PreparedStatement ps = null;
        int result = 0;
        Proctor proctor = (Proctor)user;
        try
        {
            ps = DatabaseManager.connection.prepareStatement("UPDATE "+ Table.listOfTable.PROCTOR + " SET status = ? WHERE proctor_id = ?" );
            ps.setBoolean(1, true);
            ps.setString(2, proctor.getProctorID());
            result = ps.executeUpdate();
            if(result >0)
                return true;
        } catch (SQLException ex) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    Dialogs.showInformationDialog(null, "Database error");
                }	
            });        
            Logger.getLogger(StudentManager.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return false;
    }

    @Override
    public boolean setStatusOffline(User user) {
        PreparedStatement ps = null;
        Proctor proctor = (Proctor)user;
        int result = 0;
        try
        {
            ps = DatabaseManager.connection.prepareStatement("UPDATE "+ Table.listOfTable.PROCTOR + " SET status = ?, ip_address = ? WHERE proctor_id = ?" );
            ps.setBoolean(1, false);
            ps.setString(2, null);
            ps.setString(3, proctor.getProctorID());
            result = ps.executeUpdate();
            if(result >0)
                return true;
        } catch (SQLException ex) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    Dialogs.showInformationDialog(null, "Database error");
                }	
            }); 
            Logger.getLogger(StudentManager.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return false;
    }

    @Override
    public int checkStatus(User user) {
        PreparedStatement ps = null;
        ResultSet result = null;
        Proctor proctor = (Proctor)user;
        try
        {
            ps = DatabaseManager.connection.prepareStatement("SELECT status FROM "+ Table.listOfTable.PROCTOR + " WHERE proctor_id = ?" );
            ps.setString(1, proctor.getProctorID());
            result = ps.executeQuery();
            while(result.next())
            {
                if(result.getBoolean("status"))
                    return 1;
                else
                    return 0;
                
            }
        }   catch (SQLException ex) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        Dialogs.showInformationDialog(null, "Database error");
                    }	
                });            
                Logger.getLogger(ProctorManager.class.getName()).log(Level.SEVERE, null, ex);
                return 2;
            }
        return 2;
    }
}
