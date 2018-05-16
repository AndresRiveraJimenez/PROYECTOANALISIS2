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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Duration;
import org.andres.bean.Boleta;
import org.andres.bean.Clientes;
import org.andres.recursos.FxDialogs;
import org.andres.sistema.Principal;
import org.andresrivera.conexion.Conexion;
import tray.animations.AnimationType;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

public class ControladorBoleta implements Initializable {

    private Principal escenarioPrincipal;

    @FXML private TableView<Boleta> tblBoletas;
    @FXML private TableColumn colIDBoleta;
    @FXML private TableColumn colFechaVisita;
    @FXML private TableColumn colHoraEntrada;
    @FXML private TableColumn colHoraSalida;
    @FXML private TableColumn colFechaCreacion;
    @FXML private TableColumn colCliente;
    @FXML private TableColumn colClienteID;
    @FXML private TableColumn colTecnico;
    
    @FXML private TextField txtSearch;

    @FXML private TextField txtID;
    @FXML private TextField txtFechaVisita;
    @FXML private TextField txtHoraEntrada;
    @FXML private TextField txtHoraSalida;
    @FXML private TextField txtMotivo;
    @FXML private TextField txtFechaCreada;
    @FXML private TextField txtCliente;
    @FXML private TextField txtTecnico;
    @FXML private TextArea txtDescripcion;
   
