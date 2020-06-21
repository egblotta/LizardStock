package com.example.lizardstock.vista.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.OpenableColumns;
import android.text.TextUtils;
import android.util.Log;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.lizardstock.R;
import com.example.lizardstock.interactor.DetailProductInteractor;
import com.example.lizardstock.interfaces.IDetailProduct;
import com.example.lizardstock.modelo.Product;
import com.example.lizardstock.presentador.DetailsPresenter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;

public class DetailFragment extends DialogFragment implements IDetailProduct.View, View.OnClickListener {

    @BindViews({R.id.etCodigoUpd,R.id.etNombreUpd,R.id.etCantidadUpd,R.id.etPrecioUpd})
    List<EditText> etViews;
    @BindView(R.id.btnUpdate)
    Button btnModificar;
    @BindView(R.id.imgProductoUpd)
    ImageView imgProductoUpd;
    @BindView(R.id.spnCategoriaUpd)
    Spinner spnCategoriaUpd;
    @BindView(R.id.progressUpd)
    ProgressBar progressBar;

    private IDetailProduct.Presenter presenter;
    private Uri imageUri;
    private static final int PICK_IMAGE_REQUEST = 1;

    public DetailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        ButterKnife.bind(this, view);


        ArrayAdapter<CharSequence> spnAdapter = ArrayAdapter.createFromResource(requireContext(),
                R.array.categorias, android.R.layout.simple_spinner_item);
        spnAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnCategoriaUpd.setAdapter(spnAdapter);

        btnModificar.setOnClickListener(this);
        imgProductoUpd.setOnClickListener(this);
        presenter = new DetailsPresenter(this);

        getData();
        return view;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.imgProductoUpd:
                openFileChooser();
                break;
            case R.id.btnUpdate:
                sendData();
                break;
        }
    }

    public void getData()
    {
        Bundle mArgs = getArguments();

        assert mArgs != null;
        etViews.get(0).setText(mArgs.getString("codigo"));
        etViews.get(1).setText(mArgs.getString("nombre"));
        etViews.get(2).setText(mArgs.getString("cantidad"));
        etViews.get(3).setText(mArgs.getString("precio"));

        imageUri = Uri.parse(mArgs.getString("imagen"));

        //Carga la imagen en el imageView usando Glide
        Glide.with(requireContext())
                .load(imageUri)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        imgProductoUpd.setVisibility(View.VISIBLE);
                        imgProductoUpd.setImageResource(R.drawable.ic_error_black_24dp);
                        return false;
                    }
                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        imgProductoUpd.setVisibility(View.VISIBLE);
                        return false;
                    }
                }).into(imgProductoUpd);
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
                //imgProductoUpd.setImageURI(imageUri);
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

    private void sendData() {
        progressBar.setVisibility(View.VISIBLE);
        String codigo = etViews.get(0).getText().toString().trim();
        String nombre = etViews.get(1).getText().toString().trim();
        String cantidad = etViews.get(2).getText().toString().trim();
        String precio = etViews.get(3).getText().toString().trim();
        String categoria = spnCategoriaUpd.getSelectedItem().toString().trim();

        if(!TextUtils.isEmpty(etViews.get(0).getText().toString())
                && !TextUtils.isEmpty(etViews.get(1).getText().toString())
                && !TextUtils.isEmpty(etViews.get(2).getText().toString())
                && !TextUtils.isEmpty(etViews.get(3).getText().toString())){
            presenter.firebaseUpdate(codigo, nombre, cantidad, precio, categoria, imageUri);
        }else{
            Toast.makeText(getContext(), "Ningun campo puede estar vacio.", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void successMessage(Boolean success) {
        if(success){
            Toast.makeText(getContext(), "Articulo modificado.", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
        }else{
            Toast.makeText(getContext(), "Error al modificar los datos.", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDestroyView() {super.onDestroyView();}

}