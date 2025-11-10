package com.example.currency

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.example.currency.databinding.ActivityMainBinding
import java.text.DecimalFormat

class MainActivity : AppCompatActivity() {

    // Khởi tạo ViewBinding
    private lateinit var binding: ActivityMainBinding

    // 1. Định nghĩa tỷ giá cố định (hardcoded rates)
    // Lấy USD làm đồng tiền cơ sở (base currency)
    private val exchangeRates = mapOf(
        "USD" to 1.0,
        "VND" to 25450.0,
        "EUR" to 0.92,
        "JPY" to 157.5,
        "GBP" to 0.79,
        "AUD" to 1.5,
        "CAD" to 1.37,
        "CHF" to 0.89,
        "CNY" to 7.25,
        "KRW" to 1380.0,
        "SGD" to 1.35
    )

    // Cờ (flag) để ngăn vòng lặp vô hạn khi cập nhật EditText
    private var isUpdating = false

    // Định dạng số cho đẹp
    private val decimalFormat = DecimalFormat("#,##0.##")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 2. Sử dụng ViewBinding để hiển thị layout
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 3. Khởi tạo Spinners
        setupSpinners()

        // 4. Khởi tạo Listeners (bộ lắng nghe sự kiện)
        setupListeners()
    }

    private fun setupSpinners() {
        // Lấy danh sách các mã tiền tệ từ Map
        val currencies = exchangeRates.keys.toList()

        // Tạo một Adapter (bộ điều hợp) để cung cấp dữ liệu cho Spinner
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, currencies)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Gán adapter cho cả hai spinner
        binding.spinnerFrom.adapter = adapter
        binding.spinnerTo.adapter = adapter

        // Đặt giá trị mặc định (ví dụ: USD -> VND)
        binding.spinnerFrom.setSelection(currencies.indexOf("USD"))
        binding.spinnerTo.setSelection(currencies.indexOf("VND"))
    }

    private fun setupListeners() {
        // Lắng nghe sự kiện thay đổi văn bản trên EditText "TỪ" (FROM)
        binding.edittextAmountFrom.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                // Chỉ thực hiện nếu cờ 'isUpdating' là false
                // và EditText này đang được focus (người dùng đang gõ)
                if (!isUpdating && binding.edittextAmountFrom.isFocused) {
                    convertCurrency(isUpdatingFrom = true)
                }
            }
        })

        // Lắng nghe sự kiện thay đổi văn bản trên EditText "ĐẾN" (TO)
        binding.edittextAmountTo.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                // Chỉ thực hiện nếu cờ 'isUpdating' là false
                // và EditText này đang được focus (người dùng đang gõ)
                if (!isUpdating && binding.edittextAmountTo.isFocused) {
                    convertCurrency(isUpdatingFrom = false)
                }
            }
        })

        // Lắng nghe sự kiện thay đổi lựa chọn trên cả hai Spinner
        val spinnerListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                // Khi spinner thay đổi, luôn tính toán lại
                // dựa trên giá trị của EditText "TỪ" (FROM)
                convertCurrency(isUpdatingFrom = true)
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        binding.spinnerFrom.onItemSelectedListener = spinnerListener
        binding.spinnerTo.onItemSelectedListener = spinnerListener
    }

    /**
     * Hàm tính toán và cập nhật giá trị chuyển đổi
     * @param isUpdatingFrom:
     * - true: Người dùng đang nhập ở ô "TỪ" (FROM) -> tính toán và cập nhật ô "ĐẾN" (TO)
     * - false: Người dùng đang nhập ở ô "ĐẾN" (TO) -> tính toán và cập nhật ô "TỪ" (FROM)
     */
    private fun convertCurrency(isUpdatingFrom: Boolean) {
        // Đặt cờ 'isUpdating' thành true để ngăn các TextWatcher khác chạy
        isUpdating = true

        // Xác định đâu là nguồn (source) và đâu là đích (target)
        val sourceEditText = if (isUpdatingFrom) binding.edittextAmountFrom else binding.edittextAmountTo
        val targetEditText = if (isUpdatingFrom) binding.edittextAmountTo else binding.edittextAmountFrom
        val sourceSpinner = if (isUpdatingFrom) binding.spinnerFrom else binding.spinnerTo
        val targetSpinner = if (isUpdatingFrom) binding.spinnerTo else binding.spinnerFrom

        // 1. Lấy giá trị từ các thành phần
        val amountString = sourceEditText.text.toString()
        val fromCurrency = sourceSpinner.selectedItem.toString()
        val toCurrency = targetSpinner.selectedItem.toString()

        // 2. Chuyển đổi số tiền
        val amount = amountString.toDoubleOrNull()
        if (amount == null || amount == 0.0) {
            // Nếu không có số hoặc số là 0, xóa văn bản ở ô đích
            targetEditText.setText("")
            isUpdating = false // Mở cờ
            return
        }

        // 3. Lấy tỷ giá
        val fromRate = exchangeRates[fromCurrency] ?: 1.0 // Tỷ giá của đồng tiền nguồn
        val toRate = exchangeRates[toCurrency] ?: 1.0     // Tỷ giá của đồng tiền đích

        // 4. Công thức tính toán
        // (Amount / FromRate) = Số tiền ở đồng tiền cơ sở (USD)
        // (Amount / FromRate) * ToRate = Số tiền ở đồng tiền đích
        val result = (amount / fromRate) * toRate

        // 5. Hiển thị kết quả đã định dạng
        targetEditText.setText(decimalFormat.format(result))

        // Mở cờ 'isUpdating' sau khi hoàn tất
        isUpdating = false
    }
}