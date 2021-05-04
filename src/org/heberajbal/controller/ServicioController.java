
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
import org.heberajbal.bean.Servicio;
import org.heberajbal.db.Conexion;
import org.heberajbal.report.GenerarReporte;
import org.heberajbal.system.Principal;


public class ServicioController implements Initializable {

    private Principal escenarioPrincipal;
    private enum operaciones{NUEVO,ELIMINAR,ACTUALIZAR,GUARDAD,CANCELAR,NINGUNO,EDITAR}
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<Servicio> listaServicio;
    private ObservableList<Empresa> listaEmpresa;
    private DatePicker fecha;
    
    @FXML private TextField txtCodigoServicio;
    @FXML private TextField txtTipoServicio;
    @FXML private TextField txtHoraServicio;
    @FXML private TextField txtLugarServicio;
    @FXML private TextField txtTelefonoContacto;
    @FXML private ComboBox cmbCodigoEmpresa;
    @FXML private GridPane grpFechaServicio; 
    @FXML private TableView tblServicio;
    @FXML private TableColumn colCodigoServicio;
    @FXML private TableColumn colFechaServicio;
    @FXML private TableColumn colTipoServicio;
    @FXML private TableColumn colHoraServicio;
    @FXML private TableColumn colLugarServicio;
    @FXML private TableColumn colTelefonoContacto;
    @FXML private TableColumn colCodigoEmpresa;
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;
    
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
       cargarDatos(); 
       fecha = new DatePicker(Locale.ENGLISH);
       fecha.setDateFormat(new SimpleDateFormat("yy-MM-dd"));
       fecha.getCalendarView().todayButtonTextProperty().set("Today");
       fecha.getCalendarView().setShowWeeks(false);
       grpFechaServicio.add(fecha, 1, 1);
       fecha.getStylesheets().add("/org/heberajbal/resource/DatePicker.css");
       cmbCodigoEmpresa.setItems(getEmpresa());
       
    }

    public void cargarDatos(){
        tblServicio.setItems(getServicio());
        colCodigoServicio.setCellValueFactory(new PropertyValueFactory<Servicio,Integer>("codigoServicio"));
        colFechaServicio.setCellValueFactory(new PropertyValueFactory<Servicio,Date>("fechaServicio"));
        colTipoServicio.setCellValueFactory(new PropertyValueFactory<Servicio,String>("tipoServicio"));
        colHoraServicio.setCellValueFactory(new PropertyValueFactory<Servicio,String>("horaServicio"));
        colLugarServicio.setCellValueFactory(new PropertyValueFactory<Servicio,String>("lugarServicio"));
        colTelefonoContacto.setCellValueFactory(new PropertyValueFactory<Servicio,String>("telefonoContacto"));
        colCodigoEmpresa.setCellValueFactory(new PropertyValueFactory<Servicio,Integer>("codigoEmpresa"));        
    }
    
    public void seleccionarElementos(){
        if(tblServicio.getSelectionModel().getSelectedItem() !=null){
            txtCodigoServicio.setText(String.valueOf(((Servicio)tblServicio.getSelectionModel().getSelectedItem()).getCodigoServicio()));
            fecha.selectedDateProperty().set(((Servicio)tblServicio.getSelectionModel().getSelectedItem()).getFechaServicio());
            txtTipoServicio.setText(((Servicio)tblServicio.getSelectionModel().getSelectedItem()).getTipoServicio());
            txtHoraServicio.setText(((Servicio)tblServicio.getSelectionModel().getSelectedItem()).getHoraServicio());
            txtLugarServicio.setText(((Servicio)tblServicio.getSelectionModel().getSelectedItem()).getLugarServicio());
            txtTelefonoContacto.setText(((Servicio)tblServicio.getSelectionModel().getSelectedItem()).getTelefonoContacto());
            cmbCodigoEmpresa.getSelectionModel().select(buscarEmpresa(((Servicio)tblServicio.getSelectionModel().getSelectedItem()).getCodigoEmpresa()));
        }
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
    
    public ObservableList<Empresa> getEmpresa(){
        ArrayList<Empresa> lista = new ArrayList<Empresa>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarEmpresa}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Empresa( resultado.getInt("codigoEmpresa"),
                                       resultado.getString("nombreEmpresa"),
                                       resultado.getString("direccion"),
                                       resultado.getString("telefono")));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaEmpresa = FXCollections.observableArrayList(lista);
        
    }
       
    public Empresa buscarEmpresa(int codigoEmpresa){
        Empresa resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarEmpresa(?)}");
            procedimiento.setInt(1, codigoEmpresa);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new Empresa(
                                    registro.getInt("codigoEmpresa"),
                                    registro.getString("nombreEmpresa"),
                                    registro.getString("direccion"),
                                    registro.getString("telefono"));          
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
                btnEditar.setDisable(true);
                btnReporte.setDisable(true);
                tipoDeOperacion = operaciones.GUARDAD;
                limpiarControles();
            break;
            
            case GUARDAD:
                if(Validar(txtTipoServicio, txtHoraServicio, txtLugarServicio, txtTelefonoContacto, cmbCodigoEmpresa)){
                    guardar();
                    desactivarcontroles();
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
      
    public void guardar(){
        Servicio registro = new Servicio();
        registro.setFechaServicio(fecha.getSelectedDate());
        registro.setTipoServicio(txtTipoServicio.getText());
        registro.setHoraServicio(txtHoraServicio.getText());
        registro.setLugarServicio(txtLugarServicio.getText());
        registro.setTelefonoContacto(txtTelefonoContacto.getText());
        registro.setCodigoEmpresa(((Empresa)cmbCodigoEmpresa.getSelectionModel().getSelectedItem()).getCodigoEmpresa());
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarServicio(?,?,?,?,?,?)}");
            procedimiento.setDate(1, new java.sql.Date(registro.getFechaServicio().getTime()));
            procedimiento.setString(2, registro.getTipoServicio());
            procedimiento.setString(3,registro.getHoraServicio());
            procedimiento.setString(4, registro.getLugarServicio());
            procedimiento.setString(5, registro.getTelefonoContacto());
            procedimiento.setInt(6, registro.getCodigoEmpresa());
            procedimiento.execute();
            listaServicio.add(registro);
            
        }catch(Exception e){
            e.printStackTrace();
        }
       
    }
    
    public void eliminar(){
        switch(tipoDeOperacion){
            case GUARDAD:
                desactivarcontroles();
                limpiarControles();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                tipoDeOperacion = operaciones.NINGUNO;                
                break; 
                
                default:
                    if(tblServicio.getSelectionModel().getSelectedItem() != null){
                        int respuesta = JOptionPane.showConfirmDialog(null,"¿Está seguro de eliminar el registro?","Eliminar Servicio",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
                        if(respuesta== JOptionPane.YES_OPTION){
                            try{
                                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarServicio(?)}");
                                procedimiento.setInt(1,((Servicio)tblServicio.getSelectionModel().getSelectedItem()).getCodigoServicio());
                                procedimiento.execute();
                                listaServicio.remove(tblServicio.getSelectionModel().getFocusedIndex());
                                limpiarControles();
                                tblServicio.getSelectionModel().clearSelection();
                            }catch(Exception e){
                                e.printStackTrace();
                            }
                        }
                    }else{
                        JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento");
                    }
                    break;
        }
    }
    
    public void editar(){
        switch(tipoDeOperacion){
            case NINGUNO:
                if(tblServicio.getSelectionModel().getSelectedItem() != null){
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    btnNuevo.setDisable(true);
                    btnEliminar.setDisable(true);
                    activarControles();
                    tipoDeOperacion = operaciones.ACTUALIZAR;
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
                tipoDeOperacion=operaciones.NINGUNO;
                cargarDatos();
                break;
        }
    }
    
    public void actualizar(){
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ActualizarServicio(?,?,?,?,?,?,?)}");
            Servicio registro = (Servicio)tblServicio.getSelectionModel().getSelectedItem();
            registro.setFechaServicio(fecha.getSelectedDate());
        registro.setTipoServicio(txtTipoServicio.getText());
        registro.setHoraServicio(txtHoraServicio.getText());
        registro.setLugarServicio(txtLugarServicio.getText());
        registro.setTelefonoContacto(txtTelefonoContacto.getText());
        registro.setCodigoEmpresa(((Empresa)cmbCodigoEmpresa.getSelectionModel().getSelectedItem()).getCodigoEmpresa());
        
        procedimiento.setInt(1, registro.getCodigoServicio());
        procedimiento.setDate(2, new java.sql.Date(registro.getFechaServicio().getTime()));
        procedimiento.setString(3, registro.getTipoServicio());
        procedimiento.setString(4,registro.getHoraServicio());
        procedimiento.setString(5, registro.getLugarServicio());
        procedimiento.setString(6, registro.getTelefonoContacto());
        procedimiento.setInt(7, registro.getCodigoEmpresa());
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
                desactivarcontroles();
                limpiarControles();
                tipoDeOperacion = operaciones.NINGUNO;
                break;
                
            case NINGUNO:
                if(tblServicio.getSelectionModel().getSelectedItem() != null){
                    imprimirReporte();
                }else {
                    JOptionPane.showMessageDialog(null,"Debe seleccionar un elemento");
                    tipoDeOperacion = operaciones.NINGUNO;
                }
                break;
        }
    }
    
    public void imprimirReporte(){
        Map parametro = new HashMap();
        int codServicio = Integer.valueOf(((Servicio)tblServicio.getSelectionModel().getSelectedItem()).getCodigoServicio());
        parametro.put("codServicio",codServicio);
        GenerarReporte.mostrarReporte("ReporteServicio.jasper", "Reporte Servicio", parametro);
    }
     
    public void desactivarcontroles(){
          txtCodigoServicio.setEditable(false);
          txtTipoServicio.setEditable(false);
          txtHoraServicio.setEditable(false);
          txtLugarServicio.setEditable(false);
          txtTelefonoContacto.setEditable(false);
          grpFechaServicio.setDisable(false);
          cmbCodigoEmpresa.setEditable(false);
         
    }
      
      
    public void activarControles(){
          txtCodigoServicio.setEditable(false);
          txtTipoServicio.setEditable(true);
          txtHoraServicio.setEditable(true);
          txtLugarServicio.setEditable(true);
          txtTelefonoContacto.setEditable(true);
          grpFechaServicio.setDisable(false);
          cmbCodigoEmpresa.setEditable(false);
    }
      
      
    public void limpiarControles(){
          txtCodigoServicio.setText("");
          txtTipoServicio.setText("");
          txtHoraServicio.setText("");
          txtLugarServicio.setText("");
          txtTelefonoContacto.setText("");
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
    
    private boolean Validar(TextField TServicio,TextField Hservicio,TextField LServicio,TextField TEServicio,ComboBox CEmpresa){
       boolean respuesta = false;
        
        if((fecha.getSelectedDate() != null) && !TServicio.getText().equals("") && !Hservicio.getText().equals("") 
                && !LServicio.getText().equals("") && !TEServicio.getText().equals("") && (CEmpresa.getSelectionModel().getSelectedItem() != null))
                respuesta = true;
        else
            JOptionPane.showMessageDialog(null,"Por favor llene todos los campos","ADVERTENCIA",JOptionPane.INFORMATION_MESSAGE);
        
        return respuesta;
    }
    
}
