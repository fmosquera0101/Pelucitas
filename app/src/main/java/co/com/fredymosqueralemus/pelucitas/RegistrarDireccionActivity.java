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
import android.widget.LinearLayout;

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
    private Button btnEditarDireccionUsuario;
    private Button btnEditarDireccionMinegocio;
    private LinearLayout linearLayoutCancelarEditar;
    private LinearLayout linearLayoutRegistrarDireccion;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private FirebaseUser firebaseUser;
    private FirebaseDatabase firebaseDatabase;

    private Intent intent;
    private MiNegocio miNegocio;
    private Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_direccion);
        mAuth = FirebaseAuth.getInstance();
        intent = getIntent();
        SharedPreferencesSeguro sharedPreferencesSeguro = SharedPreferencesSeguroSingleton.getInstance(this, Constantes.SHARED_PREFERENCES_INFOUSUARIO, Constantes.SECURE_KEY_SHARED_PREFERENCES);
        firebaseDatabase = FirebaseDatabase.getInstance();
        inicializarViews();
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                firebaseUser = firebaseAuth.getCurrentUser();
            }
        };
        usuario = (Usuario) intent.getSerializableExtra(Constantes.USUARIO_OBJECT);
        miNegocio = (MiNegocio) intent.getSerializableExtra(Constantes.MINEGOCIO_OBJECT);
        if (AdministrarMiPerfilActivity.class.getName().equals(intent.getStringExtra(Constantes.CALL_FROM_ACTIVITY_ADMINISTRARMIPERFIL))) {
            setDisplayHomeAsUpEnabledAndBarTittle();
            linearLayoutCancelarEditar.setVisibility(View.VISIBLE);
            btnEditarDireccionUsuario.setVisibility(View.VISIBLE);
            settearViewsFromDireccion(usuario.getDireccion());
            if("S".equals(intent.getStringExtra(Constantes.SN_READONLY_INFORMACION_USUARIO))){
                settViewsDireccionReadOnly();
            }
        } else if (AdministrarMiNegocioActivity.class.getName().equals(intent.getStringExtra(Constantes.CALL_FROM_ACTIVITY_ADMINISTRARMINEGOCIO))) {
            setDisplayHomeAsUpEnabledAndBarTittle();
            settearViewsFromDirrecionFromFirebase();
            linearLayoutCancelarEditar.setVisibility(View.VISIBLE);
            btnEditarDireccionMinegocio.setVisibility(View.VISIBLE);

        } else {
            linearLayoutRegistrarDireccion.setVisibility(View.VISIBLE);
        }

    }

    private void settViewsDireccionReadOnly() {

        etxtPais.setEnabled(false);
        etxtDepartamento.setEnabled(false);
        etxtCiudadMunicipio.setEnabled(false);
        etxtCarreraCalle.setEnabled(false);
        etxtNumero1.setEnabled(false);
        etxtNumero2.setEnabled(false);
        etxtDatosAdicionales.setEnabled(false);
        etxtBarrio.setEnabled(false);
        btnRegistrarDireccion.setEnabled(false);
        linearLayoutCancelarEditar.setVisibility(View.GONE);

    }

    private void settearViewsFromDirrecionFromFirebase() {

        DatabaseReference databaseReference = firebaseDatabase.getReference(UtilidadesFirebaseBD.getUrlInserccionDireccionesXNegocio(miNegocio.getNitNegocio()));
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Direccion direccion = dataSnapshot.getValue(Direccion.class);
                settearViewsFromDireccion(direccion);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void setDisplayHomeAsUpEnabledAndBarTittle() {
        getSupportActionBar().setTitle(getString(R.string.str_editardireccion));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void settearViewsFromDireccion(Direccion direccion) {
        etxtPais.setText(direccion.getPais());
        etxtDepartamento.setText(direccion.getDepartamento());
        etxtCiudadMunicipio.setText(direccion.getCiudad());
        etxtCarreraCalle.setText(direccion.getCarreraCalle());
        etxtNumero1.setText(direccion.getNumero1());
        etxtNumero2.setText(direccion.getNumero2());
        etxtDatosAdicionales.setText(direccion.getDatosAdicionales());
        etxtBarrio.setText(direccion.getBarrio());
    }

    private boolean isAlgunCampoFormularioDireccionVacio() {
        if (TextUtils.isEmpty(etxtPais.getText())) {
            etxtPais.requestFocus();
            etxtPais.setError(getString(R.string.error_campo_requerido));
            return true;
        }
        if (TextUtils.isEmpty(etxtDepartamento.getText())) {
            etxtDepartamento.requestFocus();
            etxtDepartamento.setError(getString(R.string.error_campo_requerido));
            return true;
        }
        if (TextUtils.isEmpty(etxtCiudadMunicipio.getText())) {
            etxtCiudadMunicipio.requestFocus();
            etxtCiudadMunicipio.setError(getString(R.string.error_campo_requerido));
            return true;
        }
        if (TextUtils.isEmpty(etxtCarreraCalle.getText())) {
            etxtCarreraCalle.requestFocus();
            etxtCarreraCalle.setError(getString(R.string.error_campo_requerido));
            return true;
        }

        if (TextUtils.isEmpty(etxtNumero1.getText())) {
            etxtNumero1.requestFocus();
            etxtNumero1.setError(getString(R.string.error_campo_requerido));
            return true;
        }
        if (TextUtils.isEmpty(etxtNumero2.getText())) {
            etxtNumero2.requestFocus();
            etxtNumero2.setError(getString(R.string.error_campo_requerido));
            return true;
        }
        if (TextUtils.isEmpty(etxtDatosAdicionales.getText())) {
            etxtDatosAdicionales.requestFocus();
            etxtDatosAdicionales.setError(getString(R.string.error_campo_requerido));
            return true;
        }
        if (TextUtils.isEmpty(etxtBarrio.getText())) {
            etxtBarrio.requestFocus();
            etxtBarrio.setError(getString(R.string.error_campo_requerido));
            return true;
        }
        return false;
    }

    /**
     * Este metodo se ejecuta en respuesta al clic del boto siguiente de esta activity
     * Es el encargado de registrar la informacion de la direccion ingresada por el usuario
     *
     * @param view Created by Fredy Mosquera Lemus on 8/02/17.
     */
    public void registrarDireccion(View view) {
        if (!isAlgunCampoFormularioDireccionVacio()) {
            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            DatabaseReference databaseReference;

            if (RegistrarMiNegocioActivity.class.getName().equals(intent.getStringExtra(Constantes.CALL_FROM_ACTIVITY_REGISTRAR_MINEGOCIO))
                    || AdministrarMiNegocioActivity.class.getName().equals(intent.getStringExtra(Constantes.CALL_FROM_ACTIVITY_ADMINISTRARMINEGOCIO))) {
                String nitMiMegocio = miNegocio.getNitNegocio();
                Direccion direccionNegocio = getDireccionFromViews(new Direccion());
                direccionNegocio.setNitIdentificacionNegocio(nitMiMegocio);
                direccionNegocio.setFechaInsercion(UtilidadesFecha.convertirDateAString(new Date(), Constantes.FORMAT_DDMMYYYYHHMMSS));
                direccionNegocio.setFechaModificacion(null);
                databaseReference = firebaseDatabase.getReference(UtilidadesFirebaseBD.getUrlInserccionDireccionesXNegocio(nitMiMegocio));
                databaseReference.setValue(direccionNegocio);
                //miNegocio.setDireccion(direccionNegocio);

                if (AdministrarMiNegocioActivity.class.getName().equals(intent.getStringExtra(Constantes.CALL_FROM_ACTIVITY_ADMINISTRARMINEGOCIO))) {
                    miNegocio.setFechaModificacion(UtilidadesFecha.convertirDateAString(new Date(), Constantes.FORMAT_DDMMYYYYHHMMSS));
                    databaseReference = firebaseDatabase.getReference(UtilidadesFirebaseBD.getUrlInsercionMiNegocio(miNegocio.getUidAdministrador()));
                    databaseReference.child(miNegocio.getKeyChild()).setValue(miNegocio);
                    finish();

                } else {
                    abrirActivityRegistrarHorario();
                }
            } else {

                Direccion direccionUsuario = getDireccionFromViews(usuario.getDireccion());
                direccionUsuario.setKeyUidUsuario(firebaseUser.getUid());
                direccionUsuario.setFechaInsercion(UtilidadesFecha.convertirDateAString(new Date(), Constantes.FORMAT_DDMMYYYYHHMMSS));
                direccionUsuario.setFechaModificacion(null);
                databaseReference = firebaseDatabase.getReference(UtilidadesFirebaseBD.getUrlInserccionDireccionesXUsuario(firebaseUser.getUid()));
                databaseReference.setValue(direccionUsuario);

                usuario.setFechaModificacion(UtilidadesFecha.convertirDateAString(new Date(), Constantes.FORMAT_DDMMYYYYHHMMSS));
                usuario.setDireccion(direccionUsuario);

                databaseReference = firebaseDatabase.getReference(UtilidadesFirebaseBD.getUrlInserccionUsuario(firebaseUser.getUid()));

                databaseReference.setValue(usuario);
                abrirActivityRegistrarPerfil(usuario);
            }

        }
    }

    private Direccion getDireccionFromViews(Direccion direccion) {
        if (null == direccion) {
            direccion = new Direccion();
        }
        direccion.setPais(etxtPais.getText().toString().trim());
        direccion.setDepartamento(etxtDepartamento.getText().toString().trim());
        direccion.setCiudad(etxtCiudadMunicipio.getText().toString().trim());
        direccion.setCarreraCalle(etxtCarreraCalle.getText().toString().trim());
        direccion.setNumero1(etxtNumero1.getText().toString().trim());
        direccion.setNumero2(etxtNumero2.getText().toString().trim());
        direccion.setDatosAdicionales(etxtDatosAdicionales.getText().toString().trim());
        direccion.setBarrio(etxtBarrio.getText().toString());
        // direccion.setFechaInsercion(UtilidadesFecha.convertirDateAString(new Date()));
        // direccion.setFechaModificacion(null);

        return direccion;
    }

    private void abrirActivityRegistrarPerfil(Usuario usuario) {
        Intent intent = new Intent(this, RegistrarPerfilUsuarioActivity.class);
        intent.putExtra(Constantes.USUARIO_OBJECT, usuario);
        startActivity(intent);
        finish();

    }

    private void abrirActivityRegistrarHorario() {
        Intent intent = new Intent(this, RegistrarHorarioActivity.class);
        //intent.putExtra(Constantes.NIT_MINEGOCIO, inte.getStringExtra(Constantes.NIT_MINEGOCIO));
        intent.putExtra(Constantes.MINEGOCIO_OBJECT, miNegocio);
        startActivity(intent);
        finish();

    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthStateListener != null) {
            mAuth.removeAuthStateListener(mAuthStateListener);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int item = menuItem.getItemId();
        if (AdministrarMiNegocioActivity.class.getName().equals(intent.getStringExtra(Constantes.CALL_FROM_ACTIVITY_ADMINISTRARMINEGOCIO)) ||
                AdministrarMiPerfilActivity.class.getName().equals(intent.getStringExtra(Constantes.CALL_FROM_ACTIVITY_ADMINISTRARMIPERFIL))) {
            if (item == android.R.id.home) {
                onBackPressed();
            }
        }
        return super.onOptionsItemSelected(menuItem);
    }

    public void cancelarEdicionDireccion(View view) {
        finish();
    }

    public void editarDireccionUsuario(View view) {
        if (!isAlgunCampoFormularioDireccionVacio()) {
            Date fechaHoy = new Date();
            Direccion direccionUsuario = getDireccionFromViews(usuario.getDireccion());
            direccionUsuario.setKeyUidUsuario(firebaseUser.getUid());
            direccionUsuario.setFechaModificacion(UtilidadesFecha.convertirDateAString(fechaHoy, Constantes.FORMAT_DDMMYYYYHHMMSS));
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(UtilidadesFirebaseBD.getUrlInserccionDireccionesXUsuario(firebaseUser.getUid()));
            databaseReference.setValue(direccionUsuario);
            usuario.setFechaModificacion(UtilidadesFecha.convertirDateAString(fechaHoy, Constantes.FORMAT_DDMMYYYYHHMMSS));
            usuario.setDireccion(direccionUsuario);
            databaseReference = FirebaseDatabase.getInstance().getReference(UtilidadesFirebaseBD.getUrlInserccionUsuario(firebaseUser.getUid()));
            databaseReference.setValue(usuario);
            finish();
        }
    }

    public void editarDireccionMiNegocio(View view) {
        if (!isAlgunCampoFormularioDireccionVacio()) {
            Direccion direccionNegocio = getDireccionFromViews(new Direccion());
            direccionNegocio.setNitIdentificacionNegocio(miNegocio.getNitNegocio());
            direccionNegocio.setFechaModificacion(UtilidadesFecha.convertirDateAString(new Date(), Constantes.FORMAT_DDMMYYYYHHMMSS));
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(UtilidadesFirebaseBD.getUrlInserccionDireccionesXNegocio(miNegocio.getNitNegocio()));
            databaseReference.setValue(direccionNegocio);

            miNegocio.setDireccion(direccionNegocio);
            miNegocio.setFechaModificacion(UtilidadesFecha.convertirDateAString(new Date(), Constantes.FORMAT_DDMMYYYYHHMMSS));
            databaseReference = FirebaseDatabase.getInstance().getReference(UtilidadesFirebaseBD.getUrlInsercionMiNegocio(miNegocio.getUidAdministrador()));
            databaseReference.setValue(miNegocio);

            finish();
        }
    }

    private void inicializarViews() {
        etxtPais = (EditText) findViewById(R.id.pais_etxt_registrardireccionlayout);
        etxtDepartamento = (EditText) findViewById(R.id.departamento_etxt_registrardireccionlayout);
        etxtCiudadMunicipio = (EditText) findViewById(R.id.ciudadmunicipio_etxt_registrardireccionlayout);
        etxtCarreraCalle = (EditText) findViewById(R.id.carreracalle_etxt_registrardireccionlayout);
        etxtNumero1 = (EditText) findViewById(R.id.numero1_etxt_registrardireccionlayout);
        etxtNumero2 = (EditText) findViewById(R.id.numero2_etxt_registrardireccionlayout);
        etxtDatosAdicionales = (EditText) findViewById(R.id.datosadicionales_etxt_registrardireccionlayout);
        etxtBarrio = (EditText) findViewById(R.id.barrio_etxt_registrardireccionlayout);
        btnRegistrarDireccion = (Button) findViewById(R.id.siguiente_btn_registrardireccionlayout);
        linearLayoutCancelarEditar = (LinearLayout) findViewById(R.id.linear_layout_editar_cancelar_registrardireccionlayout);
        btnEditarDireccionUsuario = (Button) findViewById(R.id.btn_editardireccionusuairo_registrardireccionlayout);
        btnEditarDireccionMinegocio = (Button) findViewById(R.id.btn_editardireccionminegocio_registrardireccionlayout);
        linearLayoutRegistrarDireccion = (LinearLayout) findViewById(R.id.linear_layout_registrardireccion_registrardireccionlayout);

    }
}
