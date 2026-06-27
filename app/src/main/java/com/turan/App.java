package com.turan;

import android.app.Activity;
import android.net.ConnectivityManager;

import androidx.multidex.MultiDexApplication;

import com.turan.launcher.network.Server;
import com.turan.launcher.network.ServerListener;
import com.turan.launcher.network.Story;
import com.turan.game.BuildConfig;

import java.io.File;
import java.util.ArrayList;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.google.firebase.FirebaseApp;
import com.google.firebase.analytics.FirebaseAnalytics;
import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import es.dmoral.toasty.Toasty;

public class App extends MultiDexApplication
{
	private static App instance;
	private FirebaseAnalytics mFirebaseAnalytics;
	//
	public Integer downloadID = null;
	public static final Integer INSTALL_TYPE_CLIENT = 1;
	public static final Integer INSTALL_TYPE_GAMEFILES = 2;
	public static final Integer INSTALL_TYPE_UPDATE_GAMEFILES = 3;
	//
	private String tempNick;
	private String currentGPU;
	//
	public String errorInfo, errorText;
	//
	public static final String ADRENO_TEGRA = "dxt";
	public static final String ETC = "etc";
	public static final String MALI = "etc";//"mali";
	public static final String POWER_VR = "pvr";
	//
	public ArrayList<Server> serverList;
	public ServerListener serverListener;
	public ArrayList<Story> stories;
	// urls - links
	public String URL_CLIENT;
	public String URL_GAME_FILES;
	public String URL_GAME_FILES_UPDATE;
	public String URL_VK;
	public String URL_DISCORD;
	public String URL_YOUTUBE;
	public String URL_FORUM;
	public String URL_DONATE;
	public Integer targetClientVersion;
	public Integer targetGameFilesVersion;
	//
	@Override
	public void onCreate() {
		super.onCreate();
		instance = this;

		Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
			@Override
			public void uncaughtException(Thread thread, Throwable throwable) {
				Log.e("CRASH", "Uncaught exception in thread " + thread.getName(), throwable);
				throwable.printStackTrace();
				android.os.Process.killProcess(android.os.Process.myPid());
				System.exit(1);
			}
		});

		try {
			FirebaseApp.initializeApp(this);
			mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
			Log.d("GoldParaha", "Firebase initialized successfully");
			if (mFirebaseAnalytics != null) {
				setUserProperties();
			} else {
				Log.w("GoldParaha", "FirebaseAnalytics instance is null");
			}
		} catch (Exception e) {
			Log.e("GoldParaha", "Error initializing Firebase: " + e.getMessage(), e);
		}

		initLogger();
		serverList = new ArrayList<>();
		serverList.add(new Server("198.163.207.202", 7777, "Turan Mobile", 1));

		// Load URLs from api.json
		loadApiConfig();
		// Load servers from servers.json
		loadServersConfig();

		stories = new ArrayList<>();
	}

	private void loadApiConfig() {
		new Thread(() -> {
			try {
				String githubRawUrl = "https://raw.githubusercontent.com/CppRwbDev/Cache_Gta_SAMPx64/main/api.json";
				String jsonContent = downloadFromUrl(githubRawUrl);

				if (jsonContent != null) {
					Gson gson = new Gson();
					JsonObject json = gson.fromJson(jsonContent, JsonObject.class);

					if (json != null) {
						targetClientVersion = json.get("clientVersionCode").getAsInt();
						targetGameFilesVersion = json.get("gameFilesVersionCode").getAsInt();
						URL_GAME_FILES = json.get("URL_GAME_FILES").getAsString();
						URL_GAME_FILES_UPDATE = json.get("URL_GAME_FILES_UPD").getAsString();
						URL_VK = json.get("URL_VK").getAsString();
						URL_DISCORD = json.get("URL_DISCORD").getAsString();
						URL_YOUTUBE = json.get("URL_YOUTUBE").getAsString();
						URL_FORUM = json.get("URL_FORUM").getAsString();
						URL_DONATE = json.get("URL_DONATE").getAsString();
						URL_CLIENT = json.get("URL_CLIENT").getAsString();

						Log.d("GoldParaha", "API config loaded from GitHub successfully");
						Log.d("GoldParaha", "URL_GAME_FILES: " + URL_GAME_FILES);
					}
				}
			} catch (Exception e) {
				Log.e("GoldParaha", "Error loading api.json from GitHub: " + e.getMessage(), e);
				// Fallback to default values
				URL_GAME_FILES = "https://github.com/TuranGame/turan-cache/releases/download/v1.1.1/game.zip";
				URL_GAME_FILES_UPDATE = "https://github.com/TuranGame/turan-cache/releases/download/v1.1.1/files_upd.zip";
				targetClientVersion = 11;
				targetGameFilesVersion = 100;
			}
		}).start();
	}

	private String downloadFromUrl(String urlString) {
		try {
			URL url = new URL(urlString);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.setConnectTimeout(10000);
			connection.setReadTimeout(10000);

			int responseCode = connection.getResponseCode();
			if (responseCode == HttpURLConnection.HTTP_OK) {
				BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				StringBuilder response = new StringBuilder();
				String line;
				while ((line = reader.readLine()) != null) {
					response.append(line);
				}
				reader.close();
				return response.toString();
			} else {
				Log.e("GoldParaha", "HTTP error code: " + responseCode);
				return null;
			}
		} catch (Exception e) {
			Log.e("GoldParaha", "Error downloading from URL: " + e.getMessage(), e);
			return null;
		}
	}

	private void loadServersConfig() {
		new Thread(() -> {
			try {
				String githubRawUrl = "https://raw.githubusercontent.com/CppRwbDev/Cache_Gta_SAMPx64/main/servers.json";
				String jsonContent = downloadFromUrl(githubRawUrl);

				if (jsonContent != null) {
					Gson gson = new Gson();
					Server[] servers = gson.fromJson(jsonContent, Server[].class);

					if (servers != null && servers.length > 0) {
						serverList.clear();
						for (Server server : servers) {
							serverList.add(server);
						}
						Log.d("GoldParaha", "Servers loaded from GitHub successfully. Count: " + serverList.size());
					}
				}
			} catch (Exception e) {
				Log.e("GoldParaha", "Error loading servers.json from GitHub: " + e.getMessage(), e);
			}
		}).start();
	}

	private void setUserProperties() {
		try {
			mFirebaseAnalytics.setUserProperty("app_version", "0.8.2.1");
			mFirebaseAnalytics.setUserProperty("device_architecture", "ARMx64");
			mFirebaseAnalytics.setUserProperty("build_type", "release");
			Log.d("GoldParaha", "User properties set successfully");
		} catch (Exception e) {
			Log.e("GoldParaha", "Error setting user properties: " + e.getMessage(), e);
		}
	}

	private void initLogger () {
		try {
			File appDir = new File(Config.APP_PATH);
			if (!appDir.exists()) {
				appDir.mkdirs();
			}
			File logcat = new File(Config.APP_PATH + "/logcat.txt");
			if (logcat.exists()) {
				logcat.delete();
			}
			logcat.createNewFile();
			Runtime runtime = Runtime.getRuntime();
			runtime.exec("logcat -f " + logcat.getAbsolutePath());
		} catch (Exception e) {
			Utils.writeLog(getInstance(), 'e', "InitLogger error: "+e.getMessage());
		}
		try {
			((ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE)).setNetworkPreference(ConnectivityManager.DEFAULT_NETWORK_PREFERENCE);
		}
		catch (Exception unused) {}
	}

	public static App getInstance() { return instance; } // get app context
	//
	public String getTempNickName() { return this.tempNick; } // временный ник
	public void setTempNickName(String str) { this.tempNick = str; }
	//
	public ArrayList<Server> getServerList() { return this.serverList; } // список серверов
	public ArrayList<Story> getStories() { return this.stories; } // список сторисов
	//
	public void setGPU(String str) {
		currentGPU = str;
	}

	public String getGPU() {
		return currentGPU;
	}
	//
	public static boolean isExternalStorageAvailable(Activity activity) {
		try {
			activity.getExternalFilesDir((String) null).getAbsolutePath();
			return true;
		} catch (Exception unused) {
			Toasty.error(activity, "нет доступа к хранилищу!", Toasty.LENGTH_LONG);
			return false;
		}
	}

	public static boolean isAppInstalledFromMarket(String marketNamePacket) {
		if(getInstance().getPackageManager().
				getInstallerPackageName(getInstance().getPackageName()).equals(marketNamePacket)) {
			return true;
		}
		return false;
	}

	// Yandex Metrika metodi OLIB TASHLANDI
}