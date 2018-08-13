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
   private val database = FirebaseDatabase.getInstance()
    private val myRef = database.getReference("Proyectos")
    private val myRef2 = database.getReference("Avisos")
    private var projects: ArrayList<Project> = ArrayList()
    private var adapter: ProjectAdapter? = null
    private var pro = Project()
    private var escuela: String = ""
    private var tipo = 1
    private var email: String = ""
    override fun onClick(v: View?) {
        val bundle: Bundle = Bundle()
        bundle.putString("titulo", projects.get(recyclerView1.getChildAdapterPosition(v)).titulo)
        bundle.putString("descripcion", projects.get(recyclerView1.getChildAdapterPosition(v)).descripcion)
        bundle.putString("key", projects.get(recyclerView1.getChildAdapterPosition(v)).key)
        bundle.putString("escuela", projects.get(recyclerView1.getChildAdapterPosition(v)).escuela)
        bundle.putInt("fecha", projects.get(recyclerView1.getChildAdapterPosition(v)).fecha)
        bundle.putInt("tipo", projects.get(recyclerView1.getChildAdapterPosition(v)).tipo)
        bundle.putString("email", projects.get(recyclerView1.getChildAdapterPosition(v)).email)
        val fm: FragmentManager? = fragmentManager
        val fm2: FragmentTransaction? = fm!!.beginTransaction()
        val second: ViewProjectFragment = ViewProjectFragment()
        second.setArguments(bundle)
        fm2!!.replace(R.id.container, second).commit()
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_project, container, false)
        view.recyclerView1.layoutManager = LinearLayoutManager(context, LinearLayout.VERTICAL, false)
        adapter = ProjectAdapter(this.projects)
        view.recyclerView1.adapter = adapter
        adapter!!.setOnClickListener(this)
        mostrarDatos()
        if (tipo == 0) {
            leerDatos()
        } else {
            leerDatos2()
        }
        view.fabtn.setOnClickListener {
            val mDialogView = LayoutInflater.from(context).inflate(R.layout.project_dialog, null)
            val mBuilder = AlertDialog.Builder(context).setView(mDialogView)
            val mAlertDialog = mBuilder.show()
            mDialogView.btnSendProyect.setOnClickListener {
                mAlertDialog.dismiss()
                val titulo = mDialogView.etxtTitle.text.toString()
                val descripcion = mDialogView.etxtDescripcion.text.toString()
                pro = Project(titulo, descripcion, escuela, obtenerFecha(), 0, email)
                if (tipo == 0) {
                    EnviarProyecto2()
                    leerDatos()
                } else {
                    EnviarProyecto()
                    leerDatos2()
                }
            }
            mDialogView.btncancel.setOnClickListener {
                mAlertDialog.dismiss()
            }
        }
        return view
    }

    private fun EnviarProyecto2() {

        val key = myRef2.push().key
        pro = Project(key!!, pro.titulo, pro.descripcion, pro.escuela, pro.fecha, 1, pro.email,"")
        myRef2.child(key).setValue(pro)
    }

    private fun mostrarDatos() {
        val sharedPreferences: SharedPreferences = this.activity!!.getSharedPreferences("Credenciales", Context.MODE_PRIVATE)
        escuela = sharedPreferences.getString("escuela", "escuela")
        tipo = sharedPreferences.getInt("tipo", 1)
        email = sharedPreferences.getString("email", "escuela")
    }

    private fun EnviarProyecto() {
        val key = myRef.push().key
        pro = Project(key!!, pro.titulo, pro.descripcion, pro.escuela, pro.fecha, pro.tipo, pro.email,"")
        myRef.child(key).setValue(pro)

    }

    private fun leerDatos() {
        myRef2.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Toast.makeText(context, " " + "No es posible actualizar los datos",
                        Toast.LENGTH_SHORT).show()
            }
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                projects.removeAll(projects)
                for (snapshot: DataSnapshot in dataSnapshot.children) {
                    val pjt: Project = snapshot.getValue(Project::class.java)!!
                    projects.add(pjt)
                }


            }
        })
        myRef.orderByChild("tipo").addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Toast.makeText(context, " " + "No es posible actualizar los datos",
                        Toast.LENGTH_SHORT).show()
            }
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (snapshot: DataSnapshot in dataSnapshot.children) {
                    val pjt: Project = snapshot.getValue(Project::class.java)!!
                        projects.add(pjt)
                }
                adapter!!.notifyDataSetChanged()
            }
        })
    }
    private fun leerDatos2() {
        myRef2.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Toast.makeText(context, " " + "No es posible actualizar los datos",
                        Toast.LENGTH_SHORT).show()
            }
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                projects.removeAll(projects)
                for (snapshot: DataSnapshot in dataSnapshot.children) {
                    val pjt: Project = snapshot.getValue(Project::class.java)!!


                  projects.add(pjt)
                }


            }
        })
        myRef.orderByChild("email").equalTo(email).addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Toast.makeText(context, " " + "No es posible actualizar los datos",
                        Toast.LENGTH_SHORT).show()
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for(snapshot: DataSnapshot in dataSnapshot.children) {
                    val pjt: Project = snapshot.getValue(Project::class.java)!!
                    Log.d("PROJECTFRAGMENT", "PRJ")
                   projects.add(pjt)

                }
                adapter!!.notifyDataSetChanged()
            }
        })

    }

    private fun obtenerFecha(): Int {
        val c = Calendar.getInstance()
        var f: String = "" + c.get(Calendar.YEAR)
        if (c.get(Calendar.MONTH) + 1 < 10) {
            f = f + "0" + (c.get(Calendar.MONTH) + 1)
        }else{
            f = f + (c.get(Calendar.MONTH) + 1)
        }
        if (c.get(Calendar.DAY_OF_MONTH) < 10) {
            f = f + "0" + c.get(Calendar.DAY_OF_MONTH)
        }else{
            f = f + c.get(Calendar.DAY_OF_MONTH)
        }
        return Integer.parseInt(f)

    }


}
