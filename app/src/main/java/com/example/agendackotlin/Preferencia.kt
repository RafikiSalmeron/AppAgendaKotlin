package com.example.agendackotlin

import android.os.Bundle
import android.preference.PreferenceActivity

class Preferencia : PreferenceActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addPreferencesFromResource(R.xml.mis_preferencias)


    }
}
