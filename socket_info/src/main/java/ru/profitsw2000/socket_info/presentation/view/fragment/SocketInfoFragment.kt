package ru.profitsw2000.socket_info.presentation.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import chargeit.data.domain.model.ElectricStationEntity
import chargeit.data.domain.model.SocketEntity
import chargeit.data.domain.model.State
import chargeit.socket_info.R
import chargeit.socket_info.databinding.FragmentSocketInfoBinding
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import kotlinx.android.synthetic.main.duration_picker_dialog.view.*
import kotlinx.android.synthetic.main.fragment_socket_info.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.profitsw2000.socket_info.presentation.viewmodel.SocketInfoViewModel
import java.text.SimpleDateFormat
import java.util.*

class SocketInfoFragment : Fragment() {

    private var _binding: FragmentSocketInfoBinding? = null
    private val binding get() = _binding!!
    private val socketInfoViewModel: SocketInfoViewModel by viewModel()
    private var socket: SocketEntity? = null
    private var electricStationEntity: ElectricStationEntity? = null
    private var socketId: Int = 0
    private var id: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentSocketInfoBinding.bind(
            inflater.inflate(
                R.layout.fragment_socket_info,
                container,
                false
            )
        )
        arguments?.let {
            id = it.getInt(INFO_EXTRA)
            socketId = it.getInt(SOCKET_EXTRA)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        id?.let {
            socketInfoViewModel.getElectricStationInfo(it)
        }

        observeData()

        initViews()
    }

    private fun initViews() {
        binding.reserveFieldTextView.setOnClickListener {
            socket?.let {
                if (it.status){
                    showDialog(it, resources.getString(chargeit.core.R.string.start_charging_dialog_text))
                } else {
                    showDialog(it, resources.getString(chargeit.core.R.string.stop_charging_dialog_text))
                }
            }
        }

        binding.pickDateTextView.setOnClickListener {
            val today = MaterialDatePicker.todayInUtcMilliseconds()
            val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
            calendar.timeInMillis = today
            val startDate = calendar.timeInMillis

            val upWeek = today + 1000*60*60*24*7
            calendar.timeInMillis = upWeek
            val endDate = calendar.timeInMillis

            val constraints: CalendarConstraints = CalendarConstraints.Builder()
                .setOpenAt(startDate)
                .setStart(startDate)
                .setEnd(endDate)
                .build()

            val datePicker = MaterialDatePicker
                .Builder
                .datePicker()
                .setTitleText("Выберите дату")
                .setCalendarConstraints(constraints)
                .build()

            datePicker
                .show(childFragmentManager, "DATE_PICKER")

            datePicker.addOnPositiveButtonClickListener {
                val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                val date = dateFormat.format(it)
                binding.pickDateTextView.text = date
            }

        }

        binding.pickTimeTextView.setOnClickListener {
            val timePicker = MaterialTimePicker
                .Builder()
                .setTitleText("Выберите время")
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .build()

            timePicker
                .show(childFragmentManager, "TIME_PICKER")

            timePicker.addOnPositiveButtonClickListener {
                val hourString = timePicker.hour.toString()
                val minuteString = if(timePicker.minute < 10) "0${timePicker.minute}"
                                    else "${timePicker.minute}"

                val timeString = "$hourString:$minuteString"
                binding.pickTimeTextView.text = timeString
            }
        }

        binding.pickDurationTextView.setOnClickListener {
            val builder = MaterialAlertDialogBuilder(requireContext())
            val view: View = layoutInflater.inflate(R.layout.duration_picker_dialog, null)
            view.hour_number_picker.maxValue = 7
            view.hour_number_picker.minValue = 1
            view.minute_number_picker.minValue = 0
            view.minute_number_picker.maxValue = 59


            with(builder) {
                setTitle("Выбор длительности")
                setMessage("Выберите длительность заряда автомобиля (от 1 до 8 часов)")
                setView(view)
                setPositiveButton(getString(chargeit.core.R.string.ok_button_text)) { _, _ ->
                    val hourDuration = view.hour_number_picker.value
                    val minDuration = view.minute_number_picker.value

                    val minString = if (minDuration < 10) "0$minDuration" else "$minDuration"
                    binding.pickDurationTextView.text = "$hourDuration:$minString"
                }
                setNegativeButton(getString(chargeit.core.R.string.cancel_button_text)) { _, _ -> }
                show()
            }
        }

        binding.applyChangesButton.setOnClickListener {
            val builder = MaterialAlertDialogBuilder(requireContext())
            val message = if (reserveFieldsIsValid()) "Забронировать разъём?"
                            else "Не хватает данных для резервироания"

            with(builder) {
                setTitle("Бронирование разъёма")
                setMessage(message)
                setPositiveButton(getString(chargeit.core.R.string.ok_button_text)) { _, _ ->
                    if (reserveFieldsIsValid()) {
                        Toast.makeText(requireContext(),
                                    "Разъём зарезервирован!",
                                        Toast.LENGTH_SHORT).show()
                        socketInfoViewModel.navigateUp()
                    }
                }
                setNegativeButton(getString(chargeit.core.R.string.cancel_button_text)) { _, _ -> }
                show()
            }
        }
    }

