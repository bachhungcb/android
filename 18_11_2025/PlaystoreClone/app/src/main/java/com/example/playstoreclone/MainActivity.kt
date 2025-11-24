package com.example.playstoreclone

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.mainRecyclerView)

        // 1. Tạo dữ liệu giả
        val dataList = mutableListOf<PlayStoreItem>()

        // -- Phần 1: Suggested for you (Dọc) --
        dataList.add(PlayStoreItem.Header("Suggested for you"))
        dataList.add(PlayStoreItem.VerticalApp(AppModel("Mech Assemble", "Action", "4.8", R.drawable.ic_launcher_background)))
        dataList.add(PlayStoreItem.VerticalApp(AppModel("MU: Hong Hoa Dao", "Role Playing", "4.8", R.drawable.ic_launcher_background)))
        dataList.add(PlayStoreItem.VerticalApp(AppModel("War Inc: Rising", "Strategy", "4.9", R.drawable.ic_launcher_background)))

        // -- Phần 2: Recommended for you (Ngang) --
        dataList.add(PlayStoreItem.Header("Recommended for you"))

        // Tạo list app cho phần ngang
        val horizontalApps = listOf(
            AppModel("Suno", "Music", "4.5", R.drawable.ic_launcher_background),
            AppModel("Claude", "AI", "4.7", R.drawable.ic_launcher_background),
            AppModel("DramaBox", "Movies", "4.2", R.drawable.ic_launcher_background),
            AppModel("Pili", "Tools", "4.6", R.drawable.ic_launcher_background)
        )
        // Đóng gói list app này vào 1 item duy nhất
        dataList.add(PlayStoreItem.HorizontalList(horizontalApps))


        // 2. Setup RecyclerView chính
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = MainAdapter(dataList)
    }
}