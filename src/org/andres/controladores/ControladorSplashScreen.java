/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.andres.controladores;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import org.andres.sistema.Principal;

/**
 *
 * @author d5
 */
public class ControladorSplashScreen extends Thread implements Initializable, Runnable{
    private Principal escenarioPrincipal ;
    @FXML private ProgressBar barraProgreso;
    private Thread hilo;
    private int j;
    private boolean ejecuta = true;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
        hilo = new Thread(this);
        hilo.start();
    }   

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
   
  @Override
    public void run() {
       while(j < 1000){
              j++;
            try {
                
                Thread.sleep(j);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            final double progress = j*0.019;
             Platform.runLater(() -> {
           
           barraProgreso.setProgress(progress);
          
          
            if(progress>1){
                
               System.out.println(progress);
               // validarConexion();
               j=1000;
              escenarioPrincipal.ventanaLogin();
             }
           
          
        });
             
        }
         
       }
       
    
    
}
