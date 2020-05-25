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

    public void firebaseUpload(final String categoria, final String nombre, final String cantidad,
                               final String codigo, final String precio, final Uri imagenUri){

        if(imagenUri!=null){
            final StorageReference imageRef = mStorage.child(categoria).child(codigo.trim());
            imageRef.putFile(imagenUri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {

                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if(!task.isSuccessful()){
                        throw new Exception();
                    }
                    return imageRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {

                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful()){

                        Uri downloadLink = task.getResult();
                        Map<String,Object> product = new HashMap<>();
                        product.put("nombre",nombre);
                        product.put("cantidad",cantidad);
                        product.put("codigo",codigo);
                        product.put("precio",precio);
                        product.put("categoria",categoria);

                        assert downloadLink != null;
                        product.put("imagenUrl", downloadLink.toString());
                        DatabaseReference postRef = mDatabase.child("Articulos").child(categoria).child(nombre);
                        postRef.updateChildren(product).addOnCompleteListener(new OnCompleteListener<Void>() {

                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                              presenter.addSuccess(true);
                            }
                        }).addOnFailureListener(new OnFailureListener() {

                            @Override
                            public void onFailure(@NonNull Exception e) {
                                presenter.addSuccess(false);
                            }
                        });
                    }
                }
            });
        }
    }
}
