package com.example.agendackotlin

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.sqlite.SQLiteDatabase
import android.os.Build
import android.os.Bundle
import android.preference.PreferenceManager
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.widget.Toolbar


import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

import kotlinx.android.synthetic.main.activity_main.*
import java.sql.SQLOutput
import java.util.ArrayList

class MainActivity : AppCompatActivity() {


    internal var cam: Boolean? = null
    internal var listaTelefonos = ArrayList<String>()
    internal var listaTelefonosAUX = ArrayList<String>()
    internal var listaPaises = ArrayList<String>()



    val listaContactos = ArrayList<Contacto>()
    val ContactoAUX = ArrayList<Contacto>()


    override fun onResume() {
        super.onResume()
        comprobarPermisos()
        obtener()
        rellenarRecycler()

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        MyClass.Companion.setContext(this);
        val toolbar : Toolbar = findViewById(R.id.toolbar)

        val bt : ImageButton = findViewById(R.id.imageButton)
        bt.setImageResource(R.drawable.ic_settings_black_24dp)

        val fab : FloatingActionButton = findViewById(R.id.fab)
        fab.setImageResource(R.drawable.ic_add_black_24dp)
        fab.setOnClickListener(View.OnClickListener {

            crear_contacto()

        })





        CrearBase()


    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId


        if (id == R.id.action_settings) {
            val intent = Intent(this, Preferencia::class.java)
            startActivity(intent)
            return true
        }

        return super.onOptionsItemSelected(item)
    }
    fun preferencias (view: View){
        val intent = Intent(this, Preferencia::class.java)
        startActivity(intent)

    }

    fun permisosCamara(): Boolean {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        } else {
            val toPermis = ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
            //Show request permission

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (toPermis) {
                    requestPermissions(arrayOf(Manifest.permission.CAMERA), 2909)
                }
            }


        }
        return false
    }



    fun CrearBase() {
        var admin = AdminSQLiteOpenHelper(this, "BASECONTACTOS.db", null, 1)
       var  BaseDeDatos = admin.writableDatabase
        BaseDeDatos.close()
    }


    fun crear_contacto() {

        val intent = Intent(this, CrearUser::class.java)
        startActivity(intent)


    }


    fun comprobarPermisos(): Boolean {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        } else {
            val toPermis = ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
            //Show request permission

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (toPermis) {
                    requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 2909)
                }
            }


        }
        return false
    }


    fun obtener() {
        listaContactos.clear()
        var admin = AdminSQLiteOpenHelper(this, "BASECONTACTOS.db", null, 1)
        var  BaseDeDatos = admin.writableDatabase


        val fila = BaseDeDatos.rawQuery("select * from CONTACTOS", null)
        if (fila.count != 0) {
            fila.moveToFirst()

            val fila2 = BaseDeDatos.rawQuery(
                "select * from TELEFONOS where Contactos_idContacto =" + fila.getInt(0), null
            )
            if (fila2.count != 0) {
                fila2.moveToFirst()
                listaTelefonos.clear()
                listaTelefonos.add(fila2.getString(1))
                println("TELEFONO " + listaTelefonos[0])

                while (fila2.moveToNext()) {
                    listaTelefonos.add(fila2.getString(1))
                }
                println("EMAIL ON FILA = " + fila.getString(4))


                val fila3 = BaseDeDatos.rawQuery(
                    "select * from FOTOS where Contactos_idContacto =" + fila.getInt(0), null
                )

                if (fila3.count != 0) {
                    fila3.moveToFirst()

                    println("FOTO " + fila3.getString(1))

                    listaContactos.add(
                        Contacto(
                            fila.getInt(0),
                            fila.getString(1),
                            fila.getString(2),
                            fila.getString(3),
                            listaTelefonos,
                            fila.getString(4),
                            fila3.getString(1)
                        )
                    )
                    System.out.println("TELEFONO F" + listaContactos[0].telefonos)


                }
            }
        }


        while (fila.moveToNext()) {

            val fila2 = BaseDeDatos.rawQuery(
                "select * from TELEFONOS where Contactos_idContacto =" + fila.getInt(0), null
            )
            val fila3 = BaseDeDatos.rawQuery(
                "select * from FOTOS where Contactos_idContacto =" + fila.getInt(0), null
            )

            if (fila2.count != 0) {
                fila2.moveToFirst()
                //ContactoAUX = listaContactos;
                //listaTelefonos.clear();
                listaTelefonosAUX = ArrayList()
                //System.out.println("TELEFONO Ff" +listaContactos.get(0).getTelefonos());
                //System.out.println("TELEFONO Ff" +ContactoAUX.get(0).getTelefonos());
                listaTelefonosAUX.add(fila2.getString(1))

                while (fila2.moveToNext()) {
                    listaTelefonosAUX.add(fila2.getString(1))
                }


                if (fila3.count != 0) {
                    fila3.moveToFirst()
                    listaContactos.add(
                        Contacto(
                            fila.getInt(0),
                            fila.getString(1),
                            fila.getString(2),
                            fila.getString(3),
                            listaTelefonosAUX,
                            fila.getString(4),
                            fila3.getString(1)
                        )
                    )
                    // System.out.println("TELEFONO F" +listaContactos.get(0).getTelefonos());
                }


            }
        }

        BaseDeDatos.close()

        // System.out.println("TELEFONO 2" +listaContactos.get(0).getTelefonos());

    }

    fun rellenarRecycler() {
        val adaptador = Adaptador(/*listaImagenes,*/listaContactos, this)
        val lm = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        val rv: RecyclerView = findViewById(R.id.recyclerView)
        rv.layoutManager = lm
        rv.adapter = adaptador


    }

    companion object {

        lateinit var admin : AdminSQLiteOpenHelper
        lateinit var BaseDeDatos : SQLiteDatabase
    }

}
abstract class MyClass {

    companion object {

        private lateinit var context: Context

        fun getContext(): Context {

            return context
        }

        fun setContext(con: Context) {
            context=con
        }
    }
}
