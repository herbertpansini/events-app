package br.com.southsystem.eventsapp.ui.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import br.com.southsystem.eventsapp.R
import br.com.southsystem.eventsapp.model.database.AppDatabase
import br.com.southsystem.eventsapp.model.entity.Event
import br.com.southsystem.eventsapp.repository.EventRepository
import br.com.southsystem.eventsapp.ui.activity.extensions.mostraErro
import br.com.southsystem.eventsapp.ui.viewmodel.VisualizaEventViewModel
import br.com.southsystem.eventsapp.ui.viewmodel.factory.VisualizaEventViewModelFactory
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import com.squareup.picasso.Picasso
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

private const val EVENTO_NAO_ENCONTRADO = "Evento não encontrado"
private const val TITULO_APPBAR = ""

class VisualizaEventActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var activityVisualizaEventImage: ImageView
    private lateinit var activityVisualizaEventTitle: TextView
    private lateinit var activityVisualizaEventDescription : TextView
    private lateinit var activityVisualizaEventDate : TextView

    private lateinit var mMap: GoogleMap
    private val mapFragment by lazy { supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment }

    private val eventId: String by lazy {
        intent.getStringExtra(EVENT_ID_KEY)
    }

    private val viewModel by lazy {
        val repository = EventRepository(AppDatabase.getInstance(this).eventDao())
        val factory = VisualizaEventViewModelFactory(eventId, repository)
        ViewModelProviders.of(this, factory).get(VisualizaEventViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_visualiza_event)
        setSupportActionBar(findViewById(R.id.toolbar))
        title = TITULO_APPBAR
        findViewById()
        verificaIdDoEvento()
        buscaEventoSelecionado()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val latitude = viewModel.eventEncontrado?.value?.dado?.latitude?.toDoubleOrNull()
        val longitude = viewModel.eventEncontrado?.value?.dado?.longitude?.toDoubleOrNull()

        if (latitude != null && longitude != null) {
            val local = LatLng(latitude, longitude)
            mMap.addMarker(MarkerOptions().position(local).title(viewModel.eventEncontrado?.value?.dado?.title))
            mMap.moveCamera(CameraUpdateFactory.newLatLng(local))
        }
    }

    private fun findViewById() {
        activityVisualizaEventImage = findViewById(R.id.appBarImg)
        activityVisualizaEventTitle = findViewById(R.id.content_form_event_title)
        activityVisualizaEventDescription = findViewById(R.id.content_form_event_description)
        activityVisualizaEventDate = findViewById(R.id.content_form_event_date)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.visualiza_event_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.visualiza_event_menu_share -> doSharing(activityVisualizaEventTitle.text.toString(),
                                                         activityVisualizaEventDescription.text.toString(),
                                                 this)
            R.id.visualiza_event_menu_check -> doCheckIn()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun doCheckIn() {
        val intent = Intent(this, FormEventActivity::class.java)
        intent.putExtra(EVENT_ID_KEY, eventId)
        startActivity(intent)
    }

    private fun doSharing(title: String?, description: String?, context: Context) {
        val shareIntent = Intent(Intent.ACTION_SEND)

        shareIntent.type = "text/plain"

        shareIntent.putExtra(
                Intent.EXTRA_SUBJECT,
                title
        )

        shareIntent.putExtra(
                Intent.EXTRA_TEXT,
                description
        )

        context.startActivity(
                Intent.createChooser(
                        shareIntent,
                        context.getString(R.string.share_with)
                )
        )
    }

    private fun buscaEventoSelecionado() {
        viewModel.eventEncontrado.observe(this, Observer { eventEncontrado ->
            eventEncontrado.dado?.let {
                preencheCampos(it)
            }
        })
    }

    private fun verificaIdDoEvento() {
        if (eventId.isNullOrEmpty()) {
            mostraErro(EVENTO_NAO_ENCONTRADO)
            finish()
        }
    }

    private fun preencheCampos(event: Event) {
        Picasso.get().load(event.image).into(activityVisualizaEventImage);
        activityVisualizaEventTitle.text = event.title
        activityVisualizaEventDescription.text = event.description
        activityVisualizaEventDate.text = convertLongToTime(event.date!!)

        mapFragment.getMapAsync(this@VisualizaEventActivity)
    }

    fun convertLongToTime(time: Long): String {
        val date = Date(time)
        val format = SimpleDateFormat("dd/MM/yyyy")
        return format.format(date)
    }
}