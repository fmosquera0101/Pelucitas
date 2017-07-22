package co.com.fredymosqueralemus.pelucitas;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;

import co.com.fredymosqueralemus.pelucitas.constantes.Constantes;
import co.com.fredymosqueralemus.pelucitas.horario.FormularioRegistrarHorario;
import co.com.fredymosqueralemus.pelucitas.horario.Horario;
import co.com.fredymosqueralemus.pelucitas.modelo.minegocio.MiNegocio;
import co.com.fredymosqueralemus.pelucitas.sharedpreference.SharedPreferencesSeguro;
import co.com.fredymosqueralemus.pelucitas.sharedpreference.SharedPreferencesSeguroSingleton;
import co.com.fredymosqueralemus.pelucitas.utilidades.UtilidadesFecha;
import co.com.fredymosqueralemus.pelucitas.utilidades.UtilidadesFirebaseBD;

public class RegistrarHorarioActivity extends AppCompatActivity {
    private FormularioRegistrarHorario formularioRegistrarHorario;
    private Intent intent;
    private MiNegocio miNegocio;

    private Button btnRegistrarHorario;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_horario);
        intent = getIntent();
        miNegocio = (MiNegocio)intent.getSerializableExtra(Constantes.MINEGOCIO_OBJECT);
        SharedPreferencesSeguro sharedPreferencesSeguro = SharedPreferencesSeguroSingleton.getInstance(this, Constantes.SHARED_PREFERENCES_INFOUSUARIO, Constantes.SECURE_KEY_SHARED_PREFERENCES);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        btnRegistrarHorario = (Button) findViewById(R.id.siguiente_btn_registrarhorariolayout);
        formularioRegistrarHorario = new FormularioRegistrarHorario(this, RegistrarHorarioActivity.this);
        formularioRegistrarHorario.addClickListenerCheckBoxesDias();

        if(AdministrarMiNegocioActivity.class.getName().equals(intent.getStringExtra(Constantes.CALL_FROM_ACTIVITY_ADMINISTRARMINEGOCIO))){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            btnRegistrarHorario.setText(getString(R.string.str_editar));
            getSupportActionBar().setTitle(getString(R.string.str_editarhorario));
            databaseReference = FirebaseDatabase.getInstance().getReference(UtilidadesFirebaseBD.getUrlInsercionHorariosXNegocios(miNegocio.getNitNegocio()));
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Horario horarioXNegocio = dataSnapshot.getValue(Horario.class);
                    formularioRegistrarHorario.setCheckedDias(horarioXNegocio);
                    formularioRegistrarHorario.settearHorarioLaboral(horarioXNegocio);
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

    }
    public void registrarHorario(View view){
        if(formularioRegistrarHorario.isHaSeleccionadoCampos()){
            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            DatabaseReference databaseReference;

            if(AdministrarMiNegocioActivity.class.getName().equals(intent.getStringExtra(Constantes.CALL_FROM_ACTIVITY_ADMINISTRARMINEGOCIO))){
                miNegocio.setHorarioNegocio(formularioRegistrarHorario.getHorario());
                miNegocio.setFechaModificacion(UtilidadesFecha.convertirDateAString(new Date(), Constantes.FORMAT_DDMMYYYYHHMMSS));
                databaseReference = firebaseDatabase.getReference(UtilidadesFirebaseBD.getUrlInsercionMiNegocio(miNegocio.getNitNegocio()));
                databaseReference.setValue(miNegocio);
                databaseReference = firebaseDatabase.getReference(UtilidadesFirebaseBD.getUrlInsercionHorariosXNegocios(miNegocio.getNitNegocio()));
                databaseReference.setValue(formularioRegistrarHorario.getHorario());
                finish();
            }else{
                String nitNegocio = miNegocio.getNitNegocio();
                databaseReference = firebaseDatabase.getReference(UtilidadesFirebaseBD.getUrlInsercionHorariosXNegocios(nitNegocio));
                Horario horario = formularioRegistrarHorario.getHorario();
                databaseReference.setValue(horario);
                /*miNegocio.setHorarioNegocio(horario);
                databaseReference = firebaseDatabase.getReference(UtilidadesFirebaseBD.getUrlInsercionMiNegocio(miNegocio.getUidAdministrador()));
                databaseReference.push().setValue(miNegocio);*/
                iraHome();
            }

        }
    }
    private void iraHome(){
        finish();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem){
        int item = menuItem.getItemId();
        if(AdministrarMiNegocioActivity.class.getName().equals(intent.getStringExtra(Constantes.CALL_FROM_ACTIVITY_ADMINISTRARMINEGOCIO))){
            if(item == android.R.id.home){
                onBackPressed();
            }
        }
        return super.onOptionsItemSelected(menuItem);
    }

}
