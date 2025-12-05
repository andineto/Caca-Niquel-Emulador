package com.example.myapplication

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"

    private var tentouSairUmaVez = false;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "=== INÍCIO DO ONCREATE ===")
        
        try {
            Log.d(TAG, "Carregando layout XML com tabs")
            setContentView(R.layout.activity_main)
            Log.d(TAG, "Layout XML carregado com sucesso")
            
            // Configurar ViewPager2 e TabLayout
            configurarTabs()

            configurarLogicaDeSaida()

            Log.d(TAG, "=== ONCREATE CONCLUÍDO COM SUCESSO ===")
            
        } catch (e: Exception) {
            Log.e(TAG, "=== ERRO NO ONCREATE ===")
            Log.e(TAG, "Tipo do erro: ${e.javaClass.simpleName}")
            Log.e(TAG, "Mensagem: ${e.message}")
            Log.e(TAG, "Stack trace:", e)
            throw e
        }
    }
    
    private fun configurarTabs() {
        Log.d(TAG, "Configurando tabs")
        
        val viewPager = findViewById<androidx.viewpager2.widget.ViewPager2>(R.id.view_pager)
        val tabLayout = findViewById<com.google.android.material.tabs.TabLayout>(R.id.tab_layout)
        
        // Configurar adapter
        val adapter = AdapterFragmentos(this)
        viewPager.adapter = adapter
        
        // Configurar tabs
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "🎰 IFSP BET"
                1 -> "💬 Chat"
                2 -> "💳 Cartão"
                3 -> "📋 Regras"
                else -> "Tab $position"
            }
        }.attach()
        
        Log.d(TAG, "Tabs configuradas com sucesso")
    }

    private fun configurarLogicaDeSaida(){
        Log.d(TAG, "Configurando logica de-saida")

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if(!tentouSairUmaVez){
                    Log.d(TAG, "Primeira tentaiva de sair: Mostrando Imagem")
                    mostrarImagemDeSaida()
                    tentouSairUmaVez = true;
                }else{
                    Log.d(TAG, "Segunda tentativa de sair: Fechando app")
                    isEnabled = false
                    onBackPressedDispatcher.onBackPressed()
                }
            }
        }
        onBackPressedDispatcher.addCallback(this, callback)
    }

    private fun mostrarImagemDeSaida(){
        val imageDiamente = ImageView(this)
        imageDiamente.setImageResource(R.drawable.diamante)
        imageDiamente.adjustViewBounds = true;

        AlertDialog.Builder(this)
            .setTitle("Já vai?")
            .setMessage("Vai abonadonar a chance de ficar milionário?")
            .setView(imageDiamente)
            .setPositiveButton("Ficar"){
                dialog, _ ->
                dialog.dismiss()
            }
            .setNegativeButton("Sair"){
                _, _ ->
                finish()
            }
            .setCancelable(false)
            .show()
    }
}