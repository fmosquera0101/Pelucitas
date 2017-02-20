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
import co.com.fredymosqueralemus.pelucitas.direccion.Direccion;
import co.com.fredymosqueralemus.pelucitas.horario.Horario;
import co.com.fredymosqueralemus.pelucitas.modelo.minegocio.MiNegocio;

/**
 * Created by Fredy Mosquera Lemus on 16/02/17.
 */

public class AdapterMisNegocios extends ArrayAdapter<MiNegocio> {

    private Context context;
    private int idLayout;
    private List<MiNegocio> lstMisNegocios;

    public AdapterMisNegocios(Context context, int idLayout, List<MiNegocio> lstMisNegocios){
        super(context, idLayout, lstMisNegocios);
        this.context = context;
        this.idLayout = idLayout;
        this.lstMisNegocios = lstMisNegocios;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View view = convertView;
        ItemHolderlMisNegocios itemHolderlMisNegocios;
        if(null == view){
            LayoutInflater layoutInflater = ((Activity)context).getLayoutInflater();
            itemHolderlMisNegocios = new ItemHolderlMisNegocios();
            view = layoutInflater.inflate(idLayout, parent, false);
            itemHolderlMisNegocios.imageView = (ImageView)view.findViewById(R.id.imagennegocio_layout_listview_misnegocios);
            itemHolderlMisNegocios.txtNombreNegocio = (TextView)view.findViewById(R.id.nombrenegocio_layout_listview_misnegocios);
            itemHolderlMisNegocios.txtDireccionNegocio = (TextView)view.findViewById(R.id.direccionnegocio_layout_listview_misnegocios);
            itemHolderlMisNegocios.txtHorarioNegocio = (TextView)view.findViewById(R.id.horarionegocio_layout_listview_misnegocios);
            itemHolderlMisNegocios.txtTipoNegocio = (TextView)view.findViewById(R.id.tiponegocio_layout_listview_misnegocios);
            view.setTag(itemHolderlMisNegocios);
        }else{
            itemHolderlMisNegocios = (ItemHolderlMisNegocios) view.getTag();
        }
        MiNegocio miNegocio = lstMisNegocios.get(position);
        itemHolderlMisNegocios.txtNombreNegocio.setText(miNegocio.getNombreNegocio());
        itemHolderlMisNegocios.txtDireccionNegocio.setText(getDireccion(miNegocio.getDireccion()));
        itemHolderlMisNegocios.txtHorarioNegocio.setText(getHorario(miNegocio.getHorarioNegocio()));
        itemHolderlMisNegocios.txtTipoNegocio.setText(miNegocio.getTipoNegocio().getTipoNegocio());
        return  view;

    }
    private String getDireccion(Direccion direccion){
        StringBuilder strbDireccion = new StringBuilder();
        strbDireccion.append(direccion.getCarreraCalle());
        strbDireccion.append(", ");
        strbDireccion.append(direccion.getNumero1());
        strbDireccion.append(", ");
        strbDireccion.append(direccion.getNumero2());
        strbDireccion.append(", ");
        strbDireccion.append(direccion.getDatosAdicionales());
        return strbDireccion.toString();
    }
    private String getHorario(Horario horario){
        StringBuilder strbHorario = new StringBuilder();
        strbHorario.append(horario.getDiasLaborales());
        strbHorario.append(", ");
        strbHorario.append(horario.getHoraInicial());
        strbHorario.append("-");
        strbHorario.append(horario.getHoraFinal());
        return strbHorario.toString();
    }
    private class ItemHolderlMisNegocios{
        ImageView imageView;
        TextView txtNombreNegocio;
        TextView txtDireccionNegocio;
        TextView txtHorarioNegocio;
        TextView txtTipoNegocio;
    }
}
