package co.com.fredymosqueralemus.pelucitas;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.signature.MediaStoreSignature;
import com.bumptech.glide.signature.StringSignature;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import co.com.fredymosqueralemus.pelucitas.constantes.Constantes;
import co.com.fredymosqueralemus.pelucitas.direccion.Direccion;
import co.com.fredymosqueralemus.pelucitas.horario.Horario;
import co.com.fredymosqueralemus.pelucitas.modelo.minegocio.MiNegocio;
import co.com.fredymosqueralemus.pelucitas.sharedpreference.SharedPreferencesSeguro;
import co.com.fredymosqueralemus.pelucitas.sharedpreference.SharedPreferencesSeguroSingleton;
import co.com.fredymosqueralemus.pelucitas.utilidades.Utilidades;
import co.com.fredymosqueralemus.pelucitas.utilidades.UtilidadesFirebaseBD;
import co.com.fredymosqueralemus.pelucitas.utilidades.UtilidadesImagenes;

public class AdministrarMiNegocioActivity extends AppCompatActivity {

    private ImageView imgvImagenNegocio;

    private TextView tvNitNegocio;
    private TextView tvNombre;
    private TextView tvTelefono;
    private TextView tvTipoNegocio;

    private TextView tvDireccion;
    private TextView tvBarrio;
    private TextView tvDatosAdicionales;

    private TextView tvHorariolaboral;

    private Intent intent;
    private MiNegocio miNegocio;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private SharedPreferencesSeguro sharedPreferencesSeguro;
    private int REQUEST_CAMERA = 0;
    private int SELECT_FILE = 1;

    private StorageReference storageReference;
    private String userChoose;

