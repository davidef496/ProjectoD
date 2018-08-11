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
import kotlinx.android.synthetic.main.fragment_stadistic.view.*
import java.util.*


class StadisticFragment : Fragment() {
    val database = FirebaseDatabase.getInstance()
    val myRef = database.getReference("ProyectosReady")
    var escuela = "";
    var tipo = 0;

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_stadistic, container, false)
        mostrarDatos()
        if (tipo == 0) {
            datosAnuales(v)
            datosMes(v)
        } else {
            datosAnuales2(v)
            datosMes2(v)
        }
        return v
    }

    private fun datosMes2(v: View) {
        val c = Calendar.getInstance()

        var fechaI = "" + c.get(Calendar.YEAR)
        var fechaF = "" + c.get(Calendar.YEAR)
        if (c.get(Calendar.MONTH) + 1 < 10) {
            fechaI = fechaI + "0" + (c.get(Calendar.MONTH) + 1)
            fechaF = fechaF + "0" + (c.get(Calendar.MONTH) + 1)
        } else {
            fechaI = fechaI + (c.get(Calendar.MONTH) + 1)
            fechaF = fechaF + (c.get(Calendar.MONTH) + 1)
        }
        val fechaI2 = Integer.parseInt(fechaI + "01")
        val fechaF2 = Integer.parseInt(fechaF + "31")
        myRef.orderByChild("escuela").equalTo(escuela).addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Toast.makeText(context, " " + "No es posible actualizar los datos",
                        Toast.LENGTH_SHORT).show()
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var contA = 0;
                var contM = 0;
                var contB = 0;
                var contN = 0;
                for (snapshot: DataSnapshot in dataSnapshot.children) {
                    var pjt: Project = snapshot.getValue(Project::class.java)!!
                    if (pjt.fecha >= fechaI2 && pjt.fecha <= fechaF2) {
                        if (pjt.tipo == 1) {
                            contA++;
                        } else if (pjt.tipo == 2) {
                            contM++;
                        } else if (pjt.tipo == 3) {
                            contB++;
                        } else {
                            contN++;
                        }
                    }
                }
                v.txtAltaMes.setText("Prioridad Alta: " + contA)
                v.txtMediaMes.setText("Prioridad Media: " + contM)
                v.txtBajaMes.setText("Prioridad Baja: " + contB)
                v.txtNullMes.setText("Sin Prioridad: " + contN)
                v.txtTotalMes.setText("Total: " + (contA + contB + contM + contN))
            }
        })
    }

    private fun datosAnuales2(v: View) {
        val c = Calendar.getInstance()

        val fechaI = Integer.parseInt("" + c.get(Calendar.YEAR) + "0101")
        val fechaF = Integer.parseInt("" + c.get(Calendar.YEAR) + "1231")
        Toast.makeText(context, " " + escuela,
                Toast.LENGTH_SHORT).show()
        myRef.orderByChild("escuela").equalTo(escuela).addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Toast.makeText(context, " " + "No es posible actualizar los datos",
                        Toast.LENGTH_SHORT).show()
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var contA = 0;
                var contM = 0;
                var contB = 0;
                var contN = 0;
                for (snapshot: DataSnapshot in dataSnapshot.children) {
                    var pjt: Project = snapshot.getValue(Project::class.java)!!
                    if (pjt.fecha >= fechaI && pjt.fecha <= fechaF) {
                        if (pjt.tipo == 1) {
                            contA++;
                        } else if (pjt.tipo == 2) {
                            contM++;
                        } else if (pjt.tipo == 3) {
                            contB++;
                        } else {
                            contN++;
                        }
                    }
                }
                v.txtAltaAño.setText("Prioridad Alta: " + contA)
                v.txtMediaAño.setText("Prioridad Media: " + contM)
                v.txtBajaAño.setText("Prioridad Baja: " + contB)
                v.txtNullAño.setText("Sin Prioridad: " + contN)
                v.txtTotalAño.setText("Total: " + (contA + contB + contM + contN))
            }
        })
    }

    private fun datosMes(v: View) {
        val c = Calendar.getInstance()

        var fechaI = "" + c.get(Calendar.YEAR)
        var fechaF = "" + c.get(Calendar.YEAR)
        if (c.get(Calendar.MONTH) + 1 < 10) {
            fechaI = fechaI + "0" + (c.get(Calendar.MONTH) + 1)
            fechaF = fechaF + "0" + (c.get(Calendar.MONTH) + 1)
        } else {
            fechaI = fechaI + (c.get(Calendar.MONTH) + 1)
            fechaF = fechaF + (c.get(Calendar.MONTH) + 1)
        }
        val fechaI2 = Integer.parseInt(fechaI + "00").toDouble()
        val fechaF2 = Integer.parseInt(fechaF + "32").toDouble()
        myRef.orderByChild("fecha").startAt(fechaI2).endAt(fechaF2).addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Toast.makeText(context, " " + "No es posible actualizar los datos",
                        Toast.LENGTH_SHORT).show()
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var contA = 0;
                var contM = 0;
                var contB = 0;
                var contN = 0;
                for (snapshot: DataSnapshot in dataSnapshot.children) {
                    var pjt: Project = snapshot.getValue(Project::class.java)!!
                    if (pjt.tipo == 1) {
                        contA++;
                    } else if (pjt.tipo == 2) {
                        contM++;
                    } else if (pjt.tipo == 3) {
                        contB++;
                    } else {
                        contN++;
                    }
                }
                v.txtAltaMes.setText("Prioridad Alta: " + contA)
                v.txtMediaMes.setText("Prioridad Media: " + contM)
                v.txtBajaMes.setText("Prioridad Baja: " + contB)
                v.txtNullMes.setText("Sin Prioridad: " + contN)
                v.txtTotalMes.setText("Total: " + (contA + contB + contM + contN))
            }
        })
    }

    private fun datosAnuales(v: View) {
        val c = Calendar.getInstance()

        val fechaI = Integer.parseInt("" + c.get(Calendar.YEAR) + "0000").toDouble()
        val fechaF = Integer.parseInt("" + c.get(Calendar.YEAR) + "1332").toDouble()
        myRef.orderByChild("fecha").startAt(fechaI).endAt(fechaF).addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Toast.makeText(context, " " + "No es posible actualizar los datos",
                        Toast.LENGTH_SHORT).show()
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var contA = 0;
                var contM = 0;
                var contB = 0;
                var contN = 0;
                for (snapshot: DataSnapshot in dataSnapshot.children) {
                    var pjt: Project = snapshot.getValue(Project::class.java)!!
                    if (pjt.tipo == 1) {
                        contA++;
                    } else if (pjt.tipo == 2) {
                        contM++;
                    } else if (pjt.tipo == 3) {
                        contB++;
                    } else {
                        contN++;
                    }
                }
                v.txtAltaAño.setText("Prioridad Alta: " + contA)
                v.txtMediaAño.setText("Prioridad Media: " + contM)
                v.txtBajaAño.setText("Prioridad Baja: " + contB)
                v.txtNullAño.setText("Sin Prioridad: " + contN)
                v.txtTotalAño.setText("Total: " + (contA + contB + contM + contN))
            }
        })
    }

    private fun mostrarDatos() {
        var sharedPreferences: SharedPreferences = this.activity!!.getSharedPreferences("Credenciales", Context.MODE_PRIVATE)
        escuela = sharedPreferences.getString("escuela", "escuela")
        tipo = sharedPreferences.getInt("tipo", 1)


    }


}
