package com.example.davidblanco.projectod

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.fragment_project.*
import kotlinx.android.synthetic.main.fragment_project.view.*
import kotlinx.android.synthetic.main.project_dialog.view.*
import java.util.*
import android.support.v4.app.FragmentTransaction
import android.util.Log
import android.widget.Toast
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ValueEventListener
import android.content.SharedPreferences


class ProjectFragment : Fragment(), View.OnClickListener {
    val database = FirebaseDatabase.getInstance()
    val myRef = database.getReference("Proyectos")
    private var projects: ArrayList<Project> = ArrayList<Project>()
    private var adapter: ProjectAdapter? = null
    private var pro: Project = Project();
    var escuela:String=""
    override fun onClick(v: View?) {
        var bundle: Bundle = Bundle();
        bundle.putString("titulo", projects!!.get(recyclerView1.getChildAdapterPosition(v)).titulo)
        bundle.putString("descripcion", projects!!.get(recyclerView1.getChildAdapterPosition(v)).descripcion)
        bundle.putString("key", projects!!.get(recyclerView1.getChildAdapterPosition(v)).key)
        bundle.putString("escuela", projects!!.get(recyclerView1.getChildAdapterPosition(v)).escuela)
        bundle.putString("fecha", projects!!.get(recyclerView1.getChildAdapterPosition(v)).fecha)
        bundle.putInt("tipo", projects!!.get(recyclerView1.getChildAdapterPosition(v)).tipo)
        val fm: FragmentManager? = fragmentManager
        val fm2: FragmentTransaction? = fm!!.beginTransaction()
        var second: ViewProjectFragment = ViewProjectFragment()
        second.setArguments(bundle)
        fm2!!.replace(R.id.container, second).commit()
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var view: View = inflater.inflate(R.layout.fragment_project, container, false)
        view.recyclerView1.layoutManager = LinearLayoutManager(context, LinearLayout.VERTICAL, false)
        adapter = ProjectAdapter(this.projects!!)
        view.recyclerView1.adapter = adapter
        adapter!!.setOnClickListener(this)
        mostrar()
        leerDatos()
        view.fabtn.setOnClickListener {
            val mDialogView = LayoutInflater.from(context).inflate(R.layout.project_dialog, null)
            val mBuilder = AlertDialog.Builder(context).setView(mDialogView)

            val mAlertDialog = mBuilder.show()
            mDialogView.btnSendProyect.setOnClickListener {
                mAlertDialog.dismiss()
                val titulo = mDialogView.etxtTitle.text.toString()
                val descripcion = mDialogView.etxtDescripcion.text.toString()
                pro = Project(titulo, descripcion, escuela, obtenerFecha(), 0)
                EnviarProyecto()
                leerDatos()
            }
            mDialogView.btncancel.setOnClickListener {
                mAlertDialog.dismiss()
            }
        }
        return view
    }

    private fun mostrar() {
        var sharedPreferences: SharedPreferences = this.activity!!.getSharedPreferences("Credenciales", Context.MODE_PRIVATE)
        escuela=sharedPreferences.getString("escuela","escuela")


    }

    private fun EnviarProyecto() {
        val key = myRef.push().key
        pro = Project(key!!, pro.titulo, pro.descripcion, pro.escuela, pro.fecha, pro.tipo)
        myRef.child(key).setValue(pro)
    }

    private fun leerDatos() {
        myRef.orderByChild("tipo").addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                projects.removeAll(projects)
                for (snapshot: DataSnapshot in dataSnapshot.children) {
                    var pjt: Project = snapshot.getValue(Project::class.java)!!
                    projects!!.add(pjt)
                }
                adapter!!.notifyDataSetChanged()

            }
        })
    }

    private fun obtenerFecha(): String {
        val c = Calendar.getInstance()
        var fecha = "" + c.get(Calendar.DAY_OF_MONTH) + "- " + (c.get(Calendar.MONTH) + 1) + "- " + c.get(Calendar.YEAR);
        return fecha;
    }


}
