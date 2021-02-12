package com.openclassrooms.realestatemanager.ui.createAndEditEstate;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.media.MediaSync;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.databinding.ActivityAddBinding;
import com.openclassrooms.realestatemanager.databinding.EstateFormBinding;
import com.openclassrooms.realestatemanager.injections.Injection;
import com.openclassrooms.realestatemanager.injections.ViewModelFactory;
import com.openclassrooms.realestatemanager.models.Estate;
import com.openclassrooms.realestatemanager.models.PhotoDescription;
import com.openclassrooms.realestatemanager.models.UriList;
import com.openclassrooms.realestatemanager.ui.BaseActivity;
import com.openclassrooms.realestatemanager.ui.EstateViewModel;
import com.openclassrooms.realestatemanager.ui.PhotoAdapter;
import com.openclassrooms.realestatemanager.utils.ItemClickSupport;
import com.openclassrooms.realestatemanager.utils.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;


public class AddEditActivity extends BaseActivity implements View.OnClickListener {

    protected final int PICK_IMAGE_CAMERA = 1;
    protected final int PICK_IMAGE_GALLERY = 2;
    protected final int PICK_VIDEO_CAMERA = 3;
    protected final int PICK_VIDEO_GALLERY = 4;

    private ActivityAddBinding activityAddBinding;
    private EstateFormBinding estateFormBinding;
    private DatePickerDialog mUpOfSaleDateDialog;
    private DatePickerDialog mSoldDate;
    private SimpleDateFormat mDateFormat;
    private EstateViewModel estateViewModel;
    private VideoView videoView;
    private static final String VIDEO_DIRECTORY = "/realEstateManager";
    private int GALLERY = 1, CAMERA = 2;
    private PhotoAdapter adapter;
    private long mandateNumberID;
    private List<Uri> listPhoto;
    private Bitmap selectedImage;
    private String currentPhotoPath;
    private Uri photoUri;
    private MaterialAlertDialogBuilder builder;
    private MaterialAlertDialogBuilder builderVideo;
    private UriList photo = new UriList();
    private PhotoDescription photoText = new PhotoDescription();
    private UriList video = new UriList();
    private long estateEdit;
    private long idEstate;
    private Estate estate;
    public DialogInterface dialog;
    private Cursor cursor;
    private File newfile;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //for view binding
        activityAddBinding = ActivityAddBinding.inflate(getLayoutInflater());
        estateFormBinding = activityAddBinding.includeForm;

        View view = activityAddBinding.getRoot();
        setContentView(view);

        estateFormBinding.videoView.setVisibility(View.INVISIBLE);
        if(estateEdit==0) {
            Objects.requireNonNull(estateFormBinding.deleteVideo).setVisibility(View.INVISIBLE);
        }
        this.methodRequiresTwoPermission();
        this.configureToolbar();
        this.configureUpButton();
        this.dropDownAdapters();
        this.setDateField();
        this.configureViewModel();
        this.configureRecyclerView();
        this.onClickPhotoBtn();
        this.onClickVideoBtn();
        this.onClickValidateBtn();
        this.configureOnClickRecyclerView();
        this.onClickDeleteVideo();


