package com.example.gesallprov;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.os.Bundle;
import com.example.gesallprov.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    // skapa instans av ActivityMainBinding
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // skapa en instans av ActivityMainBinding och hämta XML-filen
        binding = ActivityMainBinding.inflate(getLayoutInflater());

        // sätt layouten till att vara aktivitetens rotelement
        setContentView(binding.getRoot());

        // byt ut det nuvarande fragmentet till startsidan vid app-start
        replaceFragment(new HomeFragment());

        // lyssna på klick på navigeringsfältet (bottomNavigationView), som skapats
        // i resurmappen "menu"
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {

            // Bbt ut fragmentet beroende på vilken knapp som klickats på i navigeringsfältet
            switch (item.getItemId()){

                case R.id.home:
                    replaceFragment(new HomeFragment());
                    break;
                case R.id.quiz:
                    replaceFragment(new QuizFragment());
                    break;
                case R.id.calculator:
                    replaceFragment(new CalculatorFragment());
                    break;
                case R.id.utility:
                    replaceFragment(new UtilityFragment());
                    break;
            }

            return true;
        });
    }

    // metod för att byta ut fragmentet på aktivitetens rotelement
    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }

}
