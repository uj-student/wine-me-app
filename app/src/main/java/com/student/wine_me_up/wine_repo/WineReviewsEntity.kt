package com.student.wine_me_up.wine_repo

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class WineReviewsEntity (
    @PrimaryKey(autoGenerate = true)val id: Int,
    @ColumnInfo(name = "points") val points: String?,
    @ColumnInfo(name = "title") val title: String?,
    @ColumnInfo(name = "description") val description: String?,
    @ColumnInfo(name = "taster_name") val taster_name: String?,
    @ColumnInfo(name = "taster_twitter_handle") val taster_twitter_handle: String?,
    @ColumnInfo(name = "price") val price: Int?,
    @ColumnInfo(name = "designation") val designation: String?,
    @ColumnInfo(name = "variety") val variety: String?,
    @ColumnInfo(name = "region_1") val region_1: String?,
    @ColumnInfo(name = "region_2") val region_2: String?,
    @ColumnInfo(name = "province") val province: String?,
    @ColumnInfo(name = "country") val country: String?,
    @ColumnInfo(name = "winery") val winery: String?
    )