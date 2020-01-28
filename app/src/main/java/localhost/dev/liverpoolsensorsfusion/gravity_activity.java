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
import android.widget.TextView;

import com.androidplot.xy.XYPlot;

public class gravity_activity extends AppCompatActivity  implements SensorEventListener {
    // Define Class Parameters
    private SensorManager sensorManager;
    Sensor gravity;
    TextView textViewXAxis, textViewYAxis, textViewZAxis;
    Handler handler;
    Runnable runnable;
    float[] gravData;
    XYPlot plot;
    DynamicLinePlot dynamicPlot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gravity_activity);

        //Get text views
        textViewXAxis = (TextView) findViewById(R.id.value_x_axis);
        textViewYAxis = (TextView) findViewById(R.id.value_y_axis);
        textViewZAxis = (TextView) findViewById(R.id.value_z_axis);
        // Get the system services, we need to get the system manager services
        // Get the permission to use the sensor
        sensorManager=(SensorManager) getSystemService(Context.SENSOR_SERVICE);
        gravity=sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);

        // Now we need to register a listener
        sensorManager.registerListener(gravity_activity.this, gravity, SensorManager.SENSOR_DELAY_NORMAL);
        //Create graph
        gravData = new float[3];

        plot = (XYPlot) findViewById(R.id.plot_sensor);
        dynamicPlot = new DynamicLinePlot(plot, getBaseContext(), "Gravity (m/s^2)");
        dynamicPlot.setMaxRange(12);
        dynamicPlot.setMinRange(-12);
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
                updateGravText();
            }
        };
    }
    @Override
    public void onResume() {
        super.onResume();
        sensorManager.registerListener(this, gravity, SensorManager.SENSOR_DELAY_FASTEST);
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
        gravData = event.values;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        //Safe not to implement
    }

    private void plotData() {
        dynamicPlot.setData(gravData[0], 0);
        dynamicPlot.setData(gravData[1], 1);
        dynamicPlot.setData(gravData[2], 2);

        dynamicPlot.draw();
    }

    protected void updateGravText() {
        // Update the gravity data
        textViewXAxis.setText(String.format("%.2f", gravData[0]));
        textViewYAxis.setText(String.format("%.2f", gravData[1]));
        textViewZAxis.setText(String.format("%.2f", gravData[2]));
    }
}