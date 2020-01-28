package localhost.dev.liverpoolsensorsfusion;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SensorsActivity extends AppCompatActivity {
    // Declare button to open gravity activity
    public Button button_gravity;
    // Declare button to open accelerometer activity
    public Button button_accelerometer;
    // Declare button to open gyroscope activity
    public Button button_gyroscope;
    // Declare button to open magnetometer activity
    public Button button_magnetometer;

        // Code to open new activity when the gravity button is pressed
        public void gravity() {
        button_gravity = (Button) findViewById(R.id.button_gravity);
        button_gravity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent grav = new Intent(SensorsActivity.this, gravity_activity.class);
                startActivity(grav);
            }
        });
    } // Ends here

        // Code to open new activity when the accelerometer button is pressed
        public void accelerometer() {
        button_accelerometer = (Button) findViewById(R.id.button_accelerometer);
        button_accelerometer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent acc = new Intent(SensorsActivity.this, accelerometer_activity.class);
                startActivity(acc);
            }
        });
    }// Ends here
        // Code to open new activity when the gyroscope button is pressed
    public void gyroscope() {
        button_gyroscope = (Button) findViewById(R.id.button_gyroscope);
        button_gyroscope.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent gyr = new Intent(SensorsActivity.this, gyroscope_activity.class);
                startActivity(gyr);
            }
        });
    } // Ends here
    // Code to open new activity when the magnetometer button is pressed
    public void magnetometer() {
        button_magnetometer = (Button) findViewById(R.id.button_magnetometer);
        button_magnetometer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent magne = new Intent(SensorsActivity.this, magnetometer_activity.class);
                startActivity(magne);
            }
        });
    } // Ends here
        @Override
        protected void onCreate (Bundle savedInstanceState){
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_sensors);
            // Declare gravity activity function
            gravity();
            accelerometer();
            gyroscope();
            magnetometer();
        }
}