/*
 * Copyright (c) 2024.  Andres Mora getSoftware CO
 */

package com.getsoftwareCO.bajarvideosamicelulargratis.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.getsoftwareCO.bajarvideosamicelulargratis.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SavedAdapter extends RecyclerView.Adapter<SavedAdapter.SavedHolder> {

    private List<File> list;
    private Context context;

    public SavedAdapter(List<File> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public SavedHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.downloads_completed_item, parent, false);
        return new SavedHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SavedHolder holder, @SuppressLint("RecyclerView") final int position) {
        holder.name.setText(list.get(position).getName());

        Glide.with(context)
                .load(list.get(position).getAbsoluteFile())
                .into(holder.thumbnail);

        holder.thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                Uri fileUri = FileProvider.getUriForFile(context, "com.getsoftwareCO.bajarvideosamicelulargratis.fileprovider", list.get(position));
                intent.setDataAndType(fileUri, "video/*");
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // Añade esta línea
                context.startActivity(intent);
            }
        });

        holder.menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popUp(view, list.get(position));
            }
        });
    }

    private void popUp(View view, final File f) {
        final Context activityContext = context.getApplicationContext();

        // Verifica si el contexto es una instancia de Activity y no está finalizando
        if (activityContext instanceof Activity && !((Activity) activityContext).isFinishing()) {
            final PopupMenu popup = new PopupMenu(activityContext, view);
            popup.getMenuInflater().inflate(R.menu.download_menu, popup.getMenu());
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                public boolean onMenuItemClick(MenuItem item) {
                    int i = item.getItemId();
                    if (i == R.id.download_delete) {
                        new AlertDialog.Builder(activityContext)
                                .setMessage("Are you sure you want to delete?")
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (f.delete()) {
                                            updateList();
                                        }
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                })
                                .create()
                                .show();
                        return true;
                    } else if (i == R.id.download_share) {
                        File file = new File(Environment.getExternalStoragePublicDirectory(context.getResources().getString(R.string.app_name)), f.getName());
                        Uri fileUri = FileProvider.getUriForFile(context, "com.descarga.bajarvideosamicelular.fileprovider", file);

                        StringBuilder msg = new StringBuilder();
                        msg.append(context.getResources().getString(R.string.msg_share));
                        msg.append("\n");
                        msg.append(context.getResources().getString(R.string.app_link));

                        if (fileUri != null) {
                            Intent shareIntent = new Intent();
                            shareIntent.setAction(Intent.ACTION_SEND);
                            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); // temp permission for receiving app to read this file
                            shareIntent.setType("*/*");
                            shareIntent.putExtra(Intent.EXTRA_TEXT, msg.toString());
                            shareIntent.putExtra(Intent.EXTRA_STREAM, fileUri);
                            try {
                                activityContext.startActivity(Intent.createChooser(shareIntent, "Share via"));
                            } catch (ActivityNotFoundException e) {
                                Toast.makeText(activityContext, "No App Available", Toast.LENGTH_SHORT).show();
                            }
                        }
                        return true;
                    } else {
                        return onMenuItemClick(item);
                    }
                }
            });
            popup.show();
        } else {
            Log.e("popUp", "Invalid context or activity is finishing.");
        }
    }


    public void updateList() {
        File videoFile = new File(Environment.getExternalStoragePublicDirectory(context.getResources().getString(R.string.app_name)).getAbsolutePath());
        if (videoFile.exists()) {
            List<File> nonExistentFiles = new ArrayList<>();
            nonExistentFiles.addAll(Arrays.asList(videoFile.listFiles()));
            this.list = nonExistentFiles;
            notifyDataSetChanged();
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class SavedHolder extends RecyclerView.ViewHolder {

        private TextView name;
        private ImageView thumbnail;
        private ImageView menu;

        public SavedHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.downloadCompletedName);
            thumbnail = itemView.findViewById(R.id.downloadThumnail);
            menu = itemView.findViewById(R.id.download_menu);
        }
    }
}
