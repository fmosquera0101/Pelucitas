package co.com.fredymosqueralemus.pelucitas;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import co.com.fredymosqueralemus.pelucitas.modelo.usuario.Usuario;

public class FragmentListViewMisEmpleados extends Fragment {


    private ListView listView;
    private ProgressBar progressBar;
    private List<Usuario> listUsuarios;
    private DatabaseReference databaseReference;
    public FragmentListViewMisEmpleados() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup container, Bundle savedInstanceState){
        View view = layoutInflater.inflate(R.layout.fragment_listview_mis_empleados, container, false);
        listView = (ListView) view.findViewById(R.id.listview_fragment_listview_misempleados);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar_FragmentListViewMisEmpleados);
        progressBar.setVisibility(View.INVISIBLE);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        Intent intent = getActivity().getIntent();
        List<Usuario> listUsuario = (List)intent.getSerializableExtra("empleados");
        return view;

    }

}
