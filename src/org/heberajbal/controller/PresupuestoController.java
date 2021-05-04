
package org.heberajbal.controller;

import eu.schudt.javafx.controls.calendar.DatePicker;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
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
import javafx.scene.layout.GridPane;
import javax.swing.JOptionPane;
import org.heberajbal.bean.Empresa;
import org.heberajbal.bean.Presupuesto;
import org.heberajbal.db.Conexion;
import org.heberajbal.report.GenerarReporte;
import org.heberajbal.system.Principal;


public class PresupuestoController implements Initializable {
    private enum operaciones{NUEVO,GUARDAR,ELIMINAR,EDITAR,ACTUALIZAR,CANCELAR,NINGUNO};
    private Principal escenarioPrincipal;
    private operaciones tipoDeOperacion= operaciones.NINGUNO;
    private ObservableList<Empresa> listaEmpresa;
    private ObservableList<Presupuesto> listaPresupuesto;
    private DatePicker fecha;
    
    @FXML private TextField txtCodigoPresupuesto;
    @FXML private TextField txtCantidadPresupuesto;
    @FXML private GridPane grpFecha; 
    @FXML private ComboBox cmbCodigoEmpresa;
    @FXML private TableView tblPresupuesto;
    @FXML private TableColumn colCodigoPresupuesto;
    @FXML private TableColumn colFechaSolicitud;
    @FXML private TableColumn colCantidadPresupuesto;
    @FXML private TableColumn colCodigoEmpresa;
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
       cargarDatos();
       fecha = new DatePicker(Locale.ENGLISH);
       fecha.setDateFormat(new SimpleDateFormat("yyy-MM-dd"));
       fecha.getCalendarView().todayButtonTextProperty().set("Today");
       fecha.getCalendarView().setShowWeeks(false);
       fecha.getStylesheets().add("/org/heberajbal/resource/DatePicker.css");
       grpFecha.add(fecha,0,0);
       cmbCodigoEmpresa.setItems(getEmpresa());
    }
    
    public void cargarDatos(){
        tblPresupuesto.setItems(getPresupuesto());
        colCodigoPresupuesto.setCellValueFactory(new PropertyValueFactory<Presupuesto,Integer>("codigoPresupuesto"));
        colFechaSolicitud.setCellValueFactory(new PropertyValueFactory<Presupuesto, Date>("fechaSolicitud"));
        colCantidadPresupuesto.setCellValueFactory(new PropertyValueFactory<Presupuesto,Double>("cantidadPresupuesto"));
        colCodigoEmpresa.setCellValueFactory(new PropertyValueFactory<Presupuesto,Integer>("codigoEmpresa"));
        
        
        
        
    }
    
    public void seleccionarElementos(){
        if (tblPresupuesto.getSelectionModel().getSelectedItem() !=null){       
            txtCodigoPresupuesto.setText(String.valueOf(((Presupuesto)tblPresupuesto.getSelectionModel().getSelectedItem()).getCodigoPresupuesto()));
            fecha.selectedDateProperty().set(((Presupuesto)tblPresupuesto.getSelectionModel().getSelectedItem()).getFechaSolicitud());
            txtCantidadPresupuesto.setText(String.valueOf(((Presupuesto)tblPresupuesto.getSelectionModel().getSelectedItem()).getCantidadPresupuesto()));
            cmbCodigoEmpresa.getSelectionModel().select(buscarEmpresa(((Presupuesto)tblPresupuesto.getSelectionModel().getSelectedItem()).getCodigoEmpresa()));
        }
    }
    
   public ObservableList<Presupuesto> getPresupuesto(){
        ArrayList<Presupuesto> lista = new ArrayList<Presupuesto>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarPresupuesto}");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()){
                lista.add(new Presupuesto(resultado.getInt("codigoPresupuesto"),
                                          resultado.getDate("fechaSolicitud"),
                                          resultado.getDouble("cantidadPresupuesto"),
                                          resultado.getInt("codigoEmpresa")));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return listaPresupuesto = FXCollections.observableArrayList(lista);
    }
    
    public ObservableList<Empresa> getEmpresa(){
        ArrayList<Empresa> lista = new ArrayList<Empresa>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarEmpresa}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Empresa(resultado.getInt("codigoEmpresa"),
                                      resultado.getString("nombreEmpresa"),
                                      resultado.getString("direccion"),
                                      resultado.getString("telefono")));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return listaEmpresa = FXCollections.observableArrayList(lista);
    }
    
    public void nuevo(){
        switch(tipoDeOperacion){
            case NINGUNO:
                activarControles();
                btnNuevo.setText("Guardar");
                btnEliminar.setDisable(false);
                btnEliminar.setText("Cancelar");
                btnEditar.setDisable(true);
                btnReporte.setDisable(true);
                tipoDeOperacion = operaciones.GUARDAR;
                limpiarDatos();   
                break;
                
            case GUARDAR:
            if(validar(txtCantidadPresupuesto,cmbCodigoEmpresa)){
                guardar();
                desactivarControles();
                limpiarDatos();
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
        

    public Empresa buscarEmpresa(int codigoEmpresa){
        Empresa resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarEmpresa(?)}");
            procedimiento.setInt(1,codigoEmpresa);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new Empresa(registro.getInt("codigoEmpresa"),
                                        registro.getString("nombreEmpresa"),
                                        registro.getString("direccion"),
                                        registro.getString("telefono"));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return resultado;
    }
    
    
    
    public void guardar(){
        Presupuesto registro = new Presupuesto();
        registro.setFechaSolicitud(fecha.getSelectedDate());
        registro.setCantidadPresupuesto(Double.parseDouble(txtCantidadPresupuesto.getText()));
        registro.setCodigoEmpresa(((Empresa)cmbCodigoEmpresa.getSelectionModel().getSelectedItem()).getCodigoEmpresa());
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarPresupuesto(?,?,?)}");
            procedimiento.setDate(1, new java.sql.Date(registro.getFechaSolicitud().getTime()));
            procedimiento.setDouble(2, registro.getCantidadPresupuesto());
            procedimiento.setInt(3,registro.getCodigoEmpresa());
            procedimiento.execute();
            listaPresupuesto.add(registro);
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void eliminar(){
        switch(tipoDeOperacion){
            case GUARDAR:
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                limpiarDatos();
                desactivarControles();
                tipoDeOperacion = operaciones.NINGUNO;
                break;
                
                default:
                    if(tblPresupuesto.getSelectionModel().getSelectedItem() !=null){
                         int respuesta = JOptionPane.showConfirmDialog(null,"¿Está seguro de eliminar?","Eliminar presupuesto",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                         if(respuesta == JOptionPane.YES_OPTION){
                            try{
                                 PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarPresupuesto(?)}");
                                 procedimiento.setInt(1,((Presupuesto)tblPresupuesto.getSelectionModel().getSelectedItem()).getCodigoPresupuesto());
                                 procedimiento.execute();
                                listaPresupuesto.remove(tblPresupuesto.getSelectionModel().getSelectedIndex());
                                 
                            }catch(Exception e){
                                 e.printStackTrace();
                            }
                        }
                    }else{
                        JOptionPane.showMessageDialog(null,"Seleccione un dato");
                    }
                break;
        }
    }
    
    public void editar(){
        switch(tipoDeOperacion){
            case NINGUNO:
                if(tblPresupuesto.getSelectionModel().getSelectedItem() != null){
                    activarControles();
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    btnNuevo.setDisable(true);
                    btnEliminar.setDisable(true);
                    tipoDeOperacion = operaciones.ACTUALIZAR;
                    
                }else{
                    JOptionPane.showMessageDialog(null,"Debe seleccionar un elemento");
                }
            break;
            
            case ACTUALIZAR:
                desactivarControles();
                actualizar();
                btnEditar.setText("Editar");
                btnReporte.setText("Reporte");
                btnNuevo.setDisable(false);
                btnEliminar.setDisable(false);
                tipoDeOperacion = operaciones.NINGUNO;
                cargarDatos();
            break;
        }
    }
    
    public void actualizar(){
        try{
        PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ActualizarPresupuesto(?,?,?,?)}");
        Presupuesto registro = (Presupuesto)tblPresupuesto.getSelectionModel().getSelectedItem();
        registro.setFechaSolicitud(fecha.getSelectedDate());
        registro.setCantidadPresupuesto(Double.parseDouble(txtCantidadPresupuesto.getText()));
        registro.setCodigoEmpresa(((Empresa)cmbCodigoEmpresa.getSelectionModel().getSelectedItem()).getCodigoEmpresa());
        
        procedimiento.setInt(1, registro.getCodigoPresupuesto());
        procedimiento.setDate(2,new java.sql.Date(registro.getFechaSolicitud().getTime()));
        procedimiento.setDouble(3, registro.getCantidadPresupuesto());
        procedimiento.setInt(4,registro.getCodigoEmpresa());
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
                break;
                
            case NINGUNO:
                if(tblPresupuesto.getSelectionModel().getSelectedItem() != null){
                    imprimirReporte();
                }else{
                    JOptionPane.showMessageDialog(null,"Debe Seleccionar un elemento");
                    tipoDeOperacion = operaciones.NINGUNO;
                }
                break;
                
        }
    }
    
    public void imprimirReporte(){
        Map parametro = new HashMap();
        int codEmpresa = Integer.valueOf(((Empresa)cmbCodigoEmpresa.getSelectionModel().getSelectedItem()).getCodigoEmpresa());
        parametro.put("codEmpresa",codEmpresa);
        GenerarReporte.mostrarReporte("ReportePresupuesto.jasper", "Reporte Presupuesto", parametro);
    }
    
    public void desactivarControles(){
        txtCantidadPresupuesto.setEditable(false);
        txtCodigoPresupuesto.setEditable(false);
        grpFecha.setDisable(false);
        cmbCodigoEmpresa.setEditable(false);
    }
    
    public void activarControles(){
        txtCantidadPresupuesto.setEditable(true);
        txtCodigoPresupuesto.setEditable(false);
        grpFecha.setDisable(false);
        cmbCodigoEmpresa.setEditable(false);
        
    }
    
    public void limpiarDatos(){
        txtCodigoPresupuesto.setText("");
        txtCantidadPresupuesto.setText("");
        cmbCodigoEmpresa.getSelectionModel().clearSelection();
        
    }
    
    

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
     public void ventanaEmpresas(){
          escenarioPrincipal.ventanaEmpresas();
      }
     
     private boolean validar( TextField presupuesto,ComboBox codigoEmpresa){
         boolean respuesta = false;
         
         if(!presupuesto.getText().equals("") && (codigoEmpresa.getSelectionModel().getSelectedItem() !=null) && (fecha.getSelectedDate() !=null))
             
             respuesta=true;  
         else
             JOptionPane.showMessageDialog(null,"Por favor llene todos los campos","ADVERTENCIA",JOptionPane.INFORMATION_MESSAGE);
         
         return respuesta;
         
     }
      
}
