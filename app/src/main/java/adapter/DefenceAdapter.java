package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.myproject1.R;

import java.util.ArrayList;

import model.StudentInfo;

public class DefenceAdapter extends BaseAdapter {
    private ArrayList<StudentInfo> arrayList = new ArrayList<>();
    private Context context;
    private LayoutInflater inflater;

    public DefenceAdapter(ArrayList<StudentInfo> list, Context context){
        this.arrayList = list;
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return arrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View studentView = null;
        if (view ==null){
            studentView = inflater.inflate(R.layout.list_item_student_defence,null);
        }else {
            studentView = view;
        }

        StudentInfo studentInfo = arrayList.get(i);

        TextView tv_studentName = studentView.findViewById(R.id.tv_item_defence_studentName);
        TextView tv_studentId = studentView.findViewById(R.id.tv_item_defence_studentId);
        TextView tv_projectName = studentView.findViewById(R.id.tv_item_defence_projectName);
        TextView tv_defenceEdit = studentView.findViewById(R.id.tv_defence_edit);

        tv_studentName.setText(studentInfo.getStudengName());
        tv_studentId.setText("学号: "+ studentInfo.getStudentId());
        tv_projectName.setText("毕设题目: "+ studentInfo.getProjectName());


        if (studentInfo.getDenfeceType() == 1){
            tv_defenceEdit.setText("正常答辩");
        }else if(studentInfo.getDenfeceType() == 2){
            tv_defenceEdit.setText("延期答辩");
        }

        tv_defenceEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // students_listview.setTag(i);
                //showDefencePop();
            }
        });

        return studentView;
    }
}
