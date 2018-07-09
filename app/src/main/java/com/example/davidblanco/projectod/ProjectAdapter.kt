package com.example.davidblanco.projectod

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Scroller
import android.widget.TextView
import kotlinx.android.synthetic.main.card_view_projects.view.*

class ProjectAdapter(val projectList: ArrayList<Project>) : RecyclerView.Adapter<ProjectAdapter.ViewHolder>(), View.OnClickListener {
    private var listener: View.OnClickListener? = null

    fun setOnClickListener(listener: View.OnClickListener) {
        this.listener = listener
    }

    override fun onClick(v: View?) {
        if (listener != null) {
           listener!!.onClick(v)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.msjtitulo?.text = projectList[position].titulo
        holder.msjdescripcion?.text = projectList[position].descripcion
        holder.msjfecha?.text = projectList[position].fecha
        holder.msjEscuela?.text = projectList[position].escuela
        if(projectList.get(position).tipo ==1){
            holder.iPriorityLevel.setImageResource(R.drawable.ic_lens_red)
        }else if(projectList.get(position).tipo ==2){
            holder.iPriorityLevel.setImageResource(R.drawable.ic_lens_yelow)
        }else if(projectList.get(position).tipo ==3){
            holder.iPriorityLevel.setImageResource(R.drawable.ic_lens_green)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.card_view_projects, parent, false)
        v.setOnClickListener(this)
        return ViewHolder(v); }

    override fun getItemCount(): Int {
        return projectList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val msjtitulo = itemView.findViewById<TextView>(R.id.txtNameP)
        val msjdescripcion = itemView.findViewById<TextView>(R.id.txtDescription)
        val msjfecha = itemView.findViewById<TextView>(R.id.txtFechaP)
        val iPriorityLevel=itemView.findViewById<ImageView>(R.id.IpriorityLevel)
        val msjEscuela=itemView.findViewById<TextView>(R.id.txtSchoolC)
    }
}