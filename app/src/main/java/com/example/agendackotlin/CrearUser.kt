package com.example.agendackotlin

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.SharedPreferences
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.preference.PreferenceManager
import android.provider.MediaStore
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Spinner

import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider

import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar

import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.Date

class CrearUser : AppCompatActivity() {

    internal var valor: Int = 0
    internal var ACT_GALERIA = 1
    internal var listaTelefonos = ArrayList<String>()
    internal lateinit var mAbsolutePath: String

    internal var fotoGaleria : Uri? = null

    internal lateinit var btadd: ImageButton
    internal lateinit var etNombre: EditText
    internal lateinit var etDireccion: EditText
    internal lateinit var etWebBlog: EditText
    internal lateinit var etPhone: EditText
    internal lateinit var etMail: EditText
    internal lateinit var ivFoto: ImageView
    internal lateinit var combo: Spinner
    internal var outputStream: OutputStream? = null
    internal var enable: Boolean? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_user)


        combo = findViewById(R.id.comboboxCrear)
        etNombre = findViewById(R.id.eTNombre)
        etDireccion = findViewById(R.id.eTDireccion)
        etWebBlog = findViewById(R.id.eTWebBlog)
        etPhone = findViewById(R.id.etTelefono)
        etMail = findViewById(R.id.etMail)
        ivFoto = findViewById(R.id.ivFoto)
        btadd = findViewById(R.id.ibadddt)




        ivFoto.setImageResource(R.drawable.camara)
        val fab = findViewById<FloatingActionButton>(R.id.btfAcceptar)
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Guardado.", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()

            insertar()

            finish()
        }



        ivFoto.setOnClickListener { cogerfoto() }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (valor == 0) {
            if (requestCode == ACT_GALERIA && resultCode == Activity.RESULT_OK) {
                fotoGaleria = data!!.data
                try {
                    bm = MediaStore.Images.Media.getBitmap(contentResolver, fotoGaleria) as Bitmap
                    ivFoto.setImageBitmap(bm)
                    println(fotoGaleria)
                    valor = -1
                } catch (e: FileNotFoundException) {
                    e.printStackTrace()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }

        } else {
            if (requestCode == 2 && resultCode == Activity.RESULT_OK) {
                fotoGaleria = Uri.parse(mAbsolutePath)
                ivFoto.setImageURI(fotoGaleria)

            }


        }
    }

    fun add(view: View) {
        listaTelefonos.add(etPhone.text.toString())

        val dataAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item, listaTelefonos
        )
        etPhone.setText("")
        combo.adapter = dataAdapter

    }






    fun cogerfoto() {


            valor = 0
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            intent.type = "image/*"
            startActivityForResult(intent, ACT_GALERIA)


    }


    fun insertar() {

        admin = AdminSQLiteOpenHelper(this, "BASECONTACTOS.db", null, 1)
        BaseDeDatos = admin.writableDatabase
        val cv = ContentValues()

        val cv3 = ContentValues()

        cv.put("Nombre", etNombre.text.toString())
        cv.put("Direccion", etDireccion.text.toString())
        cv.put("WebBlog", etWebBlog.text.toString())
        cv.put("Email", etMail.text.toString())

        BaseDeDatos.insert("CONTACTOS", null, cv)
        val fila = BaseDeDatos.rawQuery("select * from CONTACTOS", null)
        fila.moveToLast()

        for (i in listaTelefonos.indices) {
            val cv2 = ContentValues()
            cv2.put("Telefono", listaTelefonos[i])
            cv2.put("Contactos_idContacto", fila.getInt(0))
            BaseDeDatos.insert("TELEFONOS", null, cv2)
        }


        cv3.put("NomFichero", "" + fotoGaleria)
        cv3.put("ObservFoto", "" + fotoGaleria)
        cv3.put("Contactos_idContacto", fila.getInt(0))

        BaseDeDatos.insert("FOTOS", null, cv3)
        println(fotoGaleria)

        BaseDeDatos.close()
        fotoGaleria = null

    }

    companion object {
        private var fotoGaleria: Uri? = null
        private var bm: Bitmap? = null

        lateinit var admin : AdminSQLiteOpenHelper
        lateinit var BaseDeDatos : SQLiteDatabase
    }
}
