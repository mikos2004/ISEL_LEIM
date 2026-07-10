//include´s
#include <Wire.h>
#include <math.h> 

//define´s
#define enderecoI2C_HS300X 0x44
#define tempodelay 34 // 34 ms é o tempo de aquisição para o sensor obter uma nova temperatura a 14 bits

#define ENDERECO 0x3E
#define RS 0x40
#define CO 0x80

#define LCD_FUNCTIONSET 0x20
#define LCD_2LINHAS 0x08
#define LCD_5X10 0x04

#define LCD_DISPLAYONOFF 0x08
#define LCD_DISPLAYON 0x04
#define LCD_CURSORON 0x02
#define LCD_BLINKON 0x01

#define CO 0x80

#define LCD_DISPALYCLEAR 0x01

#define LCD_ENTRYMODE 0x04
#define LCD_I 0x02

#define enderecoI2C_LPS22B 0x5C
#define enderecoRegistoCTRL_REG2 0x11
#define LPS22HB_PXL 0x28 // XLSB
#define LPS22HB_PL 0x29 // LSB
#define LPS22HB_PH 0x2A // MSB

//define´s dos estados dos autómatos
#define StartC 0
#define EndC 1

#define Idle 0
#define Display 1

#define Idle 0
#define Print 1

#define Function_Set 0
#define ON 1
#define CLEAR 2
#define ENTRY 3

#define SC 0
#define GoOrNo 1
#define ReadP 2

//variáveis
word humidade14bit, temperatura14bit;
float humidadeP, temperaturaGraus;
float altitude, pressaohPa;
byte valor8bits;
byte valor24bits;

byte Cursor;

//// HS300X

/**
	* Este métdodo lê os dados de humidade e temperatura do sensor.
  * Extrai os bits de interesse para a humidade e temperatura
  * @param temperatura saída atualiazada com os dados do sensor
  * @param humidade saída atualiazada com os dados do sensor
	*/
void lerHumidadeTemperaturaBinario14bit(word *temperatura, word *humidade) { 
  if(Wire1.requestFrom(enderecoI2C_HS300X, 4) == 4) {
    word H_high = Wire1.read() & 0xFF;
    word H_low = Wire1.read() & 0xFF;
    word T_high = Wire1.read() & 0xFF;
    word T_low = Wire1.read() & 0xFF;
      
    //14 bits  11 1111 1111 1111-> 0x3FFF 
    *humidade = ((H_high << 8) | H_low) & 0x3FFF;
    *temperatura = (((T_high << 8) | T_low) >> 2) & 0x3FFF;
  }
}

/**
	* Este método converte o valor binário da humidade para um valor em percentagem
  * @param humidade14bits valor binário da humidade a 14 bits
  * @param humidadePercentagem saída com os dados humidade14bits convertidos em percentagem
	*/
void converterHumidade(word humidade14bits, float *humidadePercentagem){
  *humidadePercentagem= (float)humidade14bits /(float) 0x3FFF * 100;
} 

/**
	* Este método converte o valor binário da temperatura para um valor em graus Celsius
  * @param temperatura14bits valor binário da temperatura a 14 bits
  * @param temperaturaGraus saída com os dados temperatura14bits convertidos em graus Celsius
	*/
void converterTemperatura(word temperatura14bits, float *temperaturaGraus){
  *temperaturaGraus= (float)temperatura14bits / (float)0x3FFF * 165.0- 40.0;
} 


/**
	* Este método envia a mensagem start conversion para o sensor HS300X
	*/
void startConversion(){
  Wire1.beginTransmission(enderecoI2C_HS300X); // iniciar mensagem
  Wire1.write(enderecoI2C_HS300X); // ativar sinal de escrita
  Wire1.endTransmission();    // terminar mensagem
}

/**
	* Este método usa a função lerHumidadeTemperaturaBinario14bit() para atualizar os valores da humidade e da temperatura
  * Para depois os converter usando as funções converterTemperatura() e converterHumidade()
  * @param temperatura saída com os dados atualizados da temperatura
  * @param humidade saída com os dados atualizados da humidade
	*/
