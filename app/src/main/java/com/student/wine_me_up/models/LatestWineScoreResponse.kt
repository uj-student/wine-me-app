package com.student.wine_me_up.models


data class LatestWineScoreResponse(
    val count: Int,
    val results: List<WineEntries>
)