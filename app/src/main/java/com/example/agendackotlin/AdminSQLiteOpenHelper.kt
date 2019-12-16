package com.example.agendackotlin

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper


class AdminSQLiteOpenHelper(
    context: Context?,
    name: String?,
    factory: SQLiteDatabase.CursorFactory?,
    version: Int
) : SQLiteOpenHelper(context, name, factory, 1) {

    override fun onCreate(BaseDeDatos: SQLiteDatabase) {
        BaseDeDatos.execSQL("CREATE TABLE CONTACTOS (idContacto INTEGER PRIMARY KEY, Nombre VARCHAR(50), Direccion VARCHAR(50), WebBlog VARCHAR(100), Email VARCHAR(100))")
        BaseDeDatos.execSQL("CREATE TABLE TELEFONOS (idTelefono INTEGER PRIMARY KEY, Telefono VARCHAR(50), Contactos_idContacto INTEGER, FOREIGN KEY(Contactos_idContacto) REFERENCES CONTACTOS(idContacto))")
        BaseDeDatos.execSQL("CREATE TABLE FOTOS (idFoto INTEGER PRIMARY KEY, NomFichero VARCHAR(50), ObservFoto VARCHAR(255), Contactos_idContacto INTEGER,  FOREIGN KEY(Contactos_idContacto) REFERENCES CONTACTOS(idContacto))")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS " + "TELEFONOS")
        db.execSQL("DROP TABLE IF EXISTS FOTOS")
        db.execSQL("DROP TABLE IF EXISTS CONTACTOS")

        onCreate(db)
    }
}
