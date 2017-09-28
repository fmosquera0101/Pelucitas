package co.com.fredymosqueralemus.pelucitas.utilidades;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.signature.StringSignature;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.StorageReference;

import java.io.File;

import co.com.fredymosqueralemus.pelucitas.constantes.Constantes;
import co.com.fredymosqueralemus.pelucitas.imagenes.ImagenModelo;
import co.com.fredymosqueralemus.pelucitas.modelo.minegocio.MiNegocio;
import co.com.fredymosqueralemus.pelucitas.modelo.settings.TiposDeNegocio;
import co.com.fredymosqueralemus.pelucitas.modelo.usuario.Usuario;

/**
 * Created by Fredy Mosquera Lemus on 22/02/17.
 */

public class UtilidadesImagenes {

    public static void cargarImagenMiNegocioNoCircular(final ImageView imageView, MiNegocio miNegocio, final Context context, StorageReference storageReference) {
        StorageReference storageReferenceImagenes = UtilidadesFirebaseBD.getReferenceImagenMiNegocio(storageReference, miNegocio);
        ImagenModelo imagenModelo = miNegocio.getImagenModelo();
        if(null != imagenModelo) {
            cargarImagenConGlideNoCircular(context, storageReferenceImagenes, imagenModelo.getFechaUltimaModificacion(), imageView);
        }

    }

    public static void cargarImagenMiNegocioCircular(final ImageView imageView, MiNegocio miNegocio, final Context context, StorageReference storageReference) {
        StorageReference storageReferenceImagenes = UtilidadesFirebaseBD.getReferenceImagenMiNegocio(storageReference, miNegocio);
        ImagenModelo imagenModelo = miNegocio.getImagenModelo();
        if(null != imagenModelo) {
            cargarImagenConGlideCircular(context, storageReferenceImagenes, imagenModelo.getFechaUltimaModificacion(), imageView);
        }

    }

    public static void cargarImagenPerfilUsuario(ImageView imageView, String fechaUltimaModificacion, String cedulaIdentificacion, Context context, StorageReference storageReference) {
        StorageReference storageReferenceImagenes = UtilidadesFirebaseBD.getReferenceImagenMiPerfil(storageReference, cedulaIdentificacion);
        cargarImagenConGlideNoCircular(context, storageReferenceImagenes, fechaUltimaModificacion, imageView);


    }

    public static void cargarImagenPerfilUsuarioCircular(ImageView imageView, String fechaActualizacionImagen,String cedulaIdentificacion, Context context, StorageReference storageReference) {
         StorageReference storageReferenceImagenes = UtilidadesFirebaseBD.getReferenceImagenMiPerfil(storageReference, cedulaIdentificacion);


            cargarImagenConGlideCircular(context, storageReferenceImagenes, fechaActualizacionImagen, imageView);


    }
    public static void cargarImagenTiposNegocios(ImageView imageView, TiposDeNegocio tiposDeNegocio, Context context, StorageReference storageReference){
         StorageReference storageReferenceImagenes = UtilidadesFirebaseBD.getReferenceImagenTiposNegocios(storageReference, tiposDeNegocio);
        ImagenModelo imagenModelo = tiposDeNegocio.getImagenModelo();
        if(null != imagenModelo) {
            cargarImagenConGlideNoCircular(context, storageReferenceImagenes, imagenModelo.getFechaUltimaModificacion(), imageView);
        }
    }
    private static void cargarImagenConGlideCircular(final Context context, StorageReference storageReferenceImagenes, String fechaActualizacionImagen, final ImageView imageView){
        Glide.with(context).using(new FirebaseImageLoader()).load(storageReferenceImagenes).asBitmap().
                centerCrop().diskCacheStrategy(DiskCacheStrategy.RESULT).signature(new StringSignature(String.valueOf(fechaActualizacionImagen))).
                into(new BitmapImageViewTarget(imageView) {
                    @Override
                    public void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularImage = RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                        circularImage.setCircular(true);
                        imageView.setImageDrawable(circularImage);
                    }
                });
    }

    private static void cargarImagenConGlideNoCircular(final Context context, StorageReference storageReferenceImagenes, String fechaUltimaModificacion, final ImageView imageView){
        Glide.with(context).using(new FirebaseImageLoader()).load(storageReferenceImagenes).asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.RESULT).signature(new StringSignature(String.valueOf(fechaUltimaModificacion))).
                into(imageView);
    }


    public static File getFileImagenMiNegocio(MiNegocio miNegocio){
        return new File(getPathImagenMiNegoico(miNegocio));
    }
    private static String getPathImagenMiNegoico(MiNegocio miNegocio){
        StringBuilder strbPathFileImage = new StringBuilder();
        strbPathFileImage.append(Environment.getExternalStorageDirectory());
        strbPathFileImage.append("/Pelucitas/images/");
        strbPathFileImage.append("Minegocio");
        strbPathFileImage.append(miNegocio.getNitNegocio());
        strbPathFileImage.append(".jpg");
        return strbPathFileImage.toString();
    }

    public static File getFileImagenPerfilUsuario(Usuario usuario){
        return new File(getPathImagenUsuarioPerfil(usuario));
    }
    private static String getPathImagenUsuarioPerfil(Usuario usuario){
        StringBuilder strbPathFileImage = new StringBuilder();
        strbPathFileImage.append(Environment.getExternalStorageDirectory());
        strbPathFileImage.append(Constantes.APP_FOLDER);
        strbPathFileImage.append("usuario");
        strbPathFileImage.append(usuario.getCedulaIdentificacion());
        strbPathFileImage.append(".jpg");
        return strbPathFileImage.toString();
    }
}
