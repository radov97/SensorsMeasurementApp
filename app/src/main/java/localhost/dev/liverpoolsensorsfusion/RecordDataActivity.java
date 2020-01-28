package localhost.dev.liverpoolsensorsfusion;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class RecordDataActivity extends AppCompatActivity implements SensorEventListener {
    // Define class parameters.
    private SensorManager mSensorManager;
    private Sensor mAccelerometer, mGyroscope, mMagnetometer;
    CheckBox checkAcc, checkGyro, checkMagne;
    TextView accX, accY, accZ;             // Accelerometer
    TextView magneX, magneY, magneZ;       // Magnetometer
    TextView gyroX, gyroY, gyroZ;          // Gyroscope

    String FILENAME, content1, content2, content3;
    FileOutputStream out;

    Button startButton, stopButton;
    boolean isFirstSet = true, startFlag = false;
    EditText textData;

    private Chronometer chronometer;
    private boolean running_chronometer;
    private long currentTime, startTime;

    float[] acceleration = new float[3];
    float[] gyroscope = new float[3];
    float[] magnetometer = new float[3];

    float[] new_gyroscope = new float[3];
    float[] new_magnetometer = new float[3];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_data);
        // define sensors
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mGyroscope = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        mMagnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        // define text views
        accX = (TextView) findViewById(R.id.raw_value_acc_x);
        accY = (TextView) findViewById(R.id.raw_value_acc_y);
        accZ = (TextView) findViewById(R.id.raw_value_acc_z);
        magneX = (TextView) findViewById(R.id.raw_value_magne_x);
        magneY = (TextView) findViewById(R.id.raw_value_magne_y);
        magneZ = (TextView) findViewById(R.id.raw_value_magne_z);
        gyroX = (TextView) findViewById(R.id.raw_value_gyro_x);
        gyroY = (TextView) findViewById(R.id.raw_value_gyro_y);
        gyroZ = (TextView) findViewById(R.id.raw_value_gyro_z);

        // define checkboxes
        checkAcc=(CheckBox)findViewById(R.id.checkBox);
        checkGyro=(CheckBox)findViewById(R.id.checkBox2);
        checkMagne=(CheckBox)findViewById(R.id.checkBox3);

        // file name to be entered
        textData = (EditText) findViewById(R.id.edit_text);
        textData.setHint("Enter File Name here...");

        // define chronometer
        chronometer = findViewById(R.id.chronometer);
        chronometer.setFormat("Recording: %s");
        chronometer.setBase(SystemClock.elapsedRealtime());

        // define start button
        startButton = (Button) findViewById(R.id.button_record);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // create file
                FILENAME= textData.getText() + ".csv";
                if (!checkAcc.isChecked() && !checkGyro.isChecked() && !checkMagne.isChecked()) {
                    Toast.makeText(getBaseContext(), "Please select at least one sensor!",
                            Toast.LENGTH_LONG).show();
                } else if (FILENAME.equals(".csv")) {
                    Toast.makeText(getBaseContext(), "Please insert a valid name for the file to be created!",
                            Toast.LENGTH_LONG).show();
                } else {
                    // set the recording button ON
                    startFlag = true;
                    // make the chronometer run
                    if (!running_chronometer) {
                        chronometer.setBase(SystemClock.elapsedRealtime());
                        chronometer.start();
                        running_chronometer = true;
                        checkAcc.setClickable(false);
                        checkGyro.setClickable(false);
                        checkMagne.setClickable(false);
                    }
                    // add screen message to confirm that the app is recording
                    try {
                        textData.getText().clear();
                        Toast.makeText(getBaseContext(), "Start recording the data set", Toast.LENGTH_SHORT).show();
                    } catch(Exception e) {
                        Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    // Turn off the record button
                    startButton.setClickable(false);
                }
            }
        }); // starts button ends here

        // define stop button
        stopButton=(Button)findViewById(R.id.button_stop);
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // set the recording button OFF
                // Stop writing on file
                startFlag = false;
                // if there is no file created state the following message
                if (FILENAME==null || FILENAME.equals(".csv")) {
                    Toast.makeText(getBaseContext(), "There is no recording taken in this moment!",
                            Toast.LENGTH_LONG).show();
                } else {
                    // stop the chronometer
                    chronometer.stop();
                    running_chronometer = false;
                    checkAcc.setClickable(true);
                    checkGyro.setClickable(true);
                    checkMagne.setClickable(true);
                    // add screen message to confirm that the app has saved the data set
                    try {
                        Toast.makeText(getBaseContext(), "Saved to " + getFilesDir() + "/" + FILENAME,
                                Toast.LENGTH_LONG).show();
                    } catch(Exception e) {
                        Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                // Turn back on the Record button
                startButton.setClickable(true);
            }
        });  // stop button ends here
    } // onCreate class ends here

    @Override
    public final void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Do something here if sensor accuracy changes.
    }

    @Override
    public final void onSensorChanged(SensorEvent event) {
        if (startFlag) {
            if (checkAcc.isChecked() && event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                acceleration[0] = event.values[0];
                acceleration[1] = event.values[1];
                acceleration[2] = event.values[2];
            }

            if (checkGyro.isChecked() && event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
                gyroscope[0] = event.values[0];
                gyroscope[1] = event.values[1];
                gyroscope[2] = event.values[2];
            }

            if (checkMagne.isChecked() && event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
                magnetometer[0] = event.values[0];
                magnetometer[1] = event.values[1];
                magnetometer[2] = event.values[2];
            }
            // when recording is ON do this
            // initialise the content that will be written in the file
            if (isFirstSet) {
                startTime = System.currentTimeMillis();
                isFirstSet = false;
            }
            currentTime = System.currentTimeMillis();

            content1 = (currentTime-startTime) + "," + "ACC" + "," + acceleration[0] + "," + acceleration[1] + "," + acceleration[2] + "\n";
            content2 = (currentTime-startTime) + "," + "GYR" + "," + gyroscope[0] + "," + gyroscope[1] + "," + gyroscope[2] + "\n";
            content3 = (currentTime-startTime) + "," + "MAG" + "," + magnetometer[0] + "," +magnetometer[1] + "," +  magnetometer[2] + "\n";
            // as long the recording is ON
            for (int i = 0; i < 1; i++){
                try {
                    // create the file
                    out = openFileOutput(FILENAME, Context.MODE_APPEND);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                // write to the file
                if (checkAcc.isChecked() && checkGyro.isChecked() && checkMagne.isChecked() && startFlag) {
                    updateAccText();
                    writeAcc();
                    updateGyroText();
                    HandleGyr();
                    if (new_gyroscope[0]==gyroscope[0] && new_gyroscope[1]==gyroscope[1] && new_gyroscope[2]==gyroscope[2]) {
                        writeGyr();
                    }
                    updateMagneText();
                    HandleMag();
                    if (new_magnetometer[0]==magnetometer[0] && new_magnetometer[1]==magnetometer[1] && new_magnetometer[1]==magnetometer[1]) {
                        writeMag();
                    }
                } else if (checkAcc.isChecked() && checkGyro.isChecked() && startFlag) {
                    updateAccText();
                    updateGyroText();
                    writeAcc();
                    HandleGyr();
                    if (new_gyroscope[0]==gyroscope[0] && new_gyroscope[1]==gyroscope[1] && new_gyroscope[2]==gyroscope[2]) {
                        writeGyr();
                    }
                } else if (checkAcc.isChecked() && checkMagne.isChecked() && startFlag) {
                    updateAccText();
                    updateMagneText();
                    writeAcc();
                    HandleMag();
                    if (new_magnetometer[0]==magnetometer[0] && new_magnetometer[1] == magnetometer[1] && new_magnetometer[1] == magnetometer[1]) {
                        writeMag();
                    }
                } else if (checkGyro.isChecked() && checkMagne.isChecked() && startFlag) {
                    updateGyroText();
                    updateMagneText();
                    HandleGyr();
                    HandleMag();
                    if (new_gyroscope[0]==gyroscope[0] && new_gyroscope[1]==gyroscope[1] && new_gyroscope[2]==gyroscope[2]) {
                        writeGyr();
                    }
                    if (new_magnetometer[0]==magnetometer[0] && new_magnetometer[1]==magnetometer[1] && new_magnetometer[1]==magnetometer[1]) {
                        writeMag();
                    }
                } else if (checkAcc.isChecked() && startFlag) {
                    updateAccText();
                    writeAcc();
                } else if (checkGyro.isChecked() && startFlag) {
                    updateGyroText();
                    writeGyr();
                } else if (checkMagne.isChecked()) {
                    updateMagneText();
                    writeMag();
                }
            } // for loop end
        }

    }
    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mGyroscope, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mMagnetometer, SensorManager.SENSOR_DELAY_NORMAL);
    }
    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }
    protected void updateAccText() {
        // Update the gyroscope data
        accX.setText(String.format("%.6f", acceleration[0]));
        accY.setText(String.format("%.6f", acceleration[1]));
        accZ.setText(String.format("%.6f", acceleration[2]));
    }
    protected void updateGyroText() {
        // Update the gyroscope data
        gyroX.setText(String.format("%.6f", gyroscope[0]));
        gyroY.setText(String.format("%.6f", gyroscope[1]));
        gyroZ.setText(String.format("%.6f", gyroscope[2]));
    }
    protected void updateMagneText() {
        // Update the gyroscope data
        magneX.setText(String.format("%.6f", magnetometer[0]));
        magneY.setText(String.format("%.6f", magnetometer[1]));
        magneZ.setText(String.format("%.6f", magnetometer[2]));
    }
    // Adjust sampling rate for Gyroscope
    private void HandleGyr() {
        Handler handlerGyroscope = new Handler();
        handlerGyroscope.postDelayed(new Runnable() {
            @Override
            public void run() {
                new_gyroscope[0]=gyroscope[0];
                new_gyroscope[1]=gyroscope[1];
                new_gyroscope[2]=gyroscope[2];
            }
        }, 5);
    }
    // Adjust sampling rate for Magnetometer
    private void HandleMag(){
        Handler handlerMagnetometer = new Handler();
        handlerMagnetometer.postDelayed(new Runnable() {
            @Override
            public void run() {
                new_magnetometer[0]=magnetometer[0];
                new_magnetometer[1]=magnetometer[1];
                new_magnetometer[2]=magnetometer[2];
            }
        }, 100);
    }

    private void writeAcc() {
        try {
            out.write(content1.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeGyr() {
        try {
            out.write(content2.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeMag() {
        try {
            out.write(content3.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}