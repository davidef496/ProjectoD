package com.example.davidblanco.projectod


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_profile.view.*
import java.util.ArrayList


class ProfileFragment : Fragment(){
    private val database = FirebaseDatabase.getInstance()
    private val myRef = database.getReference("Usuarios")
    private var email:String=""
    private var user: ArrayList<User> = ArrayList<User>()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_profile, container, false)
        //.setEnabled(true);
       val bundle=getArguments()
        email=bundle!!.getString("Email")
       LeerDatos(view)
        //cargarDatos()
        return view
    }

    private fun cargarDatos() {
        val user=User("David Blanco",email,"Liceo Pablo Neruda","12345",0)
        myRef.push().setValue(user)
    }

    private fun LeerDatos(view: View) {

        val query=myRef.orderByChild("email").equalTo(email)
        query.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(dataSnapshot: DatabaseError) {
            }
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (snapshot: DataSnapshot in dataSnapshot.children) {
                     val us = snapshot.getValue(User::class.java)!!
                    user.add(us)

                }
                view.txtNameProfile.setText(user.get(0).nombre)
                view.txtSchoolProfile.setText(user.get(0).escuela)
                view.txtEmailProfile.setText(user.get(0).email)

            }
        })


    }


}
