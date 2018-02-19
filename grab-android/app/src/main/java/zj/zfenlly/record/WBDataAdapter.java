package zj.zfenlly.record;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;
import android.widget.TextView;

import java.util.List;

import zj.test.scrapt.R;
import zj.test.scrapt.Tweet.DataBase.TweetNote;

/**
 * Created by Administrator on 2016/7/22.
 */
public class WBDataAdapter extends BaseAdapter {
    List<TweetNote> items;
    private LayoutInflater layoutInflater;
    private Context context;
    private int mResourceId;

    public WBDataAdapter(Context context, int textViewResourceId) {
        //super(context, textViewResourceId);
        this.layoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.mResourceId = textViewResourceId;
    }

    public void setAliases(List<TweetNote> items) {
        this.items = items;
        //addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        if (items != null)
            return items.size();
        else
            return 0;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Compan cp;
        if (convertView == null) {
            cp = new Compan();
            convertView = layoutInflater.inflate(mResourceId, null);
            cp.ts = convertView.findViewById(R.id.t_score);
            cp.ns = convertView.findViewById(R.id.n_score);
            cp.id = convertView.findViewById(R.id.wb_date);
            convertView.setTag(cp);
        } else {
            cp = (Compan) convertView.getTag();
        }
        cp.id.setText(items.get(position).getDate());
        cp.ts.setText(items.get(position).getFollow() + " : " + items.get(position).getFan() + " : " + items.get(position).getTweet() + " : " + items.get(position).getIntegral());
        cp.ns.setText(CaculateLevel.getNextLevelScore(items.get(position).getIntegral()));

//        Log.e("TAG", "--------------------------------------");
        return convertView;

    }

    class Compan {
        TextView ts;
        TextView ns;
        CheckedTextView id;
    }
}
