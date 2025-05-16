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



class KartenViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = AppDatenbank.getDatenbank(application).kartenDao()
    val alleKarten: LiveData<List<Karte>> = dao.alleKartenFürSatz(0)

    fun karteLöschen(karte: Karte) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.karteLöschen(karte)
        }
    }

    fun alleKartenLöschen() {
        viewModelScope.launch(Dispatchers.IO) {
            dao.alleKartenEinesSatzesLöschen(0)
        }
    }

    fun karteAktualisieren(karte: Karte) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.karteAktualisieren(karte)
        }
    }


}

class KartenViewModelFactory(private val application: Application) :
    ViewModelProvider.AndroidViewModelFactory(application)