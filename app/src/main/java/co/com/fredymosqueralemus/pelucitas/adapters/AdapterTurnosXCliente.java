package co.com.fredymosqueralemus.pelucitas.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.storage.StorageReference;

import java.util.List;

import co.com.fredymosqueralemus.pelucitas.R;
import co.com.fredymosqueralemus.pelucitas.modelo.agenda.TurnosXCliente;
import co.com.fredymosqueralemus.pelucitas.utilidades.UtilidadesFirebaseBD;

/**
 * Created by fredymosqueralemus on 29/06/17.
 */

public class AdapterTurnosXCliente extends ArrayAdapter<TurnosXCliente> {

    private Context context;
    private int idLayout;
    private List<TurnosXCliente> lstTurnosXCliente;
    private ItemHolderTurnosXCliente itemHolderAgendaXEmpleado;
    private StorageReference storageReference;

    public AdapterTurnosXCliente(Context context, int idLayout, List<TurnosXCliente> lstTurnosXCliente) {
        super(context, idLayout, lstTurnosXCliente);
        this.context = context;
        this.idLayout = idLayout;
        this.lstTurnosXCliente = lstTurnosXCliente;
        storageReference = UtilidadesFirebaseBD.getFirebaseStorageFromUrl();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable final View convertView, @NonNull ViewGroup parent) {
        View view = convertView;


        if (null == view) {
            LayoutInflater layoutInflater = ((Activity) context).getLayoutInflater();
            view = layoutInflater.inflate(idLayout, parent, false);
            itemHolderAgendaXEmpleado = new ItemHolderTurnosXCliente();
            itemHolderAgendaXEmpleado.imgvImagenEmpleado = (ImageView) view.findViewById(R.id.imagen_empleado_layout_turnosxcliente);
            itemHolderAgendaXEmpleado.txtvNombreNegocio = (TextView) view.findViewById(R.id.nombre_negocio_layout_turnosxcliente);
            itemHolderAgendaXEmpleado.txtvNombreEmpleado = (TextView) view.findViewById(R.id.nombre_empleado_layout_listview_misnegocios);
            itemHolderAgendaXEmpleado.txtvTelefonoEmpleado = (TextView) view.findViewById(R.id.telefono_empleado_layout_listview_misnegocios);
            itemHolderAgendaXEmpleado.txtvDirecionNegocio = (TextView) view.findViewById(R.id.direccion_negocio_layout_turnosxcliente);
            view.setTag(itemHolderAgendaXEmpleado);
        } else {
            itemHolderAgendaXEmpleado = (ItemHolderTurnosXCliente) view.getTag();
        }
        TurnosXCliente turnosXCliente = lstTurnosXCliente.get(position);
       // UtilidadesImagenes.cargarImagenPerfilUsuarioCircular(itemHolderMisEmpleados.imageView, imagenModelo.getFechaUltimaModificacion(), empleado.getCedulaIdentificacion(), context, storageReference);

        itemHolderAgendaXEmpleado.txtvNombreNegocio.setText(turnosXCliente.getNombreNegocio());
        itemHolderAgendaXEmpleado.txtvNombreEmpleado.setText(turnosXCliente.getNombreEmpleado());
        itemHolderAgendaXEmpleado.txtvTelefonoEmpleado.setText(turnosXCliente.getTelefonoNegocioEmpleado());
        itemHolderAgendaXEmpleado.txtvDirecionNegocio.setText(turnosXCliente.getDireccionNegocio());

        return view;
    }

    private class ItemHolderTurnosXCliente {
        ImageView imgvImagenEmpleado;
        TextView txtvNombreNegocio;
        TextView txtvNombreEmpleado;
        TextView txtvTelefonoEmpleado;
        TextView txtvDirecionNegocio;
    }
}
