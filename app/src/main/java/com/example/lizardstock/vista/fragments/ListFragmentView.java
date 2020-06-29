package com.example.lizardstock.vista.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lizardstock.R;
import com.example.lizardstock.interfaces.IListProduct;
import com.example.lizardstock.modelo.Product;
import com.example.lizardstock.presentador.ListPresenter;
import com.example.lizardstock.modelo.utilidades.Utilidades;
import com.example.lizardstock.vista.activities.MainActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ListFragmentView extends Fragment implements IListProduct.View{

    @BindView(R.id.spnLista)
    Spinner spnLista;
    @BindView(R.id.swLista)
    Switch mSwitch;
    @BindView(R.id.imgSinConexion)
    ImageView imagenSc;

    FloatingActionButton fab;

    IListProduct.Presenter presenter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_list_product, container, false);
        ButterKnife.bind(this, view);

        ArrayAdapter<CharSequence> spnAdapter = ArrayAdapter.createFromResource(requireContext(),
                R.array.categorias, android.R.layout.simple_spinner_item);
        spnAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnLista.setAdapter(spnAdapter);

        imagenSc.setVisibility(View.INVISIBLE);

        fabView();
        checkInternet(view);
        return view;
    }

    public ListFragmentView() {
        // Required empty public constructor
    }

    private void switchOn(final View view){
        mSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked){
                Utilidades.visualizacion=Utilidades.LIST;
            }else{
                Utilidades.visualizacion=Utilidades.GRID;
            }
            optionSpinner(view);
        });
    }

    private void optionSpinner(final View view){
        spnLista.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
                initRecycler(view,spnLista.getSelectedItem().toString());
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //
            }
        });
    }

    private void initRecycler(View v, String categoria) {
        RecyclerView mRecyclerView = v.findViewById(R.id.recyclerProducts);
        mRecyclerView.setHasFixedSize(true);
        presenter = new ListPresenter(this);

        if(Utilidades.visualizacion==Utilidades.LIST){
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
            mRecyclerView.setLayoutManager(linearLayoutManager);
        }else {
            GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),2);
            mRecyclerView.setLayoutManager(gridLayoutManager);
        }
        presenter.fillRecyclerView(mRecyclerView, categoria);
    }

    @Override
    public void openDeleteDialog(View v, ArrayList<Product> mProducts , RecyclerView recyclerView){
        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
        builder.setTitle("Eliminar articulo?");

        String categoria = spnLista.getSelectedItem().toString();
        String nombre = mProducts.get(recyclerView.getChildAdapterPosition(v)).getNombre();

        final DialogInterface.OnClickListener dialogListener = (dialog, which) -> {

            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    presenter.deleteItem(categoria,nombre);
                    break;
                case DialogInterface.BUTTON_NEGATIVE:
                    dialog.dismiss();
                    break;
            }
        };
        builder.setPositiveButton(R.string.Si,dialogListener);
        builder.setNegativeButton(R.string.No, dialogListener);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void errorMessage(Boolean error) {
        Toast.makeText(getContext(), "Error al cargar la lista.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void openUpdateDialog(View v, ArrayList<Product> mProducts , RecyclerView recyclerView) {
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        Fragment prev = getChildFragmentManager().findFragmentById(R.id.detailFragment);
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        String codigo = mProducts.get(recyclerView.getChildAdapterPosition(v)).getCodigo();
        String nombre = mProducts.get(recyclerView.getChildAdapterPosition(v)).getNombre();
        String cantidad = mProducts.get(recyclerView.getChildAdapterPosition(v)).getCantidad();
        String precio = mProducts.get(recyclerView.getChildAdapterPosition(v)).getPrecio();
        String categoria = mProducts.get(recyclerView.getChildAdapterPosition(v)).getCategoria();
        String imagen = mProducts.get(recyclerView.getChildAdapterPosition(v)).getImagenUrl();

        Bundle args = new Bundle();
        args.putString("codigo",codigo);
        args.putString("nombre",nombre);
        args.putString("cantidad",cantidad);
        args.putString("precio",precio);
        args.putString("categoria",categoria);
        args.putString("imagen",imagen);

        DialogFragment dialog = new DetailFragmentView();
        dialog.setArguments(args);
        dialog.show(ft,"Detail Fragment");
    }

    @Override
    public void successMessage(Boolean success) {
        if(success)
            Toast.makeText(getContext(), "Articulo eliminado.", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(getContext(), "Error al eliminar.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroyView() {super.onDestroyView();}

    public void fabView(){
        fab = ((MainActivity) requireActivity()).getFab();
        if(fab!=null){
            ((MainActivity) requireActivity()).fabShow();
        }
    }

    private void checkInternet(View view){
        ConnectivityManager cm = (ConnectivityManager) requireContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        assert cm != null;
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();      //describe el estado de red
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        if(isConnected){
            switchOn(view);
            optionSpinner(view);
        }
        else{
            imagenSc.setVisibility(View.VISIBLE);
            fab.hide();
            mSwitch.setVisibility(View.GONE);
            spnLista.setVisibility(View.GONE);
            Toast.makeText(getContext(), "No hay conexion a internet", Toast.LENGTH_SHORT).show();
        }

    }
}
