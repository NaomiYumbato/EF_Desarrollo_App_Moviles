package com.example.ef_naomi_yumbato

import com.example.ef_naomi_yumbato.databinding.ItemUsersBinding
import com.example.ef_naomi_yumbato.model.Users
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class UsersAdapter(private val users: List<Users>) : RecyclerView.Adapter<UsersAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_users, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return users.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = users[position]
        holder.bind(user) 
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ItemUsersBinding.bind(view)

        fun bind(user: Users) {
            binding.tvId.text = user.id.toString()
            binding.tvName.text = user.name
        }
    }
}
