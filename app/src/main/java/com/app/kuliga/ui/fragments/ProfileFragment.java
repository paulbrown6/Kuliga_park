package com.app.kuliga.ui.fragments;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.app.kuliga.R;
import com.app.kuliga.ui.fragments.viewmodels.MainViewModel;
import com.app.kuliga.ui.fragments.viewmodels.ProfileViewModel;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ProfileFragment extends Fragment {

    private ProfileViewModel mViewModel;
    private boolean gender = false;
    private String genderText = "null";

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.profile_fragment_fulljava, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        View mView = getView();
        ConstraintLayout loadingProgress = mView.findViewById(R.id.profile_progress);
        TextView textError = mView.findViewById(R.id.profile_text_error);
        Button saveButton = mView.findViewById(R.id.profile_button_save);
        saveButton.setEnabled(false);
        Button cancelButton = mView.findViewById(R.id.profile_button_cancel);
        EditText lastName = mView.findViewById(R.id.profile_input_last_name);
        EditText name = mView.findViewById(R.id.profile_input_name);
        EditText middleName = mView.findViewById(R.id.profile_input_middle_name);
        EditText birthdate = mView.findViewById(R.id.profile_input_birthdate);
        CheckBox checkMale = mView.findViewById(R.id.profile_check_male);
        CheckBox checkFemale = mView.findViewById(R.id.profile_check_female);
        EditText number = mView.findViewById(R.id.profile_input_number);
        EditText email = mView.findViewById(R.id.profile_input_login);
        ImageButton calendarButton = mView.findViewById(R.id.profile_button_calendar);
        ImageView imageOk = mView.findViewById(R.id.profile_img_ok);
        ProgressBar progressBar = mView.findViewById(R.id.profile_bar);

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().equals(number.getText().toString())){
                    numberCorrect(number);
                }
                if (s.toString().equals(birthdate.getText().toString())){
                    dateCorrect(birthdate);
                }
                mViewModel.profileDataChanged(
                        lastName.getText().toString(), name.getText().toString(),
                        middleName.getText().toString(), birthdate.getText().toString(),
                        number.getText().toString(), email.getText().toString(), gender);
            }
        };
        lastName.addTextChangedListener(textWatcher);
        name.addTextChangedListener(textWatcher);
        middleName.addTextChangedListener(textWatcher);
        birthdate.addTextChangedListener(textWatcher);
        number.addTextChangedListener(textWatcher);
        email.addTextChangedListener(textWatcher);
        checkMale.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                checkFemale.setChecked(!b);
                gender = true;
                mViewModel.profileDataChanged(
                        lastName.getText().toString(), name.getText().toString(),
                        middleName.getText().toString(), birthdate.getText().toString(),
                        number.getText().toString(), email.getText().toString(), gender);
                if (b) genderText = "male";
            }
        });
        checkFemale.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                checkMale.setChecked(!b);
                gender = true;
                mViewModel.profileDataChanged(
                        lastName.getText().toString(), name.getText().toString(),
                        middleName.getText().toString(), birthdate.getText().toString(),
                        number.getText().toString(), email.getText().toString(), gender);
                if (b) genderText = "female";
            }
        });
        MaterialDatePicker datePicker =
                MaterialDatePicker.Builder.datePicker()
                        .setTitleText("Выбрать дату")
                        .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                        .build();
        datePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @SuppressLint("SimpleDateFormat")
            @Override
            public void onPositiveButtonClick(Object selection) {
                Date date = new Date();
                date.setTime((long) selection);
                birthdate.setText(new SimpleDateFormat("dd.MM.yyyy").format(date));
            }
        });
        calendarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePicker.show(requireActivity().getSupportFragmentManager(), "profileDate");
            }
        });

        mViewModel.getState().observe(getViewLifecycleOwner(), new Observer<ProfileViewModel.ProfileFormState>() {
            @Override
            public void onChanged(@Nullable ProfileViewModel.ProfileFormState state) {
                if (state == null) {
                    return;
                }
                if (state.isDataValid() && gender) {
                    saveButton.setEnabled(true);
                } else {
                    saveButton.setEnabled(false);
                }
                if (state.getLastNameError() != null) {
                    lastName.setError(getString(state.getLastNameError()));
                }
                if (state.getNameError() != null) {
                    name.setError(getString(state.getNameError()));
                }
                if (state.getMiddleNameError() != null) {
                    middleName.setError(getString(state.getMiddleNameError()));
                }
                if (state.getBirthdateError() != null) {
                    birthdate.setError(getString(state.getBirthdateError()));
                }
                if (state.getNumberError() != null) {
                    number.setError(getString(state.getNumberError()));
                }
                if (state.getEmailError() != null) {
                    email.setError(getString(state.getEmailError()));
                }
            }
        });
        mViewModel.getResult().observe(getViewLifecycleOwner(), new Observer<ProfileViewModel.ProfileResult>() {
            @Override
            public void onChanged(@Nullable ProfileViewModel.ProfileResult regResult) {
                textError.setVisibility(View.INVISIBLE);
                saveButton.setEnabled(true);
                if (regResult == null) {
                    loadingProgress.setVisibility(View.GONE);
                    return;
                }
                if (regResult.getError() != null) {
                    textError.setText(regResult.getError());
                    textError.setVisibility(View.VISIBLE);
                    loadingProgress.setVisibility(View.GONE);
                }
                if (regResult.getSuccess() != null) {
                    progressBar.setVisibility(View.GONE);
                    imageOk.setVisibility(View.VISIBLE);
                    MainViewModel.updateUser(regResult.getSuccess());
                    new CountDownTimer( 2000, 1000) {
                        public void onTick(long millisUntilFinished) { }
                        public void onFinish() {
                            loadingProgress.setVisibility(View.GONE);
                        }
                    }.start();
                }
            }
        });

        lastName.setText(MainViewModel.getUser().getLastName());
        name.setText(MainViewModel.getUser().getFirstName());
        middleName.setText(MainViewModel.getUser().getMiddleName());
        birthdate.setText(MainViewModel.getUser().getBirthday());
        genderText = MainViewModel.getUser().getGender();
        if (genderText != null){
            gender = true;
            if (genderText.equals("male")){
                checkMale.setChecked(true);
            } else {
                checkFemale.setChecked(true);
            }
        }
        number.setText(MainViewModel.getUser().getPhone());
        email.setText(MainViewModel.getUser().getEmail());

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveButton.setEnabled(false);
                loadingProgress.setVisibility(View.VISIBLE);
                mViewModel.setProfile(lastName.getText().toString(), name.getText().toString(),
                        middleName.getText().toString(), dateFormat(birthdate.getText().toString()),
                        genderText, number.getText().toString());
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lastName.setText(MainViewModel.getUser().getLastName());
                name.setText(MainViewModel.getUser().getFirstName());
                middleName.setText(MainViewModel.getUser().getMiddleName());
                birthdate.setText(MainViewModel.getUser().getBirthday());
                genderText = MainViewModel.getUser().getGender();
                if (genderText != null){
                    gender = true;
                    if (genderText.equals("male")){
                        checkMale.setChecked(true);
                    } else {
                        checkFemale.setChecked(true);
                    }
                }
                number.setText(MainViewModel.getUser().getPhone());
                email.setText(MainViewModel.getUser().getEmail());
            }
        });
    }

    private void numberCorrect(EditText number){
        String text = number.getText().toString();
        Integer textLength = text.length();
        if (!(text.endsWith("-") || text.endsWith(" "))) {
            if (textLength > 0 && textLength < 5) {
                if (!text.contentEquals("+7 (")) {
                    number.setText("+7 (");
                    number.setSelection(number.getText().length());
                }
            } else if (textLength == 8) {
                if (!text.contains(")")) {
                    number.setText(
                            new StringBuilder(text).insert(text.length() - 1, ") ").toString());
                    number.setSelection(number.getText().length());
                }
            } else if (textLength == 13) {
                number.setText(new StringBuilder(text).insert(text.length() - 1, "-").toString());
                number.setSelection(number.getText().length());
            } else if (textLength == 14) {
                if (!text.contains("-")) {
                    number.setText(new StringBuilder(text).insert(text.length() - 1, "-").toString());
                    number.setSelection(number.getText().length());
                }
            } else if (textLength == 16) {
                number.setText(new StringBuilder(text).insert(text.length() - 1, "-").toString());
                number.setSelection(number.getText().length());
            }
        }
        if (textLength > 18) {
            number.setText(new StringBuilder(text).substring(0, 18));
            number.setSelection(number.getText().length());
        }
    }

    private void dateCorrect(EditText date){
        String text = date.getText().toString();
        Integer textLength = text.length();
        if (!text.endsWith(".")) {
            if (textLength == 3) {
                if (!text.contains(".")) {
                    date.setText(
                            new StringBuilder(text).insert(text.length() - 1, ".").toString());
                    date.setSelection(date.getText().length());
                }
            } else if (textLength == 6) {
                date.setText(
                        new StringBuilder(text).insert(text.length() - 1, ".").toString());
                date.setSelection(date.getText().length());
            }
        }
        if (textLength > 10) {
            date.setText(new StringBuilder(text).substring(0, 10));
            date.setSelection(date.getText().length());
        }
    }

    private String dateFormat(String s){
        return s.split("\\.")[2] + s.split("\\.")[1] + s.split("\\.")[0];
    }
}