/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ApplicationLayer;

import Client.MainApp;

/**
 *
 * @author phongnt
 */
public class Controller {
    private MainApp mainApp;
    public void setMainApp(MainApp mainApp){
        this.mainApp=mainApp;
    };
    public MainApp getMainApp(){
        return this.mainApp;
    }
}
