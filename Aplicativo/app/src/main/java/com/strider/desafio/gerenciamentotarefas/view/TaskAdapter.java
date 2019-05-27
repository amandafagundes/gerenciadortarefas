package com.strider.desafio.gerenciamentotarefas.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.strider.desafio.gerenciamentotarefas.R;
import com.strider.desafio.gerenciamentotarefas.Util.RetrofitSettings;
import com.strider.desafio.gerenciamentotarefas.model.Task;

import java.io.File;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

public class TaskAdapter extends BaseAdapter {
    private Context context;
    private List<Task> tasks;
    private Task currentTask;
    private LayoutInflater layoutInflater;

    public TaskAdapter(Context context, List<Task> tasks) {
        this.context = context;
        this.tasks = tasks;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return tasks.size();
    }

    @Override
    public Object getItem(int position) {
        return tasks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return tasks.get(position).getId();
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        currentTask = tasks.get(position);
        view = layoutInflater.inflate(R.layout.task_item, null);
        TextView title = view.findViewById(R.id.title);
        TextView date = view.findViewById(R.id.date);
        Button button = view.findViewById(R.id.button);

        title.setText(currentTask.getTitle());
        date.setText(currentTask.getDate());

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ImageActivity.class);
                Bundle params = new Bundle();
                params.putSerializable("task", currentTask);
                intent.putExtras(params);
                ((Activity) context).startActivityForResult(intent, 0);
            }
        });

        return view;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                final Task task = (Task) data.getSerializableExtra("task");
                if (task.getImage() != null) {
                    task.setStatus("COMPLETE");
                    showUploadDialog(task);
                }
            }
        }
    }

    private void showUploadDialog(final Task t) {
        final Dialog dialog = new Dialog(context);
        dialog.setTitle("Aguarde...");
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_upload, null);
        dialog.setContentView(view);

        TextView mText = dialog.findViewById(R.id.text);
        Button mOkButton = dialog.findViewById(R.id.ok);
        Button mCancelButton = dialog.findViewById(R.id.cancel);

        mText.setText("Enviado resolução da tarefa: \"" + t.getTitle() + " \"");

        dialog.show();

        final Call<Task> call = new RetrofitSettings().getApi().updateTask(t);
        call.enqueue(new Callback<Task>() {
            @Override
            public void onResponse(Call<Task> call, Response<Task> response) {
                File file = new File(t.getImage());
                RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                MultipartBody.Part body = MultipartBody.Part.createFormData("image", file.getName(), requestFile);

                call = new RetrofitSettings().getApi().uploadImage(body, t.getId());
                call.enqueue(new Callback<Task>() {
                    @Override
                    public void onResponse(Call<Task> call, Response<Task> response) {
                        System.out.println("onResponse");
                        removeTask(t.getId());
                        Toast.makeText(context, "Tarefa enviada com sucesso!", Toast.LENGTH_LONG);
                        dialog.dismiss();
                    }

                    @Override
                    public void onFailure(Call<Task> call, Throwable t) {
                        System.out.println("onFailure");
                        dialog.dismiss();
                    }
                });
            }

            @Override
            public void onFailure(Call<Task> call, Throwable t) {
                Toast.makeText(context, "Falha na conexão!", Toast.LENGTH_LONG);
                System.out.println("onFailure");
            }

        });

        mOkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                call.cancel();
                dialog.dismiss();
            }
        });
    }

    private void removeTask(int id) {
        for (Task t : tasks) {
            if (t.getId() == id) {
                tasks.remove(t);
                break;
            }
        }
        notifyDataSetChanged();
    }


}