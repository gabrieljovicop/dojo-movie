package com.example.dojomovie

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

data class TransactionItem(val title: String, val price: Int, val quantity: Int)

class TransactionAdapter(private val transactionList: List<TransactionItem>) :
    RecyclerView.Adapter<TransactionAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtTitle: TextView = view.findViewById(R.id.txtItemTitle)
        val txtPrice: TextView = view.findViewById(R.id.txtItemPrice)
        val txtQty: TextView = view.findViewById(R.id.txtItemQty)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_transaction, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = transactionList[position]
        val unitPrice = item.price
        val quantity = item.quantity
        val totalPrice = unitPrice * quantity

        holder.txtTitle.text = item.title
        holder.txtQty.text = "${quantity}x Rp $unitPrice"
        holder.txtPrice.text = "Rp $totalPrice"
    }

    override fun getItemCount(): Int = transactionList.size
}