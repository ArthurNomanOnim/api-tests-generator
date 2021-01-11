package bear.lab.krypton.client;

import io.restassured.response.Response;
import org.json.JSONObject;

public class PetApi extends FluentApiClient {

    private final String endpoint = "/pet";

    public Response addPet(JSONObject pet) {
        return request().body(pet).post(endpoint);
    }

    public Response updatePet(JSONObject pet) {
        return request().body(pet).post(endpoint);
    }

    public Response findPetsByStatus(String status) {
        return request().queryParam("status", status).get(endpoint + "/findByStatus");
    }

    public Response findPetById(String petId) {
        return request().pathParam("petId", petId).get(endpoint + "/{petId}");
    }

    public Response updatePetWithForm(String petId, String name, String status) {
        return request().pathParam("petId", petId).get(endpoint + "/{petId}");
    }

    public Response deletePet(String petId) {
        return request().pathParam("petId", petId).delete(endpoint + "/{petId}");
    }
}
