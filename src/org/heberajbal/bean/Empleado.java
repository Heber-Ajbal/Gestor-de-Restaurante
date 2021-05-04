
package org.heberajbal.bean;


public class Empleado {
    private int codigoEmpleado;
    private int numeroEmpleado;
    private String nombresEmpleado;
    private String apellidosEmpleado;
    private String direccionEmpleado;
    private String TelefonoContacto;
    private String gradoCocinero;
    private int codigoTipoEmpleado;
    
    public Empleado(){
       
    }

    public Empleado(int codigoEmpleado, int numeroEmpleado, String nombresEmpleado, String apellidosEmpleado, String direccionEmpleado, String telefonoContacto, String gradoCocinero, int codigoTipoEmpleado) {
        this.codigoEmpleado = codigoEmpleado;
        this.numeroEmpleado = numeroEmpleado;
        this.nombresEmpleado = nombresEmpleado;
        this.apellidosEmpleado = apellidosEmpleado;
        this.direccionEmpleado = direccionEmpleado;
        this.TelefonoContacto = telefonoContacto;
        this.gradoCocinero = gradoCocinero;
        this.codigoTipoEmpleado = codigoTipoEmpleado;
    }

   

    public int getCodigoEmpleado() {
        return codigoEmpleado;
    }

    public void setCodigoEmpleado(int codigoEmpleado) {
        this.codigoEmpleado = codigoEmpleado;
    }

    public int getNumeroEmpleado() {
        return numeroEmpleado;
    }

    public void setNumeroEmpleado(int numeroEmpleado) {
        this.numeroEmpleado = numeroEmpleado;
    }

    public String getNombresEmpleado() {
        return nombresEmpleado;
    }

    public void setNombresEmpleado(String nombresEmpleado) {
        this.nombresEmpleado = nombresEmpleado;
    }

    public String getApellidosEmpleado() {
        return apellidosEmpleado;
    }

    public void setApellidosEmpleado(String apellidosEmpleado) {
        this.apellidosEmpleado = apellidosEmpleado;
    }

    public String getDireccionEmpleado() {
        return direccionEmpleado;
    }

    public void setDireccionEmpleado(String direccionEmpleado) {
        this.direccionEmpleado = direccionEmpleado;
    }

    public String getTelefonoContacto() {
        return TelefonoContacto;
    }

    public void setTelefonoContacto(String telefonoContacto) {
        this.TelefonoContacto = telefonoContacto;
    }

    public String getGradoCocinero() {
        return gradoCocinero;
    }

    public void setGradoCocinero(String gradoCocinero) {
        this.gradoCocinero = gradoCocinero;
    }

    public int getCodigoTipoEmpleado() {
        return codigoTipoEmpleado;
    }

    public void setCodigoTipoEmpleado(int codigoTipoEmpleado) {
        this.codigoTipoEmpleado = codigoTipoEmpleado;
    }
    
     public String toString(){
       return getCodigoEmpleado()+ " | " + getNombresEmpleado();
   }
    
    
    
}
