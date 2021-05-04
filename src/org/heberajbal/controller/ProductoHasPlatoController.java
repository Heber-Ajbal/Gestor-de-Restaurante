
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
import org.heberajbal.bean.Producto;
import org.heberajbal.bean.ProductoHasPlato;
import org.heberajbal.db.Conexion;
import org.heberajbal.system.Principal;


public class ProductoHasPlatoController implements Initializable{

    Principal escenarioPrincipal;
    ObservableList<ProductoHasPlato> listaPHP;
    ObservableList<Producto> listaProducto;
    ObservableList<Plato> listaPlato;
    
    @FXML private ComboBox cmbCodigoPlato;
    @FXML private ComboBox cmbCodigoProducto;
    @FXML private TableView tblProductoHasPlato;
    @FXML private TableColumn colCodigoPlato;
    @FXML private TableColumn colCodigoProducto;
   
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        cmbCodigoPlato.setItems(getPlato());
        cmbCodigoProducto.setItems(getProducto());
        
    }
    
    public void cargarDatos(){
        tblProductoHasPlato.setItems(getPHP());
        colCodigoPlato.setCellValueFactory(new PropertyValueFactory<ProductoHasPlato,Integer>("codigoPlato"));
        colCodigoProducto.setCellValueFactory(new PropertyValueFactory<ProductoHasPlato,Integer>("codigoProducto"));
    }
    
    public ObservableList<ProductoHasPlato> getPHP(){
        ArrayList<ProductoHasPlato> lista = new ArrayList<ProductoHasPlato>();
        
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarProductoHasPlato}");
            ResultSet resultado = procedimiento.executeQuery();
            
            while(resultado.next()){
                lista.add(new ProductoHasPlato(resultado.getInt("codigoProducto"),
                                               resultado.getInt("codigoPlato")));
            }
            
            
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return listaPHP=FXCollections.observableArrayList(lista);
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
    
     public ObservableList<Producto> getProducto(){
        ArrayList<Producto> lista = new ArrayList<Producto>();
        
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarProductos}");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                lista.add(new Producto(resultado.getInt("codigoProducto"),
                                       resultado.getString("nombreProducto"),
                                       resultado.getInt("cantidad")));
                
                
            }
            
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaProducto = FXCollections.observableArrayList(lista);
    }
     
     public void seleccionarElemento(){
         if(tblProductoHasPlato.getSelectionModel().getSelectedItem() !=null) {
             cmbCodigoPlato.getSelectionModel().select(buscarPlato(((ProductoHasPlato)tblProductoHasPlato.getSelectionModel().getSelectedItem()).getCodigoPlato()));
             cmbCodigoProducto.getSelectionModel().select(buscarProducto(((ProductoHasPlato)tblProductoHasPlato.getSelectionModel().getSelectedItem()).getCodigoProducto()));
         }
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
    
    public Producto buscarProducto(int codigoProducto){
        Producto resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarProductos(?)}");
            procedimiento.setInt(1, codigoProducto);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new Producto(registro.getInt("codigoProducto"),
                                         registro.getString("nombreProducto"),
                                         registro.getInt("cantidad"));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return resultado;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    public void menuPrincipal(){
        escenarioPrincipal.menuPrincipal();
    }
    
}
