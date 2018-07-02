package com.example.davidblanco.projectod

import android.app.FragmentManager
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_view_project.view.*


class ViewProjectFragment : Fragment(), View.OnClickListener {
    val database = FirebaseDatabase.getInstance()
    val myRef = database.getReference("Proyectos")
    val myRef2 = database.getReference("ProyectosReady")
    var prj: Project = Project();
    var key2:String="";
    override fun onClick(v: View?) {
        val i = v!!.getId()
        if (i == R.id.btnCheck2) {
            moveElements()
        } else if (i == R.id.btnDelete2) {
            deleteElements(prj.key)
        }
    }

    private fun moveElements() {
        val key=myRef2.push().key
        key2=prj.key
        prj=Project(key!!,prj.titulo,prj.descripcion,prj.escuela,prj.fecha,prj.tipo)
        myRef2.child(key).setValue(prj)
        deleteElements(key2)
    }

    private fun deleteElements(key:String) {
        Toast.makeText(context, " " + prj.key,
                Toast.LENGTH_SHORT).show()
        myRef.child(key).removeValue()
        volverAtras();
    }

    private fun volverAtras() {
        val fm: android.support.v4.app.FragmentManager? = fragmentManager
        fm!!.beginTransaction().replace(R.id.container, ProjectFragment()).commit()
    }

    var arrayLevel = arrayOf("Select", "Alto", "Medio", "Bajo");

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_view_project, container, false)
        view.btnCheck2.setOnClickListener(this)
        view.btnDelete2.setOnClickListener(this)
        var bundle = getArguments()
        prj = Project(bundle!!.getString("key"), bundle!!.getString("titulo"), bundle!!.getString("descripcion"), bundle!!.getString("escuela"),
                bundle!!.getString("fecha"),bundle!!.getInt("tipo"))
        view.txtViewT.setText(prj.titulo)
        view.txtViewD.setText(prj.descripcion)
        view.txtSchoolV.setText(prj.escuela)
        var spinner = view.findViewById(R.id.spLevel) as Spinner
        var arrayAdapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, arrayLevel)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = arrayAdapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                if (position == 1) {
                    prj.tipo = 1
                } else if (position == 2) {
                    prj.tipo = 2
                } else if (position == 3) {
                    prj.tipo = 3
                }
                myRef.child(prj.key).setValue(prj)
            }
        }

        return view
    }


}
