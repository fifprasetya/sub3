package com.example.gittalk.favorite


import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.gittalk.*
import com.example.gittalk.databinding.UserItemsBinding
import de.hdodenhof.circleimageview.CircleImageView

class FavoriteAdapter(private val activity: Activity) : RecyclerView.Adapter<FavoriteAdapter.FavoriteHolder>() {

    var listFavorite = ArrayList<FavoriteModel>()
        fun setData (listFavorite: ArrayList<FavoriteModel>){
                this.listFavorite.clear()

            this.listFavorite.addAll(listFavorite)

            notifyDataSetChanged()
        }
    private lateinit var profileCircleImageView: CircleImageView
    var username: String? = null

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback){
        this.onItemClickCallback = onItemClickCallback
    }

    fun addItem(favorite:FavoriteModel){
        this.listFavorite.add(favorite)
        notifyItemInserted(this.listFavorite.size - 1)
    }

    fun removeItem(position: Int){
        this.listFavorite.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position,this.listFavorite.size)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.user_items, parent, false)
        return FavoriteHolder(view)
    }

    override fun onBindViewHolder(holder: FavoriteHolder, position: Int) {
        holder.bind(listFavorite[position])
        holder.itemView.setOnClickListener{
            username = listFavorite[holder.adapterPosition].name
           // Toast.makeText(holder.itemView.context, "Kamu memilih $username", Toast.LENGTH_SHORT).show()
            onItemClickCallback.onItemClicked(listFavorite[holder.adapterPosition])
        }
    }

    override fun getItemCount(): Int = listFavorite.size

    inner class FavoriteHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = UserItemsBinding.bind(itemView)
        fun bind(favorite: FavoriteModel) {
            with(itemView) {
                binding.tvItemUsername.text = favorite.name
                binding.tvItemId.text = favorite.id.toString()
                binding.tvItemRepo.text = favorite.repository
                var profileImageUrl = favorite.avatar
                profileCircleImageView = binding.imgItemPhoto
                Glide.with(this)
                    .load(profileImageUrl)
                    .into(profileCircleImageView)

            }
        }
    }

    interface OnItemClickCallback{
        fun onItemClicked(data: FavoriteModel)
    }
}