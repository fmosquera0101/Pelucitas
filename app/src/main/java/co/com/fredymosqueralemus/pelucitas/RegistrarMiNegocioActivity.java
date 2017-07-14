package co.com.fredymosqueralemus.pelucitas;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;

import co.com.fredymosqueralemus.pelucitas.constantes.Constantes;
import co.com.fredymosqueralemus.pelucitas.modelo.minegocio.MiNegocio;
import co.com.fredymosqueralemus.pelucitas.modelo.minegocio.NegocioXAdministrador;
import co.com.fredymosqueralemus.pelucitas.modelo.minegocio.TipoNegocio;
import co.com.fredymosqueralemus.pelucitas.sharedpreference.SharedPreferencesSeguro;
import co.com.fredymosqueralemus.pelucitas.sharedpreference.SharedPreferencesSeguroSingleton;
import co.com.fredymosqueralemus.pelucitas.utilidades.UtilidadesFecha;
import co.com.fredymosqueralemus.pelucitas.utilidades.UtilidadesFirebaseBD;

public class RegistrarMiNegocioActivity extends AppCompatActivity {

    private EditText etxtNitNegocio;
    private EditText etxtNombreNegocio;
    private EditText etxtDescripcionMiNegocio;
    private EditText etxtTelefono;
    private Spinner spnTipoNegocio;
    private Button btnRegistrarInformacionMiNegocio;

    private LinearLayout linearLayoutBtnEditarInformacionMiNegocio;
    private LinearLayout linearLayoutBtnRegistrarInforamcionMiNegocio;

    private String[] arrayTiposNegocios;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private FirebaseUser mFirebaseUser;
    private FirebaseDatabase firebaseDatabase;

    private Intent intent;
    private Context context;

