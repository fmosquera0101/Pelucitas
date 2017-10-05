package co.com.fredymosqueralemus.pelucitas;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
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
import co.com.fredymosqueralemus.pelucitas.imagenes.SeleccionarImagen;
import co.com.fredymosqueralemus.pelucitas.modelo.minegocio.MiNegocio;
import co.com.fredymosqueralemus.pelucitas.services.TaskCargarImagenMiNegocio;
import co.com.fredymosqueralemus.pelucitas.utilidades.Utilidades;
import co.com.fredymosqueralemus.pelucitas.utilidades.UtilidadesFirebaseBD;
import co.com.fredymosqueralemus.pelucitas.utilidades.UtilidadesImagenes;

public class AdministrarMiNegocioActivity extends AppCompatActivity {

    private ImageView imgvMiNegocio;
    private TextView txvNombreMiNegocio;
    private TextView txvNitMiNegocio;
    private TextView txvTipoNegocio;

    private Context context;
    private MiNegocio miNegocio;

    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    private SeleccionarImagen seleccionarImagenMiNegocio;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administrar_mi_negocio);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        Intent intent = getIntent();
        context = this;
        storageReference = UtilidadesFirebaseBD.getFirebaseStorageFromUrl();
        seleccionarImagenMiNegocio = new SeleccionarImagen(context, this);
        miNegocio = (MiNegocio) intent.getSerializableExtra(Constantes.MINEGOCIO_OBJECT);

        inicializarViewsAdministrarMisNegocios();
        getInformacionNegocio();

    }

    private void inicializarViewsAdministrarMisNegocios() {
        imgvMiNegocio = (ImageView) findViewById(R.id.imagen_minegocio_activity_administrarminegocio);
        txvNombreMiNegocio = (TextView) findViewById(R.id.nombrenegocio_activity_administrarminegocio);
        txvNitMiNegocio = (TextView) findViewById(R.id.nitnegocio_activity_administrarminegocio);
        txvTipoNegocio = (TextView) findViewById(R.id.tipo_negocio_activity_administrarminegocio);
    }

    private void settearInforamcioEnViesMiNegocio(MiNegocio miNegocio) {
        txvNombreMiNegocio.setText(miNegocio.getNombreNegocio());
        txvNitMiNegocio.setText(miNegocio.getNitNegocio());
        txvTipoNegocio.setText(miNegocio.getTipoNegocio().getTipoNegocio());

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int item = menuItem.getItemId();
        if (item == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    public void editarInformacionMiNegocio(View view) {
        Intent intent = new Intent(this, EditarInforamcionMiNegocioActivity.class);
        intent.putExtra(Constantes.MINEGOCIO_OBJECT, miNegocio);
        intent.putExtra(Constantes.CALL_FROM_ACTIVITY_ADMINISTRARMINEGOCIO, AdministrarMiNegocioActivity.class.getName());
        startActivity(intent);
    }

    public void abrirEditarDireccionMiNegocio(View view) {
        Intent intent = new Intent(this, RegistrarDireccionActivity.class);
        intent.putExtra(Constantes.MINEGOCIO_OBJECT, miNegocio);
        intent.putExtra(Constantes.CALL_FROM_ACTIVITY_ADMINISTRARMINEGOCIO, AdministrarMiNegocioActivity.class.getName());
        startActivity(intent);
    }

    public void editarHoararioMiNegocio(View view) {
        Intent intent = new Intent(this, RegistrarHorarioActivity.class);
        intent.putExtra(Constantes.MINEGOCIO_OBJECT, miNegocio);
        intent.putExtra(Constantes.CALL_FROM_ACTIVITY_ADMINISTRARMINEGOCIO, AdministrarMiNegocioActivity.class.getName());
        startActivity(intent);
    }

    public void listarEmpleados(View view) {
        Intent intent = new Intent(this, AdministrarMisEmpleadosActivity.class);
        intent.putExtra(Constantes.MINEGOCIO_OBJECT, miNegocio);
        intent.putExtra(Constantes.MINEGOCIO_OBJECT, miNegocio);
        startActivity(intent);

    }
    public void editarInformacionBasicaMiNegocio(View view){
        Intent intent = new Intent(this, RegistrarMiNegocioActivity.class);
        intent.putExtra(Constantes.MINEGOCIO_OBJECT, miNegocio);
        intent.putExtra(Constantes.CALL_FROM_ACTIVITY_ADMINISTRARMINEGOCIO, EditarInforamcionMiNegocioActivity.class.getName());
        startActivity(intent);


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
        new TaskCargarImagenMiNegocio(context, dataImage, miNegocio, imgvMiNegocio).execute();

    }
    @Override
    public void onStart(){
        super.onStart();
        getInformacionNegocio();

    }

    private void getInformacionNegocio(){
        if (null != miNegocio) {
            databaseReference.child(Constantes.MINEGOCIO_FIREBASE_BD).child(miNegocio.getNitNegocio()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    MiNegocio miNegocio = dataSnapshot.getValue(MiNegocio.class);
                    settearInforamcioEnViesMiNegocio(miNegocio);
                    UtilidadesImagenes.cargarImagenMiNegocioNoCircular(imgvMiNegocio, miNegocio, context, storageReference);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

    }
}
