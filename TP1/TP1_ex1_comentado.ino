//ISEL- LEIM - Group 06 - Computação Física - TP1 - Ex 1

//Coding UTF-08

//define´s

#define L 5         // pin do led - serve para demonstrar se as luzes estão ligados ou não
#define S1 3        // interruptor 1 - simula a porta direita do carro 
#define S2 4        // interruptor 2 - simula a porta esqueda do carro 
#define pinAviso 6  // pin do piezo - componente que demonstra a partir de um som o estado do aviso
#define M 7         // motor - simula o motor do carro 
#define Lt 8        // luzes - simula as luzes do carro

//variaveis globais

bool Luz;
bool SPD;
bool SPE; 
bool Aviso;
bool Motor;


void setup() {
  pinMode(L, OUTPUT); 
  pinMode(S1,INPUT_PULLUP);
  pinMode(S2, INPUT_PULLUP); 
  pinMode(M, INPUT_PULLUP);  //apenas um jumper do pin digital 7 ao GND tendo a resistência integrada do arduino que impede um curto circuito
  pinMode(Lt, INPUT_PULLUP); //apenas um jumper do pin digital 8 ao GND tendo a resistência integrada do arduino que impede um curto circuito
  pinMode(pinAviso, OUTPUT);
}

void loop() {
  LerEntradas();
  CircuitoCombinatorio();
  EscreverSaidas();
}


// método do tipo void que vai ler todos os pins, utilizando o comando digitalRead(), que no setup são definidos como INPUT_PULLUP

void LerEntradas()
{
  SPD = !digitalRead(S1); // SPD dá true/HIGH/1 quando a porta direita está aberta (S1 pressionado) e dá false/LOW/0 quando a porta está fechada
  SPE = !digitalRead(S2); // SPE dá true/HIGH/1 quando a porta esquerda está aberta (S2 pressionado) e dá false/LOW/0 quando ela está fechada
  Motor = !digitalRead(M); // Motor dá true/HIGH/1 quando o motor está ligado (jumper ligado á breadboard) e dá false/LOW/0 quando o motor está desligado (jumper não está ligado á breadboard)
  Luz= !digitalRead(Lt); // Luz dá true/HIGH/1 quando as luzes estão ligadas (jumper ligado á breadboard) e dá false/LOW/0 quando as luzes estão desligadas (jumper não está ligado á breadboard)
}


// método do tipo void que utiliza os valores obtidos no método LerEntradas() e atualiza uma variável booleana apartir de uma expressão lógica

void CircuitoCombinatorio()
{
  Aviso = (Luz && (SPD||SPE) && !Motor); 
  // expressão lógica simplificada, obtida através da tabela de verdade e mapa de Karnaugh (ver 1.2, 1.3 e 1.4 do relatório)
}


// método do tipo void que atualiza o estado da LED e Piezo consoante o valor lógico das variáveis booleanas (Luz e Aviso)

void EscreverSaidas()
{
  digitalWrite(L, Luz);
  digitalWrite(pinAviso, Aviso);
}