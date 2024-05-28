package com.example.umc_6th.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.umc_6th.Album
import com.example.umc_6th.databinding.ItemAlbumBinding

class AlbumRecyclerAdapter(private val albumList: ArrayList<Album>) : RecyclerView.Adapter<AlbumRecyclerAdapter.ViewHolder>() {
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): AlbumRecyclerAdapter.ViewHolder {
        val binding: ItemAlbumBinding = ItemAlbumBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(binding)
    }


    override fun onBindViewHolder(holder: AlbumRecyclerAdapter.ViewHolder, position: Int) {
        holder.bind(albumList[position])
        holder.binding.imgItemAlbumCover.setOnClickListener {
            itemClickListener?.onItemClick(albumList[position])
        }
        holder.binding.imgItemAlbumPlay.setOnClickListener {
            itemPlayClickListener?.onItemPlayClick(albumList[position])
        }
    }

    override fun getItemCount(): Int = albumList.size

    inner class ViewHolder(val binding: ItemAlbumBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(album: Album){
            binding.txItemAlbumTitle.text = album.title
            binding.txItemAlbumArtist.text = album.artist
            binding.imgItemAlbumCover.setImageResource(album.coverImg!!)
        }
    }

    interface OnItemClickListener {
        fun onItemClick(album : Album)
    }
    interface OnItemPlayClickListener {
        fun onItemPlayClick(album: Album)
    }

    private var itemClickListener: OnItemClickListener? = null
    private var itemPlayClickListener: OnItemPlayClickListener? = null

    fun setItemClickListener(listener : OnItemClickListener) {
        this.itemClickListener = listener
    }
    fun setItemPlayClickListener(listener : OnItemPlayClickListener) {
        this.itemPlayClickListener = listener
    }
}