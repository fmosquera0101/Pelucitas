package co.com.fredymosqueralemus.pelucitas;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import co.com.fredymosqueralemus.pelucitas.adapters.AdapterMisNegocios;
import co.com.fredymosqueralemus.pelucitas.modelo.minegocio.MiNegocio;


public class FragmentListviewMisnegocios extends Fragment {

    private ListView listView;
    private List<MiNegocio> lstMisNegocios;

    public void FragmentListviewMisnegocios(){

    }
    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup container, Bundle savedInstanceState){
        View view =  layoutInflater.inflate(R.layout.fragment_listview_misnegocios, container, false);
        listView = (ListView) view.findViewById(R.id.listview_fragment_listview_misnegocios);
        lstMisNegocios = new ArrayList<MiNegocio>();
        MiNegocio miNegocio = new MiNegocio();
        lstMisNegocios.add(miNegocio);
        AdapterMisNegocios adapterMisNegocios = new AdapterMisNegocios(getContext(),R.layout.layout_listview_misnegocios, lstMisNegocios);
        listView.setAdapter(adapterMisNegocios);

        return view;
    }
}
