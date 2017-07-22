package co.com.fredymosqueralemus.pelucitas.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.List;

import co.com.fredymosqueralemus.pelucitas.R;
import co.com.fredymosqueralemus.pelucitas.direccion.Direccion;
import co.com.fredymosqueralemus.pelucitas.horario.Horario;
import co.com.fredymosqueralemus.pelucitas.modelo.minegocio.MiNegocio;
import co.com.fredymosqueralemus.pelucitas.utilidades.Utilidades;
import co.com.fredymosqueralemus.pelucitas.utilidades.UtilidadesFirebaseBD;
import co.com.fredymosqueralemus.pelucitas.utilidades.UtilidadesImagenes;

/**
 * Created by Fredy Mosquera Lemus on 16/02/17.
 */

public class AdapterMisNegocios extends ArrayAdapter<MiNegocio> {

    private Context context;
    private int idLayout;
    private List<MiNegocio> lstMisNegocios;

    private StorageReference storageReference;

    public AdapterMisNegocios(Context context, int idLayout, List<MiNegocio> lstMisNegocios){
        super(context, idLayout, lstMisNegocios);
        this.context = context;
        this.idLayout = idLayout;
        this.lstMisNegocios = lstMisNegocios;
        storageReference = UtilidadesFirebaseBD.getFirebaseStorageFromUrl();

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View view = convertView;
        final ItemHolderlMisNegocios itemHolderlMisNegocios;
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
        DatabaseReference databaseReference;
        Direccion direccion = miNegocio.getDireccion();
        if(null == direccion) {
            databaseReference = FirebaseDatabase.getInstance().getReference(UtilidadesFirebaseBD.getUrlInserccionDireccionesXNegocio(miNegocio.getNitNegocio()));
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Direccion direccion  = dataSnapshot.getValue(Direccion.class);
                    itemHolderlMisNegocios.txtDireccionNegocio.setText(Utilidades.getStrDireccion(direccion) + ", " + direccion.getDatosAdicionales());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }else{
            itemHolderlMisNegocios.txtDireccionNegocio.setText(Utilidades.getStrDireccion(direccion) + ", " + direccion.getDatosAdicionales());
        }
        Horario horario = miNegocio.getHorarioNegocio();
        if(null == horario){
            databaseReference = FirebaseDatabase.getInstance().getReference(UtilidadesFirebaseBD.getUrlInsercionHorariosXNegocios(miNegocio.getNitNegocio()));
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Horario horario = dataSnapshot.getValue(Horario.class);
                    itemHolderlMisNegocios.txtHorarioNegocio.setText(Utilidades.getStrHorario(horario));
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }else{
            itemHolderlMisNegocios.txtHorarioNegocio.setText(Utilidades.getStrHorario(horario));
        }


        itemHolderlMisNegocios.txtTipoNegocio.setText(miNegocio.getTipoNegocio().getTipoNegocio());
        UtilidadesImagenes.cargarImagenMiNegocioCircular(itemHolderlMisNegocios.imageView, miNegocio, context, storageReference);

        return  view;

    }


    private class ItemHolderlMisNegocios{
        ImageView imageView;
        TextView txtNombreNegocio;
        TextView txtDireccionNegocio;
        TextView txtHorarioNegocio;
        TextView txtTipoNegocio;
    }

}
