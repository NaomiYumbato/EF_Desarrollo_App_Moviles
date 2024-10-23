package com.example.ef_naomi_yumbato

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.ef_naomi_yumbato.databinding.DetailsUsersBinding

class UserDetailsActivity : AppCompatActivity() {
    private lateinit var binding: DetailsUsersBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DetailsUsersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userId = intent.getIntExtra("USER_ID", -1)
        val userEmail = intent.getStringExtra("USER_EMAIL")
        val userPassword = intent.getStringExtra("USER_PASSWORD")
        val userName = intent.getStringExtra("USER_NAME")
        val userRole = intent.getStringExtra("USER_ROLE")
        val userCreationAt = intent.getStringExtra("USER_CREATION_AT")
        val userUpdatedAt = intent.getStringExtra("USER_UPDATED_AT")

        binding.tvId.text = userId.toString()
        binding.tvEmail.text = userEmail
        binding.tvPassword.text = userPassword
        binding.tvName.text = userName
        binding.tvRole.text = userRole
        binding.tvCreation.text = userCreationAt
        binding.tvUpdate.text = userUpdatedAt

        binding.btnVolver.setOnClickListener {
            finish()
        }
    }
}
