package com.student.wine_me_up

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


import androidx.test.rule.ActivityTestRule


@RunWith(AndroidJUnit4::class)
class UiNavigationTest {

    @get:Rule
    var activityRule: ActivityTestRule<MainActivity> = ActivityTestRule(MainActivity::class.java)


    @Test
    fun navigateToRatingsMenu() {
        onView(withId(R.id.wineRatings)).perform(click())

        Thread.sleep(1000)

        onView(withId(R.id.ratingMessageHeader)).check(matches(withText("Wine Ratings")))

        Thread.sleep(1000)

    }
}