package com.example.gesallprov;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class UtilityFragment extends Fragment implements SensorEventListener {

    // deklarerar en knapp som vid tryck kollar om enheten har en internetanslutning
    private Button checkInternetBtn;
   // deklarerar knapp som vid tryck visar en dialogruta, i detta fall en info ruta om sidan
    private Button utilityDialogBtn;
    // deklarerar knapp som vid tryck gör så att enheten vibrerar
    private Button checkVibrationBtn;
    // deklarerar textview som visar x-axelns acceleration
    private TextView tvXAxis;
    // deklarerar textview som visar y-axelns acceleration
    private TextView tvYAxis;
   // deklarerar textview som visar z-axelns acceleration
    private TextView tvZAxis;
    // deklarerar textview som visar grader i celsius
    private TextView tvTemperature;
    //deklarerar textview som visar enhetens latitutde
    private TextView tvSetLatitude;
    //deklarerar textview som visar enhetens longitude
    private TextView tvSetLongitude;

    // deklarerar en SensorManager som hanterar sensorer
    private SensorManager sensorManager;
    //deklarerar en sensor för temperaturen
    private Sensor temperatureSensor;
    //deklarerar en sensor för närheten (proximity)
    private Sensor proximitySensor;
    // skapar ett LocationManager-objekt för att hantera enhetens plats
    private LocationManager locationManager;
   // deklarerar vibrator för vibrationsknapoen
    private Vibrator vibrator;
    //boolean som sätts till true om det finns en sensor tillgänglig, annars false
    private boolean isTemperatureSensorAvailable;

    // deklarerar builder som bygger alert dialog
    private AlertDialog.Builder utilityDialogBuilder;
    // deklarerar view här så att den kan nås globalt, behövs nås i fler klasser än bara onCreateView
    View view;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // initialiserar view och inflate layouten för det här fragmentet
        view = inflater.inflate(R.layout.fragment_utility, container, false);

        // initialiserar textviews och hittar dom i layouten
        tvTemperature = view.findViewById(R.id.tvTemperature);
        tvXAxis =  view.findViewById(R.id.tvXAxis);
        tvYAxis =  view.findViewById(R.id.tvYAxis);
        tvZAxis =  view.findViewById(R.id.tvZAxis);
        tvSetLatitude = view.findViewById(R.id.tvSetLatitude);
        tvSetLongitude = view.findViewById(R.id.tvSetLongitude);

        // initialiserar knappar och hittar dom i layouten
        checkInternetBtn = view.findViewById(R.id.checkInternetBtn);
        utilityDialogBtn = view.findViewById(R.id.utilityDialogBtn);
        checkVibrationBtn = view.findViewById(R.id.checkVibrationBtn);

        //initialiserar olika tjänster som behövs i koden
        sensorManager = (SensorManager) requireActivity().getSystemService(Context.SENSOR_SERVICE);
        vibrator = (Vibrator) requireActivity().getSystemService(Context.VIBRATOR_SERVICE);
        locationManager = (LocationManager) requireActivity().getSystemService(Context.LOCATION_SERVICE);


        // kontrollerar om det finns en temperatursensor tillgänglig
        if (sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE) != null) {

            // hämtar temperatursensorn och sätter den som tillgänglig
            temperatureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
            isTemperatureSensorAvailable = true;
        } else {
            // ingen temperatur-sensor finns på mobilen,
            // sätter i TextView som visar att det inte finns någon temperatursensor tillgänglig
            tvTemperature.setText(R.string.nosensor);
            isTemperatureSensorAvailable = false;
        }


        // kolla om sensor finns, gäller för accelerometer
        if (sensorManager != null) {
            // Om ja; hämta en instans av accelerometer-sensorn från SensorManager-klassen
            Sensor accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

            // kolla om sensorn finns på enheten
            if (accelerometer != null) {
                // registrera en lyssnare för sensorn
                sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
            }
            // om ingen sensor finns, visa ett toast-meddelande att enheten inte har sensorn
        } else {
            Toast.makeText(getActivity(), "Sensor not found", Toast.LENGTH_SHORT).show();
        }

        // kontrollerar att sensor finns, gäller för närhet
        if (sensorManager != null) {
            // initaliserar närhetssensor
            proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);

            // kontrollerar om det finns en närhetssensor tillgänglig, registrerar lyssnare-
            // fär närhetssensorn genom SensorEventListener
            if (proximitySensor != null) {
                sensorManager.registerListener(this, proximitySensor, SensorManager.SENSOR_DELAY_NORMAL);
            } else {
                // visar meddelande om att en närhetssensor behövs om den inte finns
                Toast.makeText(getActivity(),"Proximity sensor is required!", Toast.LENGTH_LONG).show();
            }
        }



        // initialiserar dialogbuilder
        utilityDialogBuilder = new AlertDialog.Builder(getContext());

        // sätter en klick-lyssnare på dialog knappen
        utilityDialogBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // bygger och visar dialogen med relevant information; info om sidan
                utilityDialogBuilder.setTitle("What is this page?")
                        .setMessage(R.string.utilityDialogInfo)
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

        // sätter en klick-lyssnare på knappen för att kolla internetanslutning
        checkInternetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // anropar hasInternetConnection-metoden.Visar en Toast beroende på resultatet
                // om det finns internet säger toasten detta, annars säger den att ingen finns
                if (hasInternetConnection()) {
                    Toast.makeText(getActivity().getApplicationContext(), "Internet Connection Active", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                }
            }
        });


        // såtter en klick-lyssnare på knappen, gäller för att kolla vibration
        checkVibrationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // deklarerar vibrationseffekten
                final VibrationEffect vibrationEffect;
                // denna typ av vibration kräver version 26
                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                    // skapar vibration som vibrerar i 2 sekunder med 200 i styrka
                    vibrationEffect = VibrationEffect.createOneShot(2000, 200);
                    // anropar vibrationen, mobilen vibrerar
                    vibrator.vibrate(vibrationEffect);
                    // visar toast som säger att knappen gör så att mobilen vibrerar
                    Toast.makeText(getActivity(), "Gotcha! I do indeed vibrate!", Toast.LENGTH_SHORT).show();
                }
            }
        });



        // Be om tillstånd från användaren att få åtkomst till enhetens platsinformation, begär vid behov
        if(ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED)
            // om tillstånd finns, anropas metoden getLocation() som hämtar plats i koordinater
                getLocation();
        {
            //begär tillstånd
            requestPermissions( new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION}, 100);
        }

        // returnerar view
        return view;
    }



    // metod som hanterar begäran. Frågar om begäran, vid ja anropas getLocation(),
    // annars visas Toast-meddelande om att permissions behövs för att tjänsten ska fungera
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 100 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getLocation();
        } else {
            Toast.makeText(getActivity(), "Permissions needed to use this service", Toast.LENGTH_LONG).show();
        }
    }

    // metod som hämtar enhetens plats i latitude och longitude
    private void getLocation() {
        // Be om tillstånd från användaren att få åtkomst till enhetens platsinformation, begär vid behov
        if(ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {


            // registrerar en LocationListener för att lyssna på platsuppdateringar från LocationManager
            // varje 10:de millisekunder eller vid minst 1 meter i förändring av plats så uppdateras platsen
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10, 1, new LocationListener() {

                // anropar denna metod när platsuppdateringar tas emot från LocationManager
                @Override
                public void onLocationChanged(@NonNull Location location) {
                    // hämtar latitud och longitud från Location-objektet och konverterar från double till sträng
                    String latitude = String.valueOf(location.getLatitude());
                    String longitude = String.valueOf(location.getLongitude());

                    // sätter den nya latituden och longituden i respektive TextView
                    tvSetLatitude.setText(latitude);
                    tvSetLongitude.setText(longitude);
                }

            });
        }
    }


    // onSensorChanged() körs när något sensorvärde ändras
    @Override
    public void onSensorChanged(SensorEvent event) {
        // Kolla om händelsen kommer från en temperatur-sensor
        if (event.sensor.getType() == Sensor.TYPE_AMBIENT_TEMPERATURE) {

            // hämtar temperaturvärdet från sensorhändelsen och visar denna i TextView
            String temperatureValue = String.valueOf(event.values[0]);
            tvTemperature = (TextView) view.findViewById((R.id.tvTemperature));
            tvTemperature.setText(temperatureValue);
        }


        // Kolla om händelsen kommer från en accelerometer-sensor
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {

            // Extrahera axelvärdena från sensoreventet och konvertera till en sträng
            String xAxisValue = String.valueOf(event.values[0]);
            String yAxisValue = String.valueOf(event.values[1]);
            String zAxisValue = String.valueOf(event.values[2]);


            // Uppdatera textvyerna med de nya axelvärdena
            tvXAxis.setText(xAxisValue);
            tvYAxis.setText(yAxisValue);
            tvZAxis.setText(zAxisValue);
        }

        // Kontrollerar om sensorn är av typen närhet
        if (event.sensor.getType() == Sensor.TYPE_PROXIMITY) {

            // konverterar närhetsvärdet till en String och visar det i TextView
            String proximityValue = String.valueOf(event.values[0]);
            ((TextView) view.findViewById(R.id.tvProximity)).setText(proximityValue);
        }
    }

    // onAccuracyChanged() körs när sensorns noggrannhet ändras (används inte i det här fallet)
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}

    // onResume() körs när temperatur-aktiviteten återupptas
    @Override
    public void onResume() {
        super.onResume();
        // registrerar sensorn om den är tillgänglig
        if (isTemperatureSensorAvailable) {
            sensorManager.registerListener(this, temperatureSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    // onPause() körs när temperatur-aktiviteten pausas
    @Override
    public void onPause() {
        super.onPause();
        // avregistrerar sensorn om den är tillgänglig
        if (isTemperatureSensorAvailable) {
            sensorManager.unregisterListener(this);
        }
    }

    // tittar om det finns internetanslutning
    public boolean hasInternetConnection() {
        boolean isConnected = false;
        try {
            // hämtar ConnectivityManager system service
            ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

            // hämtar aktiv nätverksinformation
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

            // kollar ifall nätverket är tillgängligt och anslutet
            isConnected = networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected();

            // returnerar att internet finns och är anslutet
            return isConnected;

            // fångar exception vid try-catch satsen
        } catch (Exception e) {
            Log.e("Connectivity Exception", e.getMessage());
        }

        // returneras detta finns det inget internet / inte är anslutet
        return isConnected;
    }
}