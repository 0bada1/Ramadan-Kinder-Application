package com.pegasus.kinder.viewmodels

import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.ViewModel

class NavigationViewModel : ViewModel() {
    private val visitedRoutes = mutableStateMapOf<String, Boolean>()

    fun markRouteVisited(route: String) {
        visitedRoutes[route] = true
    }

    fun hasVisitedRoute(route: String): Boolean {
        return visitedRoutes[route] ?: false
    }
} 