/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package DatabaseManager;

import DatabaseEntity.User;

/**
 *
 * @author Phuc
 */
public interface AccountInterface {
    
    public boolean updateProfile(User user);
    
    public boolean authenticateAccount(User user, String ipAddress);
    
    public boolean setStatusOnline(User user);
    
    public boolean setStatusOffline(User user);
    
    public int checkStatus(User user);

}
