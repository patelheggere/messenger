package com.patelheggere.messenger.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.patelheggere.messenger.R;
import com.patelheggere.messenger.model.MessageReply;
import com.patelheggere.messenger.network.ApiInterface;
import com.patelheggere.messenger.network.RetrofitInstance;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private Spinner mSpinnerReligion;
    private EditText mEditTextMessage;
    private Button mButtonSendSMS;
    private View root;
    private String selectedReligion = "all";
    private List<String> religions;
    private ApiInterface apiInterface;
    private ProgressBar mProgressBar;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        root = inflater.inflate(R.layout.fragment_home, container, false);
        final TextView textView = root.findViewById(R.id.text_home);
        homeViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        initViews();
        initData();
        initListeners();
        setUpNetwork();

        return root;
    }

    private void initViews() {
        mProgressBar = root.findViewById(R.id.progress_circular);
        mSpinnerReligion = root.findViewById(R.id.rel_spinner);
        mEditTextMessage = root.findViewById(R.id.edittextmsg);
        mButtonSendSMS = root.findViewById(R.id.msg_send);
    }
    private void initData() {
        religions = new ArrayList<String>();
        religions.add("all");
        religions.add("hindu");
        religions.add("muslim");
        religions.add("christian");
        religions.add("jain");
        religions.add("budha");

    }
    private void initListeners() {
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, religions);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        mSpinnerReligion.setAdapter(dataAdapter);

        mButtonSendSMS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mEditTextMessage.getText()!=null && !mEditTextMessage.getText().toString().trim().isEmpty())
                {
                    sendMessage(mEditTextMessage.getText().toString(), selectedReligion);
                }
                else {
                    mEditTextMessage.setError("Please type a message");
                }
            }
        });

        mSpinnerReligion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
                selectedReligion = religions.get(pos);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void sendMessage(String message, String selectedReligion) {

        mProgressBar.setVisibility(View.VISIBLE);
        enableDisable(false);
        Call<MessageReply> assignedTasksModelCall = apiInterface.sendSMS(message, selectedReligion);
        assignedTasksModelCall.enqueue(new Callback<MessageReply>() {
            @Override
            public void onResponse(Call<MessageReply> call, Response<MessageReply> response) {
                enableDisable(true);
                mProgressBar.setVisibility(View.GONE);
                if(response.isSuccessful())
                {
                    if (response.body() != null) {
                        mEditTextMessage.setText("");
                        Toast.makeText(getContext(), "Message sent to " + response.body().getCount() + " numbers", Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    Toast.makeText(getContext(), "Message not sent try again", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<MessageReply> call, Throwable t) {
                enableDisable(true);
                mProgressBar.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Message not sent try again", Toast.LENGTH_LONG).show();
            }
        });

    }

    private void enableDisable(boolean b) {
        mButtonSendSMS.setClickable(b);
        mButtonSendSMS.setEnabled(b);
        mButtonSendSMS.setEnabled(b);
    }

    private void setUpNetwork()
    {
        RetrofitInstance retrofitInstance = new RetrofitInstance();
        retrofitInstance.setClient();
        apiInterface = retrofitInstance.getClient().create(ApiInterface.class);
    }
}