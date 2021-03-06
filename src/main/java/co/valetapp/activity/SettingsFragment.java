package co.valetapp.activity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import co.valetapp.R;
import co.valetapp.service.AutoParkService;
import co.valetapp.util.Const;

import java.util.*;

public class SettingsFragment extends DynamicFragment {

    Spinner bluetoothSpinner, alarmSpinner;
    CheckBox bluetoothCheckBox, sensorCheckBox, dockCheckBox, notificationsCheckBox, alarmCheckBox, clockCheckBox;
    RadioGroup unitsRadioGroup;
    ImageView bluetoothButton;
    SharedPreferences prefs;
    ArrayAdapter<MyBluetoothDevice> bluetoothAdapter;
    List<MyBluetoothDevice> myBluetoothDevices = new ArrayList<MyBluetoothDevice>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return getActivity().getLayoutInflater().inflate(R.layout.settings_fragment, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        prefs = getActivity().getSharedPreferences(Const.SHARED_PREFS_NAME, Context.MODE_PRIVATE);

        bluetoothButton = (ImageView) view.findViewById(R.id.bluetoothButton);

        bluetoothSpinner = (Spinner) view.findViewById(R.id.bluetoothSpinner);
        bluetoothSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                MyBluetoothDevice myBluetoothDevice = (MyBluetoothDevice) bluetoothSpinner.getSelectedItem();
                if (myBluetoothDevice != null) {
                    prefs.edit().putString(Const.BLUETOOTH_KEY, myBluetoothDevice.address).commit();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                prefs.edit().remove(Const.BLUETOOTH_KEY).commit();
            }
        });

        bluetoothCheckBox = (CheckBox) view.findViewById(R.id.bluetoothCheckBox);
        bluetoothCheckBox.setChecked(prefs.getBoolean(Const.BLUETOOTH_ENABLED_KEY, false));
        bluetoothCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    prefs.edit().putBoolean(Const.BLUETOOTH_ENABLED_KEY, true).commit();
                } else {
                    prefs.edit().putBoolean(Const.BLUETOOTH_ENABLED_KEY, false).commit();
                }
            }
        });


        dockCheckBox = (CheckBox) view.findViewById(R.id.dockCheckBox);
        dockCheckBox.setChecked(prefs.getBoolean(Const.DOCK_KEY, false));
        dockCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    prefs.edit().putBoolean(Const.DOCK_KEY, true).commit();
                } else {
                    prefs.edit().putBoolean(Const.DOCK_KEY, false).commit();
                }
            }
        });

        notificationsCheckBox = (CheckBox) view.findViewById(R.id.notificationsCheckBox);
        notificationsCheckBox.setChecked(prefs.getBoolean(Const.NOTIFICATIONS_KEY, false));
        notificationsCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    prefs.edit().putBoolean(Const.NOTIFICATIONS_KEY, true).commit();
                } else {
                    prefs.edit().putBoolean(Const.NOTIFICATIONS_KEY, false).commit();
                }
            }
        });

        alarmCheckBox = (CheckBox) view.findViewById(R.id.alarmCheckBox);
        alarmCheckBox.setChecked(prefs.getBoolean(Const.ALARM_KEY, false));
        alarmCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    prefs.edit().putBoolean(Const.ALARM_KEY, true).commit();
                } else {
                    prefs.edit().putBoolean(Const.ALARM_KEY, false).commit();
                }
            }
        });

        clockCheckBox = (CheckBox) view.findViewById(R.id.clockCheckBox);
        clockCheckBox.setChecked(prefs.getBoolean(Const.IS_24_HOUR_CLOCK, false));
        clockCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    prefs.edit().putBoolean(Const.IS_24_HOUR_CLOCK, true).commit();
                } else {
                    prefs.edit().putBoolean(Const.IS_24_HOUR_CLOCK, false).commit();
                }
            }
        });

        unitsRadioGroup = (RadioGroup) view.findViewById(R.id.unitsRadioGroup);

        if (prefs.getBoolean(Const.IS_STANDARD_UNITS, false)) {
            unitsRadioGroup.check(R.id.standardRadioButton);
        } else {
            unitsRadioGroup.check(R.id.metricRadioButton);
        }
        unitsRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override public void onCheckedChanged(RadioGroup group, int checkedId) {
                SharedPreferences.Editor editor = prefs.edit();
                if (checkedId == R.id.standardRadioButton) {
                    editor.putBoolean(Const.IS_STANDARD_UNITS, true);
                    editor.putBoolean(Const.IS_METRIC_UNITS, false);
                } else if (checkedId == R.id.metricRadioButton) {
                    editor.putBoolean(Const.IS_STANDARD_UNITS, false);
                    editor.putBoolean(Const.IS_METRIC_UNITS, true);
                }
                editor.commit();
            }
        });
    }

    private void disable() {
        bluetoothSpinner.setEnabled(false);
        bluetoothCheckBox.setEnabled(false);
        sensorCheckBox.setEnabled(false);
        dockCheckBox.setEnabled(false);
        notificationsCheckBox.setEnabled(false);
        bluetoothButton.setEnabled(false);

        Resources resources = getResources();
        bluetoothSpinner.setVisibility(View.GONE);
        bluetoothCheckBox.setTextColor(resources.getColor(R.color.hint_text));
        sensorCheckBox.setTextColor(resources.getColor(R.color.hint_text));
        dockCheckBox.setTextColor(resources.getColor(R.color.hint_text));
        notificationsCheckBox.setTextColor(resources.getColor(R.color.hint_text));
    }

    @Override
    public void onResume() {
        super.onResume();

        ParkActivity pa = (ParkActivity) getActivity();
        if (pa.servicesConnected()) {
            sensorCheckBox = (CheckBox) getView().findViewById(R.id.sensorCheckBox);
            sensorCheckBox.setEnabled(true);
            sensorCheckBox.setChecked(prefs.getBoolean(Const.PARKING_SENSOR_KEY, false));
            sensorCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        prefs.edit().putBoolean(Const.PARKING_SENSOR_KEY, true).commit();
                        Intent autoParkServiceIntent = new Intent(getActivity(), AutoParkService.class);
                        autoParkServiceIntent.setAction(AutoParkService.ACTION_START);
                        getActivity().startService(autoParkServiceIntent);
                    } else {
                        prefs.edit().putBoolean(Const.PARKING_SENSOR_KEY, false).commit();
                        Intent autoParkServiceIntent = new Intent(getActivity(), AutoParkService.class);
                        autoParkServiceIntent.setAction(AutoParkService.ACTION_STOP);
                        getActivity().startService(autoParkServiceIntent);
                    }
                }
            });
        } else {
            sensorCheckBox.setEnabled(false);
        }

        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        if (adapter != null) {
            Set<BluetoothDevice> bondedDevices = adapter.getBondedDevices();
            if (bondedDevices == null || bondedDevices.size() == 0) {
                bluetoothSpinner.setVisibility(View.GONE);
                bluetoothCheckBox.setEnabled(false);
                bluetoothCheckBox.setTextColor(getResources().getColor(R.color.hint_text));

            } else {
                bluetoothSpinner.setVisibility(View.VISIBLE);
                bluetoothCheckBox.setEnabled(true);
                bluetoothCheckBox.setTextColor(getResources().getColor(R.color.default_text));
            }

            myBluetoothDevices.clear();
            if (bondedDevices != null) {
                for (BluetoothDevice bluetoothDevice : bondedDevices) {
                    myBluetoothDevices.add(new MyBluetoothDevice(bluetoothDevice.getName(), bluetoothDevice.getAddress()));
                }
            }
        }

        bluetoothAdapter = new ArrayAdapter<MyBluetoothDevice>(getActivity(), R.layout.spinner_item, myBluetoothDevices);
        bluetoothAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        bluetoothSpinner.setAdapter(bluetoothAdapter);

        if (prefs.contains(Const.BLUETOOTH_KEY)) {
            String address = prefs.getString(Const.BLUETOOTH_KEY, "");
            int i = 0;
            for (MyBluetoothDevice myBluetoothDevice : myBluetoothDevices) {
                if (myBluetoothDevice.address.equals(address)) {
                    bluetoothSpinner.setSelection(i);

                    break;
                }

                i++;
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    private class MyBluetoothDevice {

        private final String name, address;

        private MyBluetoothDevice(String n, String a) {
            name = n;
            address = a;
        }

        @Override
        public String toString() {
            return name;
        }
    }
}
