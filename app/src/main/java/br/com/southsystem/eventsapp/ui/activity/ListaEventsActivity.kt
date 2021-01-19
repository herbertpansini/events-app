package br.com.southsystem.eventsapp.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.southsystem.eventsapp.R
import br.com.southsystem.eventsapp.model.database.AppDatabase
import br.com.southsystem.eventsapp.model.entity.Event
import br.com.southsystem.eventsapp.repository.EventRepository
import br.com.southsystem.eventsapp.ui.activity.extensions.mostraErro
import br.com.southsystem.eventsapp.ui.recyclerview.adapter.ListaEventsAdapter
import br.com.southsystem.eventsapp.ui.viewmodel.ListaEventsViewModel
import br.com.southsystem.eventsapp.ui.viewmodel.factory.ListaEventsViewModelFactory

private const val TITULO_APPBAR = "Eventos"
private const val MENSAGEM_FALHA_CARREGAR_NOTICIAS = "Não foi possível carregar os novos eventos"

class ListaEventsActivity : AppCompatActivity() {

    private lateinit var activityListaEventsRecyclerview: RecyclerView

    private val adapter by lazy {
        ListaEventsAdapter(context = this)
    }

    private val viewModel by lazy {
        val repository = EventRepository(AppDatabase.getInstance(this).eventDao())
        val factory = ListaEventsViewModelFactory(repository)
        val provedor = ViewModelProviders.of(this, factory)
        provedor.get(ListaEventsViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_events)
        title = TITULO_APPBAR
        findViewById()
        configuraRecyclerView()
        buscaEventos()
    }

    private fun findViewById() {
        activityListaEventsRecyclerview = findViewById(R.id.activity_lista_events_recyclerview)
    }

    private fun configuraRecyclerView() {
        val divisor = DividerItemDecoration(this, LinearLayoutManager.VERTICAL)
        activityListaEventsRecyclerview.addItemDecoration(divisor)
        activityListaEventsRecyclerview.adapter = adapter
        configuraAdapter()
    }

    private fun configuraAdapter() {
        adapter.quandoItemClicado = this::abreVisualizadorEventos
    }

    private fun buscaEventos() {
        viewModel.buscaTodos().observe(this, Observer { resource ->
            resource.dado?.let { adapter.atualiza(it) }
            resource.erro?.let {
                mostraErro(MENSAGEM_FALHA_CARREGAR_NOTICIAS)
            }
        })
    }

    private fun abreVisualizadorEventos(it: Event) {
        val intent = Intent(this, VisualizaEventActivity::class.java)
        intent.putExtra(EVENT_ID_KEY, it.id)
        startActivity(intent)
    }
}