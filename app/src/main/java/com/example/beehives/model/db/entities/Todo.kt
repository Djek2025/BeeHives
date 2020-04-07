package com.example.beehives.model.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Todo (
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val ownerId: Int? = null,
    var text: String? = null,
    var completed: Boolean? = false
)