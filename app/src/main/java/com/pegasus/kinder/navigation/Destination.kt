package com.pegasus.kinder.navigation

sealed class Destination(val route: String) {
    object Start : Destination("start")
    object LanguageSelection : Destination("language_selection")
    object Questions : Destination("questions")
    object UserInfo : Destination("user_info/{consent1}/{consent2}/{language}")
    object Recording : Destination("recording/{language}/{name}/{age}/{consent1}/{consent2}")
    object PhoneNumber : Destination("phone_number/{language}") {
        fun createPhoneNumberRoute(language: String): String {
            return "phone_number/$language"
        }
    }
    object WhatsApp : Destination("whatsapp/{language}") {
        fun createWhatsAppRoute(language: String): String {
            return "whatsapp/$language"
        }
    }
    // ... other existing destinations

    fun createUserInfoRoute(consent1: Boolean, consent2: Boolean, language: String): String {
        return "user_info/$consent1/$consent2/$language"
    }

    fun createRecordingRoute(language: String, name: String, age: String, consent1: Boolean, consent2: Boolean): String {
        return "recording/$language/$name/$age/$consent1/$consent2"
    }
} 