package com.example.associacao_jardim_eucaliptos

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.*

class ManageNewsFragment : Fragment() {
    private lateinit var fab: FloatingActionButton
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchView: SearchView
    private lateinit var databaseReference: DatabaseReference
    private lateinit var eventListener: ValueEventListener
    private lateinit var dataList: MutableList<DataClass>
    private lateinit var adapter: ManageNewsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_manage_news, container, false)
        (activity as? MainActivity)?.hideToolbarAndBottomNavigation()

        recyclerView = view.findViewById(R.id.recyclerView)
        fab = view.findViewById(R.id.fab)
        searchView = view.findViewById(R.id.search)
        searchView.clearFocus()

        val gridLayoutManager = GridLayoutManager(requireContext(), 1)
        recyclerView.layoutManager = gridLayoutManager

        val builder = AlertDialog.Builder(requireContext())
        builder.setCancelable(false)
        builder.setView(R.layout.progress_layout)
        val dialog = builder.create()
        dialog.show()

        dataList = ArrayList()
        adapter = ManageNewsAdapter(requireContext(), dataList)
        recyclerView.adapter = adapter

        databaseReference = FirebaseDatabase.getInstance().getReference("news")
        dialog.show()
        eventListener = databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                dataList.clear()
                for (itemSnapshot in snapshot.children) {
                    val dataClass = itemSnapshot.getValue(DataClass::class.java)
                    dataClass?.let {
                        it.key = itemSnapshot.key
                        dataList.add(it)
                    }
                }
                adapter.notifyDataSetChanged()
                dialog.dismiss()
            }

            override fun onCancelled(error: DatabaseError) {
                dialog.dismiss()
            }
        })

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let { searchList(it) }
                return true
            }
        })

        fab.setOnClickListener {
            val intent = Intent(requireContext(), UploadNewsActivity::class.java)
            startActivity(intent)
        }

        return view
    }

    private fun searchList(text: String) {
        val searchList = ArrayList<DataClass>()
        for (dataClass in dataList) {
            if (dataClass.dataTitle.toLowerCase().contains(text.toLowerCase())) {
                searchList.add(dataClass)
            }
        }
        adapter.searchDataList(searchList)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Hide the action bar and bottom navigation
        (activity as? MainActivity)?.hideToolbarAndBottomNavigation()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Show the action bar and bottom navigation
        (activity as? MainActivity)?.showToolbarAndBottomNavigation()
    }

    override fun onDestroy() {
        super.onDestroy()
        databaseReference.removeEventListener(eventListener)
    }

}
