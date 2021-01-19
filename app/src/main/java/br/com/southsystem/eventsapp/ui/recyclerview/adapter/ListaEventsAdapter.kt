package br.com.southsystem.eventsapp.ui.recyclerview.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.com.southsystem.eventsapp.R
import br.com.southsystem.eventsapp.model.entity.Event
import com.squareup.picasso.Picasso

class ListaEventsAdapter(private val context: Context,
                         private val events: MutableList<Event> = mutableListOf(),
                                 var quandoItemClicado: (event: Event) -> Unit = {}
) : RecyclerView.Adapter<ListaEventsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val viewCriada = LayoutInflater.from(context)
            .inflate(
                R.layout.item_event,
                parent, false
            )
        return ViewHolder(viewCriada)
    }

    override fun getItemCount() = events.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val event = events[position]
        holder.vincula(event)
    }

    fun atualiza(events: List<Event>) {
        notifyItemRangeRemoved(0, this.events.size)
        this.events.clear()
        this.events.addAll(events)
        notifyItemRangeInserted(0, this.events.size)
    }

    inner class ViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        private lateinit var event: Event
        val itemEventImage: ImageView
        val itemEventTitle: TextView
        //val itemEventDescription: TextView

        init {
            itemEventImage = itemView.findViewById(R.id.item_event_image)
            itemEventTitle = itemView.findViewById(R.id.item_event_title)
            //itemEventDescription = itemView.findViewById(R.id.item_event_description)

            itemView.setOnClickListener {
                if (::event.isInitialized) {
                    quandoItemClicado(event)
                }
            }
        }

        fun vincula(event: Event) {
            this.event = event
            Picasso.get().load(event.image).into(itemEventImage);
            itemEventTitle.text = event.title
            //itemEventDescription.text = event.description
        }
    }
}