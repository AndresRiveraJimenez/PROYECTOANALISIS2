package org.andres.controladores;

import java.net.URL;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.util.Duration;
import org.andres.bean.Departamento;
import org.andres.bean.Rol;
import org.andres.bean.Usuario;
import org.andres.sistema.Principal;
import org.andresrivera.conexion.Conexion;
import tray.animations.AnimationType;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;


public class ControladorAgregarUsuario implements Initializable{

    @FXML private TextField txtID;
    @FXML private TextField txtNombre;
    @FXML private TextField txtApellido;
    @FXML private TextField txtNombreUsuario;
    @FXML private TextField txtPassword;
    @FXML private ComboBox txtRol;

 
    private ArrayList<Rol> listaRol;
    private Principal escenarioPrincipal;
    private int rolEscogido;
    
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
         agregarItemRoles();
    }
    
    public void agregarItemRoles(){
        txtRol.setItems(getListaRoles());
        txtRol.setValue(" ");
    }
    
    public ObservableList<String> getListaRoles() {

        ResultSet roles = Conexion.getInstancia().hacerConsulta("select * from  Roles");
        listaRol = new ArrayList<Rol>();
        ArrayList<String> lista = new ArrayList<String>();
        try {
            lista.add("");
            while (roles.next()) {
                listaRol.add(
                        new Rol(roles.getInt("idRol"),
                                roles.getString("nombreRol"),
                                roles.getString("descripcion")));
                lista.add(roles.getString("nombreRol"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return   FXCollections.observableArrayList(lista);
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
    
    public void guardarUsuario(){
                CallableStatement  procedimiento;
        try {
            procedimiento = (CallableStatement ) Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarUsuario(?,?,?,?,?)}");
            
                procedimiento.setString(1, txtNombre.getText());
                procedimiento.setString(2, txtApellido.getText());
                procedimiento.setString(3, txtNombreUsuario.getText());
                procedimiento.setString(4, txtPassword.getText());
                procedimiento.setInt(5, rolEscogido);

                procedimiento.execute();
                TrayNotification tray = new TrayNotification("GUARDAR", "Usuario nuevo agregado", NotificationType.SUCCESS);
                tray.setAnimationType(AnimationType.POPUP);
                tray.showAndDismiss(Duration.seconds(1));
                escenarioPrincipal.ventanaUsuario();
                cerrar();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    public boolean seleccionarItemRol(){
        
        String role = txtRol.getValue().toString();
        
        if(role.isEmpty()){
            return true;
        }else{
            for(Rol rol : listaRol){
                if(rol.getNombre().equals(role)){
                    rolEscogido = rol.getIdRol();
                    return false;
                }
            }
        }
   
        return true;
    }
    
        public void validarCamposGuardar() {
        if (txtNombre.getText().isEmpty()) {
            TrayNotification tray = new TrayNotification("GUARDAR", "Debes ingresar un nombre", NotificationType.ERROR);
            tray.setAnimationType(AnimationType.FADE);
            tray.showAndDismiss(Duration.seconds(1));
            txtNombre.requestFocus();
        } else if (txtApellido.getText().isEmpty()) {
            TrayNotification tray = new TrayNotification("GUARDAR", "Debes ingresar un Apellido", NotificationType.ERROR);
            tray.setAnimationType(AnimationType.FADE);
            tray.showAndDismiss(Duration.seconds(1));
            txtApellido.requestFocus();
        } else if (txtNombreUsuario.getText().isEmpty()) {
            TrayNotification tray = new TrayNotification("GUARDAR", "Debes ingresar un nombre de usuario", NotificationType.ERROR);
            tray.setAnimationType(AnimationType.FADE);
            tray.showAndDismiss(Duration.seconds(1));
            txtNombreUsuario.requestFocus();
        } else if (txtPassword.getText().isEmpty()) {
            TrayNotification tray = new TrayNotification("GUARDAR", "Debes ingresar una contraseña", NotificationType.ERROR);
            tray.setAnimationType(AnimationType.FADE);
            tray.showAndDismiss(Duration.seconds(1));
            txtPassword.requestFocus();
        } else if (seleccionarItemRol()) {
            TrayNotification tray = new TrayNotification("GUARDAR", "Por favor seleccione un Rol", NotificationType.ERROR);
            tray.setAnimationType(AnimationType.FADE);
            tray.showAndDismiss(Duration.seconds(1));
            txtRol.requestFocus();
        }else {
            guardarUsuario();
        }
    }
}
