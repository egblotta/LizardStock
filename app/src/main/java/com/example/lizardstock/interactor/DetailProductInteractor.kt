package com.example.lizardstock.interactor

import com.example.lizardstock.interfaces.IDetailProduct
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.*

class DetailProductInteractor(private val presenter: IDetailProduct.Presenter) : IDetailProduct.Interactor {

    private val mDatabase: DatabaseReference = FirebaseDatabase.getInstance().reference

    override fun firebaseUpdate(codigo: String, nombre: String, cantidad: String, precio: String, categoria: String) {
        val product: MutableMap<String, Any> = HashMap()
        product["nombre"] = nombre
        product["cantidad"] = cantidad
        product["codigo"] = codigo
        product["precio"] = precio

        val postRef = mDatabase.child("Articulos").child(categoria).child(nombre)

        postRef.updateChildren(product)
                .addOnSuccessListener { presenter.successMessage(true) } //true
                .addOnFailureListener { presenter.successMessage(false) }
    }
}