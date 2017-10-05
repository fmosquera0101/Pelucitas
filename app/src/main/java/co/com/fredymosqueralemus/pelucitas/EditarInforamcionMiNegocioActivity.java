package co.com.fredymosqueralemus.pelucitas;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import co.com.fredymosqueralemus.pelucitas.constantes.Constantes;
import co.com.fredymosqueralemus.pelucitas.modelo.minegocio.MiNegocio;
import co.com.fredymosqueralemus.pelucitas.imagenes.SeleccionarImagen;
import co.com.fredymosqueralemus.pelucitas.services.TaskCargarImagenMiNegocio;
import co.com.fredymosqueralemus.pelucitas.sharedpreference.SharedPreferencesSeguro;
import co.com.fredymosqueralemus.pelucitas.sharedpreference.SharedPreferencesSeguroSingleton;
import co.com.fredymosqueralemus.pelucitas.utilidades.Utilidades;
import co.com.fredymosqueralemus.pelucitas.utilidades.UtilidadesFirebaseBD;
import co.com.fredymosqueralemus.pelucitas.utilidades.UtilidadesImagenes;

public class EditarInforamcionMiNegocioActivity extends AppCompatActivity {


    private Context context;
    private MiNegocio miNegocio;

    private ImageView imgvImagenMiNegocio;
    private TextView txvNombreNegocio;
    private TextView txvNitNegocio;
    private TextView txvTelefonoNegocio;
    private TextView txvTipoNegocio;

    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    private SeleccionarImagen seleccionarImagenMiNegocio;
    private SharedPreferencesSeguro sharedPreferencesSeguro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_inforamcion_mi_negocio);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        Intent intent = getIntent();
        context = this;
        storageReference = UtilidadesFirebaseBD.getFirebaseStorageFromUrl();
        miNegocio = (MiNegocio) intent.getSerializableExtra(Constantes.MINEGOCIO_OBJECT);
        sharedPreferencesSeguro = SharedPreferencesSeguroSingleton.getInstance(this, Constantes.SHARED_PREFERENCES_INFOUSUARIO, Constantes.SECURE_KEY_SHARED_PREFERENCES);
        seleccionarImagenMiNegocio = new SeleccionarImagen(context, this);
        imgvImagenMiNegocio = (ImageView) findViewById(R.id.imagen_miperfil_activity_editar_informacion_minegocio);
        txvNombreNegocio = (TextView) findViewById(R.id.nombre_negocio_activity_editar_informacion_minegocio);
        txvNitNegocio = (TextView) findViewById(R.id.nit_negocio_activity_editar_informacion_minegocio);
        txvTelefonoNegocio = (TextView) findViewById(R.id.telefono_negocio_activity_editar_informacion_minegocio);
        txvTipoNegocio = (TextView) findViewById(R.id.tipo_negocio_activity_editar_informacion_minegocio);

        obtenerInformacionMiNegocio();

    }
    private void obtenerInformacionMiNegocio(){
        if(null != miNegocio){
            databaseReference.child(Constantes.MINEGOCIO_FIREBASE_BD).child(miNegocio.getNitNegocio()).addListenerForSingleValueEvent(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            MiNegocio miNegocio = dataSnapshot.getValue(MiNegocio.class);
                            txvNombreNegocio.setText(miNegocio.getNombreNegocio());
                            txvNitNegocio.setText(getString(R.string.str_nit)+": "+miNegocio.getNitNegocio());
                            txvTelefonoNegocio.setText(getString(R.string.str_telefono)+": "+miNegocio.getTelefonoNegocio());
                            txvTipoNegocio.setText(getString(R.string.str_tiponegocio)+": "+miNegocio.getTipoNegocio().getTipoNegocio());
                            UtilidadesImagenes.cargarImagenMiNegocioNoCircular(imgvImagenMiNegocio, miNegocio, context, storageReference);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    }
            );


        }
    }
    public void editarInformacionBasicaMiNegocio(View view){
        Intent intent = new Intent(this, RegistrarMiNegocioActivity.class);
        intent.putExtra(Constantes.MINEGOCIO_OBJECT, miNegocio);
        intent.putExtra(Constantes.CALL_FROM_ACTIVITY_ADMINISTRARMINEGOCIO, EditarInforamcionMiNegocioActivity.class.getName());
        startActivity(intent);


    }
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem){
        int item = menuItem.getItemId();
        if(item == android.R.id.home){
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    public void seleccionarImagenMiNegocio(View view){
        seleccionarImagenMiNegocio.seleccionarImagen();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Utilidades.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (getString(R.string.st_camara).equals(seleccionarImagenMiNegocio.getUserChoose())) {
                        seleccionarImagenMiNegocio.camaraIntent();
                    } else if (getString(R.string.st_galeria).equals(seleccionarImagenMiNegocio.getUserChoose())) {
                        seleccionarImagenMiNegocio.galeriaIntent();
                    }
                } else {
                    Toast.makeText(context, R.string.str_permisosdenegados,
                            Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Constantes.SELECT_FILE) {
                onSelectFromGaleryResult(data);
            } else if (requestCode == Constantes.REQUEST_CAMERA) {
                onCaptureImageResult(data);
            }
        }

    }

    private void onCaptureImageResult(Intent data) {
        Bitmap bitmap = (Bitmap) data.getExtras().get("data");
        guardarImagenMiNegocio(bitmap);
    }

    private void onSelectFromGaleryResult(Intent data) {
        Bitmap bitmap = null;
        if (null != data) {
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
                guardarImagenMiNegocio(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    private void guardarImagenMiNegocio(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);

        try {
            byte[] dataImage = byteArrayOutputStream.toByteArray();
            subirImagenAFireBaseStorage(dataImage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void subirImagenAFireBaseStorage(byte[] dataImage) throws FileNotFoundException {
        Toast.makeText(context, getString(R.string.str_msj_subir_imagen_negocio),
                Toast.LENGTH_SHORT).show();
        new TaskCargarImagenMiNegocio(context, dataImage, miNegocio, imgvImagenMiNegocio).execute();

    }
    @Override
    public void onStart(){
        super.onStart();
        obtenerInformacionMiNegocio();
    }

}
