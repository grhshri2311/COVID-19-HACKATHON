package com.gprs.haryana;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.Arrays;


public class fragment_publiccaremaptview extends Fragment implements OnMapReadyCallback {


    private UiSettings mUiSettings;
    private GoogleMap mMap;
    MapView mapView;
    ArrayList<String> name, district, type;

    ArrayList<Pair<Double, Double>> arrayString;

    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_publichealthmapview, container, false);

        String[] dis = {"AMBALA",
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
                "YAMUNANA GAR"
        };

        String[] hos = {"Military Hospital, Ambala Cantt",
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
                "Mukand Lal District Civil Hospital"
        };


        String[] typ = {"Govt Hospitals",
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

        district = new ArrayList<String>(Arrays.asList(dis));
        name = new ArrayList<String>(Arrays.asList(hos));
        type = new ArrayList<String>(Arrays.asList(typ));

        arrayString = new ArrayList<>();
        String string = "[{\"first\":30.352933900000004,\"second\":76.8338952},{\"first\":30.401833999999997,\"second\":76.786577},{\"first\":30.483416300000002,\"second\":77.1257421},{\"first\":30.332261400000004,\"second\":76.832358},{\"first\":30.342020599999998,\"second\":76.8468316},{\"first\":30.3787852,\"second\":76.779832},{\"first\":28.7976795,\"second\":76.1438632},{\"first\":28.7954565,\"second\":76.1632872},{\"first\":28.4491437,\"second\":77.0241387},{\"first\":28.7971953,\"second\":76.1309222},{\"first\":29.133537399999994,\"second\":75.7462145},{\"first\":28.7985174,\"second\":76.1334902},{\"first\":28.8082119,\"second\":76.13190469999999},{\"first\":28.6964506,\"second\":76.9237593},{\"first\":28.187502199999997,\"second\":76.6141663},{\"first\":28.595678399999997,\"second\":76.2659419},{\"first\":28.3926292,\"second\":77.2986762},{\"first\":28.3646979,\"second\":77.329593},{\"first\":28.4327639,\"second\":77.3758876},{\"first\":28.3942905,\"second\":77.3295601},{\"first\":28.415444299999997,\"second\":77.319074},{\"first\":28.4387483,\"second\":77.2995528},{\"first\":28.3219738,\"second\":77.2836172},{\"first\":28.4260095,\"second\":77.29998359999999},{\"first\":28.336379299999997,\"second\":77.31654170000002},{\"first\":29.691910699999998,\"second\":77.0002617},{\"first\":28.3219738,\"second\":77.2836172},{\"first\":28.560702,\"second\":77.27259400000001},{\"first\":28.518251499999998,\"second\":77.0387131},{\"first\":28.4909688,\"second\":77.2911821},{\"first\":28.3955558,\"second\":77.28992160000001},{\"first\":28.4222361,\"second\":77.3167904},{\"first\":28.391271,\"second\":77.2927558},{\"first\":28.3935176,\"second\":77.3100765},{\"first\":28.3847439,\"second\":77.367419},{\"first\":28.397334599999997,\"second\":77.2948602},{\"first\":28.5471743,\"second\":77.2507081},{\"first\":28.381017699999997,\"second\":77.30197729999999},{\"first\":29.711278800000002,\"second\":75.9080699},{\"first\":29.7049783,\"second\":75.9068973},{\"first\":29.720188099999998,\"second\":75.9056103},{\"first\":29.5167912,\"second\":75.4484867},{\"first\":29.5168326,\"second\":75.44466899999999},{\"first\":29.7186512,\"second\":75.9058349},{\"first\":29.5033472,\"second\":75.4405724},{\"first\":29.5025804,\"second\":75.46909289999999},{\"first\":28.4185372,\"second\":77.09718149999999},{\"first\":28.4834867,\"second\":77.1076949},{\"first\":28.4695932,\"second\":77.0195641},{\"first\":28.518251499999998,\"second\":77.0387131},{\"first\":28.4594965,\"second\":77.0266383},{\"first\":28.467654999999997,\"second\":77.04266},{\"first\":28.5091211,\"second\":77.0415506},{\"first\":28.4612171,\"second\":77.033839},{\"first\":28.458136699999997,\"second\":77.06357229999999},{\"first\":28.4572129,\"second\":77.0728492},{\"first\":28.461409099999997,\"second\":77.07463059999999},{\"first\":28.4542379,\"second\":76.9731154},{\"first\":28.4594965,\"second\":77.0266383},{\"first\":28.4395016,\"second\":77.04083370000001},{\"first\":28.453856,\"second\":77.04947899999999},{\"first\":28.450558299999997,\"second\":77.0875075},{\"first\":28.497853499999998,\"second\":77.1019482},{\"first\":29.137166599999997,\"second\":75.741061},{\"first\":28.6964506,\"second\":76.9237593},{\"first\":29.141155800000003,\"second\":75.7335602},{\"first\":29.136635199999997,\"second\":75.71491209999999},{\"first\":29.118828699999995,\"second\":75.74557349999999},{\"first\":29.129130900000003,\"second\":75.7416611},{\"first\":29.130391,\"second\":75.7465659},{\"first\":29.133147599999994,\"second\":75.7460787},{\"first\":29.1307033,\"second\":75.74222209999999},{\"first\":29.119329499999996,\"second\":75.8189273},{\"first\":29.1323103,\"second\":75.7432074},{\"first\":29.1503432,\"second\":75.7056901},{\"first\":28.689355599999995,\"second\":76.8907795},{\"first\":28.6077116,\"second\":76.6422997},{\"first\":28.690440099999996,\"second\":76.9342403},{\"first\":28.607022299999997,\"second\":76.6455495},{\"first\":28.602527199999997,\"second\":76.6575766},{\"first\":28.611929000000003,\"second\":76.642876},{\"first\":30.375201099999998,\"second\":76.782122},{\"first\":28.6616176,\"second\":76.6849926},{\"first\":28.6845881,\"second\":76.9210885},{\"first\":28.701332199999996,\"second\":76.5806097},{\"first\":28.6061265,\"second\":76.6452539},{\"first\":28.6913756,\"second\":76.9314064},{\"first\":28.712366700000004,\"second\":76.9367619},{\"first\":29.378290800000002,\"second\":76.3365232},{\"first\":29.406065299999998,\"second\":76.66148530000001},{\"first\":29.4751415,\"second\":76.1899636},{\"first\":29.5959853,\"second\":76.1149662},{\"first\":29.122979500000003,\"second\":76.4013471},{\"first\":29.3118895,\"second\":76.3300797},{\"first\":30.037673299999998,\"second\":76.3095288},{\"first\":29.814544200000004,\"second\":76.4205415},{\"first\":29.791892299999997,\"second\":76.41725939999999},{\"first\":29.8005907,\"second\":76.4028874},{\"first\":29.692095999999996,\"second\":76.9766375},{\"first\":29.697012599999994,\"second\":76.9900411},{\"first\":29.707130299999996,\"second\":77.0032523},{\"first\":29.694030100000003,\"second\":76.9892328},{\"first\":29.6856929,\"second\":76.9904825},{\"first\":28.5979021,\"second\":76.27479989999999},{\"first\":29.682416300000003,\"second\":76.9963221},{\"first\":29.696181700000004,\"second\":76.994175},{\"first\":29.695562399999996,\"second\":76.9897051},{\"first\":29.682478099999997,\"second\":76.99584569999999},{\"first\":29.606366899999998,\"second\":76.9796911},{\"first\":29.6827096,\"second\":76.988385},{\"first\":29.7109366,\"second\":77.013356},{\"first\":29.988329399999998,\"second\":76.5883534},{\"first\":29.9739549,\"second\":76.8425455},{\"first\":30.147516999999997,\"second\":76.8672826},{\"first\":29.9662476,\"second\":76.8413008},{\"first\":29.814544200000004,\"second\":76.4205415},{\"first\":29.965603399999996,\"second\":76.8200695},{\"first\":28.0474739,\"second\":76.10983159999999},{\"first\":28.054062799999997,\"second\":76.1019566},{\"first\":28.410620599999998,\"second\":76.2173124},{\"first\":27.8918984,\"second\":76.1148741},{\"first\":28.3300447,\"second\":76.30518049999999},{\"first\":28.1016294,\"second\":76.2603729},{\"first\":28.2734201,\"second\":76.14013489999999},{\"first\":28.057160399999997,\"second\":76.10528},{\"first\":28.052332800000002,\"second\":76.1117991},{\"first\":29.9611023,\"second\":76.8695898},{\"first\":27.9001526,\"second\":76.993775},{\"first\":28.0431791,\"second\":77.230716},{\"first\":28.2091261,\"second\":77.2733635},{\"first\":27.897551099999998,\"second\":77.3841197},{\"first\":28.1016294,\"second\":76.2603729},{\"first\":28.136640600000003,\"second\":77.32712649999999},{\"first\":28.1528389,\"second\":77.3332317},{\"first\":28.1359961,\"second\":77.3271092},{\"first\":28.146327099999997,\"second\":77.33543759999999},{\"first\":28.1363332,\"second\":77.3214703},{\"first\":30.7123982,\"second\":76.85433929999999},{\"first\":30.833311,\"second\":76.9356735},{\"first\":30.5837352,\"second\":77.02571139999999},{\"first\":30.661016,\"second\":76.88247710000002},{\"first\":30.6745927,\"second\":76.86474129999999},{\"first\":29.2405272,\"second\":77.0117647},{\"first\":29.398643999999997,\"second\":76.966315},{\"first\":29.399843999999998,\"second\":76.9699705},{\"first\":29.814544200000004,\"second\":76.4205415},{\"first\":28.1894422,\"second\":76.61415160000001},{\"first\":28.1904047,\"second\":76.6135635},{\"first\":28.187502199999997,\"second\":76.6141663},{\"first\":28.1982412,\"second\":76.62430169999999},{\"first\":28.192453699999998,\"second\":76.61936229999999},{\"first\":28.1907848,\"second\":76.61150099999999},{\"first\":28.3968505,\"second\":76.4852265},{\"first\":28.2022162,\"second\":76.6197902},{\"first\":29.814544200000004,\"second\":76.4205415},{\"first\":28.8855942,\"second\":76.614167},{\"first\":28.8807505,\"second\":76.626768},{\"first\":28.8849699,\"second\":76.61490309999999},{\"first\":28.898273200000002,\"second\":76.58957749999999},{\"first\":28.8961816,\"second\":76.5992787},{\"first\":28.913129299999994,\"second\":76.6238108},{\"first\":28.897689399999997,\"second\":76.5768173},{\"first\":28.880891199999997,\"second\":76.6277334},{\"first\":29.6875011,\"second\":77.0105781},{\"first\":28.8956822,\"second\":76.6089986},{\"first\":28.607022299999997,\"second\":76.6455495},{\"first\":29.533593099999994,\"second\":75.0177029},{\"first\":29.54,\"second\":75.0147466},{\"first\":29.5341254,\"second\":75.03934149999999},{\"first\":29.017712,\"second\":77.059362},{\"first\":29.135397299999998,\"second\":77.0208415},{\"first\":28.989899299999998,\"second\":77.0366009},{\"first\":28.9123769,\"second\":77.1172338},{\"first\":29.682790500000003,\"second\":77.01016899999999},{\"first\":28.8744896,\"second\":76.9124465},{\"first\":28.498773399999997,\"second\":76.8720675},{\"first\":29.138204599999998,\"second\":76.6927802},{\"first\":29.0587757,\"second\":76.085601},{\"first\":30.126803600000002,\"second\":77.289971},{\"first\":30.170590099999995,\"second\":77.2914737},{\"first\":30.3015942,\"second\":77.3037636},{\"first\":30.129304299999998,\"second\":77.2889696},{\"first\":30.140070099999996,\"second\":77.2999881}]";

        try {
            JSONArray jsonArray = (JSONArray) new JSONTokener(string).nextValue();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                arrayString.add(new Pair<Double, Double>(jsonObject.optDouble("first"), jsonObject.optDouble("second")));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mapView = (MapView) view.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mUiSettings = mMap.getUiSettings();

        // Keep the UI Settings state in sync with the checkboxes.
        mUiSettings.setCompassEnabled(true);
        mUiSettings.setMyLocationButtonEnabled(true);
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
        mUiSettings.setScrollGesturesEnabled(true);
        mUiSettings.setZoomGesturesEnabled(true);
        mUiSettings.setTiltGesturesEnabled(true);
        mUiSettings.setRotateGesturesEnabled(true);

        getall();

    }


    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }


    void getall() {


        mMap.clear();

        LatLng latLng = null;
        for (int i = 0; i < arrayString.size() && i < name.size() && i < district.size() && i < type.size(); i++) {
            latLng = new LatLng(arrayString.get(i).first, arrayString.get(i).second);
            Marker marker = mMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .title(name.get(i))
                    .snippet(type.get(i))
                    .icon(bitmapDescriptorFromVector(getContext(), R.drawable.ic_place_black_24dp)));
            marker.showInfoWindow();
            CustomInfoWindow customInfoWindow = new CustomInfoWindow(getActivity());
            mMap.setInfoWindowAdapter(customInfoWindow);
            marker.showInfoWindow();
            mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick(Marker marker) {
                    view(marker.getTitle());
                }
            });
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
    }


    private void view(String x) {

        for (int i = 0; i < name.size(); i++) {
            if (name.get(i).equals(x)) {
                Bundle bundle = new Bundle();
                bundle.putString("name", name.get(i));
                bundle.putString("type", type.get(i));
                bundle.putString("district", district.get(i));

                BottomSheetDialogFragment f = new Bottomsheetpubliccarefragment();
                f.setArguments(bundle);
                f.show(getActivity().getSupportFragmentManager(), "Dialog");
                break;
            }
        }


    }
}
