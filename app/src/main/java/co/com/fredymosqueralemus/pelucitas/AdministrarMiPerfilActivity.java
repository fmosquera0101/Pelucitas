package co.com.fredymosqueralemus.pelucitas;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import co.com.fredymosqueralemus.pelucitas.constantes.Constantes;
import co.com.fredymosqueralemus.pelucitas.imagenes.SeleccionarImagen;
import co.com.fredymosqueralemus.pelucitas.modelo.usuario.Usuario;
import co.com.fredymosqueralemus.pelucitas.services.TaskCargarImagenMiPerfil;
import co.com.fredymosqueralemus.pelucitas.sharedpreference.SharedPreferencesSeguro;
import co.com.fredymosqueralemus.pelucitas.sharedpreference.SharedPreferencesSeguroSingleton;
import co.com.fredymosqueralemus.pelucitas.utilidades.UtilidadesFirebaseBD;
import co.com.fredymosqueralemus.pelucitas.utilidades.UtilidadesImagenes;

public class AdministrarMiPerfilActivity extends AppCompatActivity {

    private ImageView imgvImagenPerfilUsuario;
    private TextView txtvNombreUsuario;
    private TextView txtvCorreoUsuario;
    private ProgressBar progressBar;
    private LinearLayout linearLayoutOpcionesAdministrarMiPerfil;
    private LinearLayout layoutVeragendaEmpleadoActivityAdministrarmiperfil;
    private LinearLayout linearLayoutEditarPerfilAdministrarMiPerfilActivity;
    private DatabaseReference databaseReference;
    private SharedPreferencesSeguro sharedPreferencesSeguro;
    private Usuario usuario;
    private StorageReference storageReference;
    private Context context;
    private SeleccionarImagen seleccionarImagen;
    private Intent intent;
    private String strReadOnlyInformacionUsuario;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administrar_mi_perfil);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        storageReference = UtilidadesFirebaseBD.getFirebaseStorageFromUrl();
        intent = getIntent();
        context = this;
        inicializarViews();
        if(AdministrarMisEmpleadosActivity.class.getName().equals(intent.getStringExtra(Constantes.CALL_FROM_ACTIVITY_MISEMPLEADOS))){
            getSupportActionBar().setTitle("Informacion Empleado");
            usuario = (Usuario) intent.getSerializableExtra(Constantes.USUARIO_OBJECT);
            layoutVeragendaEmpleadoActivityAdministrarmiperfil.setVisibility(View.GONE);
            settearInformacionUsuario();
            strReadOnlyInformacionUsuario = "S";

        }else{
            sharedPreferencesSeguro = SharedPreferencesSeguroSingleton.getInstance(this, Constantes.SHARED_PREFERENCES_INFOUSUARIO, Constantes.SECURE_KEY_SHARED_PREFERENCES);
            obtenerInformacionUsuarioFromFirebase();
            seleccionarImagenPerfil();


        }

    }
    private void obtenerInformacionUsuarioFromFirebase(){
        databaseReference.child(Constantes.USUARIO_FIREBASE_BD).child(sharedPreferencesSeguro.getString(Constantes.USERUID)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                usuario = dataSnapshot.getValue(Usuario.class);
                if (null != usuario) {
                    if("S".equals(usuario.getPerfilEmpleado().getActivo())){
                        layoutVeragendaEmpleadoActivityAdministrarmiperfil.setVisibility(View.VISIBLE);
                    }else {
                        layoutVeragendaEmpleadoActivityAdministrarmiperfil.setVisibility(View.GONE);
                    }
                    settearInformacionUsuario();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void settearInformacionUsuario(){

        settearViewsInfoUsuario(usuario);
        UtilidadesImagenes.cargarImagenPerfilUsuario(imgvImagenPerfilUsuario, usuario, context, storageReference);
        progressBar.setVisibility(View.GONE);
        linearLayoutOpcionesAdministrarMiPerfil.setVisibility(View.VISIBLE);
    }

    private void inicializarViews() {
        imgvImagenPerfilUsuario = (ImageView) findViewById(R.id.imagenmiperfil_activity_administrarmiperfil);
        txtvNombreUsuario = (TextView) findViewById(R.id.nombreusuario_activity_administrarmiperfil);
        txtvCorreoUsuario = (TextView) findViewById(R.id.correo_activity_administrarmiperfil);
        progressBar = (ProgressBar) findViewById(R.id.progressbar_activity_administrarmiperfil);
        linearLayoutOpcionesAdministrarMiPerfil = (LinearLayout) findViewById(R.id.linear_layout_opciones_administrarmiperfil_activity_administrarmiperfil);
        linearLayoutEditarPerfilAdministrarMiPerfilActivity = (LinearLayout) findViewById(R.id.layout_editar_perfil_AdministrarMiPerfilActivity);
        layoutVeragendaEmpleadoActivityAdministrarmiperfil = (LinearLayout) findViewById(R.id.layout_veragenda_empleado_activity_administrarmiperfil);
    }

    private void settearViewsInfoUsuario(Usuario miUsuario) {
        txtvNombreUsuario.setText(miUsuario.getNombre() + " " + miUsuario.getApellidos());
        txtvCorreoUsuario.setText(miUsuario.getEmail());

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int item = menuItem.getItemId();
        if (item == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    public void editarInformacionPersonal(View view) {
        Intent intentnEditInfoPersonal = new Intent(this, RegistrarDatosPersonalesActivity.class);
        intentnEditInfoPersonal.putExtra(Constantes.CALL_FROM_ACTIVITY_ADMINISTRARMIPERFIL, AdministrarMiPerfilActivity.class.getName());
        intentnEditInfoPersonal.putExtra(Constantes.USUARIO_OBJECT, usuario);
        intentnEditInfoPersonal.putExtra(Constantes.SN_READONLY_INFORMACION_USUARIO, strReadOnlyInformacionUsuario);
        startActivity(intentnEditInfoPersonal);
    }

    public void abrirEditarDireccionUsuario(View view) {
        Intent intentEditarDireccionUsuario = new Intent(this, RegistrarDireccionActivity.class);
        intentEditarDireccionUsuario.putExtra(Constantes.CALL_FROM_ACTIVITY_ADMINISTRARMIPERFIL, AdministrarMiPerfilActivity.class.getName());
        intentEditarDireccionUsuario.putExtra(Constantes.USUARIO_OBJECT, usuario);
        intentEditarDireccionUsuario.putExtra(Constantes.SN_READONLY_INFORMACION_USUARIO, strReadOnlyInformacionUsuario);
        startActivity(intentEditarDireccionUsuario);

    }
    public void editarPerfiles(View view){
        Intent intentEditarPerfiles = new Intent(this, RegistrarPerfilUsuarioActivity.class);
        intentEditarPerfiles.putExtra(Constantes.CALL_FROM_ACTIVITY_ADMINISTRARMIPERFIL, AdministrarMiPerfilActivity.class.getName());
        intentEditarPerfiles.putExtra(Constantes.USUARIO_OBJECT, usuario);
        startActivity(intentEditarPerfiles);
    }


    private void seleccionarImagenPerfil() {
        seleccionarImagen = new SeleccionarImagen(context, this);
        imgvImagenPerfilUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seleccionarImagen.seleccionarImagen();
            }
        });

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Constantes.SELECT_FILE) {
                onSelectFromGaleryResult(data);
            } else if (requestCode == Constantes.REQUEST_CAMERA) {
                onCaptureImageResult(data);
            }
        }

    }

    private void onCaptureImageResult(Intent data) {
        Bitmap bitmap = (Bitmap) data.getExtras().get("data");
        guardarImagenMiNegocio(bitmap);

    }

    private void onSelectFromGaleryResult(Intent data) {
        Bitmap bitmap = null;
        if (null != data) {
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
                guardarImagenMiNegocio(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private void guardarImagenMiNegocio(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);

        try {
            byte[] dataImage = byteArrayOutputStream.toByteArray();
            subirImagenAFireBaseStorage(dataImage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void subirImagenAFireBaseStorage(byte[] dataImage) throws FileNotFoundException {
       new TaskCargarImagenMiPerfil(context, dataImage, usuario, imgvImagenPerfilUsuario).execute();

    }

    @Override
    public void  onStart(){
        super.onStart();
        if(!AdministrarMisEmpleadosActivity.class.getName().equals(intent.getStringExtra(Constantes.CALL_FROM_ACTIVITY_MISEMPLEADOS))){
            obtenerInformacionUsuarioFromFirebase();
        }
    }
    public void verAgendaEmpleado(View view){
        Intent intentAgenda = new Intent(this, CalendarAgendaXEmpleadoActivity.class);
        intentAgenda.putExtra(Constantes.USUARIO_OBJECT, usuario);
        intentAgenda.putExtra(Constantes.CALL_TO_AGREGAR_AGENDA_XEMPLEADO,  intent.getStringExtra(Constantes.CALL_FROM_ACTIVITY_CONFIGURACIONACTIVITY));
        intentAgenda.putExtra(Constantes.CALL_FROM_ACTIVITY_ADMINISTRARMIPERFIL, AdministrarMiPerfilActivity.class.getName());
        startActivity(intentAgenda);
    }

}
