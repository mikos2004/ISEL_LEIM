#define LDR A0
#define PIEZO 5
float brilho;

void setup(){
  pinMode(PIEZO, OUTPUT);
}

float LerLDR()
{
  float V=analogRead(LDR)*5.0/1023;
  float RLDR=10.0*(5.00/V-1);
  float b = pow(RLDR/23.48,-1/0.837);
  return map(b,0.01,6.25,0,100);
}

void piezo(float brilho)
{
  tone(PIEZO,(map(brilho,0,100,0.2,10)*1000));
}

void loop(){
  brilho=LerLDR();
  delay(500);
  piezo(brilho);;
}
