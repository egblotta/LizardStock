package com.example.lizardstock.interactor;

import android.net.Uri;

import androidx.annotation.NonNull;

import com.example.lizardstock.interfaces.IAddProduct;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class AddProductInteractor implements IAddProduct.Interactor {

    private StorageReference mStorage;
    private DatabaseReference mDatabase;
    private IAddProduct.Presenter presenter;


    public AddProductInteractor(IAddProduct.Presenter presenter) {
        this.presenter = presenter;
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mStorage = FirebaseStorage.getInstance().getReference();
    }

    public void firebaseUpload(final String codigo, final String nombre, final String cantidad,
                                final String precio, final String categoria, final Uri imagenUri){
        if(imagenUri!=null){
            final StorageReference imageRef = mStorage.child(categoria).child(nombre);
            imageRef.putFile(imagenUri).continueWithTask(task -> {
                if(!task.isSuccessful()){
                    throw new Exception();
                }
                return imageRef.getDownloadUrl();
            }).addOnCompleteListener(task -> {

                if(task.isSuccessful()){

                    Uri downloadLink = task.getResult();
                    Map<String,Object> product = new HashMap<>();

                    product.put("nombre",nombre);
                    product.put("cantidad",cantidad);
                    product.put("codigo",codigo);
                    product.put("precio",precio);
                    assert downloadLink != null;
                    product.put("imagenUrl", downloadLink.toString());

                    DatabaseReference postRef = mDatabase.child("Articulos").child(categoria).child(nombre);
                    postRef.setValue(product)
                            .addOnSuccessListener(task1 -> presenter.addSuccess(true))        //true
                            .addOnFailureListener(e -> presenter.addSuccess(false));           //false
                }
            });
        }
    }
}
