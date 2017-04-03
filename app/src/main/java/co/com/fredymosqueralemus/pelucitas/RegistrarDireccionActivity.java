package co.com.fredymosqueralemus.pelucitas;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;

import co.com.fredymosqueralemus.pelucitas.constantes.Constantes;
import co.com.fredymosqueralemus.pelucitas.direccion.Direccion;
import co.com.fredymosqueralemus.pelucitas.modelo.minegocio.MiNegocio;
import co.com.fredymosqueralemus.pelucitas.modelo.usuario.Usuario;
import co.com.fredymosqueralemus.pelucitas.sharedpreference.SharedPreferencesSeguro;
import co.com.fredymosqueralemus.pelucitas.sharedpreference.SharedPreferencesSeguroSingleton;
import co.com.fredymosqueralemus.pelucitas.utilidades.UtilidadesFecha;
import co.com.fredymosqueralemus.pelucitas.utilidades.UtilidadesFirebaseBD;
import co.com.fredymosqueralemus.pelucitas.utilidades.UtilidadesImagenes;

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
    private Button btnRegistrarDireccion;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private FirebaseUser firebaseUser;

    private Intent intent;
    private MiNegocio miNegocio;
    SharedPreferencesSeguro sharedPreferencesSeguro;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_direccion);

        intent = getIntent();
        miNegocio = (MiNegocio)intent.getSerializableExtra(Constantes.MINEGOCIOOBJECT);
        sharedPreferencesSeguro = SharedPreferencesSeguroSingleton.getInstance(this, Constantes.SHARED_PREFERENCES_INFOUSUARIO, Constantes.SECURE_KEY_SHARED_PREFERENCES);
        etxtPais = (EditText) findViewById(R.id.pais_etxt_registrardireccionlayout);
        etxtDepartamento = (EditText) findViewById(R.id.departamento_etxt_registrardireccionlayout);
        etxtCiudadMunicipio = (EditText) findViewById(R.id.ciudadmunicipio_etxt_registrardireccionlayout);
        etxtCarreraCalle = (EditText) findViewById(R.id.carreracalle_etxt_registrardireccionlayout);
        etxtNumero1 = (EditText) findViewById(R.id.numero1_etxt_registrardireccionlayout);
        etxtNumero2 = (EditText) findViewById(R.id.numero2_etxt_registrardireccionlayout);
        etxtDatosAdicionales = (EditText) findViewById(R.id.datosadicionales_etxt_registrardireccionlayout);
        etxtBarrio = (EditText) findViewById(R.id.barrio_etxt_registrardireccionlayout);
        btnRegistrarDireccion = (Button) findViewById(R.id.siguiente_btn_registrardireccionlayout);
        mAuth = FirebaseAuth.getInstance();
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                firebaseUser = firebaseAuth.getCurrentUser();
            }
        };
        if(AdministrarMiNegocioActivity.class.getName().equals(intent.getStringExtra(Constantes.CALL_FROM_ACTIVITY_ADMINISTRARMINEGOCIO))){
            btnRegistrarDireccion.setText(getString(R.string.str_editar));
            getSupportActionBar().setTitle(getString(R.string.str_editardireccion));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            settearInformacionDireccion();
        }

    }
    private void settearInformacionDireccion(){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child(Constantes.MINEGOCIO_FIREBASE_BD).child(sharedPreferencesSeguro.getString(Constantes.USERUID)).child(miNegocio.getKeyChild()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                MiNegocio miNegocio = dataSnapshot.getValue(MiNegocio.class);
                Direccion direccion = miNegocio.getDireccion();
                etxtPais.setText(direccion.getPais());
                etxtDepartamento.setText(direccion.getDepartamento());
                etxtCiudadMunicipio.setText(direccion.getCiudad());
                etxtCarreraCalle.setText(direccion.getCarreraCalle());
                etxtNumero1.setText(direccion.getNumero1());
                etxtNumero2.setText(direccion.getNumero2());
                etxtDatosAdicionales.setText(direccion.getDatosAdicionales());
                etxtBarrio.setText(direccion.getBarrio());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

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
            DatabaseReference databaseReference;
            if(RegistrarMiNegocioActivity.class.getName().equals(intent.getStringExtra(Constantes.CALL_FROM_ACTIVITY_REGISTRAR_MINEGOCIO))
                    || AdministrarMiNegocioActivity.class.getName().equals(intent.getStringExtra(Constantes.CALL_FROM_ACTIVITY_ADMINISTRARMINEGOCIO))){
                String nitMiMegocio = miNegocio.getNitNegocio();
                Direccion direccionNegocio = getDireccion();
                direccionNegocio.setNitIdentificacionNegocio(nitMiMegocio);
                direccionNegocio.setFechaInsercion(UtilidadesFecha.convertirDateAString(new Date()));
                direccionNegocio.setFechaModificacion(null);
                databaseReference = firebaseDatabase.getReference(UtilidadesFirebaseBD.getUrlInserccionDireccionesXNegocio(nitMiMegocio));
                databaseReference.setValue(direccionNegocio);
                miNegocio.setDireccion(direccionNegocio);
                if(AdministrarMiNegocioActivity.class.getName().equals(intent.getStringExtra(Constantes.CALL_FROM_ACTIVITY_ADMINISTRARMINEGOCIO))){
                    miNegocio.setFechaModificacion(UtilidadesFecha.convertirDateAString(new Date()));
                    databaseReference = firebaseDatabase.getReference(UtilidadesFirebaseBD.getUrlInsercionMiNegocio(miNegocio.getUidAdministrador()));
                    databaseReference.child(miNegocio.getKeyChild()).setValue(miNegocio);
                    finish();

                }else {
                    abrirActivityRegistrarHorario();
                }
            }else{

                Usuario usuario = (Usuario) intent.getSerializableExtra(Constantes.USUARIO_OBJECT);
                databaseReference = firebaseDatabase.getReference(UtilidadesFirebaseBD.getUrlInserccionUsuario(usuario.getKeyUid()));

                Direccion direccionUsuario = getDireccion();
                direccionUsuario.setKeyUidUsuario(firebaseUser.getUid());
                direccionUsuario.setFechaInsercion(UtilidadesFecha.convertirDateAString(new Date()));
                direccionUsuario.setFechaModificacion(null);
                databaseReference = firebaseDatabase.getReference(UtilidadesFirebaseBD.getUrlInserccionDireccionesXUsuario(firebaseUser.getUid()));
                databaseReference.setValue(direccionUsuario);

                usuario.setFechaModificacion(UtilidadesFecha.convertirDateAString(new Date()));
                usuario.setDireccion(direccionUsuario);

                databaseReference = firebaseDatabase.getReference(UtilidadesFirebaseBD.getUrlInserccionUsuario(firebaseUser.getUid()));

                databaseReference.setValue(usuario);
                abrirActivityRegistrarPerfil(usuario);
            }

        }
    }
    private Direccion getDireccion(){
        Direccion direccion = new Direccion();
        direccion.setPais(etxtPais.getText().toString().trim());
        direccion.setDepartamento(etxtDepartamento.getText().toString().trim());
        direccion.setCiudad(etxtCiudadMunicipio.getText().toString().trim());
        direccion.setCarreraCalle(etxtCarreraCalle.getText().toString().trim());
        direccion.setNumero1(etxtNumero1.getText().toString().trim());
        direccion.setNumero2(etxtNumero2.getText().toString().trim());
        direccion.setDatosAdicionales(etxtDatosAdicionales.getText().toString().trim());
        direccion.setBarrio(etxtBarrio.getText().toString());
        direccion.setFechaInsercion(UtilidadesFecha.convertirDateAString(new Date()));
        direccion.setFechaModificacion(null);

        return direccion;
    }
    private void abrirActivityRegistrarPerfil(Usuario usuario){
        Intent intent = new Intent(this, RegistrarPerfilUsuarioActivity.class);
        intent.putExtra(Constantes.USUARIO_OBJECT, usuario);
        startActivity(intent);
        finish();

    }
    private void abrirActivityRegistrarHorario(){
        Intent intent = new Intent(this, RegistrarHorarioActivity.class);
        //intent.putExtra(Constantes.NIT_MINEGOCIO, inte.getStringExtra(Constantes.NIT_MINEGOCIO));
        intent.putExtra(Constantes.MINEGOCIOOBJECT, miNegocio);
        startActivity(intent);
        finish();

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
