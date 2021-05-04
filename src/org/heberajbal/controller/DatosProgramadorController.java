
package org.heberajbal.controller;


import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import org.heberajbal.system.Principal;


public class DatosProgramadorController implements Initializable{

    
    
    private Principal escenarioPrincipal;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
    }

    @FXML private Button btnProgramador;
    @FXML private Button btnAdministracion;
    @FXML private AnchorPane pnlStatus;
    @FXML private Label lblNombre;
    @FXML private Label lbldatosProgramador;
    @FXML private Label lblApellido;
    @FXML private Label lblCorreo;
    @FXML private ImageView imgInicio;
   
    
    
    
    
    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    
    @FXML
    private void about (ActionEvent event){
        if(event.getSource() == btnProgramador){
           
            lbldatosProgramador.setText("DATOS PROGRAMADOR");
            lblNombre.setText("Heber Obdulio");
            lblApellido.setText("Ajbal Muj");
            lblCorreo.setText("hajbal-2019010@kinal.edu.gt");
            

        }else if(event.getSource()== btnAdministracion){
            
            lbldatosProgramador.setText("ADMINISTRADOR");
            lblNombre.setText("Fundación");
            lblApellido.setText("Kinal");
            lblCorreo.setText("Tony´s Kinal");
       
        }
    }
    
    
    
    public void menuPrincipal(){
        escenarioPrincipal.menuPrincipal();
    }
}
