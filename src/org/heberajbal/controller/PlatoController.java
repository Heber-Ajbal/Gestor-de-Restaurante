
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javax.swing.JOptionPane;
import org.heberajbal.bean.Plato;
import org.heberajbal.bean.TipoPlato;
import org.heberajbal.db.Conexion;
import org.heberajbal.system.Principal;


public class PlatoController implements Initializable{

    private enum operaciones{NUEVO,GUARDAR, ELIMINAR,EDITAR,ACTUALIZAR,CANCELAR,NINGUNO};
        private operaciones tipoDeOperacion = operaciones.NINGUNO;
        private Principal escenarioPrincipal;
        private ObservableList<Plato> listaPlato;
        private ObservableList<TipoPlato> listaTipoPlato;
        
        @FXML private TextField txtCodigoPlato;
        @FXML private TextField txtCantidad;
        @FXML private TextField txtNombre;
        @FXML private TextField txtDescripcion;
        @FXML private TextField txtPrecio;
        @FXML private ComboBox cmbCodigoTipoPlato;
        @FXML private TableView tblPlato;
        @FXML private TableColumn colCodigoPlato;
        @FXML private TableColumn colCantidad;
        @FXML private TableColumn colNombre;
        @FXML private TableColumn colDescripcion;
        @FXML private TableColumn colPrecio;
        @FXML private TableColumn colCodigoTipoPlato;
        @FXML private Button btnNuevo;
        @FXML private Button btnEliminar;
        @FXML private Button btnEditar;
        @FXML private Button btnReporte;
        
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        cmbCodigoTipoPlato.setItems(getTipoPlato());
    }
    
    public void cargarDatos(){
        tblPlato.setItems(getPlato());
        colCodigoPlato.setCellValueFactory(new PropertyValueFactory<Plato,Integer>("codigoPlato"));
        colCantidad.setCellValueFactory(new PropertyValueFactory<Plato,Integer>("cantidad"));
        colNombre.setCellValueFactory(new PropertyValueFactory<Plato,String>("nombrePlato"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<Plato,String>("descripcionPlato"));
        colPrecio.setCellValueFactory(new PropertyValueFactory<Plato,Double>("precioPlato"));
        colCodigoTipoPlato.setCellValueFactory(new PropertyValueFactory<Plato,Integer>("codigoTipoPlato"));
        
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
    
    public ObservableList<TipoPlato> getTipoPlato(){
        ArrayList<TipoPlato> lista = new ArrayList<TipoPlato>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarTipoPlato}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new TipoPlato(resultado.getInt("codigoTipoPlato"),
                                        resultado.getString("descripcionTipo")));
            }
            
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaTipoPlato = FXCollections.observableArrayList(lista);
    }
    
    public void nuevo(){
        switch(tipoDeOperacion){
            case NINGUNO:
                limpiarControles();
                activarControles();
                btnNuevo.setText("Guardar");
                btnEliminar.setText("Cancelar");
                btnEditar.setDisable(true);
                btnReporte.setDisable(true);
                tipoDeOperacion = operaciones.GUARDAR;
                break;
                
            case GUARDAR:
                if(Validar(txtCantidad, txtNombre, txtDescripcion, txtPrecio, cmbCodigoTipoPlato)){
                    guardar();
                    desactivarControles();
                    limpiarControles();
                    btnNuevo.setText("Nuevo");
                    btnEliminar.setText("Eliminar");
                    btnEditar.setDisable(false);
                    btnReporte.setDisable(false);
                    tipoDeOperacion = operaciones.NINGUNO;
                    cargarDatos();
                }
                break;         
        }
    }
    
    public TipoPlato buscarTipoPlato(int codigoTipoPlato){
       TipoPlato resultado = null;
       try{
           PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarTipoPlato(?)}");
           procedimiento.setInt(1,codigoTipoPlato);
           ResultSet registro = procedimiento.executeQuery();
           
           while (registro.next()) {
               resultado = new TipoPlato(registro.getInt("codigoTipoPlato"),
                                         registro.getString("descripcionTipo"));
               
           }
           
       }catch(Exception e ){
           e.printStackTrace();
       }
        return resultado;
    }
    
    public void guardar(){
        Plato registro = new Plato();
        
        registro.setCantidad(Integer.parseInt(txtCantidad.getText()));
        registro.setNombrePlato(txtNombre.getText());
        registro.setDescripcionPlato(txtDescripcion.getText());
        registro.setPrecioPlato(Double.parseDouble(txtPrecio.getText()));
        registro.setCodigoTipoPlato(((TipoPlato)cmbCodigoTipoPlato.getSelectionModel().getSelectedItem()).getCodigoTipoPlato());
        
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarPlatos(?,?,?,?,?)}");
            procedimiento.setInt(1,registro.getCantidad());
            procedimiento.setString(2,registro.getNombrePlato());
            procedimiento.setString(3,registro.getDescripcionPlato());
            procedimiento.setDouble(4, registro.getPrecioPlato());
            procedimiento.setInt(5,registro.getCodigoTipoPlato());
            procedimiento.execute();
            listaPlato.add(registro);
            
            
        }catch(Exception e){
            e.printStackTrace();
        }
        
    }
    
     public void selecionarElemento(){
        if(tblPlato.getSelectionModel().getSelectedItem() !=null){
        txtCodigoPlato.setText(String.valueOf(((Plato)tblPlato.getSelectionModel().getSelectedItem()).getCodigoPlato()));
        txtCantidad.setText(String.valueOf(((Plato)tblPlato.getSelectionModel().getSelectedItem()).getCantidad()));
        txtNombre.setText(((Plato)tblPlato.getSelectionModel().getSelectedItem()).getNombrePlato());
        txtDescripcion.setText(((Plato)tblPlato.getSelectionModel().getSelectedItem()).getDescripcionPlato());
        txtPrecio.setText(String.valueOf(((Plato)tblPlato.getSelectionModel().getSelectedItem()).getPrecioPlato()));
        cmbCodigoTipoPlato.getSelectionModel().select(buscarTipoPlato(((Plato)tblPlato.getSelectionModel().getSelectedItem()).getCodigoTipoPlato()));
    }
     }
    
    public void Eliminar(){
        switch(tipoDeOperacion){
            case GUARDAR:
                limpiarControles();
                desactivarControles();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                tipoDeOperacion= operaciones.NINGUNO;
                break;
                
            default:
                if(tblPlato.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null,"¿Está seguro de eliminar el registro?","Eliminar Plato", JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_OPTION){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarPlatos(?)}");
                            procedimiento.setInt(1,((Plato)tblPlato.getSelectionModel().getSelectedItem()).getCodigoPlato());
                            procedimiento.execute();
                            listaPlato.remove(tblPlato.getSelectionModel().getSelectedIndex());
                            
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                        
                }else{
                    JOptionPane.showMessageDialog(null,"Seleccione un Dato");
                }
        }
    }
    
    public void Editar(){
        switch(tipoDeOperacion){
            case NINGUNO:
                activarControles();
                btnEditar.setText("Actualizar");
                btnReporte.setText("Cancelar");
                btnNuevo.setDisable(true);
                btnEliminar.setDisable(true);
                tipoDeOperacion = operaciones.ACTUALIZAR;
            break;
            
            case ACTUALIZAR:
                actualizar();
                limpiarControles();
                desactivarControles();
                cargarDatos();
                btnEditar.setText("Editar");
                btnReporte.setText("Reporte");
                btnNuevo.setDisable(false);
                btnEliminar.setDisable(false);
                tipoDeOperacion= operaciones.NINGUNO;
        }
    }
    
    public void actualizar(){
        try{
            
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ActualizarPlatos(?,?,?,?,?,?)}");
            Plato registro= (Plato)tblPlato.getSelectionModel().getSelectedItem();
            registro.setCantidad(Integer.parseInt(txtCantidad.getText()));
            registro.setNombrePlato(txtNombre.getText());
            registro.setDescripcionPlato(txtDescripcion.getText());
            registro.setPrecioPlato(Double.parseDouble(txtPrecio.getText()));
            registro.setCodigoTipoPlato(((TipoPlato)cmbCodigoTipoPlato.getSelectionModel().getSelectedItem()).getCodigoTipoPlato());
        
            procedimiento.setInt(1, registro.getCodigoPlato());
            procedimiento.setInt(2,registro.getCantidad());
            procedimiento.setString(3,registro.getNombrePlato());
            procedimiento.setString(4,registro.getDescripcionPlato());
            procedimiento.setDouble(5, registro.getPrecioPlato());
            procedimiento.setInt(6,registro.getCodigoTipoPlato());
            procedimiento.execute();
                                   
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void reporte(){
        switch(tipoDeOperacion){
            case ACTUALIZAR:
                btnEditar.setText("Editar");
                btnReporte.setText("Reporte");
                btnNuevo.setDisable(false);
                btnEliminar.setDisable(false);
                tipoDeOperacion = operaciones.NINGUNO;
                limpiarControles();
                break;
        }
    }

    public void desactivarControles(){
        txtCodigoPlato.setEditable(false);
        txtCantidad.setEditable(false);
        txtNombre.setEditable(false);
        txtDescripcion.setEditable(false);
        txtPrecio.setEditable(false);
        cmbCodigoTipoPlato.setEditable(false);
        
    }
    
    public void activarControles(){
        txtCodigoPlato.setEditable(false);
        txtCantidad.setEditable(true);
        txtNombre.setEditable(true);
        txtDescripcion.setEditable(true);
        txtPrecio.setEditable(true);
        cmbCodigoTipoPlato.setEditable(false);
    }
    
    public void limpiarControles(){
        txtCodigoPlato.setText("");
        txtCantidad.setText("");
        txtNombre.setText("");
        txtDescripcion.setText("");
        txtPrecio.setText("");
        cmbCodigoTipoPlato.getSelectionModel().clearSelection();
    }
    
    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    public void ventanaTipoPlato(){
        escenarioPrincipal.ventanaTipoPlato();
    }
    
    public boolean Validar(TextField cantidad,TextField nombre,TextField descripcion,TextField precio, ComboBox CTipoPlato){
        boolean respuesta = false;
        
        if(!cantidad.getText().equals("") && !nombre.getText().equals("") && !descripcion.getText().equals("")
                && !precio.getText().equals("") && (CTipoPlato.getSelectionModel().getSelectedItem() != null))
            
            respuesta = true;
        else 
            JOptionPane.showMessageDialog(null,"Por favor llene todos los campos", "ADVERTENCIA",JOptionPane.INFORMATION_MESSAGE);
        
        return respuesta;
    }
    
}

