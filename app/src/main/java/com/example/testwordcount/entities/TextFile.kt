package com.example.testwordcount.entities

import java.util.*

data class TextFile(
    val name: String,
    val mapTimes: List<String>,
    val mapOrder: List<String>,
    val mapPosition: List<String>
)
