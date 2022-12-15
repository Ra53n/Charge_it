package chargeit.station_info.presentation.view.adapter

import android.content.res.Resources
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import chargeit.data.domain.model.SocketEntity
import chargeit.station_info.databinding.StationConnectorsListItemBinding

class InfoSocketListAdapter : RecyclerView.Adapter<InfoSocketListAdapter.ViewHolder>() {

    private var data: List<SocketEntity> = arrayListOf()
    private lateinit var binding: StationConnectorsListItemBinding

    fun setData(data: List<SocketEntity>) {
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
        holder.bind(data[position], holder.itemView.resources)
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(socket: SocketEntity, resources: Resources) {
            with(binding) {
                connectorTypeIconImageView.setImageResource(socket.socket.icon)
                connectorTypeNameTextView.text = socket.socket.title

                Log.d("VVV", socket.status.toString())
                if (socket.status) {
                    socketStatusTextView.text = resources.getString(chargeit.core.R.string.free_socket_status_text)
                    connectorStatusConstraintLayout.background = resources.getDrawable(chargeit.core.R.drawable.free_connector_layout_shape)
                } else {
                    socketStatusTextView.text = resources.getString(chargeit.core.R.string.busy_socket_status_text)
                    connectorStatusConstraintLayout.background = resources.getDrawable(chargeit.core.R.drawable.busy_connector_layout_shape)
                }
            }
        }
    }
}