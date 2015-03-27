/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package DatabaseEntity;

import java.io.Serializable;
import java.sql.Date;


/**
 *
 * @author Phuc
 */
public class Proctor extends User implements Serializable{
    private String proctorID;
    private String proctorIP;

    public Proctor(String proctorID, String proctorIP, String username, String password, String first_name, String last_name, Date dateOfBirth) {
        super(username, password, first_name, last_name, dateOfBirth);
        this.proctorID = proctorID;
        this.proctorIP = proctorIP;
    }
    
    public Proctor()
    {
        super();
    }
    public String getProctorID() {
        return proctorID;
    }

    public String getProctorIP() {
        return proctorIP;
    } 

    public void setProctorID(String proctorID) {
        this.proctorID = proctorID;
    }

    public void setProctorIP(String proctorIP) {
        this.proctorIP = proctorIP;
    }
    
    public void copy(Proctor proctor){
        super.copy(proctor);
        this.proctorID=proctor.proctorID;
        this.proctorIP=proctor.proctorIP;
    }
}
