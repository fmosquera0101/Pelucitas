package co.com.fredymosqueralemus.pelucitas;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import co.com.fredymosqueralemus.pelucitas.adapters.AdapterMisNegocios;
import co.com.fredymosqueralemus.pelucitas.constantes.Constantes;
import co.com.fredymosqueralemus.pelucitas.modelo.minegocio.MiNegocio;
import co.com.fredymosqueralemus.pelucitas.modelo.minegocio.TipoNegocio;


public class VisualizarTiposDeNegociosFragment extends Fragment {

    private ListView listView;
    private List<MiNegocio> lstMisNegocios;
    private ProgressBar progressBar;
    private DatabaseReference databaseReference;
    private long childreCount = 0;
    private long cantidadChildren = 0;
    public VisualizarTiposDeNegociosFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fragment_visualizar_tipos_de_negocios, container, false);
        listView = (ListView) view.findViewById(R.id.listview_misnegocios_FragmentVisualizarTiposDeNegocios);
        progressBar = (ProgressBar) view.findViewById(R.id.progressbar_FragmentVisualizarTiposDeNegocios);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        Bundle bundle = getArguments();

        poblarListViewMisNegocios(bundle.getString(Constantes.TIPO_NEGOCIO_SELECCIONADO));
        return view;
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
                            if(cantidadChildren == childreCount) {
                                AdapterMisNegocios adapterMisNegocios = new AdapterMisNegocios(getContext(), R.layout.layout_listview_misnegocios, lstMisNegocios);
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
                if(childreCount == 0){
                    progressBar.setVisibility(View.GONE);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getContext(), "No se pudo obtener la informacion",
                        Toast.LENGTH_SHORT).show();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), AdministrarMiNegocioActivity.class);
                intent.putExtra(Constantes.MINEGOCIO_OBJECT, lstMisNegocios.get(position));
                startActivity(intent);
            }
        });


    }


}
