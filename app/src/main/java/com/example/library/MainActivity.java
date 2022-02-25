package com.example.library;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

public class MainActivity extends AppCompatActivity {

    ListView bookList;
    Button add,del;
    EditText bookName,bookAuthor, bookYear;
    SharedPreferences.Editor editor;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        preferences=getSharedPreferences("Books", MODE_PRIVATE);
        editor=preferences.edit();

        bookList=findViewById(R.id.book_list);

        add=findViewById(R.id.add);
        del=findViewById(R.id.del);

        bookName =findViewById(R.id.name);
        bookAuthor=findViewById(R.id.author);
        bookYear =findViewById(R.id.year);

        LinkedList<Book> bookLinkedList=new LinkedList<>();
        String str = preferences.getString("BOOKS", "");
        String[] split = str.split("/");

        if(split.length>2){
            for (int i = 0; i < split.length; i+=4) {
                bookLinkedList.add(new Book(split[i],split[i+1], Integer.parseInt(split[i+2]), Integer.parseInt(split[i+3])));
            }
        }

        String[]keyArray={"title","author","year","cover"};
        int [] idArray={R.id.book_title,R.id.author,R.id.year,R.id.image};

        LinkedList<HashMap<String,Object>> listForAdapter=new LinkedList<>();
        for (int i = 0; i < bookLinkedList.size(); i++) {
            HashMap<String,Object>bookMap=new HashMap<>();
            bookMap.put(keyArray[0],bookLinkedList.get(i).title);
            bookMap.put(keyArray[1],bookLinkedList.get(i).author);
            bookMap.put(keyArray[2],bookLinkedList.get(i).year);
            bookMap.put(keyArray[3],bookLinkedList.get(i).coverId);
            listForAdapter.add(bookMap);

        }

        SimpleAdapter simpleAdapter=new SimpleAdapter(this,listForAdapter,R.layout.list_item,keyArray,idArray);

        bookList.setAdapter(simpleAdapter);
        bookList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getApplicationContext(), bookLinkedList.get(i).toString(), Toast.LENGTH_SHORT).show();
                FragmentManager manager = getSupportFragmentManager();
                MyAlertDialog myDialogFragment = new MyAlertDialog();
                Bundle args = new Bundle();
                String a=listForAdapter.get(i).get("title").toString()+listForAdapter.get(i).get("author").toString();
                args.putString("name",a );
                myDialogFragment.setArguments(args);
                FragmentTransaction transaction = manager.beginTransaction();
                myDialogFragment.show(transaction, "dialog");
            }
        });



        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name1= bookName.getText().toString();
                String author1=bookAuthor.getText().toString();
                int year1=0;
                try {
                    year1 = Integer.parseInt(bookYear.getText().toString());
                } catch (NumberFormatException e) {
                    Toast.makeText(getApplicationContext(), "введите год числом", Toast.LENGTH_SHORT).show();

                }
                if (year1!=0) {
                    Book book =  new Book(name1,author1,year1,R.drawable.book);
                    editor.putString("BOOKS", preferences.getString("BOOKS","")+book.toString());
                    bookLinkedList.add(book);
                    HashMap<String, Object> bookMap = new HashMap<>();
                    bookMap.put(keyArray[0], name1);
                    bookMap.put(keyArray[1], author1);
                    bookMap.put(keyArray[2], year1);
                    bookMap.put(keyArray[3], R.drawable.book);
                    listForAdapter.add(bookMap);
                    simpleAdapter.notifyDataSetChanged();
                }
                editor.commit();
            }
        });
        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name1= bookName.getText().toString();
                String author1=bookAuthor.getText().toString();
                for (int i = 0; i < listForAdapter.size(); i++) {
                    if((name1+" "+author1).equals(bookLinkedList.get(i).toString())){
                        listForAdapter.remove(i);
                        break;
                    }
                }
                editor.putString("BOOKS", "");
                Iterator<Book> bookIterator= bookLinkedList.iterator();
                while(bookIterator.hasNext()){
                    editor.putString("BOOKS", preferences.getString("BOOKS","")+bookIterator.next().toString());
                }
                simpleAdapter.notifyDataSetChanged();
                editor.commit();
            }
        });

        simpleAdapter.notifyDataSetChanged();
    }
}