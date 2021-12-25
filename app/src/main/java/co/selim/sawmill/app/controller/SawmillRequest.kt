package co.selim.sawmill.app.controller

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class SawmillRequest(
    val name: String,
    val city: String,
    val country: String,
)
