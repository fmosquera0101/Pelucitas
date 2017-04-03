package co.com.fredymosqueralemus.pelucitas;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.signature.StringSignature;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.vision.text.Text;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import co.com.fredymosqueralemus.pelucitas.adapters.AdapterMisNegocios;
import co.com.fredymosqueralemus.pelucitas.constantes.Constantes;
import co.com.fredymosqueralemus.pelucitas.modelo.minegocio.MiNegocio;
import co.com.fredymosqueralemus.pelucitas.sharedpreference.SharedPreferencesSeguro;
import co.com.fredymosqueralemus.pelucitas.sharedpreference.SharedPreferencesSeguroSingleton;
import co.com.fredymosqueralemus.pelucitas.utilidades.Utilidades;
import co.com.fredymosqueralemus.pelucitas.utilidades.UtilidadesFirebaseBD;
import co.com.fredymosqueralemus.pelucitas.utilidades.UtilidadesImagenes;

public class AdministrarMiNegocioActivity extends AppCompatActivity {

    private ImageView imgvMiNegocio;
    private TextView txvNombreMiNegocio;
    private TextView txvNitMiNegocio;
    private TextView txvTipoNegocio;

    private Intent intent;
    private Context context;
    private MiNegocio miNegocio;

    private SharedPreferencesSeguro sharedPreferencesSeguro;
    private int REQUEST_CAMERA = 0;
    private int SELECT_FILE = 1;

    private StorageReference storageReference;
    private String userChoose;
    private DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administrar_mi_negocio);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        intent = getIntent();
        context = this;
        storageReference = UtilidadesFirebaseBD.getFirebaseStorageFromUrl();
        miNegocio = (MiNegocio) intent.getSerializableExtra(Constantes.MINEGOCIOOBJECT);
        sharedPreferencesSeguro = SharedPreferencesSeguroSingleton.getInstance(this, Constantes.SHARED_PREFERENCES_INFOUSUARIO, Constantes.SECURE_KEY_SHARED_PREFERENCES);
        imgvMiNegocio = (ImageView) findViewById(R.id.imagen_minegocio_activity_administrarminegocio);
        txvNombreMiNegocio = (TextView) findViewById(R.id.nombrenegocio_activity_administrarminegocio);
        txvNitMiNegocio = (TextView) findViewById(R.id.nitnegocio_activity_administrarminegocio);
        txvTipoNegocio = (TextView) findViewById(R.id.tipo_negocio_activity_administrarminegocio);

        if (null != miNegocio) {
            databaseReference.child(Constantes.MINEGOCIO_FIREBASE_BD).child(sharedPreferencesSeguro.getString(Constantes.USERUID)).child(miNegocio.getKeyChild()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    MiNegocio miNegocio = dataSnapshot.getValue(MiNegocio.class);
                    settearInforamcioEnViesMiNegocio(miNegocio);
                    UtilidadesImagenes.cargarImagenMiNegocio(imgvMiNegocio, miNegocio, context, storageReference);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }


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
        intent.putExtra(Constantes.MINEGOCIOOBJECT, miNegocio);
        intent.putExtra(Constantes.CALL_FROM_ACTIVITY_ADMINISTRARMINEGOCIO, AdministrarMiNegocioActivity.class.getName());
        startActivity(intent);
    }

    public void editarDireccionMiNegocio(View view) {
        Intent intent = new Intent(this, RegistrarDireccionActivity.class);
        intent.putExtra(Constantes.MINEGOCIOOBJECT, miNegocio);
        intent.putExtra(Constantes.CALL_FROM_ACTIVITY_ADMINISTRARMINEGOCIO, AdministrarMiNegocioActivity.class.getName());
        startActivity(intent);
    }

    public void editarHoararioMiNegocio(View view) {
        Intent intent = new Intent(this, RegistrarHorarioActivity.class);
        intent.putExtra(Constantes.MINEGOCIOOBJECT, miNegocio);
        intent.putExtra(Constantes.CALL_FROM_ACTIVITY_ADMINISTRARMINEGOCIO, AdministrarMiNegocioActivity.class.getName());
        startActivity(intent);
    }

    public void agregarEmpleado(View view) {

    }

    @Override
    public void onStart() {
        super.onStart();
        databaseReference.child(Constantes.MINEGOCIO_FIREBASE_BD).child(sharedPreferencesSeguro.getString(Constantes.USERUID)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                MiNegocio minegocioOnchange = dataSnapshot.child(miNegocio.getKeyChild()).getValue(MiNegocio.class);
                if (null != minegocioOnchange) {
                    settearInforamcioEnViesMiNegocio(minegocioOnchange);
                    UtilidadesImagenes.cargarImagenMiNegocio(imgvMiNegocio, minegocioOnchange, context, storageReference);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
