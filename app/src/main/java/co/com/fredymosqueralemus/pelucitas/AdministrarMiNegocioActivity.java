package co.com.fredymosqueralemus.pelucitas;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import co.com.fredymosqueralemus.pelucitas.constantes.Constantes;
import co.com.fredymosqueralemus.pelucitas.direccion.Direccion;
import co.com.fredymosqueralemus.pelucitas.horario.Horario;
import co.com.fredymosqueralemus.pelucitas.modelo.minegocio.MiNegocio;
import co.com.fredymosqueralemus.pelucitas.sharedpreference.SharedPreferencesSeguro;
import co.com.fredymosqueralemus.pelucitas.sharedpreference.SharedPreferencesSeguroSingleton;
import co.com.fredymosqueralemus.pelucitas.utilidades.Utilidades;

public class AdministrarMiNegocioActivity extends AppCompatActivity {

    private ImageView imgvImagenNegocio;

    private TextView tvNitNegocio;
    private TextView tvNombre;
    private TextView tvTelefono;
    private TextView tvTipoNegocio;

    private TextView tvPaisDeptoCiudad;
    private TextView tvDireccion;
    private TextView tvBarrio;
    private TextView tvDatosAdicionales;

    private TextView tvDiasLaborales;
    private TextView tvHorariolaboral;

    private RelativeLayout relativeLayout;
    private Intent intent;
    private MiNegocio miNegocio;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private SharedPreferencesSeguro sharedPreferencesSeguro;
    private int REQUEST_CAMERA = 0;
    private int SELECT_FILE = 1;

