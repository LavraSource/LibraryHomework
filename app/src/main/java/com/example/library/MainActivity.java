package com.example.library;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.HashMap;
import java.util.LinkedList;

public class MainActivity extends AppCompatActivity {
    ListView bookList;

    Button add,del;

    EditText bookName,bookAuthor, bookYear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bookList=findViewById(R.id.book_list);

        add=findViewById(R.id.add);
        del=findViewById(R.id.del);

        bookName =findViewById(R.id.name);
        bookAuthor=findViewById(R.id.author);
        bookYear =findViewById(R.id.year);




        //TODO подготовка данных
        LinkedList<Book> bookLinkedList=new LinkedList<>();
        bookLinkedList.add(new Book("Основание","Азимов",2015,R.drawable.osnovanie));
        bookLinkedList.add(new Book("Преступление и наказание","Достоевский",1972,R.drawable.prestuplenie));
        bookLinkedList.add(new Book("Шинель","Гоголь",1998,R.drawable.shinel));
        bookLinkedList.add(new Book("Гарри Поттер и Философский каамень","Дж Роулинг",2008,R.drawable.book));
        bookLinkedList.add(new Book("Колобок","народ",2021,R.drawable.book));


        //TODO сщздать массив с ключами и идентификаторами
        String[]keyArray={"title","author","year","cover"};
        int [] idArray={R.id.book_title,R.id.author,R.id.year,R.id.image};

        //TODO сщздание списка map для адаптера
        LinkedList<HashMap<String,Object>> listForAdapter=new LinkedList<>();
        for (int i = 0; i < bookLinkedList.size(); i++) {
            HashMap<String,Object>bookMap=new HashMap<>();
            bookMap.put(keyArray[0],bookLinkedList.get(i).title);
            bookMap.put(keyArray[1],bookLinkedList.get(i).author);
            bookMap.put(keyArray[2],bookLinkedList.get(i).year);
            bookMap.put(keyArray[3],bookLinkedList.get(i).coverId);
            listForAdapter.add(bookMap);

        }
        //TODO создпние адаптера

        //ArrayAdapter<Book>adapter=new ArrayAdapter<>(this,R.layout.list_item,bookLinkedList);
        //SimpleAdapter adapter1;
        //SimpleCursorAdapter adapter2;
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
                    bookLinkedList.add(new Book(name1,author1,year1,R.drawable.book));
                    HashMap<String, Object> bookMap = new HashMap<>();
                    bookMap.put(keyArray[0], name1);
                    bookMap.put(keyArray[1], author1);
                    bookMap.put(keyArray[2], year1);
                    bookMap.put(keyArray[3], R.drawable.book);
                    listForAdapter.add(bookMap);
                    simpleAdapter.notifyDataSetChanged();
                }




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
                simpleAdapter.notifyDataSetChanged();

            }
        });

        simpleAdapter.notifyDataSetChanged();
    }
}