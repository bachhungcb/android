package com.example.mailclone

data class EmailModel(
    val sender: String,      // Tên người gửi (Vd: Edurila.com)
    val title: String,       // Tiêu đề mail
    val content: String,     // Nội dung ngắn
    val time: String,        // Giờ (12:34 PM)
    val color: Int           // Màu nền của avatar
)