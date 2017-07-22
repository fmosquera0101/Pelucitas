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
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import co.com.fredymosqueralemus.pelucitas.adapters.AdapterMisNegocios;
import co.com.fredymosqueralemus.pelucitas.constantes.Constantes;
import co.com.fredymosqueralemus.pelucitas.modelo.minegocio.MiNegocio;
import co.com.fredymosqueralemus.pelucitas.modelo.minegocio.TipoNegocio;

public class VerNegociosXTipoParaReservarActivity extends AppCompatActivity {

    private ListView listView;
    private List<MiNegocio> lstMisNegocios;
    private ProgressBar progressBar;
    private DatabaseReference databaseReference;
    private long childreCount = 0;
    private long cantidadChildren = 0;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_negocios_xtipo_para_reservar);
        listView = (ListView) findViewById(R.id.listview_misnegocios_VerNegociosXTipoParaReservarActivity);
        progressBar = (ProgressBar) findViewById(R.id.progressbar_VerNegociosXTipoParaReservarActivity);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        context = this;
        StringBuilder strbTipoNegocio = new StringBuilder();
        strbTipoNegocio.append(intent.getStringExtra(Constantes.TIPO_NEGOCIO_SELECCIONADO));
        if (strbTipoNegocio.equals(getString(R.string.str_tiponegocio_salonbelleza))) {
            strbTipoNegocio.append(getString(R.string.str_tiponegocio_salones_de_belleza));
        } else {
            strbTipoNegocio.append("s");
        }
        getSupportActionBar().setTitle(strbTipoNegocio);
        poblarListViewMisNegocios(intent.getStringExtra(Constantes.TIPO_NEGOCIO_SELECCIONADO));
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
                    DatabaseReference databaseReferenceNegocios = FirebaseDatabase.getInstance().getReference();
                    Query firebaseQuery = databaseReferenceNegocios.child(Constantes.MINEGOCIO_FIREBASE_BD).orderByChild("nombreNegocio").startAt("#" + newText).limitToFirst(100);
                    firebaseQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            lstMisNegocios = new ArrayList<MiNegocio>();
                            childreCount = dataSnapshot.getChildrenCount();
                            for (DataSnapshot child : dataSnapshot.getChildren()) {
                                MiNegocio miNegocio = child.getValue(MiNegocio.class);
                                if(miNegocio.getNombreNegocio().trim().toLowerCase().contains(newText.toLowerCase())){
                                    lstMisNegocios.add(miNegocio);
                                }
                            }
                            AdapterMisNegocios adapterMisNegocios = new AdapterMisNegocios(context, R.layout.layout_listview_misnegocios, lstMisNegocios);
                            listView.setAdapter(adapterMisNegocios);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Toast.makeText(context, "No se pudo obtener la informacion", Toast.LENGTH_SHORT).show();
                        }
                    });

                    listViewAddOnItemClickListener();
                }
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private void poblarListViewMisNegocios(final String tipoDeNegocio) {

        databaseReference.child(Constantes.TIPOS_NEGOCIOS_FIREBASE_BD).child(tipoDeNegocio).limitToLast(100).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                lstMisNegocios = new ArrayList<MiNegocio>();
                childreCount = dataSnapshot.getChildrenCount();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    final TipoNegocio tipoNegocio = child.getValue(TipoNegocio.class);

                    databaseReference.child(Constantes.MINEGOCIO_FIREBASE_BD).child(tipoNegocio.getNitNegocio()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            MiNegocio miNegocio = dataSnapshot.getValue(MiNegocio.class);
                            miNegocio.setTipoNegocio(tipoNegocio);
                            lstMisNegocios.add(miNegocio);
                            if (cantidadChildren == childreCount) {
                                AdapterMisNegocios adapterMisNegocios = new AdapterMisNegocios(context, R.layout.layout_listview_misnegocios, lstMisNegocios);
                                listView.setAdapter(adapterMisNegocios);
                                progressBar.setVisibility(View.GONE);
                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }

                    });
                    cantidadChildren++;

                }
                if (childreCount == 0) {
                    progressBar.setVisibility(View.GONE);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(context, "No se pudo obtener la informacion",
                        Toast.LENGTH_SHORT).show();
            }
        });

        listViewAddOnItemClickListener();
    }
    private void listViewAddOnItemClickListener(){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MiNegocio miNegocio =  lstMisNegocios.get(position);
                abrirActivityVerNegocioParaAgendar(miNegocio);
            }
        });

    }
    private void abrirActivityVerNegocioParaAgendar(MiNegocio miNegocio){
        Intent intent = new Intent(context, VerNegocioParaAgendarActivity.class);
        intent.putExtra(Constantes.MINEGOCIO_OBJECT, miNegocio);
        startActivity(intent);
    }
}
