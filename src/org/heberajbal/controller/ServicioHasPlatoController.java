
package org.heberajbal.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.heberajbal.bean.Plato;
import org.heberajbal.bean.Servicio;
import org.heberajbal.bean.ServicioHasPlato;
import org.heberajbal.db.Conexion;
import org.heberajbal.system.Principal;


public class ServicioHasPlatoController implements Initializable{
    
    private Principal escenarioPrincipal;
    ObservableList<ServicioHasPlato> listaServicioHasPlato;
    ObservableList<Servicio>listaServicio;
    ObservableList<Plato>listaPlato;
    
    @FXML private ComboBox cmbCodigoServicio;
    @FXML private ComboBox cmbCodigoPlato;
    @FXML private TableView tblServicioHasPlato;
    @FXML private TableColumn colCodigoServicio;
    @FXML private TableColumn colCodigoPlato;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
       cargarDatos(); 
       cmbCodigoPlato.setItems(getPlato());
       cmbCodigoServicio.setItems(getServicio());
    }
    
    public void cargarDatos(){
        tblServicioHasPlato.setItems(getSHP());
        colCodigoServicio.setCellValueFactory(new PropertyValueFactory<ServicioHasPlato,Integer>("codigoServicio"));
        colCodigoPlato.setCellValueFactory(new PropertyValueFactory<ServicioHasPlato,Integer>("codigoPlato"));
    }
    
    public ObservableList<ServicioHasPlato> getSHP(){
        ArrayList<ServicioHasPlato> lista = new ArrayList<ServicioHasPlato>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarServicioHasPlato}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new ServicioHasPlato(resultado.getInt("codigoServicio"),
                                               resultado.getInt("codigoPlato")));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return listaServicioHasPlato = FXCollections.observableArrayList(lista);
    }
    
     public ObservableList<Plato> getPlato(){
        ArrayList<Plato> lista = new ArrayList<Plato>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarPlatos}");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                lista.add(new Plato(resultado.getInt("codigoPlato"),
                                    resultado.getInt("cantidad"),
                                    resultado.getString("nombrePlato"),
                                    resultado.getString("descripcionPlato"),
                                    resultado.getDouble("precioPlato"),
                                    resultado.getInt("codigoTipoPlato")));
                
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaPlato = FXCollections.observableArrayList(lista);
    }
     
      public ObservableList<Servicio> getServicio(){
        ArrayList<Servicio> lista = new ArrayList<Servicio>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarServicio}");
            ResultSet resultado = procedimiento.executeQuery();
            
            while(resultado.next()){
                lista.add(new Servicio(resultado.getInt("codigoServicio"),
                                       resultado.getDate("fechaServicio"),
                                       resultado.getString("tipoServicio"),
                                       resultado.getString("horaServicio"),
                                       resultado.getString("lugarServicio"),
                                       resultado.getString("telefonoContacto"),
                                       resultado.getInt("codigoEmpresa")));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return  listaServicio = FXCollections.observableArrayList(lista);
    }
    
    public void seleccionarElementos(){
        if(tblServicioHasPlato.getSelectionModel().getSelectedItem() !=null) {
            cmbCodigoPlato.getSelectionModel().select(buscarPlato(((ServicioHasPlato)tblServicioHasPlato.getSelectionModel().getSelectedItem()).getCodigoPlato()));
            cmbCodigoServicio.getSelectionModel().select(buscarServicio(((ServicioHasPlato)tblServicioHasPlato.getSelectionModel().getSelectedItem()).getCodigoServicio()));
        }
    }
    
     public Servicio buscarServicio(int codigoServicio){
        Servicio resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarServicio(?)}");
            procedimiento.setInt(1, codigoServicio);
            ResultSet registro = procedimiento.executeQuery();
            
            while(registro.next()){
                resultado = new Servicio(
                                           registro.getInt("codigoServicio"),
                                           registro.getDate("fechaServicio"),
                                           registro.getString("tipoServicio"),
                                           registro.getString("horaServicio"),
                                           registro.getString("lugarServicio"),
                                           registro.getString("telefonoContacto"),
                                           registro.getInt("codigoEmpresa"));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return resultado;
    }
     
    public Plato buscarPlato(int codigoPlato){
        Plato resultado = null;
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarPlatos(?)}");
            procedimiento.setInt(1, codigoPlato);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new Plato(registro.getInt("codigoPlato"),
                                        registro.getInt("cantidad"),
                                        registro.getString("nombrePlato"),
                                        registro.getString("descripcionPlato"),
                                        registro.getDouble("precioPlato"),
                                        registro.getInt("codigoTipoPlato"));
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultado;
    }

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    public void menuPrincipal(){
        escenarioPrincipal.menuPrincipal();
    }
    
    
}
