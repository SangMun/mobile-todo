package com.example.todo

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private val items = ArrayList<Todo>()
    private lateinit var adapter: TodoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.todoList)
        val editText = findViewById<EditText>(R.id.editText)
        val addButton = findViewById<Button>(R.id.addButton)

        adapter = TodoAdapter(items)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        // Todo 목록 불러오기 (GET)
        RetrofitClient.api.getTodos()
            .enqueue(object : Callback<List<Todo>> {
                override fun onResponse(
                    call: Call<List<Todo>>,
                    response: Response<List<Todo>>
                ) {
                    if (response.isSuccessful) {
                        items.clear()
                        response.body()?.let { items.addAll(it) }
                        adapter.notifyDataSetChanged()
                    }
                }

                override fun onFailure(call: Call<List<Todo>>, t: Throwable) {
                    t.printStackTrace()
                }
            })

        // Todo 추가 (POST)
        addButton.setOnClickListener {
            val text = editText.text.toString()
            if (text.isNotBlank()) {
                val newTodo = Todo(
                    id = 0,
                    text = text,
                    completed = false
                )

                RetrofitClient.api.addTodo(newTodo)
                    .enqueue(object : Callback<Void> {
                        override fun onResponse(
                            call: Call<Void>,
                            response: Response<Void>
                        ) {

                            items.add(newTodo)
                            adapter.notifyItemInserted(items.size - 1)
                            editText.text.clear()
                        }

                        override fun onFailure(call: Call<Void>, t: Throwable) {
                            t.printStackTrace()
                        }
                    })
            }
        }
    }
}
