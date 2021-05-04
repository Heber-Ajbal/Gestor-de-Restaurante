
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
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javax.swing.JOptionPane;
import org.heberajbal.bean.Producto;
import org.heberajbal.db.Conexion;
import org.heberajbal.system.Principal;


public class ProductoController implements Initializable {

    private Principal escenarioPrincipal;
    private enum operaciones{NUEVO,GUARDAR,ELIMINAR,EDITAR,CANCELAR,REPORTE,NINGUNO,ACTUALIZAR};
    private operaciones tipoDeOperaciones = operaciones.NINGUNO;
    private ObservableList<Producto> listaProducto;
    
    private @FXML TextField txtCodigoProducto;
    private @FXML TextField txtNombre;
    private @FXML TextField txtCantidad;
    private @FXML TableView tblProducto;
    private @FXML TableColumn colCodigoProducto;
    private @FXML TableColumn colNombre;
    private @FXML TableColumn colCantidad;
    private @FXML Button btnNuevo;
    private @FXML Button btnEliminar;
    private @FXML Button btnEditar;
    private @FXML Button btnReporte;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
    }
    
    public void cargarDatos(){
        tblProducto.setItems(getProducto());
        colCodigoProducto.setCellValueFactory(new PropertyValueFactory<Producto,Integer>("codigoProducto"));
        colNombre.setCellValueFactory(new PropertyValueFactory<Producto,String>("nombreProducto"));
        colCantidad.setCellValueFactory(new PropertyValueFactory<Producto,Integer>("cantidad"));
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
    
    public void nuevo(){
        switch(tipoDeOperaciones){
            case NINGUNO:
               
                activarControles();
                btnNuevo.setText("Guardar");
                btnEliminar.setText("Cancelar");
                btnEditar.setDisable(true);
                btnReporte.setDisable(true);
                tipoDeOperaciones = operaciones.GUARDAR;
                limpiarControles();
                break;
                
            case GUARDAR:
                if(Validar(txtNombre, txtCantidad)){
                guardar();
                desactivarControles();
                cargarDatos(); 
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnReporte.setDisable(false);
                btnEditar.setDisable(false);
                tipoDeOperaciones = operaciones.NINGUNO;
                limpiarControles();
                }
                break;
                
        }
    }
    
    public void guardar(){
        Producto registro = new Producto();
        
        registro.setNombreProducto(txtNombre.getText());
        registro.setCantidad(Integer.parseInt(txtCantidad.getText()));
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarProductos(?,?)}");
            procedimiento.setString(1, registro.getNombreProducto());
            procedimiento.setInt(2,registro.getCantidad());
            procedimiento.execute();
            listaProducto.add(registro);
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void seleccionarElemento(){
        if(tblProducto.getSelectionModel().getSelectedItem() !=null){
            txtCodigoProducto.setText(String.valueOf(((Producto)tblProducto.getSelectionModel().getSelectedItem()).getCodigoProducto()));
            txtNombre.setText(((Producto)tblProducto.getSelectionModel().getSelectedItem()).getNombreProducto());
            txtCantidad.setText(String.valueOf(((Producto)tblProducto.getSelectionModel().getSelectedItem()).getCantidad()));
            
        }
    }
    
    public void Eliminar(){
        switch(tipoDeOperaciones){
            case GUARDAR:
                desactivarControles();
                limpiarControles();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                tipoDeOperaciones = operaciones.NINGUNO;
                break;
            
            default:
                if(tblProducto.getSelectionModel().getSelectedItem() !=null){
                    int respuesta = JOptionPane.showConfirmDialog(null,"¿Está seguro de Eliminar el registro?","Eliminar Producto", JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_OPTION){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarProductos(?)}");
                            procedimiento.setInt(1, ((Producto)tblProducto.getSelectionModel().getSelectedItem()).getCodigoProducto());
                            procedimiento.execute();
                            listaProducto.remove(tblProducto.getSelectionModel().getSelectedIndex());
                           limpiarControles();
                            
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                        
                    }
                }else{
                    JOptionPane.showMessageDialog(null,"Debe selecionar un Elemento");
                }
        }
        
    }
    
    public void Editar(){
        switch(tipoDeOperaciones){
            case NINGUNO:
                if(tblProducto.getSelectionModel().getSelectedItem() !=null){
                    activarControles();
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    btnNuevo.setDisable(true);
                    btnEliminar.setDisable(true);
                    tipoDeOperaciones = operaciones.ACTUALIZAR;
                }else{
                     JOptionPane.showMessageDialog(null,"Seleccione un registro");
                }
                break;
                
            case ACTUALIZAR:
                desactivarControles();
                actualizar();
                cargarDatos();
                limpiarControles();
                btnEditar.setText("Editar");
                btnReporte.setText("Reporte");
                btnNuevo.setDisable(false);
                btnEliminar.setDisable(false);
                tipoDeOperaciones=operaciones.NINGUNO;
                break;
               
                
        }
    }
    
    public void actualizar(){
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ActualizarProductos(?,?,?)}");
            Producto registro =(Producto)tblProducto.getSelectionModel().getSelectedItem();
            
            registro.setNombreProducto(txtNombre.getText());
            registro.setCantidad(Integer.parseInt(txtCantidad.getText()));
            
            procedimiento.setInt(1,registro.getCodigoProducto());
            procedimiento.setString(2,registro.getNombreProducto());
            procedimiento.setInt(3,registro.getCantidad());
            procedimiento.execute();
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
     public void reporte(){
        switch(tipoDeOperaciones){
            case ACTUALIZAR:
                btnEditar.setText("Editar");
                btnReporte.setText("Reporte");
                btnNuevo.setDisable(false);
                btnEliminar.setDisable(false);
                tipoDeOperaciones = operaciones.NINGUNO;
                limpiarControles();
                break;
        }
    }
    
    public void desactivarControles(){
        txtCodigoProducto.setEditable(false);
        txtNombre.setEditable(false);
        txtCantidad.setEditable(false);        
    }
    
    public void activarControles(){
        txtCodigoProducto.setEditable(false);
        txtNombre.setEditable(true);
        txtCantidad.setEditable(true);
    }
    
    public void limpiarControles(){
        txtCodigoProducto.setText("");
        txtNombre.setText("");
        txtCantidad.setText("");
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
    
    private  boolean Validar(TextField nombre, TextField cantidad){
        boolean respuesta = false;
        
        if(!nombre.getText().equals("") && !cantidad.getText().equals(""))
            respuesta = true; 
        else
            JOptionPane.showMessageDialog(null,"Por favor llene todos los campos","ADVERTENCIA",JOptionPane.INFORMATION_MESSAGE);
        return respuesta;
    }
    
}
