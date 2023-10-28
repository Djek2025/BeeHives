package com.example.beehives.model.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.beehives.utils.DEFAULT_PHOTO_HIVE

@Entity
data class Hive(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val apiaryId: Int? = null,
    var name: String? = "Hive",
    var breed: String? = "Golden Italian",
    var beeQueenId: Int? = null,
    val label: String? = null,
    var photo: String? = DEFAULT_PHOTO_HIVE
)