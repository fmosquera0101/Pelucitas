package co.com.fredymosqueralemus.pelucitas;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import co.com.fredymosqueralemus.pelucitas.constantes.Constantes;
import co.com.fredymosqueralemus.pelucitas.direccion.Direccion;
import co.com.fredymosqueralemus.pelucitas.modelo.usuario.Usuario;
import co.com.fredymosqueralemus.pelucitas.sharedpreference.SharedPreferencesSeguro;
import co.com.fredymosqueralemus.pelucitas.sharedpreference.SharedPreferencesSeguroSingleton;
import co.com.fredymosqueralemus.pelucitas.utilidades.Utilidades;

public class AdministrarMiPerfilActivity extends AppCompatActivity {

    private ImageView imgvImagenPerfilUsuario;
    private TextView txtvNombreUsuario;
    private TextView txtvCorreoUsuario;
    private TextView txtvFechaNacimiento;
    private TextView txtvTelefono;
    private CheckBox chbxPerfilEmpleado;
    private CheckBox chbxPerfilAdministrador;
    private TextView txtpaisdeptociudad;
    private TextView txtvDireccion;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    private SharedPreferencesSeguro sharedPreferencesSeguro;

    private Usuario usuario;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administrar_mi_perfil);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        sharedPreferencesSeguro = SharedPreferencesSeguroSingleton.getInstance(this, Constantes.SHARED_PREFERENCES_INFOUSUARIO, Constantes.SECURE_KEY_SHARED_PREFERENCES);
        inicializarViews();

        databaseReference.child(Constantes.USUARIO_FIREBASE_BD).child(sharedPreferencesSeguro.getString(Constantes.USERUID)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                usuario = dataSnapshot.getValue(Usuario.class);
                settearViewsInfoUsuario(usuario);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void inicializarViews() {
        imgvImagenPerfilUsuario = (ImageView) findViewById(R.id.imagenmiperfil_activity_administrarmiperfil);
        txtvNombreUsuario = (TextView) findViewById(R.id.nombreusuario_activity_administrarmiperfil);
        txtvCorreoUsuario = (TextView) findViewById(R.id.correo_activity_administrarmiperfil);
        txtvFechaNacimiento = (TextView) findViewById(R.id.fechanacimiento_activity_administrarmiperfil);
        txtvTelefono = (TextView) findViewById(R.id.telefono_activity_administrarmiperfil);
        chbxPerfilEmpleado = (CheckBox) findViewById(R.id.perfilempleado_chkbx_activity_administrarmiperfil);
        chbxPerfilAdministrador = (CheckBox) findViewById(R.id.perfiladministrador_chkbx_activity_administrarmiperfil);
        txtpaisdeptociudad = (TextView) findViewById(R.id.paisdeptociudad_activity_administrarmiperfil);
        txtvDireccion = (TextView) findViewById(R.id.direccion_activity_administrarmiperfil);
    }
    private void settearViewsInfoUsuario(Usuario miUsuario){
        txtvNombreUsuario.setText(miUsuario.getNombre());
        txtvCorreoUsuario.setText(sharedPreferencesSeguro.getString(Constantes.CORREO));
        txtvFechaNacimiento.setText(miUsuario.getFechaNacimiento());
        txtvTelefono.setText(miUsuario.getTelefono());
        //Settear perfil
        //Direccion direccion = miUsuario.getDireccion();
        //txtpaisdeptociudad.setText(direccion.getPais() + " "+direccion.getDepartamento() +" "+direccion.getCiudad());
        //txtvDireccion.setText(Utilidades.getStrDireccion(direccion)+", "+direccion.getDatosAdicionales());

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem){
        int item = menuItem.getItemId();
        if(item == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(menuItem);
    }
    public void seleccionarImagenMiNegocio(View view){

    }
    public void editarFechaNacimientoTelefono(View view){

    }
    public void editarPerfilEmpleado(View view){

    }
    public void editarPerfilAdministrador(View view){

    }
    public void editarDireccionUsuario(View view){

    }
}
