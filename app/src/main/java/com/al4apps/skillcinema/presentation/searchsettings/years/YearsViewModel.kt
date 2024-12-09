package com.al4apps.skillcinema.presentation.searchsettings.years

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.al4apps.skillcinema.utils.SingleLiveEvent
import timber.log.Timber
import java.text.SimpleDateFormat

class YearsViewModel : ViewModel() {

    private var yearsChunkedList1: List<List<Year>>
    private var yearsChunkedList2: List<List<Year>>
    private var selectedYearMin: Year? = null
    private var selectedYearMax: Year? = null
    private var indexRangeMin: Int
    private var indexRangeMax: Int

    private val _years1LiveData = MutableLiveData<List<Year>>()
    val years1LiveData: LiveData<List<Year>> get() = _years1LiveData

    private val _years2LiveData = MutableLiveData<List<Year>>()
    val years2LiveData: LiveData<List<Year>> get() = _years2LiveData

    private val _itemToUnselectLiveData = MutableLiveData<Year>()
    val itemToUnselectLiveData: LiveData<Year> get() = _itemToUnselectLiveData

    private val _itemToUnselect2LiveData = MutableLiveData<Year>()
    val itemToUnselect2LiveData: LiveData<Year> get() = _itemToUnselect2LiveData

    private val _rangeLiveData = MutableLiveData<YearsRange>()
    val rangeLiveData: LiveData<YearsRange> get() = _rangeLiveData

    private val _exceptionLiveData = SingleLiveEvent<Int>()
    val exceptionLiveData: LiveData<Int> get() = _exceptionLiveData

    init {
        yearsChunkedList1 = generateYears().chunkToPeriods()
        yearsChunkedList2 = generateYears().chunkToPeriods()
        indexRangeMin = yearsChunkedList1.lastIndex - 2
        indexRangeMax = yearsChunkedList2.lastIndex
    }

    private fun getYearsRange() {
        try {
            val list1 = selectedYearMin?.let { year ->
                val index = year.getChunkIndexWithTheItem(yearsChunkedList1)
                indexRangeMin = index
                val list = yearsChunkedList1[index]
                list.updateSelectedItem(year)
                list
            } ?: yearsChunkedList1[indexRangeMin]

            val list2 = selectedYearMax?.let { year ->
                val index = year.getChunkIndexWithTheItem(yearsChunkedList2)
                indexRangeMax = index
                val list = yearsChunkedList2[index]
                list.updateSelectedItem(year)
                list
            } ?: yearsChunkedList2.last()

            _years1LiveData.value = list1
            _years2LiveData.value = list2
        } catch (t: Throwable) {
            Timber.d(t)
        }
    }

    fun nextRange(table: Int) {
        try {
            if (table == YearsFragment.RANGE_TABLE_1) {
                if (indexRangeMin >= indexRangeMax) return
                indexRangeMin++
                selectedYearMin?.let { yearsChunkedList1[indexRangeMin].updateSelectedItem(it) }
                _years1LiveData.value = yearsChunkedList1[indexRangeMin]
            }
            if (table == YearsFragment.RANGE_TABLE_2) {
                if (indexRangeMax >= yearsChunkedList2.lastIndex) return
                indexRangeMax++
                selectedYearMax?.let { yearsChunkedList2[indexRangeMax].updateSelectedItem(it) }
                _years2LiveData.value = yearsChunkedList2[indexRangeMax]
            }
        } catch (t: Throwable) {
            Timber.d(t)
        }
    }

    fun prevRange(table: Int) {
        try {
            if (table == YearsFragment.RANGE_TABLE_1) {
                if (indexRangeMin < 1) return
                indexRangeMin--
                selectedYearMin?.let { yearsChunkedList1[indexRangeMin].updateSelectedItem(it) }
                _years1LiveData.value = yearsChunkedList1[indexRangeMin]
            }
            if (table == YearsFragment.RANGE_TABLE_2) {
                if (indexRangeMax <= indexRangeMin) return
                indexRangeMax--
                selectedYearMax?.let { yearsChunkedList2[indexRangeMax].updateSelectedItem(it) }
                _years2LiveData.value = yearsChunkedList2[indexRangeMax]
            }
        } catch (t: Throwable) {
            Timber.d(t)
        }
    }

    fun saveYearsRange() {
        selectedYearMin?.let { min ->
            selectedYearMax?.let { max ->
                if (min.year > max.year) {
                    _exceptionLiveData.value = YearsFragment.EXCEPTION_INCORRECT_RANGE
                    return
                }
            }
        }
        _rangeLiveData.value = YearsRange(selectedYearMin, selectedYearMax)
    }

    fun setSelectedYears(yearsRange: YearsRange) {
        selectedYearMin = yearsRange.min
        selectedYearMax = yearsRange.max
        getYearsRange()
    }

    fun onItemClick(item: Year, table: Int) {
        if (table == YearsFragment.RANGE_TABLE_1) {
            Timber.d(selectedYearMin?.year.toString())
            selectedYearMin?.let { prevSelect ->
                val prevIndex = prevSelect.getChunkIndexWithTheItem(yearsChunkedList1)
                if (prevIndex == indexRangeMin) {
                    _itemToUnselectLiveData.value = prevSelect
                }
            }
            selectedYearMin = if (item.isSelected) {
                yearsChunkedList1[indexRangeMin].updateSelectedItem(item)
                _years1LiveData.value = yearsChunkedList1[indexRangeMin]
                item
            } else null
        }
        if (table == YearsFragment.RANGE_TABLE_2) {
            selectedYearMax?.let { prevSelect ->
                val prevIndex = prevSelect.getChunkIndexWithTheItem(yearsChunkedList2)
                if (prevIndex == indexRangeMax) {
                    _itemToUnselect2LiveData.value = prevSelect
                }
            }
            selectedYearMax = if (item.isSelected) {
                yearsChunkedList2[indexRangeMax].updateSelectedItem(item)
                _years2LiveData.value = yearsChunkedList2[indexRangeMax]
                item
            } else null
        }
    }

    private fun Year.getChunkIndexWithTheItem(chunkedList: List<List<Year>>): Int {
        return chunkedList.indexOfFirst { list ->
            list.map { it.year }.contains(year)
        }

    }

    private fun List<Year>.updateSelectedItem(item: Year) {
        forEach { it.isSelected = it.year == item.year }
    }

    private fun generateYears(): List<Year> {
        val millis = System.currentTimeMillis()
        val currentYear = SimpleDateFormat("yyyy").format(millis).toInt()
        return (START_YEAR..currentYear).map(::Year)
    }

    private fun List<Year>.chunkToPeriods(): List<List<Year>> {
        return reversed().chunked(TABLE_SIZE).map { it.reversed() }.reversed()
    }

    companion object {
        private const val START_YEAR = 1900
        private const val TABLE_SIZE = 12
    }
}