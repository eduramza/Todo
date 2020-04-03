package com.eduramza.todoapp.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.eduramza.todoapp.R
import com.eduramza.todoapp.ViewModelFactory
import com.eduramza.todoapp.database.model.Todo
import com.eduramza.todoapp.database.repository.TodoRepository
import com.eduramza.todoapp.database.repository.TodoRepositoryImpl
import com.eduramza.todoapp.database.source.AppDatabase
import kotlinx.android.synthetic.main.main_fragment.*
import leakcanary.AppWatcher

class MainFragment : Fragment(), TodoAdapter.AdapterListener {

    companion object {
        fun newInstance() = MainFragment()
    }


    private lateinit var mainViewModel: MainViewModel
    private lateinit var adapter: TodoAdapter
    private lateinit var todoRepositoryImpl : TodoRepository

    lateinit var db : AppDatabase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initDB()
        todoRepositoryImpl =
            TodoRepositoryImpl(
                db.todoDao()
            )

        val factory = ViewModelFactory(todoRepositoryImpl)
        mainViewModel = ViewModelProvider(this, factory).get(MainViewModel::class.java)

        setupList()
        observer()
        fab_add.setOnClickListener { openDialogAddTodo() }

        AppWatcher.objectWatcher.watch(this, "Main Fargment was detached")
    }

    private fun initDB(){
        db = context.let {
            AppDatabase.invoke(
                it!!
            )
        }
    }

    private fun setupList(){
        adapter = TodoAdapter(mutableListOf(), this, context!!)
        rv_todo_list.layoutManager = LinearLayoutManager(context)
        rv_todo_list.adapter = adapter
    }

    private fun observer(){
        mainViewModel.getAllTodos()
        mainViewModel.observeTodos().observe(viewLifecycleOwner, Observer{
            adapter.updateList(it as MutableList<Todo>)
        })
        mainViewModel.observeException().observe(viewLifecycleOwner, Observer {
            if (it != null){
                tv_error.visibility = VISIBLE
                rv_todo_list.visibility = GONE
                fab_add.visibility = GONE
            }
        })

    }

    override fun deleteTodo(todo: Todo) {
        openDialog(todo)
    }

    private fun openDialog(todo: Todo){
        val dialog = AlertDialog.Builder(context!!)
        dialog.setTitle("Delete Todo")
        dialog.setMessage("Do you want to delete this task?")
        dialog.setPositiveButton("Delete") { _, _ ->
            mainViewModel.deleteTodo(todo)
        }
        dialog.setNegativeButton("Cancel") { _, _ ->  null}
        dialog.show()
    }

    private fun openDialogAddTodo(){
        val dialog = AlertDialog.Builder(context!!)
        dialog.setTitle("Add New Todo")

        //config edit
        val edit = EditText(context)
        edit.hint = "New Todo Name"
        val lp = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT)
        lp.setMargins(30, 0, 30, 0)
        edit.layoutParams = lp

        dialog.setView(edit)
        dialog.setPositiveButton("Save"){ _, _ ->
            mainViewModel.insertNewTask(edit.text.toString())
        }
        dialog.setNegativeButton("Cancel") { _, _ ->  null}

        dialog.show()
    }

    override fun markWithComplete(todo: Todo) {
        mainViewModel.updateTaskMarkComplete(todo)
    }

    override fun onDestroy() {
        rv_todo_list.adapter = null
        super.onDestroy()
    }

    override fun onPause() {
        rv_todo_list.adapter = null
        super.onPause()
    }

}