    private MiNegocio miNegocio;
    private SharedPreferencesSeguro sharedPreferencesSeguro;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_mi_negocio);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        firebaseDatabase = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        inicializarCurrentUser();
        intent = getIntent();
        context = this;
        sharedPreferencesSeguro = SharedPreferencesSeguroSingleton.getInstance(context, Constantes.SHARED_PREFERENCES_INFOUSUARIO, Constantes.SECURE_KEY_SHARED_PREFERENCES);
        inicializarViews();

        if (vaAEditarInformacionMiNegocio()) {
            getSupportActionBar().setTitle(R.string.titulo_editarminegocio);
            miNegocio = (MiNegocio) intent.getSerializableExtra(Constantes.MINEGOCIO_OBJECT);
            settearInfoEnViewsParaEditar(miNegocio);
        }


    }

    public static void settearTiposNegocios(Spinner spnTipoNegocio, Context context) {
        ArrayAdapter<CharSequence> arrayAdapterTiposNegocio = ArrayAdapter.createFromResource(context, R.array.arraystr_tiposnegocio, android.R.layout.simple_spinner_item);
        arrayAdapterTiposNegocio.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnTipoNegocio.setAdapter(arrayAdapterTiposNegocio);
    }

    public static int getTipoNegocioSeleccionado(String[] arrayTiposNegocios, MiNegocio miNegocio) {
        int i = 0;
        for (String strTipo : arrayTiposNegocios) {
            if (strTipo.equals(miNegocio.getTipoNegocio().getTipoNegocio())) {
                return i;
            }
            i++;
        }
        return i;
    }

    public void registrarInformacionMiNegocio(View view) {
        if (!isAlgunCampoFormularioDireccionVacio()) {
            miNegocio = new MiNegocio();
            miNegocio = getMiNegocioFromViews(miNegocio);
            miNegocio.setFechaInsercion(UtilidadesFecha.convertirDateAString(new Date(), Constantes.FORMAT_DDMMYYYYHHMMSS));
            miNegocio.setUidAdministrador(mFirebaseUser.getUid());

            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(UtilidadesFirebaseBD.getUrlInsercionMiNegocio(miNegocio.getNitNegocio()));
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    MiNegocio miNegocioFromFirebase = dataSnapshot.getValue(MiNegocio.class);
                    if (null == miNegocioFromFirebase || !miNegocio.getNitNegocio().equals(miNegocioFromFirebase.getNitNegocio())) {
                        TipoNegocio tipoNegocio = new TipoNegocio();
                        tipoNegocio = getTipoNegocioFromViews(tipoNegocio);
                        tipoNegocio.setFechaInsercion(UtilidadesFecha.convertirDateAString(new Date(), Constantes.FORMAT_DDMMYYYYHHMMSS));
                        insertarTipoNegocio(tipoNegocio);

                        NegocioXAdministrador negocioXAdministrador = getNegocioXAdministrador(miNegocio);
                        insertarNegocioXAdministrador(negocioXAdministrador);

                        miNegocio.setTipoNegocio(tipoNegocio);
                        insertarMiNegocio(miNegocio);

                        abrirActivityRegistrarDireccionMiNegocio(miNegocio);

                    } else {
                        Toast.makeText(context, getString(R.string.str_mensaje_error_nityaingresado), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }
    }

    public void cancelarEdicionMiNegocio(View view) {
        finish();
    }

    public void editarInformacionMiNegocio(View view) {
        if (!isAlgunCampoFormularioDireccionVacio()) {
            MiNegocio miNegocioEdicion = getMiNegocioFromViews(miNegocio);
            miNegocioEdicion.setFechaModificacion(UtilidadesFecha.convertirDateAString(new Date(), Constantes.FORMAT_DDMMYYYYHHMMSS));

            TipoNegocio tipoNegocio = miNegocioEdicion.getTipoNegocio();
            tipoNegocio = getTipoNegocioFromViews(tipoNegocio);
            tipoNegocio.setFechaModificacion(UtilidadesFecha.convertirDateAString(new Date(), Constantes.FORMAT_DDMMYYYYHHMMSS));
            insertarTipoNegocio(tipoNegocio);

            miNegocioEdicion.setTipoNegocio(tipoNegocio);
            insertarMiNegocio(miNegocioEdicion);

            finish();
        }
    }

    private MiNegocio getMiNegocioFromViews(MiNegocio miNegocio) {
        miNegocio.setNitNegocio(etxtNitNegocio.getText().toString().trim());
        miNegocio.setNombreNegocio(etxtNombreNegocio.getText().toString().trim());
        miNegocio.setTelefonoNegocio(etxtTelefono.getText().toString().trim());
        if (null != etxtDescripcionMiNegocio.getText()) {
            miNegocio.setDescripcionNegocio(etxtDescripcionMiNegocio.getText().toString().trim());
        } else {
            miNegocio.setDescripcionNegocio("");
        }
        return miNegocio;
    }

    private TipoNegocio getTipoNegocioFromViews(TipoNegocio tipoNegocio) {
        tipoNegocio.setTipoNegocio(spnTipoNegocio.getSelectedItem().toString());
        tipoNegocio.setNitNegocio(miNegocio.getNitNegocio());

        return tipoNegocio;
    }

    private NegocioXAdministrador getNegocioXAdministrador(MiNegocio miNegocio) {
        NegocioXAdministrador negocioXAdministrador = new NegocioXAdministrador();
        negocioXAdministrador.setNitNegocio(miNegocio.getNitNegocio());
        negocioXAdministrador.setFechaInsercion(UtilidadesFecha.convertirDateAString(new Date(), Constantes.FORMAT_DDMMYYYYHHMMSS));
        return negocioXAdministrador;
    }

    private void insertarMiNegocio(MiNegocio miNegocio) {
        DatabaseReference databaseReference = firebaseDatabase.getReference(UtilidadesFirebaseBD.getUrlInsercionMiNegocio(miNegocio.getNitNegocio()));
        databaseReference.setValue(miNegocio);

    }

    private void insertarTipoNegocio(TipoNegocio tipoNegocio) {
        DatabaseReference databaseReference;
        if (tipoNegocio.getTipoNegocio().equals(arrayTiposNegocios[1])) {
            databaseReference = firebaseDatabase.getReference(UtilidadesFirebaseBD.getUrlInsercionTiposNegocio(Constantes.TIPOS_NEGOCIOS_BARBERIA_FIREBASE_BD, tipoNegocio.getNitNegocio()));
            databaseReference.setValue(tipoNegocio);
        } else if (tipoNegocio.getTipoNegocio().equals(arrayTiposNegocios[2])) {
            databaseReference = firebaseDatabase.getReference(UtilidadesFirebaseBD.getUrlInsercionTiposNegocio(Constantes.TIPOS_NEGOCIOS_PELUQUERIA_FIREBASE_BD, tipoNegocio.getNitNegocio()));
            databaseReference.setValue(tipoNegocio);
        } else if (tipoNegocio.getTipoNegocio().equals(arrayTiposNegocios[3])) {
            databaseReference = firebaseDatabase.getReference(UtilidadesFirebaseBD.getUrlInsercionTiposNegocio(Constantes.TIPOS_NEGOCIOS_SALONESDEBELLEZA_FIREBASE_BD, tipoNegocio.getNitNegocio()));
            databaseReference.setValue(tipoNegocio);
        }
    }

    private void insertarNegocioXAdministrador(NegocioXAdministrador negocioXAdministrador) {
        DatabaseReference databaseReferenceInsertarMiNegocioXAdmon = firebaseDatabase.getReference(UtilidadesFirebaseBD.getUrlInsercionMiNegocioXAdmon(sharedPreferencesSeguro.getString(Constantes.USERUID)));
        databaseReferenceInsertarMiNegocioXAdmon.child(negocioXAdministrador.getNitNegocio()).setValue(negocioXAdministrador);
    }


    private void inicializarViews() {
        etxtNitNegocio = (EditText) findViewById(R.id.nit_negocio_etxt_registrarminegocio);
        etxtNombreNegocio = (EditText) findViewById(R.id.nombre_negocio_etxt_registrarminegocio);
        etxtTelefono = (EditText) findViewById(R.id.telefono_etxt_registrarminegocio);
        etxtDescripcionMiNegocio = (EditText) findViewById(R.id.descripcion_negocio_etxt_registrarminegocio);
        spnTipoNegocio = (Spinner) findViewById(R.id.tipo_negocio_spn_registrardatospersonaleslayout);
        arrayTiposNegocios = getResources().getStringArray(R.array.arraystr_tiposnegocio);
        btnRegistrarInformacionMiNegocio = (Button) findViewById(R.id.siguiente_btn_registrarminegocio);

        linearLayoutBtnEditarInformacionMiNegocio = (LinearLayout) findViewById(R.id.linear_layout_editarinforamcionminegocio);
        linearLayoutBtnRegistrarInforamcionMiNegocio = (LinearLayout) findViewById(R.id.linear_layout_registrarinformacionminegocio);

        settearTiposNegocios(spnTipoNegocio, this);
    }

    private void settearInfoEnViewsParaEditar(MiNegocio miNegocio) {
        linearLayoutBtnEditarInformacionMiNegocio.setVisibility(View.VISIBLE);
        linearLayoutBtnRegistrarInforamcionMiNegocio.setVisibility(View.GONE);
        etxtNitNegocio.setEnabled(false);
        etxtNitNegocio.setText(miNegocio.getNitNegocio());
        etxtNombreNegocio.setText(miNegocio.getNombreNegocio());
        etxtTelefono.setText(miNegocio.getTelefonoNegocio());
        etxtDescripcionMiNegocio.setText(null != miNegocio.getDescripcionNegocio() ? miNegocio.getDescripcionNegocio() : "");
        spnTipoNegocio.setSelection(getTipoNegocioSeleccionado(arrayTiposNegocios, miNegocio));
    }

    private boolean isAlgunCampoFormularioDireccionVacio() {
        if (TextUtils.isEmpty(etxtNitNegocio.getText())) {
            etxtNitNegocio.requestFocus();
            etxtNitNegocio.setError(getString(R.string.error_campo_requerido));
            return true;
        }
        if (TextUtils.isEmpty(etxtNombreNegocio.getText())) {
            etxtNombreNegocio.requestFocus();
            etxtNombreNegocio.setError(getString(R.string.error_campo_requerido));
            return true;
        }
        if (TextUtils.isEmpty(etxtTelefono.getText())) {
            etxtTelefono.requestFocus();
            etxtTelefono.setError(getString(R.string.error_campo_requerido));
            return true;
        }
        if (spnTipoNegocio.getSelectedItem().equals(arrayTiposNegocios[0])) {
            ((TextView) spnTipoNegocio.getSelectedView()).setError(getString(R.string.error_campo_requerido));
            return true;
        }
        return false;
    }

    private boolean vaAEditarInformacionMiNegocio() {
        return EditarInforamcionMiNegocioActivity.class.getName().equals(intent.getStringExtra(Constantes.CALL_FROM_ACTIVITY_ADMINISTRARMINEGOCIO));
    }

    private void inicializarCurrentUser() {
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                mFirebaseUser = firebaseAuth.getCurrentUser();
            }
        };
    }

    private void abrirActivityRegistrarDireccionMiNegocio(MiNegocio miNegocio) {
        Intent intent = new Intent(this, RegistrarDireccionActivity.class);
        intent.putExtra(Constantes.CALL_FROM_ACTIVITY_REGISTRAR_MINEGOCIO, RegistrarMiNegocioActivity.class.getName());
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
        if (null != mAuthStateListener) {
            mAuth.removeAuthStateListener(mAuthStateListener);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int item = menuItem.getItemId();
        if (item == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }
}
