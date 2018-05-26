/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.andres.controladores;

import java.net.URL;
import java.sql.CallableStatement;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.util.Duration;
import org.andres.bean.Asignaciones;
import org.andres.bean.Clientes;
import org.andres.bean.Tecnico;
import org.andres.sistema.Principal;
import org.andresrivera.conexion.Conexion;
import tray.animations.AnimationType;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

/**
 *
 * @author d5
 */
public class ControladorModificarDashboard implements Initializable{
    @FXML private DatePicker fecha;
    @FXML private ComboBox cmbTecnico;
    @FXML private ComboBox cmbCliente;
    @FXML private TextArea txtDescripcion;
    private int idAsignacion;
    private Asignaciones asignacion;
    private Asignaciones asignacionModificar;
    private Principal escenarioPrincipal;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cmbTecnico.setItems(getListaTecnico());
        cmbCliente.setItems(getListaClientes());
    }

    public void setIdAsignacion(int idAsignacion) {
        this.idAsignacion = idAsignacion;
        this.asignacionModificar = buscarAsignacion(idAsignacion);
        fecha.setValue( asignacionModificar.getFecha().toLocalDate());
        cmbCliente.setValue( asignacionModificar.getCliente());
        cmbTecnico.setValue( asignacionModificar.getTecnico());
        txtDescripcion.setText(asignacionModificar.getDescripcion());
    }
    public Asignaciones buscarAsignacion(int idAsignacionModificar) {
        Asignaciones resultado = null;
        try {
            PreparedStatement procedimiento = (PreparedStatement) Conexion.getInstancia().getConexion().prepareStatement("{call sp_BuscarAsignacion(?)}");
            procedimiento.setInt(1, idAsignacionModificar);
            ResultSet asignacion = procedimiento.executeQuery();
            while (asignacion.next()) {
                resultado = new Asignaciones(
                        asignacion.getInt("idAsignacion"), asignacion.getDate("fecha"), asignacion.getString("descripcion"),
                        new Tecnico(asignacion.getInt("idTecnico"),asignacion.getString("nombreTecnico")), 
                        new Clientes(asignacion.getInt("idCliente"), asignacion.getString("razonSocial")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultado;
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
        escenarioPrincipal.cerrarModalCrear();
    }
        public ObservableList<Tecnico> getListaTecnico() {
        ResultSet tecnicos = Conexion.getInstancia().hacerConsulta("exec sp_ListarTecnico");
        ArrayList<Tecnico> lista = new ArrayList<Tecnico>();
        try {
            while (tecnicos.next()) {
                lista.add(new Tecnico(tecnicos.getInt("idTecnico"), tecnicos.getString("nombreTecnico")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return FXCollections.observableArrayList(lista);
    }
        public ObservableList<Clientes> getListaClientes(){
            ResultSet clientes = Conexion.getInstancia().hacerConsulta("exec sp_ListarCliente");
            ArrayList<Clientes> lista = new ArrayList<Clientes>();
            try {
                while (clientes.next()) {
                    lista.add(new Clientes(clientes.getInt("idCliente"), clientes.getString("razonSocial")));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return FXCollections.observableArrayList(lista);
        }
 public void guardarAsignacion(){
                CallableStatement  procedimiento;
        try {
            procedimiento = (CallableStatement ) Conexion.getInstancia().getConexion().prepareCall("{call sp_ModificarAsignacion(?,?,?,?,?)}");
                Date fecha1 = Date.valueOf(fecha.getValue());
                procedimiento.setInt(1, idAsignacion);
                procedimiento.setDate(2, fecha1);
                procedimiento.setInt(3, ((Tecnico)(cmbTecnico.getSelectionModel().getSelectedItem())).getIdTecnico());
                procedimiento.setInt(4, ((Clientes)(cmbCliente.getSelectionModel().getSelectedItem())).getIdCliente());
                procedimiento.setString(5, txtDescripcion.getText());
                procedimiento.execute();
                TrayNotification tray = new TrayNotification("GUARDAR", "Asignacion modificada", NotificationType.SUCCESS);
                tray.setAnimationType(AnimationType.POPUP);
                tray.showAndDismiss(Duration.seconds(1));
                escenarioPrincipal.ventanaDashboardAsig();
                cerrar();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
 
     public void validarDatos(){
              
        if (fecha.getValue() == null) {
            TrayNotification tray = new TrayNotification("GUARDAR", "Debes ingresar un fecha", NotificationType.ERROR);
            tray.setAnimationType(AnimationType.FADE);
            tray.showAndDismiss(Duration.seconds(1));
            fecha.requestFocus();
        }else if(txtDescripcion.getText().isEmpty()){
            TrayNotification tray = new TrayNotification("GUARDAR", "Debes ingresar una descripcion", NotificationType.ERROR);
            tray.setAnimationType(AnimationType.FADE);
            tray.showAndDismiss(Duration.seconds(1));
            txtDescripcion.requestFocus();
        }else{
            guardarAsignacion();
        }
    }
    
 
 
 
}
