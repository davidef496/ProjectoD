package com.example.davidblanco.projectod

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_forgot_pass.*


class ForgotPass : AppCompatActivity(), View.OnClickListener  {
    private var mAuth: FirebaseAuth? = null
    override fun onClick(view: View?) {
        val email = txtEmailForgot.text.toString()
        val i = view!!.getId()
        if(i==R.id.btnRecuperar) {
        if(validar()){
            mAuth!!.sendPasswordResetEmail(email).addOnCompleteListener(this){
                task ->
                if(task.isSuccessful){
                    val i = Intent(applicationContext, ViewLogin::class.java)//lanza la siguiente actividad
                    this.finish()
                    startActivity(i)
                }else{
                    Toast.makeText(this, "Error al enviar el email",
                            Toast.LENGTH_SHORT).show()
                }
            }
        }else{
            Toast.makeText(this, "Error al enviar el email 222",
                    Toast.LENGTH_SHORT).show()
        }}
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_pass)
        mAuth = FirebaseAuth.getInstance()
    }
    private fun validar():Boolean{
        var valid = true

        val email = txtEmailForgot.text.toString()
        if (TextUtils.isEmpty(email)) {
            txtEmailForgot.setError("Required.")
            valid = false
        } else {
            txtEmailForgot.setError(null)
        }
        return valid
    }
}
