package ru.profitsw2000.socket_info.presentation.view.fragment

import android.graphics.Color
import android.graphics.drawable.Drawable
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import chargeit.data.domain.model.Socket
import chargeit.socket_info.R
import chargeit.socket_info.databinding.FragmentSocketInfoBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.fragment_socket_info.view.*
import ru.profitsw2000.socket_info.presentation.viewmodel.SocketInfoViewModel

class SocketInfoFragment : Fragment() {

    private var _binding: FragmentSocketInfoBinding? = null
    private val binding get() = _binding!!
    private var socket: Socket? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSocketInfoBinding.bind(
            inflater.inflate(
                R.layout.fragment_socket_info,
                container,
                false
            )
        )
        arguments?.let {
            socket = it.getParcelable(SOCKET_EXTRA)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        socket?.let {
            with(binding) {
                currentStatusTextView.text = resources.getText(chargeit.core.R.string.free_socket_status_text)
                currentStatusTextView.setTextColor(resources.getColor(chargeit.core.R.color.green_A200))
                (activity as AppCompatActivity?)!!.supportActionBar!!.title =
                    requireContext().getString(
                        chargeit.core.R.string.socket_description_text,
                        it.title,
                        it.description
                    )
            }
        }

        binding.reserveFieldTextView.setOnClickListener {
            if (binding.currentStatusTextView.text == resources.getText(chargeit.core.R.string.free_socket_status_text)) {
                showDialog(socket!!,
                    resources.getString(chargeit.core.R.string.start_charging_dialog_text),
                    resources.getString(chargeit.core.R.string.busy_socket_status_text),
                    resources.getColor(chargeit.core.R.color.red_A200),
                    resources.getString(chargeit.core.R.string.release_socket_now_text)
                )
            } else {
                showDialog(socket!!,
                    resources.getString(chargeit.core.R.string.stop_charging_dialog_text),
                    resources.getString(chargeit.core.R.string.free_socket_status_text),
                    resources.getColor(chargeit.core.R.color.green_A200),
                    resources.getString(chargeit.core.R.string.set_busy_socket_status_text)
                )
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showDialog(
        socket: Socket,
        message: String,
        status: String,
        color: Int,
        newText: String
    ) {
        val builder = MaterialAlertDialogBuilder(requireContext())

        with(builder) {
            setTitle(
                requireContext().getString(
                    chargeit.core.R.string.socket_description_text,
                    socket.title,
                    socket.description
                )
            )
            setMessage(message)
            setPositiveButton(getString(chargeit.core.R.string.ok_button_text)) { _, _ ->
                with(binding) {
                    currentStatusTextView.text = status
                    currentStatusTextView.setTextColor(color)
                    reserveFieldTextView.setText(newText)
                }
            }
            setNegativeButton(getString(chargeit.core.R.string.cancel_button_text)) { _, _ -> }
            show()
        }
    }

    companion object {
        const val SOCKET_EXTRA = "Socket"

        fun newInstance() = SocketInfoFragment()
    }

}