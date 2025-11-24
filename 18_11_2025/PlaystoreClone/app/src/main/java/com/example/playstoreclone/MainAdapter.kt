package com.example.playstoreclone
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainAdapter(private val items: List<PlayStoreItem>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    // Định nghĩa các loại View
    companion object {
        const val TYPE_HEADER = 0
        const val TYPE_VERTICAL_APP = 1
        const val TYPE_HORIZONTAL_LIST = 2
    }

    // 1. Xác định loại item dựa trên vị trí
    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is PlayStoreItem.Header -> TYPE_HEADER
            is PlayStoreItem.VerticalApp -> TYPE_VERTICAL_APP
            is PlayStoreItem.HorizontalList -> TYPE_HORIZONTAL_LIST
        }
    }

    // 2. Tạo ViewHolder tương ứng với loại item
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            TYPE_HEADER -> {
                val view = inflater.inflate(R.layout.item_header, parent, false)
                HeaderViewHolder(view)
            }
            TYPE_VERTICAL_APP -> {
                val view = inflater.inflate(R.layout.item_vertical_app, parent, false)
                VerticalAppViewHolder(view)
            }
            else -> { // TYPE_HORIZONTAL_LIST
                val view = inflater.inflate(R.layout.item_horizontal_container, parent, false)
                HorizontalContainerViewHolder(view)
            }
        }
    }

    // 3. Đổ dữ liệu
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = items[position]) {
            is PlayStoreItem.Header -> {
                (holder as HeaderViewHolder).tvTitle.text = item.title
            }
            is PlayStoreItem.VerticalApp -> {
                val holderVertical = holder as VerticalAppViewHolder
                holderVertical.tvName.text = item.app.name
                holderVertical.tvInfo.text = "${item.app.category} • ${item.app.rating} ★"
                holderVertical.img.setImageResource(item.app.imageRes)
            }
            is PlayStoreItem.HorizontalList -> {
                val holderHorizontal = holder as HorizontalContainerViewHolder

                // SETUP RECYCLERVIEW CON (NGANG) Ở ĐÂY
                holderHorizontal.rvHorizontal.layoutManager =
                    LinearLayoutManager(holderHorizontal.itemView.context, LinearLayoutManager.HORIZONTAL, false)
                holderHorizontal.rvHorizontal.adapter = HorizontalAdapter(item.apps)
            }
        }
    }

    override fun getItemCount() = items.size

    // --- CÁC CLASS VIEWHOLDER ---
    class HeaderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTitle: TextView = view.findViewById(R.id.tvHeaderTitle)
    }

    class VerticalAppViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvName: TextView = view.findViewById(R.id.tvAppName)
        val tvInfo: TextView = view.findViewById(R.id.tvAppInfo)
        val img: ImageView = view.findViewById(R.id.imgApp)
    }

    class HorizontalContainerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val rvHorizontal: RecyclerView = view.findViewById(R.id.rvHorizontal)
    }
}