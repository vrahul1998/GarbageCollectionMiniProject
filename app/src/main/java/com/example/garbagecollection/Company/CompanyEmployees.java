package com.example.garbagecollection.Company;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.garbagecollection.R;
import com.example.garbagecollection.User.UserActivities;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;


public class CompanyEmployees extends Fragment {
    RecyclerView companyEmployeesRecycleView;
    FirebaseRecyclerAdapter firebaseRecyclerAdapter;

    View companyEmployeesView;

    DatabaseReference fRef;
    FirebaseAuth fAuth;
    FirebaseUser fUser;
    FirebaseRecyclerOptions<Employee> options;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fAuth=FirebaseAuth.getInstance();
        fUser=fAuth.getCurrentUser();

        companyEmployeesView=inflater.inflate(R.layout.fragment_company_employees,container,false);
        companyEmployeesRecycleView=companyEmployeesView.findViewById(R.id.companyEmployeesRecycleView);

        companyEmployeesRecycleView.setHasFixedSize(true);
        companyEmployeesRecycleView.setLayoutManager(new LinearLayoutManager(getContext()));

        fRef= FirebaseDatabase.getInstance().getReference().child("Employees");
        Query query=FirebaseDatabase.getInstance().getReference().child("Employees").orderByChild("cid").equalTo(fUser.getUid());
        options=new FirebaseRecyclerOptions.Builder<Employee>().setQuery(query,Employee.class).build();

        //TODO: Adapter incomplete
        firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<Employee,EmployeesViewHolder>(options){
            @NonNull
            @Override
            public EmployeesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.employee_list_layout,parent,false);

                return new EmployeesViewHolder(view);

            }

            @Override
            protected void onBindViewHolder(@NonNull EmployeesViewHolder employeesViewHolder, int i, @NonNull Employee employee) {
                employeesViewHolder.setTitle(employee.getName());
                employeesViewHolder.setImage(employee.getImage(),getContext());
                employeesViewHolder.setStatus(employee.getStatus());
            }
        };
        companyEmployeesRecycleView.setAdapter(firebaseRecyclerAdapter);

        // Inflate the layout for this fragment
        return companyEmployeesView;
    }
   @Override
   public void onStart(){
        super.onStart();
        firebaseRecyclerAdapter.startListening();
   }


    private static class EmployeesViewHolder extends RecyclerView.ViewHolder{
        View mView;
        public EmployeesViewHolder(@NonNull View itemView) {
            super(itemView);
            mView=itemView;
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   // mClickListener.onItemClick(view,getAdapterPosition());
                }
            });
        }
        private EmployeesViewHolder.ClickListener mClickListener;

        public void setStatus(String status) {
            TextView empStatus=mView.findViewById(R.id.empStatus);
            empStatus.setText("status:"+status);

        }
        public void setImage(String image, Context context){
            ImageView activityImg=mView.findViewById(R.id.empImage);
            Picasso.with(context).load(image).into(activityImg);
        }
        public void setTitle(String name){
            TextView empName=mView.findViewById(R.id.empName);
            empName.setText("Name:"+name);
        }
        public interface ClickListener{
            public void onItemClick(View view,int position);
        }

        public void setOnClickListener(EmployeesViewHolder.ClickListener clickListener) {
            mClickListener = clickListener;
        }
    }
}
