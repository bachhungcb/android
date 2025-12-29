package com.example.studentmanagent

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.studentmanagent.databinding.FragmentStudentListBinding

class StudentListFragment : Fragment() {
    private lateinit var binding: FragmentStudentListBinding
    private val viewModel: StudentViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentStudentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = StudentAdapter(mutableListOf(),
            onEditClick = { _, student -> // Sửa lại StudentAdapter để callback trả về object Student
                val action = StudentListFragmentDirections.actionListToForm(student)
                findNavController().navigate(action)
            },
            onDeleteClick = { _, student ->
                viewModel.deleteStudent(student)
            }
        )

        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = adapter

        viewModel.students.observe(viewLifecycleOwner) { list ->
            // Cần cập nhật hàm updateData trong Adapter
            adapter.updateData(list)
        }

        binding.fabAdd.setOnClickListener {
            val action = StudentListFragmentDirections.actionListToForm(null)
            findNavController().navigate(action)
        }
    }
}