    private FirebaseAuth mAuth;

    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administrar_mi_negocio);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        intent = getIntent();
        firebaseDatabase = FirebaseDatabase.getInstance();
        storageReference = UtilidadesFirebaseBD.getFirebaseStorageFromUrl();

        databaseReference = firebaseDatabase.getReference();
        miNegocio = (MiNegocio) intent.getSerializableExtra(Constantes.MINEGOCIOOBJECT);
        sharedPreferencesSeguro = SharedPreferencesSeguroSingleton.getInstance(this, Constantes.SHARED_PREFERENCES_INFOUSUARIO, Constantes.SECURE_KEY_SHARED_PREFERENCES);

        imgvImagenNegocio = (ImageView) findViewById(R.id.imagennegocio_activity_administrarminegocio);
        tvNitNegocio = (TextView) findViewById(R.id.nit_activity_administrarminegocio);
        tvNombre = (TextView) findViewById(R.id.nombrenegocio_activity_administrarminegocio);
        tvTelefono = (TextView) findViewById(R.id.telefono_activity_administrarminegocio);
        tvTipoNegocio = (TextView) findViewById(R.id.tiponegocio_activity_administrarminegocio);
        tvDireccion = (TextView) findViewById(R.id.direccion_activity_administrarminegocio);
        tvBarrio = (TextView) findViewById(R.id.barrio_activity_administrarminegocio);
        tvDatosAdicionales = (TextView) findViewById(R.id.datosadicionales_activity_administrarminegocio);
        tvHorariolaboral = (TextView) findViewById(R.id.horariolaboral_activity_administrarminegocio);

        context = this;

        if(null != miNegocio){
            settearInforamcioEnViesMiNegocio(miNegocio);
            final StorageReference storageReferenceImagenes = UtilidadesFirebaseBD.getReferenceImagenMiNegocio(storageReference, miNegocio);
            storageReferenceImagenes.getMetadata().addOnSuccessListener(new OnSuccessListener<StorageMetadata>() {
                @Override
                public void onSuccess(StorageMetadata storageMetadata) {
                    Glide.with(context).using(new FirebaseImageLoader()).load(storageReferenceImagenes).diskCacheStrategy(DiskCacheStrategy.RESULT).signature(new StringSignature(String.valueOf(storageMetadata.getCreationTimeMillis()))).into(imgvImagenNegocio);
                }
            });


        }

    }
    public  void seleccionarImagenMiNegocio(View view){
        final CharSequence[] items = {getString(R.string.st_camara), getString(R.string.st_galeria), getString(R.string.st_cancelar)};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.st_elegirImagen));
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                boolean result = Utilidades.verificarPermisos(AdministrarMiNegocioActivity.this);
                if(getString(R.string.st_camara).equals(items[which])){
                    userChoose = getString(R.string.st_camara);
                    if (result){
                        camaraIntent();
                    }
                }else if(getString(R.string.st_galeria).equals(items[which])){
                    userChoose = getString(R.string.st_galeria);
                    if(result){
                        galeriaIntent();
                    }
                }else if(getString(R.string.st_cancelar).equals(items[which])){
                    dialog.dismiss();
                }

            }
        });
        builder.show();
    }

    private void galeriaIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);

        startActivityForResult(Intent.createChooser(intent, getString(R.string.st_selecImagen)), SELECT_FILE);
    }

    private void camaraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode){
            case Utilidades.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    if(getString(R.string.st_camara).equals(userChoose)){
                        camaraIntent();
                    }else if(getString(R.string.st_galeria).equals(userChoose)){
                        galeriaIntent();
                    }
                }else {
                    Toast.makeText(AdministrarMiNegocioActivity.this, R.string.str_permisosdenegados,
                            Toast.LENGTH_SHORT ).show();
                }
                break;
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK){
            if(requestCode == SELECT_FILE){
                onSelectFromGaleryResult(data);
            }else if(requestCode == REQUEST_CAMERA){
                onCaptureImageResult(data);
            }
        }

    }

    private void onCaptureImageResult(Intent data) {
        Bitmap bitmap = (Bitmap) data.getExtras().get("data");
        guardarImagenMiNegocio(bitmap);
        imgvImagenNegocio.setImageBitmap(bitmap);

    }

    private void onSelectFromGaleryResult(Intent data) {
        Bitmap bitmap = null;
        if(null != data){
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
                guardarImagenMiNegocio(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
            imgvImagenNegocio.setImageBitmap(bitmap);
        }

    }
    private void guardarImagenMiNegocio(Bitmap bitmap){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        try {
            byte [] dataImage = byteArrayOutputStream.toByteArray();
            subirImagenAFireBaseStorage(dataImage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void settearInforamcioEnViesMiNegocio(MiNegocio miNegocio){
        tvNitNegocio.setText(getString(R.string.str_nit)+miNegocio.getNitNegocio());
        tvNombre.setText(miNegocio.getNombreNegocio());
        tvTelefono.setText(getString(R.string.str_telefono)+miNegocio.getTelefonoNegocio());
        tvTipoNegocio.setText(getString(R.string.str_tiponegocio)+miNegocio.getTipoNegocio().getTipoNegocio());

        Direccion direccion = miNegocio.getDireccion();
        tvDireccion.setText(Utilidades.getStrDireccion(direccion));
        tvBarrio.setText(getString(R.string.str_barrio)+direccion.getBarrio());
        tvDatosAdicionales.setText(direccion.getDatosAdicionales());

        Horario horario = miNegocio.getHorarioNegocio();
        tvHorariolaboral.setText(Utilidades.getStrHorario(horario));

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem){
        int item = menuItem.getItemId();
        if(item == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(menuItem);
    }
    public void editarInforamcionMiNegocio(View view){
        Intent intent = new Intent(this, RegistrarMiNegocioActivity.class);
        intent.putExtra(Constantes.MINEGOCIOOBJECT, miNegocio);
        intent.putExtra(Constantes.CALL_FROM_ACTIVITY_ADMINISTRARMINEGOCIO, AdministrarMiNegocioActivity.class.getName());
        startActivity(intent);
    }
    public void editarDireccionMiNegocio(View view){
        Intent intent = new Intent(this, RegistrarDireccionActivity.class);
        intent.putExtra(Constantes.MINEGOCIOOBJECT, miNegocio);
        intent.putExtra(Constantes.CALL_FROM_ACTIVITY_ADMINISTRARMINEGOCIO, AdministrarMiNegocioActivity.class.getName());
        startActivity(intent);
    }
    public void editarHoararioMiNegocio(View view){
        Intent intent = new Intent(this, RegistrarHorarioActivity.class);
        intent.putExtra(Constantes.MINEGOCIOOBJECT, miNegocio);
        intent.putExtra(Constantes.CALL_FROM_ACTIVITY_ADMINISTRARMINEGOCIO, AdministrarMiNegocioActivity.class.getName());
        startActivity(intent);
    }
    public void agregarEmpleado(View view){

    }
    @Override
    public void onStart(){
        super.onStart();
        databaseReference.child(Constantes.MINEGOCIO_FIREBASE_BD).child(sharedPreferencesSeguro.getString(Constantes.USERUID)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                MiNegocio minegocioOnchange = dataSnapshot.child(miNegocio.getKeyChild()).getValue(MiNegocio.class);
                if(null != minegocioOnchange) {
                   settearInforamcioEnViesMiNegocio(minegocioOnchange);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void subirImagenAFireBaseStorage(byte [] dataImage) throws FileNotFoundException {
        UploadTask uploadTask = UtilidadesFirebaseBD.getReferenceImagenMiNegocio(storageReference, miNegocio).putBytes(dataImage);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AdministrarMiNegocioActivity.this, "No se pudo cargar la imagen",
                        Toast.LENGTH_SHORT ).show();

            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(AdministrarMiNegocioActivity.this, "Imagen cargada correctamente",
                        Toast.LENGTH_SHORT ).show();
            }
        });

    }
}
