package chargeit.station_info.presentation.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import chargeit.data.domain.model.Socket
import chargeit.station_info.databinding.FullInfoStationSocketListItemBinding
import chargeit.station_info.presentation.view.utils.OnItemClickListener

class SocketListAdapter (val onItemClickListener: OnItemClickListener) : RecyclerView.Adapter<SocketListAdapter.ViewHolder>() {

    private var data: List<Socket> = arrayListOf()
    private lateinit var binding: FullInfoStationSocketListItemBinding

    fun setData (data: List<Socket>) {
        this.data = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = FullInfoStationSocketListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        fun bind(socket: Socket) {
            with(binding){
                connectorTypeIconImageView.setImageResource(socket.icon)
                connectorTypeNameTextView.setText(socket.title)
                connectorPowerTextView.setText(socket.description)
                root.setOnClickListener{
                    onItemClickListener.onItemClick(it, socket)
                }
            }
        }
    }

}