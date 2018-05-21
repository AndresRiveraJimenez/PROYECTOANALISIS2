package org.andres.controladores;

import java.net.URL;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.util.Duration;
import org.andres.bean.Clientes;
import org.andres.bean.Tecnico;
import org.andres.sistema.Principal;
import org.andresrivera.conexion.Conexion;
import tray.animations.AnimationType;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

public class ControladorAgregarBoleta implements Initializable {
    private Principal escenarioPrincipal;

    @FXML private DatePicker txtFechaVisita;
    @FXML private TextField txtHoraEntrada;
    @FXML private TextField txtHoraSalida;
    @FXML private TextField txtMotivo;
    @FXML private ComboBox txtCliente;
    @FXML private ComboBox txtTecnico;
    @FXML private TextArea txtDescripcion;
    
    private ArrayList<Tecnico> listaTecnicos;
    private ArrayList<Clientes> listaClientes;
    private int tecnicoEscogido;
    private int clienteEscogido;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        agregarItemTecnicos();
        agregarItemClientes();
    }
    
    public void agregarItemTecnicos() {
        txtTecnico.setItems(getListaTecnicos());
        txtTecnico.setValue(" ");
    }
    
    public void agregarItemClientes() {
        txtCliente.setItems(getListaClientes());
        txtCliente.setValue(" ");
    }

    public ObservableList<String> getListaTecnicos() {

        ResultSet tecnicos = Conexion.getInstancia().hacerConsulta("SELECT t.idTecnico, t.nombreTecnico  FROM Tecnicos t where t.estado = 1");
        listaTecnicos = new ArrayList<Tecnico>();
        ArrayList<String> lista = new ArrayList<String>();
        try {
            lista.add("");
            while (tecnicos.next()) {
                listaTecnicos.add(
                        new Tecnico(tecnicos.getInt("idTecnico"),
                                tecnicos.getString("nombreTecnico")));
                lista.add(tecnicos.getString("nombreTecnico"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return   FXCollections.observableArrayList(lista);
    }
    
    public ObservableList<String> getListaClientes() {

        ResultSet clientes = Conexion.getInstancia().hacerConsulta("SELECT c.idCliente, c.razonSocial  FROM Clientes c where c.estado = 1");
        listaClientes = new ArrayList<Clientes>();
        ArrayList<String> lista = new ArrayList<String>();
        try {
            lista.add("");
            while (clientes.next()) {
                listaClientes.add(
                        new Clientes(clientes.getInt("idCliente"),
                                clientes.getString("razonSocial")));
                
                lista.add(clientes.getString("razonSocial"));
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
    @FXML
    public void cerrar()
    {
        escenarioPrincipal.cerrarModalCrear();
    }
    
    @FXML
    public void guardarBoleta(){

        CallableStatement  procedimiento;
        try {
            procedimiento = (CallableStatement ) Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarBoleta(?,?,?,?,?,?,?)}");
            
                procedimiento.setString(1, txtMotivo.getText());
                procedimiento.setDate(2, java.sql.Date.valueOf(txtFechaVisita.getValue()));
                procedimiento.setString(3, txtHoraEntrada.getText());
                procedimiento.setString(4, txtHoraSalida.getText());
                procedimiento.setString(5, txtDescripcion.getText());
                procedimiento.setInt(6, clienteEscogido);
                procedimiento.setInt(7, tecnicoEscogido);

                procedimiento.execute();
                TrayNotification tray = new TrayNotification("GUARDAR", "Boleta nuevo agregado", NotificationType.SUCCESS);
                tray.setAnimationType(AnimationType.POPUP);
                tray.showAndDismiss(Duration.seconds(1));
                escenarioPrincipal.ventanaBoletas();
                cerrar();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
         
    public void validarCamposGuardar() {
                    
        if (txtFechaVisita.getValue() == null) {
            TrayNotification tray = new TrayNotification("GUARDAR", "Debes ingresar un fecha de visita", NotificationType.ERROR);
            tray.setAnimationType(AnimationType.FADE);
            tray.showAndDismiss(Duration.seconds(1));
            txtFechaVisita.requestFocus();
        } else if (txtHoraEntrada.getText().isEmpty()) {
            TrayNotification tray = new TrayNotification("GUARDAR", "Debes ingresar una hora entrada valida", NotificationType.ERROR);
            tray.setAnimationType(AnimationType.FADE);
            tray.showAndDismiss(Duration.seconds(1));
            txtHoraEntrada.requestFocus();
        } else if (!validarHorayMinutos(txtHoraEntrada.getText())) {
            TrayNotification tray = new TrayNotification("GUARDAR", "Debes ingresar una hora de entrada valida", NotificationType.ERROR);
            tray.setAnimationType(AnimationType.FADE);
            tray.showAndDismiss(Duration.seconds(1));
            txtHoraEntrada.requestFocus();
        } else if (txtHoraSalida.getText().isEmpty()) {
            TrayNotification tray = new TrayNotification("GUARDAR", "Debes ingresar una hora salida valida", NotificationType.ERROR);
            tray.setAnimationType(AnimationType.FADE);
            tray.showAndDismiss(Duration.seconds(1));
            txtHoraSalida.requestFocus();
        } else if (!validarHorayMinutos(txtHoraSalida.getText())) {
            TrayNotification tray = new TrayNotification("GUARDAR", "Debes ingresar una hora salida valida", NotificationType.ERROR);
            tray.setAnimationType(AnimationType.FADE);
            tray.showAndDismiss(Duration.seconds(1));
            txtHoraSalida.requestFocus();
        } else if (txtMotivo.getText().isEmpty()) {
            TrayNotification tray = new TrayNotification("GUARDAR", "Debes ingresar un motivo", NotificationType.ERROR);
            tray.setAnimationType(AnimationType.FADE);
            tray.showAndDismiss(Duration.seconds(1));
            txtMotivo.requestFocus();
        } else if (seleccionarItemCliente()) {
            TrayNotification tray = new TrayNotification("GUARDAR", "Debes selecionar un cliente", NotificationType.ERROR);
            tray.setAnimationType(AnimationType.FADE);
            tray.showAndDismiss(Duration.seconds(1));
            txtCliente.requestFocus();
        }  else if (seleccionarItemTecnico()) {
            TrayNotification tray = new TrayNotification("GUARDAR", "Debes selecionar un tecnico", NotificationType.ERROR);
            tray.setAnimationType(AnimationType.FADE);
            tray.showAndDismiss(Duration.seconds(1));
            txtTecnico.requestFocus();
        } else if (txtDescripcion.getText().isEmpty()) {
            TrayNotification tray = new TrayNotification("GUARDAR", "Debes ingresar una descripcion", NotificationType.ERROR);
            tray.setAnimationType(AnimationType.FADE);
            tray.showAndDismiss(Duration.seconds(1));
            txtDescripcion.requestFocus();
        }  else {
            guardarBoleta();
        }
    }
        
    public boolean seleccionarItemTecnico() {

        String tecnico = txtTecnico.getValue().toString();

        if (tecnico.isEmpty()) {
            return true;
        } else {
            for (Tecnico tec : listaTecnicos) {
                if (tec.getNombre().equals(tecnico)) {
                    tecnicoEscogido = tec.getIdTecnico();
                    return false;
                }
            }
        }
        return true;
    }
    
    public boolean seleccionarItemCliente() {

        String cliente = txtCliente.getValue().toString();

        if (cliente.isEmpty()) {
            return true;
        } else {
            for (Clientes cli : listaClientes) {
                if (cli.getRazonSocial().equals(cliente)) {
                    clienteEscogido = cli.getIdCliente();
                    return false;
                }
            }
        }

        return true;
    }
        
    public boolean validarHorayMinutos(String linea) {

        if (validarHoraPatron(linea)) {
            StringTokenizer st = new StringTokenizer(linea, ":");

            while (st.hasMoreTokens()) {

                String hora = st.nextToken();
                String minutos = st.nextToken();

                if (Integer.valueOf(hora) <= 23 || Integer.valueOf(hora) == 00) {
                    if (Integer.valueOf(minutos) <= 59 || Integer.valueOf(minutos) == 00) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    public boolean validarHoraPatron(String hora) {

        String regex = "^[\\d]{1,2}:[\\d]{1,2}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(hora);

        return matcher.matches();
    }
    

    
    public boolean validarHora(String hora) {

        String regex = "^[\\d]{1,2}:[0-9]{1,2}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(hora);

        return matcher.matches();
    }
        
}

