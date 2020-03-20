package com.example.beehives.model.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Hive(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val apiaryId: Int? = null,
    val name: String? = "Hive",
    val breed: String? = "Golden Italian",
    val beeQueenId: Int? = null,
    val label: String? = null,
    val photo: String? = null,
    val todoList: String? = null
)