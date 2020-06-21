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

import java.util.List;

import butterknife.BindAnim;
import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;

public class AddFragmentView extends Fragment implements View.OnClickListener, IAddProduct.View {

    @BindViews({R.id.etCodigoAdd,R.id.etNombreAdd,R.id.etCantidadAdd,R.id.etPrecioAdd})
    List<EditText> etViews;
    @BindView(R.id.btnAdd)
    Button btnNuevo;
    @BindView(R.id.imgProductoAdd)
    ImageView imgProducto;
    @BindView(R.id.spnCategoriaAdd)
    Spinner spnCategoria;
    @BindView(R.id.progressAdd)
    ProgressBar progressAdd;

    private IAddProduct.Presenter presenter;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imageUri;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_product, container, false);
        ButterKnife.bind(this, view);

        ArrayAdapter<CharSequence> spnAdapter = ArrayAdapter.createFromResource(requireContext(),
                R.array.categorias, android.R.layout.simple_spinner_item);
        spnAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnCategoria.setAdapter(spnAdapter);

        btnNuevo.setOnClickListener(this);
        imgProducto.setOnClickListener(this);

        presenter = new AddPresenter(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imgProductoAdd:
                openFileChooser();
                break;
            case R.id.btnAdd:
                addProduct();
                break;
        }
    }

    //Abre la galeria del dispositivo
    public void openFileChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");      //solo los tipos "image" son seleccionados
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_IMAGE_REQUEST);
    }

    //Muestra la imagen seleccionada en el imgView y setea el nombre de la imagen al campo codigo
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData()  != null){
            imageUri = data.getData();
                try {
                    imgProducto.setImageURI(imageUri);
                    etViews.get(0).setText(getImageName(getContext(), imageUri));
                } catch (Exception e) {
                    e.printStackTrace();
                }
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
                fileName = fileName.substring(0, fileName.length()-4);      //elimino los ultimos 4 caracteres del String
            }
        }
        return fileName;
    }

    private void addProduct() {
        progressAdd.setVisibility(View.VISIBLE);
        String codigo = etViews.get(0).getText().toString().trim();
        String nombre = etViews.get(1).getText().toString().trim();
        String cantidad = etViews.get(2).getText().toString().trim();
        String precio = etViews.get(3).getText().toString().trim();
        String categoria = spnCategoria.getSelectedItem().toString().trim();

        if(!TextUtils.isEmpty(etViews.get(0).getText().toString())
                && !TextUtils.isEmpty(etViews.get(1).getText().toString())
                && !TextUtils.isEmpty(etViews.get(2).getText().toString())
                && !TextUtils.isEmpty(etViews.get(3).getText().toString())){
            presenter.firebaseUpload(codigo, nombre, cantidad, precio, categoria, imageUri);
            cleanFields();
        }else{
            Toast.makeText(getContext(), "Ningun campo puede estar vacio.", Toast.LENGTH_SHORT).show();
            progressAdd.setVisibility(View.GONE);
        }
    }

    @Override
    public void addSuccess(boolean success) {
        if(success)
            Toast.makeText(getContext(), "Articulo cargado.", Toast.LENGTH_SHORT).show();
        progressAdd.setVisibility(View.GONE);
    }

    private void cleanFields() {
        etViews.get(0).setText("");
        etViews.get(1).setText("");
        etViews.get(2).setText("");
        etViews.get(3).setText("");
        imgProducto.setImageResource(R.drawable.ic_image_black_24dp);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
