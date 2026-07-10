//LAB6 G0 11D
#define Trig 7
#define Echo 8
#define S1 2
#define PIEZO 4
#define G2 98
#define A2 110
#define F3 175
bool check;
float dist;
int cont=0;

void setup() {
  Serial.begin(9600);
  pinMode(Trig,OUTPUT);
  pinMode(Echo,INPUT);
  pinMode(S1, INPUT_PULLUP);
  pinMode(PIEZO,OUTPUT);
}

///////////////////////////////////////////
//////SONAR
void distance(int pinTrig, int pinEcho, int rate){
  //Inicializar os estados
  static const int ESPERA=0;
  static const int ENVIO_PULSO=1;
  static const int ANTES_ECHO=2;
  static const int ECHO_RUN=3;
  static const int MEDE_DISTANCIA=4;

  //inicializar variaveis
  static unsigned long tw=micros();
  static unsigned long t0=millis();
  static unsigned long t1=micros();
  rate = 1000/rate;
  
  static int state=ESPERA;

  switch (state){ 
    case ESPERA:
     if (millis()-t0>=rate)
     {
        t0=millis();
        state=ENVIO_PULSO;
     }
    break;
    //Caso ENVIO_PULSO
    case ENVIO_PULSO:
      tw=micros();
      digitalWrite(pinTrig, HIGH);
      if(micros()-tw>=10){
        digitalWrite(pinTrig, LOW);
        tw=micros();
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
      t1=micros();
      t0=millis();
      state=ESPERA;
    break; 
  }
}

///////////////////////////////////////
/////CSV
void  printCSV(int rate, float val1, float val2, float val3){
  //Inicializar os estados
  static const int WAIT=0;
  static const int PRINT=1;
  
  //Inicializar variaveis
  static unsigned long t0=millis(), t1;

  static int state = WAIT;

  switch (state){
    //Caso WAIT
    case WAIT:
      t1=millis()-t0;
      if(t1>1000/rate){
          state = PRINT;
        }
    break;
	//Caso PRINT
    case PRINT:
      Serial.print(val1);
      Serial.print('\t');
      Serial.print('\t'); //derivado ao millis que ocupa muito espaço
      Serial.print(val2);
      Serial.print('\t');
      Serial.println(val3);
      state = WAIT;
    break;
  }
}
///////////////////////////////////////
/////DET
void toque(int pinBotao)
{
  const int PENDING = 0, SCANNING = 1, PRINTING=2;
  static int state = PENDING;
  static bool b;
  switch (state)
  {
    //Caso PENDING
    case PENDING:
      b = digitalRead(pinBotao);
      if(b == LOW)
      {
        check=false;
        state = SCANNING;
      }
    break;
    //Caso SCANNING
    case SCANNING:
      b = digitalRead(pinBotao);
      if( b == HIGH)
      {
        check=true;
        state = PRINTING;
      }
    break;
    //Caso PRINTING
    case PRINTING:
        Serial.println("Detetado toque no botão");
        state = PENDING;
    break;
  }
}
///////////////////////////////////////
/////CONT
void contador(int pin)
{
  const int CONTAGEM = 0 , ESPERA=1, PRINT=2;

  static bool b;

  static int state = CONTAGEM;
  switch (state)
  {
    //Caso CONTAGEM
    case CONTAGEM:
     b = digitalRead(pin);
     if(b==LOW)
     {
       cont= cont + 1;
       state= ESPERA;
     }
    break;
    //Caso ESPERA
    case ESPERA:
     b = digitalRead(pin);
     if (b==HIGH)
     {
       state=PRINT;
     }
    break;
    //Caso PRINT
    case PRINT:
     state=CONTAGEM;
    break;
  }
}
///////////////////////////////////////
/////PIEZO

void melodia(int duration, int nota1, int nota2, int nota3){
  unsigned long T=duration;
  static const int NOTE_1=0, NOTE_2=1, NOTE_3=2, ESPERA=3;
  static int state=NOTE_1;
  static unsigned long t0=micros();
  switch (state)
  {
    //Caso NOTE_1
    case NOTE_1:
      if (millis()-t0>T && check==true){
        tone(PIEZO, nota1);
        t0 = millis();
        state = NOTE_2; 
      }
    break;
    //Caso NOTE_2
    case NOTE_2:
      if (millis()-t0>T){
        tone(PIEZO, nota3);
        t0 = millis();
        state = NOTE_3;
      }
    break;
    //Caso NOTE_3
    case NOTE_3:
      if (millis()-t0>T){
        tone(PIEZO, nota2);
        t0 = millis();
        state = ESPERA;
      }
    //Caso ESPERA
    case ESPERA:
      if (millis()-t0>T){
        noTone(PIEZO);
        t0 = millis();
        state = NOTE_1;
      }
    break;
 }
}

void loop() {
  distance(Trig, Echo, 10);
  printCSV(10, millis(), cont, dist);
  melodia(100, G2, A2, F3);
  toque(S1);
  contador(S1);
}