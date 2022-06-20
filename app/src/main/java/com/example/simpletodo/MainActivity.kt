package com.example.simpletodo

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.apache.commons.io.FileUtils
import java.io.File
import java.io.IOException
import java.nio.charset.Charset

class MainActivity : AppCompatActivity() {

    private var listOfTasks = mutableListOf<String>()
    lateinit var adapter : TaskItemAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val onLongClickListener = object  : TaskItemAdapter.OnLongClickListener{
            @SuppressLint("NotifyDataSetChanged")
            override fun onItemLongClicked(position: Int) {
                listOfTasks.removeAt(position)
                adapter.notifyDataSetChanged()
                saveFile()
            }
        }

        loadFile()

        val listOfTasksView = findViewById<RecyclerView>(R.id.recycleView)
        adapter = TaskItemAdapter(listOfTasks, onLongClickListener)

        listOfTasksView.adapter = adapter
        listOfTasksView.layoutManager = LinearLayoutManager(this)

        val btn = findViewById<Button>(R.id.button)
        val addTextEdit = findViewById<EditText>(R.id.addTaskField)

        btn.setOnClickListener{
            val userInput = addTextEdit.text.toString()

            if (userInput != "") {
                listOfTasks.add(userInput)
                adapter.notifyItemInserted(listOfTasks.size - 1)
                addTextEdit.setText("")
                saveFile()
            }
        }
    }

    private fun getFile() : File{
        return  File(filesDir, "data.txt")
    }

    fun loadFile(){
        try {
            listOfTasks = FileUtils.readLines(getFile(), Charset.defaultCharset())
        }catch (ioException : IOException){
            ioException.printStackTrace()
        }
    }

    fun saveFile(){
        try {
            FileUtils.writeLines(getFile(), listOfTasks)
        }catch (ioException : IOException){
            ioException.printStackTrace()
        }
    }

}
