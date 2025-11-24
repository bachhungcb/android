package com.example.playstoreclone

// Model chứa thông tin chi tiết của 1 ứng dụng
data class AppModel(
    val name: String,
    val category: String,
    val rating: String,
    val imageRes: Int // ID ảnh trong drawable
)

// Sealed class để phân loại các dòng trong RecyclerView cha
sealed class PlayStoreItem {
    // Loại 1: Tiêu đề (VD: Suggested for you)
    data class Header(val title: String) : PlayStoreItem()

    // Loại 2: Item App hiển thị dọc (VD: Mech Assemble...)
    data class VerticalApp(val app: AppModel) : PlayStoreItem()

    // Loại 3: Một danh sách ngang (Chứa List các app khác)
    data class HorizontalList(val apps: List<AppModel>) : PlayStoreItem()
}