package com.example.motorizadoyego.View.ProcesUI;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.motorizadoyego.MainActivity;
import com.example.motorizadoyego.R;
import com.example.motorizadoyego.Repository.Modelo.Delivery_Pedido;
import com.example.motorizadoyego.Repository.Modelo.Gson.GsonDelivery_Pedido;
import com.example.motorizadoyego.Repository.Modelo.Orden_estado_delivery;
import com.example.motorizadoyego.Repository.Modelo.Orden_estado_deliveryPK;
import com.example.motorizadoyego.Util.ProgressButon;
import com.example.motorizadoyego.Util.RxBus;
import com.example.motorizadoyego.ViewModel.Orden_estado_deliveryViewModel;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import android.content.ContentResolver;

import static android.app.Activity.RESULT_OK;

public class StepPictureFragment extends Fragment {

    private static final String ARG_HOME ="argumentos";

    private GsonDelivery_Pedido mGsonDelivery_pedido;

    private Delivery_Pedido mDelivery_pedido;

    private ImageView fragment_step_picture_IMAGEN;

    private CardView fragment_step_picture_CHANGE_IMAGE;

    private boolean takePicture;

    public static final int RequestPermissionCode = 1;

    private LinearLayout fragment_step_picture_RELOAD;

    private Uri filePath;

    private StorageReference storageReference;

    private String currentPhotoPath;

    ContentResolver result;

    private File fileImagen;

    private Orden_estado_deliveryViewModel viewModelEstado;

    private View mView;

    private ProgressButon progressButon;

    private TextView text_numero_orden;

    public StepPictureFragment() {
        // Required empty public constructor
    }

