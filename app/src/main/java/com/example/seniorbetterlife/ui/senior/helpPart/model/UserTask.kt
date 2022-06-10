package com.example.seniorbetterlife.ui.senior.helpPart.model

import com.example.seniorbetterlife.data.model.User

data class UserTask(
    val userAddress: UserAddress,
    val user: User,
    val date: String,
    val description: String,
    val finished: Boolean,
    val volunteer: User?
                    ) {
    constructor() : this(UserAddress("", Double.NaN,Double.NaN,), User(),"","",false, User())
}
