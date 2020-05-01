package com.student.wine_me_up.wine_recommendation

import android.util.Log
import com.student.wine_me_up.models.WineEntries
import java.util.Map.Entry.comparingByValue
import java.util.stream.Collectors
import java.util.stream.Collectors.toMap
import kotlin.math.sqrt


class WineRecommendFactory(private var wineList: List<WineEntries>) {

    private val displayLimit = 10
    fun contentBasedFiltering(userWinePreferences: Set<String>): List<WineEntries> {
        val wineList = filterWineList(userWinePreferences)
        val wineAndCosine = LinkedHashMap<List<WineEntries>, Float>()
        val returnObject = mutableSetOf<WineEntries>()


        for (wine in wineList.indices) {
            for (wine2 in wine + 1 until wineList.size) {
                val winePairs = listOf(wineList.elementAt(wine), wineList.elementAt(wine2))
                wineAndCosine[winePairs] = cosineSimilarity(wineList.elementAt(wine), wineList.elementAt(wine2))
            }
        }

        // sort wineAndCosine in descending order of float value get the top 10
        val sortedByCount = wineAndCosine.toList().sortedByDescending { (_, value) -> value}.toMap()


        var count = 0
        for (i in sortedByCount) {
            returnObject.add(i.key[0])
            returnObject.add(i.key[1])
            count ++
            if (count > displayLimit)
                break
        }
        return returnObject.toList()
    }

    fun collaborativeFiltering(){

    }

    //    preferences => [wine_type, color, classification, appellation]
    //    get list that meets the basic criteria
    private fun filterWineList(userWinePreferences: Set<String>): Set<WineEntries> {
        val returnObject = mutableListOf<WineEntries>()

        for (wine in wineList) {
            if (wine.wine_type in userWinePreferences || wine.color in userWinePreferences
                || wine.classification in userWinePreferences || wine.appellation in userWinePreferences
            ) {
                returnObject.add(wine)
            }
        }
        return returnObject.toSet()
    }

    //    cosine similarity of i and j
    //    = (i.j) / (||i||*||j||)
    private fun cosineSimilarity(firstWine: WineEntries, secondWine: WineEntries): Float {
        var dotProduct = 0
        var cosineBetweenFirstAndSecond = 0F
        val firstWineCharacteristics = firstWine.classification?.let {
            arrayListOf<String>(
                firstWine.wine_type, firstWine.color,
                it, firstWine.appellation
            )
        }

        val secondWineCharacteristics = secondWine.classification?.let {
            arrayListOf<String>(
                secondWine.wine_type, secondWine.color,
                it, secondWine.appellation
            )
        }

        // similar items
        if (!firstWineCharacteristics.isNullOrEmpty() && !secondWineCharacteristics.isNullOrEmpty()) {
            for (category in firstWineCharacteristics) {
                if (category in secondWineCharacteristics) {
                    dotProduct++
                }
            }
            val i = sqrt(firstWineCharacteristics.size.toFloat())
            val j = sqrt(secondWineCharacteristics.size.toFloat())

            cosineBetweenFirstAndSecond = dotProduct / (i * j)
        }

        return cosineBetweenFirstAndSecond

    }
}
