package com.example.lizardstock.presentador;

import android.net.Uri;

import com.example.lizardstock.interactor.DetailProductInteractor;
import com.example.lizardstock.interfaces.IDetailProduct;

public class DetailPresenter implements IDetailProduct.Presenter {

    private IDetailProduct.View view;
    private IDetailProduct.Interactor interactor;

    public DetailPresenter(IDetailProduct.View view) {
        this.view = view;
        this.interactor = new DetailProductInteractor(this);
    }

    @Override
    public void addSuccess(boolean success) {
        if (view != null) {
            view.addSuccess(success);
        }
    }

    @Override
    public void firebaseUpdate(String categoria, String nombre, String cantidad, String codigo, String precio, Uri imagenUri) {
        if (view != null) {
            interactor.firebaseUpdate(categoria, nombre, cantidad, codigo, precio, imagenUri);
        }
    }

    @Override
    public void firebaseDelete(String categoria, String nombre) {
        if (view != null) {
            interactor.firebaseDelete(categoria,nombre);
        }
    }
}
