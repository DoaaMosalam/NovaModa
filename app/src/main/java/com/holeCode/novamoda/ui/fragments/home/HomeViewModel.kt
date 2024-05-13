package com.holeCode.novamoda.ui.fragments.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.doaamosallam.domain.usecase.NovaUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val novaUseCase: NovaUseCase,
    application: Application
) : AndroidViewModel(application) {


}