package com.gprs.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import android.os.Bundle;
import android.widget.ListView;

import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

public class publichealthcare extends AppCompatActivity {

    ArrayList<String> district,hospital,type,district1,hospital1,type1;
    SearchView searchView;
    ListView listView;
    CustomPubliccareAdapter customPubliccareAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publichealthcare);

        searchView=findViewById(R.id.search);
        listView=findViewById(R.id.list);


        String dis[]={"AMBALA",
                "AMBALA",
                "AMBALA",
                "AMBALA",
                "AMBALA",
                "AMBALA",
                "BHIWANI",
                "BHIWANI",
                "BHIWANI",
                "BHIWANI",
                "BHIWANI",
                "BHIWANI",
                "BHIWANI",
                "CHARKI DADRI",
                "CHARKI DADRI",
                "CHARKI DADRI",
                "FARIDABAD",
                "FARIDABAD",
                "FARIDABAD",
                "FARIDABAD",
                "FARIDABAD",
                "FARIDABAD",
                "FARIDABAD",
                "FARIDABAD",
                "FARIDABAD",
                "FARIDABAD",
                "FARIDABAD",
                "FARIDABAD",
                "FARIDABAD",
                "FARIDABAD",
                "FARIDABAD",
                "FARIDABAD",
                "FARIDABAD",
                "FARIDABAD",
                "FARIDABAD",
                "FARIDABAD",
                "FARIDABAD",
                "FARIDABAD",
                "FATEHABAD",
                "FATEHABAD",
                "FATEHABAD",
                "FATEHABAD",
                "FATEHABAD",
                "FATEHABAD",
                "FATEHABAD",
                "FATEHABAD",
                "GURUGRAM",
                "GURUGRAM",
                "GURUGRAM",
                "GURUGRAM",
                "GURUGRAM",
                "GURUGRAM",
                "GURUGRAM",
                "GURUGRAM",
                "GURUGRAM",
                "GURUGRAM",
                "GURUGRAM",
                "GURUGRAM",
                "GURUGRAM",
                "GURUGRAM",
                "GURUGRAM",
                "GURUGRAM",
                "GURUGRAM",
                "HISAR",
                "HISAR",
                "HISAR",
                "HISAR",
                "HISAR",
                "HISAR",
                "HISAR",
                "HISAR",
                "HISAR",
                "HISAR",
                "HISAR",
                "JHAJJAR",
                "JHAJJAR",
                "JHAJJAR",
                "JHAJJAR",
                "JHAJJAR",
                "JHAJJAR",
                "JHAJJAR",
                "JHAJJAR",
                "JHAJJAR",
                "JHAJJAR",
                "JHAJJAR",
                "JHAJJAR",
                "JIND",
                "JIND",
                "JIND",
                "JIND",
                "JIND",
                "JIND",
                "KAITHAL",
                "KAITHAL",
                "KAITHAL",
                "KAITHAL",
                "KARNAL",
                "KARNAL",
                "KARNAL",
                "KARNAL",
                "KARNAL",
                "KARNAL",
                "KARNAL",
                "KARNAL",
                "KARNAL",
                "KARNAL",
                "KARNAL",
                "KARNAL",
                "KARNAL",
                "KURUKSHET RA",
                "KURUKSHET RA",
                "KURUKSHET RA",
                "KURUKSHET RA",
                "KURUKSHET RA",
                "KURUKSHET RA",
                "MAHENDRA GARH",
                "MAHENDRA GARH",
                "MAHENDRA GARH",
                "MAHENDRA GARH",
                "MAHENDRA GARH",
                "MAHENDRA GARH",
                "MAHENDRA GARH",
                "MAHENDRA GARH",
                "MAHENDRA GARH",
                "MAHENDRA GARH",
                "NUH",
                "PALWAL",
                "PALWAL",
                "PALWAL",
                "PALWAL",
                "PALWAL",
                "PALWAL",
                "PALWAL",
                "PALWAL",
                "PALWAL",
                "PANCHKULA",
                "PANCHKULA",
                "PANCHKULA",
                "PANCHKULA",
                "PANCHKULA",
                "PANIPAT",
                "PANIPAT",
                "PANIPAT",
                "PANIPAT",
                "REWARI",
                "REWARI",
                "REWARI",
                "REWARI",
                "REWARI",
                "REWARI",
                "REWARI",
                "REWARI",
                "REWARI",
                "ROHTAK",
                "ROHTAK",
                "ROHTAK",
                "ROHTAK",
                "ROHTAK",
                "ROHTAK",
                "ROHTAK",
                "ROHTAK",
                "ROHTAK",
                "ROHTAK",
                "ROHTAK",
                "SIRSA",
                "SIRSA",
                "SIRSA",
                "SONIPAT",
                "SONIPAT",
                "SONIPAT",
                "SONIPAT",
                "SONIPAT",
                "SONIPAT",
                "SONIPAT",
                "SONIPAT",
                "YAMUNANA GAR",
                "YAMUNANA GAR",
                "YAMUNANA GAR",
                "YAMUNANA GAR",
                "YAMUNANA GAR",
                "YAMUNANA GAR"};

        String hos[]={"Military Hospital, Ambala Cantt",
                "Healing Touch Hospital Pvt",
                "Civil Hospital, Naraingarh",
                "Railway Hospital, Ambala Cantt",
                "Civil Hospital Ambala Cantt",
                "Civil Hospital Ambala City",
                "Krishan Lal Jalan Hospital",
                "Jangra Multispecilaity Hospital",
                "Spes Hospital",
                "Ch. Bansilal Govt Hospital",
                "Life Line Hospital",
                "Ganpatrai Hospital",
                "ESI Hospital Bhiwani",
                "Civil Hospital",
                "Yadav Hospital",
                "Pradhan Medicare Centre",
                "B.K. Hospital Faridabad",
                "Goyal Hospital",
                "Jivagram",
                "Noble Hospital",
                "QRG Hospital Medicare",
                "Medicheck Ortho Hospital",
                "Pawan Hospital Unit-2",
                "Asian Hospital",
                "SDH Ballabgarh",
                "Shanti Devi Memorial Hospital",
                "Pawan Hospital Unit-1",
                "Fortis Escort Hospital",
                "Metro Hospital Medicare",
                "Supreme Hospital",
                "Santosh Multispeciality Hospital",
                "Sarvodya Sector 19",
                "RK Hospital",
                "QRG Central",
                "Arsh Hospital",
                "Kedar Hospital",
                "National Institute Of Medical Sciences",
                "Medicheck Hospital",
                "Rajasthan Medical Centre (RMC) Hospital Tohana",
                "Civil Hospital, Tohana",
                "Manav Sewa Sangam Hospital, Tohana",
                "Civil Hospital Fatehabad",
                "WadhwaMaternity and surgical Hospital Fatehabad",
                "Fakir chand Jain Charitable Hospital, Tohana",
                "Punjabi Sabha Hospital, Bhattu Road, Fatehabad",
                "Sadbhawdna Hospital",
                "W Pratisha Hospital, Gurugram",
                "Neelkanth Hospital, Gurugram",
                "Aryan Hospital Gurugram",
                "Metro Hospital, Palam vihar,Gurugram",
                "Artemis Hospital, Gurugram",
                "Kalyani Hospital, Gurugram",
                "Colombia Asia Hospital Gurugram",
                "Civil Hospital, Sector 10A, Gurugram",
                "Mayom Hospital ,Gurugram",
                "Fortis Hospital, Gurugram",
                "Max Hospital, Gurugram",
                "Signature Hospital, Gurugram",
                "Gurgaon",
                "Medanta the Medicity, Gurugram",
                "Poly Clinic Sector 31, Gurugram",
                "Paras Hospital, Gurugram",
                "Naryana Hospital , Gurugram",
                "Sukhda Multispeciality Hospital",
                "Civil Hospital",
                "Sarvodya Hospital, Hisar",
                "Sapra Hospital, Hisar",
                "AADHAR Hospital, Hisar",
                "Mahatma Gandhi Hospital, Hisar",
                "OPJindal Institute of Cancer & Research Hospital",
                "Ravindra Hospital, Hisar",
                "Holy Help Hospital",
                "Military Hospital Cantt. Hisar",
                "\"Geetanali Hospital Multispeciality and Critical",
                "Centre\"",
                "PDM Memorial General Hospital, B.garh",
                "Civil Hospital, Jhajjar",
                "Mission Hospital, Bahadurgarh",
                "Oscar Hospital, JJR city",
                "Advanta Hospital, JJR city",
                "\"Medicare Super Speciality Hospiatl & Trauma Centre, JJR",
                "City\"",
                "World Medical College & Hospital, Girawar",
                "Swastik Hospital, B.garh",
                "SDH Beri",
                "RS Gaur Global Multispeciality Hospital, JJR City",
                "SDH Bahadurgarh",
                "JK Hospital Bahadurgarh",
                "Gangaputra Medical College (Ayush)",
                "SDH Safidon",
                "CHC Uchana",
                "SDH Narwana",
                "CHC Julana",
                "Civil Hospital, Jind",
                "Sub Divisional Hospital Guhla",
                "Cygnus Multispecialty Hospital Kaithal",
                "District Hospital Kaithal",
                "Shah Multispecialty Hospital Kaithal",
                "Sanjiv Bansal Cygnus Hospital , Karnal",
                "Kalpana Chawla Government Medical College, Karnal",
                "Amrit Dhara My Hospital",
                "Virk Hospital Karnal",
                "Parveen Hospital",
                "Shri Hospital",
                "Memani Hospital",
                "Civil Hospital Karnal",
                "Shri Ram Chander Memorial Hospital, Karnal",
                "Uttam Hospital",
                "Arpana Hospital, Madhuban Karnal",
                "Amrit Dhara Hospital, Chaura Bazar, Karnal",
                "Dr. Gain Bhushan Nursing Home, Karnal",
                "Saraswati Mission Hospital",
                "Radha Krishan Child Care",
                "Miri-Piri Hospital Shahabad",
                "Aggarwal Nursing Home",
                "Cygnus Hospital",
                "LNJP Hospital",
                "Singhal Hospital Narnaul",
                "Shanti Hospital Narnaul",
                "CHC Sehlang",
                "CHC Nangal Chaudhary",
                "CHC Kanina",
                "CHC Ateli",
                "SDH Mahendergarh",
                "Sanwedna Hospital Narnaul",
                "Vijay Hospital Narnaul",
                "Ayuvedic Medical College",
                "Civil Hospital, Mandikhera",
                "CHC Alawalpur",
                "CHC Dudhola",
                "SDH Hodal",
                "CHC Soundh",
                "Palwal Hospital,New sohna road",
                "Apex Hospital,Delhi Mathura Road, Palwal",
                "Civil Hospital Palwal",
                "Goyal Nursing Home Palwal",
                "Sachin Hospital Palwal",
                "Command Hospital",
                "SDH Kalka",
                "CHC Raipur Rani",
                "Ojas Hospital Pvt",
                "Alchemist Hospital, Panchkula Pvt",
                "SDH Samalkha",
                "Prem hospital Panipat",
                "Civil Hosital Panipat",
                "Cygnus Hospital",
                "Yaduvanshi Hospital",
                "Dr Shivrattan Hospital",
                "Shanti Yadav Hospital",
                "Virat Hospital",
                "Devjyoti Hospital",
                "Matrika Hospital",
                "SDH Kosli",
                "Civil Hospital Rewari",
                "Cygnus Hospital",
                "Advanta Super Speciality Hospital",
                "Holy Heart SuperSpeciality",
                "Siwach Hospital",
                "Mann Hospital",
                "Mansarover Hospital",
                "Positron Hospital",
                "Oxygen Hospital",
                "Noble Heart & Superspeciality Hospital",
                "Life Care Hospital",
                "Sunflag Global Hospital",
                "Oscar Super Speciality Hospital & Trauma Centre",
                "Shah Satnam Dera Hospital (Wing -1, ground floor), Sirsa",
                "Civil Hospital, Sirsa",
                "Sanjeevani Hospital, (Wing-1, 3rd Floor), Sirsa",
                "Nidaan hospital Pvt",
                "CHC Ganaur",
                "Civil Hospital, Sonipat",
                "CHC Badkhalsa",
                "Poly Clinic Sector-7",
                "CHC Kharkhoda",
                "FIIMS Hospital",
                "CHC Gohana",
                "S.P Hospital Jag",
                "JP Hospital Pvt",
                "Civil Hospital Jagadhri",
                "CHC Chhachharuli",
                "Swami Vivekanand Hospital",
                "Mukand Lal District Civil Hospital"};
        String typ[]={"Govt Hospitals",
                "Private Hospitals",
                "Govt Hospitals",
                "Govt Hospitals",
                "Govt Hospitals",
                "Govt Hospitals",
                "Govt Hospitals",
                "Private Hospitals",
                "Private Hospitals",
                "Govt Hospitals",
                "Private Hospitals",
                "Private Hospitals",
                "Govt Hospitals",
                "Govt Hospitals",
                "Private Hospitals",
                "Private Hospitals",
                "Govt Hospitals",
                "Private Hospitals",
                "Private Hospitals",
                "Private Hospitals",
                "Private Hospitals",
                "Private Hospitals",
                "Private Hospitals",
                "Private Hospitals",
                "Govt Hospitals",
                "Private Hospitals",
                "Private Hospitals",
                "Private Hospitals",
                "Private Hospitals",
                "Private Hospitals",
                "Private Hospitals",
                "Private Hospitals",
                "Private Hospitals",
                "Private Hospitals",
                "Private Hospitals",
                "Private Hospitals",
                "Private Hospitals",
                "Private Hospitals",
                "Private Hospitals",
                "Govt Hospitals",
                "Private Hospitals",
                "Govt Hospitals",
                "Private Hospitals",
                "Private Hospitals",
                "Private Hospitals",
                "Private Hospitals",
                "Private Hospitals",
                "Private Hospitals",
                "Private Hospitals",
                "Private Hospitals",
                "Private Hospitals",
                "Private Hospitals",
                "Private Hospitals",
                "Govt Hospitals",
                "Private Hospitals",
                "Private Hospitals",
                "Private Hospitals",
                "Private Hospitals",
                "Central Ministry Hospital",
                "Private Hospitals",
                "Govt Hospitals",
                "Private Hospitals",
                "Private Hospitals",
                "Govt Hospitals",
                "Govt Hospitals",
                "Private Hospitals",
                "Private Hospitals",
                "Private Hospitals",
                "Private Hospitals",
                "Private Hospitals",
                "Private Hospitals",
                "Private Hospitals",
                "Govt Hospitals",
                "Private Hospitals",
                "Private Hospitals",
                "Govt Hospitals",
                "Private Hospitals",
                "Private Hospitals",
                "Private Hospitals",
                "Private Hospitals",
                "Private Medical College",
                "Private Hospitals",
                "Govt Hospitals",
                "Private Hospitals",
                "Govt Hospitals",
                "Private Hospitals",
                "Private Hospitals",
                "Govt Hospitals",
                "Govt Hospitals",
                "Govt Hospitals",
                "Govt Hospitals",
                "Govt Hospitals",
                "Govt Hospitals",
                "Private Hospitals",
                "Govt Hospitals",
                "Private Hospitals",
                "Private Hospitals",
                "Govt Medical College",
                "Private Hospitals",
                "Private Hospitals",
                "Private Hospitals",
                "Private Hospitals",
                "Private Hospitals",
                "Govt Hospitals",
                "Private Hospitals",
                "Private Hospitals",
                "Private Hospitals",
                "Private Hospitals",
                "Private Hospitals",
                "Private Hospitals",
                "Private Hospitals",
                "Private Hospitals",
                "Private Hospitals",
                "Private Hospitals",
                "Govt Hospitals",
                "Private Hospitals",
                "Private Hospitals",
                "Govt Hospitals",
                "Govt Hospitals",
                "Govt Hospitals",
                "Govt Hospitals",
                "Govt Hospitals",
                "Private Hospitals",
                "Private Hospitals",
                "Private Medical College",
                "Govt Hospitals",
                "Govt Hospitals",
                "Govt Hospitals",
                "Govt Hospitals",
                "Govt Hospitals",
                "Private Hospitals",
                "Private Hospitals",
                "Govt Hospitals",
                "Private Hospitals",
                "Private Hospitals",
                "Govt Hospitals",
                "Govt Hospitals",
                "Govt Hospitals",
                "Private Hospitals",
                "Private Hospitals",
                "Govt Hospitals",
                "Private Hospitals",
                "Govt Hospitals",
                "Private Hospitals",
                "Private Hospitals",
                "Private Hospitals",
                "Private Hospitals",
                "Private Hospitals",
                "Private Hospitals",
                "Private Hospitals",
                "Govt Hospitals",
                "Govt Hospitals",
                "Private Hospitals",
                "Private Hospitals",
                "Private Hospitals",
                "Private Hospitals",
                "Private Hospitals",
                "Private Hospitals",
                "Private Hospitals",
                "Private Hospitals",
                "Private Hospitals",
                "Private Hospitals",
                "Private Hospitals",
                "Private Hospitals",
                "Private Hospitals",
                "Govt Hospitals",
                "Private Hospitals",
                "Private Hospitals",
                "Govt Hospitals",
                "Govt Hospitals",
                "Govt Hospitals",
                "Govt Hospitals",
                "Govt Hospitals",
                "Private Hospitals",
                "Govt Hospitals",
                "Private Hospitals",
                "Private Hospitals",
                "Govt Hospitals",
                "Govt Hospitals",
                "Private Hospitals",
                "Govt Hospitals"};
        district=new ArrayList<String>(Arrays.asList(dis));
        hospital=new ArrayList<String>(Arrays.asList(hos));
        type=new ArrayList<String>(Arrays.asList(typ));
        district1=new ArrayList<>(district);
        type1=new ArrayList<>(type);
        hospital1=new ArrayList<>(hospital);

        customPubliccareAdapter =new CustomPubliccareAdapter(this,hospital1,district1,type1);
        listView.setAdapter(customPubliccareAdapter);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                hospital1.clear();
                district1.clear();
                type1.clear();

                int i;
                for(i=0;i<hospital.size();i++){
                    if(i<hospital.size() && i<district.size() && i<type.size())
                    if(hospital.get(i).toLowerCase().contains(query.toLowerCase())){
                        hospital1.add(hospital.get(i));
                        district1.add(district.get(i));
                        type1.add(type.get(i));
                    }
                }
                if(hospital1.size()!=0){
                    customPubliccareAdapter.notifyDataSetChanged();
                    Toast.makeText(publichealthcare.this, hospital1.size() +" results found",Toast.LENGTH_LONG).show();

                }
                else{
                    Toast.makeText(publichealthcare.this, i +"No Match found",Toast.LENGTH_LONG).show();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
            }
        });

    }
}
