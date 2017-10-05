package co.com.fredymosqueralemus.pelucitas;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import co.com.fredymosqueralemus.pelucitas.adapters.AdapterMisEmpleados;
import co.com.fredymosqueralemus.pelucitas.constantes.Constantes;
import co.com.fredymosqueralemus.pelucitas.modelo.minegocio.EmpleadosXNegocio;
import co.com.fredymosqueralemus.pelucitas.modelo.minegocio.MiNegocio;
import co.com.fredymosqueralemus.pelucitas.modelo.usuario.PerfilesXUsuario;
import co.com.fredymosqueralemus.pelucitas.modelo.usuario.Usuario;
import co.com.fredymosqueralemus.pelucitas.utilidades.UtilidadesFecha;
import co.com.fredymosqueralemus.pelucitas.utilidades.UtilidadesFirebaseBD;

public class BuscarEmpleadoActivity extends AppCompatActivity {

    private ListView listView;
    private List<Usuario> listUsuarios;
    private Context context;
    private MiNegocio miNegocio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar_empleado);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        listView = (ListView) findViewById(R.id.listview_fragment_BuscarEmpleadoActivity);
        context = this;
        Intent intent = getIntent();
        miNegocio = (MiNegocio) intent.getSerializableExtra(Constantes.MINEGOCIO_OBJECT);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int item = menuItem.getItemId();
        if (item == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        SearchView searchView = getSearchView(menu);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(final String newText) {
                if (!TextUtils.isEmpty(newText)) {
                    Query firebaseQuery = getQueryFirebase(newText);
                    consultarEmpleadosFirebase(newText, firebaseQuery);
                    listViewSetOnItemClickListener();
                } else {
                    listUsuarios.clear();

                }
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private void consultarEmpleadosFirebase(final String newText, Query firebaseQuery) {
        firebaseQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                addUsuarioToList(dataSnapshot, newText);
                setAdapterMisEmpleados();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {


            }
        });
    }

    private Query getQueryFirebase(String newText) {
        Query firebaseQuery;DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        firebaseQuery = databaseReference.child(Constantes.USUARIO_FIREBASE_BD).orderByChild("nombre").startAt("#" + newText);
        return firebaseQuery;
    }

    private void addUsuarioToList(DataSnapshot dataSnapshot, String newText) {
        listUsuarios = new ArrayList<Usuario>();
        for (DataSnapshot child : dataSnapshot.getChildren()) {
            final Usuario usuario = child.getValue(Usuario.class);
            if (usuario.getNombre().trim().toLowerCase().contains(newText.trim().toLowerCase())) {
                PerfilesXUsuario perfilesXUsuario = usuario.getPerfilEmpleado();
                if ("S".equals(perfilesXUsuario.getActivo())) {
                    listUsuarios.add(usuario);
                }

            }
        }
    }

    private void listViewSetOnItemClickListener() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage(getString(R.string.str_agregarempleado));
                createBuilderPositiveNegativeButtons(position, builder);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }

        });
    }

    private void createBuilderPositiveNegativeButtons(final int position, AlertDialog.Builder builder) {
        builder.setPositiveButton(getString(R.string.str_agregar), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Usuario usuario = listUsuarios.get(position);
                consultarInformacionEmpleadoFirebase(usuario);

            }
        });

        builder.setNegativeButton(getString(R.string.str_cancelar), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                getEmpleadosXNegocio();
            }
        });
    }

    private void consultarInformacionEmpleadoFirebase(final Usuario usuario) {
        DatabaseReference databaseReferenceEmpleado = FirebaseDatabase.getInstance().getReference(UtilidadesFirebaseBD.getUrlInserccionEmpleadosXNegocio(miNegocio.getNitNegocio()));
        databaseReferenceEmpleado.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean empleadoExisteEnNegocio = isEmpleadoExisteEnNegocio(dataSnapshot, usuario);
                if(!empleadoExisteEnNegocio){
                    usuario.setFechaModificacion(getFechaModificacion());
                    usuario.setNitNegocioEmpleador(miNegocio.getNitNegocio());
                    actualizarInfoUsuarioFirebase(usuario);
                    EmpleadosXNegocio empleadosXNegocio = getEmpleadosXNegocio(usuario);
                    insertarEmpleadoXNegocioFirebase(empleadosXNegocio);
                    Toast.makeText(context, getString(R.string.str_msj_empleado_agregado), Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private boolean isEmpleadoExisteEnNegocio(DataSnapshot dataSnapshot, Usuario usuario) {
        boolean empleadoExisteEnNegocio = false;
        for (DataSnapshot child: dataSnapshot.getChildren()) {
            EmpleadosXNegocio empleadosXNegocioFromBD = child.getValue(EmpleadosXNegocio.class);
            if(usuario.getUid().equals(empleadosXNegocioFromBD.getUidUsario())){
                Toast.makeText(context, getString(R.string.str_msj_empleado_ya_agregado), Toast.LENGTH_SHORT).show();
                empleadoExisteEnNegocio = true;
                break;
            }
        }
        return empleadoExisteEnNegocio;
    }

    private void actualizarInfoUsuarioFirebase(Usuario usuario) {
        DatabaseReference databaseReferenceEmpleado = FirebaseDatabase.getInstance().getReference(UtilidadesFirebaseBD.getUrlInserccionUsuario(usuario.getUid()));
        databaseReferenceEmpleado.setValue(usuario);
    }

    private void insertarEmpleadoXNegocioFirebase(EmpleadosXNegocio empleadosXNegocio) {
        DatabaseReference databaseReferenceEmpleado;
        databaseReferenceEmpleado = FirebaseDatabase.getInstance().getReference(UtilidadesFirebaseBD.getUrlInserccionEmpleadosXNegocio(miNegocio.getNitNegocio()));
        databaseReferenceEmpleado.push().setValue(empleadosXNegocio);
    }

    @NonNull
    private EmpleadosXNegocio getEmpleadosXNegocio(Usuario usuario) {
        EmpleadosXNegocio empleadosXNegocio = new EmpleadosXNegocio();
        empleadosXNegocio.setCedulaUsuario(usuario.getCedulaIdentificacion());
        empleadosXNegocio.setUidUsario(usuario.getUid());
        empleadosXNegocio.setFechaInsercion(getFechaModificacion());
        return empleadosXNegocio;
    }

    private String getFechaModificacion() {
        return UtilidadesFecha.convertirDateAString(new Date(), Constantes.FORMAT_DDMMYYYYHHMMSS);
    }

    private SearchView getSearchView(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_search, menu);
        MenuItem menuItem = menu.findItem(R.id.menuItemsearch);
        return (SearchView) menuItem.getActionView();
    }

    private void getEmpleadosXNegocio(){
        DatabaseReference databaseReferenceEmpleados = FirebaseDatabase.getInstance().getReference(Constantes.USUARIO_FIREBASE_BD);
        databaseReferenceEmpleados.orderByChild("nombre").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listUsuarios = new ArrayList<Usuario>();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Usuario usuario = child.getValue(Usuario.class);
                    if(miNegocio.getNitNegocio().equals(usuario.getNitNegocioEmpleador())){
                        listUsuarios.add(usuario);
                    }


                }
                setAdapterMisEmpleados();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void setAdapterMisEmpleados() {
        AdapterMisEmpleados adapterMisEmpleados = new AdapterMisEmpleados(context, R.layout.layout_listview_misempleados, listUsuarios);
        listView.setAdapter(adapterMisEmpleados);
    }
}
