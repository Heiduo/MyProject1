package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.myproject1.R;

import java.util.ArrayList;

import model.AnnounceInfo;

public class SignUpAnnounceAdapter extends BaseAdapter {
    private ArrayList<AnnounceInfo> list;
    private Context context;

    public SignUpAnnounceAdapter(ArrayList<AnnounceInfo> list, Context context ) {
        this.list = list;
        this.context = context;
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public AnnounceInfo getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewholder;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item_announce, null);

            viewholder = new ViewHolder();
            viewholder.tv_announce_name = convertView
                    .findViewById(R.id.tv_announce_name);
            viewholder.tv_announce_time = convertView
                    .findViewById(R.id.tv_announce_time);
            viewholder.tv_announce_introduce =convertView
                    .findViewById(R.id.tv_introduce);
            viewholder.tv_announce_admin =  convertView
                    .findViewById(R.id.tv_admin_name);
            viewholder.tv_announce_introduce2 = convertView
                    .findViewById(R.id.tv_introduce2);
            convertView.setTag(viewholder);
        } else {
            viewholder = (ViewHolder) convertView.getTag();
        }

        AnnounceInfo info = list.get(position);
        viewholder.tv_announce_name.setText(info.getannounceName());             //公告名
        viewholder.tv_announce_introduce.setText(info.getannounceIntroduce());   //详情
        viewholder.tv_announce_introduce2.setText(info.getannounceIntroduce2());       //详情
        viewholder.tv_announce_admin.setText(info.getannounceName());            //发布人员
        viewholder.tv_announce_time.setText(info.getannounceTime());                             //发布时间

        return convertView;
    }

    private static class ViewHolder {
        private TextView tv_announce_name;
        private TextView tv_announce_time;
        private TextView tv_announce_introduce;
        private TextView tv_announce_admin;
        private TextView tv_announce_introduce2;

    }


}
