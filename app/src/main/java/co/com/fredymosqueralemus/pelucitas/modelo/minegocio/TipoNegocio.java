package co.com.fredymosqueralemus.pelucitas.modelo.minegocio;

/**
 * Esta clase representa los tipos de negocios (peluqueria, barberia, salon de belleza)
 * Created by fredymosqueralemus on 2/02/17.
 */
public class TipoNegocio {
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
