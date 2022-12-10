package ru.profitsw2000.socket_info.presentation.view.fragment

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import chargeit.socket_info.R
import ru.profitsw2000.socket_info.presentation.viewmodel.SocketInfoViewModel

class SocketInfoFragment : Fragment() {

    private lateinit var viewModel: SocketInfoViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_socket_info, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(SocketInfoViewModel::class.java)
        // TODO: Use the ViewModel
    }

    companion object {
        fun newInstance() = SocketInfoFragment()
    }

}