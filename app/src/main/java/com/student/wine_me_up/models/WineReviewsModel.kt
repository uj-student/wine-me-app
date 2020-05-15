package com.student.wine_me_up.models

data class WineReviewsModel (
    val points: String?,
    val title: String?,
    val description: String?,
    val taster_name: String?,
    val taster_twitter_handle: String?,
    val price: Int?,
    val designation: String?,
    val variety: String?,
    val region_1: String?,
    val region_2: String?,
    val province: String?,
    val country: String?,
    val winery: String?
)