package com.strider.desafio.gerenciamentotarefas.view;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.strider.desafio.gerenciamentotarefas.R;
import com.strider.desafio.gerenciamentotarefas.util.Prefs;
import com.strider.desafio.gerenciamentotarefas.util.RetrofitSettings;
import com.strider.desafio.gerenciamentotarefas.model.Task;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

public class MainActivity extends AppCompatActivity {

    TextView mNoRequests;
    ListView mTasks;
    ProgressBar mProressBar;
    TaskAdapter adapater; Subscription subscription= null;
    Observable<Long> observable = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTasks = (ListView) findViewById(R.id.tasks);
        mProressBar = (ProgressBar) findViewById(R.id.progress_bar);
        mNoRequests = (TextView) findViewById(R.id.no_requests);

        checkPermissions();
        checkApiBaseUrl();

        subscribe();

    }

    public void subscribe(){
        mProressBar.setVisibility(View.VISIBLE);
        mNoRequests.setVisibility(View.GONE);
        mTasks.setVisibility(View.GONE);
        observable = Observable.interval(10, TimeUnit.SECONDS);
        subscription = observable
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

    private void checkApiBaseUrl() {
        try {
            if (RetrofitSettings.API_BASE_URL.isEmpty())
                RetrofitSettings.changeApiBaseUrl(Prefs.getString(getContext(), "IP"));
        }catch (IllegalArgumentException e){
            showIpDialog();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        showIpDialog();
        return true;
    }

    private void showIpDialog() {
        final Dialog dialog = new Dialog(getContext());
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_ip, null);
        dialog.setContentView(view);

        final EditText mEditText = dialog.findViewById(R.id.ip);
        Button mSaveButton = dialog.findViewById(R.id.save);

        dialog.show();

        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ip = mEditText.getText().toString();
                if (ip.isEmpty() || ip.trim().isEmpty()) {
                    mEditText.setError("Campo obrigatório!");
                }else {
                    Prefs.setString(getContext(), "IP", "http://" + ip + ":8080");
                    RetrofitSettings.changeApiBaseUrl(Prefs.getString(getContext(), "IP"));
                    subscription.unsubscribe();
                    subscribe();
                    dialog.dismiss();
                }
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

    }
}
