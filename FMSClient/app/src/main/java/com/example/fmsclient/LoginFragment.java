package com.example.fmsclient;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import model.Event;
import model.Person;
import request.LoginRequest;
import request.RegRequest;
import results.EveryPersonResult;
import results.LoginResults;
import results.RegisterResults;

public class LoginFragment extends Fragment {

    //create keys
    private static final String SUCCESS = "Success";
    private static final String FIRST_NAME = "First Name";
    private static final String LAST_NAME = "Last Name";
    private EditText host, port, username, password, firstname, lastname, email;
    RadioButton male, female;
    private Button login, register;

    //datacache
    public static DataCache dataCache = DataCache.getInstance();

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        //getting login and reg buttons
        login = view.findViewById(R.id.login);
        register = view.findViewById(R.id.register);

        //getting all the text
        host = view.findViewById(R.id.hostInput);
        port = view.findViewById(R.id.serverPortInput);
        username = view.findViewById(R.id.usernameInput);
        password = view.findViewById(R.id.passwordinput);
        firstname = view.findViewById(R.id.firstNameInput);
        lastname = view.findViewById(R.id.lastNameInput);
        email = view.findViewById(R.id.emailInput);
        male = view.findViewById(R.id.male);
        female = view.findViewById(R.id.female);

        //watching all the text
        host.addTextChangedListener(loginTextWatcher);
        port.addTextChangedListener(loginTextWatcher);
        username.addTextChangedListener(loginTextWatcher);
        password.addTextChangedListener(loginTextWatcher);
        firstname.addTextChangedListener(loginTextWatcher);
        lastname.addTextChangedListener(loginTextWatcher);
        email.addTextChangedListener(loginTextWatcher);

        //login onclick
        login.setOnClickListener(v ->{

            String userName = username.getText().toString();
            String passWord = password.getText().toString();


            @SuppressLint("HandlerLeak") Handler handler = new Handler(){
                @Override
                public void handleMessage(Message message){
                    Bundle bundle = message.getData();
                    if(bundle.getBoolean(SUCCESS)){
                        //Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT).show();
                        Toast.makeText(getActivity(), "Welcome " + bundle.getString(FIRST_NAME) + " " + bundle.getString(LAST_NAME), Toast.LENGTH_SHORT).show();
                        FragmentManager fragmentManager = getParentFragmentManager();
                        Fragment fragment = new MapFragment();
                        fragmentManager.beginTransaction().replace(R.id.mainActivity, fragment).commit();

                    }
                    else{
                        Toast.makeText(getActivity(), "Epic Failure dude", Toast.LENGTH_SHORT).show();
                    }
                }

            };

            LoginRequest loginRequest = new LoginRequest();
            loginRequest.setUsername(userName);
            loginRequest.setPassword(passWord);
            loginTask task = new loginTask(handler, loginRequest);
            ExecutorService executorService = Executors.newSingleThreadExecutor();
            executorService.submit(task);

        });

        register.setOnClickListener(v ->{

            boolean maleCheck, femaleCheck;
            String gender = "";
            maleCheck = male.isChecked();
            femaleCheck = female.isChecked();
            if(maleCheck){
                gender = "m";
            }
            else if(femaleCheck){
                gender = "f";
            }

            String Host, Port, Username, Password, Firstname, Lastname, Email;
            Host = host.getText().toString();
            Port = port.getText().toString();
            Username = username.getText().toString();
            Password = password.getText().toString();
            Firstname = firstname.getText().toString();
            Lastname = lastname.getText().toString();
            Email = email.getText().toString();


            @SuppressLint("HandlerLeak") Handler handler = new Handler(){
                @Override
                public void handleMessage(Message message){
                    Bundle bundle = message.getData();
                    if(bundle.getBoolean(SUCCESS)){
                        Toast.makeText(getActivity(), "Welcome " + bundle.getString(FIRST_NAME) + " " + bundle.getString(LAST_NAME), Toast.LENGTH_SHORT).show();
                        FragmentManager fragmentManager = getParentFragmentManager();
                        Fragment fragment = new MapFragment();
                        fragmentManager.beginTransaction().replace(R.id.mainActivity, fragment).commit();
                    }
                    else{
                        Toast.makeText(getActivity(), "Epic Failure dude", Toast.LENGTH_SHORT).show();
                    }
                }
            };

            RegRequest request = new RegRequest();
            request.setUsername(Username);
            request.setPassword(Password);
            request.setFirstName(Firstname);
            request.setLastName(Lastname);
            request.setEmail(Email);
            request.setGender(gender);
            registerTask task = new registerTask(handler, request);
            ExecutorService executorService = Executors.newSingleThreadExecutor();
            executorService.submit(task);

        });

