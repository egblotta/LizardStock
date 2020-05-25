package com.example.lizardstock.presentador;

import android.net.Uri;

import com.example.lizardstock.interactor.AddProductInteractor;
import com.example.lizardstock.interfaces.IAddProduct;

public class AddPresenter implements IAddProduct.Presenter {

    private IAddProduct.View view;
    private AddProductInteractor interactor;

    public AddPresenter(IAddProduct.View view) {
        this.view = view;
        this.interactor = new AddProductInteractor(this);
    }

    @Override
    public void addSuccess(boolean success) {
        if (view != null) {
            view.addSuccess(success);
        }
    }

    @Override
    public void firebaseUpload(String categoria, String nombre, String cantidad, String codigo, String precio, Uri imagenUri) {
        if (view != null) {
            interactor.firebaseUpload(categoria, nombre, cantidad, codigo, precio, imagenUri);
        }

    }
}
