package com.google.engedu.ghost;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;


public class GhostActivity extends AppCompatActivity {
    private static final String COMPUTER_TURN = "Computer's turn";
    private static final String USER_TURN = "Your turn";
    private GhostDictionary dictionary;
    private boolean userTurn = false;
    private Random random = new Random();
    private String wordFragment = "";
    private TextView wordTextView;
    private TextView tag;
    InputStream input;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ghost);
        wordTextView = (TextView) findViewById(R.id.ghostText);
        tag = (TextView) findViewById(R.id.gameStatus);
        try {
            input = getAssets().open("words.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            dictionary = new SimpleDictionary(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
        onStart(null);

    }

   // @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode >= 29 && keyCode <= 54) {
            wordFragment = wordFragment.concat(event.getDisplayLabel() + "");
            wordFragment = wordFragment.toLowerCase();
            wordTextView.setText(wordFragment);

            computerTurn();
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ghost, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void computerTurn() {
        TextView tag = (TextView) findViewById(R.id.gameStatus);
        userTurn = false;
        // Do computer turn stuff then make it the user's turn again
        if (dictionary.isWord(wordFragment.toLowerCase())) {
            tag.setText("COMPUTER WON");
        } else {
            String nextWord = dictionary.getAnyWordStartingWith(wordFragment.toLowerCase());

            if (nextWord == null) {
                if (wordFragment.length() >= 4)

                    tag.setText("Computer challenged you...COMPUTER WON");

               else {
                 //  wordFragment = wordFragment + "p";
                  // wordTextView.setText(wordFragment);
                    tag.setText("COMPUTER WON");

                   }
            } else {
                wordFragment = nextWord.substring(0, wordFragment.length() + 1);
                wordTextView.setText(wordFragment);
                userTurn = true;
                tag.setText(USER_TURN);
            }
        }
    }


    public void onChallenge(View view) {

        if (wordFragment.length() >= 4) {
            if (dictionary.isWord(wordFragment)) {
                tag.setText("Valid word... YOU WON");

            } else {
                String nextWord = dictionary.getAnyWordStartingWith(wordFragment);
                if (nextWord == null)
                    tag.setText("YOU WON");
                else
                    tag.setText(nextWord + "  YOU LOSE");
            }
        } else
            tag.setText("Word length too small");
    }



    /**
     * Handler for the "Reset" button.
     * Randomly determines whether the game starts with a user turn or a computer turn.
     *
     * @param view
     * @return true
     */
 /*  public void whenrestart(View view) {
        wordFragment="";
        Random r=new Random();
        userTurn = r.nextBoolean();
        TextView text = (TextView) findViewById(R.id.ghostText);
        text.setText("");
        TextView label = (TextView) findViewById(R.id.gameStatus);
        if (userTurn) {
            label.setText("New Round... "+USER_TURN);
        } else {
            label.setText("New Round... "+COMPUTER_TURN);
            computerTurn();
        }
    }
*/
    public boolean onStart(View view) {
        userTurn = random.nextBoolean();
        TextView text = (TextView) findViewById(R.id.ghostText);
        text.setText("");
        wordFragment = "";
        TextView label = (TextView) findViewById(R.id.gameStatus);
        if (userTurn) {
            label.setText(USER_TURN);
        } else {
            label.setText(COMPUTER_TURN);
            computerTurn();
        }
        return true;
    }
}
