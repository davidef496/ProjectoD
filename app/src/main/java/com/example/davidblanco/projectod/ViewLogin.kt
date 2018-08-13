package com.example.davidblanco.projectod

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.login_view.*
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.ArrayList


class ViewLogin : AppCompatActivity(), View.OnClickListener {
    private val TAG = "Login Activity"
    private var mAuth: FirebaseAuth? = null
    private var user: ArrayList<User> = ArrayList<User>()
    private val database = FirebaseDatabase.getInstance()
    private val myRef = database.getReference("Usuarios")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_view)
        val sharedPreferences: SharedPreferences = getSharedPreferences("Credenciales", Context.MODE_PRIVATE)
        if (sharedPreferences.getBoolean("sesion", false)) {
            val i = Intent(applicationContext, ViewNavigation::class.java)//lanza la siguiente actividad
            this.finish()
            startActivity(i)
        }
        mAuth = FirebaseAuth.getInstance()
        btnLogin.setOnClickListener(this)
        cBoxShow.setOnClickListener(this)
    }

    private fun logearUsuario() {
        mAuth!!.signInWithEmailAndPassword(txtUsuario.text.toString(), txtClave.text.toString())
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        guardarDatos()
                        Log.d(TAG, "signInWithEmail:success")
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithEmail:failure", task.exception)
                        pgsBar.visibility = View.GONE
                        Toast.makeText(this, "Autenticacion fallida",
                                Toast.LENGTH_SHORT).show()
                    }

                    // ...
                }
    }

    private fun guardarDatos() {
        val sharedPreferences: SharedPreferences = getSharedPreferences("Credenciales", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        val query = myRef.orderByChild("email").equalTo(txtUsuario.text.toString())
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(dataSnapshot: DatabaseError) {
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                user.removeAll(user)
                for (snapshot: DataSnapshot in dataSnapshot.children) {
                   val pjt = snapshot.getValue(User::class.java)!!
                    user.add(pjt)
                    editor.putString("email", pjt.email)
                    editor.putString("clave", pjt.password)
                    editor.putString("escuela", pjt.escuela)
                    editor.putString("nombre", pjt.nombre)
                    editor.putInt("tipo", pjt.tipo)
                    editor.putBoolean("sesion", true)
                    editor.commit()
                }
                val i = Intent(applicationContext, ViewNavigation::class.java)//lanza la siguiente actividad
                // i.putExtra("Email",txtUsuario.text.toString())
                startActivity(i)
                this@ViewLogin.finish()

            }
        })
    }

    override fun onClick(view: View?) {
        val i = view!!.getId()
        if (i == R.id.cBoxShow) {
            if (!cBoxShow.isChecked) {
                txtClave.setTransformationMethod(PasswordTransformationMethod.getInstance())
            } else {
                txtClave.setTransformationMethod(HideReturnsTransformationMethod.getInstance())
            }
        } else if (i == R.id.btnLogin) {
            if (validateForm()) {
                pgsBar.visibility = View.VISIBLE
                logearUsuario()
            }
        }
    }


    fun validateForm(): Boolean {
        var valid = true

        val email = txtUsuario.text.toString()
        if (TextUtils.isEmpty(email)) {
            txtUsuario.setError("Required.")
            valid = false
        } else {
            txtUsuario.setError(null)
        }

        val password = txtClave.text.toString()
        if (TextUtils.isEmpty(password)) {
            txtClave.setError("Required.")
            valid = false
        } else {
            txtClave.setError(null)
        }

        return valid
    }

    fun txtClick() {
        val i = Intent(applicationContext, ForgotPass::class.java)//lanza la siguiente actividad
        startActivity(i)
    }
}