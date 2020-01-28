package localhost.dev.liverpoolsensorsfusion;

import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class SensorsSpecsActivity extends AppCompatActivity {
ListView listSens;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensors_specs);
        // Define list view
        listSens=(ListView)findViewById(R.id.listSensor);
        // Connect to the phone's sensor manager
        SensorManager sManager = (SensorManager) this.getSystemService(this.SENSOR_SERVICE);
        // Get all the data from the phone's sensor manager
        List<Sensor> sensorList=sManager.getSensorList(Sensor.TYPE_ALL);
        // Put the sensor names in an array
        List<String> sensorNames= new ArrayList();
        for (int i = 0; i< sensorList.size(); i++) {
            // Get the name of each sensor and add it to the sensorNames array
            sensorNames.add(((Sensor) sensorList.get(i)).getName());
        }
        // Return the elements from the array as a string
        ArrayAdapter<String> itemsAdapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, sensorNames);
        // Add the elements from sensorNames to listSens
        listSens.setAdapter(itemsAdapter);
    }
}
