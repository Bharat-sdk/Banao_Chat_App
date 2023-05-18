package com.ivaninfotech.banaochatapp.ui.adapter

import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ivaninfotech.banaochatapp.databinding.ItemUserBinding
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.models.User
import javax.inject.Inject


class UserListAdapter  @Inject constructor():RecyclerView.Adapter<UserListAdapter.CartViewHolder>(){

    inner class CartViewHolder(val binding: ItemUserBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(currentUser: User) {
            binding.avatarImageView.setUserData(currentUser)
            binding.usernameTextView.text = currentUser.name
            binding.lastActiveTextView.text = convertDate(currentUser.lastActive?.time?:0)
            binding.rootLayout.setOnClickListener {
                onClickListener?.let { it1 -> it1(currentUser) }
               // createNewChannel(currentUser.id, holder)
            }
        }
    }


    private val differCallback = object : DiffUtil.ItemCallback<User>() {
        override fun areItemsTheSame(
            oldItem: User,
            newItem: User
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: User,
            newItem: User
        ): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        return CartViewHolder(
            ItemUserBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val data = differ.currentList[position]
        holder.bind(data)

    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var onClickListener: ((User) -> Unit)? = null

    fun setOnUserClickListener(listener: (User) -> Unit) {
        onClickListener = listener
    }

    private fun convertDate(milliseconds: Long): String {
        return DateFormat.format("dd/MM/yyyy hh:mm a", milliseconds).toString()
    }

}