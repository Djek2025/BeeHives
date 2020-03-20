package com.example.beehives.model.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Apiary(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val name: String? = "Apiary",
    val location: String? = null,
    val todoList: String? = null
)