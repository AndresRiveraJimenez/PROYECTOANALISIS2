/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.andres.controladores;

import com.jfoenix.controls.JFXButton;
import java.net.URL;
import java.sql.CallableStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.util.Duration;
import org.andres.recursos.FxDialogs;
import org.andres.reportes.GenerarReporte;
import org.andres.sistema.Principal;
import org.andresrivera.conexion.Conexion;
import tray.animations.AnimationType;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

/**
 *
 * @author d5
 */
public class ControladorAgregarCliente implements Initializable {
    @FXML private TextField txtRazonSocial;
    private TextField txtDireccion;
    private TextField txtEmail;
    private TextField txtTelefono;
    private Principal escenarioPrincipal;
    @FXML
    private Button btnGuardar;
    @FXML
    private JFXButton btnAgregar;
    @FXML
    private TextField txtRazonSocial1;
    @FXML
    private TextField txtRazonSocial11;
    @FXML
    private TextField txtRazonSocial12;
    @FXML
    private TextField txtRazonSocial2;
    @FXML
    private TextField txtRazonSocial21;
    @FXML
    private TextField txtRazonSocial22;
    @FXML
    private TextField txtRazonSocial13;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }
    @FXML
    public void cerrar()
    {
        escenarioPrincipal.cerrarModalCrear();
    }
    @FXML
    public void guardarCliente(){
                CallableStatement  procedimiento;
        try {
            procedimiento = (CallableStatement ) Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarCliente(?,?,?,?,?)}");
            
                procedimiento.setString(1, txtRazonSocial.getText());
                procedimiento.setString(2, txtTelefono.getText());
                procedimiento.setString(3, txtEmail.getText());
                procedimiento.setString(4, txtDireccion.getText());
                procedimiento.setInt(5, 1);
                procedimiento.execute();
                TrayNotification tray = new TrayNotification("GUARDAR", "Cliente nuevo agregado", NotificationType.SUCCESS);
                tray.setAnimationType(AnimationType.POPUP);
                tray.showAndDismiss(Duration.seconds(1));
                escenarioPrincipal.ventanaClientes();
                cerrar();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    public void validarCamposGuardar() {
        if (txtRazonSocial.getText().isEmpty()) {
            TrayNotification tray = new TrayNotification("GUARDAR", "Debes ingresar un nombre", NotificationType.ERROR);
            tray.setAnimationType(AnimationType.FADE);
            tray.showAndDismiss(Duration.seconds(1));
            txtRazonSocial.requestFocus();
        } else if (txtDireccion.getText().isEmpty()) {
            TrayNotification tray = new TrayNotification("GUARDAR", "Debes ingresar un Apellido", NotificationType.ERROR);
            tray.setAnimationType(AnimationType.FADE);
            tray.showAndDismiss(Duration.seconds(1));
            txtDireccion.requestFocus();
        } else if (txtEmail.getText().isEmpty()) {
            TrayNotification tray = new TrayNotification("GUARDAR", "Debes ingresar un Email", NotificationType.ERROR);
            tray.setAnimationType(AnimationType.FADE);
            tray.showAndDismiss(Duration.seconds(1));
            txtEmail.requestFocus();
        } else if (txtTelefono.getText().isEmpty()) {
            TrayNotification tray = new TrayNotification("GUARDAR", "Debes ingresar un numero telefonico", NotificationType.ERROR);
            tray.setAnimationType(AnimationType.FADE);
            tray.showAndDismiss(Duration.seconds(1));
            txtTelefono.requestFocus();
        }  else {
            guardarCliente();
        }
    }
     public void validarTxtTele() {
        if (!txtTelefono.getText().matches("\\d*")) {
            txtTelefono.clear();
        }
    }

     
}
