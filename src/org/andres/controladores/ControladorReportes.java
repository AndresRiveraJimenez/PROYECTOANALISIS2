/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.andres.controladores;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import org.andres.reportes.GenerarReporte;
import org.andres.sistema.Principal;

/**
 *
 * @author d5
 */
public class ControladorReportes implements Initializable{
    private Principal escenarioPrincipal;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    

    public void ventanaPrincipal(){
        escenarioPrincipal.ventanaPrincipal();
    }
            public void cerrar(){
        escenarioPrincipal.cerrar();
    }
    public void ventanaParametros(){
        escenarioPrincipal.ventanaParametrosRepBoleta();
    }
}
