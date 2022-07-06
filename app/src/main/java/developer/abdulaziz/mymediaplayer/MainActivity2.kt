package developer.abdulaziz.mymediaplayer

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.media.MediaPlayer
import android.media.PlaybackParams
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Parcel
import android.view.View
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import developer.abdulaziz.mymediaplayer.Object.MyObject
import developer.abdulaziz.mymediaplayer.databinding.ActivityMain2Binding
import kotlin.random.Random

class MainActivity2 : AppCompatActivity() {
    private lateinit var binding: ActivityMain2Binding
    private lateinit var mediaPlayer: MediaPlayer
    private var handler = Handler()

    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.apply {

            start(MyObject.position)
//            val playBackParams = mediaPlayer.playbackParams
//            playBackParams.speed = 1F
//            playBackParams.pitch = 1F
//            playBackParams.audioFallbackMode = PlaybackParams.AUDIO_FALLBACK_MODE_DEFAULT
//            mediaPlayer.playbackParams = playBackParams

            menu.setOnClickListener { finish() }
            imageForeground.setOnClickListener {
                if (Random.nextInt(2) == 1) it.startAnimation(360F)
                else it.startAnimation(-360F)
            }

            rightNext.setOnClickListener {
                if (MyObject.position < MyObject.list.size) {
                    MyObject.position = (++MyObject.position) % MyObject.list.size
                    mediaPlayer.stop()
                    start(MyObject.position)
                } else {
                    MyObject.position = 0
                    mediaPlayer.stop()
                    start(MyObject.position)
                }
            }
            leftNext.setOnClickListener {
                if (MyObject.position > 0) {
                    MyObject.position = (--MyObject.position) % MyObject.list.size
                    mediaPlayer.stop()
                    start(MyObject.position)
                } else {
                    MyObject.position = MyObject.list.size - 1
                    mediaPlayer.stop()
                    start(MyObject.position)
                }
            }
            rightSecond.setOnClickListener {
                if ((mediaPlayer.duration.durationInt() - mediaPlayer.currentPosition) > 15000) {
                    seekbar.progress += 15000
                    mediaPlayer.seekTo(seekbar.progress)
                    chronometer.text = mediaPlayer.currentPosition.durationString()
                }
            }
            leftSecond.setOnClickListener {
                if (mediaPlayer.currentPosition > 15000) {
                    seekbar.progress -= 15000
                    mediaPlayer.seekTo(seekbar.progress)
                    chronometer.text = mediaPlayer.currentPosition.durationString()
                }
            }

            playPause.setOnClickListener {
                if (MyObject.play) {
                    playPause.setImageResource(R.drawable.play_music)
                    MyObject.play = false
                    mediaPlayer.pause()
                } else {
                    playPause.setImageResource(R.drawable.pause_music)
                    MyObject.play = true
                    mediaPlayer.start()
                }
            }

            seekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                    if (p2) mediaPlayer.seekTo(p1)
                    chronometer.text = mediaPlayer.currentPosition.durationString()
                    if (chronometer.text == mediaPlayer.duration.durationString()) {
                        if (MyObject.position < MyObject.list.size) {
                            MyObject.position = (++MyObject.position) % MyObject.list.size
                            start(MyObject.position)
                        } else {
                            MyObject.position = 0
                            start(MyObject.position)
                        }
                    }
                }

                override fun onStartTrackingTouch(p0: SeekBar?) {}
                override fun onStopTrackingTouch(p0: SeekBar?) {}
            })
        }
    }

    private fun start(position: Int) {
        binding.apply {
            mediaPlayer = MediaPlayer.create(
                root.context,
                Uri.parse(MyObject.list[MyObject.position].musicPath)
            )
            mediaPlayer.start()
            seekbar.max = mediaPlayer.duration
            handler.postDelayed(runnable, 100)
            textCounter.text = "${position + 1}/${MyObject.list.size}"
            musicName.text = MyObject.list[position].musicTitle
            musicArtist.text = MyObject.list[position].artist
            time.text = mediaPlayer.duration.durationString()
            chronometer.text = "00:00"
            playPause.setImageResource(R.drawable.pause_music)
            MyObject.play = true
        }
    }

    private fun Int.durationString(): String {
        val m = this / 1000 / 60
        val s = this / 1000 % 60
        val min = if (m < 10) "0$m" else "$m"
        val sec = if (s < 10) "0$s" else "$s"

        return "$min:$sec"
    }

    private fun Int.durationInt(): Long =
        ((((this / 1000 / 60) * 60) + (this / 1000 % 60)) * 1000).toLong()

    override fun onPause() {
        super.onPause()
        binding.playPause.setImageResource(R.drawable.play_music)
        MyObject.play = false
        mediaPlayer.pause()
    }

    private var runnable = object : Runnable {
        override fun run() {
            binding.seekbar.progress = mediaPlayer.currentPosition
            handler.postDelayed(this, 100)
        }
    }

    private fun View.startAnimation(degree: Float) {
        val objectAnimator = ObjectAnimator.ofFloat(this, "rotation", 0f, degree)
        objectAnimator.duration = 1000
        val setAnimator = AnimatorSet()
        setAnimator.playTogether(objectAnimator)
        setAnimator.start()
    }
}