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
import android.widget.RadioButton;
import android.widget.TextView;

import com.androidplot.xy.XYPlot;

public class magnetometer_activity extends AppCompatActivity implements SensorEventListener {
    // Define Class parameters
    private SensorManager sensorManager;
    Sensor sensor, magnetometer;
    Handler handler;
    Runnable runnable;
    TextView textViewXAxis, textViewYAxis, textViewZAxis;
    RadioButton magneCalibrated, magneUncalibrated;
    float[] magneData;
    XYPlot plot;
    DynamicMagnetometerPlot dynamicPlot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_magnetometer_activity);
        //Get text views
        textViewXAxis = (TextView) findViewById(R.id.value_x_axis);
        textViewYAxis = (TextView) findViewById(R.id.value_y_axis);
        textViewZAxis = (TextView) findViewById(R.id.value_z_axis);

        //Get radio buttons
        magneCalibrated = (RadioButton) findViewById(R.id.magne_select_calibrated);
        magneUncalibrated = (RadioButton) findViewById(R.id.magne_select_uncalibrated);

        magneCalibrated.setChecked(true);

        //Sensor manager
        sensorManager = (SensorManager) getBaseContext().getSystemService(Context.SENSOR_SERVICE);
        magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        //Create graph
        magneData = new float[3];

        plot = (XYPlot) findViewById(R.id.plot_sensor);
        dynamicPlot = new DynamicMagnetometerPlot(plot, getBaseContext(), "Magnetic Field (amperes/meter)");
        dynamicPlot.setMaxRange(1000);
        dynamicPlot.setMinRange(-1000);
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
                updateMagneText();
            }
        };
    }
    @Override
    public void onResume() {
        super.onResume();
        sensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_FASTEST);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD_UNCALIBRATED), SensorManager.SENSOR_DELAY_FASTEST);

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

        if (magneCalibrated.isChecked() & i == Sensor.TYPE_MAGNETIC_FIELD) {
            magneData = event.values;
        } else if (magneUncalibrated.isChecked() & i == Sensor.TYPE_MAGNETIC_FIELD_UNCALIBRATED) {
            magneData = event.values;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        //Safe not to implement
    }

    private void plotData(){
        dynamicPlot.setData(magneData[0], 0);
        dynamicPlot.setData(magneData[1], 1);
        dynamicPlot.setData(magneData[2], 2);

        dynamicPlot.draw();
    }

    protected void updateMagneText() {
        // Update the magnetometer data
        textViewXAxis.setText(String.format("%.2f", magneData[0]));
        textViewYAxis.setText(String.format("%.2f", magneData[1]));
        textViewZAxis.setText(String.format("%.2f", magneData[2]));
    }
}