void pedidoMedicao(float *temperatura, float *humidade)
{
  lerHumidadeTemperaturaBinario14bit(&temperatura14bit, &humidade14bit);
  converterTemperatura(temperatura14bit, temperatura);
  converterHumidade(humidade14bit, humidade);
}

/**
	* Este método é um autómato com o funcionamento do sensor HS300X
  * No estado inicial, StartC, usa-se a função startConversion() e espera-se 34 ms
  * No segundo estado, EndC, usa-se o método pedidoMedicao() 
  * para atualizar os valores da temperatura e humidade
  * @param temperatura saída com os dados atualizados da temperatura
  * @param humidade saída com os dados atualizados da humidade
	*/
void HS300X(float *temperatura, float *humidade)
{
  static int state=StartC;
  static unsigned long t0=millis();
  switch (state) {
    case StartC:
      startConversion();
      if(millis()-t0>=tempodelay)
      {
        t0=millis();
        state=EndC;
      }
    break;
    case EndC:
      pedidoMedicao(temperatura, humidade);
      state=StartC;
    break;
  }
}



void setup() {
  Serial.begin(9600); // inicializa a comunicação serial
  Wire1.begin(); // inicializa a comunicação I2C
  Wire.begin();
  displayI();
  //Display_Iniciar();
  setCursor(0, 0);
  printString("H: ");
  setCursor(0, 8); 
  printString("T: ");
  setCursor(1, 0);
  printString("P: ");
  setCursor(1, 8); 
  printString("A: ");
}


void loop() {

  HS300X(&temperaturaGraus, &humidadeP); 
  LPS22HB(&pressaohPa);
  converterPressao2Metros(pressaohPa, &altitude);

  // exibe os valores de humidade e temperatura na porta serial
  DisplayShow(temperaturaGraus, humidadeP,pressaohPa , altitude);

  Python();
}



//// Python

/**
	* Este método é um autómato para fazer Serial.println() dos valores na consola, de 10 em 10 segundos.
  * Estes valores seguem uma sequência e estão entre letras diferentes para facilitar a filtração dos
  * dados pelo Python
	*/
void Python()
{
  static int state=Idle;
  static unsigned long t=millis();
  int pythonDelay = 10000; //10 segundos
  switch (state) {
    case Idle:
      if(millis()-t>pythonDelay)
      {
        t=millis();
        state=Print;
      }
    break;
    case Print:
      Serial.println(String("T") +temperaturaGraus+String("T")+ String("H")+humidadeP + String("H") + String("P")+pressaohPa +String("P") +String("A") +altitude+ String("A"));
      state=Idle;
    break;
  }
}

//// Display

/**
	* Este método é um autómato para colocar/atualizar valores no Display I2C, de 10 em 10 segundos.
  * @param temperatura 
  * @param humidade 
  * @param pressao 
  * @param altitude
	*/
void DisplayShow(float temperatura, float humidade, float pressao, float altitude)
{
  static int state=Idle;
  static unsigned long t=millis();
  int displayDelay = 10000; //10 segundos
  switch (state) {
    case Idle:
      if(millis()-t>displayDelay)
      {
        t=millis();
        state=Display;
      }
    break;
    case Display:
      setCursor(0, 2);
      printString(String(humidade));
      setCursor(0, 10);
      printString(String(temperatura));
      setCursor(1, 2);
      printString(String(pressao, 1));
      setCursor(1, 10);
      printString(String(altitude));
      state=Idle;
    break;
  }
}

/**
	* Código fornecido pelas folhas do Professor
  * @param oitoBits escreve 1 byte no display
	*/
void escreverDados8(byte oitoBits) 
{
  Wire.beginTransmission(ENDERECO);
  Wire.write(RS);
  Wire.write(oitoBits);
  Wire.endTransmission();
  delayMicroseconds(45); // tempo > 44us para comando fazer efeito
}

/**
  * Este método é um autómato baseado na sequência de instruções do fabricante do display.
	*/
