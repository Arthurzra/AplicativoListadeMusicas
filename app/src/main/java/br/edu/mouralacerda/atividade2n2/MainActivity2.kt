package br.edu.mouralacerda.atividade2n2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity2 : AppCompatActivity() {

    val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        findViewById<Button>(R.id.btnSalvar).setOnClickListener {
            val nome = findViewById<EditText>(R.id.edtNome).text.toString()
            val ano = findViewById<EditText>(R.id.edtAno).text.toString()
            val album = findViewById<EditText>(R.id.edtAlbum).text.toString()

            if (nome.isNotBlank() && ano.isNotBlank() && album.isNotBlank()) {
            val dados = hashMapOf(
                "nome" to nome,
                "ano" to ano,
                "album" to album
            )

            db.collection("musica").add(dados)
                .addOnSuccessListener {
                    Toast.makeText(this, "Sucesso", Toast.LENGTH_SHORT).show()
                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Erro", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Por favor, preencha todos os campos", Toast.LENGTH_SHORT).show()
            }

        }

    }
}