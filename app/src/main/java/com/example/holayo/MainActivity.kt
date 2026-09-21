package com.example.holayo

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

data class Perfil(
    val nombre: String,
    val dato: String,
    val apodo: String?
)

class MainActivity : AppCompatActivity() {

    private val perfil = Perfil(
        nombre = "Lourdes",
        dato = "Estudiante de Ing. en informatica",
        apodo = "luli"
    )

    private var saludoFormal = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val tvSaludo = findViewById<TextView>(R.id.tvSaludo)
        val tvDato = findViewById<TextView>(R.id.tvDato)
        val btnSaludar = findViewById<Button>(R.id.btnSaludar)

        val comoLlamarme = perfil.apodo ?: perfil.nombre

        tvSaludo.text = "Hola, soy $comoLlamarme"
        tvDato.text = perfil.dato

        btnSaludar.setOnClickListener {
            saludoFormal = !saludoFormal
            tvSaludo.text = if (saludoFormal) {
                "Hola, soy $comoLlamarme"
            } else {
                "¡Buenas! Acá $comoLlamarme"
            }
        }
    }
}