
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
import org.heberajbal.bean.Empleado;
import org.heberajbal.bean.TipoEmpleado;

import org.heberajbal.db.Conexion;
import org.heberajbal.system.Principal;




public class EmpleadosController implements Initializable{

    private Principal escenarioPrincipal;

  
    private enum operacion {NUEVO,GUARDAR,ELIMINAR,ACTUALIZAR,CANCELAR,EDITAR,NINGUNO}
    private operacion tipoDeOperacion = operacion.NINGUNO; 
    private ObservableList<TipoEmpleado> listaTipoEmpleado;
    private ObservableList<Empleado> listaEmpleados;
    
    @FXML private TextField txtcodigoEmpleado;
    @FXML private TextField txtnumeroEmpleado;
    @FXML private TextField txtnombresEmpleado;
    @FXML private TextField txtapellidosEmpleados;
    @FXML private TextField txtdireccion;
    @FXML private TextField txttelefono;
    @FXML private TextField txtgradoCocinero;
    @FXML private ComboBox cmbtipoEmpleado;
    @FXML private TableView tblEmpleados;
    @FXML private TableColumn colcodigoEmpleado;
    @FXML private TableColumn colnumeroEmpleado;
    @FXML private TableColumn colnombresEmpleado;
    @FXML private TableColumn colapellidosEmpleado;
    @FXML private TableColumn coldireccion;
    @FXML private TableColumn coltelefono;
    @FXML private TableColumn colgradoCocinero;
    @FXML private TableColumn coltipoEmpleado;
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;
    
