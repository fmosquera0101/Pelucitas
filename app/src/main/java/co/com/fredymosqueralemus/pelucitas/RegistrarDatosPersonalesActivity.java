package co.com.fredymosqueralemus.pelucitas;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Date;

import co.com.fredymosqueralemus.pelucitas.constantes.Constantes;
import co.com.fredymosqueralemus.pelucitas.modelo.usuario.Usuario;
import co.com.fredymosqueralemus.pelucitas.utilidades.UtilidadesFecha;
import co.com.fredymosqueralemus.pelucitas.utilidades.UtilidadesFirebaseBD;

/**
 * Created by Fredy Mosquera Lemus on 2/02/17.
 */

public class RegistrarDatosPersonalesActivity extends AppCompatActivity {

    private EditText etxtCedulaIdentificacion;
    private EditText etxtNombre;
    private EditText etxtApellidos;
    private EditText etxtTelefono;
    private EditText etxtFechaNacimiento;
    private LinearLayout linearLayoutEditarCancelarEdicion;
    private LinearLayout linearLayoutRegistrarInfoUsuairo;

    private Button btnCancelar;
    private Button btnEditarInfoUsuario;
    private Button btnRegistrarInfoUsuairo;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private FirebaseUser firebaseUser;

    private Intent intent;
    private Usuario usuario;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_datos_personales);
        mAuth = FirebaseAuth.getInstance();
        intent = getIntent();

        iniciarlizarViews();
        mostrarDatePickerEditTextFechaNacimiento();

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                firebaseUser = firebaseAuth.getCurrentUser();
            }
        };

        if(AdministrarMiPerfilActivity.class.getName().equals(intent.getStringExtra(Constantes.CALL_FROM_ACTIVITY_ADMINISTRARMIPERFIL))){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(getString(R.string.str_editardatospersonales));
            linearLayoutEditarCancelarEdicion.setVisibility(View.VISIBLE);
            usuario = (Usuario) intent.getSerializableExtra(Constantes.USUARIO_OBJECT);
            settearViewsFromUsuario(usuario);
        }else {
            linearLayoutRegistrarInfoUsuairo.setVisibility(View.VISIBLE);
        }
    }

    private void iniciarlizarViews(){
        etxtCedulaIdentificacion = (EditText) findViewById(R.id.cedula_ext_registrardatospersonaleslayout);
        etxtNombre = (EditText) findViewById(R.id.nombre_ext_registrardatospersonaleslayout);
        etxtApellidos = (EditText) findViewById(R.id.apellidos_ext_registrardatospersonaleslayout);
        etxtTelefono = (EditText) findViewById(R.id.telefono_ext_registrardatospersonaleslayout);
        etxtFechaNacimiento = (EditText) findViewById(R.id.fechanacimiento_ext_registrardatospersonaleslayout);
        linearLayoutEditarCancelarEdicion = (LinearLayout) findViewById(R.id.linear_layout_editar_cancelar_registrardatospersonaleslayout);
        linearLayoutRegistrarInfoUsuairo = (LinearLayout) findViewById(R.id.linear_layout_registrarinfousuario_registrardatospersonaleslayout);
        btnCancelar = (Button) findViewById(R.id.btn_cancelar_registrardatospersonaleslayout);
        btnEditarInfoUsuario = (Button) findViewById(R.id.btn_editar_registrardatospersonaleslayout);
        btnRegistrarInfoUsuairo = (Button) findViewById(R.id.siguiente_btn_registrardatospersonaleslayout);
    }

    public void registrarDatosPersonales(View view){

        if(!isAlgunCampoDatosPersonalesVacio()) {
            DatabaseReference databaseReference = getDatabaseReference(firebaseUser.getUid());
            usuario = new Usuario();
            usuario = getUsuarioFromViews(usuario);
            usuario.setFechaInsercion(UtilidadesFecha.convertirDateAString(new Date()));
            usuario.setFechaModificacion(null);
            databaseReference.setValue(usuario);
            abrirActivityRegistrarDireccion(usuario);
        }

    }
    public void editarInformacionUsuario(View view){
        if(!isAlgunCampoDatosPersonalesVacio()) {
            DatabaseReference databaseReference = getDatabaseReference(firebaseUser.getUid());
            usuario = getUsuarioFromViews(usuario);
            usuario.setFechaModificacion(UtilidadesFecha.convertirDateAString(new Date()));
            databaseReference.setValue(usuario);
            finish();
        }

    }
    private DatabaseReference getDatabaseReference(String userId){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference(UtilidadesFirebaseBD.getUrlInserccionUsuario(userId));
        return databaseReference;
    }
    public void cancelarEdicionInfoUsuario(View view){
        finish();
    }
    private void settearViewsFromUsuario(Usuario usuario){
        etxtCedulaIdentificacion.setText(usuario.getCedulaIdentificacion());
        etxtNombre.setText(usuario.getNombre());
        etxtApellidos.setText(usuario.getApellidos());
        etxtTelefono.setText(usuario.getTelefono() != null && !"".equals(usuario.getTelefono()) ? usuario.getTelefono():"");
        etxtFechaNacimiento.setText(usuario.getFechaNacimiento());

    }
    private Usuario getUsuarioFromViews(Usuario miUsuario){
        miUsuario.setKeyUid(firebaseUser.getUid());
        miUsuario.setCedulaIdentificacion(etxtCedulaIdentificacion.getText().toString().trim());
        miUsuario.setNombre(etxtNombre.getText().toString().trim());
        miUsuario.setApellidos(etxtApellidos.getText().toString().trim());
        miUsuario.setTelefono(etxtTelefono.getText() != null ? etxtTelefono.getText().toString().trim() : "");
        miUsuario.setFechaNacimiento(etxtFechaNacimiento.getText().toString());

        return miUsuario;
    }
    public void abrirActivityRegistrarDireccion(Usuario usuario){
        Intent mIntent = new Intent(this, RegistrarDireccionActivity.class);
        mIntent.putExtra(Constantes.USUARIO_OBJECT, usuario);
        startActivity(mIntent);
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

    private boolean isAlgunCampoDatosPersonalesVacio(){

        if(TextUtils.isEmpty(etxtCedulaIdentificacion.getText())){
            etxtCedulaIdentificacion.requestFocus();
            etxtCedulaIdentificacion.setError(getString(R.string.error_campo_requerido));
            return true;
        }

        if(TextUtils.isEmpty(etxtNombre.getText())){
            etxtNombre.requestFocus();
            etxtNombre.setError(getString(R.string.error_campo_requerido));
            return true;
        }

        if(TextUtils.isEmpty(etxtApellidos.getText())){
            etxtApellidos.requestFocus();
            etxtApellidos.setError(getString(R.string.error_campo_requerido));
            return true;
        }
        if(!TextUtils.isEmpty(etxtTelefono.getText())){
            int tamanoTelefono = etxtTelefono.getText().length();
            if(tamanoTelefono < Constantes.NUMERO6 || tamanoTelefono > Constantes.NUMERO10){
                etxtTelefono.setError(getString(R.string.error_numerotelefononovalido));
                return true;
            }
        }

        if(TextUtils.isEmpty(etxtFechaNacimiento.getText()) ){
            etxtFechaNacimiento.requestFocus();
            etxtFechaNacimiento.setError(getString(R.string.error_campo_requerido));
            return true;
        }else if(!esfechaNacimientoValida(UtilidadesFecha.convertirStringADate(etxtFechaNacimiento.getText().toString()))){
            etxtFechaNacimiento.requestFocus();
            etxtFechaNacimiento.setError(getString(R.string.error_fechanacimiento_novalida));
            return true;
        }
        /*
        else if(!UtilidadesFecha.isFechaddmmyyyy(etxtFechaNacimiento.getText().toString())){
            etxtFechaNacimiento.requestFocus();
            etxtFechaNacimiento.setError(getString(R.string.error_fechanacimiento_novalida));
            return true;
        }
         */




        return false;
    }

    /**
     * Metodo encargado de mostrar el DatePicker en el campo EditText de la fecha de nacimiento
     * Created by Fredy Mosquera Lemus on 7/02/17.
     */
    private void mostrarDatePickerEditTextFechaNacimiento(){
        final Calendar calendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                actualizarEditTextFechaNacimiento(calendar);
            }
        };

        etxtFechaNacimiento.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    new DatePickerDialog(RegistrarDatosPersonalesActivity.this, dateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
                    etxtFechaNacimiento.clearFocus();
                }
            }
        });

    }
    private void actualizarEditTextFechaNacimiento(Calendar calendar) {
        etxtFechaNacimiento.setText(UtilidadesFecha.convertirDateAString(calendar.getTime()));
    }

    /**
     * Este metodo valida si la fecha de nacimiento ingresada es valida, esto es, no puede ser igual
     * a la fecha de hoy, y no puede ser mayor a la fecha actual
     * TODO: se requiere validar que el usuario ingresado sea mayor de edad
     * @param fechaNacimiento
     * @return
     * Created by Fredy Mosquera Lemus on 7/02/17.
     */
    private boolean esfechaNacimientoValida(Date fechaNacimiento){
        boolean isFechaValida = true;
        Date fechaHoy = UtilidadesFecha.formatearDate(new Date());
        if (fechaNacimiento.equals(fechaHoy) || fechaNacimiento.after(fechaHoy)){
            return false;
        }
        return isFechaValida;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem){
        int item = menuItem.getItemId();
        if(item == android.R.id.home){
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }

}
