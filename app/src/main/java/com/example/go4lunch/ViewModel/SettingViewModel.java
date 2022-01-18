package com.example.go4lunch.ViewModel;

import android.content.Context;

import androidx.lifecycle.ViewModel;
import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.example.go4lunch.Repository.SharedPreferencesRepository;
import com.example.go4lunch.Util.NotificationWorker;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class SettingViewModel extends ViewModel {

    public SettingViewModel(SharedPreferencesRepository sharedPreferencesRepository) {
    this.sharedPreferencesRepository = sharedPreferencesRepository;
    }

    SharedPreferencesRepository sharedPreferencesRepository;

    public boolean getNotifications(Context requireContext) {
        return sharedPreferencesRepository.getNotifications(requireContext);
    }

    public void saveRadius(Context context, float value) {
        sharedPreferencesRepository.saveRadius(context, value);
    }

    public void saveNotifications(Context context, boolean notificationChoice) {
        SharedPreferencesRepository.saveNotifications(context, notificationChoice);
        if (notificationChoice) {
            createPeriodicRequest(context);
        } else {
            deletePeriodicRequest(context);
        }
    }

    private void createPeriodicRequest(Context context) {
        Calendar currentDate = Calendar.getInstance();
        Calendar dueDate = Calendar.getInstance();

        dueDate.set(Calendar.HOUR_OF_DAY, 12);
        dueDate.set(Calendar.MINUTE, 0);
        dueDate.set(Calendar.SECOND, 0);

        if(dueDate.before(currentDate)) {
            dueDate.add(Calendar.HOUR_OF_DAY, 24);
        }

        long timeDiff =  dueDate.getTimeInMillis() - currentDate.getTimeInMillis();
        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();

        final PeriodicWorkRequest myPeriodicWorkRequest = new PeriodicWorkRequest.Builder
                (NotificationWorker.class,1, TimeUnit.MILLISECONDS)
                .setInitialDelay(timeDiff, TimeUnit.MILLISECONDS)
                .addTag("NOTIFICATIONSJOB")
                .setConstraints(constraints)
                .build();

        WorkManager workManager =  WorkManager.getInstance(context);
        workManager.enqueue(myPeriodicWorkRequest);
    }

    private void deletePeriodicRequest(Context context) {
        WorkManager.getInstance(context).cancelAllWorkByTag("NOTIFICATIONSJOB");
    }

    public float getRadius(Context context) {
        return SharedPreferencesRepository.getRadius(context);
    }
}
