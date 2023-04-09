package com.example.connect3;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import nl.dionsegijn.konfetti.core.PartyFactory;
import nl.dionsegijn.konfetti.core.Position;
import nl.dionsegijn.konfetti.core.emitter.Emitter;
import nl.dionsegijn.konfetti.core.emitter.EmitterConfig;
import nl.dionsegijn.konfetti.core.models.Shape;
import nl.dionsegijn.konfetti.xml.KonfettiView;

public class MainActivity extends AppCompatActivity {

    // user is the number of of user, state is to check which box is pressed by which user and
    // winning state is the states that's winning
    // kv is to add confetti when a user wins
    // ifGameCompleted is to check if any of the user completed the game or not
    // winnerName says which user has won default value is 2, if 0 then O wins, if 1 then X wins
    private int user = 0;
    private int [] state = {2 ,2 ,2 ,2 ,2 ,2 ,2 ,2 ,2 };
    private final int [][] winningStates = {{0, 1, 2}, {3, 4, 5},{6, 7, 8},{0 ,3 ,6},{1, 4, 7},{2, 5, 8},{0, 4, 8},{2, 4, 6}};
    private int winnerName = 2;
    private KonfettiView kv;
    private boolean ifGameCompleted = false;
    int count = 0;

    TextView tv;
    Button retryBtn;


    // retry function after the game ends
    public void retry(View view){
        Intent intent = getIntent();
        finish();
        startActivity(intent);

    }

    public void insertSymbol(View view){

        //creating a object of the ImageView
        ImageView insert = (ImageView) view;

        //getting the id of the clicked position
        int clickedBox = Integer.parseInt(insert.getTag().toString());

        // checking if the position is already clicked or not and even if the game has completed to eliminate the possibility of selecting the remaining positing
        if(state[clickedBox] == 2 && !ifGameCompleted) {
            // updating the state i.e if its 2 that means its empty and if 0 then circle and 1 means x
            state[clickedBox] = user;
            // keep a track of no of moves for the game draw
            count++;


            if (user == 0) {
                insert.setImageResource(R.drawable.o);
                insert.animate().alpha(1).setDuration(500);
                user = 1;
            } else {
                insert.setImageResource(R.drawable.x);
                insert.animate().alpha(1).setDuration(500);
                user = 0;
            }

            //checking for winners
            for (int[] innerLoop : winningStates) {
                //we take each element from the winningStates and checking if actual states are equal or not
                //eg 1st winning option is 0 ,1 ,2 so check if states[0] == states[1] == states[2]
                if (state[innerLoop[0]] == state[innerLoop[1]] && state[innerLoop[1]] == state[innerLoop[2]] && state[innerLoop[0]] != 2) {
                    winnerName = state[innerLoop[0]];
                }

            }
            // checking who is the winner based on the winnerName value
            if (winnerName != 2) {

                EmitterConfig emitterConfig = new Emitter(100L, TimeUnit.MILLISECONDS).max(100);
                kv.start(
                        new PartyFactory(emitterConfig)
                                .spread(360)
                                .shapes(Arrays.asList(Shape.Square.INSTANCE, Shape.Circle.INSTANCE))
                                .colors(Arrays.asList(0xfce18a, 0xff726d, 0xf4306d, 0xb48def))
                                .setSpeedBetween(0f, 30f)
                                .position(new Position.Relative(0.5, 0.3))
                                .build()
                );

                // making sure after game ends the player can't press the remaining positions
                ifGameCompleted = true;

                if(winnerName == 1){
                    tv.setText("Congratulations X!");
                }else
                    tv.setText("Congratulations O!");
                retryBtn.setClickable(true);
                retryBtn.animate().alpha(1).setDuration(500);

            }
            // game draw condition
            if (count == 9){
                ifGameCompleted = true;
                tv.setText("Game Draw!");
                retryBtn.setClickable(true);
                retryBtn.animate().alpha(1).setDuration(500);
            }
        }


    }

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initialization of button, confetti and a winner display TextView
        kv = findViewById(R.id.konfettiView);
        tv = findViewById(R.id.tvWinnerName);
        retryBtn = findViewById(R.id.btnRetry);

    }
}