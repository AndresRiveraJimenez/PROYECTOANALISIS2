package org.andres.controladores;

import java.net.URL;
import java.sql.CallableStatement;
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
import javafx.scene.control.TextFormatter.Change;
import javafx.util.Duration;
import org.andres.bean.Departamento;
import org.andres.sistema.Principal;
import org.andresrivera.conexion.Conexion;
import tray.animations.AnimationType;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

public class ControladorAgregarTecnico implements Initializable {

    @FXML private TextField txtID;
    @FXML private TextField txtNombre;
    @FXML private TextField txtApellido;
    @FXML private TextField txtTelefono;
    @FXML private TextField txtEmail;
    @FXML private TextField txtUsuarioDominio;
    @FXML private ComboBox txtDepartamento;
 
    private ArrayList<Departamento> listaDepartamentos;
    private Principal escenarioPrincipal;
    private int departamentoEscogido;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        agregarItemDepartemantos();
    }

    public void agregarItemDepartemantos(){
        txtDepartamento.setItems(getListaDepartemantos());
        txtDepartamento.setValue(" ");
    }
    
    public ObservableList<String> getListaDepartemantos() {

        ResultSet departemantos = Conexion.getInstancia().hacerConsulta("select * from  Departamentos");
        listaDepartamentos = new ArrayList<Departamento>();
        ArrayList<String> lista = new ArrayList<String>();
        try {
            lista.add("");
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
    public void cerrar()
    {
        escenarioPrincipal.cerrarModalCrear();
    }
    
        public void guardarTecnico(){
                CallableStatement  procedimiento;
        try {
            procedimiento = (CallableStatement ) Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarTecnico(?,?,?,?,?,?,?)}");
            
                procedimiento.setString(1, txtNombre.getText());
                procedimiento.setString(2, txtApellido.getText());
                procedimiento.setString(3, txtTelefono.getText());
                procedimiento.setString(4, txtEmail.getText());
                procedimiento.setString(5, txtUsuarioDominio.getText());
                procedimiento.setInt(6, departamentoEscogido );
                procedimiento.setInt(7, 1);  //estado 1 por default
                procedimiento.execute();
                TrayNotification tray = new TrayNotification("GUARDAR", "Tecnico nuevo agregado", NotificationType.SUCCESS);
                tray.setAnimationType(AnimationType.POPUP);
                tray.showAndDismiss(Duration.seconds(1));
                escenarioPrincipal.ventanaTecnicos();
                cerrar();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
        
    
    public boolean seleccionarItemDept(){
        
        String departamento = txtDepartamento.getValue().toString();
        
        if(departamento.isEmpty()){
            return true;
        }else{
            for(Departamento dep : listaDepartamentos){
                if(dep.getNombre().equals(departamento)){
                    departamentoEscogido = dep.getIdDepartamento();
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
            TrayNotification tray = new TrayNotification("GUARDAR", "Debes ingresar un apellido", NotificationType.ERROR);
            tray.setAnimationType(AnimationType.FADE);
            tray.showAndDismiss(Duration.seconds(1));
            txtApellido.requestFocus();
        } else if (!validarTelefono()) {
            TrayNotification tray = new TrayNotification("GUARDAR", "Debes ingresar un numero de telefono valido", NotificationType.ERROR);
            tray.setAnimationType(AnimationType.FADE);
            tray.showAndDismiss(Duration.seconds(1));
            txtTelefono.requestFocus();
        } else if (!validarEmail()) {
            TrayNotification tray = new TrayNotification("GUARDAR", "Debes ingresar un correo valido", NotificationType.ERROR);
            tray.setAnimationType(AnimationType.FADE);
            tray.showAndDismiss(Duration.seconds(1));
            txtEmail.requestFocus();
        } else if (txtUsuarioDominio.getText().isEmpty()) {
            TrayNotification tray = new TrayNotification("GUARDAR", "Debes ingresar un dominio", NotificationType.ERROR);
            tray.setAnimationType(AnimationType.FADE);
            tray.showAndDismiss(Duration.seconds(1));
            txtUsuarioDominio.requestFocus();
        } else if (seleccionarItemDept()) {
            TrayNotification tray = new TrayNotification("GUARDAR", "Debes seleccionar un departamento", NotificationType.ERROR);
            tray.setAnimationType(AnimationType.FADE);
            tray.showAndDismiss(Duration.seconds(1));
            txtDepartamento.requestFocus();
        }else {
            guardarTecnico();
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
        int  limit =8;
        UnaryOperator<Change> textLimitFilter = change -> {
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
