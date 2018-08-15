package com.example.davidblanco.projectod

import android.app.AlertDialog

import android.content.Context
import android.content.SharedPreferences
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
import kotlinx.android.synthetic.main.comment_dialog.view.*


class ViewProjectFragment : Fragment(), View.OnClickListener {
    private val database = FirebaseDatabase.getInstance()
    private val myRef = database.getReference("Proyectos")
    private val myRef2 = database.getReference("ProyectosReady")
    private val myRef3 = database.getReference("Avisos")
    private var prj: Project = Project()
    private var key2: String = ""
    private var tipo: Int = 0
    override fun onClick(v: View?) {
        val i = v!!.getId()
        if (i == R.id.btnCheck2) {
            val builder = AlertDialog.Builder(activity)

            builder.setTitle("Finalizar proyecto")
                    .setMessage("¿El proyecto se ejecutó?, si desea puede agregar un comentario")
            builder.setPositiveButton("SI") { dialog, which ->
                moveElements2()
                dialog.dismiss()
            }
            builder.setNegativeButton("NO") { dialog, which ->
                dialog.dismiss()
            }
            builder.setNeutralButton("Comentario") { dialog, which ->
                val mDialogView = LayoutInflater.from(context).inflate(R.layout.comment_dialog, null)
                val mBuilder = AlertDialog.Builder(context).setView(mDialogView)
                val mAlertDialog = mBuilder.show()
                mDialogView.btnSendComment.setOnClickListener {
                    prj.comentario = prj.comentario + "\n" + mDialogView.etxtDescripcion2.text.toString()
                    moveElements()
                    mAlertDialog.dismiss()
                }
                mDialogView.btncancel2.setOnClickListener {
                    mAlertDialog.dismiss()
                    dialog.dismiss()
                }
            }
            val alert = builder.create()
            alert.show()

        } else if (i == R.id.btnDelete2) {
            val builder = AlertDialog.Builder(activity)

            builder.setTitle("Eliminar proyecto")
                    .setMessage("¿Desea eliminar este proyecto de forma definitiva?")
            builder.setPositiveButton("SI") { dialog, which ->
                deleteElements(prj)
                dialog.dismiss()
            }
            builder.setNegativeButton("NO") { dialog, which ->

                dialog.dismiss()
            }

            val alert = builder.create()
            alert.show()
        } else if (i == R.id.btnVolver2) {
            volverAtras()
        }
    }

    private fun moveElements() {
        if (tipo == 0) {
            moveElements2()
        } else {
            myRef.child(prj.key).setValue(prj)
            volverAtras()
        }


    }

    private fun moveElements2() {
        val key = myRef2.push().key
        key2 = prj.key
        val prj2 = Project(key!!, prj.titulo, prj.descripcion, prj.escuela, prj.fecha, prj.tipo, prj.email, prj.comentario)
        myRef2.child(key).setValue(prj2)
        deleteElements(prj)

    }

    private fun deleteElements(pj: Project) {
        if (!pj.escuela.equals("Departamento de educación")) {
            myRef.child(pj.key).removeValue()
        } else {
            myRef3.child(pj.key).removeValue()
        }
        volverAtras()
    }

    private fun volverAtras() {
        val fm: android.support.v4.app.FragmentManager? = fragmentManager
        fm!!.beginTransaction().replace(R.id.container, ProjectFragment()).commit()
    }

    val arrayLevel = arrayOf("Select", "Alto", "Medio", "Bajo")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_view_project, container, false)
        view.btnCheck2.setOnClickListener(this)
        view.btnDelete2.setOnClickListener(this)
        view.btnVolver2.setOnClickListener(this)
        val bundle = getArguments()
        prj = Project(bundle!!.getString("key"), bundle.getString("titulo"), bundle.getString("descripcion"),
                bundle.getString("escuela"), bundle.getInt("fecha"), bundle.getInt("tipo"), bundle.getString("email"), bundle.getString("comentario"))
        view.txtViewT.setText(prj.titulo)
        view.txtViewD.setText(prj.descripcion)
        view.txtSchoolV.setText(prj.escuela)
        view.txtFechaV.setText(dateFormat(prj.fecha))
        mostrarDatos()
        if (prj.escuela.equals("Departamento de educación")) {
            if (tipo == 0) {
                view.btnDelete2.visibility = View.VISIBLE
            } else {
                view.btnDelete2.visibility = View.INVISIBLE
            }
            view.btnCheck2.visibility = View.INVISIBLE
            view.spLevel.visibility = View.INVISIBLE
        } else if (tipo == 1) {
            view.spLevel.visibility = View.INVISIBLE
        } else {
            view.btnDelete2.visibility = View.VISIBLE
            view.btnCheck2.visibility = View.VISIBLE
            view.spLevel.visibility = View.VISIBLE
            val spinner = view.findViewById(R.id.spLevel) as Spinner
            val arrayAdapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, arrayLevel)
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
        }
        if (!prj.comentario.equals("")) {
            view.txtViewC.visibility = view.visibility
            view.txtViewTC.visibility = view.visibility
            view.txtViewC.text = prj.comentario
        }
        return view
    }

    private fun dateFormat(fecha: Int): String {
        val cadena = "" + fecha
        cadena.toCharArray()
        return cadena[6].toString() + cadena[7].toString() + "-" + cadena[4].toString() +
                cadena[5].toString() + "-" + cadena[0].toString() + cadena[1].toString() +
                cadena[2].toString() + cadena[3].toString()
    }

    private fun mostrarDatos() {
        val sharedPreferences: SharedPreferences = this.activity!!.getSharedPreferences("Credenciales", Context.MODE_PRIVATE)
        tipo = sharedPreferences.getInt("tipo", 1)


    }


}
