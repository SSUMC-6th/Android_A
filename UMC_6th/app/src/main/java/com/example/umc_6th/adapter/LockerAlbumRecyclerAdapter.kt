package com.example.umc_6th.adapter

import android.annotation.SuppressLint
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.umc_6th.R
import com.example.umc_6th.Song
import com.example.umc_6th.databinding.ItemLockerAlbumBinding

class LockerAlbumRecyclerAdapter() : RecyclerView.Adapter<LockerAlbumRecyclerAdapter.ViewHolder>() {

    private val switchStatus = SparseBooleanArray()
    private val songs = ArrayList<Song>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): LockerAlbumRecyclerAdapter.ViewHolder {
        val binding: ItemLockerAlbumBinding = ItemLockerAlbumBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val song = songs[position]
        holder.bind(song)

        // 항목 클릭 이벤트
        holder.itemView.setOnClickListener {
            itemClickListener.onItemClick(song)
        }

        // 더보기 클릭 이벤트
        holder.binding.imgItemLockerAlbumMore.setOnClickListener {
            itemClickListener.onRemoveAlbum(position)
        }
    }

    override fun getItemCount(): Int = songs.size

    inner class ViewHolder(val binding: ItemLockerAlbumBinding): RecyclerView.ViewHolder(binding.root){

        fun bind(song: Song){
            binding.txItemLockerAlbumTitle.text = song.title
            binding.txItemLockerAlbumArtist.text = song.artist
            binding.imgItemLockerAlbumCover.setImageResource(song.coverImg!!)
        }
    }

    interface OnItemClickListener {
        fun onItemClick(album: Song)
        abstract fun onRemoveAlbum(position: Int)
    }

    private lateinit var itemClickListener : OnItemClickListener

    fun setItemClickListener(onItemClickListener: OnItemClickListener) {
        this.itemClickListener = onItemClickListener
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addSongs(songs: ArrayList<Song>) {
        this.songs.clear()
        this.songs.addAll(songs)

        notifyDataSetChanged()
    }


    fun removeItem(position: Int){
        songs.removeAt(position)
        notifyDataSetChanged()
    }
}