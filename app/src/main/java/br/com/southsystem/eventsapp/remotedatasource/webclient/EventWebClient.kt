package br.com.southsystem.eventsapp.remotedatasource.webclient

import br.com.southsystem.eventsapp.model.entity.Event
import br.com.southsystem.eventsapp.remotedatasource.AppRetrofit
import br.com.southsystem.eventsapp.remotedatasource.dto.CheckInDto
import br.com.southsystem.eventsapp.remotedatasource.service.EventService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val REQUISICAO_NAO_SUCEDIDA = "Requisição não sucedida"

class EventWebClient(private val service: EventService = AppRetrofit().eventService) {
    private fun <T> executaRequisicao(
        call: Call<T>,
        quandoSucesso: (eventsNovos: T?) -> Unit,
        quandoFalha: (erro: String?) -> Unit
    ) {
        call.enqueue(object : Callback<T> {
            override fun onResponse(call: Call<T>, response: Response<T>) {
                if (response.isSuccessful) {
                    quandoSucesso(response.body())
                } else {
                    quandoFalha(REQUISICAO_NAO_SUCEDIDA)
                }
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                quandoFalha(t.message)
            }
        })
    }

    fun buscaTodas(
        quandoSucesso: (eventsNew: List<Event>?) -> Unit,
        quandoFalha: (erro: String?) -> Unit
    ) {
        executaRequisicao(
            service.buscaTodas(),
            quandoSucesso,
            quandoFalha
        )
    }

    fun buscaPorId(
            eventId: String,
            quandoSucesso: (event: Event?) -> Unit,
            quandoFalha: (erro: String?) -> Unit
    ) {
        executaRequisicao(
                service.buscaPorId(eventId),
                quandoSucesso,
                quandoFalha
        )
    }

    fun checkIn(
        checkInDto: CheckInDto,
        quandoSucesso: (void: Void?) -> Unit,
        quandoFalha: (erro: String?) -> Unit
    ) {
        executaRequisicao(
                service.checkIn(checkInDto),
                quandoSucesso,
                quandoFalha
        )
    }
}