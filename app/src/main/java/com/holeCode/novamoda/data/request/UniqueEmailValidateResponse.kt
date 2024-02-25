package com.holeCode.novamoda.data.request

import com.holeCode.novamoda.domain.model.User

data class UniqueEmailValidateResponse(
    val isUnique: Boolean, val user: User,
)
