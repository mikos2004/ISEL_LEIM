import serial.tools.list_ports
import tkinter as tk
from tkinter import ttk
import serial
import time
import schedule


def criar_janela():
    janela = tk.Tk()

    altitude_label = ttk.Label(janela, text="Altitude: 0 m")
    altitude_label.pack(pady=10)

    humidade_label = ttk.Label(janela, text="Humidade: 0%")
    humidade_label.pack(pady=10)

    pressao_label = ttk.Label(janela, text="Pressão Atmosférica: 0 hPa")
    pressao_label.pack(pady=10)

    temperatura_label = ttk.Label(janela, text="Temperatura: 0 ºC")
    temperatura_label.pack(pady=10)

    #progress_bar = ttk.Progressbar(janela, orient="vertical", length=200, mode="determinate")
    #progress_bar.pack(pady=20)


    janela.grid()
    s = ttk.Style()
    s.theme_use('clam')
    s.configure("red.Horizontal.TProgressbar", foreground='red', background='red')
    progress_bar = ttk.Progressbar(janela, style="red.Horizontal.TProgressbar", orient="vertical",length=200, mode="determinate")
    progress_bar.pack(pady=20)
    # escala_termometro = ttk.Scale(janela, orient="vertical", length=200, from_=0, to=100)
    # escala_termometro.pack(side=tk.LEFT, padx=20)

    def atualizar_dados():
        # Simulação de dados atualizados
        packet = serialInst.readline()
        # print(packet.decode('utf').rstrip('\n'))
        dados = packet.decode('utf').rstrip('\n')
        print(dados)
        temperatura = getTemperatura(dados)
        print(temperatura)
        humidade = getHumidade(dados)
        print(humidade)
        pressao = getPressao(dados)
        print(pressao)
        altitude = getAltitude(dados)
        print(altitude)
        # altitude = str.(serialreadline())
        # print(texto[2:-5])

        # Atualiza os rótulos com os dados
        altitude_label.config(text=f"Altitude: {float(altitude)} m")
        humidade_label.config(text=f"Humidade: {float(humidade)}%")
        pressao_label.config(text=f"Pressão Atmosférica: {float(pressao)} hPa")
        temperatura_label.config(text=f"Temperatura: {float(temperatura)} ºC")

        # Atualiza a barra de progresso
        progress_bar["value"] = float(temperatura)

        # Atualiza a escala do termômetro
        # escala_termometro.set(temperatura)

        # Agenda a próxima atualização após 1 segundo
        janela.after(1000, atualizar_dados)

    # Inicia a atualização dos dados
    atualizar_dados()

    def fechar_janela():
        janela.destroy()

    janela.protocol("WM_DELETE_WINDOW", fechar_janela)

    janela.mainloop()



def getTemperatura(string):
    T = ''
    for i in range(len(string)):
        if string[i] == 'T':
            while string[i + 1] != 'T':
                T = T + string[i + 1]
                i += 1
            return T

def getHumidade(string):
    H = ""
    for i in range(len(string)):
        if string[i] == 'H':
            while string[i + 1] != 'H':
                H = H + string[i + 1]
                i += 1
            return H

def getPressao(string):
    P = ''
    for i in range(len(string)):
        if string[i] == 'P':
            while string[i + 1] != 'P':
                P = P + string[i + 1]
                i += 1
            return P

def getAltitude(string):
    A = ''
    for i in range(len(string)):
        if string[i] == 'A':
            while string[i + 1] != 'A':
                A = A + string[i + 1]
                i += 1
            return A


### COMUNIAÇÃO SERIAL

### ATENÇÃO ###
# COLOCAR O PRINT NUMA SÓ LINHA

# Lista de portas serial encontradas
ports = serial.tools.list_ports.comports()

# Cria uma instância da classe Serial
serialInst = serial.Serial()

# Lista para guardar as portas encontradas
portList = []

# Percorre todas as portas e coloca-as na Lista.
for porta in ports:
    portList.append(str(porta))
    print(str(porta))

val = input("Escolha a porta: COM")

for x in range(0, len(portList)):
    if portList[x].startswith("COM" + str(val)):
        portVar = "COM" + str(val)
        print(portVar)
        print(portList[x])

# Define o baudrate do canal Serial para 9600 bps (bits por segundo)
serialInst.baudrate = 9600

# Atribui a porta selecionada à instância serial
serialInst.port = portVar

# Abre a conexão serial
serialInst.open()

criar_janela()

while True:
    if serialInst.in_waiting:
        packet = serialInst.readline()
        criar_janela().atualizar_dados()
        schedule.run.pending()
        time.sleep(1)

