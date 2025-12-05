package com.example.myapplication

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment

class FragmentoCartaoCredito : Fragment() {
    private val TAG = "FragmentoCartaoCredito"
    
    // Elementos da UI
    private lateinit var inputNumeroCartao: EditText
    private lateinit var inputNomeTitular: EditText
    private lateinit var inputValidade: EditText
    private lateinit var inputCVV: EditText
    private lateinit var botaoProcessar: Button
    private lateinit var textoResultado: TextView
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "Criando view do fragmento cartão de crédito")
        return inflater.inflate(R.layout.fragment_cartao_credito, container, false)
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "View criada - inicializando elementos")
        
        try {
            inicializarElementosUI(view)
            configurarMascaras()
            configurarBotaoProcessar()
            
            Log.d(TAG, "Fragmento cartão de crédito inicializado com sucesso")
        } catch (e: Exception) {
            Log.e(TAG, "Erro ao inicializar fragmento: ${e.message}")
        }
    }
    
    private fun inicializarElementosUI(view: View) {
        Log.d(TAG, "Inicializando elementos da UI")
        inputNumeroCartao = view.findViewById(R.id.input_numero_cartao)
        inputNomeTitular = view.findViewById(R.id.input_nome_titular)
        inputValidade = view.findViewById(R.id.input_validade)
        inputCVV = view.findViewById(R.id.input_cvv)
        botaoProcessar = view.findViewById(R.id.botao_processar)
        textoResultado = view.findViewById(R.id.texto_resultado)
        Log.d(TAG, "Elementos da UI inicializados")
    }
    
    private fun configurarMascaras() {
        // Máscara para número do cartão (formato: XXXX XXXX XXXX XXXX)
        inputNumeroCartao.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            
            override fun afterTextChanged(s: Editable?) {
                val texto = s.toString().replace(" ", "")
                if (texto.length <= 16) {
                    val formatado = texto.chunked(4).joinToString(" ")
                    if (formatado != s.toString()) {
                        inputNumeroCartao.removeTextChangedListener(this)
                        inputNumeroCartao.setText(formatado)
                        inputNumeroCartao.setSelection(formatado.length)
                        inputNumeroCartao.addTextChangedListener(this)
                    }
                } else {
                    s?.delete(16, s.length)
                }
            }
        })
        
        // Máscara para validade (formato: MM/AA)
        inputValidade.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            
            override fun afterTextChanged(s: Editable?) {
                val texto = s.toString().replace("/", "")
                if (texto.length <= 4) {
                    if (texto.length >= 2) {
                        val formatado = "${texto.substring(0, 2)}/${if (texto.length > 2) texto.substring(2) else ""}"
                        if (formatado != s.toString()) {
                            inputValidade.removeTextChangedListener(this)
                            inputValidade.setText(formatado)
                            inputValidade.setSelection(formatado.length)
                            inputValidade.addTextChangedListener(this)
                        }
                    }
                } else {
                    s?.delete(5, s.length)
                }
            }
        })
        
        // Limitar CVV a 3 dígitos
        inputCVV.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            
            override fun afterTextChanged(s: Editable?) {
                if (s?.length ?: 0 > 3) {
                    s?.delete(3, s.length)
                }
            }
        })
    }
    
    private fun configurarBotaoProcessar() {
        Log.d(TAG, "Configurando botão processar")
        
        botaoProcessar.setOnClickListener {
            val numeroCartao = inputNumeroCartao.text.toString().replace(" ", "")
            val nomeTitular = inputNomeTitular.text.toString().trim()
            val validade = inputValidade.text.toString()
            val cvv = inputCVV.text.toString()
            
            // Validações básicas
            if (numeroCartao.length < 16) {
                Toast.makeText(context, "⚠️ Número do cartão deve ter 16 dígitos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            
            if (nomeTitular.isEmpty()) {
                Toast.makeText(context, "⚠️ Digite o nome do titular", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            
            if (validade.length < 5) {
                Toast.makeText(context, "⚠️ Digite a validade (MM/AA)", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            
            if (cvv.length < 3) {
                Toast.makeText(context, "⚠️ CVV deve ter 3 dígitos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            
            // Simular processamento
            textoResultado.text = "💳 Processando pagamento...\n\n" +
                    "Cartão: **** **** **** ${numeroCartao.takeLast(4)}\n" +
                    "Titular: $nomeTitular\n" +
                    "Validade: $validade\n" +
                    "CVV: ***\n\n" +
                    "⏳ Aguarde..."
            
            // Simular delay de processamento
            botaoProcessar.isEnabled = false
            
            android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
                textoResultado.text = "✅ Pagamento processado com sucesso!\n\n" +
                        "Cartão: **** **** **** ${numeroCartao.takeLast(4)}\n" +
                        "Titular: $nomeTitular\n" +
                        "Validade: $validade\n\n" +
                        "💰 Seu saldo foi creditado!\n" +
                        "⚠️ Lembre-se: Este é um emulador.\n" +
                        "Nenhum dado real foi processado."
                
                botaoProcessar.isEnabled = true
                Log.d(TAG, "Pagamento simulado processado")
            }, 2000)
            
            Log.d(TAG, "Processamento de pagamento iniciado")
        }
        
        Log.d(TAG, "Botão processar configurado")
    }
}

