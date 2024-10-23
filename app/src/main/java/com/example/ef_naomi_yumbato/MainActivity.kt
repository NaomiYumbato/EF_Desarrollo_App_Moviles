package com.example.ef_naomi_yumbato

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ef_naomi_yumbato.databinding.ActivityMainBinding
import com.example.ef_naomi_yumbato.model.Users
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity(), UsersAdapter.OnUserClickListener {
    private lateinit var userAdapter: UsersAdapter
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        userAdapter = UsersAdapter(mutableListOf(), this)
        binding.recyclerView.adapter = userAdapter

        getUsers()

        binding.btnConsultar.setOnClickListener {
            val intent = Intent(this, FindUserActivity::class.java)
            startActivity(intent)
        }

    }

    private fun getUsers() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.escuelajs.co/api/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(JsonPlaceHolderApi::class.java)
        val call: Call<List<Users>> = apiService.getUsers()

        call.enqueue(object : Callback<List<Users>> {
            override fun onResponse(call: Call<List<Users>>, response: Response<List<Users>>) {
                if (response.isSuccessful) {
                    val userList = response.body()
                    if (userList != null) {
                        userAdapter = UsersAdapter(userList, this@MainActivity) // Actualiza el adaptador
                        binding.recyclerView.adapter = userAdapter
                    }
                } else {
                    Log.e("MainActivity", "Código de respuesta: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<Users>>, t: Throwable) {
                Log.e("MainActivity", "Error: ${t.message}")
            }
        })
    }

    override fun onUserClick(userId: Int) {
        getUserById(userId)
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
                if (response.isSuccessful) {
                    val user = response.body()
                    if (user != null) {
                        Log.d("MainActivity", "Usuario obtenido: ID=${user.id}, Nombre=${user.name}")

                        showUserDetails(user)
                    }
                } else {
                    Log.e("MainActivity", "Error al obtener usuario: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<Users>, t: Throwable) {
                Log.e("MainActivity", "Error de conexión: ${t.message}")
            }
        })
    }

    private fun showUserDetails(user: Users) {
        val intent = Intent(this, UserDetailsActivity::class.java).apply {
            putExtra("USER_ID", user.id)
            putExtra("USER_EMAIL", user.email)
            putExtra("USER_PASSWORD", user.password)
            putExtra("USER_NAME", user.name)
            putExtra("USER_ROLE", user.role)
            putExtra("USER_CREATION_AT", user.creationAt)
            putExtra("USER_UPDATED_AT", user.updatedAt)
        }
        startActivity(intent)
    }
}
