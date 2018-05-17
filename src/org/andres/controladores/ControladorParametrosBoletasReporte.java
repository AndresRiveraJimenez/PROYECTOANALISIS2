/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.andres.controladores;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.util.Duration;
import org.andres.reportes.GenerarReporte;
import org.andres.sistema.Principal;
import tray.animations.AnimationType;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

/**
 *
 * @author d5
 */
public class ControladorParametrosBoletasReporte implements Initializable{
    
    private Principal escenarioPrincipal;
    @FXML private DatePicker datePicFechaInicio;
    @FXML private DatePicker datePicFechaFinal;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }
    
        public void reporteClientes(){
        LocalDate fechaInicio = datePicFechaInicio.getValue();
        LocalDate fechaFinal = datePicFechaFinal.getValue();
        if(fechaInicio.isAfter(fechaFinal)){
            
            TrayNotification tray = new TrayNotification("ERROR", "El rango de para la fecha es incorrecta", NotificationType.ERROR);
            tray.setAnimationType(AnimationType.POPUP);
            tray.showAndDismiss(Duration.seconds(1));
        }
        else if((fechaInicio.isBefore(fechaFinal)&&fechaFinal.isAfter(fechaInicio))||fechaInicio.equals(fechaFinal)){
        Map parametros = new HashMap();
        parametros.put("fechaInicio", datePicFechaInicio.getValue().toString());
        parametros.put("fechaFinal", datePicFechaFinal.getValue().toString());
        GenerarReporte.getInstancia().GenerarReporte(parametros, "ReporteBoletas.jasper", "Reporte de Boletas");
        cerrar();   
        }
        
    }
                public void cerrar(){
        escenarioPrincipal.cerrarModalCrear();
    }        
}
