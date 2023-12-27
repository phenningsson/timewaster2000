package com.example.gesallprov;

import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class QuizFragment extends Fragment implements View.OnClickListener {

    // deklarerar element som används i layouten
    private TextView tvTotalQ;
    private TextView tvQuestion;
    private Button answerABtn, answerBBtn, answerCBtn, answerDBtn, submitBtn;

    // initialisering av variabler som används i koden
    private int score = 0;
    private int totalQuestions = AnswerQuestion.question.length;
    private int currentQuestionsIndex = 0;
    private String selectedAnswer = "";

    // deklarerar view här så att den kan nås globalt, behövs nås i fler klasser än bara onCreateView
    View view;

    // onCreateView-metod som är en del av Fragment-klassen
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // initialiserar view och inflate layouten för det här fragmentet
        view = inflater.inflate(R.layout.fragment_quiz, container, false);

        // initialiserar och hittar alla deklarerade element i layouten
        tvTotalQ = view.findViewById(R.id.tvTotalQ);
        tvQuestion = view.findViewById(R.id.tvQuestion);
        answerABtn = view.findViewById(R.id.answerABtn);
        answerBBtn = view.findViewById(R.id.answerBBtn);
        answerCBtn = view.findViewById(R.id.answerCBtn);
        answerDBtn = view.findViewById(R.id.answerDBtn);
        submitBtn = view.findViewById(R.id.submitBtn);

        // sätter onClick-lyssnare på alla knappar
        answerABtn.setOnClickListener(this);
        answerBBtn.setOnClickListener(this);
        answerCBtn.setOnClickListener(this);
        answerDBtn.setOnClickListener(this);
        submitBtn.setOnClickListener(this);

        // sätter totala antalet frågor i textview
        tvTotalQ.setText("Total questions: " + totalQuestions);

        // skapar ny fråga
        newQuestion();

        //returnerar view
        return view;
    }

    // onClick-metod som hanterar vad som händer när knappar klickas
    @Override
    public void onClick(View v) {

        // sätter färgen på alla alternativ-knappar till vit
        answerABtn.setBackgroundColor(Color.WHITE);
        answerBBtn.setBackgroundColor(Color.WHITE);
        answerCBtn.setBackgroundColor(Color.WHITE);
        answerDBtn.setBackgroundColor(Color.WHITE);

        // hämtar den klickade knappen
        Button clickedBtn = (Button) v;
        //om submit-knappen är den som tryckts
        if(clickedBtn.getId()==R.id.submitBtn) {
            // om det valda svaret är korrekt så ökar poängen med 1
            if (selectedAnswer.equals(AnswerQuestion.correctAnswers[currentQuestionsIndex])) {
                score++;
            }
            // ökar indexet för nuvarande fråga med 1 och skapar en ny fråga
            currentQuestionsIndex++;
            newQuestion();
        } else {
            //en av svarsalternativs-knapparna nedtryckt, ändrar färg på den nedtryckta knappen
            selectedAnswer = clickedBtn.getText().toString();
            clickedBtn.setBackgroundColor(Color.rgb(55,0,179));
        }
    }

    //metod som skapar ny frågoa
    private void newQuestion() {
        // om nuvarande frågeindex är samma som totala frågorna så anropas metod som avslutar quizet
        if (currentQuestionsIndex == totalQuestions) {
            finishQuiz();
            return;
        }

        // sätter den nuvarande frågan i textview som visar frågan
        tvQuestion.setText(AnswerQuestion.question[currentQuestionsIndex]);
        // sätter textview som visar alternativ A till det första alternativet av svar
        answerABtn.setText(AnswerQuestion.choices[currentQuestionsIndex][0]);
        // sätter textview som visar alternativ B till det andra alternativet av svar
        answerBBtn.setText(AnswerQuestion.choices[currentQuestionsIndex][1]);
        // sätter textview som visar alternativ C till det tredje alternativet av svar
        answerCBtn.setText(AnswerQuestion.choices[currentQuestionsIndex][2]);
        // sätter textview som visar alternativ D till det fjärde alternativet av svar
        answerDBtn.setText(AnswerQuestion.choices[currentQuestionsIndex][3]);
    }

    //metod som avslutar quizet
    private void finishQuiz() {
        String passStatus = "";
        //om du har mer än 60% rätt får du godkänt
        if (score > totalQuestions *0.60) {
            passStatus = "Passed";
        } else {
            passStatus = "Failed";
        }

        // en AlertDialog som visar resultatet av quizet samt ger användaren möjlighet att starta om quizet
        new AlertDialog.Builder(view.getContext())
                .setTitle(passStatus)
                .setMessage("Score is " + score + " out of " + totalQuestions)
                .setPositiveButton("Restart!", (dialogInterface, i) -> restartQuiz() )
                .setCancelable(false)
                .show();
    }

    // metod som startar om quizet och nollställer relevanta variabler
    private void restartQuiz() {
        score = 0;
        currentQuestionsIndex = 0;
        newQuestion();
    }

}