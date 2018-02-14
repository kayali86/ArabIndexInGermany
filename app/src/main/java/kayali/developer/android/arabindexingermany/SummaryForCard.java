package kayali.developer.android.arabindexingermany;


public class SummaryForCard {


    private String imagePath, companyName,companyAddress, companyServices, companyRatingAverage;
    private int companyRatingTimes;


    // Constructor
    public SummaryForCard(){
    }

    // Constructor with Parameters
    public SummaryForCard(String imagePath, String companyName, String address, String companyServices, String companyRatingAverage, int companyRatingTimes){
        this.imagePath = imagePath;
        this.companyName = companyName;
        this.companyAddress = address;
        this.companyServices = companyServices;
        this.companyRatingAverage = companyRatingAverage;
        this.companyRatingTimes = companyRatingTimes;

    }


    // Getters
    public String getImagePath(){
        return imagePath;
    }

    public String getCompanyName(){
        return companyName;
    }

    public String getCompanyServices(){
        return companyServices;
    }

    public String getCompanyAddress(){
        return companyAddress;
    }

    public String getCompanyRatingAverage() {
        return companyRatingAverage;
    }

    public int getCompanyRatingTimes() {
        return companyRatingTimes;
    }

    // Setters
/*
    public void setImagePath (String imagePath){
        this.imagePath = imagePath;
    }

    public void setCompanyName (String companyName){
        this.companyName = companyName;
    }

    public void setCompanyServices (String companyServices){
        this.companyServices = companyServices;
    }

    public void setAddress (String address){
        this.companyAddress = address;
    }


    public void setCompanyRatingAverage(String companyRatingAverage) {
        this.companyRatingAverage = companyRatingAverage;


    public void setCompanyRatingTimes(int companyRatingTimes) {
        this.companyRatingTimes = companyRatingTimes;
    }

    }
*/
}
