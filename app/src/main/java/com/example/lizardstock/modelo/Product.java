package com.example.lizardstock.modelo;

public class Product {

    private String categoria;
    private String codigo;
    private String nombre;
    private String cantidad;
    private String precio;
    private String imagenUrl;

    public Product(String categoria, String codigo, String nombre, String cantidad, String precio, String imagenUrl) {
        this.categoria = categoria;
        this.codigo = codigo;
        this.nombre = nombre;
        this.cantidad = cantidad;
        this.precio = precio;
        this.imagenUrl = imagenUrl;
    }

    public Product() {}

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCantidad() {
        return cantidad;
    }

    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public String getImagenUrl() {
        return imagenUrl;
    }

    public void setImagenUrl(String imageUrl) {
        this.imagenUrl = imageUrl;
    }
}
