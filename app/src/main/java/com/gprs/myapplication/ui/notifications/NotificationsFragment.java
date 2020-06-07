package com.gprs.myapplication.ui.notifications;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.gprs.myapplication.CustomQuoraAdapter1;
import com.gprs.myapplication.QuoraHelper;
import com.gprs.myapplication.R;

import java.util.ArrayList;

public class NotificationsFragment extends Fragment {

    ListView listView;
    CustomQuoraAdapter1 customAdapter1;
    ArrayList<QuoraHelper> arrayList;
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        final View root = inflater.inflate(R.layout.fragment_home, container, false);

        arrayList=new ArrayList<>();
        pref = root.getContext().getSharedPreferences("user", 0); // 0 - for private mode
        editor = pref.edit();


        listView=root.findViewById(R.id.quoralist);

        customAdapter1=new CustomQuoraAdapter1(getActivity(),arrayList);
        listView.setAdapter(customAdapter1);

        customAdapter1.notifyDataSetChanged();

        FirebaseDatabase.getInstance().getReference().child("Quora").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot!=null){
                    for (DataSnapshot snapshot:dataSnapshot.getChildren() ) {
                        QuoraHelper quoraHelper = snapshot.getValue(QuoraHelper.class);
                        if (quoraHelper.getPhone() != null) {
                            if (quoraHelper.getPhone().equals(pref.getString("user", ""))) {
                                arrayList.add(quoraHelper);
                                customAdapter1.notifyDataSetChanged();
                            }

                        }
                    }


                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return root;
    }
}
