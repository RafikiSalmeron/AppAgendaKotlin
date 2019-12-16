package com.example.agendackotlin

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
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
import android.widget.Toast

import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.google.android.material.floatingactionbutton.FloatingActionButton
import de.hdodenhof.circleimageview.CircleImageView

import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.Date

class ModificarContacto : AppCompatActivity() {

    internal var c: Contacto? = null
    internal lateinit var etNombre: EditText
    internal lateinit var etTelefono: EditText
    internal lateinit var etWebBlog: EditText
    internal lateinit var etDireccion: EditText
    internal lateinit var etEmail: EditText
    internal lateinit var listaTelefonos: ArrayList<String>
    internal lateinit var combo: Spinner
    internal lateinit var ivFotoContacto: CircleImageView

    internal lateinit var btModificar: ImageButton
    internal lateinit var btCancel: ImageButton



    internal var valor: Int = 0
    internal var ACT_GALERIA = 1

    internal lateinit var outputStream: OutputStream
    internal lateinit var mAbsolutePath: String
    internal var enable: Boolean? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_modificar_contacto)
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        enable = prefs.getBoolean("ckCam", false)

        combo = findViewById(R.id.comboboxModify)
        etDireccion = findViewById(R.id.etDireccionM)
        etEmail = findViewById(R.id.erEmailM)
        etNombre = findViewById(R.id.etNombreContactoM)
        etTelefono = findViewById(R.id.etTelefonoM)
        etWebBlog = findViewById(R.id.etWebBlogM)

        ivFotoContacto = findViewById(R.id.ivFotoContactoModificar)




        etTelefono.setOnClickListener { etTelefono.setText("") }


        ivFotoContacto.setOnClickListener { cogerfoto() }



        val fab : FloatingActionButton = findViewById(R.id.btmodificarcontacto)

        fab.setOnClickListener(View.OnClickListener {


            Modificar()
        })


        val fab2 : FloatingActionButton = findViewById(R.id.btcancelar)

        fab2.setOnClickListener(View.OnClickListener {

            cancel()

        })


        c = intent.getSerializableExtra("pasarcontacto") as Contacto
        if (c == null) {
            // hacer algo
            Toast.makeText(this, "Null", Toast.LENGTH_LONG).show() //Incorrecto
        } else {
            //Toast.makeText(this, ""+extras.getNombre().toString(), Toast.LENGTH_LONG).show(); //Correcto
            etNombre.setText(c!!.nombre)
            //System.out.println("TELEFONO " + c.Telefono);
            // etTelefono.setText(c.getTelefono());
            etEmail.setText(c!!.email)


            etTelefono.setOnClickListener { etTelefono.setText("") }


            //System.out.println(c.getEmail() + "EMAIL");
            etDireccion.setText(c!!.direccion)
            etWebBlog.setText(c!!.webBlog)
            listaTelefonos = c!!.telefonos
            if (c!!.foto == "null") {
                ivFotoContacto.setImageResource(R.drawable.hombre)
            } else {
                ivFotoContacto.setImageURI(Uri.parse(c!!.foto))
            }
            fotoGaleria = Uri.parse(c!!.foto)
            combo()
        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (valor == 0) {
            if (requestCode == ACT_GALERIA && resultCode == Activity.RESULT_OK) {
                fotoGaleria = data!!.data
                try {
                    bm = MediaStore.Images.Media.getBitmap(contentResolver, fotoGaleria) as Bitmap
                    ivFotoContacto.setImageBitmap(bm)
                    println(fotoGaleria)
                } catch (e: FileNotFoundException) {
                    e.printStackTrace()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }
            
        }


    }


    fun combo() {
        val dataAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item, listaTelefonos
        )

        combo.adapter = dataAdapter

    }

    fun cancel() {

        finish()
    }


    fun deleteT(view: View) {
        listaTelefonos.removeAt(combo.selectedItemPosition)
        combo()
    }

    fun addT(view: View) {
        listaTelefonos.add(etTelefono.text.toString())
        combo()
        etTelefono.setText("")

    }


    fun cogerfoto() {
                    valor = 0
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            intent.type = "image/*"
            startActivityForResult(intent, ACT_GALERIA)


    }

    private fun abrirCamara() {

        val tomarfotoIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        if (tomarfotoIntent.resolveActivity(packageManager) != null) {
            var photoFile: File? = null
            try {
                photoFile = createPhotoFile()

            } catch (ex: Exception) {
            }

            if (photoFile != null) {
                val photoUri =
                    FileProvider.getUriForFile(this, "com.example.agendacontactos", photoFile)
                tomarfotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
                valor = 1
                startActivityForResult(tomarfotoIntent, 2)

            }
        }
    }

    @Throws(IOException::class)
    private fun createPhotoFile(): File {
        val timestamp = SimpleDateFormat("yyyyMMdd HHmmss").format(Date())
        val imageFileName = "imagen$timestamp"

        val storageFile = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val photoFile = File.createTempFile(
            imageFileName,
            ".jpg",
            storageFile
        )

        mAbsolutePath = photoFile.absolutePath
        return photoFile
    }


    private fun createPhotoFileGallery() {
        val drawable = ivFotoContacto.drawable as BitmapDrawable
        val bitmap = drawable.bitmap

        val filepath = Environment.getExternalStorageDirectory()
        val dir2 = File(filepath.absolutePath + "/Pictures/")
        dir2.mkdir()
        val file = File(dir2, System.currentTimeMillis().toString() + ".jpg")
        try {
            outputStream = FileOutputStream(file)
        } catch (ex: FileNotFoundException) {
            ex.printStackTrace()
        }

        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        try {
            outputStream.flush()
        } catch (ex: IOException) {
            ex.printStackTrace()
        }

        try {
            outputStream.close()
        } catch (ex: IOException) {
            ex.printStackTrace()
        }

        mAbsolutePath = file.absolutePath
        fotoGaleria = Uri.parse(mAbsolutePath)
    }


    fun Modificar() {

        MainActivity.admin = AdminSQLiteOpenHelper(this, "BASECONTACTOS.db", null, 1)
        MainActivity.BaseDeDatos = MainActivity.admin.writableDatabase
        var nrg: Long = -1
        var nrg2: Long = -1
        var nrg3: Long = -1


        if (MainActivity.BaseDeDatos != null) {
            val cv = ContentValues()

            val cv3 = ContentValues()


            cv.put("Nombre", etNombre.text.toString().trim { it <= ' ' })
            cv.put("Direccion", etDireccion.text.toString().trim { it <= ' ' })
            cv.put("WebBlog", etWebBlog.text.toString().trim { it <= ' ' })
            cv.put("Email", etEmail.text.toString().trim { it <= ' ' })



            nrg2 = MainActivity.BaseDeDatos.delete(
                "TELEFONOS",
                "Contactos_idContacto=" + c!!.contactoId,
                null
            ).toLong()

            for (i in listaTelefonos.indices) {
                val cv2 = ContentValues()
                cv2.put("Telefono", listaTelefonos[i])
                cv2.put("Contactos_idContacto", c!!.contactoId)
                MainActivity.BaseDeDatos.insert("TELEFONOS", null, cv2)
            }


            cv3.put("NomFichero", "" + fotoGaleria!!)
            cv3.put("ObservFoto", "" + fotoGaleria!!)


            nrg = MainActivity.BaseDeDatos.update(
                "CONTACTOS",
                cv,
                "idContacto=" + c!!.contactoId,
                null
            ).toLong()

            nrg3 = MainActivity.BaseDeDatos.update(
                "FOTOS",
                cv3,
                "Contactos_idContacto=" + c!!.contactoId,
                null
            ).toLong()

        }
        Toast.makeText(this, "Numero de contactos actualizados$nrg", Toast.LENGTH_LONG)
            .show() //Correcto


        MainActivity.BaseDeDatos.close()
        finish()


    }

    companion object {
        private var fotoGaleria: Uri? = null
        private var bm: Bitmap? = null
    }

}
