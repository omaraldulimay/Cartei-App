package com.example.cartei_mobile_app.viewmodel

import androidx.lifecycle.*
import com.example.cartei_mobile_app.data.Karte
import com.example.cartei_mobile_app.repository.KartenRepository
import kotlinx.coroutines.launch
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.cartei_mobile_app.data.AppDatenbank



class KartenViewModel(application: Application, private val satzId: Int) : AndroidViewModel(application) {

    private val dao = AppDatenbank.getDatenbank(application).kartenDao()
    val alleKarten: LiveData<List<Karte>> = dao.alleKartenFürSatz(satzId)


    fun alleKartenLöschen() {
        viewModelScope.launch(Dispatchers.IO) {
            dao.alleKartenEinesSatzesLöschen(satzId)
        }
    }

    fun karteAktualisieren(karte: Karte) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.karteAktualisieren(karte)
        }
    }

    fun gelerntZurücksetzen() {
        viewModelScope.launch(Dispatchers.IO) {
            dao.gelerntZurücksetzen(satzId)
        }
    }


}

class KartenViewModelFactory(
    private val application: Application,
    private val satzId: Int
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(KartenViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return KartenViewModel(application, satzId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
