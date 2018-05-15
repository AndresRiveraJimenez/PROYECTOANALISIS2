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
import org.andres.bean.Tecnico;
import org.andres.recursos.FxDialogs;
import org.andres.sistema.Principal;
import org.andresrivera.conexion.Conexion;
import tray.animations.AnimationType;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

public class ControladorTecnico implements Initializable{
    private Principal escenarioPrincipal;
    
    @FXML private TableView<Tecnico> tblTecnicos;
    @FXML private TableColumn colIdTecnico;
    @FXML private TableColumn colNombre;
    @FXML private TableColumn colApellido;
    @FXML private TableColumn colTelefono;
    @FXML private TableColumn colCorreo;
    @FXML private TableColumn colUsuarioD;
    @FXML private TableColumn colDepartamento;
    
    @FXML private TextField txtID;
    @FXML private TextField txtNombre;
    @FXML private TextField txtApellido;
    @FXML private TextField txtTelefono;
    @FXML private TextField txtEmail;
    @FXML private TextField txtUsuarioD;
    @FXML private TextField txtDepartamento;
    @FXML private TextField txtSearch;
   
    private ObservableList<Tecnico> listaTecnicos;
   
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        mostrarDatos();
    }
    
    public void mostrarDatos() {
        tblTecnicos.setItems(getListaTecnicos());
        colIdTecnico.setCellValueFactory(new PropertyValueFactory<Tecnico, Integer>("idTecnico"));
        colNombre.setCellValueFactory(new PropertyValueFactory<Tecnico, String>("nombre"));
        colApellido.setCellValueFactory(new PropertyValueFactory<Tecnico, String>("apellido"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<Tecnico, String>("numero"));
        colCorreo.setCellValueFactory(new PropertyValueFactory<Tecnico, String>("correo"));
        colUsuarioD.setCellValueFactory(new PropertyValueFactory<Tecnico, String>("usuarioD"));
        colDepartamento.setCellValueFactory(new PropertyValueFactory<Tecnico, String>("departamento"));
       
        txtSearch.textProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                filtroTecnicoBuscar((String) oldValue, (String) newValue);
            }
        });
    }

    public ObservableList<Tecnico> getListaTecnicos() {
        ResultSet tecnico = Conexion.getInstancia().hacerConsulta("select a.idTecnico, a.nombreTecnico, "
                + "a.apellidoTecnico , a.numeroTel, a.correo, a.usuarioDominio, d.nombreDpto, a.estado "
                + "from Tecnicos a , Departamentos d  where a.idDepto = d.idDpto and a.estado = 1");
        ArrayList<Tecnico> lista = new ArrayList<Tecnico>();
        try {
            while (tecnico.next()) {
                lista.add(new Tecnico(tecnico.getInt("idTecnico"),
                        tecnico.getString("nombreTecnico"),
                        tecnico.getString("apellidoTecnico"),
                        tecnico.getString("numeroTel"),
                        tecnico.getString("correo"),
                        tecnico.getString("usuarioDominio"),
                        tecnico.getString("nombreDpto"),
                        tecnico.getInt("estado")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listaTecnicos = FXCollections.observableArrayList(lista);
    }
    
    public void tecnicoSeleccionado(){
        if(tblTecnicos.getSelectionModel().getSelectedItem()!= null){
            txtID.setText(String.valueOf(tblTecnicos.getSelectionModel().getSelectedItem().getIdTecnico()));
            txtNombre.setText(tblTecnicos.getSelectionModel().getSelectedItem().getNombre());
            txtApellido.setText(tblTecnicos.getSelectionModel().getSelectedItem().getApellido());
            txtTelefono.setText(tblTecnicos.getSelectionModel().getSelectedItem().getTelefono());
            txtEmail.setText(tblTecnicos.getSelectionModel().getSelectedItem().getCorreo());
            txtUsuarioD.setText(tblTecnicos.getSelectionModel().getSelectedItem().getUsuarioD());
            txtDepartamento.setText(tblTecnicos.getSelectionModel().getSelectedItem().getDepartamento());
        }else
        {
            TrayNotification tray = new TrayNotification("ERROR", "Debe seleccionar un Tecnico", NotificationType.ERROR);
            tray.setAnimationType(AnimationType.POPUP);
            tray.showAndDismiss(Duration.seconds(1));
        }
    }
     
        public void agregarTecnico() {
            escenarioPrincipal.ventanaAgregarTecnico();
        }
        public void modificarTecnico() {

            if (txtID.getText().isEmpty()) {

                TrayNotification tray = new TrayNotification("MODIFICAR", "Debe seleccionar un Tecnico", NotificationType.ERROR);
                tray.setAnimationType(AnimationType.POPUP);
                tray.showAndDismiss(Duration.seconds(1));
            } else {
                int idTecnicoModificar = Integer.valueOf(txtID.getText());
                escenarioPrincipal.ventanaModificarTecnico(idTecnicoModificar);
            }
        }
        public void eliminarTecnico(){
            if(txtID.getText().isEmpty())
            {
                TrayNotification tray = new TrayNotification("Eliminar", "Debe seleccionar un Tecnico", NotificationType.ERROR);
                tray.setAnimationType(AnimationType.POPUP);
                tray.showAndDismiss(Duration.seconds(1)); 
            }
            else{
              CallableStatement  procedimiento;
              if (FxDialogs.showConfirm("Eliminar Tecnico", "Desea eliminar el Tecnico seleccionado?", FxDialogs.YES, FxDialogs.NO).equals(FxDialogs.YES)) {
                        try {
                        procedimiento = (CallableStatement ) Conexion.getInstancia().getConexion().prepareCall("{call sp_EliminarTecnico(?)}");
                        procedimiento.setString(1, txtID.getText());
                        procedimiento.execute();
                        TrayNotification tray = new TrayNotification("ELIMINAR", "El Tecnico fue eliminado", NotificationType.SUCCESS);
                        tray.setAnimationType(AnimationType.POPUP);
                        tray.showAndDismiss(Duration.seconds(1));
                        mostrarDatos();
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                        }
                 }
            }
        }
    public void filtroTecnicoBuscar(String oldValue, String newValue) {
        ObservableList<Tecnico> filteredList = FXCollections.observableArrayList();
        if (txtSearch == null || (newValue.length() < oldValue.length()) || newValue == null) {
            tblTecnicos.setItems(getListaTecnicos());
        } else {
            newValue = newValue.toUpperCase();
            for (Tecnico tecnico : tblTecnicos.getItems()) {
                String nombre = tecnico.getNombre();
                if (nombre.toUpperCase().contains(newValue)) {
                    filteredList.add(tecnico);
                }
            }
            tblTecnicos.setItems(filteredList);
        }
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
