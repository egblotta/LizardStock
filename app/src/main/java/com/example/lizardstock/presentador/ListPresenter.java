package com.example.lizardstock.presentador;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lizardstock.R;
import com.example.lizardstock.adaptador.RecyclerProductAdapter;
import com.example.lizardstock.modelo.Product;
import com.example.lizardstock.vista.AddProduct;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class ListPresenter implements View.OnClickListener {

    private Context mContext;
    private EditText etNombre, etCantidad, etCodigo, etPrecio;
    private Spinner spnCategoria;
    private ImageView imgProducto;
    private StorageReference mStorage;
    private DatabaseReference mDatabase;
    private List<Product> mProducts;
    private RecyclerProductAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private Dialog dialog;

    public ListPresenter(Context mContext, StorageReference mStorage, DatabaseReference mDatabase) {
        this.mContext = mContext;
        this.mStorage = mStorage;
        this.mDatabase = mDatabase;
    }

    public ListPresenter() {}

    public void fillRecyclerView(final RecyclerView recyclerView) {

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("Articulos").push().push().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                mProducts = new ArrayList<>();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Product product = postSnapshot.getValue(Product.class);
                    mProducts.add(product);
                }
                mAdapter = new RecyclerProductAdapter(mContext, mProducts, R.layout.item_list);
                recyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(mContext, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void productDialog() {
        dialog = new Dialog(mContext);
        dialog.setContentView(R.layout.fragment_add_product);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        etCodigo = dialog.findViewById(R.id.etCodigo);
        etNombre = dialog.findViewById(R.id.etNombre);
        etCantidad = dialog.findViewById(R.id.etCantidad);
        etPrecio = dialog.findViewById(R.id.etPrecio);
        spnCategoria = dialog.findViewById(R.id.spnCategoria);
        Button btnNuevo = dialog.findViewById(R.id.btnAdd);
        btnNuevo.setOnClickListener(this);

        ArrayAdapter<CharSequence> spnAdapter = ArrayAdapter.createFromResource(mContext,
                R.array.categorias, android.R.layout.simple_spinner_item);
        spnAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnCategoria.setAdapter(spnAdapter);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgProducto:
                //addProduct.openFileChooser();
                break;
            case R.id.btnAdd:
                String nombre = etNombre.getText().toString().trim();
                String cantidad = etCantidad.getText().toString().trim();
                String codigo = etCodigo.getText().toString().trim();
                String precio = etPrecio.getText().toString().trim();
                String categoria = spnCategoria.getSelectedItem().toString().trim();
                //addPresenter.firebaseUpload(categoria, nombre, cantidad,codigo,precio,imageUri);
                //addProduct.limpiarCampos();
                break;
        }
    }
}
