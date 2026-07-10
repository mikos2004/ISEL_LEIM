//C1
#define PINPOT A0
#include <Servo.h>

Servo myservo; 

int val;

void setup() {
  myservo.attach(9);
}

void loop() {
  val = analogRead(pinpot); 
  val = map(val, 0, 1023, -90, 90); //map(val, 0, 1023, 0, 180)
  myservo.write(val); 
  delay(15); 
}
