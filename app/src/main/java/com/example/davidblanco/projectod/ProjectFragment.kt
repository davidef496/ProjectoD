package com.example.davidblanco.projectod

import android.app.AlertDialog
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
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ValueEventListener


class ProjectFragment : Fragment(), View.OnClickListener {
    val database = FirebaseDatabase.getInstance()
    val myRef = database.getReference("Proyectos")
    private var projects: ArrayList<Project> = ArrayList<Project>()
    private var adapter: ProjectAdapter? = null
    override fun onClick(v: View?) {
        var bundle: Bundle = Bundle();
        bundle.putString("titulo", projects!!.get(recyclerView1.getChildAdapterPosition(v)).titulo)
        bundle.putString("descripcion", projects!!.get(recyclerView1.getChildAdapterPosition(v)).descripcion)
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
        leerDatos()
        view.fabtn.setOnClickListener {
            val mDialogView = LayoutInflater.from(context).inflate(R.layout.project_dialog, null)
            val mBuilder = AlertDialog.Builder(context).setView(mDialogView)

            val mAlertDialog = mBuilder.show()
            mDialogView.btnSendProyect.setOnClickListener {
                mAlertDialog.dismiss()
                val titulo = mDialogView.etxtTitle.text.toString()
                val descripcion = mDialogView.etxtDescripcion.text.toString()
                projects!!.add(Project(titulo, descripcion, "escuela", obtenerFecha(), 0))
                EnviarProyecto()
                leerDatos()
            }
            mDialogView.btncancel.setOnClickListener {
                mAlertDialog.dismiss()
            }
        }
        return view
    }

    private fun EnviarProyecto() {
        myRef.push().setValue(projects!!.get(projects!!.size - 1))
    }

    private fun leerDatos() {
        myRef.addValueEventListener(object : ValueEventListener {
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
