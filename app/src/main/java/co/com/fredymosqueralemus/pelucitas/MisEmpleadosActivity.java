package co.com.fredymosqueralemus.pelucitas;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import co.com.fredymosqueralemus.pelucitas.adapters.AdapterMisEmpleados;
import co.com.fredymosqueralemus.pelucitas.constantes.Constantes;
import co.com.fredymosqueralemus.pelucitas.modelo.usuario.Usuario;

public class MisEmpleadosActivity extends AppCompatActivity {

    private ListView listView;
    private ProgressBar progressBar;
    private List<Usuario> listUsuarios;
    private DatabaseReference databaseReference;
    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_empleados);
        listView = (ListView) findViewById(R.id.listview_fragment_MisEmpleadosActivity);
        progressBar = (ProgressBar) findViewById(R.id.progressBar_MisEmpleadosActivity);
        context = this;

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
            public boolean onQueryTextChange(String newText) {
                if(null != newText && !"".equals(newText)) {
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                    Query firebaseQuery = databaseReference.child(Constantes.USUARIO_FIREBASE_BD).orderByChild("nombre").startAt("#" + newText);
                    firebaseQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            listUsuarios = new ArrayList<Usuario>();
                            for (DataSnapshot child : dataSnapshot.getChildren()) {
                                Usuario usuario = child.getValue(Usuario.class);
                                listUsuarios.add(usuario);
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
                }
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
}
