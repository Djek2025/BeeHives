package com.example.beehives.model.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Apiary(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    var name: String? = "Apiary",
    var location: String? = null,
    val todoList: String? = null
)