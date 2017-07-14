package co.com.fredymosqueralemus.pelucitas;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import co.com.fredymosqueralemus.pelucitas.adapters.AdapterMisEmpleados;
import co.com.fredymosqueralemus.pelucitas.constantes.Constantes;
import co.com.fredymosqueralemus.pelucitas.modelo.minegocio.MiNegocio;
import co.com.fredymosqueralemus.pelucitas.modelo.usuario.Usuario;

public class AdministrarMisEmpleadosActivity extends AppCompatActivity {

    private ListView listView;
    private ProgressBar progressBar;
    private List<Usuario> listUsuarios;
    private Context context;
    private Intent intent;
    MiNegocio miNegocio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_empleados);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        listView = (ListView) findViewById(R.id.listview_fragment_MisEmpleadosActivity);
        progressBar = (ProgressBar) findViewById(R.id.progressBar_MisEmpleadosActivity);
        context = this;
        intent = getIntent();
        miNegocio = (MiNegocio) intent.getSerializableExtra(Constantes.MINEGOCIO_OBJECT);
        getEmpleadosXNegocio();
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
                progressBar.setVisibility(View.GONE);
                AdapterMisEmpleados adapterMisEmpleados = new AdapterMisEmpleados(context, R.layout.layout_listview_misempleados, listUsuarios);
                listView.setAdapter(adapterMisEmpleados);

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
                Intent intentEmpleado = new Intent(context, AdministrarMiPerfilActivity.class);
                intentEmpleado.putExtra(Constantes.USUARIO_OBJECT, usuario);
                intentEmpleado.putExtra(Constantes.CALL_FROM_ACTIVITY_MISEMPLEADOS, AdministrarMisEmpleadosActivity.class.getName());

                startActivity(intentEmpleado);
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int item = menuItem.getItemId();
        if (item == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(menuItem);
    }


    public void abrirActivityBuscarEmpleado(View view){
        Intent intent = new Intent(this, BuscarEmpleadoActivity.class);
        intent.putExtra(Constantes.MINEGOCIO_OBJECT, miNegocio);
        startActivity(intent);
    }
    @Override
    public void onResume(){
        super.onResume();
        getEmpleadosXNegocio();
    }
}
