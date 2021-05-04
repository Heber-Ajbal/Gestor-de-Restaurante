
package org.heberajbal.system;


import java.io.InputStream;
import java.util.concurrent.ExecutionException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.heberajbal.controller.DatosProgramadorController;
import org.heberajbal.controller.EmpleadosController;
import org.heberajbal.controller.EmpresaController;
import org.heberajbal.controller.MenuPrincipalController;
import org.heberajbal.controller.PlatoController;
import org.heberajbal.controller.PresupuestoController;
import org.heberajbal.controller.ProductoController;
import org.heberajbal.controller.ProductoHasPlatoController;
import org.heberajbal.controller.ServicioController;
import org.heberajbal.controller.ServicioHasEmpleadoController;
import org.heberajbal.controller.ServicioHasPlatoController;
import org.heberajbal.controller.TipoEmpleadoController;
import org.heberajbal.controller.TipoPlatoController;



public class Principal extends Application {
    
    private final String PAQUETE_VISTA = "/org/heberajbal/view/";
    private Stage escenarioPrincipal;
    private Scene escena;
   
    
    @Override
    public void start(Stage escenarioPrincipal ) throws Exception{
        this.escenarioPrincipal = escenarioPrincipal;
        this.escenarioPrincipal.setTitle("Tony´s Kinal app");
        escenarioPrincipal.getIcons().add(new Image("/org/heberajbal/image/logoTK.jpg"));
        menuPrincipal();
      
  
        escenarioPrincipal.show();
    }

    public void menuPrincipal(){
        try{
            MenuPrincipalController menuPrincipal = (MenuPrincipalController)cambiarEscena("MenuPrincipalView.fxml",350,530);
            menuPrincipal.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        } 
    }
    
    public void ventanaProgramador(){
        try{
            DatosProgramadorController datosProgramador =(DatosProgramadorController) cambiarEscena("DatosPersonalesView.fxml",621 ,400 );
            datosProgramador.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
            
    }
    
    public void ventanaEmpresas(){
        try{
            EmpresaController empresaController = (EmpresaController) cambiarEscena("EmpresasView.fxml",670,501);
            empresaController.setEscenarioPrincipal(this);
            
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    
    public void ventanaTipoEmpleado(){
      try{
          TipoEmpleadoController tipoEmpleadoController = (TipoEmpleadoController) cambiarEscena("TipoEmpleadoView.fxml", 660, 500);
          tipoEmpleadoController.setEscenarioPrincipal(this);
          
      }catch(Exception e){
          e.printStackTrace();
      }
        
    }
    
    public void ventanaPresupuesto(){
        try{
            PresupuestoController presupuesto = (PresupuestoController) cambiarEscena("PresupuestoView.fxml", 670, 478);
            presupuesto.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventadaEmpleados(){
        try{
            EmpleadosController empleados = (EmpleadosController) cambiarEscena("EmpleadosView.fxml",880 ,573 );
            empleados.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventanaServicio(){
        try{
            ServicioController servicio = (ServicioController) cambiarEscena("ServicioView.fxml", 742, 523);
            servicio.setEscenarioPrincipal(this);
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventanaTipoPlato(){
        try{
            TipoPlatoController tipodePlato = (TipoPlatoController) cambiarEscena("TipoPlatoView.fxml", 670, 443);
            tipodePlato.setEscenarioPrincipal(this);
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventanaPlato(){
        try{
            PlatoController plato = (PlatoController) cambiarEscena("PlatosView.fxml", 724, 496);
            plato.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
            
    }
    
    public void ventadaProducto(){
        try{
            ProductoController producto =(ProductoController) cambiarEscena("ProductosView.fxml", 576, 457);
            producto.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }       
    }
    
    public void ventanaServicioHasEmpleado(){
        try {
            ServicioHasEmpleadoController has = (ServicioHasEmpleadoController) cambiarEscena("ServicioHasEmpleadoView.fxml", 725, 506);
            has.setEscenarioPrincipal(this); 
                    
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void ventanaServicioHasPlato(){
        try{
            ServicioHasPlatoController has = (ServicioHasPlatoController)cambiarEscena("ServicioHasPlatosView.fxml", 631, 454);
            has.setEscenarioPrincipal(this);
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventanaProductoHasPlato(){
        try{
            ProductoHasPlatoController productoHasPlatoController = (ProductoHasPlatoController)cambiarEscena("ProductosHasPlatosView.fxml", 631, 454);
            productoHasPlatoController.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
            
        }
    }
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
    public Initializable cambiarEscena (String fxml,int ancho,int alto) throws Exception{
        
        Initializable resultado = null;
        FXMLLoader cargadorFXML = new FXMLLoader();
        InputStream archivo = Principal.class.getResourceAsStream(PAQUETE_VISTA+fxml);
        cargadorFXML.setBuilderFactory(new JavaFXBuilderFactory());
        cargadorFXML.setLocation(Principal.class.getResource(PAQUETE_VISTA+fxml));
        escena =new Scene((AnchorPane)cargadorFXML.load(archivo),ancho,alto); 
        escenarioPrincipal.setScene(escena);
        escenarioPrincipal.sizeToScene();
        resultado = (Initializable)cargadorFXML.getController();
  
        return resultado;
    }


}
