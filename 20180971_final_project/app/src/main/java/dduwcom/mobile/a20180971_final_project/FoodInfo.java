package dduwcom.mobile.a20180971_final_project;

public class FoodInfo {
    private double NUTR_CONT1;
    private String DESC_KOR;

    public FoodInfo() {
    }
    public FoodInfo(double NUTR_CONT1, String DESC_KOR) {
        this.NUTR_CONT1 = NUTR_CONT1;
        this.DESC_KOR = DESC_KOR;
    }

    public double getNUTR_CONT1() {
        return NUTR_CONT1;
    }

    public void setNUTR_CONT1(double NUTR_CONT1) {
        this.NUTR_CONT1 = NUTR_CONT1;
    }

    public String getDESC_KOR() {
        return DESC_KOR;
    }

    public void setDESC_KOR(String DESC_KOR) {
        this.DESC_KOR = DESC_KOR;
    }

    @Override
    public String toString() {
        return "FoodInfo{" +
                " NUTR_CONT1=" + NUTR_CONT1 +
                ", DESC_KOR='" + DESC_KOR + '\'' +
                '}';
    }
}
