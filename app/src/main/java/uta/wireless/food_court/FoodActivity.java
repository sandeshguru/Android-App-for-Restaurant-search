package uta.wireless.food_court;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.ParcelUuid;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.UUID;


public class FoodActivity extends Activity {


    private String TAG = this.getClass().getSimpleName();
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothGatt mBluetoothGatt;
    private static final long SCAN_PERIOD = 100000;
    private boolean mScanning;
    private Handler mHandler;
    private boolean deviceFound = false;
    private ArrayList<BluetoothDevice> mFoundDevices;
    private String deviceUID;
    AlertDialog alertDialog = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(uta.wireless.food_court.R.layout.activity_food);
        final Button start= (Button)findViewById(uta.wireless.food_court.R.id.explore);
        mHandler = new Handler();
        final BluetoothManager bluetoothManager =
                (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();
        if (!mBluetoothAdapter.isEnabled()) {
            mBluetoothAdapter.enable();
        }
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Start scanning for ble sensor tag
                scanLeDevice(true);
                start.setEnabled(false);
            }

        });
        // start.setEnabled(false);
    }




    @Override
    protected void onResume() {
        super.onResume();
        // Initializes found devices list
        mFoundDevices = new ArrayList<BluetoothDevice>();
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (!mBluetoothAdapter.isEnabled()) {
            mBluetoothAdapter.enable();
        }
        //scanLeDevice(true);
    }

    /*protected void onStop()
    {
        super.onStop();
        scanLeDevice(true);
    }*/
    @Override
    protected void onPause() {
        super.onPause();
        //scanLeDevice(false);
        mFoundDevices.clear();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == uta.wireless.food_court.R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // Device scan callback.
    private BluetoothAdapter.LeScanCallback mLeScanCallback =
            new BluetoothAdapter.LeScanCallback() {

                @Override
                public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String deviceAddress = device.getAddress();



                            if(deviceAddress.equals("00:00:00:00:00:00") //replace with Sensortag 2.0 Mac address
                                    || deviceAddress.equals("00:00:00:00:00:00")
                                    || deviceAddress.equals("00:00:00:00:00:00")) {
                                if(!mFoundDevices.contains(device)) {
                                    // mFoundDevices.clear();
                                    mFoundDevices.add(device);
                                    notifyDataSetChanged();
                                }
                            }
                        }
                    });
                }
            };

    private void scanLeDevice(final boolean enable) {
        if (enable) {
            // Stops scanning after a pre-defined scan period.
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mScanning = false;
                    mBluetoothAdapter.stopLeScan(mLeScanCallback);
                    invalidateOptionsMenu();
                }
            }, SCAN_PERIOD);

            mScanning = true;
            mBluetoothAdapter.startLeScan(mLeScanCallback);
        } else {
            mScanning = false;
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
        }
        invalidateOptionsMenu();
    }

    public void notifyDataSetChanged() {
        try {
            for (BluetoothDevice b : mFoundDevices) {
                ImageView image;
                AlertDialog.Builder alertDialogBuilder;
                //AlertDialog alertDialog = null;
                final Context context = getApplicationContext();
                deviceUID = b.getAddress();
                Log.i(TAG, "Device found: " + deviceUID);

                if (alertDialog != null) {
                    Log.i(TAG, "cancelling alert");
                    alertDialog.cancel();
                }
                switch (deviceUID) {

                    case "00:00:00:00:00:00":
                        //Log.i(TAG,"Device found: "+deviceUID);
                        image = new ImageView(this);
                        image.setImageResource(uta.wireless.food_court.R.drawable.kfc);

                        alertDialogBuilder = new AlertDialog.Builder(FoodActivity.this);
                        // set title
                        alertDialogBuilder.setTitle("Your nearest restaurant is ");
                        // set dialog message
                        alertDialogBuilder.setView(image);

                        alertDialogBuilder.setCancelable(false)
                                .setPositiveButton("Short Menu", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // if this button is clicked, close
                                        dialog.cancel();
                                        Intent intent = new Intent(getApplicationContext(), ViewRestaurantsActivity.class);
                                        intent.putExtra("sid", deviceUID);
                                        startActivity(intent);
                                    }
                                })
                                .setNegativeButton("Full Menu", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // if this button is clicked, close
                                        dialog.cancel();


                                        try {
                                            Intent bi = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.kfc.com"));
                                            startActivity(bi);
                                        } catch (Exception e) {
                                            Log.e(TAG, "Error in http connection " + e.toString());
                                        }

                                    }
                                });
//                            
                        // create alert dialog
                        alertDialog = alertDialogBuilder.create();
                        // show it
                        alertDialog.show();
                        break;

                    case "00:00:00:00:00:00":
                        //Log.i(TAG,"Device found: "+deviceUID);
                        image = new ImageView(this);
                        image.setImageResource(uta.wireless.food_court.R.drawable.panda);

                        alertDialogBuilder = new AlertDialog.Builder(FoodActivity.this);
                        // set title
                        alertDialogBuilder.setTitle("Your Nearest Restuarant ");
                        // set dialog message
                        alertDialogBuilder.setView(image);
                        // .setMessage("Delete this profile?")

                        alertDialogBuilder.setCancelable(false)
                                .setPositiveButton("Short Menu", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // if this button is clicked, close
                                        dialog.cancel();
                                        Intent intent = new Intent(getApplicationContext(), ViewRestaurantsActivity.class);
                                        intent.putExtra("sid", deviceUID);
                                        startActivity(intent);

                                    }
                                })
                                .setNegativeButton("Full Menu", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // if this button is clicked, close
                                        dialog.cancel();
                                        try {
                                            Intent bi = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.pandaexpress.com"));
                                            startActivity(bi);
                                        } catch (Exception e) {
                                            Log.e(TAG, "Error in http connection " + e.toString());
                                        }

                                    }
                                });
//                            
                        // create alert dialog
                        alertDialog = alertDialogBuilder.create();
                        // show it
                        alertDialog.show();
                        break;

                    case "00:00:00:00:00:00":

                        image = new ImageView(this);
                        image.setImageResource(uta.wireless.food_court.R.drawable.mcd);

                        alertDialogBuilder = new AlertDialog.Builder(FoodActivity.this);
                        // set title
                        alertDialogBuilder.setTitle("You nearest restuarant ");
                        // set dialog message
                        alertDialogBuilder.setView(image);


                        alertDialogBuilder.setCancelable(false)
                                .setPositiveButton("Short Menu", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // if this button is clicked, close
                                        dialog.cancel();
                                        Intent intent = new Intent(getApplicationContext(), ViewRestaurantsActivity.class);
                                        intent.putExtra("sid", deviceUID);
                                        startActivity(intent);
                                    }
                                })
                                .setNegativeButton("Full Menu", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // if this button is clicked, close
                                        dialog.cancel();
//                                    
                                        try {
                                            Intent bi = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.mcdonalds.com/us/en/home.html"));
                                            startActivity(bi);
                                        } catch (Exception e) {
                                            Log.e(TAG, "Error in http connection " + e.toString());
                                        }

                                    }
                                });
//
                        // create alert dialog
                        alertDialog = alertDialogBuilder.create();
                        // show it
                        alertDialog.show();
                        break;

                    default:
                        break;
                }
            }
        }
        catch(Exception e){
                Log.e(TAG, "Too many sensors " + e.toString());
        }
    }
}
