package com.example.gesallprov;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class CalculatorFragment extends Fragment {


    // deklarerar knappar som används i layouten
    private Button calcDialogBtn;
    private Button checkPrimeBtn;
    private Button saveBtn;

    //här deklareras alla knappar som används i miniräknaren
    private Button calcOneBtn, calcTwoBtn, calcThreeBtn, calcFourBtn, calcFiveBtn, calcSixBtn,
    calcSevenBtn, calcEightBtn, calcNineBtn, calcZeroBtn, calcPointBtn, calcPMBtn,
    calcDivideBtn, calcMultiBtn, calcSubBtn, calcAddBtn, calcEqualBtn, calcACBtn, calcPerBtn;

   // deklarerar edittext och textview som används i layouten
    private EditText enterNumber;
    private TextView outputIsPrime;

    // deklarerar element som används i koden
    String numberToTxt;
    String text;
    String operator;
    String oldNumber;
    boolean isNewOperator;

    // element som behövs för att spara genom shared preferences
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String TEXT = "text";

    // deklarerar builder som bygger alert dialog
    private AlertDialog.Builder calcDialogBuilder;

    // deklarerar view här så att den kan nås globalt, behövs nås i fler klasser än bara onCreateView
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // initialiserar view och inflate layouten för det här fragmentet
        view = inflater.inflate(R.layout.fragment_calculator, container, false);


        // initialiserar variabler som används i koden
        operator = "+";
        oldNumber = "";
        isNewOperator = true;

        // hittar deklarerade element i layouten
        checkPrimeBtn = view.findViewById(R.id.checkPrimeBtn);
        outputIsPrime = view.findViewById(R.id.outputIsPrime);
        saveBtn = view.findViewById(R.id.saveBtn);
        calcDialogBtn = view.findViewById(R.id.calcDialogBtn);
        enterNumber = view.findViewById(R.id.enterNumber);
        calcOneBtn = view.findViewById(R.id.calcOneBtn);
        calcTwoBtn = view.findViewById(R.id.calcTwoBtn);
        calcThreeBtn = view.findViewById(R.id.calcThreeBtn);
        calcFourBtn = view.findViewById(R.id.calcFourBtn);
        calcFiveBtn = view.findViewById(R.id.calcFiveBtn);
        calcSixBtn = view.findViewById(R.id.calcSixBtn);
        calcSevenBtn = view.findViewById(R.id.calcSevenBtn);
        calcEightBtn = view.findViewById(R.id.calcEightBtn);
        calcNineBtn = view.findViewById(R.id.calcNineBtn);
        calcZeroBtn = view.findViewById(R.id.calcZeroBtn);
        calcPointBtn = view.findViewById(R.id.calcPointBtn);
        calcPMBtn = view.findViewById(R.id.calcPMBtn);
        calcDivideBtn = view.findViewById(R.id.calcDivideBtn);
        calcMultiBtn = view.findViewById(R.id.calcMultiBtn);
        calcSubBtn = view.findViewById(R.id.calcSubBtn);
        calcAddBtn = view.findViewById(R.id.calcAddBtn);
        calcEqualBtn = view.findViewById(R.id.calcEqualBtn);
        calcACBtn = view.findViewById(R.id.calcACBtn);
        calcPerBtn = view.findViewById(R.id.calcPerBtn);

        // sätter onClick-lyssnare på alla knappar i miniräknaren och skickar till relevant metod
        calcOneBtn.setOnClickListener(this::numberEvent);
        calcTwoBtn.setOnClickListener(this::numberEvent);
        calcThreeBtn.setOnClickListener(this::numberEvent);
        calcFourBtn.setOnClickListener(this::numberEvent);
        calcFiveBtn.setOnClickListener(this::numberEvent);
        calcSixBtn.setOnClickListener(this::numberEvent);
        calcSevenBtn.setOnClickListener(this::numberEvent);
        calcEightBtn.setOnClickListener(this::numberEvent);
        calcNineBtn.setOnClickListener(this::numberEvent);
        calcZeroBtn.setOnClickListener(this::numberEvent);
        calcPointBtn.setOnClickListener(this::numberEvent);
        calcPMBtn.setOnClickListener(this::numberEvent);

        calcDivideBtn.setOnClickListener(this::operatorEvent);
        calcMultiBtn.setOnClickListener(this::operatorEvent);
        calcSubBtn.setOnClickListener(this::operatorEvent);
        calcAddBtn.setOnClickListener(this::operatorEvent);

        calcEqualBtn.setOnClickListener(this::equalEvent);

        calcACBtn.setOnClickListener(this::allClearEvent);

        calcPerBtn.setOnClickListener(this::percentageEvent);

        // sätter en knapp-lyssnare på knappen som tittar om det är ett primtal
        checkPrimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // hämtar talet och sätter till en String
                numberToTxt = enterNumber.getText().toString();

                // tittar ifall användaren har slagit in en siffra, om inte: visa felmeddelande
                if (numberToTxt.equals("") || numberToTxt.isEmpty()) {
                    Toast.makeText(getActivity(), "Please enter a number", Toast.LENGTH_SHORT).show();
                } else {

                    //konverterar numret från String till double, miniräknaren använder double som typ
                    double numberDouble = Double.parseDouble(numberToTxt);

                    // konverterar numret från double till long för att kolla om det är primtal
                    long numberLong = (long) numberDouble;

                    // tittar om numret är ett primtal genom att anropa hjälpmetod isPrime
                    if (numberLong == numberDouble && isPrime(numberLong)) {

                        // numberLong är ett primtal, visar detta genom textview
                        outputIsPrime.setText(numberLong + " is a prime number! ");
                    } else {

                        // numberLong är inte ett primtal, visar detta genom textview
                        outputIsPrime.setText(numberLong + " is not a prime number! ");
                    }
                }
            }
        });


        // sätter knapp-lyssnare på save-knappen, vid tryck så anropas saveData()
        // hjälpmetod som sparar ifall det senaste kollade talet är ett primtal
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
            }
        });


        // initialiserar dialogbuilder
        calcDialogBuilder = new AlertDialog.Builder(getContext());

        // sätter en klick-lyssnare på dialog knappen
        calcDialogBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // bygger och visar dialogen med relevant information, dialog som beskriver sidan
                calcDialogBuilder.setTitle("What is this page?")
                        .setMessage(R.string.calcDialogInfo)
                        .setCancelable(true)
                        .setNegativeButton("Okay!", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .show();
            }
        });

        //anropar metod som laddar data
        loadData();

        //anropar metod som uppdaterar textview som visar om talet är ett primtail,
        // hämtas från den text som sparats
        updateViews();

        //returnerar view
        return view;
    }

    // metod som tittar om ett tal är ett primtal
    private boolean isPrime(long n) {
        if (n <= 1) {
            return false;
        }
        for (long i = 2; i <= Math.sqrt(n); i++) {
            if (n % i == 0) {
                return false;
            }
        }
        return true;
    }


    // metod som hanterar om något av kalkylatorns nummer, punkt eller konvertera till minus (+/-)
    // knappar trycks
    public void numberEvent(View view) {
        // om det är en ny opreator, sätt text till ""
        if (isNewOperator)
            enterNumber.setText("");
        isNewOperator = false;


        // hämtar textviews nummer som en string och sätter till en string-variabel
        String number = enterNumber.getText().toString();

        // switch case där respektive knapps id-hämtas för att ta reda på vilken knapp som trycks,
        // om exempelvis 1 trycks ned så sätts numret += 1, osv
        switch(view.getId()) {
            case R.id.calcZeroBtn:
                number += "0";
                break;

            case R.id.calcOneBtn:
                number += "1";
                break;

            case R.id.calcTwoBtn:
                number += "2";
                break;

            case R.id.calcThreeBtn:
                number += "3";
                break;

            case R.id.calcFourBtn:
                number += "4";
                break;

            case R.id.calcFiveBtn:
                number += "5";
                break;

            case R.id.calcSixBtn:
                number += "6";
                break;

            case R.id.calcSevenBtn:
                number += "7";
                break;

            case R.id.calcEightBtn:
                number += "8";
                break;

            case R.id.calcNineBtn:
                number += "9";
                break;

            case R.id.calcPointBtn:
                number += ".";
                break;

            case R.id.calcPMBtn:
                number = "-"+number;
                break;
        }

        // sätter textview till nummret
        enterNumber.setText(number);
    }

    // metod som hanterar om en operator-knapp har tryckts ned, dvs /, *, - eller +
    // sätter operator-string till den operator-knapp som tryckts ned
    public void operatorEvent(View view) {
        isNewOperator = true;
        oldNumber = enterNumber.getText().toString();

        switch (view.getId()) {
            case R.id.calcDivideBtn:
                operator = "/";
                break;

            case R.id.calcMultiBtn:
                operator = "*";
                break;

            case R.id.calcSubBtn:
                operator = "-";
                break;

            case R.id.calcAddBtn:
                operator = "+";
                break;
        }
    }

    //metod som hanterar om likamed-knappen tryckts ned
    public void equalEvent(View view) {
        // hämtar textviews nummer som en string och sätter till en string
        String newNumber = enterNumber.getText().toString();

        // deklarerar resultatet med ett default-värde på 0.0, denna variabel håller resultatet
        // av uträkningen
        double result = 0.0;

        // switch case som bestämmer vilken operation som ska utföras baserat på den operator-knapp
        // som trycks ned. Uträkningen utförs med de två operanderna oldNumber och newNumber
        // Double.parseDouble() utförs för att konvertera string-operanderna till double
        // för att utföra uträkningen. Resultatet sparas i result variabeln och
        // visas i textview genom att sätta enterNUmber.setText(result+"")
        switch(operator) {

            case "/":
                result = Double.parseDouble(oldNumber) / Double.parseDouble(newNumber);
                break;

            case "*":
                result = Double.parseDouble(oldNumber) * Double.parseDouble(newNumber);
                break;

            case "-":
                result = Double.parseDouble(oldNumber) - Double.parseDouble(newNumber);
                break;

            case "+":
                result = Double.parseDouble(oldNumber) + Double.parseDouble(newNumber);
                break;
        }
        enterNumber.setText(result+"");
    }

    // metod som anropas när AC-knappen klickas på miniräknaren. "Nollställer" miniräknaren
    // så man kan börja om från början
    public void allClearEvent(View view){
        enterNumber.setText("0");
        isNewOperator = true;
    }

    // metod som anropas när procent-knappen klickas på miniräknaren. Delar det nuvarande numret
    // med 100 för att få fram dess procensats. Detta visas genom att sätta resultatet i textview
    public void percentageEvent(View view){
        double numPercent = Double.parseDouble(enterNumber.getText().toString()) / 100;
        enterNumber.setText(numPercent+"");
        isNewOperator = true;
    }

    // metod som anropas när save-knappen trycks
    public void saveData(){
        // hämtar en instans av SharedPreferences med namnet SHARED_PREFS och tillåter andra delar av appen att läsa/skriva till filen
        SharedPreferences sharedPreferences = getActivity().getApplicationContext().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);

        // skapar en instans av Editor från SharedPreferences för att kunna skriva data till filen
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // lägger till värdet från outputIsPrime till Editor, dvs om det är ett primtal
        editor.putString(TEXT, outputIsPrime.getText().toString());

        // skickar data till SharedPreferences-filen.
        editor.apply();

        // visar en kort varning i en Toast att data har sparats
        Toast.makeText(getActivity(), "Data  saved", Toast.LENGTH_SHORT).show();
    }

    // hjälpmetod som anropas när sparad data ska laddas in för att visa om senaste sparade tal
    // var ett primtal
    public void loadData(){
        // hämtar en instans av SharedPreferences med namnet SHARED_PREFS och tillåt andra delar av appen att läsa/skriva till filen
        SharedPreferences sharedPreferences = getActivity().getApplicationContext().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);

        // hämtar textvärdet från SharedPreferences-filen med nyckelordet TEXT och använder en tom sträng som standardvärde om det inte finns något sparad data
        text = sharedPreferences.getString(TEXT, "");
    }

    // hjälpmetod som anropas för att sätta demn laddade datan på textview för att visa
    // för användaren om det senaste sparade talet var ett primtal eller ej
    public void updateViews(){
        outputIsPrime.setText(text);
    }

}