package chargeit.main_screen.ui

import android.content.res.Resources
import chargeit.core.viewmodel.CoreViewModel
import chargeit.main_screen.R
import chargeit.main_screen.domain.DefaultPlace
import chargeit.main_screen.domain.MapsFragmentDataset
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.Subject

class MapsFragmentViewModel : CoreViewModel() {
    val datasetLiveData: Observable<MapsFragmentDataset> = BehaviorSubject.create()
    fun getDefaultPlace() = DefaultPlace()
    fun powerOn() {
        datasetLiveData.mutable().onNext(MapsFragmentDataset())
    }

    private fun <T : Any> Observable<T>.mutable(): Subject<T> {
        return this as? Subject<T>
            ?: throw IllegalStateException(
                Resources.getSystem().getString(R.string.maps_fragment_viewmodel_error_message)
            )
    }
}