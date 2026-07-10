//ISEL- LEIM - Group 06 - Computação Física - TP1 - Ex 2

//Coding UTF-08

//define´s

#define L 5        // pin do led - serve para demonstrar se as luzes estão ligados ou não
#define S1 3       // simula a porta direita do carro 
#define S2 4       // interruptor 2 - simula a porta esqueda do carro
#define pinAviso 6 // pin do piezo - componente que demonstra a partir de um som o estado do aviso
#define M 7        // motor - simula o motor do carro
#define Lt 8       // luzes - simula as luzes do carro
#define pinoClk 2  // pin que serve para simular o clock - feito manualmente apartir do interruptor 1

//variaveis globais

bool Luz;
bool SPD;
bool SPE; 
bool Aviso;
bool Motor;
bool clk;
bool S;
bool J0, K0, J1, K1; // Entradas J e K
bool Q0,Q1; // Saída Q

void setup() {
  pinMode(L, OUTPUT);
  pinMode(S1,INPUT_PULLUP); //apenas um jumper do pin digital 3 ao GND tendo a resistência integrada do arduino que impede um curto circuito
  pinMode(S2, INPUT_PULLUP); 
  pinMode(M, INPUT_PULLUP);  //apenas um jumper do pin digital 7 ao GND tendo a resistência integrada do arduino que impede um curto circuito
  pinMode(Lt, INPUT_PULLUP); //apenas um jumper do pin digital 8 ao GND tendo a resistência integrada do arduino que impede um curto circuito
  pinMode(pinAviso, OUTPUT);
  pinMode(pinoClk,INPUT); // utilizou-se o modo INPUT em vez do INPUT_PULLUP devido ao mau funcionamento do clock nesse modo
  attachInterrupt(digitalPinToInterrupt(pinoClk),myclock,RISING); 
  interrupts();
}

void loop() {
  LerEntradas();
  CircuitoCombinatorio();
  CircuitoSequencial();
  EscreverSaidas();
}

// método do tipo void que vai ler todos os pins, utilizando o comando digitalRead(), que no setup são definidos como INPUT_PULLUP

void LerEntradas()
{
  SPD = !digitalRead(S1); // SPD dá true/HIGH/1 quando a porta direita está aberta (jumper ligado á breadboard) e dá false/LOW/0 quando a porta está fechada (jumper não está ligado á breadboard)
  SPE = !digitalRead(S2); // SPE dá true/HIGH/1 quando a porta esquerda está aberta (S2 pressionado) e dá false/LOW/0 quando ela está fechada
  Motor = !digitalRead(M); // Motor dá true/HIGH/1 quando o motor está ligado (jumper ligado á breadboard) e dá false/LOW/0 quando o motor está desligado (jumper não está ligado á breadboard)
  Luz= !digitalRead(Lt); // Luz dá true/HIGH/1 quando as luzes estão ligadas (jumper ligado á breadboard) e dá false/LOW/0 quando as luzes estão desligadas (jumper não está ligado á breadboard)
}

// método do tipo void que utiliza os valores obtidos no método LerEntradas() e atualiza uma variável booleana apartir de uma expressão lógica
// são chamados os seguintes métodos FES() e  FS() para assim serem atualizadas as variéveis dos flip-flops e o Aviso

void CircuitoCombinatorio()
{
  S = (Luz && (SPD||SPE) && !Motor); 
  // expressão lógica simplificada, obtida através da tabela de verdade e mapa de Karnaugh (ver 1.2, 1.3 e 1.4 do relatório)
  FES();
  FS();
}

// método do tipo void que atualiza o estado da LED e Piezo consoante o valor lógico das variáveis booleanas (Luz e Aviso)

void EscreverSaidas()
{
  digitalWrite(L, Luz);
  digitalWrite(pinAviso, Aviso);
}

// método do tipo void que atribui o valor true á variavél clk 

void myclock()
{
  clk=true;
}

// método do tipo void que verifica o estado do clk, e no caso deste ser true, o método atualiza os valores de Q1 e Q0 apartir da função FlipFlopJK() e atribui o valor false a clk

void CircuitoSequencial()
{
  if(clk)
  {
    FlipFlopJK(J1,K1,&Q1); //Atualização do valor de Q1 
    FlipFlopJK(J0,K0,&Q0); //Atualização do valor de Q0
    clk=false; 
    //O  operador de endereço "&" é usado para obter o endereço de memória de uma variável, neste caso de Q1 e Q0
  }
}

// função do tipo void que recebe como parâmetros os valores flip-flops do tipo J-K edge-triggered (J, K, Q) 

void FlipFlopJK(bool J, bool K, bool *Q) // operador unário de indireção "*"" (pointer) é usado para acessar o valor armazenado em um endereço de memória
{
	*Q = (!*Q && J) || (*Q && !K); //atualização do valor de Q usando a expressão lógica do flip-flop
}

// função do tipo void, que utiliza as expressões da função do estado seguinte (ver 2.4 do relatório)

void FES()
{
  J1= S;
  K1= 1;
	J0= S && (!Q1);
	K0= (!S);
}

// função do tipo void, que utiliza as expressões da função de saída (ver 2.4 do relatório)

void FS() 
{
  Aviso = Q1 && Q0 && S;
}