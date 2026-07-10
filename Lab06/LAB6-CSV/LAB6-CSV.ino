void setup() {
  Serial.begin(9600);
}

void  printCSV(int rate, float val1, float val2, float val3){
  static const int WAIT=0;
  static const int PRINT=1;

  static unsigned long t0=millis(), t1;

  static int state = WAIT;

  switch (state){
    case WAIT:
      t1=millis()-t0;
      if(t1>1000/rate){
          state = PRINT;
        }
    break;

    case PRINT:
      Serial.print(val1);
      Serial.print('\t');
      Serial.print(val2);
      Serial.print('\t');
      Serial.println(val3);
      state = WAIT;
    break;
  }
}

void loop() {
  printCSV(2, 2.0, 5.6, 3.2);
}