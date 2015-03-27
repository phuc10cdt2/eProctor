/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package DatabaseManager;

import DatabaseEntity.ExamRecord;
import DatabaseEntity.User;
import java.util.ArrayList;

/**
 *
 * @author Phuc
 */
public interface ExamRecordInterface {
    
    public boolean bookInvigilation(ExamRecord record);
    
    public boolean deleteExamRecord(ExamRecord record);
    
    public boolean getExamRecord(User userAccount, int n); 
    
    public boolean addExamRecord(ExamRecord record);
    
    public boolean getListCandidate(ArrayList<ExamRecord> list);
    
    public String getProctorIP(ExamRecord record);
    
    public boolean cancelInvigilation(ExamRecord record);
    
    public boolean setAuthorized(ExamRecord record);
    
    public boolean checkAuthorized(ExamRecord record);
}
