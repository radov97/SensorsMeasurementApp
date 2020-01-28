package localhost.dev.liverpoolsensorsfusion;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class mainmenu extends AppCompatActivity {

    public Button button_exit;
    // Declare button to open lantern activity
    public Button button_lantern;
    // Declare button to open sensors specifications activity
    public Button button_sensors_specifications;
    // Declare button to open help activity
    public Button button_help;
    // Declare button to open record data activity
    public Button button_record_data;
    // Declare button to open sensors activity
    public Button button_sensors;

    // Code to open new activity when the lantern button is pressed
    public void lantern(){
        button_lantern=(Button)findViewById(R.id.button_lantern);
        button_lantern.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent menulanterna=new Intent(mainmenu.this,lanternactivity.class);
                startActivity(menulanterna);
            }
        });
    } //ends here

    // Code to open new activity when the sensors specifications button is pressed
    public void sensors_specifications(){
        button_sensors_specifications=(Button)findViewById(R.id.button_magnetometer);
        button_sensors_specifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent sensors_specs=new Intent(mainmenu.this,SensorsSpecsActivity.class);
                startActivity(sensors_specs);
            }
        });
    } //ends here

    // Code to open new activity when the help button is pressed
    public void help(){
        button_help=(Button)findViewById(R.id.button_gravity);
        button_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent help_info=new Intent(mainmenu.this,HelpActivity.class);
                startActivity(help_info);
            }
        });
    } //ends here

    // Code to open new activity when the record data button is pressed
    public void recorddata(){
        button_record_data=(Button)findViewById(R.id.button_gyroscope);
        button_record_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent recorddataa=new Intent(mainmenu.this,RecordDataActivity.class);
                startActivity(recorddataa);
            }
        });
    } //ends here

    // Code to open new activity when the sensors button is pressed
    public void sensors(){
        button_sensors=(Button)findViewById(R.id.button_accelerometer);
        button_sensors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent sensors1=new Intent(mainmenu.this,SensorsActivity.class);
                startActivity(sensors1);
            }
        });
    } //ends here

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainmenu);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        // Declare lantern activity function
        lantern();
        // Declare sensors specification activity function
        sensors_specifications();
        // Declare help activity function
        help();
        // Declare record data activity function
        recorddata();
        // Declare sensors activity function
        sensors();

        // Code for back button to return to interface
        Button button_exit = (Button) findViewById(R.id.button_exit);
        button_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}