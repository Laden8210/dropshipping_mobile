package com.example.dropshipping.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.dropshipping.R;
import com.example.dropshipping.model.ShippingAddress;

import java.util.List;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.AddressViewHolder> {

    private final List<ShippingAddress> addresses;
    private long selectedAddressId;
    private final AddressSelectionListener selectionListener;
    private final AddressEditListener editListener;

    public AddressAdapter(List<ShippingAddress> addresses,
                          ShippingAddress selectedAddress,
                          AddressSelectionListener selectionListener,
                          AddressEditListener editListener) {
        this.addresses = addresses;
        this.selectedAddressId = selectedAddress != null ? selectedAddress.getId() : -1;
        this.selectionListener = selectionListener;
        this.editListener = editListener;
    }

    @Override
    public AddressViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_address, parent, false);
        return new AddressViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AddressViewHolder holder, int position) {
        ShippingAddress address = addresses.get(position);

        holder.tvAddressLine.setText(address.getAddressLine());
        holder.tvRegionCity.setText(address.getRegion() + ", " + address.getCity());
        holder.tvBrgyPostal.setText(address.getBrgy() + ", " + address.getPostalCode());

        boolean isSelected = address.getId() == selectedAddressId;
        holder.radioSelected.setChecked(isSelected);

        // Clicking on whole item selects address
        holder.itemView.setOnClickListener(v -> {
            selectedAddressId = address.getId();
            notifyDataSetChanged(); // Refresh the list to reflect selection
            selectionListener.onAddressSelected(address);
        });

        // Clicking on radio also selects address
        holder.radioSelected.setOnClickListener(v -> {
            selectedAddressId = address.getId();
            notifyDataSetChanged();
            selectionListener.onAddressSelected(address);
        });

        // Edit button logic
        holder.btnEdit.setOnClickListener(v -> editListener.onEditAddress(address));
    }

    @Override
    public int getItemCount() {
        return addresses.size();
    }

    static class AddressViewHolder extends RecyclerView.ViewHolder {
        TextView tvAddressLine, tvRegionCity, tvBrgyPostal;
        RadioButton radioSelected;
        Button btnEdit;

        public AddressViewHolder(View itemView) {
            super(itemView);
            tvAddressLine = itemView.findViewById(R.id.tvAddressLine);
            tvRegionCity = itemView.findViewById(R.id.tvRegionCity);
            tvBrgyPostal = itemView.findViewById(R.id.tvBrgyPostal);
            radioSelected = itemView.findViewById(R.id.radioSelected);
            btnEdit = itemView.findViewById(R.id.btnEdit);
        }
    }

    public interface AddressSelectionListener {
        void onAddressSelected(ShippingAddress address);
    }

    public interface AddressEditListener {
        void onEditAddress(ShippingAddress address);
    }
}
