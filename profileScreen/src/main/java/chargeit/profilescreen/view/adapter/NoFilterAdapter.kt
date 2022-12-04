package chargeit.profilescreen.view.adapter

import android.content.Context
import android.widget.ArrayAdapter
import android.widget.Filter
import androidx.annotation.LayoutRes

class NoFilterAdapter(
    context: Context,
    @LayoutRes resource: Int,
    val items: Array<String>
) : ArrayAdapter<String>(context, resource, items) {

    override fun getFilter(): Filter {
        return DisabledFilter()
    }

    private inner class DisabledFilter : Filter() {
        override fun performFiltering(arg0: CharSequence): FilterResults {
            val result = FilterResults()
            result.values = items
            result.count = items.size
            return result
        }

        override fun publishResults(arg0: CharSequence, arg1: FilterResults) {
            notifyDataSetChanged()
        }
    }

}