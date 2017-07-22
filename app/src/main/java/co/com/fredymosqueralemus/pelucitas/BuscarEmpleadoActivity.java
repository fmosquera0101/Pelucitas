package co.com.fredymosqueralemus.pelucitas;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import co.com.fredymosqueralemus.pelucitas.R;
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
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_search, menu);
        MenuItem menuItem = menu.findItem(R.id.menuItemsearch);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(final String newText) {
                if (!TextUtils.isEmpty(newText)) {
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                    Query firebaseQuery = databaseReference.child(Constantes.USUARIO_FIREBASE_BD).orderByChild("nombre").startAt("#" + newText);
                    firebaseQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
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

                            AdapterMisEmpleados adapterMisEmpleados = new AdapterMisEmpleados(context, R.layout.layout_listview_misempleados, listUsuarios);
                            listView.setAdapter(adapterMisEmpleados);

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {


                        }
                    });
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setMessage("Agregar Empleado");
                            builder.setPositiveButton("Agregar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Usuario usuario = listUsuarios.get(position);
                                    DatabaseReference databaseReferenceEmpleado = FirebaseDatabase.getInstance().getReference(UtilidadesFirebaseBD.getUrlInserccionUsuario(usuario.getUid()));
                                    usuario.setFechaModificacion(UtilidadesFecha.convertirDateAString(new Date(), Constantes.FORMAT_DDMMYYYYHHMMSS));
                                    usuario.setNitNegocioEmpleador(miNegocio.getNitNegocio());
                                    databaseReferenceEmpleado.setValue(usuario);
                                    databaseReferenceEmpleado = FirebaseDatabase.getInstance().getReference(UtilidadesFirebaseBD.getUrlInserccionEmpleadosXNegocio(miNegocio.getNitNegocio()));
                                    EmpleadosXNegocio empleadosXNegocio = new EmpleadosXNegocio();
                                    empleadosXNegocio.setCedulaUsuario(usuario.getCedulaIdentificacion());
                                    empleadosXNegocio.setUidUsario(usuario.getUid());
                                    empleadosXNegocio.setFechaInsercion(UtilidadesFecha.convertirDateAString(new Date(), Constantes.FORMAT_DDMMYYYYHHMMSS));
                                    databaseReferenceEmpleado.push().setValue(empleadosXNegocio);
                                    Toast.makeText(context, "Se agrego empleado a tu negocio",
                                            Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            });

                            builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    getEmpleadosXNegocio();
                                }
                            });
                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();
                        }

                    });

                } else {
                    listUsuarios.clear();

                }
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
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
                AdapterMisEmpleados adapterMisEmpleados = new AdapterMisEmpleados(context, R.layout.layout_listview_misempleados, listUsuarios);
                listView.setAdapter(adapterMisEmpleados);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
