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
    private val saveSocketCallback: (List<Socket>) -> Unit,
    private val socketList: List<Socket>
) : RecyclerView.Adapter<SocketSelectionAdapter.SocketSelectionViewHolder>() {

    private val checkedSocketsList: MutableList<Socket> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SocketSelectionViewHolder {
        return SocketSelectionViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.socket_selection_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: SocketSelectionViewHolder, position: Int) {
        holder.bind(socketList[position])
    }

    fun saveSockets() {
        saveSocketCallback.invoke(checkedSocketsList)
    }

    override fun getItemCount() = socketList.size

    inner class SocketSelectionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(socket: Socket) {
            itemView.findViewById<MaterialTextView>(R.id.textView).text = socket.title
            itemView.findViewById<AppCompatImageView>(R.id.image).setImageResource(socket.icon)
            itemView.findViewById<MaterialCheckBox>(R.id.checkbox)
                .setOnCheckedChangeListener { compoundButton, isChecked ->
                    if (isChecked) {
                        checkedSocketsList.add(socket)
                    } else {
                        checkedSocketsList.remove(socket)
                    }
                }
        }
    }
}