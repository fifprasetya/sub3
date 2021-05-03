package com.example.gittalk

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.gittalk.databinding.UserItemsBinding
import de.hdodenhof.circleimageview.CircleImageView

class UserAdapter : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {
    private val mData = ArrayList<UserItems>()
    private lateinit var profileCircleImageView: CircleImageView
    var username: String? = null
    private var onItemClickCallback: OnItemClickCallback? = null

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback){
        this.onItemClickCallback = onItemClickCallback
    }

    fun setData(items: ArrayList<UserItems>) {
        mData.clear()
        mData.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, position: Int): UserViewHolder {
        val mView = LayoutInflater.from(viewGroup.context).inflate(R.layout.user_items, viewGroup, false)
        return UserViewHolder(mView)

    }

    override fun onBindViewHolder(userViewHolder: UserViewHolder, position: Int) {
        userViewHolder.bind(mData[position])
        userViewHolder.itemView.setOnClickListener {
            username = mData[userViewHolder.adapterPosition].name
            Toast.makeText(userViewHolder.itemView.context, "Kamu memilih $username", Toast.LENGTH_SHORT).show()
            onItemClickCallback?.onItemClicked(mData[userViewHolder.adapterPosition])}
    }

    override fun getItemCount(): Int = mData.size
    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = UserItemsBinding.bind(itemView)
        fun bind(userItems: UserItems) {
            with(itemView) {
                binding.tvItemUsername.text = userItems.name
                binding.tvItemId.text = userItems.id
                binding.tvItemRepo.text = userItems.repository
                var profileImageUrl = userItems.avatar
                profileCircleImageView = binding.imgItemPhoto
                Glide.with(this)
                    .load(profileImageUrl)

                    .into(profileCircleImageView)

            }
        }
    }
    interface OnItemClickCallback{
        fun onItemClicked(data: UserItems)
    }
}