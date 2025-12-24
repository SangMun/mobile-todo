package com.example.todo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TodoAdapter(
    private val items: ArrayList<Todo>
) : RecyclerView.Adapter<TodoAdapter.TodoViewHolder>() {

    inner class TodoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.todoText)
        val checkBox: CheckBox = itemView.findViewById(R.id.checkBox)
        val deleteBtn: Button = itemView.findViewById(R.id.deleteBtn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.todo_item, parent, false)
        return TodoViewHolder(view)
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        val todo = items[position]

        holder.textView.text = todo.text

        holder.checkBox.setOnCheckedChangeListener(null)
        holder.checkBox.isChecked = todo.completed

        holder.checkBox.setOnCheckedChangeListener { _, isChecked ->
            todo.completed = isChecked
        }

        holder.deleteBtn.setOnClickListener {
            val pos = holder.bindingAdapterPosition
            if (pos != RecyclerView.NO_POSITION) {
                RetrofitClient.api.deleteTodo(todo.id)
                    .enqueue(object : Callback<Void> {
                        override fun onResponse(call: Call<Void>, response: Response<Void>) {
                            items.removeAt(pos)
                            notifyItemRemoved(pos)
                        }

                        override fun onFailure(call: Call<Void>, t: Throwable) {
                            t.printStackTrace()
                        }
                    })
            }
        }
    }

    override fun getItemCount(): Int = items.size
}
