package com.example.davidblanco.projectod

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_view_project.view.*


class ViewProjectFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var view=inflater.inflate(R.layout.fragment_view_project, container, false)
        var bundle=getArguments()
        var valor1:String=bundle!!.getString("titulo")
        var valor2:String=bundle!!.getString("descripcion")
        Toast.makeText(context, ""+ valor1,
                Toast.LENGTH_SHORT).show()
        view.txtViewT.setText(valor1)
        view.txtViewD.setText(valor2)
        return  view
    }


}
