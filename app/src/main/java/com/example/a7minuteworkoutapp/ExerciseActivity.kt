package com.example.a7minuteworkoutapp

import android.app.Dialog
import android.database.sqlite.SQLiteOpenHelper
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.a7minuteworkoutapp.databinding.ActivityExerciseBinding
import com.example.a7minuteworkoutapp.databinding.ExitExerciseCustomDialogBinding
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ExerciseActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    private lateinit var binding: ActivityExerciseBinding
    private lateinit var customDialogBinding: ExitExerciseCustomDialogBinding
    private var timer: CountDownTimer? = null
    private var exerciseTimerDuration: Long = 30
    private var restTimerDuration: Long = 30
    private var tProgress = 0
    private var exerciseList: ArrayList<ExerciseModel>? = null
    private var currentExercisePosition = -1
    private var tts: TextToSpeech? = null
    private var player: MediaPlayer? = null
    private var exerciseAdapter: ExerciseModelAdapter? = null

    override fun onInit(status: Int) {

        if (status == TextToSpeech.SUCCESS) {

            val result = tts!!.setLanguage(Locale.US)

            if(result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "Failed to set language")
            }
        } else {
            Log.e("TTS", "Failed to initialize")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_exercise)

        setSupportActionBar(binding.toolbarExercise)
        val actionBar = supportActionBar

        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
        }

        binding.toolbarExercise.setNavigationOnClickListener {
            setUpCustomDialog()
        }

        tts = TextToSpeech(this, this)

        exerciseList = Constants.defaultExerciseList()

        setUpRecyclerView()

        setupRestView()


    }

    private fun setUpCustomDialog() {
        val customDialog = Dialog(this)
        customDialogBinding = ExitExerciseCustomDialogBinding.inflate(layoutInflater)
        customDialog.setContentView(customDialogBinding.root)

        customDialogBinding.btnYes.setOnClickListener {
            finish()
            customDialog.dismiss()
        }

        customDialogBinding.btnNo.setOnClickListener {
            customDialog.dismiss()
        }
        customDialog.show()
    }

    private fun setupRestView() {
        binding.llRestView.visibility = View.VISIBLE
        binding.llExerciseView.visibility = View.GONE
        currentExercisePosition++
        binding.tvNextExercise.text = exerciseList!![currentExercisePosition].getName()
        if(!binding.tvNextExercise.text.isEmpty()) {
            speakOut("Upcoming exercise "+(binding.tvNextExercise.text).toString())
        }

        try {
            player = MediaPlayer.create(applicationContext, R.raw.press_start)
            player!!.isLooping = false
            player!!.start()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        if(timer != null) {
            timer!!.cancel()
            tProgress = 0
        }
        setRestProgressBar()
    }

    private fun setRestProgressBar() {
        binding.progressBarRest.progress = tProgress
        timer = object : CountDownTimer(restTimerDuration*1000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                tProgress++
                binding.tvTimer.text = (millisUntilFinished/1000).toString()
                binding.progressBarRest.progress = (restTimerDuration.toInt())-tProgress
            }

            override fun onFinish() {
                speakOut("Start "+(binding.tvNextExercise.text).toString())
                exerciseList!![currentExercisePosition].setIsSelected(true)
                exerciseAdapter!!.notifyDataSetChanged()
                setUpExerciseView()
            }
        }.start()
    }

    private fun setUpExerciseView() {
        binding.llRestView.visibility = View.GONE
        binding.llExerciseView.visibility = View.VISIBLE
        binding.ivImg.setImageResource(exerciseList!![currentExercisePosition].getImage())
        binding.tvCurrExercise.text = exerciseList!![currentExercisePosition].getName()
        if(timer != null) {
            timer!!.cancel()
            tProgress = 0
        }

        setExerciseProgressBar()
    }

    private fun setExerciseProgressBar() {
        binding.progressBarExercise.progress = tProgress
        timer = object : CountDownTimer(exerciseTimerDuration*1000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                tProgress++
                binding.tvTimerExer.text = (millisUntilFinished/1000).toString()
                binding.progressBarExercise.progress = (exerciseTimerDuration.toInt())-tProgress
            }

            override fun onFinish() {
                if(currentExercisePosition < exerciseList!!.size-1) {
                    speakOut("Take rest")
                    exerciseList!![currentExercisePosition].setIsSelected(false)
                    exerciseList!![currentExercisePosition].setIsCompleted(true)
                    exerciseAdapter!!.notifyDataSetChanged()
                    setupRestView()
                } else {
                    onWorkoutFinish()
                }
            }
        }.start()
    }

    private fun setUpRecyclerView() {
        binding.rvExerciseStatus.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        exerciseAdapter = ExerciseModelAdapter(this, exerciseList!!)
        binding.rvExerciseStatus.adapter = exerciseAdapter
    }

    private fun onWorkoutFinish() {
        binding.llExerciseView.visibility = View.GONE
        binding.rvExerciseStatus.visibility = View.GONE
        binding.llFinishView.visibility = View.VISIBLE
        addDateToDatabase()

        binding.btnFinish.setOnClickListener {
            finish()
        }


    }

    private fun addDateToDatabase() {
        val calendar = Calendar.getInstance()
        val dateTime = calendar.time
        Log.i("Date", ""+dateTime)

        val sdf = SimpleDateFormat("dd MMM yyyy HH:mm:ss", Locale.getDefault())
        val date = sdf.format(dateTime)

        val dbHandler = SqliteOpenHelper(this, null)
        dbHandler.addDate(date)
        dbHandler.close()
    }

    private fun speakOut(text: String) {
        tts!!.speak(text, TextToSpeech.QUEUE_ADD, null, "")
    }

    override fun onDestroy() {
        if(timer != null) {
            timer!!.cancel()
            tProgress = 0
        }

        if(tts != null) {
            tts!!.stop()
            tts!!.shutdown()
        }

        if(player != null) {
            player!!.stop()
        }
        super.onDestroy()
    }
}