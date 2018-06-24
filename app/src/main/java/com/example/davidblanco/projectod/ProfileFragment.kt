package com.example.davidblanco.projectod

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_profile.view.*


class ProfileFragment : Fragment(), View.OnClickListener {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var view: View = inflater.inflate(R.layout.fragment_profile, container, false)
        //.setEnabled(true);
        view.btnEditar.setOnClickListener(this)
        view.btnGuardar.setOnClickListener(this)
        return view
    }

    override fun onClick(v: View?) {
        val i = v!!.id
        if (i == R.id.btnEditar) {
            btnGuardar.setVisibility(View.VISIBLE);
            btnEditar.setVisibility(View.INVISIBLE);
            etxtName.setEnabled(true);
            etxtSchool.setEnabled(true);
            etxtPassword.setEnabled(true);
            etxtPassword2.setEnabled(true);
        } else if (i == R.id.btnGuardar) {
            btnEditar.setVisibility(View.VISIBLE);
            btnGuardar.setVisibility(View.INVISIBLE);
            etxtName.setEnabled(false);
            etxtSchool.setEnabled(false);
            etxtPassword.setEnabled(false);
            etxtPassword2.setEnabled(false);
        }
    }

}
