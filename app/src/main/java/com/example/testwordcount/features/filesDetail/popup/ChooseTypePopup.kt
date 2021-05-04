package com.example.testwordcount.features.filesDetail.popup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.example.testwordcount.databinding.PopupChooseListTypeBinding

class ChooseTypePopup : DialogFragment()  {

    companion object {
        const val TAG = "ChooseTypePopup"
    }

    private lateinit var binding: PopupChooseListTypeBinding
    private lateinit var viewModel: ChooseTypeViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = PopupChooseListTypeBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(ChooseTypeViewModel::class.java)
        dialog?.window?.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT)
        setupClickListeners(view)
    }

    private fun setupClickListeners(view: View) {
        binding.positionBtn.setOnClickListener {
            viewModel.sentListType(ChooseTypeViewModel.LIST_TYPE.POSITION)
            dismiss()
        }
        binding.alphabeticalBtn.setOnClickListener {
            viewModel.sentListType(ChooseTypeViewModel.LIST_TYPE.ALPHABETICAL)
            dismiss()
        }
        binding.timesBtn.setOnClickListener {
            viewModel.sentListType(ChooseTypeViewModel.LIST_TYPE.TIMES)
            dismiss()
        }
    }

}