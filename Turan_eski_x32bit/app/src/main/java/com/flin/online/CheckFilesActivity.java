package com.flin.online;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.flin.online.function.GetFileChecksum;
import com.flin.online.jsonenter.PublicInfo;
import com.flinc.core.R;

import java.io.File;
import java.util.ArrayList;

public class CheckFilesActivity extends AppCompatActivity {

    TextView textViewFinish;
    TextView textViewLabelWarning;
    TextView textViewProgressFull;
    TextView textViewPercent;
    LinearLayout linearLayoutProgressCheckFiles;
    LinearLayout linearLayoutBackSettings;
    LinearLayout linearLayoutReinstallGameFiles;
    ScrollView scrollViewList;
    ListView listViewСheckFiles;
    ImageView imageLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_files);

        TextView infoVersionApp = (TextView) findViewById(R.id.textViewVersion);
        infoVersionApp.setText("v"+ PublicInfo.VersionNameAppStatic+" (Build "+PublicInfo.VersionAppStatic+")");


        textViewPercent = findViewById(R.id.textViewPercent);
        textViewProgressFull = findViewById(R.id.textViewProgressFull);
        textViewLabelWarning = findViewById(R.id.textViewLabelWarning);
        linearLayoutProgressCheckFiles = findViewById(R.id.linearLayoutProgressCheckFiles);

        textViewFinish = findViewById(R.id.textViewFinish);
        textViewFinish.setText("Список файлов которые повреждены или заменены сборкой. Если в списке ");
        linearLayoutBackSettings = findViewById(R.id.linearLayoutBackSettings);
        linearLayoutReinstallGameFiles = findViewById(R.id.linearLayoutReinstallGameFiles);
        scrollViewList = findViewById(R.id.scrollViewList);
        listViewСheckFiles = findViewById(R.id.listViewСheckFiles);




        CheckStart task = new CheckStart();
        task.execute();




    }

    private class CheckStart extends AsyncTask<Void, ProgressData, String> {

        private ProgressBar progressBar;

        private ImageView imageView2;
        ListView listView;
        private ArrayAdapter<String> adapter;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            adapter = new ArrayAdapter<String>(CheckFilesActivity.this, android.R.layout.simple_list_item_1, new ArrayList<String>());
            //adapter.add("1"); // Добавляем имя файла в ArrayAdapter
            listView = findViewById(R.id.listViewСheckFiles);
            listView.setAdapter(adapter);

            imageView2 = findViewById(R.id.imageView2);
            progressBar = findViewById(R.id.progressBarInstall);
            progressBar.setMax(100);
            progressBar.setProgress(0);
            progressBar.setVisibility(View.VISIBLE);
            textViewProgressFull.setText("Подсчет файлов для проверки...");
            textViewPercent.setText("0%");
        }

        @Override
        protected String doInBackground(Void... voids) {
            int progress = 0;


            String[] textFilesGame = {"/FlinOnline/files/texdb/", "/FlinOnline/files/SAMP/", "/FlinOnline/files/data/","/FlinOnline/files/Text/","/FlinOnline/files/SAMP/fonts/"};

            //Получаем количество всех файлов по массиву
            int countFilesMax = 0;
            int countFilesProgress = 0;
            int countDirectory = 0;
            for (int i = 0; i < textFilesGame.length; i++) {
                File directory = new File(Environment.getExternalStorageDirectory() + textFilesGame[i]);
                // Получаем список всех файлов в каталоге
                File[] files = directory.listFiles();
                countDirectory++;
                StringBuilder sb = new StringBuilder();
                for (int d = 0; d < countDirectory; d++) {
                    sb.append(".");
                }
                String str = sb.toString();
                ProgressData data = new ProgressData(+1, "Подсчет файлов для проверки"+str);
                publishProgress(data);
                countFilesMax = countFilesMax + files.length;
            }

            for (int i = 0; i < textFilesGame.length; i++) {

                File directory = new File(Environment.getExternalStorageDirectory()  + textFilesGame[i]);
                // Получаем список всех файлов в каталоге
                File[] files = directory.listFiles();

                // Перечисляем все файлы в каталоге
                for (File file : files) {
                    if (file.isFile()) {
                        System.out.println("Mihail Путь: "+file.getPath()); // Выводим путь к файлу
                        System.out.println("Mihail Кэш: "+ GetFileChecksum.getHash(file.getPath()) ); // Выводим путь к файлу ;
                        countFilesProgress++;
                        int counts = (int) ((countFilesProgress / countFilesMax) * 100);
                        ProgressData data = new ProgressData(countFilesProgress, "Файл: "+file.getName()+" ["+countFilesProgress+" из "+countFilesMax+"]");
                        adapter.add(file.getName()); // Добавляем имя файла в ArrayAdapter
                        publishProgress(data);
                        try {
                            if(counts > 89) Thread.sleep(250);
                            else Thread.sleep(150);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            return "Task completed";
        }


        protected void onProgressUpdate(ProgressData... values) {
            super.onProgressUpdate(values);
            progressBar.setProgress(values[0].value1);
            textViewProgressFull.setText(""+values[0].value2);
            textViewPercent.setText(values[0].value1+"%");
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            listView.setVisibility(View.VISIBLE);
            imageView2.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
            linearLayoutProgressCheckFiles.setVisibility(View.GONE);
            textViewLabelWarning.setVisibility(View.GONE);
            textViewFinish.setVisibility(View.VISIBLE);
            linearLayoutReinstallGameFiles.setVisibility(View.VISIBLE);
            linearLayoutBackSettings.setVisibility(View.VISIBLE);
            scrollViewList.setVisibility(View.VISIBLE);
        }
    }

    public class ProgressData {
        public int value1;
        public String value2;

        public ProgressData(int value1, String value2) {
            this.value1 = value1;
            this.value2 = value2;
        }
    }

}