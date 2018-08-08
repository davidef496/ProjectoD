package com.example.davidblanco.projectod

import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_stadistic.*
import java.util.*


class StadisticFragment : Fragment() {
    val database = FirebaseDatabase.getInstance()
    val myRef = database.getReference("ProyectosReady")
    var escuela = "";
    var tipo = 0;

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        mostrarDatos()
        if (tipo == 0) {
            datosAnuales()
            datosMes()
        } else {
            datosAnuales2()
            datosMes2()
        }
        return inflater.inflate(R.layout.fragment_stadistic, container, false)
    }

    private fun datosMes2() {
        val c = Calendar.getInstance()
        var contA = 0;
        var contM = 0;
        var contB = 0;
        var fechaI = "" + c.get(Calendar.YEAR)
        var fechaF = "" + c.get(Calendar.YEAR)
        if (c.get(Calendar.MONTH) + 1 < 10) {
            fechaI = fechaI + "0" + (c.get(Calendar.MONTH) + 1)
            fechaF = fechaF + "0" + (c.get(Calendar.MONTH) + 1)
        }
        val fechaI2 = Integer.parseInt(fechaI + "01")
        val fechaF2 = Integer.parseInt(fechaF + "31")
        myRef.orderByChild("escuela").equalTo(escuela).addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Toast.makeText(context, " " + "No es posible actualizar los datos",
                        Toast.LENGTH_SHORT).show()
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (snapshot: DataSnapshot in dataSnapshot.children) {
                    var pjt: Project = snapshot.getValue(Project::class.java)!!
                    if (pjt.fecha >= fechaI2 && pjt.fecha <= fechaF2) {
                        if (pjt.tipo == 1) {
                            contA++;
                        } else if (pjt.tipo == 2) {
                            contM++;
                        } else if (pjt.tipo == 3) {
                            contB++;
                        }
                    }
                }
                Toast.makeText(context, " " + "Datos actualizados" + contA,
                        Toast.LENGTH_SHORT).show()
                txtAltaMes.setText("Prioridad Alta: " + contA)
                txtMediaMes.setText("Prioridad Media: " + contM)
                txtBajaMes.setText("Prioridad Baja: " + contB)
                txtTotalMes.setText("Total: " + (contA + contB + contM))
            }
        })
    }

    private fun datosAnuales2() {
        val c = Calendar.getInstance()
        var contA = 0;
        var contM = 0;
        var contB = 0;
        val fechaI = Integer.parseInt("" + c.get(Calendar.YEAR) + "0101")
        val fechaF = Integer.parseInt("" + c.get(Calendar.YEAR) + "1231")
        myRef.orderByChild("escuela").equalTo(escuela).addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Toast.makeText(context, " " + "No es posible actualizar los datos",
                        Toast.LENGTH_SHORT).show()
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (snapshot: DataSnapshot in dataSnapshot.children) {
                    var pjt: Project = snapshot.getValue(Project::class.java)!!
                    if (pjt.fecha >= fechaI && pjt.fecha <= fechaF) {
                        if (pjt.tipo == 1) {
                            contA++;
                        } else if (pjt.tipo == 2) {
                            contM++;
                        } else if (pjt.tipo == 3) {
                            contB++;
                        }
                    }
                }
                txtAltaAño.setText("Prioridad Alta: " + contA)
                txtMediaAño.setText("Prioridad Media: " + contM)
                txtBajaAño.setText("Prioridad Baja: " + contB)
                txtTotalAño.setText("Total: " + (contA + contB + contM))
            }
        })
    }

    private fun datosMes() {
        val c = Calendar.getInstance()
        var contA = 0;
        var contM = 0;
        var contB = 0;
        var fechaI = "" + c.get(Calendar.YEAR)
        var fechaF = "" + c.get(Calendar.YEAR)
        if (c.get(Calendar.MONTH) + 1 < 10) {
            fechaI = fechaI + "0" + (c.get(Calendar.MONTH) + 1)
            fechaF = fechaF + "0" + (c.get(Calendar.MONTH) + 1)
        }
        val fechaI2 = Integer.parseInt(fechaI + "00").toDouble()
        val fechaF2 = Integer.parseInt(fechaF + "32").toDouble()
        myRef.orderByChild("fecha").startAt(fechaI2).endAt(fechaF2).addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Toast.makeText(context, " " + "No es posible actualizar los datos",
                        Toast.LENGTH_SHORT).show()
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (snapshot: DataSnapshot in dataSnapshot.children) {
                    var pjt: Project = snapshot.getValue(Project::class.java)!!
                    if (pjt.tipo == 1) {
                        contA++;
                    } else if (pjt.tipo == 2) {
                        contM++;
                    } else if (pjt.tipo == 3) {
                        contB++;
                    }
                }
                txtAltaMes.setText("Prioridad Alta: " + contA)
                txtMediaMes.setText("Prioridad Media: " + contM)
                txtBajaMes.setText("Prioridad Baja: " + contB)
                txtTotalMes.setText("Total: " + (contA + contB + contM))
            }
        })
    }

    private fun datosAnuales() {
        val c = Calendar.getInstance()
        var contA = 0;
        var contM = 0;
        var contB = 0;
        val fechaI = Integer.parseInt("" + c.get(Calendar.YEAR) + "0000").toDouble()
        val fechaF = Integer.parseInt("" + c.get(Calendar.YEAR) + "1332").toDouble()
        myRef.orderByChild("fecha").startAt(fechaI).endAt(fechaF).addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Toast.makeText(context, " " + "No es posible actualizar los datos",
                        Toast.LENGTH_SHORT).show()
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (snapshot: DataSnapshot in dataSnapshot.children) {
                    var pjt: Project = snapshot.getValue(Project::class.java)!!
                    if (pjt.tipo == 1) {
                        contA++;
                    } else if (pjt.tipo == 2) {
                        contM++;
                    } else if (pjt.tipo == 3) {
                        contB++;
                    }
                }
                txtAltaAño.setText("Prioridad Alta: " + contA)
                txtMediaAño.setText("Prioridad Media: " + contM)
                txtBajaAño.setText("Prioridad Baja: " + contB)
                txtTotalAño.setText("Total: " + (contA + contB + contM))
            }
        })
    }

    private fun mostrarDatos() {
        var sharedPreferences: SharedPreferences = this.activity!!.getSharedPreferences("Credenciales", Context.MODE_PRIVATE)
        escuela = sharedPreferences.getString("escuela", "escuela")
        tipo = sharedPreferences.getInt("tipo", 1)


    }


}
