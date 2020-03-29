package com.example.beehives.model.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class BeeQueen (
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val hiveId: Int? = null,
    val year: Int? = null,
    val description: String? = null,
    val note: String? = null
)