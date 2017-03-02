package com.example.tuze.bluecollar.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.creativityapps.gmailbackgroundlibrary.BackgroundMail;
import com.example.tuze.bluecollar.R;
import com.example.tuze.bluecollar.model.User;

import org.parceler.Parcels;

/**
 * Created by tugce.
 */

public class ComposeMessageFragment extends DialogFragment {
    public static final String TITLE = "title";

    private EditText etMessage;
    private TextView tvCount;
    private Button btnSend;

    public ComposeMessageFragment() {

    }

    public interface ComposeTweetDialogListener {
        void onFinishEditDialog(String inputText);
    }

    public static ComposeMessageFragment newInstance(String title, User user,
                                                     User applicant) {
        ComposeMessageFragment frag = new ComposeMessageFragment();
        Bundle args = new Bundle();
        args.putString(TITLE, title);
        args.putParcelable("User", Parcels.wrap(user));
        args.putParcelable("Applicant", Parcels.wrap(applicant));
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_compose_message, container);
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Get field from view
        etMessage = (EditText) view.findViewById(R.id.etMessage);
        // Show soft keyboard automatically and request focus to field
        etMessage.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        tvCount = (TextView) view.findViewById(R.id.tvCount);
        etMessage.addTextChangedListener(textWatcher);
        btnSend = (Button) view.findViewById(R.id.btnSend);
        btnSend.setEnabled(false);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* BackgroundMail.newBuilder(getContext())
                        .withUsername("89904.keser@students.itu.edu")
                        .withPassword("!")
                        .withMailto("89904.keser@students.itu.edu")
                        .withSubject("Job Opportunity for you!")
                        .withBody(etMessage.getText().toString())
                        .withOnSuccessCallback(new BackgroundMail.OnSuccessCallback() {
                            @Override
                            public void onSuccess() {
                                //do some magic
                            }
                        })
                        .withOnFailCallback(new BackgroundMail.OnFailCallback() {
                            @Override
                            public void onFail() {
                                //do some magic
                            }
                        })
                        .send();*/

                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("message/rfc822");
                i.putExtra(Intent.EXTRA_EMAIL, new String[]{"89904.keser@students.itu.edu"});
                i.putExtra(Intent.EXTRA_SUBJECT, "Job Opportunity");
                i.putExtra(Intent.EXTRA_TEXT, etMessage.getText().toString());
                try {
                    startActivity(i);
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(getContext(), "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                }
                dismiss();
                // postTweet(etTweet.getText().toString());
                //sendBackResult();
            }
        });
    }

    //Send back results to parent fragment
    public void sendBackResult() {
        ComposeTweetDialogListener listener = (ComposeTweetDialogListener) getTargetFragment();
        listener.onFinishEditDialog(etMessage.getText().toString());
        dismiss();
    }


    //Tweet word counter
    TextWatcher textWatcher = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            int charsRemaining = 500 - s.length();
            tvCount.setText(Integer.toString(charsRemaining));

            if (charsRemaining >= 0 && charsRemaining < 500) {
                btnSend.setEnabled(true);
            } else {
                btnSend.setEnabled(false);
            }
        }
    };
}
