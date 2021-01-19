package br.com.southsystem.eventsapp.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import br.com.southsystem.eventsapp.R
import br.com.southsystem.eventsapp.databinding.ActivityFormEventBinding
import br.com.southsystem.eventsapp.model.database.AppDatabase
import br.com.southsystem.eventsapp.remotedatasource.dto.CheckInDto
import br.com.southsystem.eventsapp.repository.EventRepository
import br.com.southsystem.eventsapp.ui.activity.extensions.mostraErro
import br.com.southsystem.eventsapp.ui.viewmodel.FormEventViewModel
import br.com.southsystem.eventsapp.ui.viewmodel.factory.FormEventViewModelFactory

private const val TITULO_APPBAR = "Check-in"
private const val MENSAGEM_ERRO_CHECKIN = "Não foi possível efetuar check-in"

class FormEventActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFormEventBinding

    private val eventId: String by lazy {
        intent.getStringExtra(EVENT_ID_KEY)
    }

    private val viewModel by lazy {
        val repository = EventRepository(AppDatabase.getInstance(this).eventDao())
        val factory = FormEventViewModelFactory(repository)
        ViewModelProviders.of(this, factory).get(FormEventViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormEventBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        title = TITULO_APPBAR
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.form_event_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.form_event_confirm -> {
                val name = binding.activityFormEventName.text.toString()
                val email = binding.activityFormEventEmail.text.toString()
                checkIn(CheckInDto(eventId, name, email))
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun checkIn(checkInDto: CheckInDto) {
        viewModel.checkIn(checkInDto).observe(this, Observer {
            if (it.erro == null) {
                finish()
            } else {
                mostraErro(MENSAGEM_ERRO_CHECKIN)
            }
        })
    }
}