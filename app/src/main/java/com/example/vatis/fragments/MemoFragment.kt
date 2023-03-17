package com.example.vatis.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vatis.CellClickListener
import com.example.vatis.adapters.MemoItemAdapter
import com.example.vatis.items.MemoSubItem
import com.example.vatis.R
import com.example.vatis.items.MemoItem
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_memo.view.*



class MemoFragment(private val fileRef: DocumentReference) : Fragment(), CellClickListener {
    companion object {
        lateinit var planRef: CollectionReference
        lateinit var planQuery: Query
        var memoItemList = ArrayList<MemoItem>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        planRef = fileRef.collection("plan")
        planQuery = planRef.orderBy("order.day")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_memo, container, false)
        subscribeToRealTimeUpdates()
        return view
    }

    // build memo list from multiple memoSubLists
    private fun buildMemoList(queryMemoItemList: ArrayList<MemoSubItem>): ArrayList<MemoItem> {
        val memoItemList = ArrayList<MemoItem>()
        val dayMemoMap = queryMemoItemList.sortedWith(
            compareBy( { it.order.first}, {it.order.second} )
        ).groupBy {
            it.order.first
        }

        for ((day, memoSubItemList) in dayMemoMap) {
            memoItemList.add(MemoItem("Day $day", memoSubItemList as ArrayList<MemoSubItem>))
        }
        return memoItemList
    }

    private fun subscribeToRealTimeUpdates() {
        planQuery.addSnapshotListener { querySnapShot, firebaseFirestoreException ->
            firebaseFirestoreException?.let {
                Toast.makeText(this.context, it.message, Toast.LENGTH_LONG).show()
                return@addSnapshotListener
            }
            querySnapShot?.let {
                val queryMemoItemList = ArrayList<MemoSubItem>()
                for (document in it) {
                    val data = document.data
                    val spotName = data["name"] as String
                    val memo = data["memo"] as String
                    val order = data["order"] as Map<*, *>
                    val dayPosPair = Pair(order["day"], order["position"])

                    // add to memoSubItemList here
                    queryMemoItemList.add(MemoSubItem(spotName, memo, dayPosPair as Pair<Long, Long>))
                }
                memoItemList = buildMemoList(queryMemoItemList)
            }

            view?.memo_item_recyclerview?.layoutManager = LinearLayoutManager(activity)
            view?.memo_item_recyclerview?.adapter = MemoItemAdapter(memoItemList, this)
        }
    }

    override fun onCellClickListener(data: MemoSubItem) {
        val editDialogFragment = MemoEditDialogFragment(data, planRef)
        editDialogFragment.show(childFragmentManager, "MemoEditDialog")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }


}