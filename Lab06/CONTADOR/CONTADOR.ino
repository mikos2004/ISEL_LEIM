define S1 2

void setup() {
  Serial.begin(9600);
  pinMode(S1, INPUT_PULLUP);
}

void contador(int pin)
{
  const int CONTAGEM = 0 , ESPERA=1, PRINT=2;
  
  static int cont;
  static bool b;

  static int state = CONTAGEM;
  switch (state)
  {
    case CONTAGEM:
     b = digitalRead(pin);
     if(b==LOW)
     {
       cont= cont + 1;
       state= ESPERA;
     }
    break;
    case ESPERA:
     b = digitalRead(pin);
     if (b==HIGH)
     {
       state=PRINT;
     }
    break;
    case PRINT:
     Serial.println(cont);
     state=CONTAGEM;
    break;
  }
}


void loop() 
{
 contador(S1);
}