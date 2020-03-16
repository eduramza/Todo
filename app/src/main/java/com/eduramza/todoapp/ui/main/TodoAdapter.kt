package com.eduramza.todoapp.ui.main

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.RecyclerView
import com.eduramza.todoapp.R
import com.eduramza.todoapp.database.model.Todo
import kotlinx.android.synthetic.main.item_todo_list.view.*

class TodoAdapter(
    private val itemList: MutableList<Todo>,
    private val listener: AdapterListener,
    private val context: Context
) : RecyclerView.Adapter<TodoAdapter.ViewHolder>() {

    override fun getItemCount() = itemList.size

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(parent.inflate(R.layout.item_todo_list))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindViewHolder(itemList[position])
    }

    fun updateList(itemList: MutableList<Todo>) {
        this.itemList.clear()
        this.itemList.addAll(itemList)
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private lateinit var item: Todo

        fun bindViewHolder(item: Todo) {
            this.item = item

            itemView.tv_task.text = item.text

            configCompletedTodos(item)

            itemView.im_complete.setOnClickListener { listener.markWithComplete(item) }
            itemView.item_list_container.setOnLongClickListener{
                listener.deleteTodo(item)
                return@setOnLongClickListener true
            }

        }

        private fun configCompletedTodos(todo: Todo){
            if (todo.completed){
                DrawableCompat.setTint(
                    DrawableCompat.wrap(itemView.im_complete.drawable),
                    ContextCompat.getColor(context, R.color.colorCompleted)
                )
            } else {
                DrawableCompat.setTint(
                    DrawableCompat.wrap(itemView.im_complete.drawable),
                    ContextCompat.getColor(context, R.color.colorNotCompleted)
                )
            }
        }
    }

    interface AdapterListener{
        fun markWithComplete(todo: Todo)
        fun deleteTodo(todo: Todo)
    }
}

fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)
}