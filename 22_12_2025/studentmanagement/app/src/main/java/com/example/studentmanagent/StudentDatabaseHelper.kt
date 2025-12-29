package com.example.studentmanagent

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class StudentDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "StudentManager.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "students"
        private const val COLUMN_MSSV = "mssv"
        private const val COLUMN_NAME = "hoTen"
    }

    // Tạo bảng dữ liệu khi CSDL được khởi tạo lần đầu
    override fun onCreate(db: SQLiteDatabase?) {
        val createTableQuery = ("CREATE TABLE $TABLE_NAME (" +
                "$COLUMN_MSSV TEXT PRIMARY KEY, " +
                "$COLUMN_NAME TEXT)")
        db?.execSQL(createTableQuery)
    }

    // Xử lý khi nâng cấp version CSDL (đơn giản là xóa bảng cũ tạo lại)
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    // 1. Lấy tất cả sinh viên
    fun getAllStudents(): MutableList<Student> {
        val studentList = mutableListOf<Student>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_NAME", null)

        if (cursor.moveToFirst()) {
            do {
                val mssv = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MSSV))
                val name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME))
                studentList.add(Student(mssv, name))
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return studentList
    }

    // 2. Thêm sinh viên mới
    fun insertStudent(student: Student): Long {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COLUMN_MSSV, student.id)
        values.put(COLUMN_NAME, student.name)

        val result = db.insert(TABLE_NAME, null, values)
        db.close()
        return result
    }

    // 3. Cập nhật thông tin sinh viên (dựa theo MSSV gốc)
    fun updateStudent(originalMssv: String, student: Student): Int {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COLUMN_MSSV, student.id)
        values.put(COLUMN_NAME, student.name)

        // Cập nhật dòng có mssv trùng với originalMssv
        val result = db.update(TABLE_NAME, values, "$COLUMN_MSSV = ?", arrayOf(originalMssv))
        db.close()
        return result
    }

    // 4. Xóa sinh viên
    fun deleteStudent(mssv: String): Int {
        val db = this.writableDatabase
        val result = db.delete(TABLE_NAME, "$COLUMN_MSSV = ?", arrayOf(mssv))
        db.close()
        return result
    }
}