package com.holeCode.novamoda.auth.data

import com.holeCode.novamoda.pojo.User

data class UniqueEmailValidateResponse(
    val isUnique: Boolean, val user: User,
)
