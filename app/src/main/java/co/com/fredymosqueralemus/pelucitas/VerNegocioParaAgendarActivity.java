package co.com.fredymosqueralemus.pelucitas;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import co.com.fredymosqueralemus.pelucitas.direccion.Direccion;
import co.com.fredymosqueralemus.pelucitas.horario.Horario;
import co.com.fredymosqueralemus.pelucitas.modelo.minegocio.MiNegocio;
import co.com.fredymosqueralemus.pelucitas.utilidades.Utilidades;
import co.com.fredymosqueralemus.pelucitas.utilidades.UtilidadesFirebaseBD;
import co.com.fredymosqueralemus.pelucitas.utilidades.UtilidadesImagenes;


public class VerNegocioParaAgendarActivity extends AppCompatActivity {

    private TextView textvNombreNegocio;
    private TextView textvDireccion;
    private TextView textvTelefono;
    private TextView textvHorario;
    private TextView textvDescripcionNegocio;

    private ImageView imageViewImagenMiNegoco;

    private Intent intent;
    private MiNegocio miNegocio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_negocio_para_agendar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        textvNombreNegocio = (TextView) findViewById(R.id.nombrenegocio_VerNegocioParaAgendarActivity);
        textvDireccion = (TextView) findViewById(R.id.direccionnegocio_VerNegocioParaAgendarActivity);
        textvTelefono = (TextView) findViewById(R.id.telefono_negocio_VerNegocioParaAgendarActivity);
        textvHorario = (TextView) findViewById(R.id.horario_negocio_VerNegocioParaAgendarActivity);
        textvDescripcionNegocio = (TextView) findViewById(R.id.descripcionnegocio_VerNegocioParaAgendarActivity);
        imageViewImagenMiNegoco = (ImageView) findViewById(R.id.imagen_minegocio_VerNegocioParaAgendarActivity);

        intent = getIntent();
        miNegocio = (MiNegocio) intent.getSerializableExtra(Constantes.MINEGOCIO_OBJECT);
        settearInformacionEnViewsMiNegocio(miNegocio);
        StorageReference storageReference = UtilidadesFirebaseBD.getFirebaseStorageFromUrl();
        UtilidadesImagenes.cargarImagenMiNegocioNoCircular(imageViewImagenMiNegoco, miNegocio, this, storageReference);


    }

    private void settearInformacionEnViewsMiNegocio(MiNegocio miNegocio) {

        textvNombreNegocio.setText(miNegocio.getNombreNegocio());
        settearInfoDireccionFromFB(miNegocio);
        textvTelefono.setText(getString(R.string.str_telefono) + ": " + miNegocio.getTelefonoNegocio());
        settearInfoHorarioFromFB(miNegocio);
        textvDescripcionNegocio.setText(miNegocio.getDescripcionNegocio());

    }
    private void settearInfoDireccionFromFB(MiNegocio miNegocio){
        Direccion direccion = miNegocio.getDireccion();
        if(null == direccion){
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(UtilidadesFirebaseBD.getUrlInserccionDireccionesXNegocio(miNegocio.getNitNegocio()));
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Direccion direccion = dataSnapshot.getValue(Direccion.class);
                    textvDireccion.setText(Utilidades.getStrDireccion(direccion)+", "+direccion.getDatosAdicionales());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }else{
            textvDireccion.setText(Utilidades.getStrDireccion(direccion)+", "+direccion.getDatosAdicionales());
        }

    }

    private void settearInfoHorarioFromFB(MiNegocio miNegocio){
        Horario horario = miNegocio.getHorarioNegocio();
        if(null == horario){
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(UtilidadesFirebaseBD.getUrlInsercionHorariosXNegocios(miNegocio.getNitNegocio()));
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Horario horario  = dataSnapshot.getValue(Horario.class);
                    textvHorario.setText(Utilidades.getStrHorario(horario));
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }else{
            textvHorario.setText(Utilidades.getStrHorario(horario));
        }

    }

    public void reservarCita(View view){
        Intent intent = new Intent(this, AdministrarMisEmpleadosActivity.class);
        intent.putExtra(Constantes.MINEGOCIO_OBJECT, miNegocio);
        startActivity(intent);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int intItem = item.getItemId();
        if (intItem == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
