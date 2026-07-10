#define PIEZO 4
#define G2 98
#define A2 110
#define F3 175

void setup()
{
    pinMode(PIEZO,OUTPUT);
}
void loop()
{
  melodia(100, G2, A2, F3);
}

void melodia(int duration, int nota1, int nota2, int nota3){
  unsigned long T=duration;
  static const int NOTE_1=0, NOTE_2=1, NOTE_3=2, ESPERA=3;
  static int state=NOTE_1;
  static unsigned long t0=micros();
  switch (state)
  {
    case NOTE_1:
      if (millis()-t0>T){
        tone(PIEZO, nota2);
        t0 = millis();
        state = NOTE_2; 
      }
    break;
    case NOTE_2:
      if (millis()-t0>T){
        tone(PIEZO, nota3);
        t0 = millis();
        state = NOTE_3;
      }
    break;
    case NOTE_3:
      if (millis()-t0>T){
        tone(PIEZO, nota1);
        t0 = millis();
        state = ESPERA;
      }
    case ESPERA:
      if (millis()-t0>T){
        noTone(PIEZO);
        t0 = millis();
        state = NOTE_1;
      }
    break;
 }
}