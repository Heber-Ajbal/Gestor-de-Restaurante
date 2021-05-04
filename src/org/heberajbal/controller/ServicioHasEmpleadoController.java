
package org.heberajbal.controller;


import eu.schudt.javafx.controls.calendar.DatePicker;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
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
import org.heberajbal.bean.Empleado;
import org.heberajbal.bean.Servicio;
import org.heberajbal.bean.ServicioHasEmpleado;
import org.heberajbal.db.Conexion;
import org.heberajbal.system.Principal;


public class ServicioHasEmpleadoController implements Initializable{

    

    private enum operacion{NUEVO,ELIMINAR,EDITAR,ACTUALIZAR,NINGUNO,GUARDAR,REPORTE};
    private Principal escenarioPrincipal;
    private ObservableList<ServicioHasEmpleado> listaServicioHasEmpleado;
    private ObservableList<Servicio> listaServicio;
    private ObservableList<Empleado> listaEmpleados;
    private operacion tipoDeOperaciones = operacion.NINGUNO;
    private DatePicker fecha;
    
    @FXML private ComboBox cmbCodigoServicio;
    @FXML private ComboBox cmbCodigoEmpleado;
    @FXML private TextField txtHoraEvento;
    @FXML private TextField txtLugarEvento;
    @FXML private GridPane grpFechaEvento;
    @FXML private TableView tblServicioHasEmpleado;
    @FXML private TableColumn colCodigoServicio;
    @FXML private TableColumn colCodigoEmpleado;
    @FXML private TableColumn colHoraEvento;
    @FXML private TableColumn colLugarEvento;
    @FXML private TableColumn colFechaEvento;
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
     grpFechaEvento.add(fecha,1,1);
     fecha.getStylesheets().add("/org/heberajbal/resource/DatePicker.css");
     cmbCodigoEmpleado.setItems(getEmpleados());
     cmbCodigoServicio.setItems(getServicio());
    }
    
    public void cargarDatos(){
        tblServicioHasEmpleado.setItems(getServicioHasEmpleados());
        colCodigoServicio.setCellValueFactory(new PropertyValueFactory<ServicioHasEmpleado,Integer>("codigoServicio"));
        colCodigoEmpleado.setCellValueFactory(new PropertyValueFactory<ServicioHasEmpleado,Integer>("codigoEmpleado"));
        colFechaEvento.setCellValueFactory(new PropertyValueFactory<ServicioHasEmpleado, Date>("fechaEvento"));
        colHoraEvento.setCellValueFactory(new PropertyValueFactory<ServicioHasEmpleado,String>("horaEvento"));
        colLugarEvento.setCellValueFactory(new PropertyValueFactory<ServicioHasEmpleado,String>("lugarEvento"));
    }
    
    public ObservableList<ServicioHasEmpleado> getServicioHasEmpleados(){
        ArrayList<ServicioHasEmpleado> lista = new ArrayList<ServicioHasEmpleado>();
        
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarServicio_has_Empleados}");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                lista.add(new ServicioHasEmpleado(resultado.getInt("codigoServicio"),
                                                  resultado.getInt("codigoEmpleado"),
                                                  resultado.getDate("fechaEvento"),
                                                  resultado.getString("horaEvento"),
                                                  resultado.getString("lugarEvento")));
                
            }
            
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaServicioHasEmpleado = FXCollections.observableArrayList(lista);
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
    
    public Empleado buscarEmpleado(int codigoEmpleado){
        Empleado resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarEmpleados(?)}");
            procedimiento.setInt(1, codigoEmpleado);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado= new Empleado(
                                        registro.getInt("codigoEmpleado"),
                                        registro.getInt("numeroEmpleado"),
                                        registro.getString("nombresEmpleado"),
                                        registro.getString("apellidosEmpleado"),
                                        registro.getString("direccionEmpleado"),
                                        registro.getString("TelefonoContacto"),
                                        registro.getString("gradoCocinero"),
                                        registro.getInt("codigoTipoEmpleado"));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return resultado;
    }
    public void nuevo(){
        switch(tipoDeOperaciones){
            case NINGUNO:
                activarControles();
                btnNuevo.setText("Guardar");
                btnEliminar.setText("Cancelar");
                btnEditar.setDisable(true);
                btnReporte.setDisable(true);
                tipoDeOperaciones = operacion.GUARDAR;
                limpiarControles();
                break;
            
            case GUARDAR:
                if(Validar(cmbCodigoServicio, cmbCodigoEmpleado, txtHoraEvento, txtLugarEvento)){
                guardar();
                desactivarControles();
                limpiarControles();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                tipoDeOperaciones = operacion.NINGUNO;
                cargarDatos();
                }
                break;
                
        }
        
    }
    
    public void guardar(){
        ServicioHasEmpleado registro = new ServicioHasEmpleado();
        registro.setCodigoEmpleado(((Empleado)cmbCodigoEmpleado.getSelectionModel().getSelectedItem()).getCodigoEmpleado());
        registro.setCodigoServicio(((Servicio)cmbCodigoServicio.getSelectionModel().getSelectedItem()).getCodigoServicio());
        registro.setFechaEvento(fecha.getSelectedDate());
        registro.setHoraEvento(txtHoraEvento.getText());
        registro.setLugarEvento(txtLugarEvento.getText());
        
        
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarServicio_has_Empleados(?,?,?,?,?)}");
            procedimiento.setInt(1, registro.getCodigoServicio());
            procedimiento.setInt(2,registro.getCodigoEmpleado());            
            procedimiento.setDate(3, new java.sql.Date(registro.getFechaEvento().getTime()));
            procedimiento.setString(4,registro.getHoraEvento());
            procedimiento.setString(5, registro.getLugarEvento());
            procedimiento.execute();
            listaServicioHasEmpleado.add(registro);
           
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void seleccionarElementos(){
        if(tblServicioHasEmpleado.getSelectionModel().getSelectedItem() != null){
            txtHoraEvento.setText(((ServicioHasEmpleado)tblServicioHasEmpleado.getSelectionModel().getSelectedItem()).getHoraEvento());
            txtLugarEvento.setText(((ServicioHasEmpleado)tblServicioHasEmpleado.getSelectionModel().getSelectedItem()).getLugarEvento());
            fecha.selectedDateProperty().set(((ServicioHasEmpleado)tblServicioHasEmpleado.getSelectionModel().getSelectedItem()).getFechaEvento());
            cmbCodigoEmpleado.getSelectionModel().select(buscarEmpleado(((ServicioHasEmpleado)tblServicioHasEmpleado.getSelectionModel().getSelectedItem()).getCodigoEmpleado()));
            cmbCodigoServicio.getSelectionModel().select(buscarServicio(((ServicioHasEmpleado)tblServicioHasEmpleado.getSelectionModel().getSelectedItem()).getCodigoServicio()));
            
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
                tipoDeOperaciones= operacion.NINGUNO;
                break;
                
            default:
                if(tblServicioHasEmpleado.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null,"¿Está seguro de eliminar el registro?","Eliminar Servicio has Empleado",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if(respuesta== JOptionPane.YES_OPTION){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarServicio_has_Empleados(?,?)}");
                            procedimiento.setInt(1,((ServicioHasEmpleado)tblServicioHasEmpleado.getSelectionModel().getSelectedItem()).getCodigoServicio());
                            procedimiento.setInt(2,((ServicioHasEmpleado)tblServicioHasEmpleado.getSelectionModel().getSelectedItem()).getCodigoEmpleado());
                            procedimiento.execute();
                            listaServicioHasEmpleado.remove(tblServicioHasEmpleado.getSelectionModel().getFocusedIndex());
                            tblServicioHasEmpleado.getSelectionModel().clearSelection();
                            limpiarControles();
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }else{
                        JOptionPane.showMessageDialog(null,"Debe seleccionar un registro");
                    }
                    break;
                }
                
                
        }
    }
    
    public void Editar(){
        switch(tipoDeOperaciones){
            case NINGUNO:
                if(tblServicioHasEmpleado.getSelectionModel().getSelectedItem() !=null){
                   activarControles();
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    btnNuevo.setDisable(true);
                    btnEliminar.setDisable(true);
                    tipoDeOperaciones = operacion.ACTUALIZAR;
                    
                }else{
                    JOptionPane.showMessageDialog(null," Debe seleccionar un registro");
                }
                break;
                
            case ACTUALIZAR:
                actualizar();
                desactivarControles();
                btnEditar.setText("Editar");
                btnReporte.setText("Reporte");
                btnNuevo.setDisable(false);
                btnEliminar.setDisable(false);
                tipoDeOperaciones=operacion.NINGUNO;
                cargarDatos();
                
                break;
        }
        
    }
    public void actualizar(){
    
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ActualizarServicio_has_Empleados(?,?,?,?,?)}");
            ServicioHasEmpleado registro = (ServicioHasEmpleado)tblServicioHasEmpleado.getSelectionModel().getSelectedItem();
            registro.setCodigoEmpleado(((Empleado)cmbCodigoEmpleado.getSelectionModel().getSelectedItem()).getCodigoEmpleado());
            registro.setCodigoServicio(((Servicio)cmbCodigoServicio.getSelectionModel().getSelectedItem()).getCodigoServicio());
            registro.setFechaEvento(fecha.getSelectedDate());
            registro.setHoraEvento(txtHoraEvento.getText());
            registro.setLugarEvento(txtLugarEvento.getText());
            
            
            procedimiento.setInt(1, registro.getCodigoServicio());
            procedimiento.setInt(2,registro.getCodigoEmpleado());            
            procedimiento.setDate(3, new java.sql.Date(registro.getFechaEvento().getTime()));
            procedimiento.setString(4,registro.getHoraEvento());
            procedimiento.setString(5, registro.getLugarEvento());
            procedimiento.execute();
                       
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void desactivarControles(){
        txtHoraEvento.setEditable(false);
        txtLugarEvento.setEditable(false);
        cmbCodigoEmpleado.setEditable(false);
        cmbCodigoServicio.setEditable(false);
        grpFechaEvento.setDisable(false);
    }
    
    public void activarControles(){
        txtHoraEvento.setEditable(true);
        txtLugarEvento.setEditable(true);
        cmbCodigoEmpleado.setEditable(false);
        cmbCodigoServicio.setEditable(false);
        grpFechaEvento.setDisable(false);        
    }
    
    public void limpiarControles(){
        txtHoraEvento.setText("");
        txtLugarEvento.setText("");
        cmbCodigoEmpleado.getSelectionModel().clearSelection();
        cmbCodigoServicio.getSelectionModel().clearSelection();
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
   
    private boolean Validar(ComboBox CServicio, ComboBox CEmpleado,TextField hora,TextField lugar){
        boolean respuesta = false;
        
        if((CServicio.getSelectionModel().getSelectedItem() != null) && (CEmpleado.getSelectionModel().getSelectedItem() != null)
                && (fecha.getSelectedDate() != null) && !hora.getText().equals("") && !lugar.getText().equals(""))
            respuesta = true;
            
        else 
            JOptionPane.showMessageDialog(null,"Por favor llene todos los campos","ADVERTENCIA",JOptionPane.INFORMATION_MESSAGE);
        return respuesta;
    }
    
    
    
}
