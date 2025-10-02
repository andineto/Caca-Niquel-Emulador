package com.example.myapplication

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "=== INÍCIO DO ONCREATE ===")
        
        try {
            Log.d(TAG, "Carregando layout XML com tabs")
            setContentView(R.layout.activity_main)
            Log.d(TAG, "Layout XML carregado com sucesso")
            
            // Configurar ViewPager2 e TabLayout
            configurarTabs()
            
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
                0 -> "🎰 Caça-níquel"
                1 -> "💬 Chat"
                else -> "Tab $position"
            }
        }.attach()
        
        Log.d(TAG, "Tabs configuradas com sucesso")
    }
}