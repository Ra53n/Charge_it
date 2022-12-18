package chargeit.profilescreen.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import chargeit.data.domain.model.Socket
import chargeit.profilescreen.R
import com.google.android.material.checkbox.MaterialCheckBox
import com.google.android.material.textview.MaterialTextView

class SocketSelectionAdapter(
    private val saveSocketCallback: (List<Socket>) -> Unit
) : RecyclerView.Adapter<SocketSelectionAdapter.SocketSelectionViewHolder>() {

    private var socketList: MutableList<SocketItem> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SocketSelectionViewHolder {
        return SocketSelectionViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.socket_selection_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: SocketSelectionViewHolder, position: Int) {
        holder.bind(socketList[position])
        holder.setIsRecyclable(false)
    }

    fun setItems(list: List<SocketItem>) {
        socketList = list.toMutableList()
        notifyDataSetChanged()
    }

    fun filterItems(list: List<SocketItem>) {
        for (item in socketList) {
            list.find { it.socket.id == item.socket.id }?.isSelected = item.isSelected
        }
        socketList = list.toMutableList()
        notifyDataSetChanged()
    }

    fun saveSockets() {
        saveSocketCallback.invoke(socketList.filter { it.isSelected }.map { it.socket })
    }

    fun unselectSockets() {
        socketList.forEach { it.isSelected = false }
        notifyDataSetChanged()
    }

    override fun getItemCount() = socketList.size

    inner class SocketSelectionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(socketItem: SocketItem) {
            with(socketItem) {
                itemView.findViewById<MaterialTextView>(R.id.textView).text = socket.title
                itemView.findViewById<AppCompatImageView>(R.id.image).setImageResource(socket.icon)
                itemView.findViewById<MaterialCheckBox>(R.id.checkbox).isChecked =
                    socketItem.isSelected

                itemView.findViewById<MaterialCheckBox>(R.id.checkbox)
                    .setOnCheckedChangeListener { _, isChecked ->
                        socketItem.isSelected = isChecked
                    }
            }

        }
    }
}