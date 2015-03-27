/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package util;

/**
 *
 * @author phongnt
 */
public class StudentCommunicator extends BaseCommunicator {
    public StudentCommunicator(String host){
        super(host,Communicator.DEFAULT_PORT_PROCTOR);
        System.out.println("Connected successfully");
    }

}
