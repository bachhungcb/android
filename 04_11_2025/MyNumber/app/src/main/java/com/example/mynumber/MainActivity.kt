package com.example.mynumber

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.example.mynumber.databinding.ActivityMainBinding
import kotlin.math.sqrt

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    // Adapter để cung cấp dữ liệu cho ListView
    private lateinit var listAdapter: ArrayAdapter<Int>

    // Danh sách chứa kết quả
    private val resultsList = ArrayList<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1. Setup ViewBinding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 2. Setup ListView Adapter
        // Sử dụng layout có sẵn của Android cho một mục danh sách đơn giản
        listAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, resultsList)
        binding.listViewResults.adapter = listAdapter

        // 3. Setup Listeners (Bộ lắng nghe sự kiện)
        setupListeners()

        // 4. Cập nhật danh sách lần đầu (để hiển thị trạng thái rỗng)
        updateList()
    }

    private fun setupListeners() {
        // Lắng nghe thay đổi trên EditText
        binding.edittextNumber.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                // Khi văn bản thay đổi, gọi hàm cập nhật
                updateList()
            }
        })

        // Lắng nghe thay đổi lựa chọn trên RadioGroup
        binding.radiogroupFilter.setOnCheckedChangeListener { group, checkedId ->
            // Khi lựa chọn thay đổi, gọi hàm cập nhật
            updateList()
        }
    }

    /**
     * Hàm trung tâm: Tính toán và cập nhật danh sách kết quả
     */
    private fun updateList() {
        // 1. Lấy giá trị từ các View
        val limit = binding.edittextNumber.text.toString().toIntOrNull() ?: 0
        val checkedId = binding.radiogroupFilter.checkedRadioButtonId

        // 2. Xóa danh sách kết quả cũ
        resultsList.clear()

        // 3. Tạo danh sách mới
        // "nhỏ hơn giá trị trong EditText" -> lặp từ 1 đến (limit - 1)
        for (i in 1 until limit) {
            val shouldAdd = when (checkedId) {
                binding.radioAll.id -> true
                binding.radioEven.id -> i % 2 == 0
                binding.radioOdd.id -> i % 2 != 0
                binding.radioPrime.id -> isPrime(i)
                binding.radioPerfect.id -> isPerfect(i)
                // == SỬA DÒNG NÀY ==
                binding.radioSquare.id -> isSquare(i.toLong()) // Thêm .toLong()

                // == THÊM DÒNG NÀY ==
                binding.radioFibonacci.id -> isFibonacci(i)
                else -> false
            }

            if (shouldAdd) {
                resultsList.add(i)
            }
        }

        // 4. Cập nhật giao diện
        if (resultsList.isEmpty()) {
            // Nếu danh sách rỗng, ẩn ListView và hiện TextView thông báo
            binding.listViewResults.visibility = View.GONE
            binding.textviewEmpty.visibility = View.VISIBLE
        } else {
            // Nếu có kết quả, hiện ListView và ẩn TextView thông báo
            binding.listViewResults.visibility = View.VISIBLE
            binding.textviewEmpty.visibility = View.GONE
        }

        // 5. Thông báo cho Adapter biết dữ liệu đã thay đổi
        listAdapter.notifyDataSetChanged()
    }

    // --- CÁC HÀM PHỤ TRỢ (HELPER FUNCTIONS) ---

    /**
     * Kiểm tra số nguyên tố (Prime)
     */
    private fun isPrime(n: Int): Boolean {
        if (n <= 1) return false
        for (i in 2..sqrt(n.toDouble()).toInt()) {
            if (n % i == 0) return false
        }
        return true
    }

    /**
     * Kiểm tra số hoàn hảo (Perfect Number)
     * Là số có tổng các ước (không kể nó) bằng chính nó. (Ví dụ: 6 = 1 + 2 + 3)
     */
    private fun isPerfect(n: Int): Boolean {
        if (n <= 1) return false
        var sum = 1 // Bắt đầu từ 1 (vì 1 luôn là ước)
        for (i in 2..sqrt(n.toDouble()).toInt()) {
            if (n % i == 0) {
                sum += i
                if (i * i != n) { // Thêm ước còn lại
                    sum += n / i
                }
            }
        }
        return sum == n
    }

    /**
     * Kiểm tra số chính phương (Square Number)
     * Là số có căn bậc hai là một số nguyên. (Ví dụ: 9, 16)
     */
    private fun isSquare(n: Long): Boolean { // <-- Sửa Int thành Long
        if (n < 0) return false
        val sqrt = sqrt(n.toDouble()).toLong() // <-- Dùng toLong()
        return sqrt * sqrt == n
    }

    private fun isFibonacci(n: Int): Boolean {
        if (n < 0) return false

        // Chuyển sang Long để tính toán, tránh tràn số
        val nLong = n.toLong()
        val test1 = 5 * nLong * nLong + 4
        val test2 = 5 * nLong * nLong - 4

        // Dùng hàm isSquare (đã sửa) để kiểm tra
        return isSquare(test1) || isSquare(test2)
    }
}