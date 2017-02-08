package co.com.fredymosqueralemus.pelucitas;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

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
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private FirebaseUser firebaseUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_datos_personales);

        etxtCedulaIdentificacion = (EditText) findViewById(R.id.cedula_ext_registrardatospersonaleslayout);
        etxtNombre = (EditText) findViewById(R.id.nombre_ext_registrardatospersonaleslayout);
        etxtApellidos = (EditText) findViewById(R.id.apellidos_ext_registrardatospersonaleslayout);
        etxtTelefono = (EditText) findViewById(R.id.telefono_ext_registrardatospersonaleslayout);
        etxtFechaNacimiento = (EditText) findViewById(R.id.fechanacimiento_ext_registrardatospersonaleslayout);
        mostrarDatePickerEditTextFechaNacimiento();
        mAuth = FirebaseAuth.getInstance();
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                firebaseUser = firebaseAuth.getCurrentUser();
            }
        };
    }

    public void registrarDatosPersonales(View view){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

        DatabaseReference databaseReference = firebaseDatabase.getReference(UtilidadesFirebaseBD.getUrlInserccionUsuario(firebaseUser.getUid()));
        if(!isAlgunCampoDatosPersonalesVacio()) {
            Usuario mUsuario = new Usuario();
            mUsuario.setKeyUid(firebaseUser.getUid());
            mUsuario.setCedulaIdentificacion(etxtCedulaIdentificacion.getText().toString());
            mUsuario.setNombre(etxtNombre.getText().toString().trim());
            mUsuario.setApellidos(etxtApellidos.getText().toString().trim());
            mUsuario.setTelefono(etxtTelefono.getText() != null ? etxtTelefono.getText().toString().trim() : "-");
            mUsuario.setFechaNacimiento(etxtFechaNacimiento.getText().toString());
            mUsuario.setFechaInsercion(UtilidadesFecha.convertirDateAString(new Date()));
            mUsuario.setFechaModificacion(null);

            databaseReference.setValue(mUsuario);

            abrirActivityRegistrarDireccion();
        }

    }
    public void abrirActivityRegistrarDireccion(){
        Intent mIntent = new Intent(this, RegistrarDireccionActivity.class);
        startActivity(mIntent);
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
        boolean isCamposVacio = false;
        if(TextUtils.isEmpty(etxtCedulaIdentificacion.getText())){
            etxtCedulaIdentificacion.requestFocus();
            etxtCedulaIdentificacion.setError(getString(R.string.error_campo_requerido));
            isCamposVacio = true;
        }else if(TextUtils.isEmpty(etxtNombre.getText())){
            etxtNombre.requestFocus();
            etxtNombre.setError(getString(R.string.error_campo_requerido));
            isCamposVacio = true;
        }else if(TextUtils.isEmpty(etxtApellidos.getText())){
            etxtApellidos.requestFocus();
            etxtApellidos.setError(getString(R.string.error_campo_requerido));
            isCamposVacio = true;
        }else if(TextUtils.isEmpty(etxtTelefono.getText())){
            etxtTelefono.requestFocus();
            etxtTelefono.setError(getString(R.string.error_campo_requerido));
            isCamposVacio = true;
        }
        if(TextUtils.isEmpty(etxtFechaNacimiento.getText()) ){
            etxtFechaNacimiento.setError(getString(R.string.error_campo_requerido));
            isCamposVacio = true;
        }else if(!esfechaNacimientoValida(UtilidadesFecha.convertirStringADate(etxtFechaNacimiento.getText().toString()))){
            etxtFechaNacimiento.setError(getString(R.string.error_fechanacimiento_novalida));
            isCamposVacio = true;
        }

        return isCamposVacio;
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
        etxtFechaNacimiento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(RegistrarDatosPersonalesActivity.this, dateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
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
        Date fechaHoy = new Date();
        if (fechaNacimiento.equals(UtilidadesFecha.formatearDate(fechaHoy)) || fechaNacimiento.after(fechaHoy)){
            return false;
        }
        return isFechaValida;
    }

}
