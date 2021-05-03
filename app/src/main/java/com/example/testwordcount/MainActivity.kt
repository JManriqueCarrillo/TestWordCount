package com.example.testwordcount

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import java.io.IOException
import java.io.InputStream
import java.util.*
import kotlin.collections.HashMap

class MainActivity : AppCompatActivity() {

    private var mapTimes = sortedMapOf<String, Int>()
    private var mapOrder = sortedSetOf<String>()
    private var mapPosition = mutableSetOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val string: String
        try {
            val inputStream: InputStream = assets.open("plrabn12.txt")
            val size: Int = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            string = String(buffer)
            processText(string)
            Log.d("RES", "Times (${mapTimes.size}): ${mapTimes.toList().sortedByDescending { it.second }.toMap()}")
            Log.d("RES", "Order (${mapOrder.size}): $mapOrder")
            Log.d("RES", "Position (${mapPosition.size}): $mapPosition")
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun processText(text: String) {

        val pattern = Regex("""[\(\)\:\-\,\;\:\*\.\?\!\$\"\']""")
        val processedText = text.replace(pattern, " ").replace("\\s+".toRegex(), " ")

        val wordsList = processedText.split(" ")
        wordsList.forEach { word ->
            if (word.isNotBlank()) {
                val normalizedWord = normalizeWord(word)
                mapPosition.add(normalizedWord)
                if (mapTimes.containsKey(normalizedWord)) {
                    mapTimes[normalizedWord] = mapTimes[normalizedWord]!! + 1
                } else {
                    mapTimes[normalizedWord] = 1
                    mapOrder.add(normalizedWord)
                }
            }
        }
    }

    private fun normalizeWord(word: String): String {
        return word.toLowerCase().capitalize()
    }
}