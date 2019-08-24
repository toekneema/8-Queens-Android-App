package com.example.a8queens4;

import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button[][] buttons = new Button[8][8];
    private boolean[][] isWhiteSquare = new boolean[8][8];
    boolean[][] isThereAQueen = new boolean[8][8];
    private TextView t;
    private int queensPlaced;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        t = findViewById(R.id.info);

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                String buttonID = "button_" + i + j;
                int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
                buttons[i][j] = findViewById(resID);
                buttons[i][j].setOnClickListener(this);
                if (i%2==0 && j%2==0 || i%2==1 && j%2==1) {
                    buttons[i][j].setBackgroundResource(R.drawable.white);
                    isThereAQueen[i][j] = false;
                    isWhiteSquare[i][j] = true;
                } else {
                    buttons[i][j].setBackgroundResource(R.drawable.black);
                    isThereAQueen[i][j] = false;
                    isWhiteSquare[i][j] = false;
                }
            }
        }

        Button buttonRestart = findViewById(R.id.button_restart);
        buttonRestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                restartGame();
            }
        });
    }

    @Override
    public void onClick(View v) {
        String id = v.getResources().getResourceEntryName(v.getId());
        String numberOnly= id.replaceAll("[^0-9]", "");
        int number = Integer.parseInt(numberOnly); //something is wrong with this logic
        int row = Integer.parseInt(Integer.toString(number).substring(0, 1));
        if (number < 10) {
            row = 0;
        }
        int col = number % 10;
        //all this work just to get the row and col indexes of the damn button

        if(!beingAttacked(row, col)) {//move is valid
            if (isWhiteSquare[row][col]) {
                ((Button) v).setBackgroundResource(R.drawable.whitewithqueen);
            } else { //i think this works now
                ((Button) v).setBackgroundResource(R.drawable.blackwithqueen);
            }
            isThereAQueen[row][col] = true; //places queen on the virtual board
            queensPlaced++;
            t.setText("Valid Move");

        } else { //when move is invalid
            //queen still needs to appear on the square
            //make a textview message saying to remove the queen
            if (isWhiteSquare[row][col]) {
                ((Button) v).setBackgroundResource(R.drawable.whitewithqueen);
                ((Button) v).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((Button) v).setBackgroundResource(R.drawable.white);
                        t.setText("Invalid piece removed");
                        queensPlaced--;
                    }
                });
            } else {
                ((Button) v).setBackgroundResource(R.drawable.blackwithqueen);
                ((Button) v).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((Button) v).setBackgroundResource(R.drawable.black);
                        t.setText("Invalid piece removed");
                    }
                });
            }
//              this was an attempt to create a flashing red blink when invalid
//            final Runnable r = new Runnable(){
//                int aux = 0;
//                public void run(){
//                    ((Button) v).setBackgroundColor(Color.parseColor("#ff6347"));
//                    if(aux < 3)
//                        handler1.postDelayed(this, 350);
//                    aux++;
//                }
//            };
//            handler1.post(r);

            //this is to make a toast as well saying that it was invalid
            final Toast toastInvalid = Toast.makeText(getApplicationContext(), "Invalid Move", Toast.LENGTH_SHORT);
            toastInvalid.show();

            Handler handler2 = new Handler();
            handler2.postDelayed(new Runnable() {
                @Override
                public void run() {
                    toastInvalid.cancel();
                }
            }, 350);
            t.setText("Invalid Move! Please tap the square again to remove the invalid queen");
        }

        if (queensPlaced >= 8) {
            Toast.makeText(this, "You won!", Toast.LENGTH_LONG).show();
            t.setText("Congrats! Hit restart to play again.");
        }
    }

    private boolean beingAttacked(int row, int col) {
        // checks rows
        boolean isbeingAttacked = false;
        for (int column = 0; column < 8; column++) {
            if ((isThereAQueen[row][column] == true)) {
                isbeingAttacked = true;
            }
        }

        // checks columns
        for (int row1 = 0; row1 < 8; row1++) {
            if (isThereAQueen[row1][col] == true) {
                isbeingAttacked = true;
            }
        }
        // checks diagonals
        for (int row1 = row, col1 = col; row1 >= 0 && col1 < 8; row1--, col1++) {
            if (isThereAQueen[row1][col1] == true) {
                isbeingAttacked = true;
            }
        }
        for (int row1 = row, col1 = col; row1 < 8 && col1 >= 0; row1++, col1--) {
            if (isThereAQueen[row1][col1] == true) {
                isbeingAttacked = true;
            }
        }
        for (int row1 = row, col1 = col; row1 >= 0 && col1 >= 0; row1--, col1--) {
            if (isThereAQueen[row1][col1] == true) {
                isbeingAttacked = true;
            }
        }
        for (int row1 = row, col1 = col; row1 < 8 && col1 < 8; row1++, col1++) {
            if (isThereAQueen[row1][col1] == true) {
                isbeingAttacked = true;
            }
        }

        return isbeingAttacked;
    }

    private void restartGame() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (i%2==0 && j%2==0 || i%2==1 && j%2==1) {
                    buttons[i][j].setBackgroundResource(R.drawable.white);
                    isThereAQueen[i][j] = false;
                    isWhiteSquare[i][j] = true; //not necessary i guess cuz it was never changed
                } else {
                    buttons[i][j].setBackgroundResource(R.drawable.black);
                    isThereAQueen[i][j] = false;
                    isWhiteSquare[i][j] = false;
                }
            }
        }
        t.setText("");
        queensPlaced = 0;
    }
}
