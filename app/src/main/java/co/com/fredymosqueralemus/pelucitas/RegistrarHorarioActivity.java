package co.com.fredymosqueralemus.pelucitas;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import co.com.fredymosqueralemus.pelucitas.constantes.Constantes;
import co.com.fredymosqueralemus.pelucitas.horario.FormularioRegistrarHorario;
import co.com.fredymosqueralemus.pelucitas.horario.Horario;
import co.com.fredymosqueralemus.pelucitas.modelo.minegocio.MiNegocio;
import co.com.fredymosqueralemus.pelucitas.utilidades.UtilidadesFirebaseBD;

public class RegistrarHorarioActivity extends AppCompatActivity {
    private FormularioRegistrarHorario formularioRegistrarHorario;
    private FirebaseAuth mAuth;
    private Intent intent;
    private MiNegocio miNegocio;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_horario);
        intent = getIntent();
        miNegocio = (MiNegocio)intent.getSerializableExtra(Constantes.MINEGOCIOOBJECT);
        mAuth = FirebaseAuth.getInstance();

        formularioRegistrarHorario = new FormularioRegistrarHorario(this, RegistrarHorarioActivity.this);
        formularioRegistrarHorario.addClickListenerCheckBoxesDias();

    }
    public void registrarHorario(View view){
        if(formularioRegistrarHorario.isHaSeleccionadoCampos()){
            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            String nitNegocio = miNegocio.getNitNegocio();
            DatabaseReference databaseReference = firebaseDatabase.getReference(UtilidadesFirebaseBD.getUrlInsercionHorariosXNegocios(nitNegocio));
            Horario horario = formularioRegistrarHorario.getHorario();
            databaseReference.setValue(horario);
            miNegocio.setHorarioNegocio(horario);
            databaseReference = firebaseDatabase.getReference(UtilidadesFirebaseBD.getUrlInsercionMiNegocio(miNegocio.getUidAdministrador(), miNegocio.getNitNegocio()));
            databaseReference.push().setValue(miNegocio);
            iraHome();
        }
    }
    private void iraHome(){
        /*Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);*/
        finish();
    }

}
