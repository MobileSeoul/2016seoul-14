package com.seoulapp.ssg.ui.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Toast;

import com.seoulapp.ssg.R;
import com.seoulapp.ssg.api.SsgApiService;
import com.seoulapp.ssg.managers.PropertyManager;
import com.seoulapp.ssg.model.Model;
import com.seoulapp.ssg.model.Ssg;
import com.seoulapp.ssg.network.ServiceGenerator;
import com.seoulapp.ssg.ui.dialog.UploadPictureDialog;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapReverseGeoCoder;
import net.daum.mf.map.api.MapView;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReportActivity extends BaseActivity implements UploadPictureDialog.OnChoiceClickListener,
        View.OnClickListener, MapView.MapViewEventListener, MapReverseGeoCoder.ReverseGeoCodingResultListener {
    private static final String TAG = ReportActivity.class.getSimpleName();
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 100;
    private UploadPictureDialog mDialog;
    private static final int REQUEST_CODE_GALLERY = 0;
    private static final int REQUEST_CODE_CAMERA = 1;
    private static final int REQUEST_CODE_CROP = 2;

    private LocationManager lm;

    private Uri mImageCaptureUri;
    private String absoultePath;
    private ImageView ivUploadPicture;
    private MapView mMapView;
    private MapPOIItem mMapPOIItem;
    private ScrollView mRootScrollView;
    private EditText editLocationDetail, editComment;
    private MapReverseGeoCoder mReverseGeoCoder = null;

    private String locationDetail, comment;
    private double lat, lng;

    private Ssg ssg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            setTitle("");
        }

        ivUploadPicture = (ImageView) findViewById(R.id.iv_upload_picture);
        ivUploadPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);

                startActivityForResult(intent, REQUEST_CODE_GALLERY);
            }
        });

        mMapView = new MapView(this);
        mMapView.setDaumMapApiKey(getResources().getString(R.string.daum_map_api_key));
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mRootScrollView = (ScrollView) findViewById(R.id.scroll_view);
        ViewGroup mapViewContainer = (ViewGroup) findViewById(R.id.map_view);
        mapViewContainer.addView(mMapView);
        mMapView.setMapViewEventListener(this);
        mMapView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                mRootScrollView.requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        editComment = (EditText) findViewById(R.id.edit_comment);
        editLocationDetail = (EditText) findViewById(R.id.edit_location);

        editComment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    comment = charSequence.toString();
                } else {
                    comment = "";
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        editLocationDetail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    locationDetail = charSequence.toString();
                } else {
                    locationDetail = "";
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        Button btnSubmit = (Button) findViewById(R.id.btn_submit);
        Button btnCancel = (Button) findViewById(R.id.btn_cancel);

        btnSubmit.setOnClickListener(this);
        btnCancel.setOnClickListener(this);

    }

    @Override
    public void onCameraClick() {

    }

    @Override
    public void onGalleryClick() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);

        startActivityForResult(intent, REQUEST_CODE_GALLERY);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK)
            return;

        switch (requestCode) {
            case REQUEST_CODE_GALLERY: {

                final Bundle extras = data.getExtras();

                if (extras != null) {
//                    imagePath = mImageCaptureUri.getPath();

                    mImageCaptureUri = data.getData();

                    Log.d(TAG, mImageCaptureUri.getPath().toString());

                    Intent i = new Intent("com.android.camera.action.CROP");
                    i.setDataAndType(mImageCaptureUri, "image/*");

                    i.putExtra("outputX", 300);
                    i.putExtra("outputY", 300);
                    i.putExtra("aspectX", 1);
                    i.putExtra("aspectY", 1);
                    i.putExtra("scale", true);
                    i.putExtra("return-data", true);
                    startActivityForResult(i, REQUEST_CODE_CROP);
                    Log.d(TAG, "onActivityResult: 1");
                    break;

//                    Glide.with(this)
//                            .load(mImageCaptureUri.getPath())
//                            .into(ivUploadPicture);


                }
            }

            case REQUEST_CODE_CROP: {

                final Bundle extras = data.getExtras();
                String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/ssg/" + System.currentTimeMillis() + "jpeg";

                if (extras != null) {
                    Bitmap photo = extras.getParcelable("data");

                    ivUploadPicture.setImageBitmap(photo);
                    storeCropImage(photo, filePath);
                    absoultePath = filePath;
                    break;
                }

                File f = new File(mImageCaptureUri.getPath());
                if (f.exists()) {
                    f.delete();
                }

            }

        }

    }

    private void storeCropImage(Bitmap bitmap, String filePath) {
        String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/ssg";
        File directory_ssg = new File(dirPath);

        if (!directory_ssg.exists()) { // ssg폴더가 없다면
            directory_ssg.mkdir();
        }

        File copyFile = new File(filePath);
        BufferedOutputStream output;

        try {
            copyFile.createNewFile();
            output = new BufferedOutputStream(new FileOutputStream(copyFile));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, output);
            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                    Uri.fromFile(copyFile)));

            output.flush();
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_submit:
                if (mImageCaptureUri == null) {
                    Toast.makeText(ReportActivity.this, "이미지를 추가해주세요", Toast.LENGTH_SHORT).show();
                } else if (locationDetail == null) {
                    Toast.makeText(ReportActivity.this, "장소정보를 추가해주세요", Toast.LENGTH_SHORT).show();
                } else if (comment == null) {
                    Toast.makeText(ReportActivity.this, "코멘트를 추가해주세요", Toast.LENGTH_SHORT).show();
                } else {
                    mReverseGeoCoder = new MapReverseGeoCoder(getString(R.string.daum_map_api_key), mMapView.getMapCenterPoint(), ReportActivity.this, ReportActivity.this);
                    mReverseGeoCoder.startFindingAddress();
                }

                break;

            case R.id.btn_cancel:
                finish();
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            //여기서 위치값이 갱신되면 이벤트가 발생한다.
            //값은 Location 형태로 리턴되며 좌표 출력 방법은 다음과 같다.

            lat = location.getLatitude();
            lng = location.getLongitude();

            MapPoint mapPoint = MapPoint.mapPointWithGeoCoord(lat, lng);
            mMapView.setMapCenterPoint(mapPoint, true);
            mMapPOIItem = new MapPOIItem();
            mMapPOIItem.setItemName("낙서 요기");
            mMapPOIItem.setTag(0);
            mMapPOIItem.setMapPoint(mapPoint);
            mMapPOIItem.setMarkerType(MapPOIItem.MarkerType.BluePin); // 기본으로 제공하는 BluePin 마커 모양.
            mMapPOIItem.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin); // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.

            mMapView.addPOIItem(mMapPOIItem);

            if (ActivityCompat.checkSelfPermission(ReportActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                lm.removeUpdates(mLocationListener);
            }
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    public void onMapViewCenterPointMoved(MapView mapView, MapPoint mapPoint) {
        lat = mapPoint.getMapPointGeoCoord().latitude;
        lng = mapPoint.getMapPointGeoCoord().longitude;

        if (mMapPOIItem != null)
            mMapPOIItem.setMapPoint(mapPoint);
    }

    @Override
    public void onMapViewInitialized(MapView mapView) { // map을 사용할 준비가 되었다.

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
        } else {
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, // 등록할 위치제공자
                    100, // 통지사이의 최소 시간간격 (miliSecond)
                    1, // 통지사이의 최소 변경거리 (m)
                    mLocationListener);
            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, // 등록할 위치제공자
                    100, // 통지사이의 최소 시간간격 (miliSecond)
                    1, // 통지사이의 최소 변경거리 (m)
                    mLocationListener);
        }
    }

    @Override
    public void onMapViewZoomLevelChanged(MapView mapView, int i) {

    }

    @Override
    public void onMapViewSingleTapped(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDoubleTapped(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewLongPressed(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDragStarted(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDragEnded(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewMoveFinished(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onReverseGeoCoderFailedToFindAddress(MapReverseGeoCoder mapReverseGeoCoder) {

    }

    @Override
    public void onReverseGeoCoderFoundAddress(MapReverseGeoCoder mapReverseGeoCoder, String s) {
        onFinishReverseGeoCoding(s);
    }

    private void onFinishReverseGeoCoding(String result) {

        postImageAndData(absoultePath, PropertyManager.getInstance().getUserId(), comment, locationDetail, result, String.valueOf(lat), String.valueOf(lng));

    }

    private void postImageAndData(String filePath, String uid, String comment, String detailLocation, String pname, String lat, String lng) {
        final File file = new File(filePath);
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("picture", file.getName(), requestFile);

        RequestBody requestUserId =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), uid);
        // add another part within the multipart request
        RequestBody requestComment =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), comment);
        // add another part within the multipart request
        RequestBody requestDetailLoca =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), detailLocation);
        RequestBody requestPname =
                RequestBody.create(MediaType.parse("multipart/form-data"), pname);
        RequestBody requestLat = RequestBody.create(MediaType.parse("multipart/form-data"), lat);
        RequestBody requestLng = RequestBody.create(MediaType.parse("multipart/form-data"), lng);


        SsgApiService service = ServiceGenerator.getInstance().createService(SsgApiService.class);
        service.upload_ssg(body, requestUserId, requestComment, requestDetailLoca, requestPname, requestLat, requestLng).enqueue(new Callback<Model>() {
            @Override
            public void onResponse(Call<Model> call, Response<Model> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ReportActivity.this, "쓱이 등록 되었습니다", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Log.d(TAG, "onResponse: " + response.message());
                }

            }

            @Override
            public void onFailure(Call<Model> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }
}
