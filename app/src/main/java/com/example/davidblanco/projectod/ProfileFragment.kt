package com.example.davidblanco.projectod

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
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_profile.view.*
import java.util.ArrayList


class ProfileFragment : Fragment(), View.OnClickListener {
    val database = FirebaseDatabase.getInstance()
    val myRef = database.getReference("Usuarios")
    var email:String="";
    private var user: ArrayList<User> = ArrayList<User>()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var view: View = inflater.inflate(R.layout.fragment_profile, container, false)
        //.setEnabled(true);
        view.btnEditar.setOnClickListener(this)
        view.btnGuardar.setOnClickListener(this)
        var bundle=getArguments()
        email=bundle!!.getString("Email")
       LeerDatos(view)
        //cargarDatos()
        return view
    }

    private fun cargarDatos() {
        var user=User("David Blanco",email,"Liceo Pablo Neruda","12345",0)
        myRef.push().setValue(user)
    }

    private fun LeerDatos(view: View) {
        var pjt:User;
        var query=myRef.orderByChild("email").equalTo(email)
        query.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(dataSnapshot: DatabaseError) {
            }
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (snapshot: DataSnapshot in dataSnapshot.children) {
                     pjt = snapshot.getValue(User::class.java)!!
                    user.add(pjt)

                }
                view.etxtName.setText(user.get(0).nombre)
                view.etxtSchool.setText(user.get(0).escuela)
                view.etxtPassword.setText("holaaaa")
            }
        })


    }

    override fun onClick(v: View?) {
        val i = v!!.id
        if (i == R.id.btnEditar) {
            btnGuardar.setVisibility(View.VISIBLE);
            btnEditar.setVisibility(View.INVISIBLE);
            etxtName.setEnabled(true);
            etxtSchool.setEnabled(true);
            etxtPassword.setEnabled(true);
            etxtPassword2.setEnabled(true);
        } else if (i == R.id.btnGuardar) {
            btnEditar.setVisibility(View.VISIBLE);
            btnGuardar.setVisibility(View.INVISIBLE);
            etxtName.setEnabled(false);
            etxtSchool.setEnabled(false);
            etxtPassword.setEnabled(false);
            etxtPassword2.setEnabled(false);
        }
    }

}
