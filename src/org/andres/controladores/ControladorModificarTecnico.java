package org.andres.controladores;

import java.net.URL;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.util.Duration;
import org.andres.bean.Departamento;
import org.andres.bean.Tecnico;
import org.andres.sistema.Principal;
import org.andresrivera.conexion.Conexion;
import tray.animations.AnimationType;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

public class ControladorModificarTecnico implements Initializable {
    
    @FXML private TextField txtID;
    @FXML private TextField txtNombre;
    @FXML private TextField txtApellido;
    @FXML private TextField txtTelefono;
    @FXML private TextField txtEmail;
    @FXML private TextField txtUsuarioDominio;
    @FXML private ComboBox txtDepartamento;
    
    private Principal escenarioPrincipal;
    private Tecnico tecnicoModificar;
    private ArrayList<Departamento> listaDepartamentos;
    private int departamentoEscogido;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        txtDepartamento.setItems(getListaDepartemantos());
    }
    
    public void setTecnicoModificar(int idTecnicoModificar) {
        
        this.tecnicoModificar = buscarTecnico(idTecnicoModificar);
        txtID.setText(String.valueOf(tecnicoModificar.getIdTecnico()));
        txtNombre.setText(String.valueOf(tecnicoModificar.getNombre()));
        txtApellido.setText(String.valueOf(tecnicoModificar.getApellido()));
        txtTelefono.setText(String.valueOf(tecnicoModificar.getTelefono()));
        txtEmail.setText(String.valueOf(tecnicoModificar.getCorreo()));
        txtUsuarioDominio.setText(String.valueOf(tecnicoModificar.getUsuarioD()));        
        txtDepartamento.setValue(String.valueOf(tecnicoModificar.getDepartamento()));

    }
        
     public ObservableList<String> getListaDepartemantos() {

        ResultSet departemantos = Conexion.getInstancia().hacerConsulta("select * from  Departamentos");
        listaDepartamentos = new ArrayList<Departamento>();
        ArrayList<String> lista = new ArrayList<String>();
        try {
            while (departemantos.next()) {
                listaDepartamentos.add(
                        new Departamento(departemantos.getInt("idDpto"),
                                departemantos.getString("nombreDpto")));
                lista.add(departemantos.getString("nombreDpto"));
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

    public void cerrar() {
        escenarioPrincipal.cerrarModalCrear();
    }

    public Tecnico buscarTecnico(int idTecnicoModificar) {
        Tecnico resultado = null;
        try {
            PreparedStatement procedimiento = (PreparedStatement) Conexion.getInstancia().getConexion().prepareStatement("{call sp_BuscarTecnico(?)}");
            procedimiento.setInt(1, idTecnicoModificar);
            ResultSet tecnico = procedimiento.executeQuery();
            while (tecnico.next()) {
                resultado = new Tecnico(tecnico.getInt("idTecnico"), tecnico.getString("nombreTecnico"),
                        tecnico.getString("apellidoTecnico"),tecnico.getString("numeroTel"), 
                        tecnico.getString("correo"), tecnico.getString("usuarioDominio"),tecnico.getString("nombreDpto"), 1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultado;
    }

    public void actualizarTecnico() {
        CallableStatement procedimiento;
        try {
            procedimiento = (CallableStatement ) Conexion.getInstancia().getConexion().prepareCall("{call sp_ActualizarTecnico(?,?,?,?,?,?,?,?)}");
            
                procedimiento.setInt(1, Integer.valueOf(txtID.getText()));
                procedimiento.setString(2, txtNombre.getText());
                procedimiento.setString(3, txtApellido.getText());
                procedimiento.setString(4, txtTelefono.getText());
                procedimiento.setString(5, txtEmail.getText());
                procedimiento.setString(6, txtUsuarioDominio.getText());
                procedimiento.setInt(7, departamentoEscogido );
                procedimiento.setInt(8, 1);  //estado 1 por default
                procedimiento.execute();
                TrayNotification tray = new TrayNotification("Actualizado", "Tecnico Actualizado", NotificationType.SUCCESS);
                tray.setAnimationType(AnimationType.POPUP);
                tray.showAndDismiss(Duration.seconds(1));
                escenarioPrincipal.ventanaTecnicos();
                cerrar();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
        
    public void seleccionarItemDept() {

        String departamento = txtDepartamento.getValue().toString();

        for (Departamento dep : listaDepartamentos) {
            if (dep.getNombre().equals(departamento)) {
                departamentoEscogido = dep.getIdDepartamento();

            }
        }
    }
    
    public void validarCamposActualizar() {
        if (txtNombre.getText().isEmpty()) {
            TrayNotification tray = new TrayNotification("Actualizado", "Debes ingresar un nombre", NotificationType.ERROR);
            tray.setAnimationType(AnimationType.FADE);
            tray.showAndDismiss(Duration.seconds(1));
            txtNombre.requestFocus();
        } else if (txtApellido.getText().isEmpty()) {
            TrayNotification tray = new TrayNotification("Actualizado", "Debes ingresar un apellido", NotificationType.ERROR);
            tray.setAnimationType(AnimationType.FADE);
            tray.showAndDismiss(Duration.seconds(1));
            txtApellido.requestFocus();
        } else if (!validarTelefono()) {
            TrayNotification tray = new TrayNotification("Actualizado", "Debes ingresar un numero de telefono valido", NotificationType.ERROR);
            tray.setAnimationType(AnimationType.FADE);
            tray.showAndDismiss(Duration.seconds(1));
            txtTelefono.requestFocus();
        } else if (!validarEmail()) {
            TrayNotification tray = new TrayNotification("Actualizado", "Debes ingresar un correo valido", NotificationType.ERROR);
            tray.setAnimationType(AnimationType.FADE);
            tray.showAndDismiss(Duration.seconds(1));
            txtEmail.requestFocus();
        } else if (txtUsuarioDominio.getText().isEmpty()) {
            TrayNotification tray = new TrayNotification("Actualizado", "Debes ingresar un dominio", NotificationType.ERROR);
            tray.setAnimationType(AnimationType.FADE);
            tray.showAndDismiss(Duration.seconds(1));
            txtUsuarioDominio.requestFocus();
        }else {
            seleccionarItemDept();
            actualizarTecnico();
        }
    }
   
    public boolean validarEmail() {

        String regex = "^[\\w-_ .]+@{1}[\\w]+.{1}[a-zA-Z]{2,6}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(txtEmail.getText());

        return matcher.matches();
    }

    public boolean validarTelefono() {

        String regex = "^[\\d]{8}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(txtTelefono.getText());

        return matcher.matches();
    }
   
    public void limitTextField() {
        int limit = 8;
        UnaryOperator<TextFormatter.Change> textLimitFilter = change -> {
            if (change.isContentChange()) {
                int newLength = change.getControlNewText().length();
                if (newLength > limit) {
                    String trimmedText = change.getControlNewText().substring(0, limit);
                    change.setText(trimmedText);
                    int oldLength = change.getControlText().length();
                    change.setRange(0, oldLength);
                }
            }
            return change;
        };
        txtTelefono.setTextFormatter(new TextFormatter(textLimitFilter));
    }

}
