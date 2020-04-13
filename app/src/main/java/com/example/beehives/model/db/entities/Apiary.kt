package com.example.beehives.model.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.beehives.utils.SEPARATOR

@Entity
data class Apiary(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    var name: String? = "Apiary",
    var location: String? = SEPARATOR
)