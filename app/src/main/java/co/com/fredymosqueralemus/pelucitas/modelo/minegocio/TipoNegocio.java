package co.com.fredymosqueralemus.pelucitas.modelo.minegocio;

import java.io.Serializable;

/**
 * Esta clase representa los tipos de negocios (peluqueria, barberia, salon de belleza)
 * Created by Fredy Mosquera Lemus on 2/02/17.
 */
public class TipoNegocio implements Serializable{
    private String tipoNegocio;
    private int imagenTipoNegocio;

    public TipoNegocio(){}


    public String getTipoNegocio() {
        return tipoNegocio;
    }

    public void setTipoNegocio(String tipoNegocio) {
        this.tipoNegocio = tipoNegocio;
    }

    public int getImagenTipoNegocio() {
        return imagenTipoNegocio;
    }

    public void setImagenTipoNegocio(int imagenTipoNegocio) {
        this.imagenTipoNegocio = imagenTipoNegocio;
    }
}
