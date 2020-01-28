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

public class gyroscope_activity extends AppCompatActivity  implements SensorEventListener {
    // Define class parameters
    private SensorManager sensorManager;
    Sensor sensor, gyroscope;
    Handler handler;
    Runnable runnable;
    TextView textViewXAxis, textViewYAxis, textViewZAxis;
    RadioButton gyroCalibrated, gyroUncalibrated;
    float[] gyroData;
    XYPlot plot;
    DynamicLinePlot dynamicPlot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gyroscope_activity);

        //Get text views
        textViewXAxis = (TextView) findViewById(R.id.value_x_axis);
        textViewYAxis = (TextView) findViewById(R.id.value_y_axis);
        textViewZAxis = (TextView) findViewById(R.id.value_z_axis);

        //Get radio buttons
        gyroCalibrated = (RadioButton) findViewById(R.id.gyro_select_calibrated);
        gyroUncalibrated = (RadioButton) findViewById(R.id.gyro_select_uncalibrated);

        gyroCalibrated.setChecked(true);

        //Sensor manager
        sensorManager = (SensorManager) getBaseContext().getSystemService(Context.SENSOR_SERVICE);
        gyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        //Create graph
        gyroData = new float[3];

        plot = (XYPlot) findViewById(R.id.plot_sensor);
        dynamicPlot = new DynamicLinePlot(plot, getBaseContext(), "Gyroscope (rad/sec)");
        dynamicPlot.setMaxRange(10);
        dynamicPlot.setMinRange(-10);
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
                updateGyroText();
            }
        };
    }
    @Override
    public void onResume() {
        super.onResume();
        sensorManager.registerListener(this, gyroscope, SensorManager.SENSOR_DELAY_FASTEST);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE_UNCALIBRATED), SensorManager.SENSOR_DELAY_FASTEST);

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

        if (gyroCalibrated.isChecked() & i == Sensor.TYPE_GYROSCOPE) {
            gyroData = event.values;
        } else if (gyroUncalibrated.isChecked() & i == Sensor.TYPE_GYROSCOPE_UNCALIBRATED) {
            gyroData = event.values;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        //Safe not to implement
    }

    private void plotData() {
        dynamicPlot.setData(gyroData[0], 0);
        dynamicPlot.setData(gyroData[1], 1);
        dynamicPlot.setData(gyroData[2], 2);

        dynamicPlot.draw();
    }

    protected void updateGyroText() {
        // Update the gyroscope data
        textViewXAxis.setText(String.format("%.2f", gyroData[0]));
        textViewYAxis.setText(String.format("%.2f", gyroData[1]));
        textViewZAxis.setText(String.format("%.2f", gyroData[2]));
    }
}
