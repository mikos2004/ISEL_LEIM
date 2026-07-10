#define R 9
#define B 10
#define G 11
int Red;
int Blue;
int Green;

void setup() {
  Serial.begin(9600);
  pinMode(R,OUTPUT);
  pinMode(B,OUTPUT);
  pinMode(G,OUTPUT);
  Serial.println("Insira o valor da percentagem de brilho para os pinos R,G e B:");
}

int RecebeconsolaR() {
  while(Serial.available()==0){}
  Red= Serial.parseInt();
  while(Serial.available()==0){}
  Green= Serial.parseInt();
  while(Serial.available()==0){}
  Blue= Serial.parseInt(); 
  return Red, Green, Blue;
}

float OutputLine(int Red,int Green,int Blue) { 
  Serial.print("R"+ String(Red) + "G" + String(Green) + "B" + String(Blue));
}

float ControlaBrilho(int Red,int Green,int Blue) {
  int Red2 = map(Red,0,100,0,255);
  int Green2 = map(Green,0,100,0,255);
  int Blue2 = map(Blue,0,100,0,255);
  analogWrite(R,Red2);
  analogWrite(G,Green2); 
  analogWrite(B,Blue2);
}

void loop() {
 RecebeconsolaR();
 ControlaBrilho(Red,Green,Blue);
 OutputLine(Red,Green,Blue);
 Serial.println(" ");
}