package developer.abdulaziz.mymediaplayer.Adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import developer.abdulaziz.mymediaplayer.Classes.MyMusic
import developer.abdulaziz.mymediaplayer.R
import developer.abdulaziz.mymediaplayer.databinding.ItemBinding

class MyAdapter(var list: ArrayList<MyMusic>, var listener: MyOnClick) :
    RecyclerView.Adapter<MyAdapter.VH>() {

    inner class VH(private val binding: ItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(myMusic: MyMusic, position: Int) {
            binding.apply {
                artist.text = myMusic.artist
                musicName.text = myMusic.musicTitle
                item.setOnClickListener { listener.onClick(myMusic, position) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH =
        VH(
            ItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(hol: VH, pos: Int) = hol.onBind(list[pos], pos)
    override fun getItemCount(): Int = list.size

    interface MyOnClick {
        fun onClick(myMusic: MyMusic, position: Int)
    }
}