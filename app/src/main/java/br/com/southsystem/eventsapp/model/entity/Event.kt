package br.com.southsystem.eventsapp.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "events")
data class Event(
        @PrimaryKey val id: String,
        val title: String? = "",
        val date: Long? = null,
        val description: String = "",
        val image: String? = null,
        val price: Double = 0.0,
        val longitude: String? = null,
        val latitude: String? = null,
)