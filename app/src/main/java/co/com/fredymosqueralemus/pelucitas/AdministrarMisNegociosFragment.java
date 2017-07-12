package co.com.fredymosqueralemus.pelucitas;

import android.content.Intent;
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
import co.com.fredymosqueralemus.pelucitas.modelo.minegocio.NegocioXAdministrador;
import co.com.fredymosqueralemus.pelucitas.modelo.minegocio.TipoNegocio;
import co.com.fredymosqueralemus.pelucitas.sharedpreference.SharedPreferencesSeguro;
import co.com.fredymosqueralemus.pelucitas.sharedpreference.SharedPreferencesSeguroSingleton;


public class AdministrarMisNegociosFragment extends Fragment {

    private ListView listView;
    private List<MiNegocio> lstMisNegocios;
    private DatabaseReference databaseReference;
    private SharedPreferencesSeguro sharedPreferencesSeguro;
    private ProgressBar progressBar;
    private long childreCount = 0;
    private long cantidadChildren = 0;

    public void FragmentListviewMisnegocios() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup container, Bundle savedInstanceState) {
        View view = layoutInflater.inflate(R.layout.fragment_listview_misnegocios, container, false);
        listView = (ListView) view.findViewById(R.id.listview_fragment_listview_misnegocios);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar_fragmentListviewMisnegocios);
        sharedPreferencesSeguro = SharedPreferencesSeguroSingleton.getInstance(getContext(), Constantes.SHARED_PREFERENCES_INFOUSUARIO, Constantes.SECURE_KEY_SHARED_PREFERENCES);
        databaseReference = FirebaseDatabase.getInstance().getReference();

        poblarListViewMisNegocios();

        return view;
    }

    private void poblarListViewMisNegocios() {

        databaseReference.child(Constantes.MINEGOCIO_X_ADMON_FIREBASE_BD).child(sharedPreferencesSeguro.getString(Constantes.USERUID)).limitToLast(100).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                lstMisNegocios = new ArrayList<MiNegocio>();
                childreCount = dataSnapshot.getChildrenCount();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    final NegocioXAdministrador negocioXAdministrador = child.getValue(NegocioXAdministrador.class);
                    DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference();
                    databaseReference1.child(Constantes.MINEGOCIO_FIREBASE_BD).child(negocioXAdministrador.getNitNegocio()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            MiNegocio miNegocio = dataSnapshot.getValue(MiNegocio.class);
                            lstMisNegocios.add(miNegocio);
                            if (cantidadChildren == childreCount) {
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
                if (childreCount == 0) {
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
