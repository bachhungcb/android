package com.example.userdataform

import android.app.DatePickerDialog
import android.graphics.Color
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.userdataform.databinding.ActivityMainBinding
import java.util.Calendar // Cần import Calendar

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var defaultEditTextBg: Drawable? = null
    private var defaultRadioGroupBg: Drawable? = null
    private var defaultCheckboxBg: Drawable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Lưu lại background mặc định
        defaultEditTextBg = binding.etFirstName.background
        defaultRadioGroupBg = binding.rgGender.background
        defaultCheckboxBg = binding.cbTerms.background

        // Thiết lập các trình lắng nghe sự kiện
        setupListeners()
    }

    private fun setupListeners() {
        // --- Logic 1: Sửa lại nút Select ---
        binding.btnSelectDate.setOnClickListener {
            // Gọi hàm hiển thị hộp thoại lịch
            showDatePickerDialog()
        }

        // --- Logic 2: Nút Register (Giữ nguyên) ---
        binding.btnRegister.setOnClickListener {
            if (validateForm()) {
                Toast.makeText(this, "Registration Successful!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Please fill in all required fields.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // --- HÀM MỚI: Hiển thị hộp thoại lịch ---
    private fun showDatePickerDialog() {
        // Lấy ngày tháng hiện tại để làm giá trị mặc định
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        // Tạo DatePickerDialog
        val datePickerDialog = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDayOfMonth ->
                // Hàm này được gọi khi người dùng chọn ngày
                // (Lưu ý: tháng trả về bắt đầu từ 0, nên cần +1)
                val selectedDate = "$selectedDayOfMonth/${selectedMonth + 1}/$selectedYear"

                // Cập nhật vào EditText
                binding.etBirthday.setText(selectedDate)
            },
            year,
            month,
            day
        )

        // Hiển thị hộp thoại
        datePickerDialog.show()
    }


    // --- Các hàm validateForm() và resetFieldBackgrounds() ---
    // (Giữ nguyên, không thay đổi gì ở đây)

    private fun validateForm(): Boolean {
        resetFieldBackgrounds()
        var isValid = true
        val errorColor = Color.parseColor("#FFCDD2")

        if (binding.etFirstName.text.isBlank()) {
            binding.etFirstName.setBackgroundColor(errorColor)
            isValid = false
        }
        if (binding.etLastName.text.isBlank()) {
            binding.etLastName.setBackgroundColor(errorColor)
            isValid = false
        }
        if (binding.rgGender.checkedRadioButtonId == -1) {
            binding.rgGender.setBackgroundColor(errorColor)
            isValid = false
        }
        if (binding.etBirthday.text.isBlank()) {
            binding.etBirthday.setBackgroundColor(errorColor)
            isValid = false
        }
        if (binding.etAddress.text.isBlank()) {
            binding.etAddress.setBackgroundColor(errorColor)
            isValid = false
        }
        if (binding.etEmail.text.isBlank()) {
            binding.etEmail.setBackgroundColor(errorColor)
            isValid = false
        }
        if (!binding.cbTerms.isChecked) {
            binding.cbTerms.setBackgroundColor(errorColor)
            isValid = false
        }
        return isValid
    }

    private fun resetFieldBackgrounds() {
        binding.etFirstName.background = defaultEditTextBg
        binding.etLastName.background = defaultEditTextBg
        binding.rgGender.background = defaultRadioGroupBg
        binding.etBirthday.background = defaultEditTextBg
        binding.etAddress.background = defaultEditTextBg
        binding.etEmail.background = defaultEditTextBg
        binding.cbTerms.background = defaultCheckboxBg
    }
}
