package com.example.supporthealth.stress.statistic.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class StatisticMoodViewModel : ViewModel() {

    // Настроение: уровни (0–6)
    private val _moodCounts = MutableLiveData<List<Int>>()
    val moodCounts: LiveData<List<Int>> = _moodCounts

    // Настроение по времени суток: утро, день, вечер, ночь
    private val _dayPartMoods = MutableLiveData<List<Int>>()
    val dayPartMoods: LiveData<List<Int>> = _dayPartMoods

    // Энергия: уровни (0–6)
    private val _energyCounts = MutableLiveData<List<Int>>()
    val energyCounts: LiveData<List<Int>> = _energyCounts

    private val _dayPartEnergy = MutableLiveData<List<Int>>()
    val dayPartEnergy: LiveData<List<Int>> = _dayPartEnergy

    init {
        _moodCounts.value = listOf(1, 2, 0, 3, 1, 0, 1)
        _dayPartMoods.value = listOf(4, 3, 2, 1)

        _energyCounts.value = listOf(0, 1, 3, 2, 1, 0, 0)
        _dayPartEnergy.value = listOf(2, 3, 1, 1)
    }

}
