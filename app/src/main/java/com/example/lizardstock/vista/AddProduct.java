package com.example.lizardstock.vista;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.lizardstock.R;
import com.example.lizardstock.presentador.AddPresenter;
import com.example.lizardstock.presentador.ListPresenter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.Objects;

public class AddProduct extends Fragment implements View.OnClickListener {

    private EditText etNombre, etCantidad, etCodigo, etPrecio;
    private ImageView imgProducto;
    private Spinner spnCategoria;
    private AddPresenter addPresenter;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imageUri;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_product, container, false);

        //Referencias firebase
        StorageReference mStorage = FirebaseStorage.getInstance().getReference();
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        addPresenter = new AddPresenter(getContext(), mStorage, mDatabase);

        etCodigo = view.findViewById(R.id.etCodigo);
        etNombre = view.findViewById(R.id.etNombre);
        etCantidad = view.findViewById(R.id.etCantidad);
        etPrecio = view.findViewById(R.id.etPrecio);
        spnCategoria = view.findViewById(R.id.spnCategoria);

        ArrayAdapter<CharSequence> spnAdapter = ArrayAdapter.createFromResource(requireContext(),
                R.array.categorias, android.R.layout.simple_spinner_item);
        spnAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnCategoria.setAdapter(spnAdapter);

        //Progress Bar
        ProgressBar progressBar = view.findViewById(R.id.progressAdd);

        //Buttons
        Button btnNuevo = view.findViewById(R.id.btnAdd);
        btnNuevo.setOnClickListener(this);
        imgProducto = view.findViewById(R.id.imgProducto);
        imgProducto.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.imgProducto:
                openFileChooser();
                break;
            case R.id.btnAdd:
                String nombre = etNombre.getText().toString().trim();
                String cantidad = etCantidad.getText().toString().trim();
                String codigo = etCodigo.getText().toString().trim();
                String precio = etPrecio.getText().toString().trim();
                String categoria = spnCategoria.getSelectedItem().toString().trim();
                addPresenter.firebaseUpload(categoria, nombre, cantidad,codigo,precio,imageUri);
                limpiarCampos();
                break;
        }
    }

    //Abre la galeria
    private void openFileChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");      //solo los tipos "image" son seleccionados
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_IMAGE_REQUEST);
    }

    //Devuelve el nombre de la imagen
    private String getImageName(Context context, Uri uri){
        String fileName = null;
        try (Cursor cursor = context.getContentResolver()
                .query(uri, null, null, null, null, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                // get file name
                fileName = cursor.getString(
                        cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
            }
        }
        return fileName;
    }

    //Muestra la imagen seleccionada en el imgView
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

    private void limpiarCampos() {
        etCodigo.setText("");
        etNombre.setText("");
        etCantidad.setText("");
        etPrecio.setText("");
        imgProducto.setImageResource(R.drawable.ic_image_black_24dp);
    }

}
