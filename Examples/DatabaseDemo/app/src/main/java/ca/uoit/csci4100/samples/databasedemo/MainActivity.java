package ca.uoit.csci4100.samples.databasedemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ListView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ContactHelper dbHelper = new ContactHelper(this);

        // Note: In reality, all of these database calls would/should happen in an AsyncTask

        // clear the database
        dbHelper.deleteAllContacts();

        // insert some test data
        dbHelper.addNewContact("Roberta", "Bondar", "roberta.bondar@nasa.gov", "555-1234");
        dbHelper.addNewContact("Julie", "Payette", "julie.payette@nasa.gov", "555-4321");
        dbHelper.addNewContact("Marc", "Garneau", "marc.garneau@nasa.gov", "555-2345");
        dbHelper.addNewContact("Chris", "Hadfield", "chris.hadfield@nasa.gov", "555-5432");
        dbHelper.addNewContact("Bjarni", "Triggvason", "bjarni.triggvason@nasa.gov", "555-0101");
        dbHelper.addNewContact("Valentina", "Tereshkova", "vtereshkova@roscosmos.ru", "+7(495)631-97-64");

        // test the delete functionality
        Contact toDelete = dbHelper.addNewContact("Era", "Sme", "gonesoon@deleter.com", "123-4567");
        dbHelper.deleteContactById(toDelete.getId());

        // test the update functionality
        Contact contact = dbHelper.addNewContact("Upda",
                                                 "Tme",
                                                 "notsure@email.com",
                                                 "");
        contact.setEmail("actual@email.com");
        contact.setPhone("555-7890");

        if (!dbHelper.updateContact(contact)) {
            Log.e("contactmanager", "Unable to update!");
        }

        // load the test data into a local array list
        List<Contact> allContacts = dbHelper.getAllContacts();
        for (int i = 0; i < allContacts.size(); i++) {
            Contact current = allContacts.get(i);
            Log.i("contactmanager", current.getFirstName() + " " + current.getLastName());
        }

        if (allContacts.size() > 0) {
            //showContact(allContacts.get(0));
            showContacts(allContacts);
        }

    }

    private void showContacts(List<Contact> contacts) {
        ListView contactList = (ListView)findViewById(R.id.lstContacts);
        contactList.setAdapter(new ContactArrayAdapter(this, contacts));
    }

    /* this is left here for reference, but it is not used
       anymore with the ListView display
     */
    /*
    private void showContact(Contact contact) {
        EditText firstNameField = (EditText)findViewById(R.id.txtFirstName);
        EditText lastNameField = (EditText)findViewById(R.id.txtLastName);
        EditText emailField = (EditText)findViewById(R.id.txtEMail);
        EditText phoneField = (EditText)findViewById(R.id.txtPhone);
        firstNameField.setText(contact.getFirstName());
        lastNameField.setText(contact.getLastName());
        emailField.setText(contact.getEmail());
        phoneField.setText(contact.getPhone());
    }
    */
}
