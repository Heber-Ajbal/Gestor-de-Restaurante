
package org.heberajbal.bean;


public class ServicioHasPlato {
    
    private int codigoServicio;
    private int codigoPlato;
    
    public ServicioHasPlato(){
        
    }

    public ServicioHasPlato(int codigoServicio, int codigoPlato) {
        this.codigoServicio = codigoServicio;
        this.codigoPlato = codigoPlato;
    }

    public int getCodigoServicio() {
        return codigoServicio;
    }

    public void setCodigoServicio(int codigoServicio) {
        this.codigoServicio = codigoServicio;
    }

    public int getCodigoPlato() {
        return codigoPlato;
    }

    public void setCodigoPlato(int codigoPlato) {
        this.codigoPlato = codigoPlato;
    }
    
    
}
