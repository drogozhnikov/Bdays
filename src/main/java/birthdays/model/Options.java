package birthdays.model;

public class Options {

    private int fullScreenHeigth = 1080;
    private int fullScreenWidth = 1920;

    private int screehHeight = 600;
    private int screenWidth = 800;

    private int cdHeigth = 150;
    private int cdWidth = 300;

    private String pgLowCase;
    private String pgUpperCase;
    private String pgNums;

    public static Options instance = null;


    public static synchronized Options getInstance() {
        return instance;
    }

    public static void setInstance(Options options) {
        instance = options;
    }

    public int getFullScreenHeigth() {
        return fullScreenHeigth;
    }

    public void setFullScreenHeigth(int fullScreenHeigth) {
        this.fullScreenHeigth = fullScreenHeigth;
    }

    public int getFullScreenWidth() {
        return fullScreenWidth;
    }

    public void setFullScreenWidth(int fullScreenWidth) {
        this.fullScreenWidth = fullScreenWidth;
    }

    public int getScreehHeight() {
        return screehHeight;
    }

    public void setScreehHeight(int screehHeight) {
        this.screehHeight = screehHeight;
    }

    public int getScreenWidth() {
        return screenWidth;
    }

    public void setScreenWidth(int screenWidth) {
        this.screenWidth = screenWidth;
    }

    public int getCdHeigth() {
        return cdHeigth;
    }

    public void setCdHeigth(int cdHeigth) {
        this.cdHeigth = cdHeigth;
    }

    public int getCdWidth() {
        return cdWidth;
    }

    public void setCdWidth(int cdWidth) {
        this.cdWidth = cdWidth;
    }

    public String getPgLowCase() {
        return pgLowCase;
    }

    public void setPgLowCase(String pgLowCase) {
        this.pgLowCase = pgLowCase;
    }

    public String getPgUpperCase() {
        return pgUpperCase;
    }

    public void setPgUpperCase(String pgUpperCase) {
        this.pgUpperCase = pgUpperCase;
    }

    public String getPgNums() {
        return pgNums;
    }

    public void setPgNums(String pgNums) {
        this.pgNums = pgNums;
    }

    public int getPgLength() {
        return pgLength;
    }

    public void setPgLength(int pgLength) {
        this.pgLength = pgLength;
    }

    private int pgLength;


}

