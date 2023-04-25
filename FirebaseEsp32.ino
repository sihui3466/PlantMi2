#include <WiFi.h>
#include <Firebase_ESP_Client.h>
#include "DHT.h"


//#include <FirebaseJson.h>

//#define API_KEY 
#define API_KEY "AIzaSyBwtfezoQi7zxRu70keOXq8-4-GaDsfOYk"
#define DATABASE_URL"https://plantmi-c4d3f-default-rtdb.asia-southeast1.firebasedatabase.app/"

//Method skeleton
void wifiConnStatus(int tryDelay ,int numberOfTries);
void firebaseInit();
void firebaseReset();

/// WIFI NAME AND PASSWORD, in our case we will use our phone hotspot as our school's network have additional 
/// security layer that prevents uncertified clients to join the network 
const char* ssid     = "AndroidAPb18f";
const char* password = "moose9699";
int _tryDelay = 500;
int _numberOfTries = 20;

// Motor control pins
#define ENA 33
#define IN1 25
#define IN2 26

// temp and humidity pin
#define DHTPIN 13
#define DHTTYPE DHT11
DHT dht(DHTPIN, DHTTYPE);


///FIREBASE CREDENTIALS
//Define Firebase Data object
FirebaseData fbdo;
FirebaseAuth auth;
FirebaseConfig config;
FirebaseJson jsonSend;
//const char* path = "/water_plant";





void setup() 
{
  Serial.begin(115200);
  delay(10);

  // air sensor init
  dht.begin();

  // water level sensor init
  pinMode(34, INPUT);  

  // soil moisture init
  pinMode(36, INPUT);

  // light sensor init
  pinMode(32, INPUT);
  

  // wifi init
  WiFi.begin(ssid , password);
  wifiConnStatus(_tryDelay,_numberOfTries);

  //Firebase init
  firebaseInit();
  Firebase.begin(&config, &auth);
  Firebase.reconnectWiFi(true);

  firebaseReset(); 
 
  Serial.println("System Initialised");

  // pump init
  // Set motor control pins as output
  pinMode(ENA, OUTPUT);
  pinMode(IN1, OUTPUT);
  pinMode(IN2, OUTPUT);

  // send pump status to firebase
    jsonSend.add("type", "pump");
    jsonSend.add("value", false);
    Firebase.RTDB.setJSON(&fbdo, "/water_plant" , &jsonSend);


  


}
int pushNum =0; 
void loop()
{  
  // Listen for watering command from Firebase
  
  if (Firebase.RTDB.getBool(&fbdo, "/water_plant/value")) {
    if (fbdo.dataType() == "boolean") {
        if (fbdo.boolData() == true){
          // Water the plant
          digitalWrite(IN1, HIGH);
          digitalWrite(IN2, LOW);
          digitalWrite(ENA, HIGH);
          delay(5000);
          digitalWrite(IN1, LOW);
          digitalWrite(IN2, LOW);
          digitalWrite(ENA, LOW);

          // Reset watering command
          jsonSend.add("type", "pump");
          jsonSend.add("value", false);
          Firebase.RTDB.setJSON(&fbdo, "/water_plant" , &jsonSend);
          //Firebase.RTDB.updateNode(&fbdo, "/", jsonSend);
        }
    }
  }
    // Read the moisture sensor data
  int moisture = analogRead(36);

    Serial.println("moisture:"+ moisture);
    if (moisture > 3500) {
    // Turn on pump
    digitalWrite(IN1, HIGH);
    digitalWrite(IN2, LOW);
    digitalWrite(ENA, HIGH);
    Serial.println("Motor ON");
  } else {
    // Turn off pump
    digitalWrite(IN1, LOW);
    digitalWrite(IN2, LOW);
    digitalWrite(ENA, LOW);
    Serial.println("Motor OFF");
  }

  // Read the water level sensor data
  int level = analogRead(34);
  
  // Read the light sensor data
  int light = analogRead(32);

    Serial.println("light:"+ light);

  // Read the light sensor data
  int air = analogRead(34);

  if(Firebase.ready())
  {   
    // send soil data to firebase    
    FirebaseJson json;
    String moisture_str;
    moisture_str = String(moisture);
    json.add("type", "soil_moisture");
    json.add("value", moisture_str);
    Firebase.RTDB.setJSON(&fbdo, "sensor_soil", &json);

    // send light data to firebase
    String light_str;
    light_str = String(light);
    json.add("type", "light");
    json.add("value", light_str);
    Firebase.RTDB.setJSON(&fbdo, "sensor_light", &json);

    // send water level data to firebase
    String level_str;
    level_str = String(level);
    json.add("type", "water_level");
    json.add("value", level_str);
    Firebase.RTDB.setJSON(&fbdo, "sensor_level", &json);
    Serial.print(level);
    Serial.println(F("water level"));
    
    
    // Reading temperature or humidity takes about 250 milliseconds!
  // Sensor readings may also be up to 2 seconds 'old' (its a very slow sensor)
  float humid = dht.readHumidity();
  // Read temperature as Celsius (the default)
  float temp = dht.readTemperature();
  
  // Check if any reads failed and exit early (to try again).
  if (isnan(humid) || isnan(temp)) {
    Serial.println(F("Failed to read from DHT sensor!"));
    return;
  }

  // send air data to firebase
  String humid_str;
  String temp_str;
  humid_str = String(humid);
  temp_str = String(temp);
  json.add("type", "air");
  json.add("humidity", humid_str);
  json.add("temperature", temp_str);
  Firebase.RTDB.setJSON(&fbdo, "sensor_air", &json);  

  // Compute heat index in Fahrenheit (the default)
  // float hif = dht.computeHeatIndex(f, h);
  // Compute heat index in Celsius (isFahreheit = false)
  // float hic = dht.computeHeatIndex(t, h, false);

  // Serial.print(F("Humidity: "));
  // Serial.print(h);
  // Serial.print(F("%  Temperature: "));
  // Serial.print(t);
  // Serial.print(F("°C "));
  // Serial.print(f);
  // Serial.print(F("°F  Heat index: "));
  // Serial.print(hic);
  // Serial.print(F("°C "));
  // Serial.print(hif);
  // Serial.println(F("°F"));
    

    // counter for firebase    
    Firebase.RTDB.setInt(&fbdo, "button/btnState", pushNum);
    Serial.println("Wrote to Firebase");
    delay(10);
    pushNum++;
      
  } 

  // Wait for some time before taking another reading
  delay(1000);
}

