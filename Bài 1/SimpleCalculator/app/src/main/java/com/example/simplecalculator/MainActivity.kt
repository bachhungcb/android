package com.example.simplecalculator

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    // Khai báo biến cho màn hình hiển thị
    private lateinit var tvResult: TextView

    // --- Biến lưu trữ trạng thái ---
    private var currentInput: String = "0"
    private var previousInput: String = ""
    private var currentOperation: String = ""
    private var isNewEntry: Boolean = true
    private var hasDecimalPoint: Boolean = false // Xử lý nút "."

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main) // Liên kết file Kotlin với file XML

        // 1. Ánh xạ (link) tvResult từ file XML
        tvResult = findViewById(R.id.tvResult)
        updateDisplay() // Cập nhật màn hình lần đầu

        // 2. Gán Listener cho các nút SỐ (0-9)
        findViewById<Button>(R.id.btn0).setOnClickListener { handleNumberInput("0") }
        findViewById<Button>(R.id.btn1).setOnClickListener { handleNumberInput("1") }
        findViewById<Button>(R.id.btn2).setOnClickListener { handleNumberInput("2") }
        findViewById<Button>(R.id.btn3).setOnClickListener { handleNumberInput("3") }
        findViewById<Button>(R.id.btn4).setOnClickListener { handleNumberInput("4") }
        findViewById<Button>(R.id.btn5).setOnClickListener { handleNumberInput("5") }
        findViewById<Button>(R.id.btn6).setOnClickListener { handleNumberInput("6") }
        findViewById<Button>(R.id.btn7).setOnClickListener { handleNumberInput("7") }
        findViewById<Button>(R.id.btn8).setOnClickListener { handleNumberInput("8") }
        findViewById<Button>(R.id.btn9).setOnClickListener { handleNumberInput("9") }

        // 3. Gán Listener cho các nút PHÉP TOÁN (+, -, x, /)
        findViewById<Button>(R.id.btnPlus).setOnClickListener { handleOperatorInput("+") }
        findViewById<Button>(R.id.btnMinus).setOnClickListener { handleOperatorInput("-") }
        findViewById<Button>(R.id.btnMultiply).setOnClickListener { handleOperatorInput("x") }
        findViewById<Button>(R.id.btnDivide).setOnClickListener { handleOperatorInput("/") }

        // 4. Gán Listener cho nút BẰNG (=)
        findViewById<Button>(R.id.btnEquals).setOnClickListener { handleEquals() }

        // 5. Gán Listener cho các nút CHỨC NĂNG (C, CE, BS, +/-, .)
        findViewById<Button>(R.id.btnC).setOnClickListener { handleClearAll() }
        findViewById<Button>(R.id.btnCE).setOnClickListener { handleClearEntry() }
        findViewById<Button>(R.id.btnBS).setOnClickListener { handleBackspace() }
        findViewById<Button>(R.id.btnPlusMinus).setOnClickListener { handlePlusMinus() }
        findViewById<Button>(R.id.btnDot).setOnClickListener { handleDecimalPoint() }
    }

    // --- Các hàm xử lý logic ---

    private fun handleNumberInput(number: String) {
        if (isNewEntry) {
            currentInput = number
            isNewEntry = false
            // Reset cờ dấu thập phân khi bắt đầu số mới
            hasDecimalPoint = if (number == ".") true else false
        } else {
            // Ngăn không cho số bắt đầu bằng 0 trừ khi đó là "0."
            if (currentInput == "0") {
                currentInput = number
            } else {
                currentInput += number
            }
        }
        updateDisplay()
    }

    private fun handleOperatorInput(operator: String) {
        // Nếu người dùng bấm 5 -> + -> 3 -> + ... (bấm phép toán liên tiếp)
        if (previousInput.isNotEmpty() && currentOperation.isNotEmpty() && !isNewEntry) {
            handleEquals() // Tính toán phép trước
        }

        previousInput = currentInput
        currentOperation = operator
        isNewEntry = true // Sẵn sàng nhận số mới (toán hạng 2)
        hasDecimalPoint = false // Reset cờ dấu thập phân
    }

    private fun handleEquals() {
        if (previousInput.isEmpty() || currentOperation.isEmpty() || isNewEntry) {
            return
        }

        try {
            // Yêu cầu là số nguyên, nên ta dùng Long
            val num1 = previousInput.toLong()
            val num2 = currentInput.toLong()
            var result: Long = 0

            when (currentOperation) {
                "+" -> result = num1 + num2
                "-" -> result = num1 - num2
                "x" -> result = num1 * num2
                "/" -> {
                    if (num2 == 0L) { // 0L là kiểu Long
                        tvResult.text = "Error" // Lỗi chia cho 0
                        resetCalculatorState()
                        isNewEntry = true
                        return
                    }
                    result = num1 / num2 // Phép chia số nguyên
                }
            }

            currentInput = result.toString() // Cập nhật kết quả

            updateDisplay()
            resetCalculatorState() // Đặt lại trạng thái
            isNewEntry = true
            hasDecimalPoint = false

        } catch (e: NumberFormatException) {
            // Xử lý nếu nhập số quá lớn hoặc lỗi ".":
            // Vì yêu cầu là số nguyên, ta chỉ xử lý lỗi cơ bản
            if (previousInput.contains(".") || currentInput.contains(".")) {
                tvResult.text = "Error: Not Integer"
            } else {
                tvResult.text = "Error"
            }
            resetCalculatorState()
            isNewEntry = true
        }
    }

    // Nút C: Xóa phép toán, nhập lại phép toán từ đầu
    private fun handleClearAll() {
        currentInput = "0"
        previousInput = ""
        currentOperation = ""
        isNewEntry = true
        hasDecimalPoint = false
        updateDisplay()
    }

    // Nút CE: Xóa giá trị toán hạng hiện tại về 0
    private fun handleClearEntry() {
        currentInput = "0"
        isNewEntry = true
        hasDecimalPoint = false
        updateDisplay()
    }

    // Nút BS: Xóa chữ số hàng đơn vị của toán hạng hiện tại
    private fun handleBackspace() {
        if (isNewEntry) return // Không xóa nếu đang là kết quả của phép toán

        if (currentInput.length > 1) {
            if (currentInput.endsWith(".")) {
                hasDecimalPoint = false
            }
            currentInput = currentInput.dropLast(1)
        } else {
            currentInput = "0"
            isNewEntry = true
            hasDecimalPoint = false
        }
        updateDisplay()
    }

    // Xử lý nút +/-
    private fun handlePlusMinus() {
        if (currentInput == "0" || isNewEntry) return
        currentInput = if (currentInput.startsWith("-")) {
            currentInput.substring(1)
        } else {
            "-$currentInput"
        }
        updateDisplay()
    }

    // Xử lý nút .
    private fun handleDecimalPoint() {
        // Bài toán yêu cầu số nguyên, nên ta có thể vô hiệu hóa nút này
        // Hoặc ta xử lý nó, nhưng `handleEquals` sẽ báo lỗi

        // Cách 1: Vô hiệu hóa (không làm gì cả)
        return;

        /* // Cách 2: Cho phép nhập, nhưng handleEquals sẽ báo lỗi (như đã code)
        if (!hasDecimalPoint) {
            if (isNewEntry) {
                currentInput = "0."
                isNewEntry = false
            } else {
                currentInput += "."
            }
            hasDecimalPoint = true
            updateDisplay()
        }
        */
    }

    // Hàm cập nhật màn hình
    private fun updateDisplay() {
        tvResult.text = currentInput
    }

    // Hàm reset trạng thái phép toán
    private fun resetCalculatorState() {
        previousInput = ""
        currentOperation = ""
    }
}