package com.example.studentmanagement

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class StudentAdapter(
    private val studentList: MutableList<Student>,
    private val onEditClick: (Int) -> Unit,
    private val onDeleteClick: (Int) -> Unit
) : RecyclerView.Adapter<StudentAdapter.StudentViewHolder>() {

    // ViewHolder: Nắm giữ các thành phần giao diện của 1 dòng
    inner class StudentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.tvName)
        val tvMssv: TextView = itemView.findViewById(R.id.tvMssv)
        val btnDelete: ImageView = itemView.findViewById(R.id.btnDelete)

        init {
            // Click vào dòng -> Sửa
            itemView.setOnClickListener { onEditClick(adapterPosition) }
            // Click vào icon thùng rác -> Xóa
            btnDelete.setOnClickListener { onDeleteClick(adapterPosition) }
        }
    }

    // Tạo view cho 1 dòng
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_student, parent, false)
        return StudentViewHolder(view)
    }

    // Gán dữ liệu vào dòng
    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        val student = studentList[position]
        holder.tvName.text = student.hoTen
        holder.tvMssv.text = student.mssv
    }

    override fun getItemCount(): Int = studentList.size
}