package com.example.filemanagement

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.io.File

class FileAdapter(
    private var files: List<File>,
    private val onFileClick: (File) -> Unit,
    private val onFileLongClick: (File, View) -> Unit
) : RecyclerView.Adapter<FileAdapter.FileViewHolder>() {

    inner class FileViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.tvFileName)
        val imgIcon: ImageView = itemView.findViewById(R.id.imgIcon)

        fun bind(file: File) {
            tvName.text = file.name
            if (file.isDirectory) {
                // Sửa dòng này: Dùng icon folder bạn vừa tạo
                imgIcon.setImageResource(R.drawable.ic_folder)
            } else {
                // Sửa dòng này: Dùng icon file bạn vừa tạo
                imgIcon.setImageResource(R.drawable.ic_file)
            }

            itemView.setOnClickListener { onFileClick(file) }

            // Xử lý Long Click để hiện Context Menu
            itemView.setOnLongClickListener {
                onFileLongClick(file, it)
                true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_file, parent, false)
        return FileViewHolder(view)
    }

    override fun onBindViewHolder(holder: FileViewHolder, position: Int) {
        holder.bind(files[position])
    }

    override fun getItemCount() = files.size

    fun updateData(newFiles: List<File>) {
        files = newFiles
        notifyDataSetChanged()
    }
}