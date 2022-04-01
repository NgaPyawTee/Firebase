package com.homework.firebase;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterForRE extends RecyclerView.Adapter<AdapterForRE.ViewHolder> {
    private Context context;
    private List<Upload> uploadList;

    private Listener interfaceListener;

    public AdapterForRE(Context context, List<Upload> uploadList) {
        this.uploadList = uploadList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.image_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Upload uploadCurrent = uploadList.get(position);
        holder.name.setText(uploadCurrent.getName());
        Picasso.get()
                .load(uploadCurrent.getImageURL())
                .placeholder(R.mipmap.ic_launcher)
                .fit()
                .centerCrop()
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return uploadList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
    View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {
        public TextView name;
        public ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name_tv);
            imageView = itemView.findViewById(R.id.adapter_image_view);

            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onClick(View view) {
            if (interfaceListener != null){
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION){
                    interfaceListener.onItemClicked(position);
                }
            }
        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            contextMenu.setHeaderTitle("Select action");
            MenuItem doWhatever = contextMenu.add(Menu.NONE,1,1,"Do whatever");
            MenuItem delete = contextMenu.add(Menu.NONE,2,2,"Delete");

            doWhatever.setOnMenuItemClickListener(this);
            delete.setOnMenuItemClickListener(this);
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            if (interfaceListener != null){
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION){
                    switch (menuItem.getItemId()){
                        case 1:
                            interfaceListener.onDoWhateverClicked(position);
                            return true;
                        case 2:
                            interfaceListener.onDeleteClicked(position);
                            return true;
                    }
                }
            }
            return false;
        }
    }

    public interface Listener {
        void onItemClicked (int position);

        void onDoWhateverClicked (int position);

        void onDeleteClicked (int position);
    }

    public void setListener(Listener listener){
        interfaceListener = listener;
    }
}
