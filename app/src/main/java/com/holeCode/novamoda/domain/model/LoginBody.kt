package com.holeCode.novamoda.domain.model

/*this is login body
* {
	"email": "algazzar.abdelrahman@gmail.com",
	"password": "123456"
}*/
data class LoginBody(
    val email: String?,
    val password: String?,
)
