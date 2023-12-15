package com.tienbx.diary.navigation

import com.tienbx.diary.util.Constants

sealed class Screen(val route: String) {
    object Authentication: Screen(route = "authentication_screen")
    object Home: Screen(route = "home_screen")
    object Write: Screen(route = "write_screen?${Constants.WRITE_SCREEN_ARG_KEY}={${Constants.WRITE_SCREEN_ARG_KEY}}") {
        fun passDiaryId(diaryId: String) =
            "write_screen?${Constants.WRITE_SCREEN_ARG_KEY}=$diaryId"
    }
}
