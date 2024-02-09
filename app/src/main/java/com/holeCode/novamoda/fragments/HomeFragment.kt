package com.holeCode.novamoda.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.holeCode.novamoda.R
import com.holeCode.novamoda.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private lateinit var homeBinding:FragmentHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        homeBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        return homeBinding.root
    }

}