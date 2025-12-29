package com.example.studentmanagent

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class StudentViewModel(application: Application) : AndroidViewModel(application) {

    private val dbHelper = StudentDatabaseHelper(application)
    private val _students = MutableLiveData<MutableList<Student>>()
    val students: LiveData<MutableList<Student>> get() = _students

    init {
        loadData() // Tải dữ liệu từ SQLite ngay khi ViewModel được tạo
    }

    // Hàm đọc dữ liệu từ DB
    fun loadData() {
        _students.value = dbHelper.getAllStudents()
    }

    fun addStudent(student: Student) {
        dbHelper.insertStudent(student)
        loadData() // Refresh lại list sau khi thêm
    }

    fun updateStudent(oldMssv: String, newStudent: Student) {
        dbHelper.updateStudent(oldMssv, newStudent)
        loadData() // Refresh lại list sau khi sửa
    }

    fun deleteStudent(student: Student) {
        dbHelper.deleteStudent(student.id)
        loadData() // Refresh lại list sau khi xóa
    }
}