    private fun observeData() {
        socketInfoViewModel.electricStationLiveData.observe(viewLifecycleOwner) {

            electricStationEntity = it[0]
            socket = it[0]?.listOfSockets?.find { it.id == socketId }

/*          val socketList = it[0]?.listOfSockets
            val newSocket = socket?.copy(status = false)
            val index = it[0]?.listOfSockets?.indexOf(socket)
            val socketList = it[0]?.listOfSockets?.toMutableList()
            socketList?.find { it.id == socketId }?.status = false
            val newElectricStationEntity = socketList?.let { it1 -> electricStationEntity!!.copy( listOfSockets = it1.toList() ) }
            val imSocketList = it[0]?.listOfSockets
            imSocketList?.filter { it.id == socketId }?.forEach { it.status = false }*/

            socket?.let {
                if (it.status) {
                    setSocketInfo(
                        resources.getString(chargeit.core.R.string.free_socket_status_text),
                        resources.getColor(chargeit.core.R.color.green_A200),
                        resources.getString(chargeit.core.R.string.set_busy_socket_status_text))
                } else {
                    setSocketInfo(
                        resources.getString(chargeit.core.R.string.busy_socket_status_text),
                        resources.getColor(chargeit.core.R.color.red_A200),
                        resources.getString(chargeit.core.R.string.release_socket_now_text))
                }
            }
        }

        socketInfoViewModel.updateLiveData.observe(viewLifecycleOwner) {
            when (it) {
                State.Success -> {
                    id?.let { socketInfoViewModel.getElectricStationInfo(it) }
                }
                State.Error -> {
                    Toast.makeText(requireContext(), "Не удалось изменить статус!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun showDialog(
        socket: SocketEntity,
        message: String
    ) {
        val builder = MaterialAlertDialogBuilder(requireContext())

        with(builder) {
            setTitle(
                requireContext().getString(
                    chargeit.core.R.string.socket_description_text,
                    socket.socket.title,
                    socket.socket.description
                )
            )
            setMessage(message)
            setPositiveButton(getString(chargeit.core.R.string.ok_button_text)) { _, _ ->
                    socket.status = !socket.status
                    electricStationEntity?.let { socketInfoViewModel.updateElectricStation(it) }
            }
            setNegativeButton(getString(chargeit.core.R.string.cancel_button_text)) { _, _ -> }
            show()
        }
    }

    private fun setSocketInfo(
        status: String,
        color: Int,
        newText: String
    ) {
        with(binding) {
            currentStatusTextView.text = status
            currentStatusTextView.setTextColor(color)
            reserveFieldTextView.text = newText
        }
    }

    private fun reserveFieldsIsValid() : Boolean {
        return !(pick_date_text_view.text == resources.getString(chargeit.core.R.string.pick_from_dialog_text) ||
            pick_time_text_view.text == resources.getString(chargeit.core.R.string.pick_from_dialog_text) ||
            pick_duration_text_view.text == resources.getString(chargeit.core.R.string.pick_from_dialog_text))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val SOCKET_EXTRA = "Socket"
        const val INFO_EXTRA = "Station info"

        fun newInstance() = SocketInfoFragment()
    }

}