// Salvar primeira linha em branco
String linha = String("");

// Variáveis da ULA.
char A, B, OP, SAIDA;

// Mapeamento das portas de saída
int saida_0 = 13;
int saida_1 = 12;
int saida_2 = 11;
int saida_3 = 10;


//Protótipos
int toInt(char);
void zeroL();
void umL();
void An();
void Bn();
void AouB();
void AeB();
void AxorB();
void AnandB();
void AnorB();
void AxnorB();
void AnouB();
void AouBn();
void AneB();
void AeBn();
void AnouBn();
void AneBn();

// Array de funcoes pra não precisar fazer 500 if-elses
void (*instrucoes[16])() = {
    zeroL, umL, An, Bn,
    AouB, AeB, AxorB, AnandB,
    AnorB, AxnorB, AnouB, AouBn,
    AneB, AeBn, AnouBn, AneBn
    };

void setup()
{
    // Iniciar serial
    Serial.begin(9600);

    // Ajustar os LED'S para OUTPUT
    pinMode(saida_0, OUTPUT);
    pinMode(saida_1, OUTPUT);
    pinMode(saida_2, OUTPUT);
    pinMode(saida_3, OUTPUT);
}

void loop()
{
    // Caso haja dados a serem lidos
    if (Serial.available() > 0)
    {
        //Ler instrucoes
        linha = Serial.readString();

        // Atribuir valores numericos as variaveis
        A = toInt(linha.charAt(0));
        B = toInt(linha.charAt(1));
        OP = toInt(linha.charAt(2));

        if (Serial.read() == '\n')
        {

            // Chamada da função através de um "hashMap" primitivo
            (*instrucoes[OP])();

            // Mostrar LED'S de saida
            digitalWrite(saida_0, SAIDA & 0x8);
            digitalWrite(saida_1, SAIDA & 0x4);
            digitalWrite(saida_2, SAIDA & 0x2);
            digitalWrite(saida_3, SAIDA & 0x1);

        }
    }
}

// Converter caracteres para seus valores numericos
int toInt(char c)
{
    int resp = 0;

    if (c >= 65 && c <= 70)
    {
        resp = c - '7';
    }
    else
    {
        resp = c - '0';
    }

    return resp;
}

// Zero Lógico
void zeroL()
{
    SAIDA = 0x0;
}

// Um Lógico
void umL()
{
    SAIDA = 0xF;
}

// NOT A
void An()
{
    SAIDA = ~A & 0xF;
}

// NOT B
void Bn()
{
    SAIDA = ~B & 0xF;
}

// A OR B
void AouB()
{
    SAIDA = A | B;
}

// A AND B
void AeB()
{
    SAIDA = A & B;
}

// A ⊕ B
void AxorB()
{
    SAIDA = A ^ B;
}

// (A . B)'
void AnandB()
{
    SAIDA = ~(A & B);
}

// (A + B)'
void AnorB()
{
    SAIDA = ~(A | B);
}

// (A ⊕ B)'
void AxnorB()
{
    SAIDA = ~(A ^ B);
}

// A' + B
void AnouB()
{
    SAIDA = (~A) | B;
}

// A + B'
void AouBn()
{
    SAIDA = A | (~B);
}

// A' . B
void AneB()
{
    SAIDA = (~A) & B;
}

// A . B'
void AeBn()
{
    SAIDA = A & (~B);
}

// A' + B'
void AnouBn()
{
    SAIDA = (~A) | (~B);
}

// A' . B'
void AneBn()
{
    SAIDA = (~A) & (~B);
}