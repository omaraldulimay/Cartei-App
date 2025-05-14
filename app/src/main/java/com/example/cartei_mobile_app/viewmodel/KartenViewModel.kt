package com.example.cartei.viewmodel

import androidx.lifecycle.*
import com.example.cartei.data.Karte
import com.example.cartei.repository.KartenRepository
import kotlinx.coroutines.launch

class KartenViewModel(private val repository: KartenRepository) : ViewModel() {

    private val _aktuellerSatzId = MutableLiveData<Int>()

    val karten: LiveData<List<Karte>> = _aktuellerSatzId.switchMap { satzId ->
        repository.kartenFürSatz(satzId)
    }

    fun ladeKartenFürSatz(satzId: Int) {
        _aktuellerSatzId.value = satzId
    }

    fun karteEinfügen(karte: Karte) {
        viewModelScope.launch {
            repository.karteEinfügen(karte)
        }
    }

    fun karteLöschen(karte: Karte) {
        viewModelScope.launch {
            repository.karteLöschen(karte)
        }
    }

    fun karteAktualisieren(karte: Karte) {
        viewModelScope.launch {
            repository.karteAktualisieren(karte)
        }
    }
}
