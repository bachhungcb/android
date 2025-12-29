package com.example.studentmanagent

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class StudentAdapter(
    private var students: MutableList<Student>,
    // Thêm 2 tham số này vào constructor để sửa lỗi
    private val onEditClick: (Int, Student) -> Unit,
    private val onDeleteClick: (Int, Student) -> Unit
) : RecyclerView.Adapter<StudentAdapter.StudentViewHolder>() {

    class StudentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvMssv: TextView = itemView.findViewById(R.id.tvMssv)
        val tvName: TextView = itemView.findViewById(R.id.tvName)
        val btnDelete: ImageButton = itemView.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        // Đảm bảo bạn đã có file layout item_student.xml
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_student, parent, false)
        return StudentViewHolder(view)
    }

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        val student = students[position]
        holder.tvMssv.text = student.id
        holder.tvName.text = student.name

        // Xử lý sự kiện click vào item để sửa
        holder.itemView.setOnClickListener {
            onEditClick(position, student)
        }

        // Xử lý sự kiện click nút xóa
        holder.btnDelete.setOnClickListener {
            onDeleteClick(position, student)
        }
    }

    override fun getItemCount(): Int = students.size

    // Hàm cập nhật dữ liệu mới
    fun updateData(newStudents: List<Student>) {
        students.clear()
        students.addAll(newStudents)
        notifyDataSetChanged()
    }
}