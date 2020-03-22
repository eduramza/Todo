package com.eduramza.todoapp.ui.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.eduramza.todoapp.database.model.Todo
import com.eduramza.todoapp.database.repository.TodoRepository
import com.nhaarman.mockitokotlin2.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations
import java.lang.Exception

@ExperimentalCoroutinesApi
class MainViewModelTest{

    //1
    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    //2
    private val testDispatcher = TestCoroutineDispatcher()

    //3
    @Mock
    private lateinit var repository : TodoRepository
    @Mock
    private lateinit var observeTodo : Observer<List<Todo>>
    @Mock
    private lateinit var observerError: Observer<String>
    private lateinit var viewModel: MainViewModel

    //4
    @Before
    fun setup(){
        MockitoAnnotations.initMocks(this)
        viewModel = MainViewModel(repository)

        viewModel.observeTodos().observeForever(observeTodo)
        viewModel.observeException().observeForever(observerError)

        Dispatchers.setMain(testDispatcher)
    }

    //5
    @After
    fun tearDown(){
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun test_insertNewTodo() = testDispatcher.runBlockingTest {
        //given
        whenever(repository.insertTask(anyString())).thenReturn(mockedList())

        //action
        viewModel.insertNewTask(anyString())

        //then
        verify(observeTodo).onChanged(mockedList())
    }

    @Test
    fun test_deleteNewTodo() = testDispatcher.runBlockingTest {
        //given
        val todo = Todo(1, "FIRST", false)
        whenever(repository.deleteTodo(any())).thenReturn(mockedList())

        //action
        viewModel.deleteTodo(any())

        //then
        verify(observeTodo).onChanged(mockedList())
    }

    @Test
    fun test_markAsCompleteTodo() = testDispatcher.runBlockingTest {
        //given
        whenever(repository.markComplete(any())).thenReturn(mockedList())

        //action
        viewModel.updateTaskMarkComplete(any())

        //then
        verify(observeTodo).onChanged(mockedList())
    }

    @Test
    fun test_getAllTodo() = testDispatcher.runBlockingTest {
        //given
        whenever(repository.getAllTodos()).thenReturn(mockedList())

        //action
        viewModel.getAllTodos()

        //then
        verify(observeTodo).onChanged(mockedList())
    }

    @Test(expected = Exception::class)
    fun test_dontInsertNewTodo() = testDispatcher.runBlockingTest {
        //given
        doThrow(Exception::class).whenever(repository).insertTask(anyString())

        //action
        viewModel.insertNewTask(anyString())

    }

    private fun mockedList(): List<Todo>{
        return listOf(Todo(1, "Todo 1", false),
            Todo(2, "Todo 2", false),
            Todo(3, "Todo 3", true))
    }

}