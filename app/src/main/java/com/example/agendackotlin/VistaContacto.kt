package com.example.agendackotlin

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast

import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_vista_contacto.*

class VistaContacto : AppCompatActivity() {
    internal var Rutas: Boolean? = null
    internal var enable: Boolean? = null
    internal var ruta = "d"
    internal var tel: String? = null
    internal var c: Contacto? = null
    internal lateinit var combo: Spinner
    internal lateinit var ivFotoContacto: CircleImageView
    internal lateinit var tvNombreContacto: TextView
    internal var tvTelefono: TextView? = null
    internal lateinit var tvEmail: TextView
    internal lateinit var tvDireccion: TextView
    internal lateinit var tvWebBlog: TextView
    internal lateinit var btllamar: ImageButton
    internal lateinit var btemail: ImageButton
    internal lateinit var btWebBlog: ImageButton
    internal lateinit var btDelete: ImageButton
    internal lateinit var btModificar: ImageButton
    internal lateinit var btMapa: ImageButton
    internal lateinit var btCoche: ImageButton
    internal lateinit var btWalk: ImageButton
    internal lateinit var btBici: ImageButton
    internal var context: Context? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vista_contacto)
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
         enable = prefs.getBoolean("ckMapa", true)
        val prefs2 = PreferenceManager.getDefaultSharedPreferences(this)
        Rutas = prefs2.getBoolean("ckruta", true)


        btBici = findViewById(R.id.ibbici)
        btCoche = findViewById(R.id.ibcoche)
        btWalk = findViewById(R.id.ibWalk)
        combo = findViewById(R.id.comboboxTelefono)
        btMapa = findViewById(R.id.btDireccion)
        //btModificar = findViewById(R.id.btModificar)
       // btDelete = findViewById(R.id.btDelete)
        btWebBlog = findViewById(R.id.btWebBLog)
        btemail = findViewById(R.id.btemail)
        btllamar = findViewById(R.id.btllamar)
        ivFotoContacto = findViewById(R.id.ivFotoContacto)
        tvNombreContacto = findViewById(R.id.tvNombreContacto)
        tvEmail = findViewById(R.id.tvEmail)
        tvDireccion = findViewById(R.id.tvDireccion)
        tvWebBlog = findViewById(R.id.tvWebBlog)



        btWalk.setImageResource(R.drawable.walk)
        btCoche.setImageResource(R.drawable.coche)
        btBici.setImageResource(R.drawable.ciclista)
       // btDelete.setImageResource(R.drawable.papelera)
        //btModificar.setImageResource(R.drawable.editar)
        ivFotoContacto.setImageResource(R.drawable.hombre)
        btllamar.setImageResource(R.drawable.llamar)
        btemail.setImageResource(R.drawable.email)
        btWebBlog.setImageResource(R.drawable.detective)
        btMapa.setImageResource(R.drawable.mapas)

        val fab : FloatingActionButton = findViewById(R.id.btmodificar)

        fab.setOnClickListener(View.OnClickListener {

           modificar()

        })


        val fab2 : FloatingActionButton = findViewById(R.id.btdelete)

        fab2.setOnClickListener(View.OnClickListener {

            delete()

        })




        c = intent.getSerializableExtra("pasacontacto") as Contacto
        if (c == null) {
            // hacer algo
            Toast.makeText(this, "Null", Toast.LENGTH_LONG).show() //Incorrecto
        } else {
            //Toast.makeText(this, ""+extras.getNombre().toString(), Toast.LENGTH_LONG).show(); //Correcto
            tvNombreContacto.text = c!!.nombre
            //System.out.println("TELEFONO " + c.Telefono);

            tvEmail.text = c!!.email
            //System.out.println(c.getEmail() + "EMAIL");
            tvDireccion.text = c!!.direccion
            tvWebBlog.text = c!!.webBlog

            if (c!!.foto == "null") {
            } else {
                ivFotoContacto.setImageURI(Uri.parse(c!!.foto))
            }


        }


        combo()

    }

    fun mapas(view: View) {
        if (enable==false) {
            Toast.makeText(this, "¡Mapas Desactivados!", Toast.LENGTH_LONG).show() //Correcto

        }else {
        if (Rutas!!) {
            val gmmIntentUri =
                Uri.parse("google.navigation:q=+" + tvDireccion.text.toString().trim { it <= ' ' } + "&mode=" + ruta)
            val i = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            //i.setData(Uri.parse("geo:0,0?q=+"+tvDireccion.getText().toString().trim()));
            startActivity(i)
        } else {

                val i = Intent(Intent.ACTION_VIEW)
                i.data = Uri.parse("geo:0,0?q=+" + tvDireccion.text.toString().trim { it <= ' ' })
                startActivity(i)
            }

        }
    }


    fun llamar(view: View) {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CALL_PHONE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            val i = Intent(Intent.ACTION_CALL)
            i.data = Uri.parse("tel:" + c!!.telefonos[combo.selectedItemPosition])
            if (i.resolveActivity(packageManager) != null) {
                startActivity(i)
            }


        } else {
            val toCall = ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.CALL_PHONE
            ) != PackageManager.PERMISSION_GRANTED
            //Show request permission

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (toCall) {
                    requestPermissions(arrayOf(Manifest.permission.CALL_PHONE), 2909)
                }
                llamar(btllamar)

            }


        }


    }

    fun email(view: View) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.data = Uri.parse("mailto")

        intent.type = "message/rfc822"
        intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(tvEmail.text.toString()))
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(Intent.createChooser(intent, "Elige un cliente de Email"))
        }
    }


    fun modificar() {

        intent =  Intent(this, ModificarContacto::class.java);
        intent.putExtra("pasarcontacto", c);
        startActivity(intent);
        finish();


    }


    fun delete() {
        MainActivity.admin = AdminSQLiteOpenHelper(this, "BASECONTACTOS.db", null, 1)
        MainActivity.BaseDeDatos = MainActivity.admin.writableDatabase
        var nrg: Long = -1
        var nrg2: Long = -1
        var nrg3: Long = -1

        if (MainActivity.BaseDeDatos != null) {
            nrg = MainActivity.BaseDeDatos.delete("CONTACTOS", "idContacto=" + c!!.contactoId, null)
                .toLong()
            nrg2 = MainActivity.BaseDeDatos.delete(
                "TELEFONOS",
                "Contactos_idContacto=" + c!!.contactoId,
                null
            ).toLong()
            nrg3 = MainActivity.BaseDeDatos.delete(
                "FOTOS",
                "Contactos_idContacto=" + c!!.contactoId,
                null
            ).toLong()
        }



        MainActivity.BaseDeDatos.close()
        finish()
    }


    fun bici(view: View) {
        if (Rutas!!) {
            ruta = "b"
            Toast.makeText(this, "Ruta en Bici seleccionada", Toast.LENGTH_LONG).show() //Correcto
        } else {
            Toast.makeText(this, "Rutas desactivadas", Toast.LENGTH_LONG).show() //Correcto
        }
    }

    fun coche(view: View) {
        if (Rutas!!) {
            ruta = "d"
            Toast.makeText(this, "Ruta en Coche seleccionada", Toast.LENGTH_LONG).show() //Correcto
        } else {
            Toast.makeText(this, "Rutas desactivadas", Toast.LENGTH_LONG).show() //Correcto
        }
    }

    fun walk(view: View) {
        if (Rutas!!) {
            ruta = "w"
            Toast.makeText(this, "Ruta a Pie seleccionada", Toast.LENGTH_LONG).show() //Correcto
        } else {
            Toast.makeText(this, "Rutas desactivadas", Toast.LENGTH_LONG).show() //Correcto
        }
    }

    fun combo() {
        val dataAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item, c!!.telefonos
        )

        combo.adapter = dataAdapter

    }

    fun buscar(view: View) {
        val uri = Uri.parse("https:" + tvWebBlog.text.toString())
        val intent = Intent(Intent.ACTION_VIEW, uri)
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        }

    }


}
