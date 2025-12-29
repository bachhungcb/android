package com.example.filemanagement

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.filemanagement.databinding.ActivityMainBinding // Đảm bảo đúng package name
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: FileAdapter
    private var currentPath: File = Environment.getExternalStorageDirectory()
    private var selectedFile: File? = null // Lưu file đang được chọn (Long click)
    private var fileToCopy: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        checkPermissionAndLoadFiles()
    }

    // 1. Cấu hình RecyclerView
    private fun setupRecyclerView() {
        adapter = FileAdapter(emptyList(),
            onFileClick = { file ->
                if (file.isDirectory) {
                    currentPath = file
                    loadFiles(currentPath)
                } else {
                    openFile(file)
                }
            },
            onFileLongClick = { file, view ->
                selectedFile = file
                openContextMenu(view) // Mở Context Menu
            }
        )
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
        registerForContextMenu(binding.recyclerView) // Đăng ký context menu cho list
    }

    // 2. Xin quyền truy cập (Quan trọng cho Android 11+)
    private fun checkPermissionAndLoadFiles() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()) {
                val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                intent.data = Uri.parse("package:$packageName")
                startActivity(intent)
            } else {
                loadFiles(currentPath)
            }
        } else {
            // Cho Android cũ hơn
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE), 100)
            } else {
                loadFiles(currentPath)
            }
        }
    }

    // 3. Load danh sách file
    private fun loadFiles(directory: File) {
        title = directory.path // Hiển thị đường dẫn trên thanh tiêu đề
        val files = directory.listFiles()?.toList() ?: emptyList()
        // Sắp xếp: Folder lên trước, File xuống sau
        val sortedFiles = files.sortedWith(compareBy({ !it.isDirectory }, { it.name }))
        adapter.updateData(sortedFiles)
    }

    // 4. Mở file (Text hoặc Ảnh)
    private fun openFile(file: File) {
        val extension = file.extension.lowercase()

        if (extension == "txt") {
            // Đọc file text (như cũ)
            try {
                val text = file.readText()
                AlertDialog.Builder(this)
                    .setTitle(file.name)
                    .setMessage(text)
                    .setPositiveButton("Đóng", null)
                    .show()
            } catch (e: Exception) {
                Toast.makeText(this, "Không thể đọc file", Toast.LENGTH_SHORT).show()
            }
        }
        else if (listOf("png", "jpg", "jpeg", "bmp").contains(extension)) {
            // HIỂN THỊ ẢNH (MỚI)
            val imageView = android.widget.ImageView(this)

            // Load ảnh từ file vào ImageView
            val bitmap = android.graphics.BitmapFactory.decodeFile(file.absolutePath)
            imageView.setImageBitmap(bitmap)

            // Hiển thị trong Dialog
            AlertDialog.Builder(this)
                .setTitle(file.name)
                .setView(imageView) // Đặt ảnh vào dialog
                .setPositiveButton("Đóng", null)
                .show()
        }
        else {
            Toast.makeText(this, "Định dạng không hỗ trợ", Toast.LENGTH_SHORT).show()
        }
    }

    // --- OPTION MENU (Tạo mới) ---
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menu?.add(0, 1, 0, "Tạo thư mục mới")
        menu?.add(0, 2, 0, "Tạo file văn bản mới")

        // THÊM NÚT DÁN (PASTE)
        menu?.add(0, 3, 0, "Dán file đã copy")
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            1 -> showCreateDialog(isFolder = true)
            2 -> showCreateDialog(isFolder = false)
            3 -> pasteFile() // Gọi hàm dán
        }
        return super.onOptionsItemSelected(item)
    }

    // --- CONTEXT MENU (Xóa, Sửa, Copy) ---
    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menu?.setHeaderTitle("Thao tác với ${selectedFile?.name}")
        menu?.add(0, 101, 0, "Đổi tên")
        menu?.add(0, 102, 0, "Xóa")
        if (selectedFile?.isFile == true) {
            menu?.add(0, 103, 0, "Sao chép")
        }
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            101 -> showRenameDialog()
            102 -> showDeleteDialog()
            103 -> showCopyDialog()
        }
        return super.onContextItemSelected(item)
    }

    // THÊM HÀM NÀY
    private fun pasteFile() {
        if (fileToCopy == null) {
            Toast.makeText(this, "Chưa có file nào được sao chép!", Toast.LENGTH_SHORT).show()
            return
        }

        val source = fileToCopy!!
        // Tạo file đích tại thư mục hiện tại (currentPath)
        val dest = File(currentPath, source.name)

        if (dest.exists()) {
            Toast.makeText(this, "File đã tồn tại ở thư mục này!", Toast.LENGTH_SHORT).show()
            return
        }

        try {
            // Thực hiện copy dữ liệu
            source.inputStream().use { input ->
                dest.outputStream().use { output ->
                    input.copyTo(output)
                }
            }
            // Load lại danh sách để thấy file mới
            loadFiles(currentPath)
            Toast.makeText(this, "Đã dán file thành công!", Toast.LENGTH_SHORT).show()

            // (Tuỳ chọn) Reset biến copy nếu chỉ muốn paste 1 lần
            // fileToCopy = null
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Lỗi khi dán file: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    // --- CÁC HÀM XỬ LÝ DIALOG VÀ LOGIC ---

    // Hộp thoại tạo mới
    private fun showCreateDialog(isFolder: Boolean) {
        val input = EditText(this)
        AlertDialog.Builder(this)
            .setTitle(if (isFolder) "Tạo thư mục" else "Tạo file")
            .setView(input)
            .setPositiveButton("Tạo") { _, _ ->
                val name = input.text.toString()
                if (name.isNotEmpty()) {
                    val newFile = File(currentPath, if (isFolder) name else "$name.txt")
                    if (isFolder) newFile.mkdir() else newFile.createNewFile()
                    loadFiles(currentPath) // Refresh list
                }
            }
            .setNegativeButton("Hủy", null)
            .show()
    }

    // Hộp thoại Đổi tên
    private fun showRenameDialog() {
        val file = selectedFile ?: return
        val input = EditText(this)
        input.setText(file.name)
        AlertDialog.Builder(this)
            .setTitle("Đổi tên")
            .setView(input)
            .setPositiveButton("Lưu") { _, _ ->
                val newName = input.text.toString()
                val newFile = File(file.parent, newName)
                if (file.renameTo(newFile)) {
                    loadFiles(currentPath)
                    Toast.makeText(this, "Đổi tên thành công", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Hủy", null)
            .show()
    }

    // Hộp thoại Xóa
    private fun showDeleteDialog() {
        val file = selectedFile ?: return
        AlertDialog.Builder(this)
            .setTitle("Xác nhận xóa")
            .setMessage("Bạn có chắc muốn xóa ${file.name}?")
            .setPositiveButton("Xóa") { _, _ ->
                file.deleteRecursively() // Xóa cả thư mục con nếu là folder
                loadFiles(currentPath)
                Toast.makeText(this, "Đã xóa", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Không", null)
            .show()
    }

    // Hộp thoại Copy (Copy đến cùng thư mục nhưng thêm đuôi _copy để demo)
    // Thực tế bạn có thể hiện danh sách thư mục để chọn nơi đến.
    private fun showCopyDialog() {
        val file = selectedFile ?: return

        AlertDialog.Builder(this)
            .setTitle("Sao chép")
            .setMessage("Bạn muốn sao chép file '${file.name}'?\n\n(Sau khi chọn 'Có', hãy di chuyển đến thư mục khác và chọn 'Dán' ở menu góc phải)")
            .setPositiveButton("Có") { _, _ ->
                // Lưu file vào biến tạm
                fileToCopy = file
                Toast.makeText(this, "Đã nhớ file! Hãy đến thư mục đích và chọn 'Dán'", Toast.LENGTH_LONG).show()
            }
            .setNegativeButton("Không", null)
            .show()
    }

    // Xử lý nút Back để quay lại thư mục cha
    override fun onBackPressed() {
        if (currentPath.absolutePath != Environment.getExternalStorageDirectory().absolutePath) {
            currentPath = currentPath.parentFile ?: Environment.getExternalStorageDirectory()
            loadFiles(currentPath)
        } else {
            super.onBackPressed()
        }
    }
}