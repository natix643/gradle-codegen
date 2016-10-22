package credit.client;

import credit.server.Address;
import credit.server.Debtor;

public class PluggedInClient {

    public void useServerClass() {
        Address address = new Address();
        address.setStreet("Ve Smečkách");
        address.setHouseNumber("20");
        address.setCity("Prague");
        address.setZipCode("11000");
    }
}
