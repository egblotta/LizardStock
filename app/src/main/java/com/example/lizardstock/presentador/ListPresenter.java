package com.example.lizardstock.presentador;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.example.lizardstock.interactor.ListProductInteractor;
import com.example.lizardstock.interfaces.IListProduct;
import com.example.lizardstock.modelo.Product;

import java.util.ArrayList;

public class ListPresenter implements IListProduct.Presenter {

    private IListProduct.View view;
    private ListProductInteractor interactor;

    public ListPresenter(IListProduct.View view) {
        this.view = view;
        this.interactor = new ListProductInteractor(this);
    }

    @Override
    public void fillRecyclerView(RecyclerView recyclerView, String categoria) {
        if (view != null) {
            interactor.fillRecyclerView(recyclerView,categoria);
        }
    }

    @Override
    public void deleteItem(String categoria, String nombre) {
        if(view!=null){
            interactor.deleteItem(categoria,nombre);
        }
    }

    @Override
    public void openDeleteDialog(View v, ArrayList<Product> mProducts, RecyclerView recyclerView) {
        if (view != null) {
            view.openDeleteDialog(v,mProducts,recyclerView);
        }
    }

    @Override
    public void errorMessage(Boolean error) {
        if (view != null) {
            view.errorMessage(error);
        }
    }

    @Override
    public void openUpdateDialog(View v, ArrayList<Product> mProducts, RecyclerView recyclerView) {
        if (view != null) {
            view.openUpdateDialog(v,mProducts,recyclerView);
        }
    }

    @Override
    public void successMessage(Boolean success) {
        if (view != null) {
            view.errorMessage(success);
        }
    }

}