      @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        cmbtipoEmpleado.setItems(getTipoEmpleado());
    }
    
    public void cargarDatos(){
        tblEmpleados.setItems(getEmpleados());
        colcodigoEmpleado.setCellValueFactory(new PropertyValueFactory<Empleado,Integer>("codigoEmpleado"));
        colnumeroEmpleado.setCellValueFactory(new PropertyValueFactory<Empleado,Integer>("numeroEmpleado"));
        colnombresEmpleado.setCellValueFactory(new PropertyValueFactory<Empleado,String>("nombresEmpleado"));
        colapellidosEmpleado.setCellValueFactory(new PropertyValueFactory<Empleado,String>("apellidosEmpleado"));
        coldireccion.setCellValueFactory(new PropertyValueFactory<Empleado,String>("direccionEmpleado"));
        coltelefono.setCellValueFactory(new PropertyValueFactory<Empleado,String>("TelefonoContacto"));
        colgradoCocinero.setCellValueFactory(new PropertyValueFactory<Empleado,String>("gradoCocinero"));
        coltipoEmpleado.setCellValueFactory(new PropertyValueFactory<Empleado,Integer>("codigoTipoEmpleado"));
        
    }
    
    public void seleccionarElemento(){
        if (tblEmpleados.getSelectionModel().getSelectedItem() !=null) {
            txtcodigoEmpleado.setText(String.valueOf(((Empleado)tblEmpleados.getSelectionModel().getSelectedItem()).getCodigoEmpleado()));
            txtnumeroEmpleado.setText(String.valueOf(((Empleado)tblEmpleados.getSelectionModel().getSelectedItem()).getNumeroEmpleado()));
            txtnombresEmpleado.setText(((Empleado)tblEmpleados.getSelectionModel().getSelectedItem()).getNombresEmpleado());
            txtapellidosEmpleados.setText(((Empleado)tblEmpleados.getSelectionModel().getSelectedItem()).getApellidosEmpleado());
            txtdireccion.setText(((Empleado)tblEmpleados.getSelectionModel().getSelectedItem()).getDireccionEmpleado());
            txttelefono.setText(((Empleado)tblEmpleados.getSelectionModel().getSelectedItem()).getTelefonoContacto());
            txtgradoCocinero.setText(((Empleado)tblEmpleados.getSelectionModel().getSelectedItem()).getGradoCocinero());
            cmbtipoEmpleado.getSelectionModel().select(buscarTipoEmpleado(((Empleado)tblEmpleados.getSelectionModel().getSelectedItem()).getCodigoTipoEmpleado()));
        }
       
    }
    
    
    public ObservableList<Empleado> getEmpleados(){
        ArrayList<Empleado> lista = new ArrayList<Empleado>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarEmpleados}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Empleado(resultado.getInt("codigoEmpleado"),
                                        resultado.getInt("numeroEmpleado"),
                                        resultado.getString("nombresEmpleado"),
                                        resultado.getString("apellidosEmpleado"),
                                        resultado.getString("direccionEmpleado"),
                                        resultado.getString("TelefonoContacto"),
                                        resultado.getString("gradoCocinero"),
                                        resultado.getInt("codigoTipoEmpleado")));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
       return listaEmpleados = FXCollections.observableArrayList(lista);
    }
    
  public ObservableList<TipoEmpleado> getTipoEmpleado(){
        ArrayList<TipoEmpleado> lista = new ArrayList<TipoEmpleado>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarTipoEmpleado}");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()){
                lista.add(new TipoEmpleado(resultado.getInt("codigoTipoEmpleado"),
                                           resultado.getString("descripcion")));
                
                
            }
            
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaTipoEmpleado = FXCollections.observableArrayList(lista);
    } 
    
   public TipoEmpleado buscarTipoEmpleado(int codigoTipoEmpleado){
        TipoEmpleado resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarTipoEmpleado(?)}");
            procedimiento.setInt(1,codigoTipoEmpleado);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new TipoEmpleado(registro.getInt("codigoTipoEmpleado"),
                                             registro.getString("descripcion"));
            }
            
        }catch(Exception e){
            e.printStackTrace();
        }
        return resultado;
    }
   
   public void nuevo(){
       switch(tipoDeOperacion){
           case NINGUNO:
              activarControles();
              btnNuevo.setText("Guardar");
              btnEliminar.setText("Cancelar");
              btnEliminar.setDisable(false);
              btnEditar.setDisable(true);
              btnReporte.setDisable(true);
              tipoDeOperacion =operacion.GUARDAR;
              limpiarDatos();
               
            break; 
            
           case GUARDAR:
              if(validarCampos(txtnumeroEmpleado, txtnombresEmpleado, txtapellidosEmpleados, txtdireccion, txttelefono, txtgradoCocinero)){ 
               guardar();
               desactivarControles();
               limpiarDatos();
               btnNuevo.setText("Nuevo");
               btnEliminar.setText("Eliminar");
               btnEditar.setDisable(false);
               btnReporte.setDisable(false);
               cargarDatos();
               tipoDeOperacion = operacion.NINGUNO;
              }
            break;
            
       }
   }
    
      public void guardar(){
          Empleado registro = new Empleado();
          registro.setNumeroEmpleado(Integer.parseInt(txtnumeroEmpleado.getText()));
          registro.setApellidosEmpleado(txtapellidosEmpleados.getText());
          registro.setNombresEmpleado(txtnombresEmpleado.getText());
          registro.setDireccionEmpleado(txtdireccion.getText());
          registro.setTelefonoContacto(txttelefono.getText());
          registro.setGradoCocinero(txtgradoCocinero.getText());
          registro.setCodigoTipoEmpleado(((TipoEmpleado)cmbtipoEmpleado.getSelectionModel().getSelectedItem()).getCodigoTipoEmpleado());                 
          try{
              PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarEmpleados(?,?,?,?,?,?,?)}");
              procedimiento.setInt(1, registro.getNumeroEmpleado());
              procedimiento.setString(2, registro.getApellidosEmpleado());
              procedimiento.setString(3,registro.getNombresEmpleado());
              procedimiento.setString(4, registro.getDireccionEmpleado());
              procedimiento.setString(5,registro.getTelefonoContacto());
              procedimiento.setString(6, registro.getGradoCocinero());
              procedimiento.setInt(7,registro.getCodigoTipoEmpleado());
              procedimiento.execute();
              listaEmpleados.add(registro);
              
          }catch(Exception e){
              e.printStackTrace();
          }
      }
   
    public void eliminar(){
        switch(tipoDeOperacion){
            case GUARDAR:
                limpiarDatos();
                desactivarControles();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                tipoDeOperacion = operacion.NINGUNO;
            break;
            
            default:
                if(tblEmpleados.getSelectionModel().getSelectedItem() !=null){
                    int respuesta = JOptionPane.showConfirmDialog(null,"¿Está seguro de Eliminar?","Eliminar Empleado", JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_OPTION){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarEmpleados(?)}");
                            procedimiento.setInt(1,((Empleado)tblEmpleados.getSelectionModel().getSelectedItem()).getCodigoEmpleado());
                            procedimiento.execute();
                            listaEmpleados.remove(tblEmpleados.getSelectionModel().getSelectedIndex());
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }else {
                    JOptionPane.showMessageDialog(null ,"Seleccione un Dato");
                }
                break;
        }
    }
    
    public void editar(){
        switch(tipoDeOperacion){
            case NINGUNO:
                if(tblEmpleados.getSelectionModel().getSelectedItem() !=null){
                activarControles();
                btnEditar.setText("Actualizar");
                btnReporte.setText("Cancelar");
                btnEliminar.setDisable(true);
                btnNuevo.setDisable(true);
                tipoDeOperacion = operacion.ACTUALIZAR;
                
                }else{
                    JOptionPane.showMessageDialog(null,"Debe seleccionar un elemento");
                }
            break;
            
            case ACTUALIZAR:
                actualizar();
                btnEditar.setText("Editar");
                btnReporte.setText("Reporte");
                btnNuevo.setDisable(false);
                btnEliminar.setDisable(false);
                tipoDeOperacion = operacion.NINGUNO;
                cargarDatos();
            break;
                
        }
        
    }
    
    public void actualizar(){
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ActualizarEmpleados(?,?,?,?,?,?,?,?)}");
            Empleado registro = (Empleado)tblEmpleados.getSelectionModel().getSelectedItem();
            registro.setNumeroEmpleado(Integer.parseInt(txtnumeroEmpleado.getText()));
            registro.setApellidosEmpleado(txtapellidosEmpleados.getText());
            registro.setNombresEmpleado(txtnombresEmpleado.getText());
            registro.setDireccionEmpleado(txtdireccion.getText());
            registro.setTelefonoContacto(txttelefono.getText());
            registro.setGradoCocinero(txtgradoCocinero.getText());
            registro.setCodigoTipoEmpleado(((TipoEmpleado)cmbtipoEmpleado.getSelectionModel().getSelectedItem()).getCodigoTipoEmpleado());
            procedimiento.setInt(1,registro.getCodigoEmpleado());
            procedimiento.setInt(2, registro.getNumeroEmpleado());
            procedimiento.setString(3, registro.getApellidosEmpleado());
            procedimiento.setString(4,registro.getNombresEmpleado());
            procedimiento.setString(5, registro.getDireccionEmpleado());
            procedimiento.setString(6,registro.getTelefonoContacto());
            procedimiento.setString(7, registro.getGradoCocinero());
            procedimiento.setInt(8,registro.getCodigoTipoEmpleado());
            procedimiento.execute();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
      
    public void reporte(){
        switch(tipoDeOperacion){
            case ACTUALIZAR:
                desactivarControles();
                limpiarDatos();
                btnEditar.setText("Editar");
                btnReporte.setText("Reporte");
                btnNuevo.setDisable(false);
                btnEliminar.setDisable(false);
                tipoDeOperacion= operacion.NINGUNO;
                break;
                
                
        }
    }
      
   public void desactivarControles(){
       txtapellidosEmpleados.setEditable(false);
       txtcodigoEmpleado.setEditable(false);
       txtdireccion.setEditable(false);
       txtgradoCocinero.setEditable(false);
       txtnombresEmpleado.setEditable(false);
       txtnumeroEmpleado.setEditable(false);
       txttelefono.setEditable(false);
       cmbtipoEmpleado.setEditable(false);
   }
   
   public void activarControles(){
       txtcodigoEmpleado.setEditable(false);
       txtapellidosEmpleados.setEditable(true);
       txtdireccion.setEditable(true);
       txtgradoCocinero.setEditable(true);
       txtnombresEmpleado.setEditable(true);
       txtnumeroEmpleado.setEditable(true);
       txttelefono.setEditable(true);
       cmbtipoEmpleado.setEditable(false);
   }
   
   public void limpiarDatos(){
       txtapellidosEmpleados.setText("");
       txtcodigoEmpleado.setText("");
       txtdireccion.setText("");
       txtgradoCocinero.setText("");
       txtnombresEmpleado.setText("");
       txtnumeroEmpleado.setText("");
       txttelefono.setText("");
       cmbtipoEmpleado.getSelectionModel().clearSelection();
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
   
    public void ventanaTipoEmpleado(){
      escenarioPrincipal.ventanaTipoEmpleado();
  }
    
    private boolean validarCampos(TextField numero, TextField nombre,TextField apellido, TextField direccion, TextField telefono, TextField grado){
        boolean opcion = false;
        if(!numero.getText().equals("") && !nombre.getText().equals("") && !apellido.getText().equals("") && !direccion.getText().equals("") && !telefono.getText().equals("")
                && !grado.getText().equals(""))
            opcion = true;
        else 
            JOptionPane.showMessageDialog(null,"Por favor llene todos los campos", "ADVERTENCIA",JOptionPane.INFORMATION_MESSAGE);
        
        return opcion;
    }
    
    
}
