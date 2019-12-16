package com.example.agendackotlin

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import de.hdodenhof.circleimageview.CircleImageView

import java.util.ArrayList

class Adaptador(/*ArrayList<String> listaImagenes,*/listaContactos: ArrayList<Contacto>,
                                                    internal var context: Context
) : RecyclerView.Adapter<Adaptador.ViewHolder>() {
    internal var listaImagenes = ArrayList<String>()
    internal var listaNombres = ArrayList<String>()
    internal var listaContactos = ArrayList<Contacto>()

    init {
        //this.listaImagenes = listaImagenes;
        this.listaContactos = listaContactos
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_layout, parent, false)

        return ViewHolder(view)
    }

    fun pasarContacto() {


    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //  holder.iv.setImageURI(Uri.parse(listaImagenes.get(position)));


        if (listaContactos[position].foto == "null") {
            holder.iv.setImageResource(R.drawable.hombre)
        } else {
            //holder.iv.setImageResource(R.drawable.hombre);
            //Bitmap bm = (Bitmap) MediaStore.Images.Media.getBitmap(getContentResolver(),listaContactos.get(position).getFoto());
            holder.iv.setImageURI(Uri.parse(listaContactos[position].foto))


        }
        // holder.tv.setText(listaNombres.get(position));
        holder.tv.text = listaContactos[position].nombre
        holder.tvTelefono.text = listaContactos[position].telefonos[0]





        /*       holder.iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "Estoy en la posicion del ArrayList "+ position, Toast.LENGTH_LONG).show();
            }
        });
*/
        holder.itemView.setOnClickListener { view ->
            //listaImagenes.remove(position);
            /*
            Toast.makeText(
                view.context,
                "Estoy en la posicion del ArrayList $position",
                Toast.LENGTH_LONG
            ).show()
*/
                val intento = Intent(this.context,VistaContacto::class.java)
            intento.putExtra("pasacontacto", listaContactos.get(position))
                this.context.startActivity(intento)

        }


    }

    override fun getItemCount(): Int {
        return listaContactos.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        internal var iv: CircleImageView

        internal var tv: TextView
        internal var tvTelefono: TextView

        init {
            iv = itemView.findViewById(R.id.imagen_item)
            tv = itemView.findViewById(R.id.texto_item)
            tvTelefono = itemView.findViewById(R.id.tvTelefonoR)
        }
    }


}
