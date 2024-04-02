package com.example.masterdetaildmt.navigation
enum class View{
    HOME,
    DETAILS
}
sealed class NavigationItem(val route : String) {
     object HomeView : NavigationItem(View.HOME.name)
     object DetailsView : NavigationItem(View.DETAILS.name)
}