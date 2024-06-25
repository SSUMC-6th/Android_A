package com.example.myfirstapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myfirstapp.data.entities.Album
import com.example.myfirstapp.databinding.ItemAlbumBinding

class AlbumRVAdapter(private val albumlist: ArrayList<Album>): RecyclerView.Adapter<AlbumRVAdapter.ViewHolder>() {

    interface MyItemClickListener{
        fun onItemClick(album: Album)
        fun onPlayAlbum(album : Album)
        fun onRemoveAlbum(position: Int)
    }


    private lateinit var myItemClickListener: MyItemClickListener
    fun setMyItemClickListener(itemClickListener: MyItemClickListener){
        myItemClickListener = itemClickListener
    }
    fun addItem(album: Album){
        albumlist.add(album)
        notifyDataSetChanged()
    }

    fun removeItem(position: Int){
        albumlist.removeAt(position)
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding : ItemAlbumBinding = ItemAlbumBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(albumlist[position])
        holder.itemView.setOnClickListener{
            myItemClickListener.onItemClick(albumlist[position])
        }
    //        holder.binding.itemAlbumTitleTv.setOnClickListener { mItemClickListener.onRemoveAlbum(position)
        holder.binding.itemAlbumPlayImgIv.setOnClickListener {
            myItemClickListener.onPlayAlbum(albumlist[position])
        }
    }

    override fun getItemCount(): Int = albumlist.size
    inner class ViewHolder(val binding: ItemAlbumBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(album: Album){
            binding.itemAlbumTitleTv.text = album.title
            binding.itemAlbumSingerTv.text = album.singer
            binding.itemAlbumCoverImgIv.setImageResource(album.coverImg!!)
        }

    }

}