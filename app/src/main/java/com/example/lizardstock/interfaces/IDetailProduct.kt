package com.example.lizardstock.interfaces

interface IDetailProduct {
    interface View {
        fun successMessage(success: Boolean)
    }

    interface Presenter {
        fun successMessage(success: Boolean)
        fun firebaseUpdate(codigo: String, nombre: String, cantidad: String,
                           precio: String, categoria: String)
    }

    interface Interactor {
        fun firebaseUpdate(codigo: String, nombre: String, cantidad: String,
                           precio: String, categoria: String)
    }
}