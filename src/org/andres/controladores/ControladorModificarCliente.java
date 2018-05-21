/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.andres.controladores;

import java.net.URL;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.util.Duration;
import org.andres.bean.Clientes;
import org.andres.sistema.Principal;
import org.andresrivera.conexion.Conexion;
import tray.animations.AnimationType;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

/**
 *
 * @author d5
 */
public class ControladorModificarCliente implements Initializable {
    @FXML private TextField txtID;
    @FXML private TextField txtRazonSocial;
    @FXML private TextField txtDireccion;
    @FXML private TextField txtEmail;
    @FXML private TextField txtTelefono;
    private Clientes clienteModificar;
    private Principal escenarioPrincipal;
    

    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
      
    }
        public void setClienteModificar(int idClienteModificar) {
            System.out.println(idClienteModificar);
            this.clienteModificar = buscarCliente(idClienteModificar);
            txtID.setText(String.valueOf(clienteModificar.getIdCliente()));
            txtRazonSocial.setText(String.valueOf(clienteModificar.getRazonSocial()));
            txtTelefono.setText(String.valueOf(clienteModificar.getTelefono()));
            txtEmail.setText(String.valueOf(clienteModificar.getCorreo()));
            txtDireccion.setText(String.valueOf(clienteModificar.getDireccion()));
            
    }

    public Clientes getClienteModificar() {
        return clienteModificar;
    }


     
    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }
    public void cerrar()
    {
        escenarioPrincipal.cerrarModalCrear();
    }
    
    public Clientes buscarCliente(int idClienteModificar) {
        Clientes resultado = null;
        try {
            PreparedStatement procedimiento = (PreparedStatement) Conexion.getInstancia().getConexion().prepareStatement("{call sp_BuscarCliente(?)}");
            procedimiento.setInt(1, idClienteModificar);
            ResultSet cliente = procedimiento.executeQuery();
            while (cliente.next()) {
                resultado = new Clientes(cliente.getInt("idCliente"), cliente.getString("razonSocial"),cliente.getString("telefono"),cliente.getString("correo"), cliente.getString("direccion"), 1);
               

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultado;
    }
    public void actualizarCliente(){
                CallableStatement  procedimiento;
        try {
            procedimiento = (CallableStatement ) Conexion.getInstancia().getConexion().prepareCall("{call sp_ActualizarClientes(?,?,?,?,?)}");
                procedimiento.setInt(1, Integer.valueOf(txtID.getText()));
                procedimiento.setString(2, txtRazonSocial.getText());
                procedimiento.setString(3, txtTelefono.getText());
                procedimiento.setString(4, txtEmail.getText());
                procedimiento.setString(5, txtDireccion.getText());
                procedimiento.execute();
                TrayNotification tray = new TrayNotification("GUARDAR", "El cliente fue modificado", NotificationType.SUCCESS);
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
            TrayNotification tray = new TrayNotification("ACTUALIZAR", "Debes ingresar una razon social", NotificationType.ERROR);
            tray.setAnimationType(AnimationType.FADE);
            tray.showAndDismiss(Duration.seconds(1));
            txtRazonSocial.requestFocus();
        } else if (txtDireccion.getText().isEmpty()) {
            TrayNotification tray = new TrayNotification("ACTUALIZAR", "Debes ingresar una direccion", NotificationType.ERROR);
            tray.setAnimationType(AnimationType.FADE);
            tray.showAndDismiss(Duration.seconds(1));
            txtDireccion.requestFocus();
        } else if (!validarEmail()) {
            TrayNotification tray = new TrayNotification("ACTUALIZAR", "Debes ingresar un Email valido", NotificationType.ERROR);
            tray.setAnimationType(AnimationType.FADE);
            tray.showAndDismiss(Duration.seconds(1));
            txtEmail.requestFocus();
        } else if (!validarTelefono()) {
            TrayNotification tray = new TrayNotification("ACTUALIZAR", "Debes ingresar un numero telefonico valido", NotificationType.ERROR);
            tray.setAnimationType(AnimationType.FADE);
            tray.showAndDismiss(Duration.seconds(1));
            txtTelefono.requestFocus();
        }  else {
            actualizarCliente();
        }
    }
        
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
