package icu.shaoyayu.android.security.presenter.entity;

/**
 * @author shaoyayu
 * 基本数据信息
 */
public class UpdateJsonBean  {
    protected String versionName;
    protected String appName;
    protected String versionIntroduction;
    protected String downloadAddress;
    protected String releaseTime;

    //对于服务器的值
    public interface BeanKey{
        String VERSION_NAME = "version_name";
        String NAME = "name";
        String INTRO = "introduction";
        String APK_LINK = "apk_link";
        String RELEASE_TIME = "release_time";
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getVersionIntroduction() {
        return versionIntroduction;
    }

    public void setVersionIntroduction(String versionIntroduction) {
        this.versionIntroduction = versionIntroduction;
    }

    public String getDownloadAddress() {
        return downloadAddress;
    }

    public void setDownloadAddress(String downloadAddress) {
        this.downloadAddress = downloadAddress;
    }

    public String getReleaseTime() {
        return releaseTime;
    }

    public void setReleaseTime(String releaseTime) {
        this.releaseTime = releaseTime;
    }
}
