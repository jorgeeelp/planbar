package com.losjorges.planbar.models

data class Producto(
    val id_producto: Int,
    val nombre_producto: String,
    val precio_producto: Double,
    val observaciones_producto: String,
    val foto_producto: String
)