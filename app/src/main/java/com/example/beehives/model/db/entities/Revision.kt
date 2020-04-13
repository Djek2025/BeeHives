package com.example.beehives.model.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Revision(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val hiveId: Int? = null,
    var date: Long? = 1583020800L,
    val strength: Int? = 60,
    val frames: String? = null,
    val note: String? = null
)