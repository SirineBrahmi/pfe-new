package com.example.pfe;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ClassroomAdapter extends RecyclerView.Adapter<ClassroomAdapter.ClassroomViewHolder> {

    private Context context;
    private List<Classroom> classroomList;

    public ClassroomAdapter(Context context, List<Classroom> classroomList) {
        this.context = context;
        this.classroomList = classroomList;
    }

    @Override
    public ClassroomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_classroom, parent, false);
        return new ClassroomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ClassroomViewHolder holder, int position) {
        Classroom classroom = classroomList.get(position);
        holder.textClassroomId.setText("ID: " + classroom.getClassroomId());
        holder.textNom.setText("Nom: " + classroom.getNom());
        holder.textCreateur.setText("Créateur: " + classroom.getCreateur());
    }

    @Override
    public int getItemCount() {
        return classroomList.size();
    }

    public static class ClassroomViewHolder extends RecyclerView.ViewHolder {

        TextView textClassroomId, textNom, textCreateur;

        public ClassroomViewHolder(View itemView) {
            super(itemView);
            textClassroomId = itemView.findViewById(R.id.textClassroomId);
            textNom = itemView.findViewById(R.id.textNom);
            textCreateur = itemView.findViewById(R.id.textCreateur);
        }
    }
}
