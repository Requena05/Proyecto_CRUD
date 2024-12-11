package com.example.proyecto_crud

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.tasks.await

class Util {
    companion object {

        fun existeTaller(clubs: MutableList<Taller>, nombre: String): Boolean {
            return clubs.any { it.nombre!!.lowercase() == nombre.lowercase() }
        }
        fun obtenerListaTaller(db_ref: DatabaseReference, contexto: Context): MutableList<Taller> {
            val lista_Taller = mutableListOf<Taller>()
            db_ref.child("Motor").child("Talleres")
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        snapshot.children.forEach { club ->
                            val club_actual = club.getValue(Taller::class.java)
                            lista_Taller.add(club_actual!!)
                        }
                    }
                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(contexto, "Error al obtener los Talleres", Toast.LENGTH_SHORT)
                            .show()
                    }

                })
            return lista_Taller
        }
        fun escribirTaller(db_ref: DatabaseReference,id: String, taller: Taller) {
            db_ref.child("Motor").child("Talleres").child(id).setValue(taller)
        }
        suspend fun guardarLogo(almacen: StorageReference, id: String, Logo: Uri): String {
            var urlAlmacen: Uri
            urlAlmacen =
                almacen.child("Logos").child(id).putFile(Logo).await()
                    .storage.downloadUrl.await()

            return urlAlmacen.toString()
        }
        fun tostadaCorrutina(activity: AppCompatActivity, contexto: Context, texto: String){
            activity.runOnUiThread{
                Toast.makeText(contexto,texto,Toast.LENGTH_SHORT).show()
            }
        }

        fun animacion_carga(contexto: Context): CircularProgressDrawable {
            val animacion = CircularProgressDrawable(contexto)
            animacion.strokeWidth = 5f
            animacion.centerRadius = 30f
            animacion.start()

            return animacion
        }

        val transicion = DrawableTransitionOptions.withCrossFade(500)
        fun opcionesGlide(context: Context): RequestOptions {
            val options = RequestOptions()
                .placeholder(animacion_carga(context))
                .fallback(R.drawable.logoflashfix)
                .error(R.drawable.error_404)
            return options
        }


    }
}