//==================Firebase init codes ====================
void firebaseInit()
{
  config.api_key = API_KEY;
  config.database_url = DATABASE_URL;
  Firebase.begin(&config,&auth); 

  /* Sign up */
  if (Firebase.signUp(&config, &auth, "", "")){
    Serial.println("ok");
    //signupOK = true;
  }
  else{
    Serial.printf("%s\n", config.signer.signupError.message.c_str());
  }
  /* Assign the callback function for the long running token generation task */
  //config.token_status_callback = tokenStatusCallback; //see addons/TokenHelper.h
}

void firebaseReset()
{
  
  if(Firebase.ready())
  {
    Firebase.RTDB.setJSON(&fbdo, "button", &jsonSend);
    Serial.println("Init Firebase");
  }
}
//================== WIFI CONNECTION CODES =================
void wifiConnStatus(int tryDelay ,int numberOfTries)
{
  while (true) 
  {   
    switch(WiFi.status())
      {
      case WL_NO_SSID_AVAIL:
        Serial.println("[WiFi] SSID not found");
        break;
      case WL_CONNECT_FAILED:
        Serial.print("[WiFi] Failed - WiFi not connected! Reason: ");
        return;
        break;
      case WL_CONNECTION_LOST:
        Serial.println("[WiFi] Connection was lost");
        break;
      case WL_SCAN_COMPLETED:
        Serial.println("[WiFi] Scan is completed");
        break;
      case WL_DISCONNECTED:
        Serial.println("[WiFi] WiFi is disconnected");
        break;
      case WL_CONNECTED:
        Serial.println("[WiFi] WiFi is connected!");
        Serial.print("[WiFi] IP address: ");
        Serial.println(WiFi.localIP());
        return;
        break;
      default:
        Serial.print("[WiFi] WiFi Status: ");
        Serial.println(WiFi.status());
        break;
    }

    delay(tryDelay);
    if(numberOfTries <= 0){
      Serial.println("[WiFi] Failed to connect to WiFi!");
      // Use disconnect function to force stop trying to connect
      WiFi.disconnect();
      return;
    } 
    else 
    {
      numberOfTries--;
    }
  }
}
