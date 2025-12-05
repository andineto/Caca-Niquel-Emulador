package com.example.myapplication

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment

class FragmentoRegras : Fragment() {
    private val TAG = "FragmentoRegras"
    
    // Ícones do caça-níquel com multiplicadores (mesmos do FragmentoCacaNiquel)
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
    
    private lateinit var textoRegras: TextView
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "Criando view do fragmento regras")
        return inflater.inflate(R.layout.fragment_regras, container, false)
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "View criada - inicializando elementos")
        
        try {
            textoRegras = view.findViewById(R.id.texto_regras)
            atualizarTextoRegras()
            
            Log.d(TAG, "Fragmento regras inicializado com sucesso")
        } catch (e: Exception) {
            Log.e(TAG, "Erro ao inicializar fragmento: ${e.message}")
        }
    }
    
    private fun atualizarTextoRegras() {
        val regras = StringBuilder()
        
        regras.append("🎰 REGRAS DO JOGO 🎰\n\n")
        regras.append("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n\n")
        
        regras.append("📋 COMO JOGAR:\n\n")
        regras.append("1. Configure seu saldo inicial\n")
        regras.append("2. Escolha o valor da aposta\n")
        regras.append("3. As rodadas acontecem automaticamente a cada 10 segundos\n")
        regras.append("4. Para ganhar, você precisa que os 3 rolos mostrem o mesmo símbolo\n")
        regras.append("5. O prêmio é calculado: Aposta × Multiplicador\n\n")
        
        regras.append("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n\n")
        
        regras.append("💰 MULTIPLICADORES:\n\n")
        
        // Ordenar por multiplicador (menor para maior)
        val iconesOrdenados = iconesCaçaNiquel.toList().sortedBy { it.second }
        
        for ((icone, multiplicador) in iconesOrdenados) {
            val nivel = when {
                multiplicador <= 3.0 -> "⭐"
                multiplicador <= 5.0 -> "⭐⭐"
                multiplicador <= 8.0 -> "⭐⭐⭐"
                multiplicador <= 10.0 -> "⭐⭐⭐⭐"
                else -> "⭐⭐⭐⭐⭐"
            }
            regras.append("$icone $nivel Multiplicador: ${multiplicador.toInt()}x\n")
            regras.append("   Exemplo: Aposta R$ 50,00 = R$ ${String.format("%.2f", 50.0 * multiplicador)}\n\n")
        }
        
        regras.append("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n\n")
        
        regras.append("🎯 DICAS:\n\n")
        regras.append("• Quanto mais rodadas sem ganhar, maior a chance de vitória\n")
        regras.append("• Ícones com multiplicadores menores aparecem com mais frequência\n")
        regras.append("• O jogo é balanceado para dar chances justas de vitória\n")
        regras.append("• Você receberá uma notificação ao gastar mais de R$ 500,00\n\n")
        
        regras.append("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n\n")
        
        regras.append("⚠️ AVISOS:\n\n")
        regras.append("• Este é um jogo educacional\n")
        regras.append("• Jogue com responsabilidade\n")
        regras.append("• Não use valores reais de cartão de crédito\n")
        regras.append("• O emulador de cartão é apenas para demonstração\n")
        
        textoRegras.text = regras.toString()
        Log.d(TAG, "Texto de regras atualizado")
    }
}

