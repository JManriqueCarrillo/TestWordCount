package com.example.testwordcount.repository.impl

import android.content.Context
import com.example.testwordcount.repository.AssetsRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.InputStream
import javax.inject.Inject

class AssetsRepositoryImpl @Inject constructor(
    @ApplicationContext val context: Context
) : AssetsRepository {

    override fun getFiles(): List<String> {
        try {
            return context.assets.list("")!!.filter {
                it.contains(".txt")
            }.toList()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return listOf()
    }
}