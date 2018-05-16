package org.andres.controladores;

import java.net.URL;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalTime;
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
import org.andres.bean.Boleta;
import org.andres.bean.Clientes;
import org.andres.bean.Tecnico;
import org.andres.sistema.Principal;
import org.andresrivera.conexion.Conexion;
import tray.animations.AnimationType;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

public class ControladorModificarBoleta implements Initializable {
    
    @FXML private TextField txtID;
    @FXML private TextField txtBoletaCreada;
    @FXML private TextField txtHoraEntrada;
    @FXML private TextField txtHoraSalida;
    @FXML private TextField txtMotivo;
    @FXML private TextField txtCliente;
    @FXML private ComboBox txtTecnico;
    @FXML private TextArea txtDescripcion;
    @FXML private DatePicker txtFechaVisita;
        
    private Principal escenarioPrincipal;
    private Boleta boletaModificar;
    private ArrayList<Tecnico> listaTecnicos;
    private int tecnicoEscogido;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        txtTecnico.setItems(getListaTecnicos());
    }
    
    public ObservableList<String> getListaTecnicos() {
        ResultSet tecnico = Conexion.getInstancia().hacerConsulta("select a.idTecnico, a.nombreTecnico "
                + "from Tecnicos a  where a.estado = 1");
        listaTecnicos = new ArrayList<Tecnico>();
        ArrayList<String> lista = new ArrayList<String>();
        try {
            while (tecnico.next()) {
                listaTecnicos.add(new Tecnico(tecnico.getInt("idTecnico"),
                        tecnico.getString("nombreTecnico")));
                lista.add(tecnico.getString("nombreTecnico"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return FXCollections.observableArrayList(lista);
    }
    
    public void setBoletaModificar(int idBoletaModificar) {    
        
        this.boletaModificar = buscarBoleta(idBoletaModificar);
        txtID.setText(String.valueOf(boletaModificar.getIdBoleta()));
        txtBoletaCreada.setText(boletaModificar.getFechaCreadoES());
        txtHoraEntrada.setText(boletaModificar.getHoraEntrada().toString());
        txtHoraSalida.setText(boletaModificar.getHoraSalida().toString());   
        txtMotivo.setText(boletaModificar.getMotivo());
        txtCliente.setText(String.valueOf(boletaModificar.getCliente().getIdCliente()));
        txtTecnico.setValue((boletaModificar.getTecnico()));
        txtDescripcion.setText(boletaModificar.getDescripcion());
        txtFechaVisita.setValue(boletaModificar.getFechaCreado());

    }
    
    public Boleta buscarBoleta(int idBoletaModificar) {
        Boleta resultado = null;
        try {
            PreparedStatement procedimiento = (PreparedStatement) Conexion.getInstancia().getConexion().prepareStatement("{call sp_BuscarBoleta(?)}");
            procedimiento.setInt(1, idBoletaModificar);
            ResultSet boleta = procedimiento.executeQuery();
            while (boleta.next()) {
                resultado = new Boleta(
                        boleta.getInt("idBoleta"), boleta.getString("motivoVisita"), 
                        boleta.getDate("fechaVisita").toLocalDate(), boleta.getTime("horaEntrada").toLocalTime(), 
                        boleta.getTime("horaSalida").toLocalTime(), boleta.getString("descTrabajo"),
                        boleta.getDate("fechaCreacion").toLocalDate(),new Clientes(boleta.getInt("idCliente") ,boleta.getString("razonSocial")),
                        boleta.getString("nombreTecnico"), boleta.getInt("estado"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultado;
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
    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void cerrar() {
        escenarioPrincipal.cerrarModalCrear();
    }
    
    public void actualizarBoleta() {
        CallableStatement  procedimiento;
        try {
            procedimiento = (CallableStatement ) Conexion.getInstancia().getConexion().prepareCall("{call sp_ActualizarBoleta(?,?,?,?,?,?,?,?)}");
            
                procedimiento.setInt(1,Integer.valueOf(txtID.getText()));
                procedimiento.setString(2, txtMotivo.getText());
                procedimiento.setDate(3, java.sql.Date.valueOf(txtFechaVisita.getValue()));
                procedimiento.setString(4, txtHoraEntrada.getText());
                procedimiento.setString(5, txtHoraSalida.getText());
                procedimiento.setString(6, txtDescripcion.getText());
                procedimiento.setInt(7, Integer.valueOf(txtCliente.getText()));
                procedimiento.setInt(8, tecnicoEscogido);

                procedimiento.execute();
                TrayNotification tray = new TrayNotification("Actualizado", "Boleta actualizada", NotificationType.SUCCESS);
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
            TrayNotification tray = new TrayNotification("ERROR", "Debes ingresar un Fecha de visita", NotificationType.ERROR);
            tray.setAnimationType(AnimationType.FADE);
            tray.showAndDismiss(Duration.seconds(1));
            txtFechaVisita.requestFocus();
        } else if (txtHoraEntrada.getText().isEmpty()) {
            TrayNotification tray = new TrayNotification("ERROR", "Debes ingresar una hora entra", NotificationType.ERROR);
            tray.setAnimationType(AnimationType.FADE);
            tray.showAndDismiss(Duration.seconds(1));
            txtHoraEntrada.requestFocus();
        } else if (!validarHorayMinutos(txtHoraEntrada.getText())) {
            TrayNotification tray = new TrayNotification("ERROR", "Parece que la hora de entrada no es valida", NotificationType.ERROR);
            tray.setAnimationType(AnimationType.FADE);
            tray.showAndDismiss(Duration.seconds(1));
            txtHoraEntrada.requestFocus();
        } else if (txtHoraSalida.getText().isEmpty()) {
            TrayNotification tray = new TrayNotification("ERROR", "Debes ingresar una hora salida", NotificationType.ERROR);
            tray.setAnimationType(AnimationType.FADE);
            tray.showAndDismiss(Duration.seconds(1));
            txtHoraSalida.requestFocus();
        } else if (!validarHorayMinutos(txtHoraSalida.getText())) {
            TrayNotification tray = new TrayNotification("ERROR", "Parece que la hora de salida no es valida", NotificationType.ERROR);
            tray.setAnimationType(AnimationType.FADE);
            tray.showAndDismiss(Duration.seconds(1));
            txtHoraSalida.requestFocus();
        } else if (txtMotivo.getText().isEmpty()) {
            TrayNotification tray = new TrayNotification("ERROR", "Debes ingresar un Motivo", NotificationType.ERROR);
            tray.setAnimationType(AnimationType.FADE);
            tray.showAndDismiss(Duration.seconds(1));
            txtMotivo.requestFocus();
        } else if (txtCliente.getText().isEmpty()) {
            TrayNotification tray = new TrayNotification("ERROR", "Debes ingresar un cliente", NotificationType.ERROR);
            tray.setAnimationType(AnimationType.FADE);
            tray.showAndDismiss(Duration.seconds(1));
            txtCliente.requestFocus();
        } else if ( buscarCliente( Integer.valueOf(txtCliente.getText())) == null) {
            TrayNotification tray = new TrayNotification("ERROR", "Debes ingresar un numero de cliente valido", NotificationType.ERROR);
            tray.setAnimationType(AnimationType.FADE);
            tray.showAndDismiss(Duration.seconds(1));
            txtCliente.requestFocus();
        }else if (txtDescripcion.getText().isEmpty()) {
            TrayNotification tray = new TrayNotification("ERROR", "Debes ingresar una descripcion", NotificationType.ERROR);
            tray.setAnimationType(AnimationType.FADE);
            tray.showAndDismiss(Duration.seconds(1));
            txtDescripcion.requestFocus();
        }  else {
            seleccionarItemTecnico();
            actualizarBoleta();
        }
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
    
    public void seleccionarItemTecnico() {

        String tecnico = txtTecnico.getValue().toString();

        for (Tecnico tec : listaTecnicos) {
            if (tec.getNombre().equals(tecnico)) {
                tecnicoEscogido = tec.getIdTecnico();
            }
        }
    }
    
    public boolean validarHora(String hora) {

        String regex = "^[\\d]{1,2}:[0-9]{1,2}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(hora);

        return matcher.matches();
    }
    
    public void validarIdCliente() {
        if (!txtCliente.getText().matches("\\d*")) {
            txtCliente.clear();
        }
    }
}
