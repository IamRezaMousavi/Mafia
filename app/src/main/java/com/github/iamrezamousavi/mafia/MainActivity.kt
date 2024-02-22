package com.github.iamrezamousavi.mafia

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.iamrezamousavi.mafia.databinding.ActivityMainBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MainActivity : AppCompatActivity() {
    private val SP_ID = "shared_prefs"
    private val SP_RECYCLER_DATA_ID = "recycler_data"

    private lateinit var binding: ActivityMainBinding
    private lateinit var playerAdapter: PlayerAdapter
    private lateinit var playerList: ArrayList<Player>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.button1.setOnClickListener {
            startActivity(Intent(this, RoleActivity::class.java))
        }

        binding.textField.setEndIconOnClickListener {
            val name = binding.textField.editText?.text.toString()
            if (name.isNotEmpty()) {
                playerList.add(Player(name))
                playerAdapter.notifyItemInserted(playerList.lastIndex)
                binding.textField.editText?.setText("")
                Toast.makeText(this, name, Toast.LENGTH_SHORT).show()
            }
        }

        binding.mainToolBar.setNavigationOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }

        binding.mainToolBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menuItemSelectAll -> {
                    playerList.forEachIndexed { index, player ->
                        player.isChecked = true
                        playerAdapter.notifyItemChanged(index)
                    }
                    Toast.makeText(this, "Select All", Toast.LENGTH_SHORT).show()
                    true
                }

                else -> {
                    false
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        playerList = loadData()
        playerAdapter = PlayerAdapter(
            playerList,
            onSelect = { pos, isChecked ->
                Toast.makeText(this, "$pos is $isChecked", Toast.LENGTH_SHORT).show()
            },
            onDelete = { pos ->
                playerList.removeAt(pos)
                Toast.makeText(this, "Item $pos Deleted", Toast.LENGTH_SHORT).show()
            }
        )
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        binding.peopleList.layoutManager = layoutManager
        binding.peopleList.adapter = playerAdapter
    }

    override fun onPause() {
        super.onPause()
        saveData(playerList)
    }

    private fun saveData(list: ArrayList<Player>) {
        val sharedPreference = getSharedPreferences(SP_ID, Context.MODE_PRIVATE)
        val editor = sharedPreference.edit()
        val gson = Gson()
        val json = gson.toJson(list)
        editor.putString(SP_RECYCLER_DATA_ID, json)
        editor.apply()
    }

    private fun loadData(): ArrayList<Player> {
        val sharedPreference = getSharedPreferences(SP_ID, Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreference.getString(SP_RECYCLER_DATA_ID, null)
        val type = object : TypeToken<ArrayList<Player>>() {}.type
        return gson.fromJson(json, type) ?: ArrayList()
    }
}
