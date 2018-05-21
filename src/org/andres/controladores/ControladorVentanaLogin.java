/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.andres.controladores;

import java.net.URL;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.util.Duration;
import org.andres.bean.Rol;
import org.andres.bean.Usuario;
import org.andres.sistema.Principal;
import org.andresrivera.conexion.Conexion;
import tray.animations.AnimationType;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

public class ControladorVentanaLogin implements Initializable{
    @FXML private TextField txtUser;
    @FXML private TextField txtPass;
    private Usuario userAuth;
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
    
    public void validarCampos(){
        if(txtUser.getText().isEmpty()){
            TrayNotification tray = new TrayNotification("Login", "Debe ingresar un nombre de usuario", NotificationType.ERROR);
            tray.setAnimationType(AnimationType.FADE);
            tray.showAndDismiss(Duration.seconds(1));
            txtUser.requestFocus();
        }else if(txtPass.getText().isEmpty()){
            TrayNotification tray = new TrayNotification("Login", "Debe ingresar una contraseña", NotificationType.ERROR);
            tray.setAnimationType(AnimationType.FADE);
            tray.showAndDismiss(Duration.seconds(1));
            txtPass.requestFocus();
        }else{
            validarUsuario();
        }
    }
    
    public void validarUsuario(){
        if(validarCredenciales()) {
            System.out.println("usuario valido");
            msmBienvenida();
            escenarioPrincipal.setUserAuth(userAuth);
            escenarioPrincipal.ventanaPrincipal();
        } else {
            System.out.println("Usuario invalido");
            txtPass.setText("");
            txtUser.setText("");
            testErrorLogin();
         }
    }
    
    public boolean validarCredenciales(){
         CallableStatement procedimiento;
         ResultSet usuario;

        try {
            procedimiento = (CallableStatement ) Conexion.getInstancia().getConexion().prepareCall("{call sp_validarCredenciales(?,?)}");
            
                procedimiento.setString(1, txtUser.getText());
                procedimiento.setString(2, txtPass.getText());
                procedimiento.execute();
                
                usuario = procedimiento.getResultSet();
                if(usuario != null){
                    usuario.next();
                    this.userAuth = new Usuario( usuario.getInt("idUsuario"), usuario.getString("nombre"), 
                            usuario.getString("apellido"), usuario.getString("nombreUsuario"),
                            new Rol(usuario.getInt("idRol"),usuario.getString("nombreRol"), usuario.getString("descripcion") ));
                    return true;
                }                                   
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }
    
    public void msmBienvenida() {
        TrayNotification tray = new TrayNotification("Bienvenido", userAuth.getNombre()+" "+userAuth.getApellido() , NotificationType.SUCCESS);
        tray.setAnimationType(AnimationType.POPUP);
        tray.showAndDismiss(Duration.seconds(1));
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