package com.example.ef_naomi_yumbato

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.ef_naomi_yumbato.databinding.FindUsersBinding
import com.example.ef_naomi_yumbato.model.UserFirebase
import com.example.ef_naomi_yumbato.model.Users
import com.google.firebase.database.FirebaseDatabase
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class FindUserActivity : AppCompatActivity() {
    private lateinit var binding: FindUsersBinding
    private lateinit var database: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FindUsersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = FirebaseDatabase.getInstance()

        binding.btnBuscar.setOnClickListener {
            val userIdInput = binding.inputId.text.toString()
            if (userIdInput.isEmpty()) {
                showAlert("Por favor, ingrese un ID válido.")
            } else {
                val userId = userIdInput.toIntOrNull()
                if (userId != null) {
                    getUserById(userId)
                } else {
                    showAlert("El ID debe ser un número.")
                }
            }
        }

        binding.btnVolver.setOnClickListener {
            finish()
        }
    }

    private fun getUserById(userId: Int) {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.escuelajs.co/api/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(JsonPlaceHolderApi::class.java)
        val call: Call<Users> = apiService.getUserById(userId)

        call.enqueue(object : Callback<Users> {
            override fun onResponse(call: Call<Users>, response: Response<Users>) {
                when (response.code()) {
                    200 -> {
                        val user = response.body()
                        if (user != null) {
                            displayUserDetails(user)
                            checkUserInFirebase(user.id) { exists ->
                                if (!exists) {
                                    registerUserInFirebase(user.id, user.name, user.email)
                                } else {
                                    showAlert("El usuario ya fue registrado en Firebase.")
                                }
                            }
                        } else {
                            showAlert("Campo vacío. Por favor, introduzca el ID.")
                        }
                    }
                    404 -> {
                        showAlert("Usuario no encontrado.")
                        displayUserDetailsWithNull()
                    }
                    400 -> {
                        showAlert("Usuario no encontrado. Por favor, verifica el ID ingresado.")
                        displayUserDetailsWithNull()
                    }
                    else -> {
                        showAlert("Error: ${response.code()}")
                    }
                }
                binding.inputId.text?.clear()
            }

            override fun onFailure(call: Call<Users>, t: Throwable) {
                Log.e("FindUserActivity", "Error: ${t.message}")
                showAlert("Error de conexión.")
            }
        })
    }

    private fun displayUserDetails(user: Users) {
        val userDetails = "ID: \n${user.id}\n" +
                "Email: \n${user.email}\n" +
                "Nombre: \n${user.name}\n" +
                "Rol: \n${user.role}\n" +
                "Creado: \n${user.creationAt}\n" +
                "Actualizado: \n${user.updatedAt}"
        binding.tvUserDetails.text = userDetails
    }

    private fun displayUserDetailsWithNull() {
        val userDetails = "ID: \nNULO\n" +
                "Email: \nNULO\n" +
                "Nombre: \nNULO\n" +
                "Rol: \nNULO\n" +
                "Creado: \nNULO\n" +
                "Actualizado: \nNULO"
        binding.tvUserDetails.text = userDetails
    }

    private fun showAlert(message: String) {
        AlertDialog.Builder(this)
            .setTitle("Alerta")
            .setMessage(message)
            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
            .show()
    }

    private fun registerUserInFirebase(id: Int?, name: String?, email: String?) {
        if (id != null && name != null && email != null) {
            val userFirebase = UserFirebase(id, name, email)

            database.reference.child("collection").child(id.toString()).setValue(userFirebase)
                .addOnSuccessListener {
                    showAlert("Usuario registrado en Firebase.")
                }
                .addOnFailureListener { e ->
                    showAlert("Error al registrar el usuario: ${e.message}")
                }
        }
    }

    private fun checkUserInFirebase(userId: Int, callback: (Boolean) -> Unit) {
        val database = FirebaseDatabase.getInstance().reference.child("collection").child(userId.toString())
        database.get().addOnSuccessListener { snapshot ->
            callback(snapshot.exists())
        }.addOnFailureListener { e ->
            showAlert("Error al verificar el usuario: ${e.message}")
            callback(false)
        }
    }
}
