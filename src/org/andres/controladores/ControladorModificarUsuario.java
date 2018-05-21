package org.andres.controladores;

import java.net.URL;
import java.sql.CallableStatement;
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
import javafx.scene.control.TextField;
import javafx.util.Duration;
import org.andres.bean.Rol;
import org.andres.bean.Usuario;
import org.andres.sistema.Principal;
import org.andresrivera.conexion.Conexion;
import tray.animations.AnimationType;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;


public class ControladorModificarUsuario implements Initializable{

    @FXML private TextField txtID;
    @FXML private TextField txtNombre;
    @FXML private TextField txtApellido;
    @FXML private TextField txtNombreUsuario;
    @FXML private TextField txtPassword;
    @FXML private ComboBox txtRol;

    private Usuario usuarioModificar;
    private ArrayList<Rol> listaRol;
    private Principal escenarioPrincipal;
    private int rolEscogido;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        txtRol.setItems(getListaRol());
    }
    
    public void setUsuariooModificar(int idUsuario) {
        this.usuarioModificar = buscarUsuario(idUsuario);
        txtID.setText(String.valueOf(usuarioModificar.getIdUsuario()));
        txtNombre.setText(String.valueOf(usuarioModificar.getNombre()));
        txtApellido.setText(String.valueOf(usuarioModificar.getApellido()));
        txtNombreUsuario.setText(String.valueOf(usuarioModificar.getUser()));
        txtPassword.setText("");
        txtRol.setValue(String.valueOf(usuarioModificar.getRol().getNombre()));

    }
    
    public ObservableList<String> getListaRol() {

        ResultSet roles = Conexion.getInstancia().hacerConsulta("select * from  Roles");
        listaRol = new ArrayList<Rol>();
        ArrayList<String> lista = new ArrayList<String>();
        try {
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
 
    public Usuario buscarUsuario(int idUsuarioModificar) {
        Usuario resultado = null;
        try {
            PreparedStatement procedimiento = (PreparedStatement) Conexion.getInstancia().getConexion().prepareStatement("{call sp_BuscarUsuario(?)}");
            procedimiento.setInt(1, idUsuarioModificar);
            ResultSet usuario = procedimiento.executeQuery();
            while (usuario.next()) {
                resultado = new Usuario(usuario.getInt("idUsuario"), usuario.getString("nombre"),
                        usuario.getString("apellido"),usuario.getString("nombreUsuario"), 
                        new Rol(usuario.getInt("idRol"), usuario.getString("nombreRol"), usuario.getString("descripcion") ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultado;
    }
    
    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }

    
    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void cerrar() {
        escenarioPrincipal.cerrarModalCrear();
    }
    
    
    public void actualizarUsuario() {
        CallableStatement procedimiento;
        String pass = txtPassword.getText().isEmpty()? "vacio" : txtPassword.getText() ;

        try {
            procedimiento = (CallableStatement ) Conexion.getInstancia().getConexion().prepareCall("{call sp_ActualizarUsuario(?,?,?,?,?,?)}");
            
                procedimiento.setInt(1, Integer.valueOf(txtID.getText()));
                procedimiento.setString(2, txtNombre.getText());
                procedimiento.setString(3, txtApellido.getText());
                procedimiento.setString(4, txtNombreUsuario.getText());
                procedimiento.setString(5, pass);
                procedimiento.setInt(6, rolEscogido);
                procedimiento.execute();
                TrayNotification tray = new TrayNotification("Actualizado", "Usuario Actualizado", NotificationType.SUCCESS);
                tray.setAnimationType(AnimationType.POPUP);
                tray.showAndDismiss(Duration.seconds(1));
                escenarioPrincipal.ventanaUsuario();
                cerrar();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    public void seleccionarItemRol() {

        String rol = txtRol.getValue().toString();

        for (Rol ro : listaRol) {
            if (ro.getNombre().equals(rol)) {
                rolEscogido = ro.getIdRol();
            }
        }
    }
    
    public boolean validarSiExisteUsuario(){
        
        if(txtNombreUsuario.getText().equals(usuarioModificar.getUser())){
            return false;
        }
        CallableStatement procedimiento;
        ResultSet usuario;

        try {
            procedimiento = (CallableStatement ) Conexion.getInstancia().getConexion().prepareCall("{call sp_validarSiExisteUsuario(?)}");
            
                procedimiento.setString(1, txtNombreUsuario.getText());
                procedimiento.execute();
                
                usuario = procedimiento.getResultSet();
                usuario.next();
                if(usuario.getString(1).equals("si")){
                    return true;
                }                                 
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }
    
    public void validarCamposActualizar() {
        if (txtNombre.getText().isEmpty()) {
            TrayNotification tray = new TrayNotification("Actualizado", "Debes ingresar un nombre", NotificationType.ERROR);
            tray.setAnimationType(AnimationType.FADE);
            tray.showAndDismiss(Duration.seconds(1));
            txtNombre.requestFocus();
        } else if (txtApellido.getText().isEmpty()) {
            TrayNotification tray = new TrayNotification("Actualizado", "Debes ingresar un Apellido", NotificationType.ERROR);
            tray.setAnimationType(AnimationType.FADE);
            tray.showAndDismiss(Duration.seconds(1));
            txtApellido.requestFocus();
        } else if (txtNombreUsuario.getText().isEmpty()) {
            TrayNotification tray = new TrayNotification("Actualizado", "Debes ingresar un nombre de usuario", NotificationType.ERROR);
            tray.setAnimationType(AnimationType.FADE);
            tray.showAndDismiss(Duration.seconds(1));
            txtNombreUsuario.requestFocus();
        }else if (validarSiExisteUsuario()) {
            TrayNotification tray = new TrayNotification("Actualizado", "El usuario ya existe ingrese otro nombre de usuario", NotificationType.ERROR);
            tray.setAnimationType(AnimationType.FADE);
            tray.showAndDismiss(Duration.seconds(1));
            txtNombreUsuario.requestFocus();
        } else {
            seleccionarItemRol();
            actualizarUsuario();
        };
    }
}
