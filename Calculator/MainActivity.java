package com.example.calculator;


import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

public class MainActivity extends AppCompatActivity {

    private TextView solutionTextView, resultTextView;
    private StringBuilder input = new StringBuilder();
    private boolean lastNumeric = false;
    private boolean stateError = false;
    private boolean lastDot = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        solutionTextView = findViewById(R.id.solution_textview);
        resultTextView = findViewById(R.id.result_textview);

        setNumericOnClickListener();
        setOperatorOnClickListener();
    }

    private void setNumericOnClickListener() {
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MaterialButton button = (MaterialButton) v;
                if (stateError) {
                    solutionTextView.setText(button.getText());
                    stateError = false;
                } else {
                    solutionTextView.append(button.getText());
                }
                lastNumeric = true;
                lastDot = false;
            }
        };

        findViewById(R.id.button0).setOnClickListener(listener);
        findViewById(R.id.button1).setOnClickListener(listener);
        findViewById(R.id.button2).setOnClickListener(listener);
        findViewById(R.id.button3).setOnClickListener(listener);
        findViewById(R.id.button4).setOnClickListener(listener);
        findViewById(R.id.button5).setOnClickListener(listener);
        findViewById(R.id.button6).setOnClickListener(listener);
        findViewById(R.id.button7).setOnClickListener(listener);
        findViewById(R.id.button8).setOnClickListener(listener);
        findViewById(R.id.button9).setOnClickListener(listener);
        findViewById(R.id.button_dot).setOnClickListener(listener);
    }

    private void setOperatorOnClickListener() {
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lastNumeric && !stateError) {
                    MaterialButton button = (MaterialButton) v;
                    solutionTextView.append(button.getText());
                    lastNumeric = false;
                    lastDot = false;
                }
            }
        };

        findViewById(R.id.button_plus).setOnClickListener(listener);
        findViewById(R.id.button_minus).setOnClickListener(listener);
        findViewById(R.id.button_multiply).setOnClickListener(listener);
        findViewById(R.id.button_divide).setOnClickListener(listener);
        findViewById(R.id.button_openBracket).setOnClickListener(listener);
        findViewById(R.id.button_closeBracket).setOnClickListener(listener);

        findViewById(R.id.button_C).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                solutionTextView.setText("");
                resultTextView.setText("");
                lastNumeric = false;
                stateError = false;
                lastDot = false;
                input.setLength(0);
            }
        });

        findViewById(R.id.button_AC).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (solutionTextView.getText().length() > 0) {
                    CharSequence currentText = solutionTextView.getText();
                    solutionTextView.setText(currentText.subSequence(0, currentText.length() - 1));
                }
            }
        });

        findViewById(R.id.button_equals).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onEqual();
            }
        });
    }

    private void onEqual() {
        if (lastNumeric && !stateError) {
            String text = solutionTextView.getText().toString();
            try {
                double result = evaluate(text);
                resultTextView.setText(Double.toString(result));
                lastDot = true;
            } catch (Exception ex) {
                resultTextView.setText("Error");
                stateError = true;
                lastNumeric = false;
            }
        }
    }

    private double evaluate(String expression) {
        return new Object() {
            int pos = -1, ch;

            void nextChar() {
                ch = (++pos < expression.length()) ? expression.charAt(pos) : -1;
            }

            boolean eat(int charToEat) {
                while (ch == ' ') nextChar();
                if (ch == charToEat) {
                    nextChar();
                    return true;
                }
                return false;
            }

            double parse() {
                nextChar();
                double x = parseExpression();
                if (pos < expression.length()) throw new RuntimeException("Unexpected: " + (char) ch);
                return x;
            }

            double parseExpression() {
                double x = parseTerm();
                for (; ; ) {
                    if (eat('+')) x += parseTerm();
                    else if (eat('-')) x -= parseTerm();
                    else return x;
                }
            }

            double parseTerm() {
                double x = parseFactor();
                for (; ; ) {
                    if (eat('*')) x *= parseFactor();
                    else if (eat('/')) x /= parseFactor();
                    else return x;
                }
            }

            double parseFactor() {
                if (eat('+')) return parseFactor();
                if (eat('-')) return -parseFactor();

                double x;
                int startPos = this.pos;
                if (eat('(')) {
                    x = parseExpression();
                    eat(')');
                } else if ((ch >= '0' && ch <= '9') || ch == '.') {
                    while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
                    x = Double.parseDouble(expression.substring(startPos, this.pos));
                } else {
                    throw new RuntimeException("Unexpected: " + (char) ch);
                }

                return x;
            }
        }.parse();
    }
}
