package com.example.myapplication

import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
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
            
            // Incrementar contador de rodadas
            rodadasDesdeUltimaVitoria++
            
            // Simular rolagem do caça-níquel com ícones
            val iconesLista = iconesCaçaNiquel.keys.toList()
            val indice1 = Random.nextInt(iconesLista.size)
            val indice2 = Random.nextInt(iconesLista.size)
            val indice3 = Random.nextInt(iconesLista.size)
            
            val icone1 = iconesLista[indice1]
            val icone2 = iconesLista[indice2]
            val icone3 = iconesLista[indice3]
            
            // Atualizar os rolos visualmente
            reel1.text = icone1
            reel2.text = icone2
            reel3.text = icone3
            
            Log.d(TAG, "Resultados: $icone1 - $icone2 - $icone3")
            
            val resultadoTexto = "🎰 Resultado: $icone1 - $icone2 - $icone3"
            
            if (icone1 == icone2 && icone2 == icone3) {
                // Vitória!
                val multiplicador = iconesCaçaNiquel[icone1] ?: 1.0
                val premio = valorAposta * multiplicador
                saldoJogador += premio
                
                // Atualizar informações de vitória
                ultimoValorGanho = premio
                rodadasDesdeUltimaVitoria = 0
                
                resultadoJogo.text = "$resultadoTexto\n\n🎉 JACKPOT! 🎉\n💰 Ganhou: R$ ${String.format("%.2f", premio)}\n⭐ Multiplicador: ${multiplicador}x\n💵 Saldo: R$ ${String.format("%.2f", saldoJogador)}"
                Log.d(TAG, "JACKPOT! Premio: $premio, Multiplicador: $multiplicador, Saldo: $saldoJogador")
            } else {
                // Perdeu
                saldoJogador -= valorAposta
                
                resultadoJogo.text = "$resultadoTexto\n\n😔 Não foi desta vez!\n💸 Perdeu: R$ ${String.format("%.2f", valorAposta)}\n💵 Saldo: R$ ${String.format("%.2f", saldoJogador)}"
                Log.d(TAG, "Perdeu. Aposta: $valorAposta, Saldo: $saldoJogador")
                
                // Verificar se acabou o dinheiro
                if (saldoJogador <= 0) {
                    resultadoJogo.text = "$resultadoTexto\n\n😔 Não foi desta vez!\n💸 Perdeu: R$ ${String.format("%.2f", valorAposta)}\n\n💳 Saldo zerado! Configure um novo saldo para continuar!"
                    Log.d(TAG, "Saldo zerado - oferecendo novo saldo")
                    mostrarTelaEscolhaSaldo()
                }
            }
            
            atualizarStatusJogo()
            atualizarValorApostaTexto()
            atualizarUltimaVitoria()
            
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
    
    override fun onDestroy() {
        super.onDestroy()
        timerAtivo?.cancel()
        Log.d(TAG, "Fragmento destruído - timer cancelado")
    }
}
