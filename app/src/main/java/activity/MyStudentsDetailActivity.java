package activity;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.myproject1.R;

import model.StudentInfo;

/**
 * 学生详情信息
 */
public class MyStudentsDetailActivity extends Activity {
    private final static String TAG = "StudentDetail";
    private StudentInfo student;
    private TextView tv_studentName, tv_studentId, tv_studentSex, tv_studentEmail, tv_studentTel,
            tv_studentClass, tv_studentMajor, tv_projectName, tv_projectStage, tv_projectProgress,
            tv_graduationDest, tv_creditOwed;

    private TextView button;
    private String[] array_dest;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_student);

        initView();
        initData();
    }

    private void initView() {
        tv_creditOwed = findViewById(R.id.tv_creditOwed);
        tv_graduationDest = findViewById(R.id.tv_graduationDest);
        tv_projectName = findViewById(R.id.tv_projectName);
        tv_projectStage = findViewById(R.id.tv_projectStage);
        tv_projectProgress = findViewById(R.id.tv_projectProgress);
        tv_studentClass = findViewById(R.id.tv_studentClass);
        tv_studentMajor = findViewById(R.id.tv_studentMajor);
        tv_studentTel = findViewById(R.id.tv_studentTel);
        tv_studentEmail = findViewById(R.id.tv_studentEmail);
        tv_studentSex = findViewById(R.id.tv_studentSex);
        tv_studentName = findViewById(R.id.tv_studentName);
        tv_studentId = findViewById(R.id.tv_studentId);

        button = findViewById(R.id.tv_determine);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        init();
    }

    private void init() {
        student = (StudentInfo) getIntent().getSerializableExtra("StudentInfo");
        System.out.println(student.getStudentId());
    }

    private void initData() {

        tv_studentId.setText(student.getStudentId() == null ? "" : student.getStudentId());
        tv_studentName.setText(student.getStudentName() == null ? "" : student.getStudentName());
        tv_studentSex.setText(student.getSex() == null ? "" : student.getSex());
        tv_studentEmail.setText(student.getE_mail() == null ? "" : student.getE_mail());
        tv_studentTel.setText(student.getTelephone() == null ? "" : student.getTelephone());
        tv_studentMajor.setText(student.getMajor());
        tv_studentClass.setText(student.getS_class());
        tv_projectProgress.setText(student.getProjectprogress() == 0 ? "0%" :
                student.getProjectprogress() + "%");
        tv_projectStage.setText(student.getProjectstage() == null ? "" : student.getProjectstage());
        tv_projectName.setText(student.getProjectName() == null ? "" : student.getProjectName());

        String dest = student.getGraduationdest();
        Resources res = getResources();
        array_dest = res.getStringArray(R.array.graduation_destination);

        if (dest.equals(array_dest[0])) {       //出国
            String destination = student.getDestination();
            tv_graduationDest.setText(dest + " : " + destination);
        } else if (dest.equals(array_dest[1])) {//就业
            tv_graduationDest.setText(dest);
        } else if (dest.equals(array_dest[2])) {//考研
            tv_graduationDest.setText(dest);
        } else if (dest.equals(array_dest[3])) {//保研
            tv_graduationDest.setText(dest);
        } else if (dest.equals(array_dest[4])) {//其他
            tv_graduationDest.setText(dest);
        } else {
            tv_graduationDest.setText("");
        }

        int creditOwed = student.getCreditowed();
        if (creditOwed > 0) {
            tv_creditOwed.setTextColor(Color.RED);
        }
        tv_creditOwed.setText("" + creditOwed);
    }


}
