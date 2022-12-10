package chargeit.profilescreen.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import chargeit.data.domain.model.Socket
import chargeit.profilescreen.R

class SocketSelectionAdapter(private val socketList: List<Socket>) :
    RecyclerView.Adapter<SocketSelectionAdapter.SocketSelectionViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SocketSelectionViewHolder {
        return SocketSelectionViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.socket_selection_item, parent)
        )
    }

    override fun onBindViewHolder(holder: SocketSelectionViewHolder, position: Int) {
        holder.bind(socketList[position])
    }

    override fun getItemCount() = socketList.size

    inner class SocketSelectionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(socket: Socket) {

        }
    }
}