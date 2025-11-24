package com.example.studentmanagement

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var edtMssv: EditText
    private lateinit var edtName: EditText
    private lateinit var btnAdd: Button
    private lateinit var btnUpdate: Button
    private lateinit var recyclerView: RecyclerView

    private lateinit var studentAdapter: StudentAdapter
    private var studentList = mutableListOf<Student>()
    private var currentEditingIndex: Int = -1 // Biến lưu vị trí đang sửa

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 1. Ánh xạ View
        edtMssv = findViewById(R.id.edtMssv)
        edtName = findViewById(R.id.edtName)
        btnAdd = findViewById(R.id.btnAdd)
        btnUpdate = findViewById(R.id.btnUpdate)
        recyclerView = findViewById(R.id.recyclerView)

        // 2. Setup RecyclerView
        studentAdapter = StudentAdapter(studentList,
            onEditClick = { position ->
                // Khi click vào 1 dòng -> Đẩy dữ liệu lên ô nhập
                val student = studentList[position]
                edtMssv.setText(student.mssv)
                edtName.setText(student.hoTen)
                currentEditingIndex = position
                Toast.makeText(this, "Đang sửa: ${student.hoTen}", Toast.LENGTH_SHORT).show()
            },
            onDeleteClick = { position ->
                // Khi click xóa
                deleteStudent(position)
            }
        )
        recyclerView.adapter = studentAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        // 3. Sự kiện nút Add
        btnAdd.setOnClickListener {
            val mssv = edtMssv.text.toString()
            val name = edtName.text.toString()
            if (mssv.isNotEmpty() && name.isNotEmpty()) {
                studentList.add(Student(mssv, name))
                studentAdapter.notifyItemInserted(studentList.size - 1)
                clearInput()
            } else {
                Toast.makeText(this, "Vui lòng nhập đủ thông tin", Toast.LENGTH_SHORT).show()
            }
        }

        // 4. Sự kiện nút Update
        btnUpdate.setOnClickListener {
            if (currentEditingIndex != -1) {
                val mssv = edtMssv.text.toString()
                val name = edtName.text.toString()

                if (mssv.isNotEmpty() && name.isNotEmpty()) {
                    studentList[currentEditingIndex].mssv = mssv
                    studentList[currentEditingIndex].hoTen = name
                    studentAdapter.notifyItemChanged(currentEditingIndex)

                    currentEditingIndex = -1 // Reset trạng thái
                    clearInput()
                    Toast.makeText(this, "Cập nhật thành công", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Chưa chọn sinh viên để sửa", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun deleteStudent(position: Int) {
        studentList.removeAt(position)
        studentAdapter.notifyItemRemoved(position)
        studentAdapter.notifyItemRangeChanged(position, studentList.size)
        if (currentEditingIndex == position) {
            currentEditingIndex = -1
            clearInput()
        }
    }

    private fun clearInput() {
        edtMssv.text.clear()
        edtName.text.clear()
        edtMssv.requestFocus()
    }
}