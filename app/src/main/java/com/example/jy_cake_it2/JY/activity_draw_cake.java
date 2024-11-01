package com.example.jy_cake_it2.JY;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import yuku.ambilwarna.AmbilWarnaDialog;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.jy_cake_it2.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


public class activity_draw_cake extends AppCompatActivity {

    String lettering_text;
    int selected_color;
    Typeface selected_font;
    TextView mainText1, mainText2, myText, btn1;
    Button btnOrder, btn_gallery, btn_ai, btnColorPicker, btn_refresh;;
    Spinner spnShape, spnLetterShape, spnTaste, spnSize;
    //spnColor 혹씨 쓸 수도 있음
    int default_color;
    EditText edit_letter;
    ImageView circle, square;
    GradientDrawable circle_drawble;
    GradientDrawable square_drawble;
    //매소드에서 사용할 이미지 선택 요청 코드
    private static final int REQUEST_IMAGE_PICK = 1;
    private Uri selectedImageUri;// 갤러리에서 가져온 이미지 URI 저장
    private BottomSheetBehavior bottomSheetBehavior;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw_cake);
        default_color = ContextCompat.getColor(activity_draw_cake.this, R.color.light_pink);
        //WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        //EdgeToEdge.enable(this);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_draw_cake);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.nav_draw_cake) {
//                switch (item.getItemId()) {
//                    case R.id.nav_draw_cake:
                    startActivity(new Intent(activity_draw_cake.this, activity_draw_cake.class));
                    finish();
                    return true;
                } else if (id == R.id.nav_browse) {
//                    case R.id.nav_browse:
                    startActivity(new Intent(activity_draw_cake.this, activity_browse.class));
                    finish();
                    return true;
                } else if (id == R.id.nav_order) {
//                    case R.id.nav_order:
                    startActivity(new Intent(activity_draw_cake.this, user_order.class));
                    finish();
                    return true;
                } else if (id == R.id.nav_mypage) {
//                    case R.id.nav_mypage:
                    startActivity(new Intent(activity_draw_cake.this, Activity_my_page.class));
                    finish();
                    return true;
                }
                return false;
            }
        });
        // TextView 초기화
        mainText1 = findViewById(R.id.main_text1);
        mainText2 = findViewById(R.id.main_text2);
        myText = findViewById(R.id.circle_text);

        // Button 초기화
        btnOrder = findViewById(R.id.btn_order);
        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 주문하기 버튼 클릭 이벤트 처리
                // 이미지, 각 스피너에 있는 데이터들 모아놨다 마지막에 전송 + 케이크 이미지 가져오기
                String cake_type = "1호";
                String cake_shape = spnShape.getSelectedItem().toString();
                String cake_color = "보이는 것과 맞는 색";
                String cake_flavor = spnTaste.getSelectedItem().toString();
                String lettering = " ";
                if (edit_letter.getText() != null) {
                    lettering = edit_letter.getText().toString();
                }
                Intent intent = new Intent(activity_draw_cake.this, activity_set_reservation.class);
                intent.putExtra("cake_type", cake_type);
                intent.putExtra("cake_shape", cake_shape);
                intent.putExtra("cake_color", cake_color);
                intent.putExtra("cake_flavor", cake_flavor);
                intent.putExtra("lettering", lettering);

                if (spnShape.getSelectedItem().toString().equals("원형")) {
                    //새 이미지 뷰를 저장할 때마다 cake_iamge[idx++] 이런 느낌으로 인덱스 추가되게
                    saveBitmapAndPassBitmap(circle, "사진 설명", lettering_text, selected_color, selected_font, intent);
                } else if (spnShape.getSelectedItem().toString().equals("사각형")) {
                    saveBitmapAndPassBitmap(square, "사진 설명", lettering_text, selected_color, selected_font, intent);
                } else {
                    System.out.print("이미지 전송 실패");
                }
                startActivity(intent);
            }
        });

        View bottomSheet = findViewById(R.id.bottom_sheet);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);

        // BottomSheet의 초기 상태 설정 (일부만 보이도록 collapsed 상태)
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        //bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

        // BottomSheet의 기본 peek 높이 설정 (예: 300dp)
        int peekHeight = (int) getResources().getDimension(R.dimen.bottom_sheet_peek_height);
        bottomSheetBehavior.setPeekHeight(peekHeight);

        // BottomSheet가 드래그될 때 상태 변화를 감지할 수 있는 콜백 설정
        bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                    // BottomSheet가 완전히 펼쳐졌을 때의 동작
                } else if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    // BottomSheet가 닫혔을 때의 동작
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                // 슬라이딩 시 상태를 처리할 수 있는 콜백
            }
        });
        btn_ai = findViewById(R.id.btn_ai);
        btn_ai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity_draw_cake.this, AI_image_generate.class);
                startActivity(intent);
            }
        });
        final ActivityResultLauncher<Intent> galleryLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        selectedImageUri = result.getData().getData();
                        if (selectedImageUri != null) {
                            updateShapeImageView();
                        }
                    }
                }
        );
