package org.andres.sistema;
import java.io.IOException;
import java.io.InputStream;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.andres.bean.Usuario;
import org.andres.controladores.ControladorAgregarBoleta;
import org.andres.controladores.ControladorAgregarCliente;
import org.andres.controladores.ControladorAgregarTecnico;
import org.andres.controladores.ControladorAgregarUsuario;
import org.andres.controladores.ControladorBoleta;
import org.andres.controladores.ControladorClientes;
import org.andres.controladores.ControladorCrearDashboard;
import org.andres.controladores.ControladorDashboardAsignaciones;
import org.andres.controladores.ControladorModificarBoleta;
import org.andres.controladores.ControladorModificarCliente;
import org.andres.controladores.ControladorModificarDashboard;
import org.andres.controladores.ControladorModificarTecnico;
import org.andres.controladores.ControladorModificarUsuario;
import org.andres.controladores.ControladorParametrosBoletasReporte;
import org.andres.controladores.ControladorReportes;
import org.andres.controladores.ControladorSplashScreen;
import org.andres.controladores.ControladorTecnico;
import org.andres.controladores.ControladorUsuario;
import org.andres.controladores.ControladorVentanaLogin;
import org.andres.controladores.ControladorVentanaPrincipal;

public class Principal extends Application{
    
    private final String PAQUETE_VISTAS = "/org/andres/vistas/" ;
    private Stage escenario;
    private  Stage dialog;
     private  Stage dialog2;
    private Usuario userAuth; 

    public Stage getDialog2() {
        return dialog2;
    }

    public void setDialog2(Stage dialog2) {
        this.dialog2 = dialog2;
    }

    public void setDialog(Stage dialog) {
        this.dialog = dialog;
    }
    
    public void setUserAuth(Usuario usuarioAuth){
        this.userAuth = usuarioAuth;
    }
    
    @Override
    public void start(Stage escenario) throws Exception {
      this.escenario = escenario;
      this.escenario.getIcons().add(new Image("/org/andres/recursos/icono app.png"));
      this.escenario.initStyle(StageStyle.UNDECORATED);
      this.escenario.centerOnScreen();
       ventanaDashboardAsig();
      this.escenario.show();
    }
    public void cerrar(){
        this.escenario.close();
    }
    public void cerrarModalCrear()
    {
        this.dialog.close();
        escenario.setOpacity(1);
    }

