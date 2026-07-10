//LAB04 Grupo 0 - LEIM 11D -SA

#define PINPOT A0
#define PINS1 2
#define PINS2 3
#define PINR3 A1
#define PIND1 4
#define PIND2 5

int LDR, D2, fLDR, fD2, contador, aux;
float brilhoLed, brilho, V2, V5, x, fb, fbL, fV2, fV5, px;
bool V4, V3, fV4, fV3;

void setup()
{
  Serial.begin(9600);
  pinMode(PINS1, INPUT);
  pinMode(PINS2, INPUT_PULLUP);
  pinMode(PIND1, OUTPUT);
  pinMode(PIND2, OUTPUT);
}

float leituraV2(){
  float V2= analogRead(PINPOT)*5.00/1023;
  return V2;
}

float calcx(){
  float x= (fV2/5.00);
  return x;
}

bool leituraV3(){
  bool V3=!digitalRead(PINS1);
  return V3;
}

bool leituraV4(){
  bool V4=!digitalRead(PINS2);
  return V4;
}

float leituraV5(){
  float V5= analogRead(PINR3)*5.00/1023;
  return V5;
}

float leituraLDR(){
  float R4 =10;
  float LDR= (5.00*R4/(leituraV5()+R4));
  float brilho= pow((LDR/23.48), (-1/0.837));
  return brilho;
}

void enviaconsola(){
  Serial.print(millis());
  Serial.print("\t");
  Serial.print(fV2,3);
  Serial.print("\t");
  Serial.print(fb,3);
  Serial.print("\t");
  Serial.print(fV3);
  Serial.print("\t");
  Serial.print(fV4);
  Serial.print("\t");
  Serial.print(fV5, 3);
  Serial.print("\t");
  Serial.print(contador);
  Serial.print("\t");
  Serial.print(aux);
  Serial.print("\t");
  Serial.println(px, 3);
}

//Ciclo de leitura 2xps
void Ciclo2seg(){
  enviaconsola();
  delay(500);
}

//LEDs
void piscaLed1(float f){
  digitalWrite(PIND1,HIGH);
  delay(500/f);
  digitalWrite(PIND1,LOW);
  delay(500/f);
}

float regleituraD2(int brilho){
  float brilhoLed= brilho*255/100;
  analogWrite(PIND2, brilhoLed);
}

//RecebeConsola
int lerserial(){
  if(Serial.available()>0){
    int aux=Serial.parseInt();
    if(aux>=0&&aux<=100){
      return aux;
    }
  }
  return -1;
}

void loop(){
  fV2=leituraV2();
  fb=leituraLDR();
  fV3=leituraV3();
  fV4=leituraV4();
  fV5=leituraV5();
  px=calcx();
  //enviaconsola(); //Já está no Ciclo2seg
  Ciclo2seg();
  piscaLed1(5);
  regleituraD2(100);
  float potLed1(49.5/5*V2+0.5);
  aux =lerserial();
  if (aux>=0){
    regleituraD2(aux);
    delay(100)
  }
  //Contador
  if((leituraV3()==1)||(leituraV4()==1)){
      contador=contador+1;
      delay(1000);
  }

}