        //for title toolbar
        ActionBar ab = getSupportActionBar();
        if (estateEdit == 0) {
            Objects.requireNonNull(ab).setTitle("Create Estate");
        } else {
            Objects.requireNonNull(ab).setTitle("Edit Estate");
        }
        //For date picker
        mDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE);
        //For hide mandate number
        estateFormBinding.etMandate.setVisibility(View.INVISIBLE);
        estateFormBinding.inputMandate.setVisibility(View.INVISIBLE);
        //for textWatcher
        estateFormBinding.etSurface.addTextChangedListener(estateWatcher);
        estateFormBinding.etRooms.addTextChangedListener(estateWatcher);
        estateFormBinding.etBedrooms.addTextChangedListener(estateWatcher);
        estateFormBinding.etBathrooms.addTextChangedListener(estateWatcher);
        estateFormBinding.etPrice.addTextChangedListener(estateWatcher);
        estateFormBinding.etDescription.addTextChangedListener(estateWatcher);
        estateFormBinding.etAddress.addTextChangedListener(estateWatcher);
        estateFormBinding.etPostalCode.addTextChangedListener(estateWatcher);
        estateFormBinding.etCity.addTextChangedListener(estateWatcher);
        estateFormBinding.etAgent.addTextChangedListener(estateWatcher);
        //for video in edit
        estateFormBinding.videoView.requestFocus();
        MediaController mediaController = new MediaController(this);
        estateFormBinding.videoView.setMediaController(mediaController);
        mediaController.setAnchorView(estateFormBinding.videoView);
        estateFormBinding.videoView.start();

    }

    /**
     * RecyclerView for photos
     */

    public void configureRecyclerView() {

        listPhoto = new ArrayList<>();

        adapter = new PhotoAdapter(listPhoto, Glide.with(this), photoText.getPhotoDescription(), estateEdit);
        LinearLayoutManager horizontalLayoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        Objects.requireNonNull(estateFormBinding.rvPhoto).setLayoutManager(horizontalLayoutManager);
        estateFormBinding.rvPhoto.setAdapter(adapter);
    }

    /**
     * Adapter generic for dropdown
     *
     * @param resId
     * @return
     */
    private ArrayAdapter<String> factoryAdapter(int resId) {
        return new ArrayAdapter<>(this, R.layout.dropdown_menu_popup_item, getResources().getStringArray(resId));
    }

    /**
     * For set adapters dropdown
     */
    public void dropDownAdapters() {

        estateFormBinding.etEstate.setAdapter(factoryAdapter(R.array.ESTATES));
        estateFormBinding.etRooms.setAdapter(factoryAdapter(R.array.ROOMS));
        estateFormBinding.etBedrooms.setAdapter(factoryAdapter(R.array.BEDROOMS));
        estateFormBinding.etBathrooms.setAdapter(factoryAdapter(R.array.BATHROOMS));
        estateFormBinding.etAgent.setAdapter(factoryAdapter(R.array.AGENT));
    }

    /**
     * for date picker
     */
    private void setDateField() {
        estateFormBinding.upOfSaleDate.setOnClickListener(this);
        estateFormBinding.soldDate.setOnClickListener(this);
        //For up of sale date
        Calendar newCalendar = Calendar.getInstance();
        mUpOfSaleDateDialog = new DatePickerDialog(this, (view, year, monthOfYear, dayOfMonth) -> {
            Calendar newDate = Calendar.getInstance();
            newDate.set(year, monthOfYear, dayOfMonth);
            estateFormBinding.upOfSaleDate.setText(mDateFormat.format(newDate.getTime()));
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        //For sold date
        mSoldDate = new DatePickerDialog(this, (view, year, monthOfYear, dayOfMonth) -> {
            Calendar newDate = Calendar.getInstance();
            newDate.set(year, monthOfYear, dayOfMonth);
            estateFormBinding.soldDate.setText(mDateFormat.format(newDate.getTime()));
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }

    /**
     * For click on date picker
     *
     * @param view
     */
    @Override
    public void onClick(View view) {
        if (view == estateFormBinding.upOfSaleDate) {
            mUpOfSaleDateDialog.show();
            mUpOfSaleDateDialog.getDatePicker().setMaxDate((Calendar.getInstance().getTimeInMillis()));

        } else if (view == estateFormBinding.soldDate) {
            mSoldDate.show();
            mSoldDate.getDatePicker().setMaxDate(Calendar.getInstance().getTimeInMillis());
        }
    }

    /**
     * For click on validate button
     */
    public void onClickValidateBtn() {

        estateFormBinding.validateFabBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //For description photo in recyclerView
                ArrayList<String> photoDescriptionList = new ArrayList<>();
                for (int i = 0; i < photoText.getPhotoDescription().size(); i++) {
                    AppCompatEditText editText = Objects.requireNonNull(Objects.requireNonNull(activityAddBinding.includeForm.rvPhoto.getLayoutManager()).findViewByPosition(i)).findViewById(R.id.photo_description);
                    String desc = Objects.requireNonNull(editText.getText()).toString();
                    photoDescriptionList.add(desc);
                }
                photoText.setPhotoDescription(photoDescriptionList);

                fieldsRequired();
                soldDateRequired();
                if (!soldDateRequired()){
                    return;
                }
                saveEstates();
            }
        });
    }

    /**
     * For dates whein sold is checked
     *
     * @return
     */
    public boolean soldDateRequired() {
        String soldDateInput = Objects.requireNonNull(estateFormBinding.inputSoldDate.getEditText()).getText().toString();
        if (estateFormBinding.availableRadiobtn.isChecked() && soldDateInput.isEmpty()) {
            estateFormBinding.soldDate.setError("Required");
            return false;
        }
        return true;
    }

    /**
     * For fields required in Estate form
     *
     * @return
     */
    public boolean fieldsRequired() {

        String surfaceInput = Objects.requireNonNull(estateFormBinding.inputSurface.getEditText()).getText().toString().trim();
        String roomsInput = Objects.requireNonNull(estateFormBinding.inputRooms.getEditText()).getText().toString().trim();
        String bedroomsInput = Objects.requireNonNull(estateFormBinding.inputBedrooms.getEditText()).getText().toString().trim();
        String bathroomsInput = Objects.requireNonNull(estateFormBinding.inputBathrooms.getEditText()).getText().toString().trim();
        String priceInput = Objects.requireNonNull(estateFormBinding.inputPrice.getEditText()).getText().toString().trim();
        String descriptionInput = Objects.requireNonNull(estateFormBinding.inputDescription.getEditText()).getText().toString().trim();
        String addressInput = Objects.requireNonNull(estateFormBinding.inputAddress.getEditText()).getText().toString().trim();
        String postalCodeInput = Objects.requireNonNull(estateFormBinding.inputPostalCode.getEditText()).getText().toString().trim();
        String cityInput = Objects.requireNonNull(estateFormBinding.inputCity.getEditText()).getText().toString().trim();
        String agentInput = Objects.requireNonNull(estateFormBinding.inputAgent.getEditText()).getText().toString();

        if (surfaceInput.isEmpty() && roomsInput.isEmpty() && bedroomsInput.isEmpty() && bathroomsInput.isEmpty() && priceInput.isEmpty()
                && descriptionInput.isEmpty() && addressInput.isEmpty() && postalCodeInput.isEmpty() && cityInput.isEmpty() && agentInput.isEmpty()) {
            estateFormBinding.etSurface.setError("Required");
            estateFormBinding.etRooms.setError("Required");
            estateFormBinding.etBedrooms.setError("Required");
            estateFormBinding.etBathrooms.setError("Required");
            estateFormBinding.etPrice.setError("Required");
            estateFormBinding.etDescription.setError("Required");
            estateFormBinding.etAddress.setError("Required");
            estateFormBinding.etPostalCode.setError("Required");
            estateFormBinding.etCity.setError("Required");
            estateFormBinding.etAgent.setError("Required");

            return false;
        }
        return true;
    }

    /**
     * For invalidate submit button if no all fields required are fill up
     */
    private TextWatcher estateWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String surfaceInput = Objects.requireNonNull(estateFormBinding.inputSurface.getEditText()).getText().toString().trim();
            String roomsInput = Objects.requireNonNull(estateFormBinding.inputRooms.getEditText()).getText().toString().trim();
            String bedroomsInput = Objects.requireNonNull(estateFormBinding.inputBedrooms.getEditText()).getText().toString().trim();
            String bathroomsInput = Objects.requireNonNull(estateFormBinding.inputBathrooms.getEditText()).getText().toString().trim();
            String priceInput = Objects.requireNonNull(estateFormBinding.inputPrice.getEditText()).getText().toString().trim();
            String descriptionInput = Objects.requireNonNull(estateFormBinding.inputDescription.getEditText()).getText().toString().trim();
            String addressInput = Objects.requireNonNull(estateFormBinding.inputAddress.getEditText()).getText().toString().trim();
            String postalCodeInput = Objects.requireNonNull(estateFormBinding.inputPostalCode.getEditText()).getText().toString().trim();
            String cityInput = Objects.requireNonNull(estateFormBinding.inputCity.getEditText()).getText().toString().trim();
            String agentInput = Objects.requireNonNull(estateFormBinding.inputAgent.getEditText()).getText().toString();

            estateFormBinding.validateFabBtn.setEnabled(!surfaceInput.isEmpty() && !roomsInput.isEmpty() && !bedroomsInput.isEmpty()
                    && !bathroomsInput.isEmpty() && !priceInput.isEmpty() && !descriptionInput.isEmpty() && !addressInput.isEmpty() && !postalCodeInput.isEmpty()
                    && !cityInput.isEmpty() && !agentInput.isEmpty());
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    /**
     * For save in database
     */
    public void saveEstates() {

        int ground = 0;

        if (!estateFormBinding.etGround.getText().toString().isEmpty()) {
            ground = Integer.parseInt(estateFormBinding.etGround.getText().toString());
        }
        Estate estate = new Estate(

                mandateNumberID,
                estateFormBinding.etEstate.getText().toString(),
                Integer.parseInt(Objects.requireNonNull(estateFormBinding.etSurface.getText()).toString()),
                Integer.parseInt(estateFormBinding.etRooms.getText().toString().replace("5 et +", "5")),
                Integer.parseInt(estateFormBinding.etBedrooms.getText().toString().replace("5 et +", "5")),
                Integer.parseInt(estateFormBinding.etBathrooms.getText().toString().replace("4 et +", "4")),
                ground,
                Double.parseDouble(Objects.requireNonNull((estateFormBinding.etPrice.getText())).toString()),
                Objects.requireNonNull(estateFormBinding.etDescription.getText()).toString(),
                Objects.requireNonNull(estateFormBinding.etAddress.getText()).toString(),
                Integer.parseInt(Objects.requireNonNull(estateFormBinding.etPostalCode.getText()).toString()),
                Objects.requireNonNull(estateFormBinding.etCity.getText()).toString(),
                estateFormBinding.boxSchools.isChecked(),
                estateFormBinding.boxStores.isChecked(),
                estateFormBinding.boxPark.isChecked(),
                estateFormBinding.boxRestaurants.isChecked(),
                estateFormBinding.availableRadiobtn.isChecked(),
                Utils.dateStringToLong(Objects.requireNonNull(estateFormBinding.upOfSaleDate.getText()).toString()),
                Objects.requireNonNull(estateFormBinding.soldDate.getText()).toString(),
                estateFormBinding.etAgent.getText().toString(),
                photo,
                photoText,
                video);
        Log.d("saveEstate", "saveEstate" + estate);
        if (estateEdit == 0) {
            this.estateViewModel.createEstate(estate);
            Snackbar.make(activityAddBinding.getRoot(), "Your new Estate is created", Snackbar.LENGTH_SHORT)
                    .addCallback(new Snackbar.Callback() {
                        @Override
                        public void onDismissed(Snackbar snackbar, int event) {
                            super.onDismissed(snackbar, event);
                            finish();
                        }
                    })
                    .show();
        } else {
            this.estateViewModel.updateEstate(estate);
            Snackbar.make(activityAddBinding.getRoot(), "Your new Estate is updated", Snackbar.LENGTH_SHORT)
                    .addCallback(new Snackbar.Callback() {
                        @Override
                        public void onDismissed(Snackbar snackbar, int event) {
                            super.onDismissed(snackbar, event);
                            finish();
                        }
                    }).show();
        }
    }

    /**
     * Configure ViewModel
     */
    private void configureViewModel() {
        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory(this);
        this.estateViewModel = ViewModelProviders.of(this, viewModelFactory).get(EstateViewModel.class);
        //for retrieve id from click edit button
        Intent intent = new Intent(Objects.requireNonNull(this.getIntent()));
        estateEdit = intent.getLongExtra("iDEstate", idEstate);

        Log.d("idEdit", String.valueOf(estateEdit));

        this.estateViewModel.getEstate(estateEdit).observe(this, this::updateUIFromEdit);
    }

    /**
     * For edit
     *
     * @param estate
     */
    @SuppressLint("SetTextI18n")
    private void updateUIFromEdit(Estate estate) {
        this.estate = estate;

        if (estate != null) {

            mandateNumberID = estate.getMandateNumberID();
            estateFormBinding.etMandate.setText(String.valueOf(estate.getMandateNumberID()));
            estateFormBinding.etEstate.setText(estate.getEstateType(), false);
            estateFormBinding.etSurface.setText(Objects.requireNonNull(estate.getSurface()).toString());
            estateFormBinding.etDescription.setText(estate.getDescription());
            estateFormBinding.etRooms.setText(Objects.requireNonNull(estate.getRooms()).toString().replace("5 et +", "5"), false);
            estateFormBinding.etBathrooms.setText(Objects.requireNonNull(estate.getBathrooms()).toString().replace("4 et +", "4"), false);
            estateFormBinding.etBedrooms.setText(Objects.requireNonNull(estate.getBedrooms()).toString().replace("5 et +", "5"), false);
            estateFormBinding.etGround.setText(Objects.requireNonNull(estate.getGround()).toString());
            estateFormBinding.etPrice.setText(Objects.requireNonNull(estate.getPrice()).toString());
            estateFormBinding.etAddress.setText(estate.getAddress());
            estateFormBinding.etPostalCode.setText(Objects.requireNonNull(estate.getPostalCode()).toString());
            estateFormBinding.etCity.setText(estate.getCity());
            estateFormBinding.boxSchools.setChecked(estate.getSchools());
            estateFormBinding.boxPark.setChecked(estate.getPark());
            estateFormBinding.boxRestaurants.setChecked(estate.getRestaurants());
            estateFormBinding.boxStores.setChecked(estate.getStores());
            estateFormBinding.availableRadiobtn.setChecked(estate.getSold());
            estateFormBinding.upOfSaleDate.setText(Utils.longDateToString(Objects.requireNonNull(estate.getUpOfSaleDate())));
            estateFormBinding.soldDate.setText(estate.getSoldDate());
            estateFormBinding.etAgent.setText(estate.getAgentName(), false);

            if (!estate.getPhotoList().getPhotoList().isEmpty()) {
                listPhoto.clear();
                photo.getPhotoList().clear();
                photoText.getPhotoDescription().clear();
                for (String photoStr : estate.getPhotoList().getPhotoList()) {
                    listPhoto.add(Uri.parse(photoStr));
                }
                adapter.setPhotoList(listPhoto);
                adapter.setPhotoDescription(estate.getPhotoDescription().getPhotoDescription());
                photo.getPhotoList().addAll(estate.getPhotoList().getPhotoList());
            }
            if (!estate.getVideo().getPhotoList().isEmpty() && estate.getVideo().getPhotoList().size() > 0) {
                for (String videoStr : estate.getVideo().getPhotoList()) {

                    estateFormBinding.deleteVideo.setVisibility(View.VISIBLE);
                    estateFormBinding.videoView.setVisibility(View.VISIBLE);
                    estateFormBinding.videoView.setVideoURI(Uri.parse(videoStr));
                }
            }
        }
    }

    /**
     * For click on photo btn
     */
    public void onClickPhotoBtn() {
        estateFormBinding.photoBtn.setOnClickListener(v -> {
            selectImage();
            saveImageInInternalStorage();
        });
    }

    /**
     * For click on video btn
     */
    public void onClickVideoBtn() {
        estateFormBinding.cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectVideo();
            }
        });
    }

    /**
     * For alert dialog for choose take photo or choose in gallery
     */
    protected void selectImage() {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};

        builder = new MaterialAlertDialogBuilder(this);
        builder.setTitle("Add pictures");
        builder.setItems(options, (dialog, item) -> {

            if (options[item].equals("Take Photo")) {

                Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                if (takePicture.resolveActivity(getPackageManager()) != null) {
                    //Create the File where the photo should go
                    File photoFile = null;
                    try {
                        photoFile = createImageFile();
                    } catch (IOException ex) {
                        Log.e("PhotoFileException", Objects.requireNonNull(ex.getMessage()));
                    }
                    //Continue only if the file was successfully created
                    if (photoFile != null) {

                        photoUri = FileProvider.getUriForFile(getApplicationContext(), "com.openclassrooms.realestatemanager.fileprovider", photoFile);

                        takePicture.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                        Log.d("PhotoUri", "photoUri =" + photoUri);

                        startActivityForResult(takePicture, 1);
                    }
                }
            } else if (options[item].equals("Choose from Gallery")) {

                Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto, 2);

            } else if (options[item].equals("Cancel")) {
                dialog.dismiss();
            }
        });

        builder.show();
    }

    /**
     * Create photo file
     *
     * @return
     * @throws IOException
     */
    protected File createImageFile() throws IOException {
        //Create an image file name
        String timeStamp = new SimpleDateFormat("ddMMyyyy", Locale.FRANCE).format(new Date());
        String imageFileName = "JPEG" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName, /*prefix*/
                ".jpg", /*suffix*/
                storageDir /*directory*/
        );