    private String userChoose;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administrar_mi_negocio);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        intent = getIntent();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        miNegocio = (MiNegocio) intent.getSerializableExtra(Constantes.MINEGOCIOOBJECT);
        sharedPreferencesSeguro = SharedPreferencesSeguroSingleton.getInstance(this, Constantes.SHARED_PREFERENCES_INFOUSUARIO, Constantes.SECURE_KEY_SHARED_PREFERENCES);
        imgvImagenNegocio = (ImageView) findViewById(R.id.imagennegocio_activity_administrarminegocio);
        tvNitNegocio = (TextView) findViewById(R.id.nit_activity_administrarminegocio);
        tvNombre = (TextView) findViewById(R.id.nombrenegocio_activity_administrarminegocio);
        tvTelefono = (TextView) findViewById(R.id.telefono_activity_administrarminegocio);
        tvTipoNegocio = (TextView) findViewById(R.id.tiponegocio_activity_administrarminegocio);
        relativeLayout = (RelativeLayout) findViewById(R.id.editar_informacionminegocio_activity_administrarminegocio);

        tvPaisDeptoCiudad = (TextView) findViewById(R.id.pasideptociudad_activity_administrarminegocio);
        tvDireccion = (TextView) findViewById(R.id.direccion_activity_administrarminegocio);
        tvBarrio = (TextView) findViewById(R.id.barrio_activity_administrarminegocio);
        tvDatosAdicionales = (TextView) findViewById(R.id.datosadicionales_activity_administrarminegocio);

        tvDiasLaborales = (TextView) findViewById(R.id.diaslaborales_activity_administrarminegocio);
        tvHorariolaboral = (TextView) findViewById(R.id.horariolaboral_activity_administrarminegocio);

        if(null != miNegocio){
            settearInforamcioEnViesMiNegocio(miNegocio);

        }

        imgvImagenNegocio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seleccionarImagenNegocio();
            }
        });



    }
    private void seleccionarImagenNegocio(){
        final CharSequence[] items = {"Camara", "Galeria", "Cancelar"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Elegir Imagen");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                boolean result = Utilidades.verificarPermisos(AdministrarMiNegocioActivity.this);
                if("Camara".equals(items[which])){
                    userChoose = "Camara";
                    if (result){
                        camaraIntent();
                    }
                }else if("Galeria".equals(items[which])){
                    userChoose = "Galeria";
                    if(result){
                        galeriaIntent();
                    }
                }else if("Cancelar".equals(items[which])){
                    dialog.dismiss();
                }

            }
        });
        builder.show();
    }

    private void galeriaIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);

        startActivityForResult(Intent.createChooser(intent, "Seleccionar imagen"), SELECT_FILE);
    }

    private void camaraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode){
            case Utilidades.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    if("Camara".equals(userChoose)){
                        camaraIntent();
                    }else if("Galeria".equals(userChoose)){
                        galeriaIntent();
                    }
                }else {
                    Toast.makeText(AdministrarMiNegocioActivity.this, R.string.str_permisosdenegados,
                            Toast.LENGTH_SHORT ).show();
                }
                break;
        }
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
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, byteArrayOutputStream);
        File file = new File(Environment.getExternalStorageDirectory(), "MiNegocio"+ System.currentTimeMillis()+".jpg");
        try {
            file.createNewFile();
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(byteArrayOutputStream.toByteArray());
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        imgvImagenNegocio.setImageBitmap(bitmap);

    }

    private void onSelectFromGaleryResult(Intent data) {
        Bitmap bitmap = null;
        if(null != data){
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
            imgvImagenNegocio.setImageBitmap(bitmap);
        }

    }

    private void settearInforamcioEnViesMiNegocio(MiNegocio miNegocio){
        tvNitNegocio.setText("Nit: "+miNegocio.getNitNegocio());
        tvNombre.setText(miNegocio.getNombreNegocio());
        tvTelefono.setText("Telefono: "+miNegocio.getTelefonoNegocio());
        tvTipoNegocio.setText("Tipo Negocio: "+miNegocio.getTipoNegocio().getTipoNegocio());

        Direccion direccion = miNegocio.getDireccion();
        tvPaisDeptoCiudad.setText(getPaisDeptoCiudad(direccion));
        tvDireccion.setText(getStrDireccion(direccion));
        tvBarrio.setText("Barrio: "+direccion.getBarrio());
        tvDatosAdicionales.setText(direccion.getDatosAdicionales());

        Horario horario = miNegocio.getHorarioNegocio();
        tvDiasLaborales.setText(horario.getDiasLaborales());
        tvHorariolaboral.setText(getStrHorarioLaboral(horario));

    }
    private String getStrHorarioLaboral(Horario horario){
        StringBuilder strbHorario = new StringBuilder();
        strbHorario.append(horario.getHoraInicial());
        strbHorario.append("-");
        strbHorario.append(horario.getHoraFinal());

        return strbHorario.toString();

    }
    private String getPaisDeptoCiudad(Direccion direccion){
        StringBuilder strbPaisDeptoCiudad = new StringBuilder();
        strbPaisDeptoCiudad.append(direccion.getCiudad());
        strbPaisDeptoCiudad.append(", ");
        strbPaisDeptoCiudad.append(direccion.getDepartamento());
        strbPaisDeptoCiudad.append(", ");
        strbPaisDeptoCiudad.append(direccion.getPais());


        return strbPaisDeptoCiudad.toString();
    }
    private String getStrDireccion(Direccion direccion){
        StringBuilder strbDireccion = new StringBuilder();
        strbDireccion.append(direccion.getCarreraCalle());
        strbDireccion.append(", ");
        strbDireccion.append(direccion.getNumero1());
        strbDireccion.append("-");
        strbDireccion.append(direccion.getNumero2());

        return strbDireccion.toString();


    }
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem){
        int item = menuItem.getItemId();
        if(item == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(menuItem);
    }
    public void editarInforamcionMiNegocio(View view){
        Intent intent = new Intent(this, RegistrarMiNegocioActivity.class);
        intent.putExtra(Constantes.MINEGOCIOOBJECT, miNegocio);
        intent.putExtra(Constantes.CALL_FROM_ACTIVITY_ADMINISTRARMINEGOCIO, AdministrarMiNegocioActivity.class.getName());
        startActivity(intent);
    }
    public void editarDireccionMiNegocio(View view){
        Intent intent = new Intent(this, RegistrarDireccionActivity.class);
        intent.putExtra(Constantes.MINEGOCIOOBJECT, miNegocio);
        intent.putExtra(Constantes.CALL_FROM_ACTIVITY_ADMINISTRARMINEGOCIO, AdministrarMiNegocioActivity.class.getName());
        startActivity(intent);
    }
    public void editarHoararioMiNegocio(View view){
        Intent intent = new Intent(this, RegistrarHorarioActivity.class);
        intent.putExtra(Constantes.MINEGOCIOOBJECT, miNegocio);
        intent.putExtra(Constantes.CALL_FROM_ACTIVITY_ADMINISTRARMINEGOCIO, AdministrarMiNegocioActivity.class.getName());
        startActivity(intent);
    }
    public void agregarEmpleado(View view){

    }
    @Override
    public void onStart(){
        super.onStart();
        databaseReference.child(Constantes.MINEGOCIO_FIREBASE_BD).child(sharedPreferencesSeguro.getString(Constantes.USERUID)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                MiNegocio minegocioOnchange = dataSnapshot.child(miNegocio.getKeyChild()).getValue(MiNegocio.class);
                if(null != minegocioOnchange) {
                   settearInforamcioEnViesMiNegocio(minegocioOnchange);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
