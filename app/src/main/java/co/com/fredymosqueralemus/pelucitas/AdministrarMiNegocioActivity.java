package co.com.fredymosqueralemus.pelucitas;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import co.com.fredymosqueralemus.pelucitas.constantes.Constantes;
import co.com.fredymosqueralemus.pelucitas.modelo.minegocio.MiNegocio;
import co.com.fredymosqueralemus.pelucitas.sharedpreference.SharedPreferencesSeguro;
import co.com.fredymosqueralemus.pelucitas.sharedpreference.SharedPreferencesSeguroSingleton;
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
    private StorageReference storageReference;
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
        miNegocio = (MiNegocio) intent.getSerializableExtra(Constantes.MINEGOCIO_OBJECT);
        sharedPreferencesSeguro = SharedPreferencesSeguroSingleton.getInstance(this, Constantes.SHARED_PREFERENCES_INFOUSUARIO, Constantes.SECURE_KEY_SHARED_PREFERENCES);
        imgvMiNegocio = (ImageView) findViewById(R.id.imagen_minegocio_activity_administrarminegocio);
        txvNombreMiNegocio = (TextView) findViewById(R.id.nombrenegocio_activity_administrarminegocio);
        txvNitMiNegocio = (TextView) findViewById(R.id.nitnegocio_activity_administrarminegocio);
        txvTipoNegocio = (TextView) findViewById(R.id.tipo_negocio_activity_administrarminegocio);

        getInforamcionNegocio();

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

    public void agregarEmpleado(View view) {

    }
    @Override
    public void onStart(){
        super.onStart();
        getInforamcionNegocio();

    }

    private void getInforamcionNegocio(){
        if (null != miNegocio) {
            databaseReference.child(Constantes.MINEGOCIO_FIREBASE_BD).child(sharedPreferencesSeguro.getString(Constantes.USERUID)).child(miNegocio.getKeyChild()).addListenerForSingleValueEvent(new ValueEventListener() {
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
}
