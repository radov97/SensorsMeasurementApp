package localhost.dev.liverpoolsensorsfusion;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.TextView;

import com.androidplot.xy.XYPlot;

public class accelerometer_activity extends AppCompatActivity implements SensorEventListener{
    // Define class parameters
    private SensorManager sensorManager;
    Sensor sensor, accelerometer;
    Handler handler;
    Runnable runnable;
    TextView textViewXAxis, textViewYAxis, textViewZAxis;
    RadioButton accCalibrated, accUncalibrated, accLinear;
    float[] accData;
    XYPlot plot;
    DynamicAccelerometerPlot dynamicPlot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accelerometer_activity);

        //Get text views
        textViewXAxis = (TextView) findViewById(R.id.value_x_axis);
        textViewYAxis = (TextView) findViewById(R.id.value_y_axis);
        textViewZAxis = (TextView) findViewById(R.id.value_z_axis);

        //Get radio buttons
        accCalibrated = (RadioButton) findViewById(R.id.acc_calibrated);
        accUncalibrated = (RadioButton) findViewById(R.id.acc_uncalibrated);
        accLinear = (RadioButton) findViewById(R.id.acc_linear);

        accCalibrated.setChecked(true);

        //Sensor manager
        sensorManager = (SensorManager) getBaseContext().getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        //Create graph
        accData = new float[3];

        plot = (XYPlot) findViewById(R.id.plot_sensor);
        dynamicPlot = new DynamicAccelerometerPlot(plot, getBaseContext(), "Acceleration (m/s^2)");
        dynamicPlot.setMaxRange(30);
        dynamicPlot.setMinRange(-30);
        dynamicPlot.addSeriesPlot("X", 0, ContextCompat.getColor(getBaseContext(), R.color.graphX));
        dynamicPlot.addSeriesPlot("Y", 1, ContextCompat.getColor(getBaseContext(), R.color.graphY));
        dynamicPlot.addSeriesPlot("Z", 2, ContextCompat.getColor(getBaseContext(), R.color.graphZ));

        //Handler for graph plotting on background thread
        handler = new Handler();

        //Runnable for background plotting
        runnable = new Runnable()
        {
            @Override
            public void run() {
                handler.postDelayed(this, 10);
                plotData();
                updateAccText();
            }
        };
    }
    @Override
    public void onResume() {
        super.onResume();
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_FASTEST);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER_UNCALIBRATED), SensorManager.SENSOR_DELAY_FASTEST);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION), SensorManager.SENSOR_DELAY_FASTEST);

        handler.post(runnable);
    }
    @Override

    public void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
        handler.removeCallbacks(runnable);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        sensor = event.sensor;

        int i = sensor.getType();

        if (accCalibrated.isChecked() & i == Sensor.TYPE_ACCELEROMETER) {
            accData = event.values;
        } else if (accUncalibrated.isChecked() & i == Sensor.TYPE_ACCELEROMETER_UNCALIBRATED) {
            accData = event.values;
        } else if (accLinear.isChecked() & i == Sensor.TYPE_LINEAR_ACCELERATION) {
            accData = event.values;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        //Safe not to implement
    }

    private void plotData() {
        dynamicPlot.setData(accData[0], 0);
        dynamicPlot.setData(accData[1], 1);
        dynamicPlot.setData(accData[2], 2);

        dynamicPlot.draw();
    }

    protected void updateAccText() {
        // Update the gyroscope data
        textViewXAxis.setText(String.format("%.2f", accData[0]));
        textViewYAxis.setText(String.format("%.2f", accData[1]));
        textViewZAxis.setText(String.format("%.2f", accData[2]));
    }
}
