package com.example.testwordcount.io

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.InputStream
import javax.inject.Inject

class FileProcessor @Inject constructor(@ApplicationContext val context: Context) {

    suspend fun readFile(nameFile: String): String {
        val inputStream: InputStream = context.assets.open(nameFile)
        val size: Int = inputStream.available()
        val buffer = ByteArray(size)
        inputStream.read(buffer)
        return String(buffer)
    }

}