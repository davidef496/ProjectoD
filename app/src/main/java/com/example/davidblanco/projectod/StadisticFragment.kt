package com.example.davidblanco.projectod

import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.support.v4.app.Fragment
import android.support.v4.content.PermissionChecker.checkSelfPermission
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_profile.view.*
import kotlinx.android.synthetic.main.fragment_stadistic.*
import kotlinx.android.synthetic.main.fragment_stadistic.view.*
import org.apache.poi.hssf.usermodel.HSSFCellStyle
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.apache.poi.hssf.util.HSSFColor
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.Sheet
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*


class StadisticFragment : Fragment(), View.OnClickListener {

    private val WRITE_EXTERNAL_STORAGE_CODE = 1
    val database = FirebaseDatabase.getInstance()
    val myRef = database.getReference("ProyectosReady")
    private var projects: ArrayList<Project> = ArrayList<Project>()
    var escuela = "";
    var tipo = 0;

    override fun onClick(v: View?) {
        if (validate()) {
           if(tipo==1){
                generarInforme()
            }else{
              generarInforme2()
           }
        }}


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == WRITE_EXTERNAL_STORAGE_CODE) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                saveToFile(requireContext())
            } else {
                Toast.makeText(context, "Es necesario otorgar permisos de almacenamiento", Toast.LENGTH_LONG)
            }
        }

    }

    private fun validate(): Boolean {
        var valid = true
        if (etxtDayI.text.isEmpty() || Integer.parseInt(etxtDayI.text.toString())>31 || Integer.parseInt(etxtDayI.text.toString())<1) {
            etxtDayI.setError("Required.")
            valid = false
        } else if (etxtMonthI.text.isEmpty() || Integer.parseInt(etxtMonthI.text.toString())>12 || Integer.parseInt(etxtMonthI.text.toString())<1) {
            etxtMonthI.setError("Required.")
            valid = false
        } else if (etxtYearI.text.isEmpty() || Integer.parseInt(etxtYearI.text.toString())<1900) {
            etxtYearI.setError("Required.")
            valid = false
        } else if (etxtDayF.text.isEmpty() || Integer.parseInt(etxtDayF.text.toString())>31 || Integer.parseInt(etxtDayF.text.toString())<1) {
            etxtDayF.setError("Required.")
            valid = false
        } else if (etxtMonthF.text.isEmpty()  || Integer.parseInt(etxtMonthF.text.toString())>12 || Integer.parseInt(etxtMonthF.text.toString())<1) {
            etxtMonthF.setError("Required.")
            valid = false
        } else if (etxtYearF.text.isEmpty() || Integer.parseInt(etxtYearF.text.toString())<1900) {
            etxtYearF.setError("Required.")
            valid = false
        }
        return valid

    }


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
        v.btnInforme.setOnClickListener(this)
        return v
    }

    private fun saveToFile(context: Context) {
        val wb = HSSFWorkbook()
        var c: Cell? = null

        //Cell style for header row
        val cs = wb.createCellStyle()
        cs.fillForegroundColor = HSSFColor.LIGHT_ORANGE.index
        cs.setFillPattern(HSSFCellStyle.ALT_BARS)
        //New Sheet
        var sheet1: Sheet? = null
        sheet1 = wb.createSheet("Proyectos")

        // Generate column headings
        val row = sheet1!!.createRow(0)
        c = row.createCell(0)
        c.setCellValue("Fecha")
        c.cellStyle = cs

        c = row.createCell(1)
        c.setCellValue("Prioridad")
        c.cellStyle = cs

        c = row.createCell(2)
        c.setCellValue("Titulo")
        c.cellStyle = cs

        c = row.createCell(3)
        c.setCellValue("Descripcion")
        c.cellStyle = cs

        c = row.createCell(4)
        c.setCellValue("Escuela")
        c.cellStyle = cs

        c = row.createCell(5)
        c.setCellValue("Email")
        c.cellStyle = cs

        c = row.createCell(6)
        c.setCellValue("Comentario")
        c.cellStyle = cs

        sheet1.setColumnWidth(0, 8 * 500)
        sheet1.setColumnWidth(1, 8 * 500)
        sheet1.setColumnWidth(2, 20 * 500)
        sheet1.setColumnWidth(3, 20 * 500)
        sheet1.setColumnWidth(4, 20 * 500)
        sheet1.setColumnWidth(5, 20 * 500)
        sheet1.setColumnWidth(6, 20 * 500)

        val cd = wb.createCellStyle()
        cd.borderTop = HSSFCellStyle.BORDER_THIN
        cd.borderBottom = HSSFCellStyle.BORDER_THIN
        cd.borderLeft = HSSFCellStyle.BORDER_THIN
        cd.borderRight = HSSFCellStyle.BORDER_THIN
        cd.wrapText = true

        for (i in projects.indices) {
            val row = sheet1!!.createRow(i + 1)
            // row.height=700
            c = row.createCell(0)
            c.setCellValue(dateFormat(projects[i].fecha))
            c.cellStyle = cd

            if (projects[i].tipo == 1) {
                c = row.createCell(1)
                c.setCellValue("Alta")
                c.cellStyle = cd
            }else if (projects[i].tipo == 2) {
                c = row.createCell(1)
                c.setCellValue("Media")
                c.cellStyle = cd
            }else if (projects[i].tipo == 3) {
                c = row.createCell(1)
                c.setCellValue("Baja")
                c.cellStyle = cd
            }else{
                c = row.createCell(1)
                c.setCellValue("Sin prioridad")
                c.cellStyle = cd
            }

            c = row.createCell(2)
            c.setCellValue(projects[i].titulo)
            c.cellStyle = cd

            c = row.createCell(3)
            c.setCellValue(projects[i].descripcion)
            c.cellStyle = cd

            c = row.createCell(4)
            c.setCellValue("" + projects[i].escuela)
            c.cellStyle = cd

            c = row.createCell(5)
            c.setCellValue(projects[i].email)
            c.cellStyle = cd

            c = row.createCell(6)
            c.setCellValue(projects[i].comentario)
            c.cellStyle = cd
        }
        val timeStap = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(System.currentTimeMillis())
        val fileName: String = "Mis Proyectos " + timeStap + ".xls"
        var os: FileOutputStream? = null
        try {
            val path: File = Environment.getExternalStorageDirectory()
            val dir: File = File("" + path + "/Mis Proyectos/")
            dir.mkdir()
            val file = File(dir, fileName)
            os = FileOutputStream(file)
            wb.write(os)
            Toast.makeText(context, "Guardado en: \n " + dir,
                    Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
        }
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
        myRef.orderByChild("escuela").equalTo(escuela).addListenerForSingleValueEvent(object : ValueEventListener {
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
        myRef.orderByChild("escuela").equalTo(escuela).addListenerForSingleValueEvent(object : ValueEventListener {
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
        myRef.orderByChild("fecha").startAt(fechaI2).endAt(fechaF2).addListenerForSingleValueEvent(object : ValueEventListener {
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
        myRef.orderByChild("fecha").startAt(fechaI).endAt(fechaF).addListenerForSingleValueEvent(object : ValueEventListener {
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

    private fun generarInforme() {
        var fechaI = "" + etxtYearI.text.toString()
        var fechaF = "" + etxtYearF.text.toString()
        // fecha inicial formato
        if (etxtMonthI.length() < 2) {
            fechaI = fechaI + "0" + (etxtMonthI.text.toString())
        } else {
            fechaI = fechaI + (etxtMonthI.text.toString())
        }
        if (etxtDayI.length() < 2) {
            fechaI = fechaI + "0" + (etxtDayI.text.toString())
        } else {
            fechaI = fechaI + (etxtDayI.text.toString())
        }
// fecha final formato
        if (etxtMonthF.length() < 2) {
            fechaF = fechaF + "0" + (etxtMonthF.text.toString())
        } else {
            fechaF = fechaF + (etxtMonthF.text.toString())
        }
        if (etxtDayF.length() < 2) {
            fechaF = fechaF + "0" + (etxtDayF.text.toString())
        } else {
            fechaF = fechaF + (etxtDayF.text.toString())
        }
//transformacion a numero de las fechas
        val fechaI2 = Integer.parseInt(fechaI).toDouble()
        val fechaF2 = Integer.parseInt(fechaF).toDouble()
        myRef.orderByChild("escuela").equalTo(escuela).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Toast.makeText(context, " " + "No es posible actualizar los datos",
                        Toast.LENGTH_SHORT).show()
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                projects.removeAll(projects)
                for (snapshot: DataSnapshot in dataSnapshot.children) {
                    var pjt: Project = snapshot.getValue(Project::class.java)!!
                    if(pjt.fecha>=fechaI2 && pjt.fecha<=fechaF2){
                    projects.add(pjt)
                }
                }
                validatePermission()
            }
        })

    }

    private fun validatePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(requireContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                var permissions: Array<String> = arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                requestPermissions(permissions, WRITE_EXTERNAL_STORAGE_CODE)
            } else {
                saveToFile(requireContext())
            }
        } else {
            saveToFile(requireContext())
        }
    }

    private fun dateFormat(fecha: Int): String {
        val cadena = "" + fecha
        val d = cadena.toCharArray()
        return cadena[6].toString() + cadena[7].toString() + "-" + cadena[4].toString() +
                cadena[5].toString() + "-" + cadena[0].toString() + cadena[1].toString() +
                cadena[2].toString() + cadena[3].toString();
    }

    private fun generarInforme2() {
        var fechaI = "" + etxtYearI.text.toString()
        var fechaF = "" + etxtYearF.text.toString()
        // fecha inicial formato
        if (etxtMonthI.length() < 2) {
            fechaI = fechaI + "0" + (etxtMonthI.text.toString())
        } else {
            fechaI = fechaI + (etxtMonthI.text.toString())
        }
        if (etxtDayI.length() < 2) {
            fechaI = fechaI + "0" + (etxtDayI.text.toString())
        } else {
            fechaI = fechaI + (etxtDayI.text.toString())
        }
// fecha final formato
        if (etxtMonthF.length() < 2) {
            fechaF = fechaF + "0" + (etxtMonthF.text.toString())
        } else {
            fechaF = fechaF + (etxtMonthF.text.toString())
        }
        if (etxtDayF.length() < 2) {
            fechaF = fechaF + "0" + (etxtDayF.text.toString())
        } else {
            fechaF = fechaF + (etxtDayF.text.toString())
        }
//transformacion a numero de las fechas
        val fechaI2 = Integer.parseInt(fechaI).toDouble()
        val fechaF2 = Integer.parseInt(fechaF).toDouble()
        myRef.orderByChild("fecha").startAt(fechaI2).endAt(fechaF2).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Toast.makeText(context, " " + "No es posible actualizar los datos",
                        Toast.LENGTH_SHORT).show()
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                projects.removeAll(projects)
                for (snapshot: DataSnapshot in dataSnapshot.children) {
                    var pjt: Project = snapshot.getValue(Project::class.java)!!
                        projects.add(pjt)
                }
                validatePermission()
            }
        })

    }
}

