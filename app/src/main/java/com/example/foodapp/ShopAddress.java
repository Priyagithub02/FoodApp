package com.example.foodapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.GoogleMap;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

public class ShopAddress extends AppCompatActivity {
    EditText address_1, address_2, shop_landmark, postal_code;
    TextView maplocation;
    Spinner shop_state, shop_city;
    Button mapbutton, submit;
    String shopstate, shopcity;
    int PLACE_PICKER_REQUEST = 1;
    ArrayAdapter<CharSequence> state_Adapter, cityAdapter;
    DatabaseReference databaseReference;
    FirebaseAuth mAuth;

    @SuppressLint({"WrongViewCast", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_address);

        address_1 = findViewById(R.id.address1);
        address_2 = findViewById(R.id.address2);
        shop_state = findViewById(R.id.shopstate);
        shop_city = findViewById(R.id.shopcity);
        shop_landmark = findViewById(R.id.landmark);
        postal_code = findViewById(R.id.postalcode);
        mapbutton = findViewById(R.id.map);
        maplocation = findViewById(R.id.map_location);
        submit = findViewById(R.id.submitbuton);

        state_Adapter = ArrayAdapter.createFromResource(this, R.array.array_indian_states, R.layout.spinner_layout);
        databaseReference = FirebaseDatabase.getInstance().getReference();

        state_Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        shop_state.setAdapter(state_Adapter);
        shop_state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                shop_city = findViewById(R.id.shopcity);
                shopstate = shop_state.getSelectedItem().toString();
                int parentID = parent.getId();
                if (parentID == R.id.state) ;
                {
                    switch (shopstate) {
                        case "Select Your State":
                            cityAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                                    R.array.array_default_districts, R.layout.spinner_layout);
                            break;
                        case "Andhra Pradesh":
                            cityAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                                    R.array.array_andhra_pradesh_districts, R.layout.spinner_layout);
                            break;
                        case "Arunachal Pradesh":
                            cityAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                                    R.array.array_arunachal_pradesh_districts, R.layout.spinner_layout);
                            break;
                        case "Assam":
                            cityAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                                    R.array.array_assam_districts, R.layout.spinner_layout);
                            break;
                        case "Bihar":
                            cityAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                                    R.array.array_bihar_districts, R.layout.spinner_layout);
                            break;
                        case "Chhattisgarh":
                            cityAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                                    R.array.array_chhattisgarh_districts, R.layout.spinner_layout);
                            break;
                        case "Goa":
                            cityAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                                    R.array.array_goa_districts, R.layout.spinner_layout);
                            break;
                        case "Gujarat":
                            cityAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                                    R.array.array_gujarat_districts, R.layout.spinner_layout);
                            break;
                        case "Haryana":
                            cityAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                                    R.array.array_haryana_districts, R.layout.spinner_layout);
                            break;
                        case "Himachal Pradesh":
                            cityAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                                    R.array.array_himachal_pradesh_districts, R.layout.spinner_layout);
                            break;
                        case "Jharkhand":
                            cityAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                                    R.array.array_jharkhand_districts, R.layout.spinner_layout);
                            break;
                        case "Karnataka":
                            cityAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                                    R.array.array_karnataka_districts, R.layout.spinner_layout);
                            break;
                        case "Kerala":
                            cityAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                                    R.array.array_kerala_districts, R.layout.spinner_layout);
                            break;
                        case "Madhya Pradesh":
                            cityAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                                    R.array.array_madhya_pradesh_districts, R.layout.spinner_layout);
                            break;
                        case "Maharashtra":
                            cityAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                                    R.array.array_maharashtra_districts, R.layout.spinner_layout);
                            break;
                        case "Manipur":
                            cityAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                                    R.array.array_manipur_districts, R.layout.spinner_layout);
                            break;
                        case "Meghalaya":
                            cityAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                                    R.array.array_meghalaya_districts, R.layout.spinner_layout);
                            break;
                        case "Mizoram":
                            cityAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                                    R.array.array_mizoram_districts, R.layout.spinner_layout);
                            break;
                        case "Nagaland":
                            cityAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                                    R.array.array_nagaland_districts, R.layout.spinner_layout);
                            break;
                        case "Odisha":
                            cityAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                                    R.array.array_odisha_districts, R.layout.spinner_layout);
                            break;
                        case "Punjab":
                            cityAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                                    R.array.array_punjab_districts, R.layout.spinner_layout);
                            break;
                        case "Rajasthan":
                            cityAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                                    R.array.array_rajasthan_districts, R.layout.spinner_layout);
                            break;
                        case "Sikkim":
                            cityAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                                    R.array.array_sikkim_districts, R.layout.spinner_layout);
                            break;
                        case "Tamil Nadu":
                            cityAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                                    R.array.array_tamil_nadu_districts, R.layout.spinner_layout);
                            break;
                        case "Telangana":
                            cityAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                                    R.array.array_telangana_districts, R.layout.spinner_layout);
                            break;
                        case "Tripura":
                            cityAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                                    R.array.array_tripura_districts, R.layout.spinner_layout);
                            break;
                        case "Uttar Pradesh":
                            cityAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                                    R.array.array_uttar_pradesh_districts, R.layout.spinner_layout);
                            break;
                        case "Uttarakhand":
                            cityAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                                    R.array.array_uttarakhand_districts, R.layout.spinner_layout);
                            break;
                        case "West Bengal":
                            cityAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                                    R.array.array_west_bengal_districts, R.layout.spinner_layout);
                            break;
                        case "Andaman and Nicobar Islands":
                            cityAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                                    R.array.array_andaman_nicobar_districts, R.layout.spinner_layout);
                            break;
                        case "Chandigarh":
                            cityAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                                    R.array.array_chandigarh_districts, R.layout.spinner_layout);
                            break;
                        case "Dadra and Nagar Haveli":
                            cityAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                                    R.array.array_dadra_nagar_haveli_districts, R.layout.spinner_layout);
                            break;
                        case "Daman and Diu":
                            cityAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                                    R.array.array_daman_diu_districts, R.layout.spinner_layout);
                            break;
                        case "Delhi":
                            cityAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                                    R.array.array_delhi_districts, R.layout.spinner_layout);
                            break;
                        case "Jammu and Kashmir":
                            cityAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                                    R.array.array_jammu_kashmir_districts, R.layout.spinner_layout);
                            break;
                        case "Lakshadweep":
                            cityAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                                    R.array.array_lakshadweep_districts, R.layout.spinner_layout);
                            break;
                        case "Ladakh":
                            cityAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                                    R.array.array_ladakh_districts, R.layout.spinner_layout);
                            break;
                        case "Puducherry":
                            cityAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                                    R.array.array_puducherry_districts, R.layout.spinner_layout);
                            break;
                        default:
                            break;
                    }
                    shop_city.setAdapter(cityAdapter);

                    shop_city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            shopcity = shop_city.getSelectedItem().toString();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        mapbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                try {
                    startActivityForResult(builder.build(ShopAddress.this),PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String shop_add1 = address_1.getText().toString();
                String shop_add2 = address_2.getText().toString();
                String landmark = shop_landmark.getText().toString();
                String shopcity = shop_city.getSelectedItem().toString();
                String shopstate = shop_state.getSelectedItem().toString();
                String postalcode = postal_code.getText().toString();
                String location = maplocation.getText().toString();
                if(shop_add1.isEmpty()||shopcity.isEmpty()||shopstate.isEmpty()||postalcode.isEmpty()){
                    Toast.makeText(ShopAddress.this, "Enter address to continue", Toast.LENGTH_SHORT).show();
                }else{
                databaseReference = FirebaseDatabase.getInstance().getReference().child("SHOPS").child("SHOP ADDRESS").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                databaseReference.setValue(new ShopAddressList(shop_add1, shop_add2, landmark, shopcity, shopstate, postalcode,location));
                Intent i = new Intent(ShopAddress.this, ShopDocuments.class);
                startActivity(i);
                }
            }
        });
    }
    @Override
    protected void onActivityResult ( int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PLACE_PICKER_REQUEST){
            if(resultCode == RESULT_OK){
                Place place = PlacePicker.getPlace(data,this);
                StringBuilder stringBuilder = new StringBuilder();
                String address = String.format("Place: %s",place.getAddress());
                maplocation.setText(address);
            }
        }
    }
}



