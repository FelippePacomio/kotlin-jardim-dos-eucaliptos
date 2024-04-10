package com.example.pi_5_semestre

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class EventsFragment : Fragment() {

    private lateinit var eventsRecyclerView: RecyclerView
    private lateinit var eventsAdapter: EventsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_events, container, false)
        val eventsList = generateEventsItems()

        eventsRecyclerView = rootView.findViewById(R.id.eventsRecyclerView)
        eventsRecyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        eventsRecyclerView.setHasFixedSize(true)

        eventsAdapter = EventsAdapter(eventsList)
        eventsRecyclerView.adapter = eventsAdapter

        return rootView
    }

    private fun generateEventsItems(): List<EventsItem> {
        val eventsItems = mutableListOf<EventsItem>()
        eventsItems.add(
            EventsItem(
                R.drawable.ferias,
                "As férias escolares estão chegando. Dê uma espiada na programação para a criançada no próximo mês de junho!"
            )
        )
        eventsItems.add(
            EventsItem(
                R.drawable.facaparte,
                "Tem vontade de fazer parte da equipe? Junte-se a nós!"
            )
        )
        eventsItems.add(
            EventsItem(
                R.drawable.psocial,
                "Confira nossos próximos projetos sociais na comunidade do Jardim dos Eucaliptos"
            )
        )
        eventsItems.add(
            EventsItem(
                R.drawable.cestabasica,
                "Entrega de Cestas Básicas Maio 2024"
            )
        )
        return eventsItems
    }
}
