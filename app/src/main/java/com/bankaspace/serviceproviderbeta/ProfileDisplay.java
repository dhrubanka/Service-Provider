package com.bankaspace.serviceproviderbeta;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bankaspace.serviceproviderbeta.imagehandler.ImageLoader;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileDisplay extends Fragment {
    TextView userid,usernme,emailid,gendertype,categorytype,fullname,personalno,pricerange,businessdes,whatsapp,localitty,addresss;
    ImageView picpath;


    public ProfileDisplay() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (!SharedPrefManager.getInstance(getContext()).isLoggedIn()) {

            startActivity(new Intent(getContext(), LoginActivity.class));
        }

        // Inflate the layout for this fragment
       View view= inflater.inflate(R.layout.profile_display_fragment, container, false);
        userid=view.findViewById(R.id.userid);
        usernme=view.findViewById(R.id.username);
        emailid=view.findViewById(R.id.emailid);
        gendertype=view.findViewById(R.id.gender);
        categorytype=view.findViewById(R.id.categorytype);
        fullname=view.findViewById(R.id.fullname);
        personalno=view.findViewById(R.id.mobileNumber);
        pricerange=view.findViewById(R.id.pricing);
        businessdes=view.findViewById(R.id.businessdescription);
        whatsapp=view.findViewById(R.id.whatsapp);
        localitty=view.findViewById(R.id.localitty);
        addresss=view.findViewById(R.id.addresss);


        User user=SharedPrefManager.getInstance(getContext()).getUser();
        userid.setText(user.getId());
        usernme.setText(user.getUsername());
        emailid.setText(user.getEmail());
        gendertype.setText(user.getGender());
        categorytype.setText(user.getCategory());
        fullname.setText(user.getName());
        personalno.setText(user.getPersonalno());
        pricerange.setText(user.getPricerange());
        businessdes.setText(user.getBusinessdes());
        whatsapp.setText(user.getWhatsapp());
        localitty.setText(user.getLocality());
        addresss.setText(user.getAddress());

        int loader = R.drawable.man;
        picpath=(ImageView) view.findViewById(R.id.profilepicture);
        String image_url =(String) user.getPicpath();
        Glide.with(this)
                .load(image_url)
                .centerCrop()
                .into(picpath);




       return view;
    }

}
