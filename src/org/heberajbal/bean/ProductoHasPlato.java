
package org.heberajbal.bean;


public class ProductoHasPlato {
    private int codigoPlato;
    private int codigoProducto;
    
    public ProductoHasPlato(){
    
    }

    public ProductoHasPlato(int codigoPlato, int codigoProducto) {
        this.codigoPlato = codigoPlato;
        this.codigoProducto = codigoProducto;
    }

    public int getCodigoPlato() {
        return codigoPlato;
    }

    public void setCodigoPlato(int codigoPlato) {
        this.codigoPlato = codigoPlato;
    }

    public int getCodigoProducto() {
        return codigoProducto;
    }

    public void setCodigoProducto(int codigoProducto) {
        this.codigoProducto = codigoProducto;
    }
    
    
    
}
