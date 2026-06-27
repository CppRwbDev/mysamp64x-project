package com.flin.online.jsonenter;


public final class PublicInfo {
    //Статика

    //   public static int VersionAppStatic = 56;
    //  public static String VersionNameAppStatic = "4.0.9b";


    public static int VersionAppStatic = 134;
    public static String VersionNameAppStatic = "1.1.5 (open Test)";

    /*public static int VersionAppStatic = BuildConfig.VERSION_CODE;
    public static String VersionNameAppStatic = BuildConfig.VERSION_NAME;*/

    public static int SourceApp = 8; //DL - 1 // Инста 2 //вк - 3 //пост - 4  // 5 - обнова // 6 trashbox //7 - дичь //8 - play  //9 - 4pda //55 тестеры

    //Обновление через сеть
    public static int status = 0; //0 - неизвестно 1 - есть инет, 2 нету

    public static int checkVerLauncher = 14;
    public static int checkVerClient = 61;
    public static int checkStartClient;

    //Выбор установки Lite/Full
    public static int SelectInstallTypeClient = 0;
    //Выбор установки обновления
    public static int updateInstallTypeClient = 0;

    //Ссылка на скачивание
    public static int jsonVersion = 0;
    public static boolean successInstallApk = false;
    public static boolean successUpdateApk = false;
    public static int successDonate = 0;
    public static int successInstallMods = 0;

    public static int insertID = 0;

    public static String donateUrl;

    public static String checkVerName;
    public static int checkVerClientType;
    public static int checkInstallGame;

    public static int modsVersion = 1;
    public static int modsVersionCancel = 1;
    public static int modsAccess = 1;
    public static String modsAccessText = "none";
    public static String modsDomainHost = "http://d1.flin-rp.su";
    public static String modsFolderMods = "/mods/";
    public static String modsName = "flin_winter.zip";
    public static String modsCheckSum = "1805752fee308ec09e5ff8cc38b125b8";
    public static int modsSize = 2150;

    //Новый клиент
    public static int checkReleaseClient = 1;
    public static int checkCountDownloadsFiles = 1;
    public static int checkCountDownloadsFiles7z = 1;
    public static int checkCountDownloadsFilesCurrent = 1;
    public static String checkReleasePackageName = "com.flin.online";

    public static String checkFilesGameURL = "https://d1.flin-rp.com/client/filesgame/";
    public static String checkFilesGame1 = "files.z01";
    public static String checkFilesGame2 = "files.z02";
    public static String checkFilesGame3 = "files.zip";
    public static String checkFilesGame4 = "";
    public static String checkFilesGame5 = "";
    public static String checkFilesGame6 = "";
    public static String checkFilesGame7 = "";
    public static String checkFilesGame8 = "";
    public static String checkFilesGame9 = "";
    public static String checkFilesGame10 = "";
    public static String checkFilesGame11 = "";
    public static String checkFilesGame12 = "";
    public static String checkFilesGame13 = "";
    public static String checkFilesGame14 = "";
    public static String checkFilesGameArchive = "files.zip";
    public static int checkFilesGameArchiveVersion = 1;

    public static String checkFilesClientURL = "https://d1.flin-rp.com/client/filesclient/";
    public static String checkFilesClient1 = "files_samp.zip";
    public static String checkFilesClient2 = "";
    public static String checkFilesClient3 = "";
    public static int checkFilesClientVersion = 2;


    public static String checkFilesClientBetaURL = "https://d1.flin-rp.com/client/filesclient/";
    public static String checkFilesClientBeta = "files_samp.zip";
    public static int checkFilesClientBetaVersion = 1;

    public static String checkFilesTexctureURL = "https://d1.flin-rp.com/client/filestexture/";
    public static String checkFilesTexcture1 = "files_texture.zip";
    public static String checkFilesTexcture2 = "";
    public static String checkFilesTexcture3 = "";
    public static String checkFilesTexcture4 = "";
    public static String checkFilesTexcture5 = "";
    public static int checkFilesTexctureVersion = 1;

    public static String checkFilesTexctureURLBeta = "https://d1.flin-rp.com/client/filestexture/";
    public static String checkFilesTexctureBeta = "files_texture.zip";

    public static String checkFilesPatchUpdateURL = "";
    public static String checkFilesPatchUpdate1 = "";
    public static String checkFilesPatchUpdate2 = "";
    public static String checkFilesPatchUpdate3 = "";
    public static String checkFilesPatchUpdate4 = "";
    public static String checkFilesPatchUpdate5 = "";
    public static int checkFilesPatchUpdateVersion = 1;


    public static String checkFilesTextDBURL = "https://d1.flin-rp.com/client/filestexture/";
    public static String checkFilesTextDB1 = "files_texture.zip";
    public static String checkFilesTextDB2 = "";
    public static String checkFilesTextDB3 = "";
    public static int checkFilesTextDBVersion = 1;
    public static int checkFilesTextDBVersionLocal = 0;



