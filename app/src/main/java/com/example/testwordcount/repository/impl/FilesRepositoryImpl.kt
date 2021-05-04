package com.example.testwordcount.repository.impl

import android.content.Context
import com.example.testwordcount.entities.TextFile
import com.example.testwordcount.repository.FilesRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.InputStream
import javax.inject.Inject

class FilesRepositoryImpl @Inject constructor(
    @ApplicationContext val context: Context
) : FilesRepository {

    override suspend fun readFile(name: String): String {
        val inputStream: InputStream = context.assets.open(name)
        val size: Int = inputStream.available()
        val buffer = ByteArray(size)
        inputStream.read(buffer)
        return String(buffer)
    }

    override suspend fun processText(name: String, text: String): TextFile {
        val mapTimes = sortedMapOf<String, Int>()
        val mapOrder = sortedSetOf<String>()
        val mapPosition = mutableSetOf<String>()

        val pattern = Regex("""[\(\)\:\-\,\;\:\*\.\?\!\$\"\']""")
        val processedText = text.replace(pattern, " ").replace("\\s+".toRegex(), " ")

        val wordsList = processedText.split(" ")
        wordsList.forEach { word ->
            if (word.isNotBlank()) {
                //val normalizedWord = normalizeWord(word)
                val normalizedWord = word
                mapPosition.add(normalizedWord)
                if (mapTimes.containsKey(normalizedWord)) {
                    mapTimes[normalizedWord] = mapTimes[normalizedWord]!! + 1
                } else {
                    mapTimes[normalizedWord] = 1
                    mapOrder.add(normalizedWord)
                }
            }
        }

        return TextFile(
            name,
            mapTimes.toList().sortedByDescending { it.second }.map { "${it.first} (${it.second})" },
            mapOrder.toList(),
            mapPosition.toList()
        )
    }

    private fun normalizeWord(word: String): String {
        return word.toLowerCase().capitalize()
    }
}