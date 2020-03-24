package com.eduramza.todoapp.database.repository

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.eduramza.todoapp.database.model.Todo
import com.eduramza.todoapp.database.source.AppDatabase
import com.eduramza.todoapp.database.source.TodoDao
import com.nhaarman.mockitokotlin2.any
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.*

import org.junit.Assert.*
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner

//1
@ExperimentalCoroutinesApi
@RunWith(RobolectricTestRunner::class)
class TodoRepositoryImplTest {

    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    //2
    @Mock
    private lateinit var database: AppDatabase
    @Mock
    private lateinit var dao: TodoDao
    private lateinit var repository: TodoRepositoryImpl

    private val testDispatcher = TestCoroutineDispatcher()

    //3
    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        val context: Context = InstrumentationRegistry.getInstrumentation().targetContext
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries().build()
        dao = database.todoDao()

        repository = TodoRepositoryImpl(dao)

        Dispatchers.setMain(testDispatcher)
    }

    //4
    @After
    fun tearDown() {
        database.close()
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun insertTask() = testDispatcher.runBlockingTest {

        //when
        repository.insertTask("New Task")
        val result = repository.getAllTodos()

        //then
        assertEquals(1, result.size)
        assertEquals(false, result[0].completed)
    }

    @Test
    fun getAllTodos() = testDispatcher.runBlockingTest {
        //given
        val items  = getBaseList()
        addItems(items)

        //when
        val result = repository.getAllTodos()

        //then
        assertEquals(items, result)
    }

    @Test
    fun deleteTodo() = testDispatcher.runBlockingTest {

        //given
        val items  = getBaseList()
        addItems(items)

        //when
        repository.deleteTodo(items[1])

        val result = repository.getAllTodos()

        //then
        assertEquals(2, result.size)
    }

    @Test
    fun markComplete() = testDispatcher.runBlockingTest {
        //given
        val items  = getBaseList()
        addItems(items)

        //when
        repository.markComplete(items[2])
        val result = repository.getAllTodos()

        //then
        assertEquals(true, result[2].completed)

    }

    private fun getBaseList() = listOf(Todo(1, "Task 1", false),
        Todo(2, "Task 3", false),
        Todo(3, "Task 4", false))

    private fun addItems(list: List<Todo>) = runBlockingTest{
        list.forEach {
            repository.insertTask(it.text)
        }
    }
}