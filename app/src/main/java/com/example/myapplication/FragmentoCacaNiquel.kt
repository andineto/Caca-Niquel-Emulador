package com.example.myapplication

import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import kotlin.random.Random

class FragmentoCacaNiquel : Fragment() {
    private val TAG = "FragmentoCacaNiquel"
    
    // Variáveis do caça-níquel
    private var saldoJogador = 0.0
    private var valorAposta = 50.0
    private var timerAtivo: CountDownTimer? = null
    private var saldoConfigurado = false
    private var rodadasDesdeUltimaVitoria = 0
    private var ultimoValorGanho = 0.0
    private var modoDebug = false
    private var totalGasto = 0.0
    private val limiteGastoNotificacao = 500.0 // Limite para notificação de gasto
    private var notificacaoGastoExibida = false
    
    // Ícones do caça-níquel com multiplicadores
    private val iconesCaçaNiquel = mapOf(
        "🍒" to 2.0,   // Cereja - multiplicador baixo
        "🍋" to 3.0,   // Limão - multiplicador médio-baixo
        "🍊" to 4.0,   // Laranja - multiplicador médio
        "🍇" to 5.0,   // Uva - multiplicador médio-alto
        "🍓" to 6.0,   // Morango - multiplicador alto
        "🍑" to 8.0,   // Pêssego - multiplicador muito alto
        "🍎" to 10.0,  // Maçã - multiplicador super alto
        "💎" to 20.0   // Diamante - multiplicador máximo
    )
    
