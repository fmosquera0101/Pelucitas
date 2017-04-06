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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;

import java.io.File;

import co.com.fredymosqueralemus.pelucitas.constantes.Constantes;
import co.com.fredymosqueralemus.pelucitas.imagenes.ImagenModelo;
import co.com.fredymosqueralemus.pelucitas.modelo.minegocio.MiNegocio;
import co.com.fredymosqueralemus.pelucitas.modelo.usuario.Usuario;

/**
 * Created by Fredy Mosquera Lemus on 22/02/17.
 */

public class UtilidadesImagenes {

    public static void cargarImagenMiNegocio(final ImageView imageView, MiNegocio miNegocio, final Context context, StorageReference storageReference) {
        final StorageReference storageReferenceImagenes = UtilidadesFirebaseBD.getReferenceImagenMiNegocio(storageReference, miNegocio);
        ImagenModelo imagenModelo = miNegocio.getImagenModelo();
        if(null != imagenModelo) {
            cargarImagenConGlide(context, storageReferenceImagenes, imagenModelo, imageView);
        }

    }

    public static void cargarImagenPerfilUsuario(ImageView imageView, Usuario usuario, Context context, StorageReference storageReference) {
        final StorageReference storageReferenceImagenes = UtilidadesFirebaseBD.getReferenceImagenMiPerfil(storageReference, usuario);
        ImagenModelo imagenModelo = usuario.getImagenModelo();
        if(null != imagenModelo) {
            cargarImagenConGlide(context, storageReferenceImagenes, imagenModelo, imageView);
        }

    }
    private static void cargarImagenConGlide(final Context context, StorageReference storageReferenceImagenes, ImagenModelo imagenModelo, final ImageView imageView){
        Glide.with(context).using(new FirebaseImageLoader()).load(storageReferenceImagenes).asBitmap().
                centerCrop().diskCacheStrategy(DiskCacheStrategy.RESULT).signature(new StringSignature(String.valueOf(imagenModelo.getFechaUltimaModificacion()))).
                into(new BitmapImageViewTarget(imageView) {
                    @Override
                    public void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularImage = RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                        circularImage.setCircular(true);
                        imageView.setImageDrawable(circularImage);
                    }
                });
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
