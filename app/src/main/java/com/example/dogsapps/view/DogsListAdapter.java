package com.example.dogsapps.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dogsapps.model.DogBreed;
import com.example.dogsapps.R;
import com.example.dogsapps.databinding.ItemDogBinding;

import java.util.ArrayList;
import java.util.List;

public class DogsListAdapter extends RecyclerView.Adapter<DogsListAdapter.DogViewHolder> implements DogClickListener {
    private ArrayList<DogBreed> dogsList;

    public DogsListAdapter(ArrayList<DogBreed> dogsList) {
        this.dogsList = dogsList;

    }

    public void updateDogsList(List<DogBreed> newDogsList) {
        dogsList.clear();
        dogsList.addAll(newDogsList);
        notifyDataSetChanged();

    }

    @NonNull
    @Override
    public DogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dog,parent,false);
//        return new DogViewHolder(view);


        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemDogBinding view = DataBindingUtil.inflate(inflater, R.layout.item_dog, parent, false);
        return new DogViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DogViewHolder holder, int position) {

//        ImageView image = holder.itemView.findViewById(R.id.imageView);
//        TextView name = holder.itemView.findViewById(R.id.name);
//        TextView lifespan = holder.itemView.findViewById(R.id.lifespan);
//        LinearLayout layout = holder.itemView.findViewById(R.id.dogLayout);
//
//        name.setText(dogsList.get(position).dogBreed);
//        lifespan.setText(dogsList.get(position).lifeSpan);
//        Util.loadImage(image,dogsList.get(position).imageUrl,Util.getProgressDrawable(image.getContext()));
//   1     layout.setOnClickListener(v  -> {
//            ListFragmentDirections.detailfragment action = ListFragmentDirections.detailFragment();
//
//            Navigation.findNavController(layout).navigate(action);
//            action.setDogUuid(dogsList.get(position).uuid);
//
//        });

//2        layout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ListFragmentDirections.detailfragment action = ListFragmentDirections.detailFragment();
//                action.setDogUuid(dogsList.get(position).uuid);
//                Navigation.findNavController(layout).navigate(action);
//            }
//        });


        holder.itemView.setDog(dogsList.get(position));
        holder.itemView.setListener(this);

    }

    @Override
    public void onDogClicked(View v) {
        String uuidString = ((TextView) v.findViewById(R.id.dogId)).getText().toString();
        int uuid = Integer.valueOf(uuidString);

        ListFragmentDirections.DetailFragment action = ListFragmentDirections.detailFragment();
        action.setDogUuid(uuid);
        Navigation.findNavController(v).navigate(action);
    }


    @Override
    public int getItemCount() {
        return dogsList.size();
    }

class DogViewHolder extends RecyclerView.ViewHolder {
    public ItemDogBinding itemView;

    public DogViewHolder(@NonNull ItemDogBinding itemView) {
        super(itemView.getRoot());
        this.itemView = itemView;
    }
}
}
