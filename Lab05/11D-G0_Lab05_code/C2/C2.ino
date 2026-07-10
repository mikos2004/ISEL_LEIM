#define trig 7
#define echo 8
#define buzz 4

int aux;
unsigned long comp;
unsigned long dist;

void setup(){
  Serial.begin(9600);
  pinMode(trig,OUTPUT);
  pinMode(echo,INPUT);
  pinMode(buzz,OUTPUT);
}

float ValorSonar(){
  digitalWrite(trig,HIGH);
  delayMicroseconds(10);
  digitalWrite(trig,LOW);
  duracao= pulseIn(echo,HIGH);
  dist= duracao/29/2; //dist em cm
  Serial.println(dist);
  return dist;
}

void piezo(){
  if(dist>=5 && dist<=50){
   tone(buzz, map(dist,5,50, 5, 200)/10);
  } else{
   noTone(buzz);
  }
}

void loop(){
 ValorSonar();
 piezo();
}
