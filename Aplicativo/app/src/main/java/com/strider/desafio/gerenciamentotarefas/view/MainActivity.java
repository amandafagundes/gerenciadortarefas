package com.strider.desafio.gerenciamentotarefas.view;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.strider.desafio.gerenciamentotarefas.R;
import com.strider.desafio.gerenciamentotarefas.Util.RetrofitSettings;
import com.strider.desafio.gerenciamentotarefas.model.Task;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

public class MainActivity extends AppCompatActivity {

    TextView mNoRequests;
    ListView mTasks;
    ProgressBar mProressBar;
    TaskAdapter adapater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTasks = (ListView) findViewById(R.id.tasks);
        mProressBar = (ProgressBar) findViewById(R.id.progress_bar);
        mNoRequests = (TextView) findViewById(R.id.no_requests);

        checkPermissions();

        Observable.interval(10, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Long>() {
                    @Override
                    public void onCompleted() {
                        System.out.println("onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        System.out.println("onError");
                        mProressBar.setVisibility(View.GONE);
                        mTasks.setVisibility(View.GONE);
                        mNoRequests.setText("Falha na comunicação com o servidor");
                        mNoRequests.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onNext(Long aLong) {
                        System.out.println("oi");
                        HashMap<String, Object> param = new HashMap<>();
                        param.put("status", "PENDING");
                        final Call<List<Task>> call = new RetrofitSettings().getApi().getTasks(param);
                        call.enqueue(new Callback<List<Task>>() {
                            @Override
                            public void onResponse(Call<List<Task>> call, Response<List<Task>> response) {
                                mProressBar.setVisibility(View.GONE);
                                if (!response.body().isEmpty()) {
                                    mNoRequests.setVisibility(View.GONE);
                                    adapater = new TaskAdapter(getContext(), response.body());
                                    mTasks.setAdapter(adapater);
                                    mTasks.setVisibility(View.VISIBLE);
                                } else {
                                    mTasks.setVisibility(View.GONE);
                                    mNoRequests.setText("Sem tarefas pendentes");
                                    mNoRequests.setVisibility(View.VISIBLE);
                                }
                            }

                            @Override
                            public void onFailure(Call<List<Task>> call, Throwable t) {
                                mProressBar.setVisibility(View.GONE);
                                mTasks.setVisibility(View.GONE);
                                mNoRequests.setText("Falha na comunicação com o servidor");
                                mNoRequests.setVisibility(View.VISIBLE);
                            }
                        });

                    }
                });
    }

    private Context getContext() {
        return this;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        adapater.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                String strEditText = data.getStringExtra("editTextValue");
            }
        }
    }

    private void checkPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            }, 0);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
        }

    }
}