    public  void ventanaSplash(){
        try {
                 ControladorSplashScreen ventanSplash = (ControladorSplashScreen) cambiarEscena("ViewSplash.fxml", 778, 442);
                 ventanSplash.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void ventanaLogin(){
        try {
                ControladorVentanaLogin ventanaLogin = (ControladorVentanaLogin) cambiarEscena("ViewLogin.fxml", 778,442);
                ventanaLogin.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void ventanaPrincipal(){
        try {
                ControladorVentanaPrincipal ventanaPrincipal = (ControladorVentanaPrincipal) cambiarEscena("ViewPrincipal2.fxml", 957, 565);
                ventanaPrincipal.setEscenarioPrincipal(this);
                ventanaPrincipal.setUserAuth(userAuth);
                this.escenario.centerOnScreen();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void ventanaClientes(){
        try {
                ControladorClientes ventanaClientes = (ControladorClientes)cambiarEscena("ViewClientes.fxml",1110 ,597);
                ventanaClientes.setEscenarioPrincipal(this);
                ventanaClientes.setUserAuth(userAuth);
                this.escenario.centerOnScreen();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
        public void ventanaAgregarCliente() {
        try {
            escenario.setOpacity(0.95);
            Stage dialog = new Stage();
             dialog.initStyle(StageStyle.UNDECORATED);
             dialog.centerOnScreen();
                setDialog(dialog);

            ControladorAgregarCliente agregarEmpleado = (ControladorAgregarCliente) cambiarEscenaModal("ViewClienteNuevo.fxml", 382, 302,dialog);
            agregarEmpleado.setEscenarioPrincipal(this);
            dialog.initOwner(escenario);
            dialog.initModality(Modality.APPLICATION_MODAL); 
            dialog.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
        public void ventanaModificarCliente(int i) {
        try {
            escenario.setOpacity(0.95);
            Stage dialog = new Stage();
             dialog.initStyle(StageStyle.UNDECORATED);
             dialog.centerOnScreen();
            setDialog(dialog);

            ControladorModificarCliente modificarCliente = (ControladorModificarCliente) cambiarEscenaModal("ViewModificarCliente.fxml", 382, 302,dialog);
            modificarCliente.setEscenarioPrincipal(this);
            modificarCliente.setClienteModificar(i);
            
            dialog.initOwner(escenario);
            dialog.initModality(Modality.APPLICATION_MODAL); 
            dialog.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void ventanaTecnicos(){
        try {
            ControladorTecnico ventanaTecnicos = (ControladorTecnico)cambiarEscena("ViewTecnicos.fxml",1110 ,597);
            ventanaTecnicos.setEscenarioPrincipal(this);
            ventanaTecnicos.setUserAuth(userAuth);
            this.escenario.centerOnScreen();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
        public void ventanaAgregarTecnico() {
        try {
            escenario.setOpacity(0.95);
            Stage dialog = new Stage();
             dialog.initStyle(StageStyle.UNDECORATED);
             dialog.centerOnScreen();
                setDialog(dialog);

            ControladorAgregarTecnico agregarTecnico = (ControladorAgregarTecnico) cambiarEscenaModal("ViewTecnicoNuevo.fxml", 382, 441,dialog);
            agregarTecnico.setEscenarioPrincipal(this);
            dialog.initOwner(escenario);
            dialog.initModality(Modality.APPLICATION_MODAL); 
            dialog.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
         public void ventanaModificarTecnico(int i) {
        try {
            escenario.setOpacity(0.95);
            Stage dialog = new Stage();
             dialog.initStyle(StageStyle.UNDECORATED);
             dialog.centerOnScreen();
            setDialog(dialog);

            ControladorModificarTecnico modificarTecnico = (ControladorModificarTecnico) cambiarEscenaModal("ViewTecnicoModificar.fxml", 382, 441,dialog);
            modificarTecnico.setEscenarioPrincipal(this);
            modificarTecnico.setTecnicoModificar(i);
            
            dialog.initOwner(escenario);
            dialog.initModality(Modality.APPLICATION_MODAL); 
            dialog.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void ventanaBoletas(){
        try {
            ControladorBoleta ventanaBoleta = (ControladorBoleta)cambiarEscena("ViewBoleta.fxml",1110 ,597);
            ventanaBoleta.setEscenarioPrincipal(this);
            ventanaBoleta.setUserAuth(userAuth);
            this.escenario.centerOnScreen();
        } catch (Exception e) {
            e.printStackTrace();
        }
    } 
        public void ventanaAgregarBoleta() {
        try {
            escenario.setOpacity(0.95);
            Stage dialog = new Stage();
            dialog.initStyle(StageStyle.UNDECORATED);
            dialog.centerOnScreen();
            setDialog(dialog);

            ControladorAgregarBoleta agregarBoleta = (ControladorAgregarBoleta) cambiarEscenaModal("ViewBoletaNueva.fxml", 707, 582, dialog);
            agregarBoleta.setEscenarioPrincipal(this);
            dialog.initOwner(escenario);
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
       
        public void ventanaModificarBoleta(int i) {
        try {
            escenario.setOpacity(0.95);
            Stage dialog = new Stage();
            dialog.initStyle(StageStyle.UNDECORATED);
            dialog.centerOnScreen();
            setDialog(dialog);

            ControladorModificarBoleta modificarBoleta = (ControladorModificarBoleta) cambiarEscenaModal("ViewBoletaModificar.fxml", 707, 582, dialog);
            modificarBoleta.setEscenarioPrincipal(this);
            modificarBoleta.setBoletaModificar(i);

            dialog.initOwner(escenario);
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void ventanaUsuario() {
        try {
            ControladorUsuario ventanaUsuario = (ControladorUsuario) cambiarEscena("ViewUsuario.fxml", 1110, 597);
            ventanaUsuario.setEscenarioPrincipal(this);
            ventanaUsuario.setUserAuth(userAuth);
            this.escenario.centerOnScreen();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
        public void ventanaAgregarUsuario() {
        try {
            escenario.setOpacity(0.95);
            Stage dialog = new Stage();
            dialog.initStyle(StageStyle.UNDECORATED);
            dialog.centerOnScreen();
            setDialog(dialog);

            ControladorAgregarUsuario agregarUsuario = (ControladorAgregarUsuario) cambiarEscenaModal("ViewUsuarioNuevo.fxml", 382, 409, dialog);
            agregarUsuario.setEscenarioPrincipal(this);
            dialog.initOwner(escenario);
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
       
        public void ventanaModificarUsuario(int i) {
        try {
            escenario.setOpacity(0.95);
            Stage dialog = new Stage();
            dialog.initStyle(StageStyle.UNDECORATED);
            dialog.centerOnScreen();
            setDialog(dialog);

            ControladorModificarUsuario modificarUsuario = (ControladorModificarUsuario) cambiarEscenaModal("ViewUsuarioModificar.fxml", 382, 409, dialog);
            modificarUsuario.setEscenarioPrincipal(this);
            modificarUsuario.setUsuariooModificar(i);

            dialog.initOwner(escenario);
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
        
    public void ventanaReportes(){
        try {
                ControladorReportes ventanaReportes = (ControladorReportes)cambiarEscena("ViewReportes.fxml",1110 ,597);
                ventanaReportes.setEscenarioPrincipal(this);
                ventanaReportes.setUserAuth(userAuth);
                this.escenario.centerOnScreen();
        } catch (Exception e) {
            e.printStackTrace();
        }
    } 
        public void ventanaParametrosRepBoleta() {
        try {
            escenario.setOpacity(0.95);
            Stage dialog = new Stage();
             dialog.initStyle(StageStyle.UNDECORATED);
             dialog.centerOnScreen();
                setDialog(dialog);

            ControladorParametrosBoletasReporte parametrosRepBoletas = (ControladorParametrosBoletasReporte) cambiarEscenaModal("ViewParametrosReporteBoletas.fxml", 382, 302,dialog);
            parametrosRepBoletas.setEscenarioPrincipal(this);
            dialog.initOwner(escenario);
            dialog.initModality(Modality.APPLICATION_MODAL); 
            dialog.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void ventanaDashboardAsig(){
        try {
                ControladorDashboardAsignaciones ventanaDashAsig = (ControladorDashboardAsignaciones)cambiarEscena("ViewDashboard.fxml",1110,594);
                ventanaDashAsig.setEscenarioPrincipal(this);
                ventanaDashAsig.setUserAuth(userAuth);
                this.escenario.centerOnScreen();
        } catch (Exception e) {
            e.printStackTrace();
        } 
    }
            public void ventanaCrearAsignacion() {
        try {
            escenario.setOpacity(0.95);
            Stage dialog = new Stage();
             dialog.initStyle(StageStyle.UNDECORATED);
             dialog.centerOnScreen();
            setDialog(dialog);

            ControladorCrearDashboard creadAsignacion = (ControladorCrearDashboard) cambiarEscenaModal("ViewCrearDashboard.fxml", 414, 377,dialog);
            creadAsignacion.setEscenarioPrincipal(this);
            
            dialog.initOwner(escenario);
            dialog.initModality(Modality.APPLICATION_MODAL); 
            dialog.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
     public void ventanaModificarAsignacion(int idAsignacion) {
        try {
            escenario.setOpacity(0.95);
            Stage dialog = new Stage();
             dialog.initStyle(StageStyle.UNDECORATED);
             dialog.centerOnScreen();
            setDialog(dialog);

            ControladorModificarDashboard modificarAsignacion = (ControladorModificarDashboard) cambiarEscenaModal("ViewModificarAsignacion.fxml", 414, 377,dialog);
            modificarAsignacion.setEscenarioPrincipal(this);
            modificarAsignacion.setIdAsignacion(idAsignacion);
            
            dialog.initOwner(escenario);
            dialog.initModality(Modality.APPLICATION_MODAL); 
            dialog.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public Initializable cambiarEscena(String fxml, int ancho, int alto) throws IOException, Exception{
        Initializable resultado = null;
        FXMLLoader cargador = new FXMLLoader();
        cargador.setBuilderFactory(new JavaFXBuilderFactory());
        cargador.setLocation(Principal.class.getResource(PAQUETE_VISTAS + fxml));
        Scene escena = new Scene((AnchorPane) cargador.load(), ancho, alto);
        escenario.setScene(escena);
        escenario.sizeToScene();
        resultado = (Initializable)cargador.getController();
        return resultado;
    }
    public Initializable cambiarEscenaModal(String archivoFxml, int ancho, int alto, Stage modal) throws Exception {
        Initializable resultado = null;
        FXMLLoader cargador = new FXMLLoader();
        InputStream archivo = Principal.class.getResourceAsStream(PAQUETE_VISTAS + archivoFxml);
        
        cargador.setBuilderFactory(new JavaFXBuilderFactory());
        cargador.setLocation(Principal.class.getResource(PAQUETE_VISTAS + archivoFxml));
        Scene escena = new Scene((AnchorPane) cargador.load(), ancho, alto);
        modal.setScene(escena);
        modal.sizeToScene();
        resultado = (Initializable) cargador.getController();
        return resultado;
    }
    public static void main(String[] args){
        launch(args);
    }
}
