package co.com.fredymosqueralemus.pelucitas.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.signature.StringSignature;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.List;

import co.com.fredymosqueralemus.pelucitas.R;
import co.com.fredymosqueralemus.pelucitas.constantes.Constantes;
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
        StorageReference storageReferenceImagenes = UtilidadesFirebaseBD.getReferenceImagenMiNegocio(storageReference, miNegocio);

        itemHolderlMisNegocios.txtNombreNegocio.setText(miNegocio.getNombreNegocio());
        itemHolderlMisNegocios.txtDireccionNegocio.setText(Utilidades.getStrDireccion(miNegocio.getDireccion())+", "+miNegocio.getDireccion().getDatosAdicionales());
        itemHolderlMisNegocios.txtHorarioNegocio.setText(Utilidades.getStrHorario(miNegocio.getHorarioNegocio()));
        itemHolderlMisNegocios.txtTipoNegocio.setText(miNegocio.getTipoNegocio().getTipoNegocio());


        Glide.with(context).using(new FirebaseImageLoader()).load(storageReferenceImagenes).asBitmap().
                centerCrop().diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).
                into(new BitmapImageViewTarget(itemHolderlMisNegocios.imageView){
            @Override
            public void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularImage = RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                circularImage.setCircular(true);
                itemHolderlMisNegocios.imageView.setImageDrawable(circularImage);
            }
        });

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
