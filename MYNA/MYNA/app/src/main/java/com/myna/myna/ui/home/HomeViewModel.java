package com.myna.myna.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeViewModel extends ViewModel {

    private final MutableLiveData<String> mText;
    private final MutableLiveData<String> mWelcomeMessage;
    // You could add more LiveData here for portfolio values, news headlines etc.
    // private final MutableLiveData<String> mPortfolioValue;

    public HomeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("INVEST CENTRE"); // Original text, keep for compatibility if XML still references it
        mWelcomeMessage = new MutableLiveData<>();
        mWelcomeMessage.setValue("Welcome, Investor!"); // Dynamic welcome message

        // Example for dynamic data:
        // mPortfolioValue = new MutableLiveData<>();
        // mPortfolioValue.setValue("₹ 2,50,000.00");
    }

    public LiveData<String> getText() {
        return mText;
    }

    public LiveData<String> getWelcomeMessage() {
        return mWelcomeMessage;
    }

    // Example setter for dynamic data from API/DB
    public void setWelcomeMessage(String message) {
        mWelcomeMessage.setValue(message);
    }

    // public void setPortfolioValue(String value) {
    //     mPortfolioValue.setValue(value);
    // }
    // public LiveData<String> getPortfolioValue() {
    //     return mPortfolioValue;
    // }
}