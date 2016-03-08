package zemmahi.youssef.letsmeet;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by mahdi on 16-03-01.
 */
public class UserAdapter extends BaseAdapter {
    public List<Utilisateur> fData;
    private ArrayList<Utilisateur> fArrayList;
    Context fC;
    ViewHolder fV;
    RoundImage fRoundImage;

    public UserAdapter(List<Utilisateur> utilisateurs, Context context) {
        fData = utilisateurs;
        fC = context;
        this.fArrayList = new ArrayList<Utilisateur>();
        this.fArrayList.addAll(fData);
    }
    @Override
    public int getCount() {
        return fData.size();
    }

    @Override
    public Object getItem(int position) {
        return fData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if(view == null){
            LayoutInflater layoutInflater = (LayoutInflater) fC.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.activity_each_contact_in_user_list, null);
            Log.e("Inside", "here--------------------------- In view1");
        } else {
            view = convertView;
            Log.e("Inside", "here--------------------------- In view2");
        }
        fV = new ViewHolder();
        fV.title = (TextView)view.findViewById(R.id.txtVwContactName);
        fV.check = (CheckBox) view.findViewById(R.id.chkBoxContact);
        fV.email = (TextView) view.findViewById(R.id.txtVwContactEmail);
        fV.imageView = (ImageView) view.findViewById(R.id.imgVwContactPic);

        final Utilisateur data = (Utilisateur) fData.get(position);
        fV.title.setText(data.getName());
        fV.check.setChecked(data.getCheckedBox());
        fV.email.setText(data.getCourriel());

        // Set image if exists
        try {

            if (data.getThumb() != null) {
                fV.imageView.setImageBitmap(data.getThumb());
            } else {
                fV.imageView.setImageResource(R.drawable.ic_photos);
            }
            // Seting round image
            Bitmap bm = BitmapFactory.decodeResource(view.getResources(), R.drawable.ic_photos); // Load default image
            fRoundImage = new RoundImage(bm);
            fV.imageView.setImageDrawable(fRoundImage);
        } catch (OutOfMemoryError e) {
            // Add default picture
            fV.imageView.setImageDrawable(this.fC.getDrawable(R.drawable.ic_photos));
            e.printStackTrace();
        }

        Log.e("Image Thumb", "--------------" + data.getThumb());

        // Set check box listener android
        fV.check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckBox checkBox = (CheckBox) view;
                if (checkBox.isChecked()) {
                    data.setCheckedBox(true);
                  } else {
                    data.setCheckedBox(false);
                }
            }
        });

        view.setTag(data);
        return view;
    }
    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        fData.clear();
        if (charText.length() == 0) {
            fData.addAll(fArrayList);
        } else {
            for (Utilisateur wp : fArrayList) {
                if (wp.getName().toLowerCase(Locale.getDefault())
                        .contains(charText)) {
                    fData.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }
    static class ViewHolder {
        ImageView imageView;
        TextView title, email;
        CheckBox check;
    }
}
