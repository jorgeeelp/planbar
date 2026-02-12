package com.losjorges.planbar.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.losjorges.planbar.models.Empleado
import com.losjorges.planbar.models.LoginResponse
import com.losjorges.planbar.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminScreen() {
    val context = LocalContext.current
    var dni by remember { mutableStateOf("") }
    var nombre by remember { mutableStateOf("") }
    var rol by remember { mutableStateOf("") }
    var listaEmpleados by remember { mutableStateOf(emptyList<Empleado>()) }

    // Función para refrescar la lista
    fun cargarEmpleados() {
        RetrofitClient.instance.getEmpleadosAdmin().enqueue(object : Callback<List<Empleado>> {
            override fun onResponse(call: Call<List<Empleado>>, response: Response<List<Empleado>>) {
                if (response.isSuccessful) listaEmpleados = response.body() ?: emptyList()
            }
            override fun onFailure(call: Call<List<Empleado>>, t: Throwable) {}
        })
    }

    LaunchedEffect(Unit) { cargarEmpleados() }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Administrador - Gestión") })
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // FORMULARIO DE INSERCIÓN
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Añadir Nuevo Empleado", fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(value = dni, onValueChange = { dni = it }, label = { Text("DNI") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Nombre") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = rol, onValueChange = { rol = it }, label = { Text("Rol (camarero/cocinero/admin)") }, modifier = Modifier.fillMaxWidth())
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {
                            RetrofitClient.instance.insertEmpleado(dni, nombre, rol).enqueue(object : Callback<LoginResponse> {
                                override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                                    if (response.isSuccessful && response.body()?.success == true) {
                                        dni = ""; nombre = ""; rol = ""
                                        cargarEmpleados() // Refrescar lista
                                        Toast.makeText(context, "Insertado", Toast.LENGTH_SHORT).show()
                                    }
                                }
                                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {}
                            })
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) { Text("Añadir Empleado") }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            Text("Lista de Empleados", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Spacer(modifier = Modifier.height(8.dp))

            // LISTA DE EMPLEADOS
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(listaEmpleados) { emp ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .background(Color.White)
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("ID: ${emp.id_empleado} | ${emp.rol_empleado.uppercase()}", fontSize = 12.sp, color = Color.Gray)
                            Text(emp.nombre_empleado, fontWeight = FontWeight.Bold)
                            Text("DNI: ${emp.dni_empleado}", fontSize = 14.sp)
                        }

                        IconButton(
                            onClick = {
                                RetrofitClient.instance.deleteEmpleado(emp.id_empleado).enqueue(object : Callback<LoginResponse> {
                                    override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                                        cargarEmpleados()
                                    }
                                    override fun onFailure(call: Call<LoginResponse>, t: Throwable) {}
                                })
                            },
                            modifier = Modifier.background(Color.Red, shape = MaterialTheme.shapes.small)
                        ) {
                            Text("-", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 24.sp)
                        }
                    }
                    Divider()
                }
            }
        }
    }
}