    private ObservableList<Boleta> listaBoletas;
    
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        mostrarDatos();
    }

    public void mostrarDatos() {
        tblBoletas.setItems(getListaBoletas());
        colIDBoleta.setCellValueFactory(new PropertyValueFactory<Boleta, String>("idBoleta"));
        colFechaVisita.setCellValueFactory(new PropertyValueFactory<Boleta, String>("fechaVisitaES"));
        colHoraEntrada.setCellValueFactory(new PropertyValueFactory<Boleta, String>("horaEntrada"));
        colHoraSalida.setCellValueFactory(new PropertyValueFactory<Boleta, String>("horaSalida"));
        colFechaCreacion.setCellValueFactory(new PropertyValueFactory<Boleta, String>("fechaCreadoES"));
        colCliente.setCellValueFactory(new PropertyValueFactory<Boleta, String>("nombeCliente"));
        colClienteID.setCellValueFactory(new PropertyValueFactory <Boleta ,String> ("idCliente"));
        colTecnico.setCellValueFactory(new PropertyValueFactory<Boleta, String>("tecnico"));
       
        txtSearch.textProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                filtroBoletaBuscar((String) oldValue, (String) newValue);
            }
        });
    }

    public ObservableList<Boleta> getListaBoletas() {
    
        ResultSet boleta = Conexion.getInstancia().hacerConsulta("select b.idBoleta, b.motivoVisita, b.fechaVisita, "
                + "b.horaEntrada, b.horaSalida, b.descTrabajo, b.fechaCreacion, c.razonSocial,b.idCliente, t.nombreTecnico, b.estado "
                + "from Boletas b, Tecnicos t, Clientes c  where t.idTecnico = b.idTecnico and c.idCliente = b.idCliente "
                + " and b.estado = 1");
        ArrayList<Boleta> lista = new ArrayList<Boleta>();
    
        try {
            while (boleta.next()) {
                lista.add(new Boleta(boleta.getInt("idBoleta"),
                        boleta.getString("motivoVisita"),
                        boleta.getDate("fechaVisita").toLocalDate(),
                        boleta.getTime("horaEntrada").toLocalTime(),
                        boleta.getTime("horaSalida").toLocalTime(),
                        boleta.getString("descTrabajo"),
                        boleta.getDate("fechaCreacion").toLocalDate(),
                        new Clientes(boleta.getInt("idCliente"),boleta.getString("razonSocial")),
                        boleta.getString("nombreTecnico"),
                        boleta.getInt("estado")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return listaBoletas = FXCollections.observableArrayList(lista);
    }
    

    public void filtroBoletaBuscar(String oldValue, String newValue) {
        ObservableList<Boleta> filteredList = FXCollections.observableArrayList();
        if (txtSearch == null || (newValue.length() < oldValue.length()) || newValue == null) {
            tblBoletas.setItems(getListaBoletas());
        } else {
            newValue = newValue.toUpperCase();
            for (Boleta boleta : tblBoletas.getItems()) {
                String nombre = boleta.getCliente().getRazonSocial();
                if (nombre.toUpperCase().contains(newValue)) {
                    filteredList.add(boleta);
                }
            }
            tblBoletas.setItems(filteredList);
        }
    }
        
    public void ventanaPrincipal() {
        escenarioPrincipal.ventanaPrincipal();
    }

    public void boletaSeleccionada() {
        if (tblBoletas.getSelectionModel().getSelectedItem() != null) {
            txtID.setText(String.valueOf(tblBoletas.getSelectionModel().getSelectedItem().getIdBoleta()));
            txtFechaVisita.setText(tblBoletas.getSelectionModel().getSelectedItem().getFechaVisitaES());
            txtHoraEntrada.setText(tblBoletas.getSelectionModel().getSelectedItem().getHoraEntrada().toString());
            txtHoraSalida.setText(tblBoletas.getSelectionModel().getSelectedItem().getHoraSalida().toString());
            txtMotivo.setText(tblBoletas.getSelectionModel().getSelectedItem().getMotivo());
            txtFechaCreada.setText(tblBoletas.getSelectionModel().getSelectedItem().getFechaCreadoES());
            txtDescripcion.setText(tblBoletas.getSelectionModel().getSelectedItem().getDescripcion());
            txtCliente.setText(tblBoletas.getSelectionModel().getSelectedItem().getCliente().getRazonSocial());
            txtTecnico.setText(tblBoletas.getSelectionModel().getSelectedItem().getTecnico());
        } else {
            TrayNotification tray = new TrayNotification("ERROR", "Debe seleccionar un Tecnico", NotificationType.ERROR);
            tray.setAnimationType(AnimationType.POPUP);
            tray.showAndDismiss(Duration.seconds(1));
        }
    }
    
    
    public void agregarBoleta() {
        escenarioPrincipal.ventanaAgregarBoleta();
    }
    
    public void modificarBoleta() {
        if (txtID.getText().isEmpty()) {

            TrayNotification tray = new TrayNotification("MODIFICAR", "Debe seleccionar una Boleta", NotificationType.ERROR);
            tray.setAnimationType(AnimationType.POPUP);
            tray.showAndDismiss(Duration.seconds(1));
        } else {
            int idBoletaModificar = Integer.valueOf(txtID.getText());
            escenarioPrincipal.ventanaModificarBoleta(idBoletaModificar);
        }
    }
    
    public void eliminarBoleta() {
        if (txtID.getText().isEmpty()) {
            TrayNotification tray = new TrayNotification("Eliminar", "Debe seleccionar una Boleta", NotificationType.ERROR);
            tray.setAnimationType(AnimationType.POPUP);
            tray.showAndDismiss(Duration.seconds(1));
        } else {
            CallableStatement procedimiento;
            if (FxDialogs.showConfirm("Eliminar BOleta", "Desea eliminar la boleta seleccionado?", FxDialogs.YES, FxDialogs.NO).equals(FxDialogs.YES)) {
                try {
                    procedimiento = (CallableStatement) Conexion.getInstancia().getConexion().prepareCall("{call sp_EliminarBoleta(?)}");
                    procedimiento.setString(1, txtID.getText());
                    procedimiento.execute();
                    TrayNotification tray = new TrayNotification("ELIMINAR", "La boleta fue eliminado", NotificationType.SUCCESS);
                    tray.setAnimationType(AnimationType.POPUP);
                    tray.showAndDismiss(Duration.seconds(1));
                    mostrarDatos();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    public void cerrar() {
        escenarioPrincipal.cerrar();
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
}
