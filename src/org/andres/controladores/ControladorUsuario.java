package org.andres.controladores;

import java.net.URL;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Duration;
import org.andres.bean.Rol;
import org.andres.bean.Usuario;
import org.andres.recursos.FxDialogs;
import org.andres.sistema.Principal;
import org.andresrivera.conexion.Conexion;
import tray.animations.AnimationType;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

public class ControladorUsuario implements Initializable {
    private Principal escenarioPrincipal;

    @FXML private TableView<Usuario> tblUsuario;
    @FXML private TableColumn colIdUsuario;
    @FXML private TableColumn colNombre;
    @FXML private TableColumn colApellido;
    @FXML private TableColumn colNombreUsuario;
    @FXML private TableColumn colRole;
    
    @FXML private TextField txtID;
    @FXML private TextField txtNombre;
    @FXML private TextField txtApellido;
    @FXML private TextField txtNombreUsuario;
    @FXML private TextField txtRol;
    @FXML private TextField txtSearch;

    private ObservableList<Usuario> listaUsuarios;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        mostrarDatos();
    }
    
    
    public void mostrarDatos() {
        tblUsuario.setItems(getListaUsuarios());
        colIdUsuario.setCellValueFactory(new PropertyValueFactory<Usuario, Integer>("idUsuario"));
        colNombre.setCellValueFactory(new PropertyValueFactory<Usuario, String>("nombre"));
        colApellido.setCellValueFactory(new PropertyValueFactory<Usuario, String>("apellido"));
        colNombreUsuario.setCellValueFactory(new PropertyValueFactory<Usuario, String>("user"));
        colRole.setCellValueFactory(new PropertyValueFactory<Usuario, String>("nombreRol"));

        txtSearch.textProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                filtroUsuarioBuscar((String) oldValue, (String) newValue);
            }
        });
    }
    
    public ObservableList<Usuario> getListaUsuarios() {
        ResultSet usuario = Conexion.getInstancia().hacerConsulta("SELECT u.idUsuario, u.nombre, u.apellido, u.nombreUsuario, "
                + "u.contraseña,u.estado, r.idRol, r.nombreRol, r.descripcion FROM Usuarios u, Roles r where u.idRol = r.idRol "
                + "and u.estado =1;");
        ArrayList<Usuario> lista = new ArrayList<Usuario>();
        try {
            while (usuario.next()) {
                lista.add(new Usuario(usuario.getInt("idUsuario"),
                        usuario.getString("nombre"),
                        usuario.getString("apellido"),
                        usuario.getString("nombreUsuario"),
                        usuario.getString("contraseña"),
                        usuario.getInt("estado"),
                        new Rol(usuario.getInt("idRol"), usuario.getString("nombreRol"),
                        usuario.getString("descripcion"))));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listaUsuarios = FXCollections.observableArrayList(lista);
    }
    
    
    public void usuarioSeleccionado(){
        if(tblUsuario.getSelectionModel().getSelectedItem()!= null){
            txtID.setText(String.valueOf(tblUsuario.getSelectionModel().getSelectedItem().getIdUsuario()));
            txtNombre.setText(tblUsuario.getSelectionModel().getSelectedItem().getNombre());
            txtApellido.setText(tblUsuario.getSelectionModel().getSelectedItem().getApellido());
            txtNombreUsuario.setText(tblUsuario.getSelectionModel().getSelectedItem().getUser());
            txtRol.setText(tblUsuario.getSelectionModel().getSelectedItem().getRol().getNombre());
        }else
        {
            TrayNotification tray = new TrayNotification("ERROR", "Debe seleccionar un Usuario", NotificationType.ERROR);
            tray.setAnimationType(AnimationType.POPUP);
            tray.showAndDismiss(Duration.seconds(1));
        }
    }
    
     public void modificarUsuario() {

            if (txtID.getText().isEmpty()) {

                TrayNotification tray = new TrayNotification("MODIFICAR", "Debe seleccionar un Usuario", NotificationType.ERROR);
                tray.setAnimationType(AnimationType.POPUP);
                tray.showAndDismiss(Duration.seconds(1));
            } else {
                int idUsuarioModificar = Integer.valueOf(txtID.getText());
                escenarioPrincipal.ventanaModificarUsuario(idUsuarioModificar);
            }
        }
        public void eliminarUsuario(){
            if(txtID.getText().isEmpty())
            {
                TrayNotification tray = new TrayNotification("Eliminar", "Debe seleccionar un Usuario", NotificationType.ERROR);
                tray.setAnimationType(AnimationType.POPUP);
                tray.showAndDismiss(Duration.seconds(1)); 
            }
            else{
              CallableStatement  procedimiento;
              if (FxDialogs.showConfirm("Eliminar Usuario", "Desea eliminar el Usuario seleccionado?", FxDialogs.YES, FxDialogs.NO).equals(FxDialogs.YES)) {
                        try {
                        procedimiento = (CallableStatement ) Conexion.getInstancia().getConexion().prepareCall("{call sp_EliminarUsuario(?)}");
                        procedimiento.setString(1, txtID.getText());
                        procedimiento.execute();
                        TrayNotification tray = new TrayNotification("ELIMINAR", "El Usuario fue eliminado", NotificationType.SUCCESS);
                        tray.setAnimationType(AnimationType.POPUP);
                        tray.showAndDismiss(Duration.seconds(1));
                        mostrarDatos();
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                        }
                 }
            }
        }
    
    
    public void filtroUsuarioBuscar(String oldValue, String newValue) {
        ObservableList<Usuario> filteredList = FXCollections.observableArrayList();
        if (txtSearch == null || (newValue.length() < oldValue.length()) || newValue == null) {
            tblUsuario.setItems(getListaUsuarios());
        } else {
            newValue = newValue.toUpperCase();
            for (Usuario usuario : tblUsuario.getItems()) {
                String nombre = usuario.getNombre();
                if (nombre.toUpperCase().contains(newValue)) {
                    filteredList.add(usuario);
                }
            }
            tblUsuario.setItems(filteredList);
        }
    }
   
    public void agregarUsuario() {
        escenarioPrincipal.ventanaAgregarUsuario();
    }
    
    public void ventanaPrincipal() {
        escenarioPrincipal.ventanaPrincipal();
    }

    public void cerrar() {
        escenarioPrincipal.cerrar();
    }
    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
}
