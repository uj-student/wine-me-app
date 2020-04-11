package com.student.wine_me_up.wine_repo

import androidx.room.*

@Entity
data class Wine(
    @PrimaryKey val wine_id: Int,
    @ColumnInfo(name = "wine") var wine: String,
    @ColumnInfo(name = "wine_slug") val wine_slug: String,
    @ColumnInfo(name = "appellation") val appellation: String,
    @ColumnInfo(name = "appellation_slug") val appellation_slug: String,
    @ColumnInfo(name = "color") val color: String,
    @ColumnInfo(name = "wine_type") val wine_type: String,
    @ColumnInfo(name = "regions") val regions: String,
    @ColumnInfo(name = "country") val country: String,
    @ColumnInfo(name = "classification") val classification: String?,
    @ColumnInfo(name = "vintage") val vintage: String,
    @ColumnInfo(name = "date") val date: String,
    @ColumnInfo(name = "is_primeurs") val is_primeurs: Boolean,
    @ColumnInfo(name = "score") val score: Double,
    @ColumnInfo(name = "confidence_index") val confidence_index: String,
    @ColumnInfo(name = "journalist_count") val journalist_count: Int,
    @ColumnInfo(name = "lwin") val lwin: Long,
    @ColumnInfo(name = "lwin_11") val lwin_11: Long
)
