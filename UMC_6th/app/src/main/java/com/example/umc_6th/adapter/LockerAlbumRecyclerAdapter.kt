package com.example.umc_6th.adapter

import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.umc_6th.Album
import com.example.umc_6th.databinding.ItemLockerAlbumBinding

class LockerAlbumRecyclerAdapter(private val albumList: ArrayList<Album>) : RecyclerView.Adapter<LockerAlbumRecyclerAdapter.ViewHolder>() {

    private val switchStatus = SparseBooleanArray()
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): LockerAlbumRecyclerAdapter.ViewHolder {
        val binding: ItemLockerAlbumBinding = ItemLockerAlbumBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LockerAlbumRecyclerAdapter.ViewHolder, position: Int) {
        holder.bind(albumList[position])
        holder.itemView.setOnClickListener {
            itemClickListener.onItemClick(albumList[position])
        }

        holder.binding.imgItemLockerAlbumMore.setOnClickListener {
            itemClickListener.onRemoveAlbum(position)
        }

        val switch =  holder.binding.switchRV
        switch.isChecked = switchStatus[position]
        switch.setOnClickListener {
            if (switch.isChecked) {
                switchStatus.put(position, true)
            }
            else {
                switchStatus.put(position, false)
            }

            notifyItemChanged(position)
        }
    }

    override fun getItemCount(): Int = albumList.size

    inner class ViewHolder(val binding: ItemLockerAlbumBinding): RecyclerView.ViewHolder(binding.root){

        fun bind(album: Album){
            binding.txItemLockerAlbumTitle.text = album.title
            binding.txItemLockerAlbumArtist.text = album.artist
            binding.imgItemLockerAlbumCover.setImageResource(album.coverImg!!)
        }
    }

    interface OnItemClickListener {
        fun onItemClick(album : Album)
        abstract fun onRemoveAlbum(position: Int)
    }

    private lateinit var itemClickListener : OnItemClickListener

    fun setItemClickListener(onItemClickListener: OnItemClickListener) {
        this.itemClickListener = onItemClickListener
    }

    fun removeItem(position: Int){
        albumList.removeAt(position)
        notifyDataSetChanged()
    }
}