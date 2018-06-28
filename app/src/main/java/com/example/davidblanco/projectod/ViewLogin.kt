package com.example.davidblanco.projectod

import android.content.Intent
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
import android.widget.CompoundButton
import kotlinx.android.synthetic.main.nav_header_view_navigation.*


class ViewLogin :AppCompatActivity(), View.OnClickListener {
    private var TAG="Login Activity---->"
    private var mAuth: FirebaseAuth? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_view)
        mAuth = FirebaseAuth.getInstance()
        btnLogin.setOnClickListener(this)
        cBoxShow.setOnClickListener(this)
    }
    private fun LogearUsuario(){
    mAuth!!.signInWithEmailAndPassword(txtUsuario.text.toString(), txtClave.text.toString())
    .addOnCompleteListener(this) { task ->
        if (task.isSuccessful) {
            Log.d(TAG, "signInWithEmail:success")
            val user = mAuth!!.getCurrentUser()
            val i = Intent(applicationContext, ViewNavigation::class.java)//lanza la siguiente actividad
            i.putExtra("Email",txtUsuario.text.toString())
            this.finish()
            startActivity(i)
        } else {
            // If sign in fails, display a message to the user.
            Log.w(TAG, "signInWithEmail:failure", task.exception)
            pgsBar.visibility=View.GONE
            Toast.makeText(this, "Autenticacion fallida",
                    Toast.LENGTH_SHORT).show()
        }

        // ...
    }
}
    override fun onClick(view: View?) {
        val i = view!!.getId()
      if(i==R.id.cBoxShow) {
            if (!cBoxShow.isChecked) {
                txtClave.setTransformationMethod(PasswordTransformationMethod.getInstance());
            } else {
                txtClave.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            }
         }else if(i==R.id.btnLogin){
        if(validateForm()) {
            pgsBar.visibility=View.VISIBLE
            LogearUsuario()
        }}}


    fun validateForm():Boolean {
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
    fun txtClick(view: View?){
        val i = Intent(applicationContext, ForgotPass::class.java)//lanza la siguiente actividad
        startActivity(i)
    }
}