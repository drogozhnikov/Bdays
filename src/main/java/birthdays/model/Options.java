package birthdays.model;

public class Options {

    private int fullScreenHeigth = 1080;
    private int fullScreenWidth = 1920;

    private int screehHeight = 600;
    private int screenWidth = 800;

    private int cdHeigth = 150;
    private int cdWidth = 300;

    public static Options instance = null;

    public static void setInstance(Options options) {
        instance = options;
    }

    public void setFullScreenHeigth(int fullScreenHeigth) {
        this.fullScreenHeigth = fullScreenHeigth;
    }

    public void setFullScreenWidth(int fullScreenWidth) {
        this.fullScreenWidth = fullScreenWidth;
    }

    public void setScreehHeight(int screehHeight) {
        this.screehHeight = screehHeight;
    }

    public void setScreenWidth(int screenWidth) {
        this.screenWidth = screenWidth;
    }

    public void setCdHeigth(int cdHeigth) {
        this.cdHeigth = cdHeigth;
    }

    public void setCdWidth(int cdWidth) {
        this.cdWidth = cdWidth;
    }

}

