package com.example.lizardstock.presentador;

import android.content.Context;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.example.lizardstock.interactor.ListProductInteractor;
import com.example.lizardstock.interfaces.IListProduct;
import com.example.lizardstock.modelo.Product;

import java.util.List;

public class ListPresenter implements IListProduct.Presenter {

    private IListProduct.View view;
    private ListProductInteractor interactor;
    private Context mContext;

    public ListPresenter(IListProduct.View view, Context mContext) {
        this.view = view;
        this.mContext = mContext;
        this.interactor = new ListProductInteractor(this,mContext);
    }

    @Override
    public void fillRecyclerView(RecyclerView recyclerView, String categoria) {
        if (view != null) {
            interactor.fillRecyclerView(recyclerView,categoria);
        }
    }

    @Override
    public void addSuccess(boolean success) {
        if(view!=null){
            view.addSuccess(success);
        }
    }
}
