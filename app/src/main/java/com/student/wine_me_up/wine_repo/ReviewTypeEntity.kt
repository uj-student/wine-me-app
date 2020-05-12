package com.student.wine_me_up.wine_repo

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ReviewTypeEntity (
    @PrimaryKey(autoGenerate = true) val id: Int?,
    val type_description: String
)