    public static StepPictureFragment newInstance(GsonDelivery_Pedido gsonDelivery_pedido) {
        StepPictureFragment fragment = new StepPictureFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_HOME, gsonDelivery_pedido);
        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mGsonDelivery_pedido=(GsonDelivery_Pedido) getArguments().getSerializable(ARG_HOME);
            mDelivery_pedido=mGsonDelivery_pedido.getDelivery_information();
        }

        takePicture=true;
        FirebaseApp.initializeApp(getContext());
        FirebaseStorage storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        viewModelEstado= ViewModelProviders.of(this).get(Orden_estado_deliveryViewModel.class);
        viewModelEstado.init();
        result=(ContentResolver)getContext().getContentResolver();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_step_picture, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        EnableRuntimePermission();

        NavController navController= Navigation.findNavController(view);


        declararWidget(view);


        clickUpLoadPhoto();

        sendDataEstado(navController);
    }


    private void sendDataEstado(NavController nav){
        viewModelEstado.getOrden_estado_deliveryLiveData().observe(getViewLifecycleOwner(), new Observer<Orden_estado_delivery>() {
            @Override
            public void onChanged(Orden_estado_delivery orden_estado_delivery) {

                progressButon.buttoFinished();
                mView.setEnabled(true);

                if(orden_estado_delivery!=null){

                    Toast.makeText(getContext(),"LA FOTO FUE ENVIADA",Toast.LENGTH_SHORT).show();

                    RxBus.getInstance().publishStepPictureFragment(mDelivery_pedido);

                }else{
                    Toast.makeText(getContext(),"SUCEDIO UN PROBLEMAS VOLVER A ENVIAR FOTO O VERIFICAR INTERNET",Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    private void declararWidget(View view){
        fragment_step_picture_IMAGEN=view.findViewById(R.id.fragment_step_picture_IMAGEN);
        fragment_step_picture_RELOAD=view.findViewById(R.id.fragment_step_picture_RELOAD);
        fragment_step_picture_CHANGE_IMAGE=view.findViewById(R.id.fragment_step_picture_CHANGE_IMAGE);

        mView=view.findViewById(R.id.layout_button_progress);

        progressButon= new ProgressButon(getContext(),view,"Foto lista",0);
        progressButon.initName();

        text_numero_orden=view.findViewById(R.id.text_numero_orden);

        String numero_orden="Orden #"+mGsonDelivery_pedido.getDelivery_information().getIdventa();

        text_numero_orden.setText(numero_orden);
    }

    private void setDataWidget(){

    }


    private void clickUpLoadPhoto(){

        mView.setOnClickListener( v->{
            System.out.println("CLICK UPLOAD PHOTO");

            if(takePicture){

                mView.setEnabled(false);

                progressButon.buttonActivated();

                //COMENTAMOS LA LINEA TEMPORALMENTE

               //uploadImage();

                sendDataState("");

            }else{
                Toast.makeText(getContext(),"Tomar la foto correspondiente",Toast.LENGTH_SHORT).show();
            }



        });


        fragment_step_picture_RELOAD.setOnClickListener( v->{

            dispatchTakePictureIntent();
        });


        fragment_step_picture_CHANGE_IMAGE.setOnClickListener( v->{

            if(fileImagen!=null){
                fileImagen.delete();
            }
            dispatchTakePictureIntent();

        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 7 && resultCode == RESULT_OK) {


            Toast.makeText(getContext(),"LA FOTO FUE TOMADA",     Toast.LENGTH_LONG).show();
            fragment_step_picture_RELOAD.setVisibility(View.GONE);

             fileImagen=new File(currentPhotoPath);

             filePath=Uri.fromFile(fileImagen);

            Bitmap d = new BitmapDrawable(getResources() ,currentPhotoPath).getBitmap();
            int nh = (int) ( d.getHeight() * (512.0 / d.getWidth()) );
            Bitmap scaled = Bitmap.createScaledBitmap(d, 512, nh, true);
            fragment_step_picture_IMAGEN.setImageBitmap(scaled);

            takePicture=true;

        }
    }


    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getContext().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(getContext(),
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent,7);
            }
        }
    }



    private void EnableRuntimePermission(){
        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                Manifest.permission.CAMERA)) {
            Toast.makeText(getContext(),"CAMERA permission allows us to Access CAMERA app",     Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(getActivity(),new String[]{
                    Manifest.permission.CAMERA}, RequestPermissionCode);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == RequestPermissionCode) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getContext(), "Permission Granted, Now your application can access CAMERA.", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getContext(), "Permission Canceled, Now your application cannot access CAMERA.", Toast.LENGTH_LONG).show();
            }
        }
    }


    private void uploadImage()
    {
        if (filePath != null) {

            // Code for showing progressDialog while uploading
            ProgressDialog progressDialog
                    = new ProgressDialog(getContext());
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            // Defining the child of storageReference
            StorageReference ref
                    = storageReference
                    .child(
                            "images/"
                                    + UUID.randomUUID().toString());

            // adding listeners on upload
            // or failure of image
            // Progress Listener for loading
// percentage on the dialog box


            Bitmap bitmap = ((BitmapDrawable) fragment_step_picture_IMAGEN.getDrawable()).getBitmap();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 25, baos);
            byte[] data = baos.toByteArray();

            UploadTask uploadTask =ref.putBytes(data);


                   uploadTask.addOnSuccessListener(
                            new OnSuccessListener<UploadTask.TaskSnapshot>() {

                                @Override
                                public void onSuccess(
                                        UploadTask.TaskSnapshot taskSnapshot)
                                {

                                    // Image uploaded successfully
                                    // Dismiss dialog
                                    progressDialog.dismiss();
                                    Toast
                                            .makeText(getContext(),
                                                    "Image Uploaded!!",
                                                    Toast.LENGTH_SHORT)
                                            .show();


                                   //StepPictureFragmentDirections.ActionStepPictureFragmentToStep4Fragment action= StepPictureFragmentDirections.actionStepPictureFragmentToStep4Fragment(mGsonDelivery_pedido,mGsonDelivery_pedido);
                                  //  navController.navigate(action);

                                    System.out.println("TENEMOS LA URL "+ref.getDownloadUrl());


                                    uploadTask.continueWithTask(task -> {
                                        if (!task.isSuccessful()) {
                                            throw task.getException();
                                        }

                                        // Continue with the task to get the download URL
                                        return ref.getDownloadUrl();
                                    }).addOnCompleteListener(task -> {
                                        if (task.isSuccessful()) {
                                            Uri downloadUri = task.getResult();
                                            System.out.println(downloadUri.toString());

                                            sendDataState(downloadUri.toString());

                                        }
                                    });


                                }
                            })

                    .addOnFailureListener(e -> {

                        // Error, Image not uploaded
                        progressDialog.dismiss();
                        Toast
                                .makeText(getContext(),
                                        "Failed " + e.getMessage(),
                                        Toast.LENGTH_SHORT)
                                .show();
                    })
                    .addOnProgressListener(
                            taskSnapshot -> {
                                double progress
                                        = (100.0
                                        * taskSnapshot.getBytesTransferred()
                                        / taskSnapshot.getTotalByteCount());
                                progressDialog.setMessage(
                                        "Uploaded "
                                                + (int)progress + "%");
                            });





        }



    }



    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );



        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        System.out.println(currentPhotoPath+" RUTAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
        return image;
    }


    private void sendDataState(String path){
        Orden_estado_deliveryPK pk = new Orden_estado_deliveryPK();
        pk.setIdventa(mDelivery_pedido.getIdventa());
        pk.setIdestado_delivery(6);

        Orden_estado_delivery orden_estado_delivery= new Orden_estado_delivery();
        orden_estado_delivery.setId(pk);
        orden_estado_delivery.setIdrepartidor(mDelivery_pedido.getIdrepartidor());
        orden_estado_delivery.setFecha(null);
        orden_estado_delivery.setDetalle(path);

        viewModelEstado.updateEstado(orden_estado_delivery,mDelivery_pedido.getIdusuario());
    }
}
