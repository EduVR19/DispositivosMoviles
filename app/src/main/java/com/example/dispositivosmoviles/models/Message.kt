package com.example.dispositivosmoviles.models

import android.net.Uri
import java.util.*

data class Message (
    var message: String = "",
    var from: String = "",
    var dob: Date = Date(),
    var imgUrl : String=""
)
