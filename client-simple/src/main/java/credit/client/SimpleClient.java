package credit.client;

import credit.server.Debtor;

public class SimpleClient {

    public void useServerClass() {
        Debtor debtor = new Debtor();
        debtor.setFirstName("Jiří");
        debtor.setLastName("Machart");
    }
}
