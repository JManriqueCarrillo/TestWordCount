package com.example.testwordcount.entities

import java.util.*

data class TextFile(
    val name: String,
    val mapTimes: SortedMap<String, Int>,
    val mapOrder: SortedSet<String>,
    val mapPosition: MutableSet<String>
)
