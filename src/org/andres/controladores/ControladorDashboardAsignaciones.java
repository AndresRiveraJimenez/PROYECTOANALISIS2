/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.andres.controladores;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.CallableStatement;
import java.sql.Date;
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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Duration;
import org.andres.bean.Asignaciones;
import org.andres.bean.Clientes;
import org.andres.bean.Tecnico;
import org.andres.bean.Usuario;
import org.andres.recursos.FxDialogs;
import org.andres.sistema.Principal;
import org.andresrivera.conexion.Conexion;
import tray.animations.AnimationType;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

/**
 *
 * @author d5
 */
public class ControladorDashboardAsignaciones implements Initializable{
    @FXML private TableView<Asignaciones> tblAsignaciones;
    @FXML private TableColumn colIdAsignacion;
    @FXML private TableColumn colFecha;
    @FXML private TableColumn colTecnico;
    @FXML private TableColumn colRazonSocial;
    @FXML private TableColumn colDescripcion;
    @FXML private Label labelUserAuth;
    private ObservableList<Asignaciones> listaAsignaciones;
    private Usuario userAuth;
    private Principal escenarioPrincipal;
    @FXML private TextField txtIdAsignacion;
    @FXML private TextField txtFecha;
    @FXML private TextField txtTecnico;
    @FXML private TextField txtCliente;
    @FXML private TextArea txtDescripcion;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        mostrarDatos();
    }

    public void setUserAuth(Usuario userAuth){
        this.userAuth = userAuth;
        labelUserAuth.setText(userAuth.getUser());
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
    public void crearAsignacion(){
        escenarioPrincipal.ventanaCrearAsignacion();
    }
    public void modificarAsignacion(){
        escenarioPrincipal.ventanaModificarAsignacion(Integer.valueOf(txtIdAsignacion.getText()));
    }
    public void linkDashboard() throws URISyntaxException, IOException{
        Desktop.getDesktop().browse(new URI("http://localhost:59102/Asignaciones/Index"));
    }

    public ObservableList<Asignaciones> getListaAsignaciones() {
         ResultSet asignacion = Conexion.getInstancia().hacerConsulta("select a.idAsignacion, a.fecha, c.nombreTecnico, d.razonSocial, b.descripcion \n" +
"from Asignaciones a, detalleAsignaciones b, Tecnicos c, Clientes d \n" +
"where a.idAsignacion = b.idAsignacion and a.idTecnico = c.idTecnico and b.idCliente = d.idCliente and a.estado =1");
        ArrayList<Asignaciones> lista = new ArrayList<Asignaciones>();
        try{
            while(asignacion.next()){
                lista.add(new Asignaciones(asignacion.getInt("idAsignacion"), 
                        asignacion.getDate("fecha"), asignacion.getString("nombreTecnico"),
                        asignacion.getString("razonSocial"), asignacion.getString("descripcion")));                        
                
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return listaAsignaciones = FXCollections.observableArrayList(lista);
    }
    
    public void mostrarDatos(){
         tblAsignaciones.setItems(getListaAsignaciones());
         colIdAsignacion.setCellValueFactory(new PropertyValueFactory<Asignaciones, Integer>("idAsignacion"));
        colFecha.setCellValueFactory(new PropertyValueFactory<Asignaciones, Date>("fecha"));
        colTecnico.setCellValueFactory(new PropertyValueFactory<Asignaciones,String>("nombreTecnico"));
        colRazonSocial.setCellValueFactory(new PropertyValueFactory<Asignaciones,String>("razonSocial"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<Clientes,String>("Descripcion"));
    }
        public void clienteSeleccionado(){
        if(tblAsignaciones.getSelectionModel().getSelectedItem()!= null){
            Asignaciones asignacion = (Asignaciones)tblAsignaciones.getSelectionModel().getSelectedItem();
            txtIdAsignacion.setText(String.valueOf(asignacion.getIdAsignacion()));
            txtFecha.setText(String.valueOf(asignacion.getFecha()));
            txtTecnico.setText(asignacion.getNombreTecnico());
            txtCliente.setText(asignacion.getRazonSocial());
            txtDescripcion.setText(asignacion.getDescripcion());
        }else
        {
            TrayNotification tray = new TrayNotification("ERROR", "Debe seleccionar una asignacion", NotificationType.ERROR);
            tray.setAnimationType(AnimationType.POPUP);
            tray.showAndDismiss(Duration.seconds(1));
        }
    }
             public void eliminarCliente(){
            if(txtIdAsignacion.getText().isEmpty())
            {
                TrayNotification tray = new TrayNotification("Eliminar", "Debe seleccionar una Asgnacion", NotificationType.ERROR);
                tray.setAnimationType(AnimationType.POPUP);
                tray.showAndDismiss(Duration.seconds(1)); 
            }
            else{
              CallableStatement  procedimiento;
              if (FxDialogs.showConfirm("Eliminar Asignacion", "Desea eliminar la asignacion seleccionada?", FxDialogs.SI, FxDialogs.NO).equals(FxDialogs.SI)) {
                        try {
                        procedimiento = (CallableStatement ) Conexion.getInstancia().getConexion().prepareCall("{call sp_EliminarAsignacion(?)}");
                        procedimiento.setInt(1, Integer.valueOf(txtIdAsignacion.getText()));
                        procedimiento.execute();
                        TrayNotification tray = new TrayNotification("ELIMINAR", "La asignacion fue eliminada", NotificationType.SUCCESS);
                        tray.setAnimationType(AnimationType.POPUP);
                        tray.showAndDismiss(Duration.seconds(1));
                        
                        txtIdAsignacion.setText("");
                        txtFecha.setText("");
                        txtTecnico.setText("");
                        txtCliente.setText("");
                        txtDescripcion.setText("");
                        mostrarDatos();
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                        }
                 }
            }
        }
}
