package bear.lab.krypton.client;

public class RootApi {

    public PetApi pet;
    public OrderApi order;
    protected String token;
    protected String url;

    public RootApi(String token) {
        this.url =
        this.token = token;
        this.pet = new PetApi();
        this.order = new OrderApi();
    }
}
