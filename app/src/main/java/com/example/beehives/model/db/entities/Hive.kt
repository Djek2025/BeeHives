package com.example.beehives.model.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.beehives.view.activities.DEFAULT_PHOTO_HIVE

@Entity
data class Hive(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val apiaryId: Int? = null,
    val name: String? = "Hive",
    val breed: String? = "Golden Italian",
    val beeQueenId: Int? = null,
    val label: String? = null,
    var photo: String? = DEFAULT_PHOTO_HIVE,
    val todoList: String? = null
)