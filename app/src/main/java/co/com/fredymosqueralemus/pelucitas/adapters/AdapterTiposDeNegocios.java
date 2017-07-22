package co.com.fredymosqueralemus.pelucitas.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.google.firebase.storage.StorageReference;

import java.util.List;

import co.com.fredymosqueralemus.pelucitas.R;
import co.com.fredymosqueralemus.pelucitas.modelo.settings.TiposDeNegocio;
import co.com.fredymosqueralemus.pelucitas.utilidades.UtilidadesFirebaseBD;
import co.com.fredymosqueralemus.pelucitas.utilidades.UtilidadesImagenes;


/**
 * Created by fredymosqueralemus on 4/7/2017
 */
public class AdapterTiposDeNegocios extends ArrayAdapter<TiposDeNegocio> {
    private Context context;
    private int layoutResourceId;
    private List<TiposDeNegocio> lstTiposDeNegocio;
    private StorageReference storageReference;

    public AdapterTiposDeNegocios(Context context, List<TiposDeNegocio> lstTiposDeNegocio){
        super(context, R.layout.layout_inicio_tipos_de_negocios, lstTiposDeNegocio);
        this.context = context;
        this.layoutResourceId = R.layout.layout_inicio_tipos_de_negocios;
        this.lstTiposDeNegocio = lstTiposDeNegocio;
        storageReference = UtilidadesFirebaseBD.getFirebaseStorageFromUrl();
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        TipoEntidadesItemHolder tipoEntidadesItemHolder;
        if (null == view) {
            LayoutInflater layoutInflater = ((Activity) context).getLayoutInflater();
            view = layoutInflater.inflate(layoutResourceId, parent, false);
            tipoEntidadesItemHolder = new TipoEntidadesItemHolder();
            tipoEntidadesItemHolder.tvTipoEntidad = (TextView) view.findViewById(R.id.tipo_entidad_layout_inicio);
            tipoEntidadesItemHolder.imgImagenTipoEntidad = (ImageView) view.findViewById(R.id.miniatura_tipo_entidad_layout_inicio);

            view.setTag(tipoEntidadesItemHolder);
        }else{
            tipoEntidadesItemHolder = (TipoEntidadesItemHolder) view.getTag();
        }

        TiposDeNegocio tiposDeNegocio  = lstTiposDeNegocio.get(position);
        tipoEntidadesItemHolder.tvTipoEntidad.setText(tiposDeNegocio.getNombreTipoNegocio());
        tipoEntidadesItemHolder.imgImagenTipoEntidad.setImageResource(tiposDeNegocio.getIdImagen());
        UtilidadesImagenes.cargarImagenTiposNegocios(tipoEntidadesItemHolder.imgImagenTipoEntidad, tiposDeNegocio, context, storageReference);
        return view;
    }

    private class TipoEntidadesItemHolder{

        TextView tvTipoEntidad;
        ImageView imgImagenTipoEntidad;
    }
}
