package com.example.test_trip_logic.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.test_trip_logic.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class MyBottomSheet : BottomSheetDialogFragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =inflater.inflate(R.layout.fragment_my_bottom_sheet, container, false)


        val button = view.findViewById<Button>(R.id.actionButton)

        button.setOnClickListener {
            dismiss()
        }

        return view
    }


}