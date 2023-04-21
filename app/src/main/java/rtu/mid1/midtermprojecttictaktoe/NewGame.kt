package rtu.mid1.midtermprojecttictaktoe

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class NewGame : AppCompatActivity() {

    var turn: String = "player"
        set(value) {
            field = value
            if (value == "pc") {
                if (!isBoardFull()) {
                    val botComplexity = intent.getStringExtra("botComplexity")
                    val buttonArray = Array<Button>(9) { findViewById(resources.getIdentifier("button${it + 1}", "id", packageName)) }
                    if (botComplexity == "Medium") {
                        pcTurn(buttonArray)
                    } else if (botComplexity == "Random")  {
                        pcTurnRandom(buttonArray)
                    } else {
                        return
                    }
                }
            }
        }

    var gameBoard = arrayOf(
        charArrayOf(' ', ' ', ' '),
        charArrayOf(' ', ' ', ' '),
        charArrayOf(' ', ' ', ' ')
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        val intent = intent
        val botComplexity = intent.getStringExtra("botComplexity")
        val nicknameIntent = intent.getStringExtra("nickname")

        val nickname = findViewById<TextView>(R.id.nickname)

        val originalText = nickname.text.toString()
        val newText = originalText.replace("player", nicknameIntent.toString())
        if (botComplexity == "Player") {
            nickname.text = "Player 1 turn"
        } else {

            nickname.text = newText
        }

        val back = findViewById<Button>(R.id.back)
        back.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        val button1 = findViewById<Button>(R.id.button1)
        val button2 = findViewById<Button>(R.id.button2)
        val button3 = findViewById<Button>(R.id.button3)
        val button4 = findViewById<Button>(R.id.button4)
        val button5 = findViewById<Button>(R.id.button5)
        val button6 = findViewById<Button>(R.id.button6)
        val button7 = findViewById<Button>(R.id.button7)
        val button8 = findViewById<Button>(R.id.button8)
        val button9 = findViewById<Button>(R.id.button9)


        button1.setOnClickListener {
            makeMove(button1, 0, 0)
            button1.isEnabled = false
        }

        button2.setOnClickListener {
            makeMove(button2, 0, 1)
            button2.isEnabled = false
        }
        button3.setOnClickListener {
            makeMove(button3, 0, 2)
            button3.isEnabled = false
        }
        button4.setOnClickListener {
            makeMove(button4, 1, 0)
            button4.isEnabled = false
        }
        button5.setOnClickListener {
            makeMove(button5, 1, 1)
            button5.isEnabled = false
        }
        button6.setOnClickListener {
            makeMove(button6, 1, 2)
            button6.isEnabled = false
        }
        button7.setOnClickListener {
            makeMove(button7, 2, 0)
            button7.isEnabled = false
        }
        button8.setOnClickListener {
            makeMove(button8, 2, 1)
            button8.isEnabled = false
        }
        button9.setOnClickListener {
            makeMove(button9, 2, 2)
            button9.isEnabled = false
        }
    }

    fun pcTurnRandom(buttonArray: Array<Button>) {
        var row: Int
        var col: Int
        do {
            // select a random row and column
            row = (0 until 3).random()
            col = (0 until 3).random()
        } while (gameBoard[row][col] != ' ') // repeat until we find an empty cell
        // set the character "O" to the selected button
        makeMove(buttonArray[row * 3 + col], row, col)

        // Check if this is the last free cell
        if (isBoardFull()) {
            return
        }
    }
    fun isBoardFull(): Boolean {
        for (row in gameBoard) {
            for (cell in row) {
                if (cell == ' ') {
                    return false
                }
            }
        }
        return true
    }

    fun pcTurn(buttonArray: Array<Button>) {

        // Checking the possibility of winning on the next turn
        for (i in 0..2) {
            for (j in 0..2) {
                if (gameBoard[i][j] == ' ') {
                    gameBoard[i][j] = 'O'
                    if (checkForWin('O')) {
                        makeMove(buttonArray[i * 3 + j], i, j)
                        return
                    }
                    gameBoard[i][j] = ' '
                }
            }
        }

        // Checking the ability to block player xD
        for (i in 0..2) {
            for (j in 0..2) {
                if (gameBoard[i][j] == ' ') {
                    gameBoard[i][j] = 'X'
                    if (checkForWin('X')) {
                        makeMove(buttonArray[i * 3 + j], i, j)
                        gameBoard[i][j] = 'O'
                        return
                    }
                    gameBoard[i][j] = ' '
                }
            }
        }
        pcTurnRandom(buttonArray)
    }
    fun checkForWin(symbol: Char): Boolean {
        for (i in 0..2) {
            if (gameBoard[i][0] == symbol && gameBoard[i][1] == symbol && gameBoard[i][2] == symbol) {
                return true
            }
        }
        for (i in 0..2) {
            if (gameBoard[0][i] == symbol && gameBoard[1][i] == symbol && gameBoard[2][i] == symbol) {
                return true
            }
        }
        if (gameBoard[0][0] == symbol && gameBoard[1][1] == symbol && gameBoard[2][2] == symbol) {
            return true
        }
        if (gameBoard[0][2] == symbol && gameBoard[1][1] == symbol && gameBoard[2][0] == symbol) {
            return true
        }
        return false
    }

    fun makeMove(button: Button, i: Int, j: Int) {
        if (turn == "player") {
            button.setBackgroundResource(R.drawable.setx)
            button.backgroundTintList = ColorStateList.valueOf(Color.GREEN)
            gameBoard[i][j] = 'X'
        }
        //  turn PC
        else
        {
            button.setBackgroundResource(R.drawable.seto)
            button.backgroundTintList = ColorStateList.valueOf(Color.RED)
            gameBoard[i][j] = 'O'
            button.isEnabled = false
        }
        if (checkWinner() == 'X' || checkWinner() == 'O' || checkWinner() == '-') {
            val winner = findViewById<TextView>(R.id.winner)
            val botComplexity = intent.getStringExtra("botComplexity")
            val originalText = winner.text.toString()
            var newText: String
            if (checkWinner() == 'X') {
                if (botComplexity == "Player") {
                    newText = originalText.replace("...", "Player 1")
                } else {
                    newText = originalText.replace("...", "Player")
                }
            } else if (checkWinner() == 'O') {
                if (botComplexity == "Player") {
                    newText = originalText.replace("...", "Player 2")
                } else {
                    newText = originalText.replace("...", "PC")
                }

            } else {
                newText = originalText.replace("And winner is ...!", "DRAW...")
            }

            winner.visibility = View.VISIBLE
            winner.text = newText
            gameOver()
        }
        changeTurn()
    }
    fun gameOver () {
        val button1 = findViewById<Button>(R.id.button1)
        val button2 = findViewById<Button>(R.id.button2)
        val button3 = findViewById<Button>(R.id.button3)
        val button4 = findViewById<Button>(R.id.button4)
        val button5 = findViewById<Button>(R.id.button5)
        val button6 = findViewById<Button>(R.id.button6)
        val button7 = findViewById<Button>(R.id.button7)
        val button8 = findViewById<Button>(R.id.button8)
        val button9 = findViewById<Button>(R.id.button9)

        val buttons = arrayOf(button1, button2, button3, button4, button5, button6, button7, button8, button9)

        for (button in buttons) {
            button.isEnabled = false
        }

    }

    fun changeTurn() {
        val turnText = findViewById<TextView>(R.id.nickname)
        val botComplexity = intent.getStringExtra("botComplexity")
        if (turn == "player") {
            if (botComplexity == "Player") {
                turnText.setText("Player 2 turn")
            }
            turn = "pc"
        }
        //  turn PC
        else
        {
            if (botComplexity == "Player") {
                turnText.setText("Player 1 turn")
            }
            turn = "player"
        }
    }
    fun checkWinner(): Char {
        // Check rows
        for (i in 0..2) {
            if (gameBoard[i][0] != ' ' && gameBoard[i][0] == gameBoard[i][1] && gameBoard[i][0] == gameBoard[i][2]) {
                return gameBoard[i][0]
            }
        }

        // Check columns
        for (j in 0..2) {
            if (gameBoard[0][j] != ' ' && gameBoard[0][j] == gameBoard[1][j] && gameBoard[0][j] == gameBoard[2][j]) {
                return gameBoard[0][j]
            }
        }

        // Check diagonals
        if (gameBoard[0][0] != ' ' && gameBoard[0][0] == gameBoard[1][1] && gameBoard[0][0] == gameBoard[2][2]) {
            return gameBoard[0][0]
        }
        if (gameBoard[0][2] != ' ' && gameBoard[0][2] == gameBoard[1][1] && gameBoard[0][2] == gameBoard[2][0]) {
            return gameBoard[0][2]
        }

        // Check if game is tied (no winner)
        for (i in 0..2) {
            for (j in 0..2) {
                if (gameBoard[i][j] == ' ') {
                    // There is at least one empty space, so game is not tied yet
                    return ' '
                }
            }
        }
        // Game is tied (no winner)
        return '-'
    }
}