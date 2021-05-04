package com.example.testwordcount.repository

import com.example.testwordcount.entities.TextFile

interface FilesRepository {
    suspend fun readFile(name: String): String
    suspend fun processText(name: String, text: String): TextFile
}