package com.contactliste.kamfasage.contactliste;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Contact_MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact__main);

        recupContact(getContentResolver());

        final ListView list = (ListView)findViewById(android.R.id.list);
        //final List<String> contacts = recupContact(this.getContentResolver());
        final List<Map<String, Object>> contacts = recupContact(this.getContentResolver());


        if (contacts != null)
        {

            //list.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, contacts));

            final SimpleAdapter adapter = new SimpleAdapter(this, contacts, R.layout.contactlayout, new String[] { "name", "photo", "numero" }, new int[] { R.id.name,
                    R.id.photo, R.id.numero });
            adapter.setViewBinder(new SimpleAdapter.ViewBinder()
            {

                @Override
                public boolean setViewValue(View view, Object data, String textRepresentation)
                {
                    if ((view instanceof ImageView) & (data instanceof Bitmap))
                    {
                        final ImageView image = (ImageView) view;
                        final Bitmap photo = (Bitmap) data;
                        image.setImageBitmap(photo);
                        return true;
                    }
                    return false;
                }
            });

            list.setAdapter(adapter);

        }
    }


    private List<Map<String, Object>> recupContact(ContentResolver contentResolver ){



        //requete avec la methode query avec en parametre URI permettant de récupérer le contact
        final Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, new String[]{ContactsContract.Data._ID, ContactsContract.Data.DISPLAY_NAME, ContactsContract.Contacts.HAS_PHONE_NUMBER}, null, null, null);

        final Cursor cursor2 = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null, null, null, null);


        //Liste à retourner
        final List<Map <String, Object>> contacts = new ArrayList<Map<String, Object>>();
        //final Set<String> contacts = new HashSet<String>();

            if ((cursor == null)&&(cursor == null)){
                Log.e("recupContact","impossible de recuperer le contact");
                return null;
            }
            if((cursor.moveToFirst() == true)&&(cursor2.moveToFirst() == true)){
                final String phoneNum = cursor2.getString(cursor2.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                do{
                    //pour recuperer le nom de contact
                   final String nomContact = cursor.getString(cursor.getColumnIndex(ContactsContract.Data.DISPLAY_NAME));
                  // final long idContact = cursor.getLong(cursor.getColumnIndex(ContactsContract.Data._ID));
                    final long idContact = Long.parseLong(cursor.getString(cursor.getColumnIndex(ContactsContract.Data._ID)));
                    final int hasPhoneNum = cursor.getInt(cursor.getColumnIndex(ContactsContract.Data.HAS_PHONE_NUMBER));






                    if( hasPhoneNum > 0){
                        final Bitmap photo = recupPhoto(contentResolver, idContact);

                        final Map<String, Object> contact = new HashMap<String, Object>();
                        contact.put("name", nomContact);
                        contact.put("photo", photo);
                        contact.put("numero", phoneNum);


                        contacts.add(contact);
                        Log.d("recupContact","contact id =" + "" + idContact + "" + "et nom de contact ="+""+nomContact+"numero"+phoneNum);

                    }



                }while ((cursor.moveToNext()== true)&&(cursor2.moveToNext()== true));
            }
            if((cursor.isClosed()==false)&&(cursor.isClosed()==false)){
                cursor.close();
<<<<<<< HEAD



                Log.e("recupContact","impossible de recuperer le contact");
=======
                cursor2.close();
            }
        //final List<String> sortieContact = new ArrayList<String>(contacts);
       // Collections.sort(sortieContact);
        return contacts;
    }



    // methode permettant de recuperer les photos

    private Bitmap recupPhoto ( ContentResolver contentResolver, long idContact){

        Bitmap photo = null;
        //recuperation des info relatives  a un contact spécifique
        final Uri contactUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, idContact);

        //nous spécifions que nous voulons avoir acces au photo
        final  Uri photUri = Uri.withAppendedPath(contactUri,ContactsContract.Contacts.Photo.CONTENT_DIRECTORY);

        final Cursor cursor = contentResolver.query(photUri, new String[]{ContactsContract.Contacts.Photo.DATA15},null, null, null);

        // permet de vérifier que le curseur n'est pas null
        if (cursor == null)
        {
            Log.i("recupPhoto", "impossible de recuperer la photo du contact ayant ID '" + idContact + "'");
            return null;
        }
        if (cursor.moveToFirst() == true)
        {
            final byte[] data = cursor.getBlob(0);

            if (data != null)
            {
                 photo = BitmapFactory.decodeStream(new ByteArrayInputStream(data));
>>>>>>> master
            }
        }
        if (cursor.isClosed() == false)
        {
            cursor.close();
        }
    return photo;
    }

}
