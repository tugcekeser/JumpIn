package com.example.tuze.bluecollar.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.tuze.bluecollar.R;
import com.example.tuze.bluecollar.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;


public class SignupActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 123;
    private static final String APP_TAG = "my_camera_app";
    private String mPhotoFileName = "photo.jpg";
    private static final String TAG = "RegisterActivity";
    private Vibrator vib;
    private Animation animShake;
    private FirebaseAuth auth;
    @BindView(R.id.signupInputName)
    EditText signupInputName;
    @BindView(R.id.signupInputEmail)
    EditText signupInputEmail;
    @BindView(R.id.signupInputPassword)
    EditText signupInputPassword;
    @BindView(R.id.signupInputDob)
    EditText signupInputDob;
    @BindView(R.id.signupInputLayoutName)
    TextInputLayout signupInputLayoutName;
    @BindView(R.id.signupInputLayoutEmail)
    TextInputLayout signupInputLayoutEmail;
    @BindView(R.id.signupInputLayoutPassword)
    TextInputLayout signupInputLayoutPassword;
    @BindView(R.id.signupInputLayoutDob)
    TextInputLayout signupInputLayoutDOB;
    @BindView(R.id.rbEmployer)
    RadioButton rbEmployer;
    @BindView(R.id.rbJobSeeker)
    RadioButton rbJobSeeker;
    @BindView(R.id.btnSignUp)
    Button btnSignUp;
    @BindView(R.id.ivAddPhoto)
    ImageView ivAddPhoto;
    @BindView(R.id.ivImage)
    ImageView ivImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);

        auth = FirebaseAuth.getInstance();
        animShake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
        vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        ivAddPhoto.setOnClickListener(this);
        btnSignUp.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.ivAddPhoto) {
            capturePhoto(view);
        } else if (id == R.id.btnSignUp) {
            saveProfile();
        }
    }

    private void saveProfile() {

        if (!checkName()) {
            signupInputName.setAnimation(animShake);
            signupInputName.startAnimation(animShake);
            vib.vibrate(120);
            return;
        }
        if (!checkEmail()) {
            signupInputEmail.setAnimation(animShake);
            signupInputEmail.startAnimation(animShake);
            vib.vibrate(120);
            return;
        }
        if (!checkPassword()) {
            signupInputPassword.setAnimation(animShake);
            signupInputPassword.startAnimation(animShake);
            vib.vibrate(120);
            return;
        }
        if (!checkDOB()) {
            signupInputDob.setAnimation(animShake);
            signupInputDob.startAnimation(animShake);
            vib.vibrate(120);
            return;
        }

        final User user = new User();
        user.setName(signupInputName.getText().toString());
        user.setEmail(signupInputEmail.getText().toString());
        user.setDob(signupInputDob.getText().toString());
        user.setPassword(signupInputPassword.getText().toString());

        if (rbJobSeeker.isChecked() == true)
            user.setType(1);

        if (rbEmployer.isChecked() == true)
            user.setType(2);

        signupInputLayoutName.setErrorEnabled(false);
        signupInputLayoutEmail.setErrorEnabled(false);
        signupInputLayoutPassword.setErrorEnabled(false);
        signupInputLayoutDOB.setErrorEnabled(false);

        auth.createUserWithEmailAndPassword(signupInputEmail.getText().toString(), signupInputPassword.getText().toString())
                .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());
                        //progressBar.setVisibility(View.GONE);

                        if (!task.isSuccessful()) {
                            Log.d(TAG, "Authentication failed." + task.getException());

                        } else {
                            String key = FirebaseDatabase.getInstance().getReference().child("users").push().getKey();
                            user.setUserId(key);
                            FirebaseDatabase.getInstance().getReference().child("user").child(key).setValue(user);

                            startActivity(new Intent(SignupActivity.this, HomeActivity.class).putExtra("User", user));
                            finish();
                        }
                    }
                });
        Toast.makeText(getApplicationContext(), "You are successfully Registered !!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Uri takenPhotoUri = getPhotoFileUri(mPhotoFileName);
                Bitmap rotatedBitmap = rotateBitmapOrientation(takenPhotoUri.getPath());
                Bitmap scaledBitmap = getScaledBitmap(rotatedBitmap);

                // Load the taken image into a preview
                ivImage.setImageBitmap(scaledBitmap);

            } else { // Result was a failure
                Toast.makeText(this, "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /*Take profile photo*/
    public void capturePhoto(View view) {
        // create Intent to take a picture and return control to the calling application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, getPhotoFileUri(mPhotoFileName)); // set the image file name

        if (intent.resolveActivity(getPackageManager()) != null) {
            // Start the image capture intent to take photo
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }

    public Uri getPhotoFileUri(String fileName) {
        // Only continue if the SD Card is mounted
        if (isExternalStorageAvailable()) {
            File mediaStorageDir = new File(
                    getExternalFilesDir(Environment.DIRECTORY_PICTURES), APP_TAG);

            // Create the storage directory if it does not exist
            if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
                Log.d(APP_TAG, "failed to create directory");
            }

            // Return the file target for the photo based on filename
            return Uri.fromFile(new File(mediaStorageDir.getPath() + File.separator + fileName));
        }
        return null;
    }

    public boolean isExternalStorageAvailable() {
        String state = Environment.getExternalStorageState();
        return state.equals(Environment.MEDIA_MOUNTED);
    }

    public Bitmap getScaledBitmap(Bitmap bitmap) {
        //Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, ivPreview.getWidth(), ivPreview.getHeight(), true);
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, 400, 200, true);
        return scaledBitmap;
    }

    public Bitmap rotateBitmapOrientation(String photoFilePath) {
        // Create and configure BitmapFactory
        BitmapFactory.Options bounds = new BitmapFactory.Options();
        bounds.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(photoFilePath, bounds);
        BitmapFactory.Options opts = new BitmapFactory.Options();
        Bitmap bm = BitmapFactory.decodeFile(photoFilePath, opts);
        // Read EXIF Data
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(photoFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String orientString = exif.getAttribute(ExifInterface.TAG_ORIENTATION);
        int orientation = orientString != null ? Integer.parseInt(orientString) : ExifInterface.ORIENTATION_NORMAL;
        int rotationAngle = 0;
        if (orientation == ExifInterface.ORIENTATION_ROTATE_90) rotationAngle = 90;
        if (orientation == ExifInterface.ORIENTATION_ROTATE_180) rotationAngle = 180;
        if (orientation == ExifInterface.ORIENTATION_ROTATE_270) rotationAngle = 270;
        // Rotate Bitmap
        Matrix matrix = new Matrix();
        matrix.setRotate(rotationAngle, (float) bm.getWidth() / 2, (float) bm.getHeight() / 2);
        Bitmap rotatedBitmap = Bitmap.createBitmap(bm, 0, 0, bounds.outWidth, bounds.outHeight, matrix, true);
        // Return result
        return rotatedBitmap;
    }


    /* Input Check */
    private boolean checkName() {
        if (signupInputName.getText().toString().trim().isEmpty()) {

            signupInputLayoutName.setErrorEnabled(true);
            signupInputLayoutName.setError(getString(R.string.err_msg_name));
            signupInputName.setError(getString(R.string.err_msg_required));
            return false;
        }
        signupInputLayoutName.setErrorEnabled(false);
        return true;
    }

    private boolean checkEmail() {
        String email = signupInputEmail.getText().toString().trim();
        if (email.isEmpty() || !isValidEmail(email)) {

            signupInputLayoutEmail.setErrorEnabled(true);
            signupInputLayoutEmail.setError(getString(R.string.err_msg_email));
            signupInputEmail.setError(getString(R.string.err_msg_required));
            requestFocus(signupInputEmail);
            return false;
        }
        signupInputLayoutEmail.setErrorEnabled(false);
        return true;
    }

    private boolean checkPassword() {
        if (signupInputPassword.getText().toString().trim().isEmpty()) {

            signupInputLayoutPassword.setError(getString(R.string.err_msg_password));
            requestFocus(signupInputPassword);
            return false;
        }
        signupInputLayoutPassword.setErrorEnabled(false);
        return true;
    }

    private boolean checkDOB() {

        try {
            boolean isDateValid = false;
            String[] s = signupInputDob.getText().toString().split("/");
            int date = Integer.parseInt(s[1]);
            int month = Integer.parseInt(s[0]);

            if (date < 32 && month < 13)
                isDateValid = true;

            if (signupInputDob.getText().toString().trim().isEmpty() && isDateValid) {

                signupInputLayoutDOB.setError(getString(R.string.err_msg_dob));
                requestFocus(signupInputDob);
                signupInputDob.setError(getString(R.string.err_msg_required));

                return false;
            }
        } catch (Exception ex) {
            signupInputLayoutDOB.setError(getString(R.string.err_msg_dob));
            requestFocus(signupInputDob);
            return false;
        }

        signupInputDob.setError(null);
        return true;
    }

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

}