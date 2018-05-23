/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.andres.controladores;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import org.andres.bean.Usuario;
import org.andres.sistema.Principal;
import tray.animations.AnimationType;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

public class ControladorVentanaPrincipal implements Initializable {
    
    @FXML private ImageView imgFondo;
    @FXML private Label labelUserAuth;
    
    @FXML private Button btnClientes;
    @FXML private Button btnTecnicos;
    @FXML private Button btnBoletas;
    @FXML private Button btnDashboard;
    @FXML private Button btnReportes;
    @FXML private Button btnUsuarios;
    
    private Principal escenarioPrincipal;
    private Usuario userAuth;
    private TrayNotification tray = new TrayNotification();
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        transiciones();

    }
    
    public void deshabilitarBotton(){
        this.btnDashboard.setDisable(true);
        this.btnReportes.setDisable(true);
        this.btnUsuarios.setDisable(true);
    }
    
    public void setUserAuth(Usuario userAuth){
        this.userAuth = userAuth;
        labelUserAuth.setText(userAuth.getUser());
        if(this.userAuth.getRol().getIdRol() == 2){
            deshabilitarBotton();
        }
    }
    
       public void transiciones(){
       FadeTransition trancicion = new FadeTransition(Duration.seconds(2), imgFondo);
       trancicion.setFromValue(1.0);
       trancicion.setToValue(0.01);
       trancicion.setCycleCount(2);
       trancicion.setAutoReverse(true);
       trancicion.play();
 
    }
        public void testMensaje(){
                String title = "Bienvenido";
                String message = "Aplicacion para boletas!";
                NotificationType notification = NotificationType.SUCCESS;
                AnimationType animacion = AnimationType.POPUP;
                    tray.setTray(title, message, notification);
                    tray.showAndDismiss(Duration.seconds(3));
             
    }

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    public void mostrarClientes(){
        escenarioPrincipal.ventanaClientes();
    }   
    public void mostrarTecnicos(){
        escenarioPrincipal.ventanaTecnicos();
    } 
    public void mostrarBoletas(){
        escenarioPrincipal.ventanaBoletas();
    } 
    public void mostrarUsuario(){
        escenarioPrincipal.ventanaUsuario();
    } 
    public void mostrarReportes(){
        escenarioPrincipal.ventanaReportes();
    }
    public void mostrarDashAsignaciones(){
        escenarioPrincipal.ventanaDashboardAsig();
    }
    public void cerrar(){
        escenarioPrincipal.cerrar();
    }
}
