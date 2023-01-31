package com.example.android_sound

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class SoundGenerator : AppCompatActivity(){
    lateinit var _player: Player
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        val ga = SoundPlayer(this)
//        ga.play()

        this._player = Player()
        this._player.init(this)
        generatorView1()!!.setPro(true)
        generatorView1()!!.init(this, this._player, 0)
    }

    fun generatorView1(): GeneratorView? {
        return findViewById<View>(R.id.generator1) as GeneratorView
    }

    override fun onResume() {
        generatorView1()!!.postInvalidate()
        super.onResume()
    }
}