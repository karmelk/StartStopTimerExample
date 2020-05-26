package dev.kmworks.coroutinetimer


import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import dev.kmworks.coroutinetimer.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    var userId: Int = 0
    var secondCount: Int = 10
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityMainBinding = DataBindingUtil.setContentView(this@MainActivity, R.layout.activity_main)
        binding.lifecycleOwner = this
        val viewModel = ViewModelProviders.of(this).get(MainActivityViewModel::class.java)
        binding.viewModel = viewModel
        progressBar_user_three.max = secondCount

        binding.timerAction.setOnClickListener {
            when (viewModel.state.value) {
                startTimer -> {
                    viewModel.changeTimerStop(stopTimer)
                }
                stopTimer -> {
                    if (userId == 4) {
                        userId = 0
                    }
                    userId++
                    viewModel.changeTimerAction(startTimer, userId, secondCount)
                }
            }

        }


        viewModel.timer.observe(this, Observer {
            viewModel.countUpTimer()
        })

        viewModel.timerProgress.observe(this, Observer {
            for ((userId, progress) in it.entries) {
                progressBar_user_three.progress = progress
                Log.i("Milisecond", "$progress $userId")
            }
        })
    }
}
