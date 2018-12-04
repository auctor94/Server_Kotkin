package Charts;

public abstract class Chart implements ChartOperations{
    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
