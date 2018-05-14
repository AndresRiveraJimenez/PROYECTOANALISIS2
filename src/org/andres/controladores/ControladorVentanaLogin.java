/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.andres.controladores;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.util.Duration;
import org.andres.sistema.Principal;
import tray.animations.AnimationType;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

/**
 *
 * @author d5
 */
public class ControladorVentanaLogin implements Initializable{
    @FXML private TextField txtUser;
    @FXML private TextField txtPass;
    private Principal escenarioPrincipal;
    private TrayNotification tray = new TrayNotification();
    @Override
    public void initialize(URL location, ResourceBundle resources) {
     }

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    public void validarUsuario(){
        if(txtUser.getText().equals("admin") & txtPass.getText().equals("admin")) {
            System.out.println("usuario valido");
            escenarioPrincipal.ventanaPrincipal();
        } else {
            System.out.println("Usuario invalido");
            txtPass.setText("");
            txtUser.setText("");
            testErrorLogin();
         }
    }
    public void cerrar(){
        escenarioPrincipal.cerrar();
    }
            public void testErrorLogin(){
                String title = "Error";
                String message = "Verifica tu usuario y contraseña";
                NotificationType notification = NotificationType.ERROR;
                AnimationType animacion = AnimationType.POPUP;
                    tray.setTray(title, message, notification);
                    tray.setAnimationType(animacion);
                    tray.showAndDismiss(Duration.seconds(3));
             
    }
}