package kayali.developer.android.arabindexingermany;


public class SummaryForCard {


    private String imagePath, companyName,companyAddress, companyServices, companyRatingAverage;
    private int companyRatingTimesInt;


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
        this.companyRatingTimesInt = companyRatingTimes;

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

    public String getCompanyRatingTimes() {
        String companyRatingTimes = String.valueOf(companyRatingTimesInt);
        return companyRatingTimes;
    }

}
