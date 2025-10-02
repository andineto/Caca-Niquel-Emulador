package com.example.myapplication

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ScrollView
import android.widget.TextView
import androidx.fragment.app.Fragment
import kotlin.random.Random

class FragmentoChat : Fragment() {
    private val TAG = "FragmentoChat"
    
    // Elementos da UI
    private lateinit var areaChat: TextView
    private lateinit var scrollChat: ScrollView
    private lateinit var campoMensagem: EditText
    private lateinit var botaoEnviar: Button
    private lateinit var botaoLimparChat: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "Criando view do fragmento chat")
        return inflater.inflate(R.layout.fragment_chat, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "View criada - inicializando elementos")
        
        try {
            inicializarElementosUI(view)
            configurarChatSimulado()
            
            Log.d(TAG, "Fragmento chat inicializado com sucesso")
            
        } catch (e: Exception) {
            Log.e(TAG, "Erro ao inicializar fragmento chat: ${e.message}")
        }
    }
    
    private fun inicializarElementosUI(view: View) {
        Log.d(TAG, "Inicializando elementos da UI")
        areaChat = view.findViewById(R.id.area_chat)
        scrollChat = view.findViewById(R.id.scroll_chat)
        campoMensagem = view.findViewById(R.id.campo_mensagem)
        botaoEnviar = view.findViewById(R.id.botao_enviar)
        botaoLimparChat = view.findViewById(R.id.botao_limpar_chat)
        Log.d(TAG, "Elementos da UI inicializados")
    }
    
    private fun configurarChatSimulado() {
        Log.d(TAG, "Configurando chat simulado")
        
        botaoEnviar.setOnClickListener {
            Log.d(TAG, "Botão enviar chat clicado")
            enviarMensagemChat()
        }
        
        botaoLimparChat.setOnClickListener {
            Log.d(TAG, "Botão limpar chat clicado")
            limparChat()
        }
        
        // Permitir enviar com Enter
        campoMensagem.setOnEditorActionListener { _, _, _ ->
            enviarMensagemChat()
            true
        }
        
        Log.d(TAG, "Chat simulado configurado")
    }
    
    private fun enviarMensagemChat() {
        val mensagem = campoMensagem.text.toString().trim()
        
        if (mensagem.isNotEmpty()) {
            Log.d(TAG, "Enviando mensagem: $mensagem")
            
            // Adicionar mensagem do usuário
            adicionarMensagemChat("👤 Você: $mensagem")
            
            // Limpar campo
            campoMensagem.text.clear()
            
            // Gerar resposta automática
            gerarRespostaAutomatica(mensagem)
            
            // Scroll para baixo
            scrollParaBaixo()
        }
    }
    
    private fun gerarRespostaAutomatica(mensagem: String) {
        Log.d(TAG, "Gerando resposta automática para: $mensagem")
        
        val respostas = listOf(
            "🎰 Que sorte! Ganhei muito no caça-níquel hoje!",
            "💰 Apostei R$ 100 e ganhei R$ 1000! Incrível!",
            "😔 Perdi tudo hoje... mas amanhã é um novo dia!",
            "🎲 Alguém mais está jogando? Vamos competir!",
            "🏆 Acabei de fazer uma sequência de 5 vitórias!",
            "💸 Cuidado com as apostas altas, pessoal!",
            "🎯 Dica: comece com apostas pequenas!",
            "🎊 Que dia de sorte! Todos estão ganhando!",
            "🤔 Será que o caça-níquel está com sorte hoje?",
            "💡 Alguém tem alguma estratégia para ganhar?",
            "🔥 Acabei de ganhar 777! Que emoção!",
            "💎 Apostei tudo e ganhei! Não façam isso em casa!",
            "🎪 Este jogo é viciante demais!",
            "🌟 Hoje é meu dia de sorte!",
            "🎭 Alguém mais está viciado nisso?",
            "💫 Acabei de perder tudo... mas vou tentar de novo!",
            "🎨 Que cores bonitas nos números!",
            "🎵 O som da vitória é incrível!",
            "🎪 Parece que estou em um cassino real!",
            "🎯 Minha estratégia está funcionando!"
        )
        
        val resposta = respostas[Random.nextInt(respostas.size)]
        adicionarMensagemChat("🤖 Bot: $resposta")
        
        Log.d(TAG, "Resposta gerada: $resposta")
    }
    
    private fun adicionarMensagemChat(mensagem: String) {
        val textoAtual = areaChat.text.toString()
        val novoTexto = "$textoAtual\n\n$mensagem"
        areaChat.text = novoTexto
        Log.d(TAG, "Mensagem adicionada ao chat: $mensagem")
    }
    
    private fun limparChat() {
        Log.d(TAG, "Limpando chat")
        areaChat.text = "👋 Chat limpo!\n\n💬 Digite uma mensagem abaixo e veja as respostas automáticas dos outros jogadores!"
    }
    
    private fun scrollParaBaixo() {
        scrollChat.post {
            scrollChat.fullScroll(ScrollView.FOCUS_DOWN)
        }
    }
}
