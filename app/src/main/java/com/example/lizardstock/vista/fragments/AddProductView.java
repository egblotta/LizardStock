package com.example.lizardstock.vista.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.lizardstock.R;
import com.example.lizardstock.interfaces.IAddProduct;
import com.example.lizardstock.presentador.AddPresenter;

public class AddProductView extends Fragment implements View.OnClickListener, IAddProduct.View {

    private EditText etNombre, etCantidad, etCodigo, etPrecio;
    private ImageView imgProducto;
    private Spinner spnCategoria;
    private ProgressBar progressAdd;
    private IAddProduct.Presenter presenter;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imageUri;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_product, container, false);

        etCodigo = view.findViewById(R.id.etCodigo);
        etNombre = view.findViewById(R.id.etNombre);
        etCantidad = view.findViewById(R.id.etCantidad);
        etPrecio = view.findViewById(R.id.etPrecio);
        spnCategoria = view.findViewById(R.id.spnCategoria);
        progressAdd = view.findViewById(R.id.progressAdd);

        ArrayAdapter<CharSequence> spnAdapter = ArrayAdapter.createFromResource(requireContext(),
                R.array.categorias, android.R.layout.simple_spinner_item);
        spnAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnCategoria.setAdapter(spnAdapter);

        //Buttons
        Button btnNuevo = view.findViewById(R.id.btnAdd);
        btnNuevo.setOnClickListener(this);
        imgProducto = view.findViewById(R.id.imgProducto);
        imgProducto.setOnClickListener(this);
        presenter = new AddPresenter(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imgProducto:
                openFileChooser();
                break;
            case R.id.btnAdd:
                addProduct();
                break;
        }
    }

    //Abre la galeria del dispositivo
    private void openFileChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");      //solo los tipos "image" son seleccionados
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_IMAGE_REQUEST);
    }

    private void addProduct() {
        progressAdd.setVisibility(View.VISIBLE);
        String nombre = etNombre.getText().toString().trim();
        String cantidad = etCantidad.getText().toString().trim();
        String codigo = etCodigo.getText().toString().trim();
        String precio = etPrecio.getText().toString().trim();
        String categoria = spnCategoria.getSelectedItem().toString().trim();

        if(!TextUtils.isEmpty(etNombre.getText().toString())
                && !TextUtils.isEmpty(etCodigo.getText().toString())
                && !TextUtils.isEmpty(etPrecio.getText().toString())
                && !TextUtils.isEmpty(etCantidad.getText().toString())){
            presenter.firebaseUpload(categoria, nombre, cantidad, codigo, precio, imageUri);
            cleanFields();
        }else{
            Toast.makeText(getContext(), "Ningun campo puede estar vacio.", Toast.LENGTH_SHORT).show();
            progressAdd.setVisibility(View.GONE);
        }

    }

    //Devuelve el nombre de la imagen
    private String getImageName(Context context, Uri uri){
        String fileName = null;
        try (Cursor cursor = context.getContentResolver()
                .query(uri, null, null, null, null, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                // get file name
                fileName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
            }
        }
        return fileName;
    }

    private void cleanFields() {
        etCodigo.setText("");
        etNombre.setText("");
        etCantidad.setText("");
        etPrecio.setText("");
        imgProducto.setImageResource(R.drawable.ic_image_black_24dp);
    }

    //Muestra la imagen seleccionada en el imgView y setea el nombre de la imagen al campo codigo
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData()  != null){
            imageUri = data.getData();
                try {
                    imgProducto.setImageURI(imageUri);
                    etCodigo.setText(getImageName(getContext(), imageUri));
                } catch (Exception e) {
                    e.printStackTrace();
                }
        }
    }

    @Override
    public void addSuccess(boolean success) {
            if(success)
                Toast.makeText(getContext(), "Articulo cargado.", Toast.LENGTH_SHORT).show();
            progressAdd.setVisibility(View.GONE);
        }

}
