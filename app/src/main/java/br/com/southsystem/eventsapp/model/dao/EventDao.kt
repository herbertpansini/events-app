package br.com.southsystem.eventsapp.model.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import br.com.southsystem.eventsapp.model.entity.Event

@Dao
interface EventDao {
    @Query("SELECT * FROM events ORDER BY id DESC")
    fun buscaTodos(): LiveData<List<Event>>

    @Query("SELECT * FROM events WHERE id = :id")
    fun buscaPorId(id: String): LiveData<Event>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun salva(events: List<Event>)

    @Query("DELETE FROM events")
    fun deleteAll()
}