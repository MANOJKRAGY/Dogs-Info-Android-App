package com.example.dogsapps.ViewModel;

import android.app.Application;
import android.os.AsyncTask;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.dogsapps.model.DogBreed;
import com.example.dogsapps.model.DogDao;
import com.example.dogsapps.model.DogDatabase;
import com.example.dogsapps.model.DogsApiService;
import com.example.dogsapps.Util.SharedPreferencesHelper;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class ListViewModel extends AndroidViewModel {


    public MutableLiveData<List<DogBreed>> dogs = new MutableLiveData<List<DogBreed>>();
    public MutableLiveData<Boolean> dogLoadError = new MutableLiveData<Boolean>();
    public MutableLiveData<Boolean> loading = new MutableLiveData<Boolean>();


    private DogsApiService dogsService = new DogsApiService();
    private CompositeDisposable disposable = new CompositeDisposable();

    private AsyncTask<List<DogBreed> ,Void, List<DogBreed>> insertTask;
    private AsyncTask<Void,Void,List<DogBreed>> retrieveTask;

    private SharedPreferencesHelper prefHelper = SharedPreferencesHelper.getInstance(getApplication());
    private long refreshTime = 5* 60*1000*1000*1000L;

    public ListViewModel(@NonNull Application application) {
        super(application);
    }

    public void refresh() {

//        DogBreed dog1 = new DogBreed("1","corgi","15 years","","","","");
//        DogBreed dog2 = new DogBreed("2","rotwailer","10 years","","","","");
//        DogBreed dog3 = new DogBreed("3","labrador","12 years","","","","");
//
//        ArrayList<DogBreed> dogsList = new ArrayList<>();
//        dogsList.add(dog1);
//        dogsList.add(dog2);
//        dogsList.add(dog3);
//
//        dogs.setValue(dogsList);
//        dogLoadError.setValue(false);
//        loading.setValue(false);

        //fetchFromRemote();
        //fetchFromDatabase();


        long updateTime = prefHelper.getUpdateTime();
        long currentTime = System.nanoTime();
        if(updateTime != 0 && currentTime - updateTime <refreshTime){
            fetchFromDatabase();
        }else{
            fetchFromRemote();
        }
    }
    public void refreshBypassCache(){
        fetchFromRemote();
    }

    private void fetchFromDatabase(){
        loading.setValue(true);
        retrieveTask = new RetrieveDogTask();
        retrieveTask.execute();
    }

    private void fetchFromRemote() {
        loading.setValue(true);
        disposable.add(
                dogsService.getDogs()
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<List<DogBreed>>() {
                            @Override
                            public void onSuccess(List<DogBreed> dogBreeds) {
                               insertTask = new InsertDogsTask();
                               insertTask.execute(dogBreeds);
                                Toast.makeText(getApplication(),"Dogs retrieved from endpoint",Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onError(Throwable e) {
                                dogLoadError.setValue(true);
                                loading.setValue(false);
                                e.printStackTrace();
                            }
                        })
        );
    }
    private void dogsRetrieved(List<DogBreed> dogList){
        dogs.setValue(dogList);
        dogLoadError.setValue(false);
        loading.setValue(false);

    }



    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();

        if(insertTask != null){
            insertTask.cancel(true);
            insertTask = null;
        }

        if(retrieveTask != null){
            retrieveTask.cancel(true);
            retrieveTask = null;
        }
    }

    private  class InsertDogsTask extends AsyncTask<List<DogBreed> ,Void,List<DogBreed>> {

        @Override
        protected List<DogBreed> doInBackground(List<DogBreed>... lists) {
            List<DogBreed> list = lists[0];
            DogDao dao = DogDatabase.getInstance(getApplication()).dogDao();
            dao.deleteAllDogs();

            ArrayList<DogBreed> newList = new ArrayList<>(list);
            List<Long> result = dao.insertAll(newList.toArray(new DogBreed[0]));

            int i =0;
            while (i< list.size()){
                list.get(i).uuid = result.get(i).intValue();
                ++i;
            }
            return  list;
        }

        @Override
        protected void onPostExecute(List<DogBreed> dogBreeds) {
           dogsRetrieved(dogBreeds);
           prefHelper.saveUpdateTime(System.nanoTime());
        }
    }

    private  class RetrieveDogTask extends AsyncTask<Void,Void,List<DogBreed>>{

        @Override
        protected List<DogBreed> doInBackground(Void... voids) {
          return DogDatabase.getInstance(getApplication()).dogDao().getAllDogs();
        }

        @Override
        protected void onPostExecute(List<DogBreed> dogBreeds) {
            dogsRetrieved(dogBreeds);
            Toast.makeText(getApplication(),"Dogs retrieved from database",Toast.LENGTH_SHORT).show();

        }
    }

}
