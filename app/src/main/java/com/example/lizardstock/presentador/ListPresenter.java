package com.example.lizardstock.presentador;

import androidx.recyclerview.widget.RecyclerView;

import com.example.lizardstock.interactor.ListProductInteractor;
import com.example.lizardstock.interfaces.IListProduct;

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
    public void successMessage(Boolean success) {
        if(view!=null){
            view.successMessage(success);
        }
    }

}