//        Save file : path for use with ACTION_VIEW intent
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    /**
     * For photos
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_CANCELED) {
            if (requestCode == PICK_IMAGE_CAMERA) {
                if (resultCode == Activity.RESULT_OK) {

                    listPhoto.add(photoUri);
                    photo.getPhotoList().add(String.valueOf(photoUri));
                    photoText.getPhotoDescription().add("");
                    adapter.setPhotoList(listPhoto);
                }
            }
            if (requestCode == PICK_IMAGE_GALLERY && data != null && data.getData() != null) {
                if (resultCode == Activity.RESULT_OK) {

                    Uri contentUri = Objects.requireNonNull(data).getData();
                    String timeStamp = new SimpleDateFormat("ddMMyyyy", Locale.FRANCE).format(new Date());
                    String imageFileName = "JPEG" + timeStamp + "." + getFileExt(contentUri);
                    Log.d("Test uri gallery", "onActivityResult : Gallery Image Uri:" + imageFileName);

                    //For save image in internal storage
                    FileOutputStream fOut = null;
                    try {
                        fOut = openFileOutput("imageGallery", MODE_PRIVATE);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    OutputStreamWriter osw = new OutputStreamWriter(Objects.requireNonNull(fOut));
                    try {
                        osw.write(imageFileName);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    int len = imageFileName.length();
                    try {
                        osw.flush();
                        osw.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    listPhoto.add(contentUri);
                    photo.getPhotoList().add(String.valueOf(contentUri));
                    photoText.getPhotoDescription().add("");
                    adapter.setPhotoList(listPhoto);
                }
            }
            if (requestCode == PICK_VIDEO_CAMERA && data != null && data.getData() != null) {
                if (resultCode == Activity.RESULT_OK) {
                    Uri contentURI = data.getData();
                    String recordedVideoPath = getPath(contentURI);
                    Log.d("recordedVideoPaht", recordedVideoPath);
                    saveVideoToInternalStorage(recordedVideoPath);
                    Objects.requireNonNull(estateFormBinding.videoView).setVideoURI(contentURI);
                    estateFormBinding.videoView.setVisibility(View.VISIBLE);
                    estateFormBinding.videoView.requestFocus();
                    MediaController mediaController = new MediaController(this);
                    estateFormBinding.videoView.setMediaController(mediaController);
                    mediaController.setAnchorView(estateFormBinding.videoView);
                    estateFormBinding.videoView.start();
                    video.getPhotoList().add(String.valueOf(contentURI));
                }
            }
            if (requestCode == PICK_VIDEO_GALLERY && data != null && data.getData() != null) {
                if (resultCode == Activity.RESULT_OK) {

                    Uri contentURI = data.getData();

                    String selectedVideoPath = getPath(contentURI);
                    Log.d("path", selectedVideoPath);
                    saveVideoToInternalStorage(selectedVideoPath);
                    Objects.requireNonNull(estateFormBinding.videoView).setVideoURI(contentURI);
                    estateFormBinding.videoView.setVisibility(View.VISIBLE);
                    estateFormBinding.videoView.requestFocus();
                    MediaController mediaController = new MediaController(this);
                    estateFormBinding.videoView.setMediaController(mediaController);
                    mediaController.setAnchorView(estateFormBinding.videoView);
                    estateFormBinding.videoView.start();
                    estateFormBinding.videoView.setVisibility(View.VISIBLE);
                    video.getPhotoList().add(selectedVideoPath);
                }
            }
        }
    }

    private String getFileExt(Uri contentUri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(contentUri));
    }

    /**
     * For save photo in internal storage
     */
    public void saveImageInInternalStorage() {
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        File file = new File(directory, "UniqueFileName" + ".jpg");
        if (!file.exists()) {
            Log.d("path", file.toString());
            FileOutputStream fos = null;
            try {
                if (selectedImage != null) {
                    fos = new FileOutputStream(file);
                    selectedImage.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                    fos.flush();
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * For alert dialog for choose take video or choose video
     */
    protected void selectVideo() {
        final CharSequence[] options = {"Take Video", "Choose Video", "Cancel"};

        builderVideo = new MaterialAlertDialogBuilder(this);
        builderVideo.setTitle("Add video");
        builderVideo.setItems(options, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Take Video")) {
                    Intent takeVideo = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                    startActivityForResult(takeVideo, PICK_VIDEO_CAMERA);

                } else if (options[item].equals("Choose Video")) {
                    Intent pickVideo = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickVideo, PICK_VIDEO_GALLERY);

                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builderVideo.show();
    }

    /**
     * For save video in internal storage
     * @param filePath
     */
    private void saveVideoToInternalStorage (String filePath) {

        try {

            File currentFile = new File(filePath);
            File wallpaperDirectory = new File(Environment.getExternalStorageDirectory() + VIDEO_DIRECTORY);
            newfile = new File(wallpaperDirectory, Calendar.getInstance().getTimeInMillis() + ".mp4");

            if (!wallpaperDirectory.exists()) {
                wallpaperDirectory.mkdirs();
            }

            if(currentFile.exists()){

                InputStream in = new FileInputStream(currentFile);
                OutputStream out = new FileOutputStream(newfile);

                // Copy the bits from instream to outstream
                byte[] buf = new byte[1024];
                int len;

                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                in.close();
                out.close();
                Log.d("vii", "Video file saved successfully.");
            }else{
                Log.e("vii", "Video saving failed. Source file missing.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * For video
     *
     * @param uri
     * @return
     */
    public String getPath(Uri uri) {

        String[] projection = {MediaStore.Video.Media.DATA};
        cursor = getContentResolver().query(uri, projection, null, null, null);

        if (cursor != null) {
            // HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
            // THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
            int column_index = ((Cursor) cursor)
                    .getColumnIndexOrThrow(MediaStore.Video.Media.DATA);

            cursor.moveToFirst();
            return cursor.getString(column_index);
        } else {
            return null;
        }
    }

    /**
     * For delete photos and descriptions
     */
    private void configureOnClickRecyclerView() {
        ItemClickSupport.addTo(estateFormBinding.rvPhoto, R.layout.activity_add_photo_item)
                .setOnItemClickListener((recyclerView, position, v) -> {

                    if (estateEdit != 0) {

                        String estatePhoto = estate.getPhotoList().getPhotoList().get(position);
                        Log.d("estatePhoto", "estatePhoto" + estatePhoto);
                        String estateDescription = estate.getPhotoDescription().getPhotoDescription().get(position);
                        listPhoto.remove(Uri.parse(estatePhoto));
                        photo.getPhotoList().remove(estatePhoto);
                        photoText.getPhotoDescription().remove(estateDescription);
                        adapter.setPhotoList(listPhoto);
                        adapter.notifyItemRemoved(position);
                        adapter.notifyDataSetChanged();
                    }
                });
    }

    /**
     * For delete video
     */
    public void onClickDeleteVideo() {

        estateFormBinding.deleteVideo.setOnClickListener(v -> {

            String estateVideo = estate.getVideo().getPhotoList().get(0);
            video.getPhotoList().remove(estateVideo);
            estate.setVideo(video);
            estateFormBinding.videoView.setVisibility(View.INVISIBLE);
            estateFormBinding.deleteVideo.setVisibility(View.INVISIBLE);

        });
    }
}