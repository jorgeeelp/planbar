package com.losjorges.planbar.network

import com.losjorges.planbar.models.Empleado
import com.losjorges.planbar.models.LoginResponse
import com.losjorges.planbar.models.Mesa
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    // ==========================================
    // 1. LOGIN Y SEGURIDAD
    // ==========================================
    @FormUrlEncoded
    @POST("login_admin.php")
    fun loginAdmin(@Field("pass") pass: String): Call<LoginResponse>


    // ==========================================
    // 2. GESTIÓN DE EMPLEADOS
    // ==========================================

    // Listado para pantalla de selección inicial (sin administradores)
    @GET("get_empleados.php")
    fun getEmpleados(): Call<List<Empleado>>

    // Listado completo para el panel de administración
    @GET("get_empleados_admin.php")
    fun getEmpleadosAdmin(): Call<List<Empleado>>

    @FormUrlEncoded
    @POST("insert_empleado.php")
    fun insertEmpleado(
        @Field("dni") dni: String,
        @Field("nombre") nombre: String,
        @Field("rol") rol: String
    ): Call<LoginResponse>

    @FormUrlEncoded
    @POST("update_empleado.php")
    fun updateEmpleado(
        @Field("id") id: Int,
        @Field("dni") dni: String,
        @Field("nombre") nombre: String,
        @Field("rol") rol: String
    ): Call<LoginResponse>

    @FormUrlEncoded
    @POST("delete_empleado.php")
    fun deleteEmpleado(@Field("id") id: Int): Call<LoginResponse>


    // ==========================================
    // 3. GESTIÓN DE MESAS
    // ==========================================

    @GET("get_mesas.php")
    fun getMesas(): Call<List<Mesa>>

    @FormUrlEncoded
    @POST("insert_mesa.php")
    fun insertMesa(
        @Field("numero") numero: Int,
        @Field("capacidad") capacidad: Int
    ): Call<LoginResponse>

    @FormUrlEncoded
    @POST("update_mesa.php")
    fun updateMesa(
        @Field("id") id: Int,
        @Field("numero") numero: Int,
        @Field("capacidad") capacidad: Int
    ): Call<LoginResponse>

    @FormUrlEncoded
    @POST("delete_mesa.php")
    fun deleteMesa(@Field("id") id: Int): Call<LoginResponse>
}

object RetrofitClient {
    private const val BASE_URL = "http://10.0.2.2/api/"

    val instance: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}