package com.example.seniorbetterlife.helpPart.model

import com.example.seniorbetterlife.data.model.User
import java.util.*

data class UserTask(val userAddress: UserAddress,
                    val user: User,
                    val date: String,
                    val description: String,
                    val finished: Boolean
                    ) {
    constructor() : this(UserAddress("", Double.NaN,Double.NaN,), User(),"","",false)
}
