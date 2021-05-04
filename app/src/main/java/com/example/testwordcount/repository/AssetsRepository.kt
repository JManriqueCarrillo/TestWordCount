package com.example.testwordcount.repository

interface AssetsRepository {
    fun getFiles(): List<String>
}