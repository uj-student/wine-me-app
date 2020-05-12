package com.student.wine_me_up.wine_recommendation

import com.student.wine_me_up.models.WineModel
import kotlin.math.sqrt


class WineRecommendFactory(private var wineList: List<WineModel>) {

    private val displayLimit = 20
    fun contentBasedFiltering(userWinePreferences: Set<String>): List<WineModel> {
        val wineList = filterWineList(userWinePreferences)
        val wineAndCosine = LinkedHashMap<List<WineModel>, Float>()
        val returnObject = mutableSetOf<WineModel>()


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

    //    preferences => [wine_type, color, classification, appellation]
    //    get list that meets the basic criteria
    private fun filterWineList(userWinePreferences: Set<String>): Set<WineModel> {
        val returnObject = mutableListOf<WineModel>()

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
    private fun cosineSimilarity(firstWine: WineModel, secondWine: WineModel): Float {
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

    fun collaborativeFiltering(){

    }
}