    // Elementos da UI
    private lateinit var layoutEscolhaSaldo: LinearLayout
    private lateinit var botaoSaldo500: Button
    private lateinit var botaoSaldo1000: Button
    private lateinit var botaoSaldo2000: Button
    private lateinit var botaoSaldo5000: Button
    private lateinit var inputDepositoPersonalizado: EditText
    private lateinit var botaoDepositoPersonalizado: Button
    private lateinit var statusJogo: TextView
    private lateinit var ultimaVitoria: TextView
    private lateinit var reel1: TextView
    private lateinit var reel2: TextView
    private lateinit var reel3: TextView
    private lateinit var botaoDiminuirAposta: Button
    private lateinit var valorApostaTexto: TextView
    private lateinit var botaoAumentarAposta: Button
    private lateinit var timerTexto: TextView
    private lateinit var botaoDebug: Button
    private lateinit var botaoJogarManual: Button
    private lateinit var resultadoJogo: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "Criando view do fragmento caça-níquel")
        return inflater.inflate(R.layout.fragment_caca_niquel, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "View criada - inicializando elementos")
        
        try {
            inicializarElementosUI(view)
            configurarControlesAposta()
            configurarBotoesSaldo()
            configurarDepositoPersonalizado()
            configurarBotaoDebug()
            
            // Verificar se precisa configurar saldo inicial
            if (!saldoConfigurado) {
                mostrarTelaEscolhaSaldo()
            }
            
            atualizarStatusJogo()
            atualizarValorApostaTexto()
            atualizarUltimaVitoria()
            
            Log.d(TAG, "Fragmento caça-níquel inicializado com sucesso")
            
        } catch (e: Exception) {
            Log.e(TAG, "Erro ao inicializar fragmento: ${e.message}")
        }
    }
    
    private fun inicializarElementosUI(view: View) {
        Log.d(TAG, "Inicializando elementos da UI")
        layoutEscolhaSaldo = view.findViewById(R.id.layout_escolha_saldo)
        botaoSaldo500 = view.findViewById(R.id.botao_saldo_500)
        botaoSaldo1000 = view.findViewById(R.id.botao_saldo_1000)
        botaoSaldo2000 = view.findViewById(R.id.botao_saldo_2000)
        botaoSaldo5000 = view.findViewById(R.id.botao_saldo_5000)
        inputDepositoPersonalizado = view.findViewById(R.id.input_deposito_personalizado)
        botaoDepositoPersonalizado = view.findViewById(R.id.botao_deposito_personalizado)
        statusJogo = view.findViewById(R.id.status_jogo)
        ultimaVitoria = view.findViewById(R.id.ultima_vitoria)
        reel1 = view.findViewById(R.id.reel_1)
        reel2 = view.findViewById(R.id.reel_2)
        reel3 = view.findViewById(R.id.reel_3)
        botaoDiminuirAposta = view.findViewById(R.id.botao_diminuir_aposta)
        valorApostaTexto = view.findViewById(R.id.valor_aposta_texto)
        botaoAumentarAposta = view.findViewById(R.id.botao_aumentar_aposta)
        timerTexto = view.findViewById(R.id.timer_texto)
        botaoDebug = view.findViewById(R.id.botao_debug)
        botaoJogarManual = view.findViewById(R.id.botao_jogar_manual)
        resultadoJogo = view.findViewById(R.id.resultado_jogo)
        Log.d(TAG, "Elementos da UI inicializados")
    }
    
    private fun mostrarTelaEscolhaSaldo() {
        Log.d(TAG, "Mostrando tela de escolha de saldo")
        layoutEscolhaSaldo.visibility = View.VISIBLE
        resultadoJogo.text = "💰 Escolha seu saldo inicial para começar a jogar!"
        Log.d(TAG, "Tela de escolha de saldo exibida")
    }
    
    private fun configurarBotoesSaldo() {
        Log.d(TAG, "Configurando botões de saldo")
        
        botaoSaldo500.setOnClickListener {
            configurarSaldoInicial(500.0)
        }
        
        botaoSaldo1000.setOnClickListener {
            configurarSaldoInicial(1000.0)
        }
        
        botaoSaldo2000.setOnClickListener {
            configurarSaldoInicial(2000.0)
        }
        
        botaoSaldo5000.setOnClickListener {
            configurarSaldoInicial(5000.0)
        }
        
        Log.d(TAG, "Botões de saldo configurados")
    }
    
    private fun configurarDepositoPersonalizado() {
        Log.d(TAG, "Configurando depósito personalizado")
        
        botaoDepositoPersonalizado.setOnClickListener {
            val valorTexto = inputDepositoPersonalizado.text.toString().trim()
            
            if (valorTexto.isEmpty()) {
                android.widget.Toast.makeText(
                    context,
                    "⚠️ Por favor, digite um valor!",
                    android.widget.Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }
            
            try {
                val valor = valorTexto.toDouble()
                
                if (valor <= 0) {
                    android.widget.Toast.makeText(
                        context,
                        "⚠️ O valor deve ser maior que zero!",
                        android.widget.Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }
                
                if (valor > 100000) {
                    android.widget.Toast.makeText(
                        context,
                        "⚠️ O valor máximo é R$ 100.000,00!",
                        android.widget.Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }
                
                configurarSaldoInicial(valor)
                inputDepositoPersonalizado.text.clear()
                Log.d(TAG, "Depósito personalizado configurado: R$ $valor")
                
            } catch (e: NumberFormatException) {
                android.widget.Toast.makeText(
                    context,
                    "⚠️ Valor inválido! Digite apenas números.",
                    android.widget.Toast.LENGTH_SHORT
                ).show()
                Log.e(TAG, "Erro ao converter valor: ${e.message}")
            }
        }
        
        Log.d(TAG, "Depósito personalizado configurado")
    }
    
    private fun configurarBotaoDebug() {
        Log.d(TAG, "Configurando botão de debug")
        
        botaoDebug.setOnClickListener {
            modoDebug = !modoDebug
            
            if (modoDebug) {
                // Ativar modo debug
                botaoDebug.text = "🔧 DEBUG: Modo Manual"
                botaoDebug.setBackgroundColor(0xFF9C27B0.toInt())
                botaoJogarManual.visibility = View.VISIBLE
                timerAtivo?.cancel()
                timerTexto.text = "🔧 MODO DEBUG ATIVADO\n🎰 Use o botão JOGAR AGORA para testar"
                Log.d(TAG, "Modo debug ativado")
            } else {
                // Desativar modo debug
                botaoDebug.text = "🔧 DEBUG: Modo Automático"
                botaoDebug.setBackgroundColor(0xFF4CAF50.toInt())
                botaoJogarManual.visibility = View.GONE
                iniciarTimerAutomatico()
                Log.d(TAG, "Modo debug desativado")
            }
        }
        
        botaoJogarManual.setOnClickListener {
            Log.d(TAG, "Botão jogar manual clicado")
            executarCaçaNiquel()
        }
        
        Log.d(TAG, "Botão de debug configurado")
    }
    
    private fun configurarSaldoInicial(valor: Double) {
        Log.d(TAG, "Configurando saldo inicial: $valor")
        
        saldoJogador = valor
        saldoConfigurado = true
        totalGasto = 0.0 // Resetar gasto total ao configurar novo saldo
        notificacaoGastoExibida = false
        
        // Esconder tela de escolha
        layoutEscolhaSaldo.visibility = View.GONE
        
        resultadoJogo.text = "🎉 Bem-vindo ao Caça-níquel!\n💰 Saldo inicial: R$ ${String.format("%.2f", saldoJogador)}\n🎰 Configure sua aposta e as rodadas automáticas começarão!"
        
        iniciarTimerAutomatico()
        atualizarStatusJogo()
        atualizarUltimaVitoria()
        
        Log.d(TAG, "Saldo inicial configurado: $saldoJogador")
    }
    
    private fun configurarControlesAposta() {
        Log.d(TAG, "Configurando controles de aposta")
        
        botaoDiminuirAposta.setOnClickListener {
            Log.d(TAG, "Botão diminuir aposta clicado")
            if (valorAposta > 10.0) {
                valorAposta -= 10.0
                atualizarValorApostaTexto()
                atualizarStatusJogo()
                Log.d(TAG, "Aposta diminuída para: $valorAposta")
            }
        }
        
        botaoAumentarAposta.setOnClickListener {
            Log.d(TAG, "Botão aumentar aposta clicado")
            if (valorAposta < saldoJogador && valorAposta < 500.0) {
                valorAposta += 10.0
                atualizarValorApostaTexto()
                atualizarStatusJogo()
                Log.d(TAG, "Aposta aumentada para: $valorAposta")
            }
        }
        
        Log.d(TAG, "Controles de aposta configurados")
    }
    
    
    private fun iniciarTimerAutomatico() {
        if (modoDebug) {
            Log.d(TAG, "Modo debug ativo - timer não iniciado")
            return
        }
        
        Log.d(TAG, "Iniciando timer automático de 10 segundos")
        
        timerAtivo = object : CountDownTimer(10000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val segundos = millisUntilFinished / 1000
                timerTexto.text = "⏰ Próxima rodada automática em: $segundos segundos"
                Log.d(TAG, "Timer: $segundos segundos restantes")
            }

            override fun onFinish() {
                Log.d(TAG, "Timer finalizado - executando rodada automática")
                executarCaçaNiquel()
                iniciarTimerAutomatico() // Reiniciar timer
            }
        }.start()
        
        Log.d(TAG, "Timer automático iniciado")
    }
    
    private fun executarCaçaNiquel() {
        Log.d(TAG, "=== EXECUTANDO CAÇA-NÍQUEL ===")
        
        try {
            // Verificar se tem saldo suficiente
            if (valorAposta > saldoJogador) {
                resultadoJogo.text = "❌ Saldo insuficiente!\n💵 Saldo atual: R$ ${String.format("%.2f", saldoJogador)}\n🎯 Aposta: R$ ${String.format("%.2f", valorAposta)}\n\n💳 Configure um novo saldo para continuar jogando!"
                Log.d(TAG, "Saldo insuficiente para aposta")
                mostrarTelaEscolhaSaldo()
                return
            }
            rodadasDesdeUltimaVitoria++
            
            // aumentar chances de vitória baseado em rodadas sem ganhar
            val iconesLista = iconesCaçaNiquel.keys.toList()
            val icone1: String
            val icone2: String
            var icone3: String
            
            // Probabilidade de vitória aumenta com rodadas sem ganhar
            val chanceVitoria = when {
                rodadasDesdeUltimaVitoria >= 10 -> 0.50
                rodadasDesdeUltimaVitoria >= 7 -> 0.40
                rodadasDesdeUltimaVitoria >= 5 -> 0.30
                rodadasDesdeUltimaVitoria >= 3 -> 0.20
                else -> 0.10 // Chance base de 20%
            }
            val vaiGanhar = Random.nextDouble() < chanceVitoria
            
            if (vaiGanhar) {
                // Garantir vitória
                val iconeVencedor = escolherIconeVencedor()
                icone1 = iconeVencedor
                icone2 = iconeVencedor
                icone3 = iconeVencedor
                Log.d(TAG, "Vitória garantida após $rodadasDesdeUltimaVitoria rodadas (chance: ${chanceVitoria * 100}%)")
            } else {
                // Sistema ponderado: ícones com multiplicadores menores têm mais chance
                icone1 = escolherIconeBalanceado()
                icone2 = escolherIconeBalanceado()
                icone3 = escolherIconeBalanceado()
                
                // Se os dois primeiros forem iguais, aumentar chance do terceiro ser igual (50%)
                if (icone1 == icone2 && Random.nextDouble() < 0.5) {
                    icone3 = icone1
                }
            }
            
            // Atualizar os rolos visualmente
            animarReel(reel1, icone1){

            }

            animarReel(reel2, icone2){

            }

            animarReel(reel3, icone3){
                processarResultado (icone1, icone2, icone3)
            }

            Log.d(TAG, "Resultados: $icone1 - $icone2 - $icone3")

            
        } catch (e: Exception) {
            Log.e(TAG, "Erro no caça-níquel: ${e.message}")
            resultadoJogo.text = "❌ Erro no jogo: ${e.message}"
        }
    }
    
    private fun atualizarValorApostaTexto() {
        valorApostaTexto.text = "R$ ${String.format("%.2f", valorAposta)}"
        Log.d(TAG, "Valor da aposta atualizado: $valorAposta")
    }
    
    private fun atualizarStatusJogo() {
        val statusTexto = "💰 Saldo: R$ ${String.format("%.2f", saldoJogador)}\n🎯 Aposta: R$ ${String.format("%.2f", valorAposta)}"
        statusJogo.text = statusTexto
        Log.d(TAG, "Status do jogo atualizado - Saldo: $saldoJogador")
    }
    
    private fun atualizarUltimaVitoria() {
        val textoUltimaVitoria = if (ultimoValorGanho > 0) {
            "🏆 Última vitória: R$ ${String.format("%.2f", ultimoValorGanho)} há $rodadasDesdeUltimaVitoria rodada(s)"
        } else {
            "🏆 Última vitória: Nenhuma ainda"
        }
        ultimaVitoria.text = textoUltimaVitoria
        Log.d(TAG, "Última vitória atualizada: $textoUltimaVitoria")
    }
    private fun processarResultado(icone1: String, icone2: String, icone3: String){
        val resultadoTexto = "🎰 Resultado: $icone1 - $icone2 - $icone3"

        if (icone1 == icone2 && icone2 == icone3) {
            val multiplicador = iconesCaçaNiquel[icone1] ?: 1.0
            val premio = valorAposta * multiplicador
            saldoJogador += premio
            ultimoValorGanho = premio
            rodadasDesdeUltimaVitoria = 0

            resultadoJogo.text = "$resultadoTexto\n\n🎉 JACKPOT! 🎉\n💰 Ganhou: R$ ${String.format("%.2f", premio)}\n⭐ Multiplicador: ${multiplicador}x\n💵 Saldo: R$ ${String.format("%.2f", saldoJogador)}"
        } else {
            saldoJogador -= valorAposta
            totalGasto += valorAposta
            
            // Verificar e exibir notificação de gasto
            verificarNotificacaoGasto()
            
            resultadoJogo.text = "$resultadoTexto\n\n😔 Não foi desta vez!\n💸 Perdeu: R$ ${String.format("%.2f", valorAposta)}\n💵 Saldo: R$ ${String.format("%.2f", saldoJogador)}"

            if (saldoJogador <= 0) {
                resultadoJogo.text = "$resultadoTexto\n\n😔 Não foi desta vez!\n💸 Perdeu: R$ ${String.format("%.2f", valorAposta)}\n\n💳 Saldo zerado! Configure um novo saldo para continuar!"
                mostrarTelaEscolhaSaldo()
            }
        }

        atualizarStatusJogo()
        atualizarValorApostaTexto()
        atualizarUltimaVitoria()
    }
    
    private fun escolherIconeBalanceado(): String {
        // Sistema ponderado: ícones com multiplicadores menores têm mais chance
        val pesos = mapOf(
            "🍒" to 30.0,  // 30% de chance
            "🍋" to 25.0,  // 25% de chance
            "🍊" to 20.0,  // 20% de chance
            "🍇" to 12.0,  // 12% de chance
            "🍓" to 7.0,   // 7% de chance
            "🍑" to 4.0,   // 4% de chance
            "🍎" to 1.5,   // 1.5% de chance
            "💎" to 0.5    // 0.5% de chance
        )
        
        val total = pesos.values.sum()
        var random = Random.nextDouble() * total
        var acumulado = 0.0
        
        for ((icone, peso) in pesos) {
            acumulado += peso
            if (random <= acumulado) {
                return icone
            }
        }
        
        return pesos.keys.first()
    }
    
    private fun escolherIconeVencedor(): String {
        // Escolher um ícone vencedor, preferindo multiplicadores médios
        val iconesVencedores = listOf("🍒", "🍋", "🍊", "🍇", "🍓")
        return iconesVencedores.random()
    }
    
    private fun verificarNotificacaoGasto() {
        if (totalGasto >= limiteGastoNotificacao && !notificacaoGastoExibida) {
            notificacaoGastoExibida = true
            android.widget.Toast.makeText(
                context,
                "⚠️ Atenção! Você já gastou R$ ${String.format("%.2f", totalGasto)}. Considere fazer uma pausa!",
                android.widget.Toast.LENGTH_LONG
            ).show()
            Log.d(TAG, "Notificação de gasto exibida: R$ $totalGasto")
        }
    }
    private fun animarReel(reel: TextView, resultadoFinal: String, onFinish: () -> Unit){
        val iconesLista = iconesCaçaNiquel.keys.toList()
        val duracao = 1500L
        val intervalo = 100L

        object : CountDownTimer(duracao, intervalo){
            override fun onTick(millisUntilFinished: Long) {
                val iconeAleatorio = iconesLista.random()
                reel.text = iconeAleatorio
            }

            override fun onFinish() {
                reel.text = resultadoFinal
                onFinish()
            }
        }.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        timerAtivo?.cancel()
        Log.d(TAG, "Fragmento destruído - timer cancelado")
    }
}
