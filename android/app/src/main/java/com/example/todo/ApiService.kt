package com.example.todo

import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @GET("api/todo")
    fun getTodos(): Call<List<Todo>>

    @POST("api/todo")
    fun addTodo(@Body todo: Todo): Call<Void>

    @DELETE("api/todo/{id}")
    fun deleteTodo(@Path("id") id: Int): Call<Void>
}
