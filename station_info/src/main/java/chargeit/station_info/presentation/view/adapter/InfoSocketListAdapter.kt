package chargeit.station_info.presentation.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import chargeit.data.domain.model.Socket
import chargeit.station_info.databinding.StationConnectorsListItemBinding

class InfoSocketListAdapter : RecyclerView.Adapter<InfoSocketListAdapter.ViewHolder>() {

    private var data: List<Socket> = arrayListOf()
    private lateinit var binding: StationConnectorsListItemBinding

    fun setData(data: List<Socket>) {
        this.data = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = StationConnectorsListItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(socket: Socket) {
            with(binding) {
                connectorTypeIconImageView.setImageResource(socket.icon)
                connectorTypeNameTextView.setText(socket.title)
            }
        }
    }

    private fun getImageRes(id: Int) = when (id) {
        0 -> chargeit.core.R.drawable.type_1_j1772
        1 -> chargeit.core.R.drawable.type_2_mannekes
        else -> chargeit.core.R.drawable.type_1_j1772
    }
}