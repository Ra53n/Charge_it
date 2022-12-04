package chargeit.station_info.ui.full

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import chargeit.core.view.CoreFragment
import chargeit.station_info.R


class FullStationInfoFragment : CoreFragment(R.layout.fragment_full_station_info) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_full_station_info, container, false)
    }

/*    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity?)!!.supportActionBar!!.setDisplayHomeAsUpEnabled(false)
    }

    override fun onStop() {
        super.onStop()
        (activity as AppCompatActivity?)!!.supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }*/

    companion object {
        const val TAG = "FullStationInfoFragment"
        const val INFO_EXTRA1 = "Station info"

/*        @JvmStatic
        fun newInstance() =
            FullStationInfoFragment().apply {
                arguments = Bundle().apply {
                }
            }*/
    }
}