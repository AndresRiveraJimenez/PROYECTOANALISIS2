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
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.util.Duration;
import org.andres.sistema.Principal;
import org.andresrivera.conexion.Conexion;
import tray.animations.AnimationType;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

public class ControladorAgregarCliente implements Initializable {
    @FXML private TextField txtRazonSocial;
    @FXML private TextField txtDireccion;
    @FXML private TextField txtEmail;
    @FXML private TextField txtTelefono;
    private Principal escenarioPrincipal;
    @FXML
    private Button btnGuardar;
    @FXML
    private JFXButton btnAgregar;

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
            TrayNotification tray = new TrayNotification("GUARDAR", "Debes ingresar una razon social", NotificationType.ERROR);
            tray.setAnimationType(AnimationType.FADE);
            tray.showAndDismiss(Duration.seconds(1));
            txtRazonSocial.requestFocus();
        } else if (txtDireccion.getText().isEmpty()) {
            TrayNotification tray = new TrayNotification("GUARDAR", "Debes ingresar una direccion", NotificationType.ERROR);
            tray.setAnimationType(AnimationType.FADE);
            tray.showAndDismiss(Duration.seconds(1));
            txtDireccion.requestFocus();
        } else if (!validarEmail()) {
            TrayNotification tray = new TrayNotification("GUARDAR", "Debes ingresar un Email valido", NotificationType.ERROR);
            tray.setAnimationType(AnimationType.FADE);
            tray.showAndDismiss(Duration.seconds(1));
            txtEmail.requestFocus();
        } else if (!validarTelefono()) {
            TrayNotification tray = new TrayNotification("GUARDAR", "Debes ingresar un numero telefonico valido", NotificationType.ERROR);
            tray.setAnimationType(AnimationType.FADE);
            tray.showAndDismiss(Duration.seconds(1));
            txtTelefono.requestFocus();
        }  else {
            guardarCliente();
        }
    }
  /*  public void validarTxtTele() {

        if (!txtTelefono.getText().matches("\\d*")) {
            txtTelefono.clear();
        }
    }*/
    
    public boolean validarEmail() {

        String regex = "^[\\w-_ .]+@{1}[\\w]+.{1}[a-zA-Z]{2,6}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(txtEmail.getText());

        return matcher.matches();
    }
    
    public boolean validarTelefono() {

        String regex = "^[\\d]{8}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(txtTelefono.getText());

        return matcher.matches();
    }
    
    public void limitTextField() {
        int  limit =8;
        UnaryOperator<TextFormatter.Change> textLimitFilter = change -> {
            if (change.isContentChange()) {
                int newLength = change.getControlNewText().length();
                if (newLength > limit) {
                    String trimmedText = change.getControlNewText().substring(0, limit);
                    change.setText(trimmedText);
                    int oldLength = change.getControlText().length();
                    change.setRange(0, oldLength);
                }
            }
            return change;
        };
        txtTelefono.setTextFormatter(new TextFormatter(textLimitFilter));
    } 

     
}
