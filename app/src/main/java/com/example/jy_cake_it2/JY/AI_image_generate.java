package com.example.jy_cake_it2.JY;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.jy_cake_it2.R;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AI_image_generate extends AppCompatActivity {
    Button btn_save_image, btn_generate, btn_back;
    EditText image_text;
    ImageView rectangle, circle;
    ProgressBar progress_bar;



    //    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    private static final int WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 1;
    //    OkHttpClient client = new OkHttpClient();
    public static final MediaType JSON = MediaType.get("application/json");

    OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ai_image_generator);

        btn_save_image = findViewById(R.id.btn_save_image);
        btn_generate = findViewById(R.id.btn_generate_ai_image);
        image_text = findViewById(R.id.image_text);
        rectangle = findViewById(R.id.rectangle);
//        circle = findViewById(R.id.circle);
        progress_bar = findViewById(R.id.progress_bar);

        //이미지 저장 버튼 저장 권한 요청->saveImage메소드로 저장, 이전 액티비티로 돌아가게 할것
//        btn_save_image.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (ContextCompat.checkSelfPermission(AI_image_generate.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//                    ActivityCompat.requestPermissions(AI_image_generate.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_EXTERNAL_STORAGE_REQUEST_CODE);
//                } else {
//                    saveImage();
//                    Intent intent = new Intent(AI_image_generate.this, activity_draw_cake.class);
//                    startActivity(intent);
//                }
//            }
//        });
        btn_save_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    if (!Environment.isExternalStorageManager()) {
                        Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                        startActivity(intent);
                    } else {
                        saveImage();
//                        Intent intent = new Intent(AI_image_generate.this, activity_draw_cake.class);
//                        startActivity(intent);
                    }
                } else {
                    if (ContextCompat.checkSelfPermission(AI_image_generate.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        if (ActivityCompat.shouldShowRequestPermissionRationale(AI_image_generate.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                            Toast.makeText(AI_image_generate.this, "이미지를 저장하려면 저장 권한이 필요합니다.", Toast.LENGTH_LONG).show();
                        }
                        ActivityCompat.requestPermissions(AI_image_generate.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_EXTERNAL_STORAGE_REQUEST_CODE);
                    } else {
                        saveImage();
//                        Intent intent = new Intent(AI_image_generate.this, activity_draw_cake.class);
//                        startActivity(intent);
                    }
                }
            }
        });
        btn_generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = image_text.getText().toString().trim()+"only cake"+"on top view"+"cake putted on center";
                if(text.isEmpty()){
                    image_text.setError("이미지 특징을 입력해주세요!");
                    return;
                }
                callAPI(text);
            }
        });
        btn_back = findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AI_image_generate.this, activity_draw_cake.class);
                startActivity(intent);
            }
        });
    }

    void callAPI(String text) {
        setInProgress(true);
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("model", "dall-e-2");
            jsonBody.put("prompt", text);
            jsonBody.put("size", "512x512");
            jsonBody.put("quality", "standard");
            jsonBody.put("n", 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //openai API요청
        RequestBody requestBody = RequestBody.create(jsonBody.toString(), JSON);
        Request request = new Request.Builder()
                .url("https://api.openai.com/v1/images/generations")
                .header("Authorization", "write your token")
                .post(requestBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                runOnUiThread(() -> {
                    Toast.makeText(getApplicationContext(), "이미지 생성 실패", Toast.LENGTH_LONG).show();
                    setInProgress(false);
                });
            }
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
//                Log.i("Response: ", response.body().string());
                if (!response.isSuccessful()) {
                    runOnUiThread(() -> {
                        Toast.makeText(getApplicationContext(), "서버 오류: " + response.message(), Toast.LENGTH_LONG).show();
                        setInProgress(false);
                    });
                    return;
                }

                String responseBody = response.body().string();
                try {
                    JSONObject jsonObject = new JSONObject(responseBody);
                    String imageUrl = jsonObject.getJSONArray("data").getJSONObject(0).getString("url");
                    loadImage(imageUrl);
                } catch (Exception e) {
                    e.printStackTrace();
                    runOnUiThread(() -> {
                        Toast.makeText(getApplicationContext(), "응답 처리 오류", Toast.LENGTH_LONG).show();
                        setInProgress(false);
                    });
                }
            }
        });
    }

    void setInProgress(boolean inProgress) {
        runOnUiThread(() -> {
            if (inProgress) {
                progress_bar.setVisibility(View.VISIBLE);
                btn_generate.setVisibility(View.GONE);
            } else {
                progress_bar.setVisibility(View.GONE);
                btn_generate.setVisibility(View.VISIBLE);
            }
        });
    }
    void loadImage(String url) {
        runOnUiThread(() -> {
            Picasso.get()
                    .load(url)
                    .resize(256, 256)  // 이미지 크기 조정
                    .centerInside()
                    .into(rectangle);
        });
        setInProgress(false);
    }

    //    void saveImage() {
//        BitmapDrawable drawable = (BitmapDrawable) rectangle.getDrawable();
//        if (drawable == null) {
//            Toast.makeText(this, "이미지가 없습니다", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        Bitmap bitmap = drawable.getBitmap();
//
//        ContentValues values = new ContentValues();
//        values.put(MediaStore.Images.Media.TITLE, "Generated Image");
//        values.put(MediaStore.Images.Media.DESCRIPTION, "Image generated by AI");
//        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
//        Uri uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
//
//        try (OutputStream out = getContentResolver().openOutputStream(uri)) {
//            if (out != null) {
//                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
//                Toast.makeText(this, "이미지가 저장되었습니다", Toast.LENGTH_SHORT).show();
//            } else {
//                Toast.makeText(this, "이미지 저장 실패", Toast.LENGTH_SHORT).show();
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//            Toast.makeText(this, "이미지 저장 중 오류 발생", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == WRITE_EXTERNAL_STORAGE_REQUEST_CODE) {
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                saveImage();
//            } else {
//                Toast.makeText(this, "권한이 거부되었습니다", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }
    void saveImage() {
        BitmapDrawable drawable = (BitmapDrawable) rectangle.getDrawable();
        if (drawable == null) {
            Toast.makeText(this, "이미지가 없습니다", Toast.LENGTH_SHORT).show();
            return;
        }
        Bitmap bitmap = drawable.getBitmap();
        String fileName = "cake_" + System.currentTimeMillis() + ".jpg";
        File directory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "cake_image");

        if (!directory.exists()) {
            if (!directory.mkdirs()) {
                Toast.makeText(this, "폴더 생성 실패", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        File file = new File(directory, fileName);

        try (OutputStream out = new FileOutputStream(file)) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            Toast.makeText(this, "이미지가 저장되었습니다: " + file.getAbsolutePath(), Toast.LENGTH_LONG).show();
            // 갤러리에 이미지 추가
            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri contentUri = Uri.fromFile(file);
            mediaScanIntent.setData(contentUri);
            this.sendBroadcast(mediaScanIntent);

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "이미지 저장 중 오류 발생", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == WRITE_EXTERNAL_STORAGE_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                saveImage();
            } else {
                Toast.makeText(this, "권한이 거부되었습니다", Toast.LENGTH_SHORT).show();
            }
        }
    }
}