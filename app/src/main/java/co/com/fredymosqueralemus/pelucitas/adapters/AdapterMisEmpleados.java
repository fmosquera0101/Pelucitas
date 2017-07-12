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
import co.com.fredymosqueralemus.pelucitas.modelo.usuario.Usuario;
import co.com.fredymosqueralemus.pelucitas.utilidades.UtilidadesFirebaseBD;
import co.com.fredymosqueralemus.pelucitas.utilidades.UtilidadesImagenes;

/**
 * Created by fredymosqueralemus on 30/05/17.
 */

public class AdapterMisEmpleados extends ArrayAdapter<Usuario> {

    private Context context;
    private int idLayout;
    private List<Usuario> listEmpleados;
    private StorageReference storageReference;

    public AdapterMisEmpleados(Context context, int idLayout, List<Usuario> listEmpleados){
        super(context, idLayout, listEmpleados);
        this.context = context;
        this.idLayout = idLayout;
        this.listEmpleados = listEmpleados;

        storageReference = UtilidadesFirebaseBD.getFirebaseStorageFromUrl();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        View view = convertView;
        ItemHolderMisEmpleados itemHolderMisEmpleados;
        if(null == view){
            LayoutInflater layoutInflater = ((Activity)context).getLayoutInflater();
            itemHolderMisEmpleados = new ItemHolderMisEmpleados();
            view = layoutInflater.inflate(idLayout, parent, false);
            itemHolderMisEmpleados.imageView = (ImageView) view.findViewById(R.id.imagen_miempleado_layout_listview_misnegocios);
            itemHolderMisEmpleados.txtvNombreEmpleado = (TextView) view.findViewById(R.id.nombre_empleado_layout_listview_misnegocios);
            itemHolderMisEmpleados.txtvTelefono = (TextView) view.findViewById(R.id.telefono_empleado_layout_listview_misnegocios);
            itemHolderMisEmpleados.txtvEmail = (TextView) view.findViewById(R.id.email_empleado_layout_listview_misnegocios);

            view.setTag(itemHolderMisEmpleados);
        }else{
            itemHolderMisEmpleados = (ItemHolderMisEmpleados) view.getTag();
        }
        Usuario empleado = listEmpleados.get(position);

        itemHolderMisEmpleados.txtvNombreEmpleado.setText(empleado.getNombre());
        itemHolderMisEmpleados.txtvTelefono.setText(empleado.getTelefono());
        itemHolderMisEmpleados.txtvEmail.setText(empleado.getEmail());
        UtilidadesImagenes.cargarImagenPerfilUsuarioCircular(itemHolderMisEmpleados.imageView, empleado, context, storageReference);
        return  view;
    }

    private class ItemHolderMisEmpleados{
        ImageView imageView;
        TextView txtvNombreEmpleado;
        TextView txtvTelefono;
        TextView txtvEmail;
    }
}
