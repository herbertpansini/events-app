package br.com.southsystem.eventsapp.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import br.com.southsystem.eventsapp.model.dao.EventDao
import br.com.southsystem.eventsapp.model.entity.Event
import br.com.southsystem.eventsapp.remotedatasource.dto.CheckInDto
import br.com.southsystem.eventsapp.remotedatasource.webclient.EventWebClient

class EventRepository(private val eventDao: EventDao,
                      private val eventWebclient: EventWebClient = EventWebClient()) {

    private val mediador = MediatorLiveData<Resource<List<Event>?>>()
    private val mediatorLiveData = MediatorLiveData<Resource<Event?>>()

    fun buscaTodos(): LiveData<Resource<List<Event>?>> {
        mediador.addSource(buscaInterno()) { eventsEncontrados ->
            mediador.value = Resource(dado = eventsEncontrados)
        }

        val falhasDaWebApiLiveData = MutableLiveData<Resource<List<Event>?>>()
            mediador.addSource(falhasDaWebApiLiveData) {resourceDeFalha ->
            val resourceAtual = mediador.value
            val resourceNovo: Resource<List<Event>?> = if(resourceAtual != null) {
                Resource(dado = resourceAtual.dado, erro = resourceDeFalha.erro)
            } else {
                resourceDeFalha
            }
            mediador.value = resourceNovo
        }

        buscaNaApi(
            quandoFalha = { erro ->
                falhasDaWebApiLiveData.value = Resource(dado = null, erro = erro)
            })
        return mediador
    }

    private fun buscaNaApi(
            quandoFalha: (erro: String?) -> Unit
    ) {
        eventWebclient.buscaTodas(
                quandoSucesso = { eventsNovos ->
                    eventsNovos?.let {
                        it
                    }
                }, quandoFalha = quandoFalha
        )
    }

    fun buscaPorId(
        eventId: String
    ): LiveData<Resource<Event?>> {
        mediatorLiveData.addSource(buscaPorIdInterno(eventId)) { eventEncontrado ->
            mediatorLiveData.value = Resource(dado = eventEncontrado)
        }

        val falhasDaWebApiLiveData = MutableLiveData<Resource<Event?>>()
            mediatorLiveData.addSource(falhasDaWebApiLiveData) {resourceDeFalha ->
            val resourceAtual = mediatorLiveData.value
            val resourceNovo: Resource<Event?> = if(resourceAtual != null) {
                Resource(dado = resourceAtual.dado, erro = resourceDeFalha.erro)
            } else {
                resourceDeFalha
            }
                mediatorLiveData.value = resourceNovo
        }

        buscaPorIdNaApi(
                eventId,
                quandoFalha = { erro ->
                    falhasDaWebApiLiveData.value = Resource(dado = null, erro = erro)
                })

        return mediatorLiveData
    }
        
    private fun buscaPorIdNaApi(
            eventId: String,
            quandoFalha: (erro: String?) -> Unit
    ) {
        eventWebclient.buscaPorId(
                eventId,
                quandoSucesso = { eventEncontrado ->
                    eventEncontrado?.let {
                        it
                    }
                }, quandoFalha = quandoFalha
        )
    }
    

    private fun buscaInterno() : LiveData<List<Event>> {
        return eventDao.buscaTodos()
    }

    private fun buscaPorIdInterno(eventId: String) : LiveData<Event> {
        return eventDao.buscaPorId(eventId)
    }

    private fun salvaInterno(
        events: List<Event>
    ) {
        BaseAsyncTask(
            quandoExecuta = {
                eventDao.salva(events)
            }, quandoFinaliza = {}
        ).execute()
    }

    fun doCheckIn(checkInDto: CheckInDto): LiveData<Resource<Void?>> {
        val liveData = MutableLiveData<Resource<Void?>>()
        checkIn(checkInDto, quandoSucesso = {
            liveData.value = Resource(null)
        }, quandoFalha = { erro ->
            liveData.value = Resource(dado = null, erro = erro)
        })
        return liveData
    }

    private fun checkIn(
            checkInDto: CheckInDto,
            quandoSucesso: (void: Void?) -> Unit,
            quandoFalha: (erro: String?) -> Unit
    ) {
        eventWebclient.checkIn(
                checkInDto,
                quandoSucesso = quandoSucesso,
                quandoFalha = quandoFalha
        )
    }
}