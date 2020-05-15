package com.student.wine_me_up.models

import java.io.Serializable

data class WineModel(
    val wine: String,
    val wine_id: Int,
    val wine_slug: String,
    val appellation: String,
    val appellation_slug: String,
    val color: String,
    val wine_type: String,
    val regions: List<String>,
    val country: String,
    var classification: String? = null,
    val vintage: String,
    val date: String,
    val is_primeurs: Boolean,
    val score: Double,
    val confidence_index: String,
    val journalist_count: Int,
    val lwin: Long,
    val lwin_11: Long
):Serializable