        return view;
    }

    private TextWatcher loginTextWatcher = new TextWatcher(){

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            String Host, Port, Username, Password, Firstname, Lastname, Email;
            Host = host.getText().toString();
            Port = port.getText().toString();
            Username = username.getText().toString();
            Password = password.getText().toString();
            Firstname = firstname.getText().toString();
            Lastname = lastname.getText().toString();
            Email = email.getText().toString();

            login.setEnabled(!Username.isEmpty() && !Password.isEmpty());

            register.setEnabled(!Host.isEmpty() && !Port.isEmpty() && !Username.isEmpty() && !Password.isEmpty() && !Email.isEmpty() && !Firstname.isEmpty() && !Lastname.isEmpty());

        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    private static class loginTask implements Runnable{

        public LoginRequest request;
        public Handler handler;
        public LoginResults loginResults;

        public loginTask(Handler handler, LoginRequest request){
            this.handler = handler;
            this.request = request;
        }
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void run() {
            ServerProxy proxy = new ServerProxy("10.0.2.2", "8080");

            loginResults = proxy.login(request);

            if(loginResults.isSuccess()){

                dataCache.updateCache(loginResults.getAuthtoken(), proxy);
                dataCache.setBasePerson(loginResults.getPersonID());
                Person basePerson = dataCache.people.get(loginResults.getPersonID());

                Bundle bundle = new Bundle();
                Message message = Message.obtain();
                bundle.putBoolean(SUCCESS, true);
                bundle.putString(FIRST_NAME, basePerson.getFirstName());
                bundle.putString(LAST_NAME, basePerson.getLastName());
                message.setData(bundle);
                handler.sendMessage(message);

            }
            else{
                Bundle bundle = new Bundle();
                Message message = Message.obtain();
                bundle.putBoolean(SUCCESS, false);
                message.setData(bundle);
                handler.sendMessage(message);
            }
        }
    }

    private static class registerTask implements Runnable {

        public RegRequest request;
        public Handler handler;
        public RegisterResults registerResults;

        public registerTask(Handler handler, RegRequest request) {
            this.handler = handler;
            this.request = request;
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void run() {
            ServerProxy proxy = new ServerProxy("10.0.2.2", "8080");

            registerResults = proxy.register(request);

            if (registerResults.isSuccess()) {

                dataCache.updateCache(registerResults.getAuthtoken(), proxy);
                dataCache.setBasePerson(registerResults.getPersonID());
                Person basePerson = dataCache.people.get(registerResults.getPersonID());

                Bundle bundle = new Bundle();
                Message message = Message.obtain();
                bundle.putBoolean(SUCCESS, true);
                bundle.putString(FIRST_NAME, basePerson.getFirstName());
                bundle.putString(LAST_NAME, basePerson.getLastName());
                message.setData(bundle);
                handler.sendMessage(message);
            } else {
                Bundle bundle = new Bundle();
                Message message = Message.obtain();
                bundle.putBoolean(SUCCESS, false);
                message.setData(bundle);
                handler.sendMessage(message);
            }
        }
    }
}