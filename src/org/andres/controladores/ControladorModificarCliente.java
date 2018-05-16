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
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
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
}
