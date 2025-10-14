package com.example.dropshipping.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dropshipping.R;
import com.example.dropshipping.adapter.AddressAdapter;
import com.example.dropshipping.adapter.CheckoutProductAdapter;
import com.example.dropshipping.adapter.ProductAdapter;
import com.example.dropshipping.api.PostCallback;
import com.example.dropshipping.api.PostTask;
import com.example.dropshipping.model.CheckoutProduct;
import com.example.dropshipping.model.ShippingAddress;
import com.example.dropshipping.util.Messenger;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class OrderProductActivity extends AppCompatActivity implements CheckoutProductAdapter.OnQuantityChangedListener, PostCallback {

    private RecyclerView productsRecyclerView;
    private CheckoutProductAdapter productAdapter;
    private TextView subtotalPrice, shippingPrice, taxPrice, totalPrice;
    private MaterialButton placeOrderButton;
    private RadioGroup paymentMethodGroup;
    private double subtotal, shipping, tax;

    // Address views
    private LinearLayout noAddressLayout, addressDetailsLayout;
    private TextView addressLine, addressRegionCity, addressBrgyPostal;
    private MaterialButton btnAddAddress, btnChangeAddress;
    private View addressCard;

    // Address data
    private ShippingAddress selectedAddress;
    private List<ShippingAddress> addressList = new ArrayList<>();
    private static final String PREFS_NAME = "AddressPrefs";
    private static final String SELECTED_ADDRESS_ID = "selected_address_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_product);

        // Initialize views
        productsRecyclerView = findViewById(R.id.productsRecyclerView);
        subtotalPrice = findViewById(R.id.subtotalPrice);
        shippingPrice = findViewById(R.id.shippingPrice);
        taxPrice = findViewById(R.id.taxPrice);
        totalPrice = findViewById(R.id.totalPrice);
        placeOrderButton = findViewById(R.id.placeOrderButton);
        paymentMethodGroup = findViewById(R.id.paymentMethodGroup);

        // Initialize address views
        addressCard = findViewById(R.id.addressCard);
        noAddressLayout = findViewById(R.id.noAddressLayout);
        addressDetailsLayout = findViewById(R.id.addressDetailsLayout);

        addressLine = findViewById(R.id.addressLine);
        addressRegionCity = findViewById(R.id.addressRegionCity);
        addressBrgyPostal = findViewById(R.id.addressBrgyPostal);
        btnAddAddress = findViewById(R.id.btnAddAddress);
        btnChangeAddress = findViewById(R.id.btnChangeAddress);

        productsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        productAdapter = new CheckoutProductAdapter(getSampleProducts(), this);
        productsRecyclerView.setAdapter(productAdapter);

        calculateTotals();

        setupAddressSection();


        placeOrderButton.setOnClickListener(v -> placeOrder());
    }

    private void setupAddressSection() {

        addressCard.setOnClickListener(v -> showAddressSelectionBottomSheet());
        btnAddAddress.setOnClickListener(v -> showAddressFormBottomSheet(null));
        btnChangeAddress.setOnClickListener(v -> showAddressSelectionBottomSheet());

        loadSelectedAddress();
    }



    private void loadSelectedAddress() {
        try {
            JSONObject requestJson = new JSONObject();


            new PostTask(this, new PostCallback() {
                @Override
                public void onPostSuccess(String responseData) {
                    try {
                        JSONObject response = new JSONObject(responseData);
                        if (response.getString("status").equals("success")) {
                            JSONArray dataArray = response.getJSONArray("data");
                            addressList.clear();

                            // Parse all addresses
                            for (int i = 0; i < dataArray.length(); i++) {
                                JSONObject addressData = dataArray.getJSONObject(i);
                                ShippingAddress address = new ShippingAddress();
                                address.setId(addressData.getLong("address_id"));
                                address.setAddressLine(addressData.getString("address_line"));
                                address.setRegion(addressData.getString("region"));
                                address.setCity(addressData.getString("city"));
                                address.setBrgy(addressData.getString("brgy"));
                                address.setPostalCode(addressData.getString("postal_code"));
                                addressList.add(address);
                            }

                            SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
                            long addressId = prefs.getLong(SELECTED_ADDRESS_ID, -1);


                            for (ShippingAddress address : addressList) {
                                if (address.getId() == addressId) {
                                    selectedAddress = address;
                                    break;
                                }
                            }


                            if (selectedAddress == null && !addressList.isEmpty()) {
                                selectedAddress = addressList.get(0);
                            }


                            runOnUiThread(() -> {
                                updateAddressUI();
                            });

                        } else {
                            String errorMessage = response.optString("message", "No addresses found");
                            runOnUiThread(() -> {
                                Toast.makeText(OrderProductActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                                selectedAddress = null;
                                updateAddressUI();
                            });
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        runOnUiThread(() -> {
                            Toast.makeText(OrderProductActivity.this, "Error parsing addresses", Toast.LENGTH_SHORT).show();
                            selectedAddress = null;
                            updateAddressUI();
                        });
                    }
                }

                @Override
                public void onPostError(String errorMessage) {
                    runOnUiThread(() -> {
                        Toast.makeText(OrderProductActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                        selectedAddress = null;
                        updateAddressUI();
                    });
                }
            }, "error", "address/get-user-address.php").execute(requestJson);

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to load addresses", Toast.LENGTH_SHORT).show();
            selectedAddress = null;
            updateAddressUI();
        }
    }


    private void updateAddressUI() {
        if (selectedAddress != null) {
            noAddressLayout.setVisibility(View.GONE);
            addressDetailsLayout.setVisibility(View.VISIBLE);

            addressLine.setText(selectedAddress.getAddressLine());
            addressRegionCity.setText(selectedAddress.getRegion() + ", " + selectedAddress.getCity());
            addressBrgyPostal.setText(selectedAddress.getBrgy() + ", " + selectedAddress.getPostalCode());
        } else {
            noAddressLayout.setVisibility(View.VISIBLE);
            addressDetailsLayout.setVisibility(View.GONE);
        }
    }

    private void showAddressSelectionBottomSheet() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        View bottomSheetView = getLayoutInflater().inflate(R.layout.address_bottom_sheet, null);
        bottomSheetDialog.setContentView(bottomSheetView);

        RecyclerView addressRecyclerView = bottomSheetView.findViewById(R.id.addressRecyclerView);
        MaterialButton btnAddNewAddress = bottomSheetView.findViewById(R.id.btnAddNewAddress);

        AddressAdapter addressAdapter = new AddressAdapter(addressList, selectedAddress,
                address -> {
                    selectedAddress = address;

                    // Persist selection
                    SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
                    prefs.edit().putLong(SELECTED_ADDRESS_ID, address.getId()).apply();

                    updateAddressUI();
                    bottomSheetDialog.dismiss();
                },
                this::showAddressFormBottomSheet
        );


        addressRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        addressRecyclerView.setAdapter(addressAdapter);

        // Add new address button
        btnAddNewAddress.setOnClickListener(v -> {
            bottomSheetDialog.dismiss();
            showAddressFormBottomSheet(null);
        });

        bottomSheetDialog.show();
    }

    private void showAddressFormBottomSheet(ShippingAddress existingAddress) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        View bottomSheetView = getLayoutInflater().inflate(R.layout.address_form_bottom_sheet, null);
        bottomSheetDialog.setContentView(bottomSheetView);

        com.google.android.material.textfield.TextInputEditText etAddressLine =
                bottomSheetView.findViewById(R.id.etAddressLine);
        com.google.android.material.textfield.TextInputEditText etRegion =
                bottomSheetView.findViewById(R.id.etRegion);
        com.google.android.material.textfield.TextInputEditText etCity =
                bottomSheetView.findViewById(R.id.etCity);
        com.google.android.material.textfield.TextInputEditText etBrgy =
                bottomSheetView.findViewById(R.id.etBrgy);
        com.google.android.material.textfield.TextInputEditText etPostalCode =
                bottomSheetView.findViewById(R.id.etPostalCode);
        MaterialButton btnSaveAddress = bottomSheetView.findViewById(R.id.btnSaveAddress);


        if (existingAddress != null) {

            etAddressLine.setText(existingAddress.getAddressLine());
            etRegion.setText(existingAddress.getRegion());
            etCity.setText(existingAddress.getCity());
            etBrgy.setText(existingAddress.getBrgy());
            etPostalCode.setText(existingAddress.getPostalCode());
        }


        btnSaveAddress.setOnClickListener(v -> {

            String addressLine = etAddressLine.getText().toString().trim();
            String region = etRegion.getText().toString().trim();
            String city = etCity.getText().toString().trim();
            String brgy = etBrgy.getText().toString().trim();
            String postalCode = etPostalCode.getText().toString().trim();

            // Create/update address
            ShippingAddress address;
            if (existingAddress != null) {
                address = existingAddress;
            } else {
                address = new ShippingAddress();
                address.setId(System.currentTimeMillis());
            }


            address.setAddressLine(addressLine);
            address.setRegion(region);
            address.setCity(city);
            address.setBrgy(brgy);
            address.setPostalCode(postalCode);


            try {

                JSONObject addressJson = new JSONObject();

                addressJson.put("address_line", address.getAddressLine());
                addressJson.put("region", address.getRegion());
                addressJson.put("city", address.getCity());
                addressJson.put("brgy", address.getBrgy());
                addressJson.put("postal_code", address.getPostalCode());

                new PostTask(this, new PostCallback() {
                    @Override
                    public void onPostSuccess(String responseData) {
                        try {
                            JSONObject response = new JSONObject(responseData);
                            if (response.getString("status").equals("success")) {
                                Messenger.showAlertDialog(OrderProductActivity.this, "Address Saved", "Your address has been saved successfully.", "Ok").show();

                                selectedAddress = address;
                                updateAddressUI();
                                bottomSheetDialog.dismiss();
                            } else {
                                String errorMessage = response.optString("message", "Failed to save address");
                                onPostError(errorMessage);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            onPostError("An error occurred while saving the address.");
                        }
                    }

                    @Override
                    public void onPostError(String errorMessage) {
                        Messenger.showAlertDialog(OrderProductActivity.this, "Address Error", errorMessage, "Ok").show();

                    }
                }, "error", "address/save-address.php").execute(addressJson);

            }catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Failed to save address", Toast.LENGTH_SHORT).show();
            }


        });

        bottomSheetDialog.show();
    }


    private List<CheckoutProduct> getSampleProducts() {
        List<CheckoutProduct> products = new ArrayList<>();

        if (getIntent().hasExtra("checkoutProduct")) {
            CheckoutProduct product = getIntent().getParcelableExtra("checkoutProduct");
            if (product != null) {
                products.add(product);
            }
        }

        if (getIntent().hasExtra("checkout_products")) {
            String jsonProducts = getIntent().getStringExtra("checkout_products");
            if (jsonProducts != null && !jsonProducts.isEmpty()) {
                products.addAll(new Gson().fromJson(jsonProducts, new TypeToken<List<CheckoutProduct>>(){}.getType()));
            }
        }

        return products;
    }

    @Override
    public void onQuantityChanged(int position, int newQuantity) {
        calculateTotals();
    }

    private void calculateTotals() {
        subtotal = 0;
        double totalWeight = 0;
        double maxHeight = 0;

        // Calculate subtotal and collect shipping data
        for (CheckoutProduct product : productAdapter.getProductList()) {
            double productTotal = product.getPrice() * product.getQuantity();
            subtotal += productTotal;

            // Get product dimensions (you'll need to add these to your CheckoutProduct model)
            double productWeight = product.getWeight(); // in grams
            double productHeight = product.getLength(); // in cm

            // Accumulate total weight and track maximum height
            totalWeight += productWeight * product.getQuantity();
            maxHeight = Math.max(maxHeight, productHeight);
        }

        // Calculate shipping fee based on total weight and max height
        shipping = calculateShippingFeeWithQuantity(totalWeight, maxHeight);
        tax = subtotal * 0.12;

        subtotalPrice.setText(String.format("₱%.2f", subtotal));
        shippingPrice.setText(String.format("₱%.2f", shipping));
        taxPrice.setText(String.format("₱%.2f", tax));
        totalPrice.setText(String.format("₱%.2f", subtotal + shipping + tax));
    }

    private double shippingFeeByWeightandHeight(double weight, double height) {
        Log.d("ShippingFee", "Weight: " + weight + ", Height: " + height);
        if (weight <= 1000.0 && height <= 10.0) { // 1000g = 1kg
            return 50.00;
        } else if (weight <= 5000.0 && height <= 50.0) { // 5000g = 5kg
            return 100.00;
        } else if (weight <= 10000.0 && height <= 100.0) { // 10000g = 10kg
            return 200.00;
        } else {
            return 500.00;
        }
    }

    private double calculateShippingFeeWithQuantity(double totalWeightInGrams, double maxHeightInCm) {
        return shippingFeeByWeightandHeight(totalWeightInGrams, maxHeightInCm);
    }

    private void placeOrder() {

        if (selectedAddress == null) {
            Toast.makeText(this, "Please select a delivery address", Toast.LENGTH_SHORT).show();
            return;
        }

        int selectedId = paymentMethodGroup.getCheckedRadioButtonId();
        String paymentMethod = "";

        paymentMethod = "cod";

        JSONObject orderDetails = new JSONObject();
        try {
            orderDetails.put("payment_method", paymentMethod);
            orderDetails.put("subtotal", this.subtotal);
            orderDetails.put("shipping", this.shipping);
            orderDetails.put("tax", this.tax);
            orderDetails.put("total", this.subtotal + this.shipping + this.tax);

            // Add address
            orderDetails.put("shipping_address_id", selectedAddress.getId());

            JSONArray productsArray = new JSONArray();
            for (CheckoutProduct product : productAdapter.getProductList()) {
                JSONObject productObject = new JSONObject();
                productObject.put("pid", product.getPid());
                productObject.put("name", product.getName());
                productObject.put("price", product.getPrice());
                productObject.put("quantity", product.getQuantity());
                productObject.put("store_id", product.getStoreId());
                productObject.put("variation_id", product.getVariantId());
                if (product.getCartId() != null) {
                    productObject.put("cart_id", product.getCartId());
                }
                productsArray.put(productObject);
            }
            orderDetails.put("products", productsArray);


            new PostTask(this, this, "error", "order/place-order.php").execute(orderDetails);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPostSuccess(String responseData) {
        try {
            JSONObject response = new JSONObject(responseData);
            if (response.getString("status").equals("success")) {
                Messenger.showAlertDialog(this, "Order Success", "Your order has been placed successfully!", "Ok", "Back", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(OrderProductActivity.this, HeroActivity.class));
                    }
                }, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(OrderProductActivity.this, HeroActivity.class));
                    }
                }).show();

            } else {
                String errorMessage = response.optString("message", "Failed to place order");
                onPostError(errorMessage);
            }
        } catch (Exception e) {
            e.printStackTrace();
            onPostError("An error occurred while processing the order.");
        }
    }

    @Override
    public void onPostError(String errorMessage) {
        Messenger.showAlertDialog(this, "Order Error", errorMessage, "Ok").show();
    }

}