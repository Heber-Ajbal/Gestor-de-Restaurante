
package org.heberajbal.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import org.heberajbal.system.Principal;


public class MenuPrincipalController implements Initializable{
private Principal escenarioPrincipal;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
    }

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    

    public void ventanaProgramador(){
        escenarioPrincipal.ventanaProgramador();
    }
    
  public void ventanaEmpresas(){
      escenarioPrincipal.ventanaEmpresas();
  }

  public void ventanaTipoEmpleado(){
      escenarioPrincipal.ventanaTipoEmpleado();
  }

  public void ventanaPresupuesto(){
      escenarioPrincipal.ventanaPresupuesto();
}
  
  public void ventanaEmpleados(){
      escenarioPrincipal.ventadaEmpleados();
  }
  
  public void ventanaTipoPlato(){
      escenarioPrincipal.ventanaTipoPlato();
  }
  
  public void ventanaProducto(){
      escenarioPrincipal.ventadaProducto();
  }
  
  public void ventanaServicioHasPlato(){
      escenarioPrincipal.ventanaServicioHasPlato();
  }
  
  public void ventanaProductoHasPlato(){
      escenarioPrincipal.ventanaProductoHasPlato();
  }
  
  public void ventanaServicioHasEmpleado(){
      escenarioPrincipal.ventanaServicioHasEmpleado();
  }
     
  
  
}
