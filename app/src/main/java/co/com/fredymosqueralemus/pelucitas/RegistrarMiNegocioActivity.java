package co.com.fredymosqueralemus.pelucitas;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

import co.com.fredymosqueralemus.pelucitas.constantes.Constantes;
import co.com.fredymosqueralemus.pelucitas.modelo.minegocio.MiNegocio;
import co.com.fredymosqueralemus.pelucitas.modelo.minegocio.TipoNegocio;
import co.com.fredymosqueralemus.pelucitas.utilidades.UtilidadesFecha;
import co.com.fredymosqueralemus.pelucitas.utilidades.UtilidadesFirebaseBD;

public class RegistrarMiNegocioActivity extends AppCompatActivity {

    private EditText etxtNitNegocio;
    private EditText etxtNombreNegocio;
    private EditText etxtTelefono;
    private Spinner spnTipoNegocio;
    private String [] arrayTiposNegocios;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private FirebaseUser mFirebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_mi_negocio);

        etxtNitNegocio = (EditText) findViewById(R.id.nit_negocio_etxt_registrarminegocio);
        etxtNombreNegocio = (EditText) findViewById(R.id.nombre_negocio_etxt_registrarminegocio);
        etxtTelefono = (EditText) findViewById(R.id.telefono_etxt_registrarminegocio);
        spnTipoNegocio = (Spinner) findViewById(R.id.tipo_negocio_spn_registrardatospersonaleslayout);
        arrayTiposNegocios = getResources().getStringArray(R.array.arraystr_tiposnegocio);
        ArrayAdapter<CharSequence> arrayAdapterTiposNegocio = ArrayAdapter.createFromResource(this, R.array.arraystr_tiposnegocio, android.R.layout.simple_spinner_item);
        arrayAdapterTiposNegocio.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnTipoNegocio.setAdapter(arrayAdapterTiposNegocio);

        mAuth = FirebaseAuth.getInstance();
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                mFirebaseUser = firebaseAuth.getCurrentUser();
            }
        };
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setHomeButtonEnabled(true);
    }

    public void registrarInformacionMiNegocio(View view){
        if(!isAlgunCampoFormularioDireccionVacio()){
            FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
            DatabaseReference mDatabaseReference;
            MiNegocio miNegocio = new MiNegocio();
            miNegocio.setNitNegocio(etxtNitNegocio.getText().toString().trim());
            miNegocio.setNombreNegocio(etxtNombreNegocio.getText().toString().trim());
            miNegocio.setTelefonoNegocio(etxtTelefono.getText().toString().trim());
            TipoNegocio tipoNegocio = new TipoNegocio();
            tipoNegocio.setTipoNegocio(spnTipoNegocio.getSelectedItem().toString());
            miNegocio.setTipoNegocio(tipoNegocio);
            miNegocio.setFechaInsercion(UtilidadesFecha.convertirDateAString(new Date()));
            miNegocio.setFechaModificacion(null);
            miNegocio.setUidAdministrador(mFirebaseUser.getUid());
            mDatabaseReference = mFirebaseDatabase.getReference(UtilidadesFirebaseBD.getUrlInsercionMiNegocio(miNegocio.getNitNegocio()));
            mDatabaseReference.setValue(miNegocio);
            if(miNegocio.getTipoNegocio().equals(arrayTiposNegocios[1])){
                mDatabaseReference = mFirebaseDatabase.getReference(UtilidadesFirebaseBD.getUrlInsercionTiposNegocio(Constantes.TIPOS_NEGOCIOS_BARBERIA_FIREBASE_BD));
                mDatabaseReference.setValue(miNegocio.getNitNegocio());
            }else  if(miNegocio.getTipoNegocio().equals(arrayTiposNegocios[2])){
                mDatabaseReference = mFirebaseDatabase.getReference(UtilidadesFirebaseBD.getUrlInsercionTiposNegocio(Constantes.TIPOS_NEGOCIOS_PELUQUERIA_FIREBASE_BD));
                mDatabaseReference.setValue(miNegocio.getNitNegocio());
            }else  if(miNegocio.getTipoNegocio().equals(arrayTiposNegocios[3])){
                mDatabaseReference = mFirebaseDatabase.getReference(UtilidadesFirebaseBD.getUrlInsercionTiposNegocio(Constantes.TIPOS_NEGOCIOS_SALONESDEBELLEZA_FIREBASE_BD));
                mDatabaseReference.setValue(miNegocio.getNitNegocio());
            }
            abrirActivityRegistrarDireccionMiNegocio(miNegocio);
        }
    }
    private void  abrirActivityRegistrarDireccionMiNegocio(MiNegocio miNegocio){
        Intent intent = new Intent(this, RegistrarDireccionActivity.class);
        intent.putExtra(Constantes.CALL_FROM_ACTIVITY_REGISTRAR_MINEGOCIO, RegistrarMiNegocioActivity.class.getName());
        intent.putExtra(Constantes.NIT_MINEGOCIO, miNegocio.getNitNegocio());
        startActivity(intent);

    }

    private boolean isAlgunCampoFormularioDireccionVacio(){
        if(TextUtils.isEmpty(etxtNitNegocio.getText())){
            etxtNitNegocio.requestFocus();
            etxtNitNegocio.setError(getString(R.string.error_campo_requerido));
            return true;
        }
        if(TextUtils.isEmpty(etxtNombreNegocio.getText())){
            etxtNombreNegocio.requestFocus();
            etxtNombreNegocio.setError(getString(R.string.error_campo_requerido));
            return true;
        }
        if(TextUtils.isEmpty(etxtTelefono.getText())){
            etxtTelefono.requestFocus();
            etxtTelefono.setError(getString(R.string.error_campo_requerido));
            return  true;
        }
        if(spnTipoNegocio.getSelectedItem().equals(arrayTiposNegocios[0])){
            ((TextView) spnTipoNegocio.getSelectedView()).requestFocus();
            ((TextView) spnTipoNegocio.getSelectedView()).setError(getString(R.string.error_campo_requerido));
            return true;
        }
        return false;
    }
    @Override
    public void onStart(){
        super.onStart();
        mAuth.addAuthStateListener(mAuthStateListener);
    }
    @Override
    public void onStop(){
        super.onStop();
        if(null != mAuthStateListener){
            mAuth.removeAuthStateListener(mAuthStateListener);
        }
    }
}