void displayI()
{
  static int state=Function_Set;
  static unsigned long t0=millis();
  switch (state) {
    case Function_Set:
      if(millis()-t0>=15)
      {
        functionSet();
        t0=millis();
        state=ON;
      }
    break;
    case ON:
      if(millis()-t0>=40)
        {
          displayOn();
          t0=millis();
          state=CLEAR;
        }
    break;
    case CLEAR:
      if(millis()-t0>=40)
        {
          displayClear();
          t0=millis();
          state=ENTRY;
        }
    break;
    case ENTRY:
      if(millis()-t0>=1540)
        {
          entryMode();
        }
    break;
  }
}

/**
  * Este método é baseado na sequência de instruções do fabricante do display
  * Versão de inicialização do display com delay()´s
	*/
void Display_Iniciar()
{
  delay(15);
  functionSet();
  delayMicroseconds(40);
  displayOn();
  delayMicroseconds(40);
  displayClear();
  delayMicroseconds(1540);
  entryMode();
}

/**
	* Este método escreve dados no display (CO e oitoBits)
  * @param oitoBits escreve 1 byte no display
	*/
void escreverComando8(byte oitoBits)
{
  Wire.beginTransmission(ENDERECO);
  Wire.write(CO);
  Wire.write(oitoBits);
  Wire.endTransmission();
}

/**
	* Este método usa a função escreverComando8() para ligar o display
	*/
void displayOn()
{
  escreverComando8(LCD_DISPLAYONOFF|LCD_DISPLAYON|LCD_CURSORON|LCD_BLINKON);
}

/**
	* Este método usa a função escreverComando8() para desligar o display
	*/
void displayOff()
{
  escreverComando8(LCD_DISPLAYONOFF);
}

/**
	* Este método usa a função escreverComando8() para atribuir funcionalidades ao display
	*/
void functionSet()
{
  escreverComando8(LCD_FUNCTIONSET|LCD_2LINHAS|LCD_5X10);
}

/**
	* Este método usa a função escreverComando8() para atribuir funcionalidades ao cursor do display
	*/
void entryMode()
{
  escreverComando8(LCD_ENTRYMODE|LCD_I);
}

/**
	* Este método usa a função escreverComando8() para "limpar" o display
	*/
void displayClear()
{
  escreverComando8(LCD_DISPALYCLEAR);
}

/**
	* Este método usa a função escreverComando8() para colocar o cursor numa posição do display
  @param linha byte que representa uma linha do display
  @param coluna byte que representa uma coluna do display
	*/
void setCursor(byte linha, byte coluna)
{
  Cursor = 0x80 | linha<<6 | coluna & 0x3F; //A6 -> linha; A5-0 -> coluna
  escreverComando8(Cursor);
}

/**
	* Este método usa a função escreverDados8() para escrever o char c no display
  @param c char que será escrito no display
	*/
void printchar(char c)
{
  escreverDados8(c);
}

/**
	* Este método usa a função printchar() para escrever o conteudo da String (char a char) no display
  * até encontrar um char = '\0'
  @param s String que será escrita no display
	*/
void printString (String s)
{
  int i= 0;
  while(s[i]!='\0'){
    printchar(s[i]);
    i++;
  }
}

//// LPS22HB

/**
	* Este método é um autómato com o funcionamento do sensor LPS22HB
  * No estado inicial, SC, usa-se a função startConversionP()
  * No segundo estado, GoOrNo, espera-se que o metodo endConversion() retorne true
  * No ultimo estado , ReadP, usa-se o método lerPressao() para atualizar o valor da pressao
  * @param pressaohPa saída com os dados atualizados da pressao
	*/
void LPS22HB(float *pressaohPa)
{
  static int state=SC;
  switch (state) {
    case SC:
      startConversionP();
      state=GoOrNo;
    break;
    case GoOrNo:
    if (endConversion()) 
      {
        state=ReadP;
      }
    break;
    case ReadP:
      lerPressao(pressaohPa);
      state=SC;
    break;
  }
}

/**
	* Este método é a versao anterior ao LPS22HB()
  * Substituida por um autómato devido a utilização do while()
  * @param pressaohPa saída com os dados atualizados da pressao
	*/
void oneShot(float *pressaohPa){
  startConversionP();
  while (!endConversion());
  lerPressao(pressaohPa);
}

/**
	* Este método envia a mensagem start conversion para o sensor LPS22HB
	*/
