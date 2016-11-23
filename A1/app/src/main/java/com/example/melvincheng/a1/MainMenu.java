package com.example.melvincheng.a1;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainMenu extends AppCompatActivity {

    private int questions_answered;

    String[] answer_array;
    String[] question_array;
    String[] question_num_array;
    int yesCount;
    int noCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        answer_array = new String[getResources().getStringArray(R.array.question_lbl).length];
        question_num_array = getResources().getStringArray(R.array.questions_num_lbl);
        question_array = getResources().getStringArray(R.array.question_lbl);

        questions_answered = 0;
    }

    public void start_quiz(View v) {
        Intent intent = new Intent(this, AskQuestion.class);
        intent.putExtra("question_number", question_num_array[0]);
        intent.putExtra("question", question_array[0]);
        this.startActivityForResult(intent, 0);
    }
    public void onActivityResult(int reqCode, int resCode, Intent result) {
        super.onActivityResult(reqCode, resCode, result);
        String answer = "None";
        if(resCode == Activity.RESULT_OK) {
            answer = result.getStringExtra("answer");
        }

        if (answer.equals("yes")){
            yesCount++;
        } else if (answer.equals("no")){
            noCount++;
        }
        questions_answered++;

        if (questions_answered != answer_array.length) {
            Intent intent = new Intent(this, AskQuestion.class);
            intent.putExtra("question_number", question_num_array[questions_answered]);
            intent.putExtra("question", question_array[questions_answered]);
            this.startActivityForResult(intent, 0);

        } else {
            Intent intent = new Intent(this, Summary.class);
            intent.putExtra("yesCount", yesCount);
            intent.putExtra("noCount", noCount);
            questions_answered = 0;
            yesCount = 0;
            noCount = 0;
            this.startActivity(intent);
        }
    }
}
