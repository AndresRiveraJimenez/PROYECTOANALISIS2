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
import org.andres.bean.Clientes;
import org.andres.recursos.FxDialogs;
import org.andres.sistema.Principal;
import org.andresrivera.conexion.Conexion;
import tray.animations.AnimationType;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;
public class ControladorClientes implements Initializable {
    private Principal escenarioPrincipal;
    @FXML private TableView<Clientes> tblClientes;
    @FXML private TableColumn colIDCliente;
    @FXML private TableColumn colRazonSocial;
    @FXML private TableColumn colTelefono;
    @FXML private TableColumn colCorreo;
    @FXML private TableColumn colDireccion;
    @FXML private TextField txtID;
    @FXML private TextField txtRazonSocial;
    @FXML private TextField txtDireccion;
    @FXML private TextField txtEmail;
    @FXML private TextField txtTelefono;
    @FXML private TextField txtSearch;
    private ObservableList<Clientes> listaClientes;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        mostrarDatos();
    }
    public void mostrarDatos(){
        tblClientes.setItems(getListaClientes());
        colIDCliente.setCellValueFactory(new PropertyValueFactory<Clientes, Integer>("idCliente"));
        colRazonSocial.setCellValueFactory(new PropertyValueFactory<Clientes,String>("razonSocial"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<Clientes,String>("telefono"));
        colCorreo.setCellValueFactory(new PropertyValueFactory<Clientes,String>("correo"));
        colDireccion.setCellValueFactory(new PropertyValueFactory<Clientes,String>("direccion"));
                txtSearch.textProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                filtroClientesBuscar((String) oldValue, (String) newValue);

            }
        });
    }
    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }

    public ObservableList<Clientes> getListaClientes() {
        ResultSet cliente = Conexion.getInstancia().hacerConsulta("select a.idCliente, a.razonSocial, a.telefono, a.correo, a.direccion, a.estado from Clientes a where a.estado = 1");
        ArrayList<Clientes> lista = new ArrayList<Clientes>();
        try{
            while(cliente.next()){
                lista.add(new Clientes(cliente.getInt("idCliente"),
                                       cliente.getString("razonSocial"), 
                                       cliente.getString("telefono"),
                                       cliente.getString("correo"),
                                       cliente.getString("direccion"), 
                                       cliente.getInt("estado")));
                
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return listaClientes= FXCollections.observableArrayList(lista);
    }
    public void clienteSeleccionado(){
        if(tblClientes.getSelectionModel().getSelectedItem()!= null){
            txtID.setText(String.valueOf(tblClientes.getSelectionModel().getSelectedItem().getIdCliente()));
            txtRazonSocial.setText(tblClientes.getSelectionModel().getSelectedItem().getRazonSocial());
            txtTelefono.setText(tblClientes.getSelectionModel().getSelectedItem().getTelefono());
            txtEmail.setText(tblClientes.getSelectionModel().getSelectedItem().getCorreo());
            txtDireccion.setText(tblClientes.getSelectionModel().getSelectedItem().getDireccion());
        }else
        {
            TrayNotification tray = new TrayNotification("ERROR", "Debe seleccionar un cliente", NotificationType.ERROR);
            tray.setAnimationType(AnimationType.POPUP);
            tray.showAndDismiss(Duration.seconds(1));
        }
    }
        public void agregarCliente(){
        escenarioPrincipal.ventanaAgregarCliente();
    }
        public void modificarCliente() {

        if (txtID.getText().isEmpty()) {

            TrayNotification tray = new TrayNotification("MODIFICAR", "Debe seleccionar un Cliente", NotificationType.ERROR);
            tray.setAnimationType(AnimationType.POPUP);
            tray.showAndDismiss(Duration.seconds(1));
        } else {
            int idClienteModificar = Integer.valueOf(txtID.getText());
            escenarioPrincipal.ventanaModificarCliente(idClienteModificar);
        }
    }
        public void eliminarCliente(){
            if(txtID.getText().isEmpty())
            {
                TrayNotification tray = new TrayNotification("Eliminar", "Debe seleccionar un Cliente", NotificationType.ERROR);
                tray.setAnimationType(AnimationType.POPUP);
                tray.showAndDismiss(Duration.seconds(1)); 
            }
            else{
              CallableStatement  procedimiento;
              if (FxDialogs.showConfirm("Eliminar Cliente", "Desea eliminar el cliente seleccionado?", FxDialogs.YES, FxDialogs.NO).equals(FxDialogs.YES)) {
                        try {
                        procedimiento = (CallableStatement ) Conexion.getInstancia().getConexion().prepareCall("{call sp_EliminarCliente(?)}");
                        procedimiento.setString(1, txtID.getText());
                        procedimiento.execute();
                        TrayNotification tray = new TrayNotification("ELIMINAR", "El cliente fue eliminado", NotificationType.SUCCESS);
                        tray.setAnimationType(AnimationType.POPUP);
                        tray.showAndDismiss(Duration.seconds(1));
                        
                        txtID.setText("");
                        txtRazonSocial.setText("");
                        txtEmail.setText("");
                        txtDireccion.setText("");
                        txtTelefono.setText("");
                        mostrarDatos();
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                        }
                 }
            }
        }
        public void filtroClientesBuscar(String oldValue, String newValue) {
        ObservableList<Clientes> filteredList = FXCollections.observableArrayList();
        if (txtSearch == null || (newValue.length() < oldValue.length()) || newValue == null) {
            tblClientes.setItems(getListaClientes());
        } else {
            newValue = newValue.toUpperCase();
            for (Clientes cliente : tblClientes.getItems()) {
                String razonSocial = cliente.getRazonSocial();
                if (razonSocial.toUpperCase().contains(newValue)) {
                    filteredList.add(cliente);
                }
            }
            tblClientes.setItems(filteredList);
        }
    }
        public void ventanaPrincipal()
        {
            escenarioPrincipal.ventanaPrincipal();
        }
        public void cerrar(){
        escenarioPrincipal.cerrar();
    }
}
