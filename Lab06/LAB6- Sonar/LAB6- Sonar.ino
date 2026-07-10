#define Trig 2
#define Echo 3

void setup() {
  Serial.begin(9600);
  pinMode(Trig,OUTPUT);
  pinMode(Echo,INPUT);
}

void distance(int pinTrig, int pinEcho, int rate){
  static const int ENVIO_PULSO=0;
  static const int ANTES_ECHO=1;
  static const int ECHO_RUN=2;
  static const int MEDE_DISTANCIA=3;
  static unsigned long t0=micros();
  static unsigned long t1=micros();
  float dist;
  
  static int state=ENVIO_PULSO;

switch (state){
    //Caso ENVIO_PULSO
    case ENVIO_PULSO:
      digitalWrite(pinTrig, HIGH);
      if(micros()-t0>=1000000/rate){
        digitalWrite(pinTrig, LOW);
        t0=micros();
        state=ANTES_ECHO;
      }
    break;
    //Caso ANTES_ECHO
    case ANTES_ECHO:
    if(digitalRead(pinEcho) == HIGH){
      t1=micros();
      state=ECHO_RUN;
    }
    break;
    //Caso ECHO_RUN
    case ECHO_RUN:
      if(digitalRead(pinEcho) == LOW){
        state=MEDE_DISTANCIA;
    }
    break; 
    //Caso MEDE_DISTANCIA
    case MEDE_DISTANCIA:
      dist=(micros()-t1)/58.0;
      Serial.println(dist);
      t0=micros();
      t1=micros();
      state=ENVIO_PULSO;
    break; 
  }
}

void loop() {
  distance(Trig, Echo, 10);
}
