package com.example.studentmanagent

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.studentmanagent.databinding.FragmentStudentFormBinding


class StudentFormFragment : Fragment() {
    private lateinit var binding: FragmentStudentFormBinding
    private val viewModel: StudentViewModel by activityViewModels()
    private val args: StudentFormFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentStudentFormBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val receivedStudent = args.student
        val isUpdate = receivedStudent != null

        binding.tvTitle.text = if (isUpdate) "Cập Nhật Sinh Viên" else "Thêm Sinh Viên Mới"

        // Tạo object tạm để Binding (Copy dữ liệu cũ nếu là sửa, hoặc tạo mới)
        val tempStudent = if (isUpdate) receivedStudent!!.copy() else Student("", "")

        binding.student = tempStudent

        binding.btnSave.setOnClickListener {
            if (tempStudent.id.isNotEmpty() && tempStudent.name.isNotEmpty()) {
                if (isUpdate) {
                    // Update: Truyền MSSV gốc (receivedStudent.mssv) và thông tin mới (tempStudent)
                    viewModel.updateStudent(receivedStudent!!.id, tempStudent)
                    Toast.makeText(context, "Đã cập nhật", Toast.LENGTH_SHORT).show()
                } else {
                    // Add New
                    viewModel.addStudent(tempStudent)
                    Toast.makeText(context, "Đã thêm mới", Toast.LENGTH_SHORT).show()
                }
                findNavController().popBackStack()
            } else {
                Toast.makeText(context, "Vui lòng nhập đủ thông tin", Toast.LENGTH_SHORT).show()
            }
        }
    }
}