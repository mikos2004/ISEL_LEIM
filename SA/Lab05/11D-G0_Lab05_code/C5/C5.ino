#include <Servo.h>
#define servo 9

Servo nameServo;

void setup() 
{
  Serial.begin(9600);
  nameServo.attach(9);
}

float simulador(unsigned long t)
{
  const float ydc=90, yac=90; 
  const int T=5; // T=5 segundos
  return ydc+yac*sin(6.28*t/1000/T); 
}

void loop(){
  unsigned long t=millis();
  nameServo.write(simulador(t));
  Serial.println(simulador(t));
  delay(15);
}
