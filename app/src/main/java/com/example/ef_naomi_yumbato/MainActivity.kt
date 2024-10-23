package com.example.ef_naomi_yumbato

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ef_naomi_yumbato.databinding.ActivityMainBinding
import com.example.ef_naomi_yumbato.model.Users
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    private lateinit var userAdapter: UsersAdapter
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userAdapter = UsersAdapter(mutableListOf())
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = userAdapter

        getUsers()
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
                        userAdapter = UsersAdapter(userList)
                        binding.recyclerView.adapter = userAdapter
                    }
                } else {
                    println("Código de respuesta: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<Users>>, t: Throwable) {
                println("Error: ${t.message}")
            }
        })
    }
}
