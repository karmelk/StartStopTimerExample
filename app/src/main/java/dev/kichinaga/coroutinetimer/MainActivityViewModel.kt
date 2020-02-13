package dev.kichinaga.coroutinetimer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class MainActivityViewModel : ViewModel(), CoroutineScope {
    var recursionJob: Job?=null
    private val job = SupervisorJob()
    private var _userID: Int = 0
    private var _secondCount: Int = 0
    private val hashMap: HashMap<Int, Int> = HashMap()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private val _state = MutableLiveData<Int>().also { it.value = 2 }
    val state: LiveData<Int>
        get() = _state

    private val _timer: MutableLiveData<Int> by lazy { MutableLiveData<Int>() }
    private val _timerProress: MutableLiveData<MutableMap<Int, Int>> by lazy { MutableLiveData<MutableMap<Int, Int>>() }

    val timer: LiveData<Int>
        get() = _timer
    val timerProgress: LiveData<MutableMap<Int, Int>>
        get() = _timerProress

    fun changeTimerAction(changeState: Int, userId: Int, secondCount: Int) {

       if(recursionJob!=null){
           recursionJob!!.cancel()
       }
        _secondCount = secondCount
        hashMap.clear()
        hashMap[userId] = 1
        _timer.value = 1
        _timerProress.value = hashMap
        _userID = userId
        _state.value = changeState
    }

    fun changeTimerStop(changeState: Int) {
        if(recursionJob!=null){
            recursionJob!!.cancel()
        }
        hashMap.clear()
        hashMap[_userID] = 0
        _timerProress.value = hashMap
        _state.value = changeState
    }

    fun countUpTimer() {
        recursionJob = launch(Dispatchers.Main) {
            delay(1000)
            if (state.value == startTimer) {
                val updateValue = timer.value!!.plus(1)
                if (updateValue > _secondCount) {
                    if(recursionJob!=null){
                        recursionJob!!.cancel()
                    }
                    _state.value = stopTimer
                    hashMap.clear()
                    hashMap[_userID] = 0
                    //_timer.value = 1
                    _timerProress.value = hashMap

                } else {
                    hashMap.clear()
                    hashMap[_userID] = updateValue
                    _timer.value = updateValue
                    _timerProress.value = hashMap
                }
            }

        }
    }

    override fun onCleared() {
        job.cancelChildren()
        super.onCleared()
    }


}