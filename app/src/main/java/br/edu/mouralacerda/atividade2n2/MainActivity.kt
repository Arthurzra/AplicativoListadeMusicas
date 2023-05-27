package br.edu.mouralacerda.atividade2n2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import  android.widget.ArrayAdapter
import android.app.AlertDialog


class MainActivity : AppCompatActivity() {

    private lateinit var listView: ListView
    val db = Firebase.firestore
    private lateinit var adapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //botao pra mandar pra proxima activity
        val botaoNext = findViewById<Button>(R.id.btnNext)
        botaoNext.setOnClickListener {
            val intent = Intent(this, MainActivity2::class.java)
            startActivity(intent)
        }

        listView = findViewById(R.id.ListView)
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1)
        listView.adapter = adapter





        listView.setOnItemLongClickListener { parent, view, position, id ->
            val selectedItem = adapter.getItem(position)
            val alertDialog = AlertDialog.Builder(this)
            alertDialog.setMessage("Deseja excluir este item?")
            alertDialog.setPositiveButton("Sim") { _, _ ->
                deleteItem(selectedItem!!)
            }
            alertDialog.setNegativeButton("Não", null)
            alertDialog.create().show()
            true
        }


    }

    private fun fetchData() {
        db.collection("musica")
            .get()
            .addOnSuccessListener { result ->
                adapter.clear() // Limpar a lista antes de adicionar os novos itens
                for (document in result) {
                    val nome = document.getString("nome") ?: ""
                    val ano = document.getString("ano") ?: ""
                    val album = document.getString("album") ?: ""
                    val item = "$nome - $ano - $album"
                    adapter.add(item)
                }
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Erro ao obter dados: $exception", Toast.LENGTH_SHORT).show()
            }
    }



    private fun deleteItem(item: String) {
        val itemSplit = item.split(" - ")
        val nome = itemSplit[0]
        val ano = itemSplit[1]
        val album = itemSplit[2]

        db.collection("musica")
            .whereEqualTo("nome", nome)
            .whereEqualTo("ano", ano)
            .whereEqualTo("album", album)
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    document.reference.delete()
                }
                adapter.remove(item)
                adapter.notifyDataSetChanged()
                Toast.makeText(this, "Item excluído com sucesso", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Erro ao excluir item: $exception", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onResume() {
        super.onResume()
        fetchData()
    }

}