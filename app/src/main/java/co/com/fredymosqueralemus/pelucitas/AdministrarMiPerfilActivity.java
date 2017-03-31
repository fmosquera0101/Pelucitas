package co.com.fredymosqueralemus.pelucitas;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.signature.StringSignature;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import co.com.fredymosqueralemus.pelucitas.constantes.Constantes;
import co.com.fredymosqueralemus.pelucitas.direccion.Direccion;
import co.com.fredymosqueralemus.pelucitas.modelo.usuario.PerfilesXUsuario;
import co.com.fredymosqueralemus.pelucitas.modelo.usuario.Usuario;
import co.com.fredymosqueralemus.pelucitas.sharedpreference.SharedPreferencesSeguro;
import co.com.fredymosqueralemus.pelucitas.sharedpreference.SharedPreferencesSeguroSingleton;
import co.com.fredymosqueralemus.pelucitas.utilidades.Utilidades;
import co.com.fredymosqueralemus.pelucitas.utilidades.UtilidadesFecha;
import co.com.fredymosqueralemus.pelucitas.utilidades.UtilidadesFirebaseBD;
import co.com.fredymosqueralemus.pelucitas.utilidades.UtilidadesImagenes;

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

    private String userChoose;

    private int REQUEST_CAMERA = 0;
    private int SELECT_FILE = 1;

    private StorageReference storageReference;
    private Context context;

    private PerfilesXUsuario perfilAdministrador;
    private PerfilesXUsuario perfilEmpleado;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administrar_mi_perfil);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        sharedPreferencesSeguro = SharedPreferencesSeguroSingleton.getInstance(this, Constantes.SHARED_PREFERENCES_INFOUSUARIO, Constantes.SECURE_KEY_SHARED_PREFERENCES);
        storageReference = UtilidadesFirebaseBD.getFirebaseStorageFromUrl();
        context = this;
        inicializarViews();

        databaseReference.child(Constantes.USUARIO_FIREBASE_BD).child(sharedPreferencesSeguro.getString(Constantes.USERUID)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                usuario = dataSnapshot.getValue(Usuario.class);
                if(null != usuario) {
                    settearViewsInfoUsuario(usuario);
                    File fileImage = UtilidadesImagenes.getFileImagenPerfilUsuario(usuario);
                    if (fileImage.exists()) {
                        Glide.with(context).load(fileImage).diskCacheStrategy(DiskCacheStrategy.RESULT).signature(new StringSignature(String.valueOf(fileImage.lastModified()))).into(imgvImagenPerfilUsuario);
                    } else {
                        final StorageReference storageReferenceImagenes = UtilidadesFirebaseBD.getReferenceImagenMiPerfil(storageReference, usuario);
                        storageReferenceImagenes.getMetadata().addOnSuccessListener(new OnSuccessListener<StorageMetadata>() {
                            @Override
                            public void onSuccess(StorageMetadata storageMetadata) {
                                Glide.with(context).using(new FirebaseImageLoader()).load(storageReferenceImagenes).diskCacheStrategy(DiskCacheStrategy.RESULT).signature(new StringSignature(String.valueOf(storageMetadata.getCreationTimeMillis()))).into(imgvImagenPerfilUsuario);
                            }
                        });
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        databaseReference.child(Constantes.PERFILESX_USUARIO_FIREBASE_BD).child(Constantes.PERFIL_ADMINISTRADOR_FIREBASE_BD).child(sharedPreferencesSeguro.getString(Constantes.USERUID)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                perfilAdministrador = dataSnapshot.getValue(PerfilesXUsuario.class);
                boolean isPerfil = (null != perfilAdministrador && "S".equals(perfilAdministrador.getActivo()));
                chbxPerfilAdministrador.setChecked(isPerfil);

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        databaseReference.child(Constantes.PERFILESX_USUARIO_FIREBASE_BD).child(Constantes.PERFIL_EMPLEADO_FIREBASE_BD).child(sharedPreferencesSeguro.getString(Constantes.USERUID)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                perfilEmpleado = dataSnapshot.getValue(PerfilesXUsuario.class);
                boolean isPerfil = (null != perfilEmpleado && "S".equals(perfilEmpleado.getActivo()));

                 chbxPerfilEmpleado.setChecked(isPerfil);

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        /*
        chbxPerfilAdministrador.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(chbxPerfilAdministrador.isChecked()){
                    perfilAdministrador.setFechaModificacion(UtilidadesFecha.convertirDateAString(new Date()));
                    databaseReference.setValue(perfilAdministrador);
                }
            }
        });

        chbxPerfilEmpleado.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(chbxPerfilEmpleado.isChecked()){
                    perfilEmpleado.setFechaModificacion(UtilidadesFecha.convertirDateAString(new Date()));
                    databaseReference = firebaseDatabase.getReference(UtilidadesFirebaseBD.getUrlInserccionPerfilEmpleado(usuario.getKeyUid()));
                    databaseReference.setValue(perfilEmpleado);

                }
            }
        });*/

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

    private void settearViewsInfoUsuario(Usuario miUsuario) {
        txtvNombreUsuario.setText(miUsuario.getNombre() + " " + miUsuario.getApellidos());
        txtvCorreoUsuario.setText(sharedPreferencesSeguro.getString(Constantes.CORREO));
        txtvFechaNacimiento.setText(miUsuario.getFechaNacimiento());
        txtvTelefono.setText(miUsuario.getTelefono());
        //Settear perfil
        Direccion direccion = miUsuario.getDireccion();
        txtpaisdeptociudad.setText(direccion.getPais() + " " + direccion.getDepartamento() + " " + direccion.getCiudad());
        txtvDireccion.setText(Utilidades.getStrDireccion(direccion) + ", " + direccion.getDatosAdicionales()+", " + direccion.getBarrio());

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int item = menuItem.getItemId();
        if (item == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    public void editarFechaNacimientoTelefono(View view) {
        Intent intentEditarFechaNacTel = new Intent(this, RegistrarDatosPersonalesActivity.class);
        startActivity(intentEditarFechaNacTel);
    }

    public void editarDireccionUsuario(View view) {
        Intent intentEditarDireccionUsuario = new Intent(this, RegistrarDireccionActivity.class);
        startActivity(intentEditarDireccionUsuario);

    }




    public void seleccionarImagenPerfil(View view) {
        {
            final CharSequence[] items = {getString(R.string.st_camara), getString(R.string.st_galeria), getString(R.string.st_cancelar)};
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(getString(R.string.st_elegirImagen));
            builder.setItems(items, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    boolean result = Utilidades.verificarPermisos(AdministrarMiPerfilActivity.this);
                    if (getString(R.string.st_camara).equals(items[which])) {
                        userChoose = getString(R.string.st_camara);
                        if (result) {
                            camaraIntent();
                        }
                    } else if (getString(R.string.st_galeria).equals(items[which])) {
                        userChoose = getString(R.string.st_galeria);
                        if (result) {
                            galeriaIntent();
                        }
                    } else if (getString(R.string.st_cancelar).equals(items[which])) {
                        dialog.dismiss();
                    }

                }
            });
            builder.show();
        }

    }

    private void galeriaIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);

        startActivityForResult(Intent.createChooser(intent, getString(R.string.st_selecImagen)), SELECT_FILE);
    }

    private void camaraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK){
            if(requestCode == SELECT_FILE){
                onSelectFromGaleryResult(data);
            }else if(requestCode == REQUEST_CAMERA){
                onCaptureImageResult(data);
            }
        }

    }

    private void onCaptureImageResult(Intent data) {
        Bitmap bitmap = (Bitmap) data.getExtras().get("data");
        guardarImagenMiNegocio(bitmap);
        imgvImagenPerfilUsuario.setImageBitmap(bitmap);

    }

    private void onSelectFromGaleryResult(Intent data) {
        Bitmap bitmap = null;
        if(null != data){
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
                guardarImagenMiNegocio(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
            imgvImagenPerfilUsuario.setImageBitmap(bitmap);
        }

    }
    private void guardarImagenMiNegocio(Bitmap bitmap){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        File fileFolderImages = new File(Environment.getExternalStorageDirectory(), "/co.com.fredymosqueralemus.pelucitas/imagenes/usuario/perfil");
        if(!fileFolderImages.exists()){
            fileFolderImages.mkdirs();
        }
        File file = new File(Environment.getExternalStorageDirectory()+"/co.com.fredymosqueralemus.pelucitas/imagenes/usuario/perfil", "usuario"+usuario.getCedulaIdentificacion()+".jpg");
        try {
            byte [] dataImage = byteArrayOutputStream.toByteArray();
            file.createNewFile();
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(dataImage);
            fileOutputStream.close();
            subirImagenAFireBaseStorage(dataImage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void subirImagenAFireBaseStorage(byte [] dataImage) throws FileNotFoundException {
        UploadTask uploadTask = UtilidadesFirebaseBD.getReferenceImagenMiPerfil(storageReference, usuario).putBytes(dataImage);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(AdministrarMiPerfilActivity.this, "No se pudo cargar la imagen",
                        Toast.LENGTH_SHORT ).show();

            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(AdministrarMiPerfilActivity.this, "Imagen cargada correctamente",
                        Toast.LENGTH_SHORT ).show();
            }
        });

    }

    public void actualizarPerfilAdministrador(View view){
        if(chbxPerfilAdministrador.isChecked()){
            perfilAdministrador.setActivo("S");
        }else{
            perfilAdministrador.setActivo("N");
        }
        perfilAdministrador.setFechaModificacion(UtilidadesFecha.convertirDateAString(new Date()));
        databaseReference = firebaseDatabase.getReference(UtilidadesFirebaseBD.getUrlInserccionPerfilAdministrador(usuario.getKeyUid()));
        databaseReference.setValue(perfilAdministrador);
    }

    public void actualizarPerfilEmpleado(View view){
        if(chbxPerfilEmpleado.isChecked()){
            perfilEmpleado.setActivo("S");
        }else{
            perfilEmpleado.setActivo("N");
        }
        perfilEmpleado.setFechaModificacion(UtilidadesFecha.convertirDateAString(new Date()));
        databaseReference = firebaseDatabase.getReference(UtilidadesFirebaseBD.getUrlInserccionPerfilEmpleado(usuario.getKeyUid()));
        databaseReference.setValue(perfilEmpleado);
    }
}
