package co.com.fredymosqueralemus.pelucitas.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.List;

import co.com.fredymosqueralemus.pelucitas.R;
import co.com.fredymosqueralemus.pelucitas.modelo.minegocio.TipoNegocio;



/**
 * Created by fredymosqueralemus on 4/7/2017
 */
public class AdapterTiposDeNegocios extends ArrayAdapter<TipoNegocio> {
    private Context context;
    private int layoutResourceId;
    private List<TipoNegocio> lstTiposDeNegocio;

    public AdapterTiposDeNegocios(Context context, List<TipoNegocio> lstTiposDeNegocio){
        super(context, R.layout.layout_inicio_tipos_de_negocios, lstTiposDeNegocio);
        this.context = context;
        this.layoutResourceId = R.layout.layout_inicio_tipos_de_negocios;
        this.lstTiposDeNegocio = lstTiposDeNegocio;
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

        TipoNegocio tiposDeNegocio = lstTiposDeNegocio.get(position);
        tipoEntidadesItemHolder.tvTipoEntidad.setText(tiposDeNegocio.getTipoNegocio());
        tipoEntidadesItemHolder.imgImagenTipoEntidad.setImageResource(tiposDeNegocio.getIdImagen());

        return view;
    }

    private class TipoEntidadesItemHolder{

        TextView tvTipoEntidad;
        ImageView imgImagenTipoEntidad;
    }
}
