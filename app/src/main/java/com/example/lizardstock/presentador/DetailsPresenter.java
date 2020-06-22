package com.example.lizardstock.presentador;

import android.net.Uri;

import com.example.lizardstock.interactor.DetailProductInteractor;
import com.example.lizardstock.interfaces.IDetailProduct;

public class DetailsPresenter implements IDetailProduct.Presenter {

    private IDetailProduct.View view;
    private DetailProductInteractor interactor;

    public DetailsPresenter(IDetailProduct.View view) {
        this.view = view;
        this.interactor = new DetailProductInteractor(this);
    }

    @Override
    public void firebaseUpdate(final String codigo, final String nombre, final String cantidad,
                               final String precio, final String categoria) {
        if (view != null) {
            interactor.firebaseUpdate(codigo, nombre, cantidad, precio, categoria);
        }
    }

    @Override
    public void successMessage(Boolean success) {
        if (view != null) {
            view.successMessage(success);
        }
    }

}