    public static String checkDownLoadAPK = "https://d1.flin-rp.com/client/apk/";
    public static String checkDownLoadAPKName = "flinDebug-0.01-flin-20.12.121412.apk";
    public static int checkDownLoadAPKVersion = 1;

    public static String checkDownLoadBetaAPK = "https://d1.flin-rp.com/client/apk/";
    public static String checkDownLoadAPKBetaName = "flinDebug-0.01-flin-20.12.121412.apk";
    public static int checkDownLoadBetaVersion = 1;

    public static String PayMethodServer1 = "https://unitpay.ru/pay/182051-6b933?sum=";
    public static String PayMethodServer2 = "https://unitpay.ru/pay/203041-490e1?sum=";


    public static String checkURLListDownloadFiles = "http://d1.flin-rp.su";

    //Донат
    public static int donatetype = 1; //qiwi unitpay



    //Контакты
    public static String contactTelegram ;
    public static String contactVK;
    public static String contactDiscord;
    public static String contactYouTube;
    public static String contactForum;
    public static String contactSite;
    public static String urlSendTechProblem;

    public static String urlMonitoring;
    public static String urlUpdateAPK;
    public static int verClient;
    public static int verLauncher;

    //
    public static int currentFilesUpdate;


    //Сервер Мониторинг
    public static int OnlineServerOne = 100;
    public static int OnlineServerTwo = 100;


    //Даты для обновления
    public static String  dateUpdateFiles;
    public static String  dateUpdateApk;


    //Финиш загрузки на карту
    public static boolean finishTask;

    //Дирректори игры
    public static int dirGameLocalType;
    public static String dirGameLocal = "/storage/0C30-F321/dd/";
    public static String dirGameLocalSelectCard = "/storage/0C30-F321/dd/";



    //Новые архивы

    public static String checkFilesGame7ZURL;
    public static String checkFilesGame7Z1;
    public static String checkFilesGame7Z2;
    public static String checkFilesGame7Z3;
    public static String checkFilesGame7Z4;
    public static String checkFilesGame7Z5;
    public static String checkFilesGameArchive7Z;
    public static int checkFilesGame7ZUpdateLoading; //Проверка на загрузку наложенных архивов
    public static int checkSelectInstall; //Проверка

    public static String checkFilesTexctureURL7Z = "https://d1.flin-rp.com/client/filestexture/";
    public static String checkFilesTexcture7Z1 = "files_texture.zip";
    public static String checkFilesTexcture7Z2 = "";
    public static String checkFilesTexcture7Z3 = "";
    public static String checkFilesTexcture7Z4 = "";
    public static String checkFilesTexcture7Z5 = "";
    public static int checkFilesTexctureVersion7Z = 1;


    public static String checkFilesClientURL7Z = "https://d1.flin-rp.com/client/filesclient/";
    public static String checkFilesClient7Z1 = "files_samp.zip";
    public static String checkFilesClient7Z2 = "";
    public static String checkFilesClient7Z3 = "";
    public static int checkFilesClientVersion7Z = 2;

    public static String checkFilesTextDBURL7Z = "https://d1.flin-rp.com/client/filestexture/";
    public static String checkFilesTextDB7Z1 = "files_texture.zip";
    public static String checkFilesTextDB7Z2 = "";
    public static String checkFilesTextDB7Z3 = "";
    public static int checkFilesTextDBVersion7Z = 1;

    public static int checkInstallLastVersion; //Проверка


    public static String jsonMods; //
    public static String checkModsURL = "";
    //   public static String checkModsURL = "https://d1.flin-rp.com/mods/";
    public static int GTAmods;
    public static String GTAmodsDir;
    public static int checkInstallModsLastVersion; //Проверка

    //Диалоги
    public static int DialogID = -1;
    public static int DialogType = -1;

    public static String gpu;
    public static int gpuID; //1 - Adreno 2 - Mali 3 - PowerGP
    public static int checkGPU = 0;


    //Авторизация
    public static String pp;
    public static String ppp;
    public static int selectServerConnect = 0; //Выбранный сервер в диалоге при входе в игру
    public static String getSelectServerConnectNick = "none"; //Выбранный сервер в диалоге при входе в игру
    public static int goggleCodeInit = 0;
    public static String goggleCode = "none";

    public static int dialogID_pass = 0;
    public static int dialogID_admPass = 0;
    public static int dialogID_google = 0;


    public static String json_news = "none";
    public static int loagingNews;

    //Сообщение о багах
    public static String error_sendMessage = " ";
    public static String checkPostDataDonate = " ";

    public static String sendGameTelegram = "";
    public static String UID = "";
    public static String UIP = "192.168.1.5";

    int countStart =0;

}