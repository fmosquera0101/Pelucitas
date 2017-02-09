package co.com.fredymosqueralemus.pelucitas;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

import co.com.fredymosqueralemus.pelucitas.modelo.usuario.DireccionUsuario;
import co.com.fredymosqueralemus.pelucitas.utilidades.UtilidadesFecha;
import co.com.fredymosqueralemus.pelucitas.utilidades.UtilidadesFirebaseBD;

/**
 * Created by Fredy Mosquera Lemus on 2/02/17.
 */
public class RegistrarDireccionActivity extends AppCompatActivity {

    private EditText etxtPais;
    private EditText etxtDepartamento;
    private EditText etxtCiudadMunicipio;
    private EditText etxtCarreraCalle;
    private EditText etxtNumero1;
    private EditText etxtNumero2;
    private EditText etxtBarrio;
    private EditText etxtDatosAdicionales;

    FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private FirebaseUser firebaseUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_direccion);

        etxtPais = (EditText) findViewById(R.id.pais_etxt_registrardireccionlayout);
        etxtDepartamento = (EditText) findViewById(R.id.departamento_etxt_registrardireccionlayout);
        etxtCiudadMunicipio = (EditText) findViewById(R.id.ciudadmunicipio_etxt_registrardireccionlayout);
        etxtCarreraCalle = (EditText) findViewById(R.id.carreracalle_etxt_registrardireccionlayout);
        etxtNumero1 = (EditText) findViewById(R.id.numero1_etxt_registrardireccionlayout);
        etxtNumero2 = (EditText) findViewById(R.id.numero2_etxt_registrardireccionlayout);
        etxtDatosAdicionales = (EditText) findViewById(R.id.datosadicionales_etxt_registrardireccionlayout);
        etxtBarrio = (EditText) findViewById(R.id.barrio_etxt_registrardireccionlayout);

        mAuth = FirebaseAuth.getInstance();
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                firebaseUser = firebaseAuth.getCurrentUser();
            }
        };
    }
    private boolean isAlgunCampoFormularioDireccionVacio(){
        if(TextUtils.isEmpty(etxtPais.getText())){
            etxtPais.requestFocus();
            etxtPais.setError(getString(R.string.error_campo_requerido));
            return true;
        }
        if(TextUtils.isEmpty(etxtDepartamento.getText())){
            etxtDepartamento.requestFocus();
            etxtDepartamento.setError(getString(R.string.error_campo_requerido));
            return true;
        }
        if(TextUtils.isEmpty(etxtCiudadMunicipio.getText())){
            etxtCiudadMunicipio.requestFocus();
            etxtCiudadMunicipio.setError(getString(R.string.error_campo_requerido));
            return true;
        }
        if(TextUtils.isEmpty(etxtCarreraCalle.getText())){
            etxtCarreraCalle.requestFocus();
            etxtCarreraCalle.setError(getString(R.string.error_campo_requerido));
            return true;
        }

        if(TextUtils.isEmpty(etxtNumero1.getText())){
            etxtNumero1.requestFocus();
            etxtNumero1.setError(getString(R.string.error_campo_requerido));
            return true;
        }
        if(TextUtils.isEmpty(etxtNumero2.getText())){
            etxtNumero2.requestFocus();
            etxtNumero2.setError(getString(R.string.error_campo_requerido));
            return true;
        }
        if(TextUtils.isEmpty(etxtDatosAdicionales.getText())){
            etxtDatosAdicionales.requestFocus();
            etxtDatosAdicionales.setError(getString(R.string.error_campo_requerido));
            return true;
        }
        if(TextUtils.isEmpty(etxtBarrio.getText())){
            etxtBarrio.requestFocus();
            etxtBarrio.setError(getString(R.string.error_campo_requerido));
            return true;
        }
        return false;
    }

    /**
     * Este metodo se ejecuta en respuesta al clic del boto siguiente de esta activity
     * Es el encargado de registrar la informacion de la direccion ingresada por el usuario
     * @param view
     * Created by Fredy Mosquera Lemus on 8/02/17.
     */
    public void registrarDireccion(View view){
        if(!isAlgunCampoFormularioDireccionVacio()){
            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            DatabaseReference databaseReference = firebaseDatabase.getReference(UtilidadesFirebaseBD.getUrlInserccionDireccionesXUsuario(firebaseUser.getUid()));

            DireccionUsuario direccionUsuario = new DireccionUsuario();
            direccionUsuario.setKeyUidUsuario(firebaseUser.getUid());
            direccionUsuario.setPais(etxtPais.getText().toString().trim());
            direccionUsuario.setDepartamento(etxtDepartamento.getText().toString().trim());
            direccionUsuario.setCiudad(etxtCiudadMunicipio.getText().toString().trim());
            direccionUsuario.setCarreraCalle(etxtCarreraCalle.getText().toString().trim());
            direccionUsuario.setNumero1(etxtNumero1.getText().toString().trim());
            direccionUsuario.setNumero2(etxtNumero2.getText().toString().trim());
            direccionUsuario.setDatosAdicionales(etxtDatosAdicionales.getText().toString().trim());
            direccionUsuario.setBarrio(etxtBarrio.getText().toString());
            direccionUsuario.setFechaInsercion(UtilidadesFecha.convertirDateAString(new Date()));
            direccionUsuario.setFechaModificacion(null);
            databaseReference.setValue(direccionUsuario);
            abrirActivityHome();
        }
    }
    private void abrirActivityHome(){
        Intent intent = new Intent(this, RegistrarPerfilUsuarioActivity.class);
        startActivity(intent);

    }
    @Override
    public void onStart(){
        super.onStart();
        mAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    public void onStop(){
        super.onStop();
        if(mAuthStateListener != null){
            mAuth.removeAuthStateListener(mAuthStateListener);
        }
    }
}
