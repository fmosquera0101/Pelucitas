package co.com.fredymosqueralemus.pelucitas;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import co.com.fredymosqueralemus.pelucitas.adapters.AdapterMisEmpleados;
import co.com.fredymosqueralemus.pelucitas.constantes.Constantes;
import co.com.fredymosqueralemus.pelucitas.modelo.minegocio.EmpleadosXNegocio;
import co.com.fredymosqueralemus.pelucitas.modelo.minegocio.MiNegocio;
import co.com.fredymosqueralemus.pelucitas.modelo.usuario.PerfilesXUsuario;
import co.com.fredymosqueralemus.pelucitas.modelo.usuario.Usuario;
import co.com.fredymosqueralemus.pelucitas.utilidades.UtilidadesFirebaseBD;

public class VerListaEmpleadosParaReservarActivity extends AppCompatActivity {

    private ListView listView;
    private ProgressBar progressBar;
    private List<Usuario> listUsuarios;
    private Context context;
    private MiNegocio miNegocio;
    private long childrenCount = 0;
    private long cantidadChildren = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_lista_empleados_para_reservar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        listView = (ListView) findViewById(R.id.listview_empleados_VerListaEmpleadosParaReservarActivity);
        progressBar = (ProgressBar) findViewById(R.id.progressBar_VerListaEmpleadosParaReservarActivity);
        context = this;
        Intent intent = getIntent();
        miNegocio = (MiNegocio) intent.getSerializableExtra(Constantes.MINEGOCIO_OBJECT);
        getEmpleadosXNegocio();
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
                    cantidadChildren = 0;
                    DatabaseReference databaseReferenceNegoXEmpleados = FirebaseDatabase.getInstance().getReference(UtilidadesFirebaseBD.getUrlInserccionEmpleadosXNegocio(miNegocio.getNitNegocio()));
                    databaseReferenceNegoXEmpleados.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            listUsuarios = new ArrayList<Usuario>();
                            childrenCount = dataSnapshot.getChildrenCount();
                            for (DataSnapshot child : dataSnapshot.getChildren()) {
                                EmpleadosXNegocio empleadosXNegocio = child.getValue(EmpleadosXNegocio.class);
                                DatabaseReference databaseReferenceEmpleados = FirebaseDatabase.getInstance().getReference(UtilidadesFirebaseBD.getUrlInserccionUsuario(empleadosXNegocio.getUidUsario()));
                                databaseReferenceEmpleados.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        Usuario usuario = dataSnapshot.getValue(Usuario.class);
                                        if (null != usuario) {
                                            PerfilesXUsuario perfilesXUsuario = usuario.getPerfilEmpleado();
                                            if (miNegocio.getNitNegocio().equals(usuario.getNitNegocioEmpleador()) && "S".equals(perfilesXUsuario.getActivo())
                                                    && usuario.getNombre().trim().toLowerCase().contains(newText.trim().toLowerCase())) {
                                                listUsuarios.add(usuario);
                                            }


                                            if (childrenCount == cantidadChildren) {
                                                AdapterMisEmpleados adapterMisEmpleados = new AdapterMisEmpleados(context, R.layout.layout_listview_misempleados, listUsuarios);
                                                listView.setAdapter(adapterMisEmpleados);
                                                progressBar.setVisibility(View.GONE);
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });

                                cantidadChildren++;

                            }
                            if (childrenCount == 0) {
                                progressBar.setVisibility(View.GONE);
                            }


                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            progressBar.setVisibility(View.GONE);

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

    private void getEmpleadosXNegocio() {
        DatabaseReference databaseReferenceNegoXEmpleados = FirebaseDatabase.getInstance().getReference(UtilidadesFirebaseBD.getUrlInserccionEmpleadosXNegocio(miNegocio.getNitNegocio()));
        databaseReferenceNegoXEmpleados.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listUsuarios = new ArrayList<Usuario>();
                childrenCount = dataSnapshot.getChildrenCount();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    EmpleadosXNegocio empleadosXNegocio = child.getValue(EmpleadosXNegocio.class);
                    DatabaseReference databaseReferenceEmpleados = FirebaseDatabase.getInstance().getReference(UtilidadesFirebaseBD.getUrlInserccionUsuario(empleadosXNegocio.getUidUsario()));
                    databaseReferenceEmpleados.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Usuario usuario = dataSnapshot.getValue(Usuario.class);
                            if (miNegocio.getNitNegocio().equals(usuario.getNitNegocioEmpleador())) {
                                listUsuarios.add(usuario);
                            }
                            if (childrenCount == cantidadChildren) {
                                AdapterMisEmpleados adapterMisEmpleados = new AdapterMisEmpleados(context, R.layout.layout_listview_misempleados, listUsuarios);
                                listView.setAdapter(adapterMisEmpleados);
                                progressBar.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                    cantidadChildren++;

                }
                if (childrenCount == 0) {
                    progressBar.setVisibility(View.GONE);
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                progressBar.setVisibility(View.GONE);

            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Usuario usuario = listUsuarios.get(position);
                Intent intentEmpleado = new Intent(context, CalendarAgendaXEmpleadoActivity.class);
                intentEmpleado.putExtra(Constantes.USUARIO_OBJECT, usuario);
                intentEmpleado.putExtra(Constantes.MINEGOCIO_OBJECT, miNegocio);

                intentEmpleado.putExtra(Constantes.CALL_FROM_ACTIVITY_MISEMPLEADOS, AdministrarMisEmpleadosActivity.class.getName());
                intentEmpleado.putExtra(Constantes.CALL_FROM_ACTIVITY_VER_PERFIL_PARA_AGENDAR, VerListaEmpleadosParaReservarActivity.class.getName());

                startActivity(intentEmpleado);
            }
        });
    }
}
