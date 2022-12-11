package chargeit.main_screen.ui.filters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import chargeit.main_screen.R
import chargeit.main_screen.domain.messages.FiltersMessage
import chargeit.main_screen.domain.ChargeFilter
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.materialswitch.MaterialSwitch

class FiltersFragmentAdapter(
    private val onSwitchChecked: ((position: Int, adapter: FiltersFragmentAdapter, isChecked: Boolean) -> Unit)? = null
) : RecyclerView.Adapter<FiltersFragmentAdapter.MainViewHolder>() {

    private var chargeFilters = FiltersMessage.ChargeFilters(listOf())

    fun setFilters(data: FiltersMessage.ChargeFilters) {
        chargeFilters = data
        notifyDataSetChanged()
    }

    fun switchFilter(position: Int, isChecked: Boolean) {
        chargeFilters.filters[position].isChecked = isChecked
    }

    fun switchAllOff() {
        chargeFilters.filters.forEach { filter ->
            filter.isChecked = false
        }
        notifyDataSetChanged()
    }

    fun getFilters() = chargeFilters

    inner class MainViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val icon = itemView.findViewById<ShapeableImageView>(R.id.item_image)
        private val title = itemView.findViewById<TextView>(R.id.item_name)
        private val switch = itemView.findViewById<MaterialSwitch>(R.id.item_state)

        init {
            switch.setOnCheckedChangeListener { _, isChecked ->
                onSwitchChecked?.invoke(adapterPosition, this@FiltersFragmentAdapter, isChecked)
            }
        }

        fun bind(filter: ChargeFilter) {
            icon.setImageDrawable(icon.context.getDrawable(filter.icon))
            title.text = filter.title
            switch.isChecked = filter.isChecked
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        MainViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.filter_list_item,
                parent, false
            ) as View
        )

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        holder.bind(chargeFilters.filters[position])
    }

    override fun getItemCount() = chargeFilters.filters.size

}