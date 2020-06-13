package com.example.dogsapps.view;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dogsapps.databinding.FragmentDetailBinding;
import com.example.dogsapps.model.DogBreed;
import com.example.dogsapps.R;
import com.example.dogsapps.Util.Util;
import com.example.dogsapps.ViewModel.DetailViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;


public class detailfragment extends Fragment {



    private  int dogUuid;
    private DetailViewModel viewModel;
    private FragmentDetailBinding binding;

//    @BindView(R.id.dogImage)
//    ImageView dogImage;
//
//
//    @BindView(R.id.dogName)
//    TextView dogName;
//
//    @BindView(R.id.dogPurpose)
//    TextView dogPurpose;
//
//    @BindView(R.id.dogTemperament)
//    TextView dogTemperament;
//
//    @BindView(R.id.dogLifeSpan)
//    TextView dogLifeSpan;

    public detailfragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       // View view = inflater.inflate(R.layout.fragment_detail,container,false);

//        ButterKnife.bind(this,view);
        FragmentDetailBinding binding = DataBindingUtil.inflate(inflater,R.layout.fragment_detail,container,false);
        this.binding = binding;
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(getArguments() != null){
            dogUuid = detailfragmentArgs.fromBundle(getArguments()).getDogUuid();

        }

        viewModel = ViewModelProviders.of(this).get(DetailViewModel.class);
        viewModel.fetch(dogUuid);

        observeViewModel();


    }

    private void observeViewModel() {
        viewModel.dogLiveData.observe(this,dogBreed -> {
            if(dogBreed != null && dogBreed instanceof DogBreed && getContext() != null){
//                dogName.setText(dogBreed.dogBreed);
//                dogPurpose.setText(dogBreed.breedFor);
//                dogTemperament.setText(dogBreed.temperament);
//                dogLifeSpan.setText(dogBreed.lifeSpan);
//                if(dogBreed.imageUrl != null){
//                    Util.loadImage(dogImage,dogBreed.imageUrl,new CircularProgressDrawable(getContext()));
//                }

                binding.setDog(dogBreed);
            }
        });
    }


}