//
//        btn1 = findViewById(R.id.back);
//        btn1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(activity_draw_cake.this, Login_main.class);
//                startActivity(intent);
//            }
//        });
        btn_gallery = findViewById(R.id.btn_gallery);
        btn_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 갤러리 열기
                Intent galleryIntent = new Intent(Intent.ACTION_PICK);
                galleryIntent.setType("image/*");
                galleryLauncher.launch(galleryIntent);
            }
        });
        circle = findViewById(R.id.circle);
        square = findViewById(R.id.rectangle);
        spnShape = findViewById(R.id.spn_shape);
        String[] cake_shapes = getResources().getStringArray(R.array.cake_shape);
        ArrayAdapter<String> cakeShapeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, cake_shapes);
        spnShape.setAdapter(cakeShapeAdapter);

        spnShape.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                switch (position) {
                    case 0:
                        findViewById(R.id.circle).setVisibility(View.VISIBLE);
                        findViewById(R.id.rectangle).setVisibility(View.GONE);

                        break;
                    case 1:
                        findViewById(R.id.circle).setVisibility(View.GONE);
                        findViewById(R.id.rectangle).setVisibility(View.VISIBLE);
                        break;

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                findViewById(R.id.circle).setVisibility(View.VISIBLE);
                findViewById(R.id.rectangle).setVisibility(View.GONE);
            }
        });
        btn_refresh = findViewById(R.id.btn_refresh);
        btn_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recreate();
            }
        });
        // Spinner 초기화
        btnColorPicker = findViewById(R.id.color_sel_btn);
        btnColorPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openColorPicker();
            }
        });
        edit_letter = findViewById(R.id.edit_lettering);
        spnLetterShape = findViewById(R.id.spn_letter_shape);
        String[] cake_letter_shapes = getResources().getStringArray(R.array.cake_letter_shape);
        ArrayAdapter<String> cakeLetterShapeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, cake_letter_shapes);
        spnLetterShape.setAdapter(cakeLetterShapeAdapter);
        spnLetterShape.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                // 글씨체 선택에 따른 처리
                Typeface typeface = null;
                switch (position) {
                    case 0:
                        typeface = ResourcesCompat.getFont(getApplicationContext(), R.font.english_korean_test_font1);
                        selected_font = ResourcesCompat.getFont(getApplicationContext(), R.font.english_korean_test_font1);
                        break;
                    case 1:
                        typeface = ResourcesCompat.getFont(getApplicationContext(), R.font.english_korean_test_font2);
                        selected_font = ResourcesCompat.getFont(getApplicationContext(), R.font.english_korean_test_font2);
                        break;
                    case 2:
                        typeface = ResourcesCompat.getFont(getApplicationContext(), R.font.english_test_font1);
                        selected_font = ResourcesCompat.getFont(getApplicationContext(), R.font.english_test_font1);
                        break;
                    case 3:
                        typeface = ResourcesCompat.getFont(getApplicationContext(), R.font.english_test_font2);
                        selected_font = ResourcesCompat.getFont(getApplicationContext(), R.font.english_test_font2);
                        break;
                }
                if (typeface != null) {
                    edit_letter.setTypeface(typeface);
                    myText.setTypeface(typeface);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // 아무것도 선택되지 않았을 때 처리할 내용
            }
        });
        edit_letter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                myText.setText(charSequence); // 추가된 TextView의 텍스트 업데이트
                lettering_text = charSequence.toString();//입력된 텍스트를 도형에 그려주기 위해 내용 변수에 저장
                myText.setVisibility(View.VISIBLE); // 텍스트가 입력되면 TextView 보이게 설정
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        spnTaste = findViewById(R.id.spn_taste);
        String[] cake_taste = getResources().getStringArray(R.array.cake_taste);
        ArrayAdapter<String> cakeTasteAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, cake_taste);
        spnTaste.setAdapter(cakeTasteAdapter);
        spnTaste.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                // 맛에 따른 데이터 처리
                switch (position) {
                    case 0:
                        break;
                    case 1:
                        break;
                    case 2:
                        break;
                    case 3:
                        break;
                    case 4:
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // 아무것도 선택되지 않았을 때 처리할 내용
            }
        });
        spnSize = findViewById(R.id.spn_size);
        String[] cake_size = getResources().getStringArray(R.array.cake_size);
        ArrayAdapter<String> cakeSizeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, cake_size);
        spnSize.setAdapter(cakeSizeAdapter);
        spnSize.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                // 맛에 따른 데이터 처리
                switch (position) {
                    case 0:
                        break;
                    case 1:
                        break;
                    case 2:
                        break;
                    case 3:
                        break;
                    case 4:
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // 아무것도 선택되지 않았을 때 처리할 내용
            }
        });
    }
    public void openColorPicker() {
        AmbilWarnaDialog dialog = new AmbilWarnaDialog(this, default_color, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onCancel(AmbilWarnaDialog dialog) {
                // 취소 시 특별한 처리를 하지 않음
            }

            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                default_color = color; // 선택된 색상으로 기본 색상 업데이트
                selected_color = color;
                circle_drawble.setColor(default_color);
                square_drawble.setColor(default_color);
                GradientDrawable circleDrawableNew = (GradientDrawable) ContextCompat.getDrawable(getApplicationContext(), R.drawable.circle_shape).mutate();
                GradientDrawable squareDrawableNew = (GradientDrawable) ContextCompat.getDrawable(getApplicationContext(), R.drawable.rectangle_shape).mutate();

                circleDrawableNew.setColor(color);
                squareDrawableNew.setColor(color);

                circle.setImageDrawable(circleDrawableNew);
                square.setImageDrawable(squareDrawableNew);

                // 강제로 UI를 다시 그립니다.
                circle.invalidate();
                square.invalidate();
            }
        });
        dialog.show();
    }
    public String imgViewToBase64(ImageView imageView) {
        //imageview 에서  drawable 객체를 가져와 BitmapDrawble  로 캐스팅
        BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();

        //Bitmapdrawble 에서 비트맵 추출
        Bitmap bitmap = drawable.getBitmap();

        //비트맵을 압축하여 ByteArrayOutputStream에 저장
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        byte[] byteArray = outputStream.toByteArray();

        //bytearray를 base64로 인코딩 하여 문자열로 변환후 반환
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }
    public Bitmap getBitmapWithShapeAndText(ImageView imageView, String text, int shapeColor, Typeface selected_font) {
        // circle, square을 포함하는 ImageView의 크기와 동일한 비트맵 생성
        Bitmap bitmap = Bitmap.createBitmap(imageView.getWidth(), imageView.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        // ImageView의 Drawable을 가져와서 그리기 (모양 포함)
        Drawable drawable = imageView.getDrawable();
        if (drawable != null) {
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
        }

        // 도형 색상 설정 (배경색을 그리는 부분)
        Paint shapePaint = new Paint();
        shapePaint.setColor(shapeColor);  // 사용자 선택한 색상
        shapePaint.setStyle(Paint.Style.FILL);

        // 도형이 원형(circle)인지 사각형(square)인지에 따라 도형을 그리기
        if (imageView.getId() == R.id.circle) {
            canvas.drawCircle(canvas.getWidth() / 2, canvas.getHeight() / 2, canvas.getWidth() / 2, shapePaint); // 원형 그리기
        } else if (imageView.getId() == R.id.rectangle) {
            canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), shapePaint);  // 사각형 그리기
        }

        if (text == null) {
            text = "";  // null이면 빈 문자열로 대체
        }
        // 텍스트 그리기
        Paint textPaint = new Paint();
        textPaint.setColor(Color.BLACK);  // 텍스트 색상
        textPaint.setTextSize(50);  // 텍스트 크기
        textPaint.setTypeface(selected_font);
        textPaint.setAntiAlias(true);
        textPaint.setTextAlign(Paint.Align.CENTER);

        // 텍스트를 ImageView의 중앙에 그리기
        int xPos = (canvas.getWidth() / 2);
        int yPos = (int) ((canvas.getHeight() / 2) - ((textPaint.descent() + textPaint.ascent()) / 2));
        canvas.drawText(text, xPos, yPos, textPaint);

        return bitmap;  // 최종 비트맵 반환
    }
    public void saveBitmapAndPassBitmap(ImageView imageView, String description, String text, int shapeColor, Typeface selectedFont, Intent intent) {
        // `getBitmapWithShapeAndText`로부터 Bitmap 생성
        Bitmap bitmap = getBitmapWithShapeAndText(imageView, text, shapeColor, selectedFont);

        // Bitmap을 파일로 저장
        String filePath = saveBitmapAsFile(bitmap);

        if (filePath != null) {
            // Intent 생성 및 파일 경로 추가
            intent.putExtra("imagePath", filePath);
            intent.putExtra("description", description);
            Log.d("Debug", "Intent created with image file path: " + filePath); // 경로 출력
        } else {
            Log.e("Error", "Failed to save bitmap as file.");
        }
    }
    public String saveBitmapAsFile(Bitmap bitmap) {
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        String fileName = "cake_img_" + System.currentTimeMillis() + ".jpg";
        File imageFile = new File(storageDir, fileName);

        try (FileOutputStream fos = new FileOutputStream(imageFile)) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos);
            fos.flush();
            Log.d("Debug", "Bitmap saved at: " + imageFile.getAbsolutePath());
            return imageFile.getAbsolutePath();
        } catch (IOException e) {
            Log.e("Error", "Failed to save bitmap as file: " + e.getMessage());
            return null;
        }
    }
    private void updateShapeImageView() {
        if (selectedImageUri != null) { // 이미지가 선택된 경우에만 업데이트
            if (spnShape.getSelectedItem().toString().equals("원형")) {
                circle.setVisibility(View.VISIBLE);
                square.setVisibility(View.GONE);
                circle.setImageURI(selectedImageUri);
                circle.setBackground(circle_drawble); // 원형 배경 설정
                circle.setClipToOutline(true); // 이미지가 원형에 맞춰 잘리도록 설정
            } else if (spnShape.getSelectedItem().toString().equals("사각형")) {
                circle.setVisibility(View.GONE);
                square.setVisibility(View.VISIBLE);
                square.setImageURI(selectedImageUri);
                square.setBackground(square_drawble); // 사각형 배경 설정
                square.setClipToOutline(true); // 이미지가 사각형에 맞춰 잘리도록 설정
            }
        } else {
            // 이미지가 선택되지 않은 경우 기본 모양 유지
            if (spnShape.getSelectedItem().toString().equals("원형")) {
                circle.setVisibility(View.VISIBLE);
                square.setVisibility(View.GONE);
                circle.setImageDrawable(null); // 이미지를 비우고
                circle.setBackground(circle_drawble); // 원형 배경 설정
            } else if (spnShape.getSelectedItem().toString().equals("사각형")) {
                circle.setVisibility(View.GONE);
                square.setVisibility(View.VISIBLE);
                square.setImageDrawable(null); // 이미지를 비우고
                square.setBackground(square_drawble); // 사각형 배경 설정
            }
        }
    }
}