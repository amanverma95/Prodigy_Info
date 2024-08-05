package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private GridLayout gridLayout;
    private Button[] buttons = new Button[9];
    private TextView textViewResult;
    private Button buttonReset;

    private char currentPlayer = 'X';
    private char[] board = new char[9];
    private boolean gameActive = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gridLayout = findViewById(R.id.gridLayout);
        textViewResult = findViewById(R.id.textViewResult);
        buttonReset = findViewById(R.id.buttonReset);

        for (int i = 0; i < gridLayout.getChildCount(); i++) {
            buttons[i] = (Button) gridLayout.getChildAt(i);
            buttons[i].setOnClickListener(new ButtonClickListener(i));
        }

        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetGame();
            }
        });
    }

    private class ButtonClickListener implements View.OnClickListener {
        int index;

        ButtonClickListener(int index) {
            this.index = index;
        }

        @Override
        public void onClick(View v) {
            if (gameActive && board[index] == '\0') {
                board[index] = currentPlayer;
                buttons[index].setText(String.valueOf(currentPlayer));
                buttons[index].setEnabled(false);

                if (checkWinner()) {
                    textViewResult.setText("Player " + currentPlayer + " wins!");
                    gameActive = false;
                } else if (isBoardFull()) {
                    textViewResult.setText("It's a draw!");
                    gameActive = false;
                } else {
                    currentPlayer = (currentPlayer == 'X') ? 'O' : 'X';
                }
            }
        }
    }

    private boolean checkWinner() {
        // Check rows, columns, and diagonals
        for (int i = 0; i < 3; i++) {
            if (board[i*3] == currentPlayer && board[i*3+1] == currentPlayer && board[i*3+2] == currentPlayer) {
                return true;
            }
            if (board[i] == currentPlayer && board[i+3] == currentPlayer && board[i+6] == currentPlayer) {
                return true;
            }
        }
        if (board[0] == currentPlayer && board[4] == currentPlayer && board[8] == currentPlayer) {
            return true;
        }
        if (board[2] == currentPlayer && board[4] == currentPlayer && board[6] == currentPlayer) {
            return true;
        }
        return false;
    }

    private boolean isBoardFull() {
        for (char c : board) {
            if (c == '\0') {
                return false;
            }
        }
        return true;
    }

    private void resetGame() {
        currentPlayer = 'X';
        gameActive = true;
        textViewResult.setText("");
        for (int i = 0; i < board.length; i++) {
            board[i] = '\0';
            buttons[i].setText("");
            buttons[i].setEnabled(true);
        }
    }
}
