package com.example.davidblanco.projectod

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView

class ProjectAdapter(val projectList: MutableList<Project>,var lisFilter: MutableList<Project>) : RecyclerView.Adapter<ProjectAdapter.ViewHolder>(), View.OnClickListener, Filterable {
    constructor(projectList: MutableList<Project>):this(projectList,projectList)

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
        holder.msjfecha?.text = dateForrmat(projectList[position].fecha)
        holder.msjEscuela?.text = projectList[position].escuela
        if (projectList.get(position).tipo == 0) {
            holder.iPriorityLevel.setImageResource(R.drawable.ic_lens_white)
        } else if (projectList.get(position).tipo == 1) {
            holder.iPriorityLevel.setImageResource(R.drawable.ic_lens_red)
        } else if (projectList.get(position).tipo == 2) {
            holder.iPriorityLevel.setImageResource(R.drawable.ic_lens_yelow)
        } else if (projectList.get(position).tipo == 3) {
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
        val iPriorityLevel = itemView.findViewById<ImageView>(R.id.IpriorityLevel)
        val msjEscuela = itemView.findViewById<TextView>(R.id.txtSchoolC)
    }

    private fun dateForrmat(fecha: Int): String {
        val cadena = "" + fecha
        cadena.toCharArray()
        return cadena[6].toString() + cadena[7].toString() + "-" + cadena[4].toString() +
                cadena[5].toString() + "-" + cadena[0].toString() + cadena[1].toString() +
                cadena[2].toString() + cadena[3].toString()
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                var charString = constraint.toString()
                if (charString.isEmpty()) {
                    lisFilter = projectList
                } else {
                    var filteredList = mutableListOf<Project>()
                    for (p: Project in projectList) {
                        if (p.titulo.toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(p)
                        }
                    }
                    lisFilter = filteredList
                }
                var filterResults = FilterResults()
                filterResults.values = lisFilter
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                lisFilter = results!!.values as MutableList<Project>
                notifyDataSetChanged()
            }

        }
    }
}