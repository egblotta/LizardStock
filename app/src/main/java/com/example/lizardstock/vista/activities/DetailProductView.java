package com.example.lizardstock.vista.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.example.lizardstock.R;
import com.example.lizardstock.interfaces.IDetailProduct;
import com.example.lizardstock.presentador.AddPresenter;
import com.example.lizardstock.presentador.DetailPresenter;

import static android.view.View.*;

public class DetailProductView extends AppCompatActivity implements OnClickListener, IDetailProduct.View {

    private EditText etNombre, etCantidad, etCodigo, etPrecio;
    private ImageView imgProducto;
    private Spinner spnCategoria;
    private ProgressBar progressAdd;
    private IDetailProduct.Presenter presenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_product);

        etCodigo = findViewById(R.id.etCodigo);
        etNombre = findViewById(R.id.etNombre);
        etCantidad = findViewById(R.id.etCantidad);
        etPrecio = findViewById(R.id.etPrecio);
        spnCategoria =findViewById(R.id.spnCategoria);
        progressAdd = findViewById(R.id.progressAdd);

        ArrayAdapter<CharSequence> spnAdapter = ArrayAdapter.createFromResource(this,
                R.array.categorias, android.R.layout.simple_spinner_item);
        spnAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnCategoria.setAdapter(spnAdapter);

        presenter = new DetailPresenter(this);
        Button btnModificar = findViewById(R.id.btnUpdate);
        btnModificar.setOnClickListener(this);
        Button btnEliminar = findViewById(R.id.btnDelete);
        btnEliminar.setOnClickListener(this);
    }

    @Override
    public void addSuccess(boolean success) {
        if(success)
            Toast.makeText(this, "Articulo eliminado.", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(this, "Error al eliminar el producto.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnUpdate:
                updateProduct();
                break;
            case R.id.btnDelete:
                deleteProduct();
        }

    }

    void deleteProduct(){
        String nombre = etNombre.getText().toString().trim();
        String categoria = spnCategoria.getSelectedItem().toString().trim();
        presenter.firebaseDelete(categoria,nombre);
    }

    void updateProduct(){
        progressAdd.setVisibility(View.VISIBLE);
        String nombre = etNombre.getText().toString().trim();
        String cantidad = etCantidad.getText().toString().trim();
        String codigo = etCodigo.getText().toString().trim();
        String precio = etPrecio.getText().toString().trim();
        String categoria = spnCategoria.getSelectedItem().toString().trim();
       // presenter.firebaseUpdate(categoria, nombre, cantidad, codigo, precio);
    }
}