void startConversionP() {
  byte valor;
  lerRegisto8bit(enderecoI2C_LPS22B, enderecoRegistoCTRL_REG2, &valor); // ler registo CTRL_REG2 de 8 bits via I2C
  escreverRegisto8bits(enderecoI2C_LPS22B, enderecoRegistoCTRL_REG2, valor | 0x01); // afetar com o valor lógico 1 o bit 0 do registo CTRL_REG2 e escrever o novo valor no CTRL_REG2
}

/**
	* Este método indica quando o sensor acaba a aquisição da pressão
  * @return true quando obter o seu valor
  * @return false caso contrário
	*/
bool endConversion() {
  byte registoValor;
  lerRegisto8bit(enderecoI2C_LPS22B,enderecoRegistoCTRL_REG2, &registoValor);// Solicitar a leitura de 1 byte do regisro
  if(registoValor==0x10){
    return true;
  }else {
    return false;
  }
}

/**
	* Código fornecido pelas folhas do Professor
  * @param enderecoI2C
  * @param enderecoRegisto
  * @param valor8bits 
  * @return true caso execute a leitura do byte
  * @return false caso não execute
	*/
bool lerRegisto8bit(byte enderecoI2C, byte enderecoRegisto, byte *valor8bits) {
  Wire1.beginTransmission(enderecoI2C);
  Wire1.write(enderecoRegisto);
  Wire1. endTransmission();
  if (Wire1.requestFrom(enderecoI2C, 1)==1){
    *valor8bits = Wire1.read();
    return true;
  }
  return false;
}

/**
	* Este método permite a leitura a 24 bits do sensor LPS22HB
  * @param enderecoI2C
  * @param enderecoRegisto
  * @param valor24bits 
  * @return true caso execute a leitura
  * @return false caso não execute
	*/
bool lerRegisto24bit(byte enderecoI2C, byte enderecoRegisto, byte *valor24bits) {
  Wire1.beginTransmission(enderecoI2C);
  Wire1.write(enderecoRegisto);
  Wire1. endTransmission();
  
  if (Wire1.requestFrom(enderecoI2C, 3) == 3 ){

    byte MSB = Wire1.read();
    byte LSB = Wire1.read();
    byte XLSB = Wire1.read();
    
    *valor24bits = (MSB<< 16) | (LSB << 8) | XLSB;
    
    return true;
  }
  return false;
}

/**
	* Este método permite escrever 1 byte no sensor LPS22HB
  * @param enderecoI2C
  * @param enderecoRegisto
  * @param valor8bits 
	*/
void escreverRegisto8bits(byte enderecoI2C, byte enderecoRegisto, byte valor8bits) {
  Wire1.beginTransmission(enderecoI2C);
  Wire1.write(enderecoRegisto);
  Wire1.write(valor8bits);
  Wire1.endTransmission();
}  

/**
	* Este método obtém o valor binario da pressão e converte em hPa
  * @param pressaohPa valor float da pressao
	*/
void lerPressao(float *pressaohPa) {
  byte pressaoXL, pressaoL, pressaoH;
  
  if (lerRegisto24bit(enderecoI2C_LPS22B, LPS22HB_PXL, &pressaoXL) &&
   lerRegisto8bit(enderecoI2C_LPS22B, LPS22HB_PL, &pressaoL) && 
   lerRegisto8bit(enderecoI2C_LPS22B, LPS22HB_PH, &pressaoH)) {
    // Combinar os valores lidos para obter a pressão a 24 bits
    *pressaohPa = ((word)pressaoH << 16 | (word)pressaoL << 8 | (word)pressaoXL)/4096.0;
    
  } else {
    *pressaohPa = 0.0;  // Valor padrão de pressão em caso de falha
  }
}

/**
	* Este método utiliza o valor da pressao para calcular o valor da altitude, 
  * comparando a pressao atual com a do nivel das águas do mar
  * @param pressaohPa valor float da pressao
  * @param altitude saída com os dados da altitude em metros
	*/
void converterPressao2Metros(float pressaohPa, float*altitude) {
    float pressaoNivelMarHPa = 1013.25;
    *altitude= 44300.0*(1-pow((pressaohPa/pressaoNivelMarHPa), (1/5.255)));
}