package rtu.mid1.midtermprojecttictaktoe

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    var botСomplexity: String = "Medium"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setBotLevel("Medium")

        val startButton = findViewById<Button>(R.id.startButton)
        startButton.setOnClickListener {
            val nickname = findViewById<TextView>(R.id.nickname)
            val nicknameIntent = nickname.text.toString()
            val intent = Intent(this, NewGame::class.java)
            intent.putExtra("nickname", nicknameIntent)
            intent.putExtra("botComplexity", botСomplexity)
            startActivity(intent)

        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.exit -> {
                this.finish()
                return true
            }
            R.id.random -> {
                setBotLevel("Random")
                return true
            }
            R.id.medium -> {
                setBotLevel("Medium")
                return true
            }
            R.id.player -> {
                setBotLevel("Player")
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun setBotLevel(level: String) {
        botСomplexity = level
        val greetings = findViewById<TextView>(R.id.greeting)
        val originalText = greetings.text.toString()
        val newText = originalText.split(": ")[0] + ": " + botСomplexity
        greetings.text = newText
    }
}

