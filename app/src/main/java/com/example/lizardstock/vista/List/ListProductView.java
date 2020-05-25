package com.example.lizardstock.vista.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lizardstock.R;
import com.example.lizardstock.interfaces.IListProduct;
import com.example.lizardstock.presentador.ListPresenter;

public class ListProductView extends Fragment implements IListProduct.View {

    private IListProduct.Presenter presenter;
    private Spinner spnCategoria;


    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_list_product, container, false);

        spnCategoria = view.findViewById(R.id.spnLista);

        ArrayAdapter<CharSequence> spnAdapter = ArrayAdapter.createFromResource(requireContext(),
                R.array.categorias, android.R.layout.simple_spinner_item);
        spnAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnCategoria.setAdapter(spnAdapter);

        ImageView imagenSc = view.findViewById(R.id.imagen_sinconexion);
        imagenSc.setVisibility(View.INVISIBLE);
        presenter = new ListPresenter(this,getContext());

        spnCategoria.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
                initRecycler(view,spnCategoria.getSelectedItem().toString());
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return view;
    }


    private void initRecycler(View view, String categoria) {
        try {
            RecyclerView mRecyclerView = view.findViewById(R.id.recyclerProducts);
            mRecyclerView.setHasFixedSize(true);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
            mRecyclerView.setLayoutManager(layoutManager);
            presenter.fillRecyclerView(mRecyclerView, categoria);
        }catch (Exception e){
            e.getCause();
        }
    }

    @Override
    public void addSuccess(boolean success) {
            if(success)
                Toast.makeText(getContext(), "Lista cargada.", Toast.LENGTH_SHORT).show();
    }
}
