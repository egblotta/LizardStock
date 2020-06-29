package com.example.lizardstock.presentador

import com.example.lizardstock.interactor.DetailProductInteractor
import com.example.lizardstock.interfaces.IDetailProduct

class DetailsPresenter(private val view: IDetailProduct.View?) : IDetailProduct.Presenter {

    private val interactor: DetailProductInteractor = DetailProductInteractor(this)

    override fun firebaseUpdate(codigo: String, nombre: String, cantidad: String, precio: String, categoria: String) {
        if (view != null) {
            interactor.firebaseUpdate(codigo, nombre, cantidad, precio, categoria)
        }
    }

    override fun successMessage(success: Boolean) {
        view?.successMessage(success)
    }

}