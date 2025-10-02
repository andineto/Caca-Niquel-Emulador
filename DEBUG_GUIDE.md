# Guia de Debug - App Caça-níquel

## 1. Configuração do Debug no Android Studio

### Passo 1: Abrir o Logcat
1. No Android Studio, vá em **View → Tool Windows → Logcat**
2. Ou use o atalho: **Alt + 6** (Windows/Linux) ou **Cmd + 6** (Mac)

### Passo 2: Filtrar logs do seu app
1. No Logcat, selecione seu dispositivo/emulador
2. No campo de filtro, digite: `MainActivity` ou `Fragmento`
3. Isso mostrará apenas os logs do seu app

### Passo 3: Executar em modo Debug
1. Clique no botão **Debug** (ícone de bug) ao invés de Run
2. Ou use **Shift + F9**

## 2. Verificar Logs de Erro

### Logs importantes para monitorar:
- `MainActivity`: Logs da Activity principal
- `FragmentoCacaNiquel`: Logs do fragmento do caça-níquel  
- `FragmentoChat`: Logs do fragmento do chat
- `AndroidRuntime`: Erros de crash

### O que procurar nos logs:
```
D/MainActivity: onCreate iniciado
D/MainActivity: Layout carregado com sucesso
D/MainActivity: ViewPager configurado com sucesso
D/FragmentoCacaNiquel: onCreateView iniciado
D/FragmentoCacaNiquel: Binding criado com sucesso
```

## 3. Possíveis Problemas e Soluções

### Problema 1: Theme não encontrado
**Sintoma**: `android.view.InflateException: Binary XML file line #X: Error inflating class`
**Solução**: Já corrigido - mudamos para `Theme.AppCompat.Light.DarkActionBar`

### Problema 2: ViewBinding não funciona
**Sintoma**: `java.lang.NullPointerException` ao acessar views
**Solução**: Verificar se `viewBinding = true` está no build.gradle

### Problema 3: Fragment não carrega
**Sintoma**: App abre mas não mostra conteúdo
**Solução**: Verificar logs dos Fragments

## 4. Comandos Úteis para Debug

### Limpar logs antes de testar:
```bash
adb logcat -c
```

### Ver logs em tempo real:
```bash
adb logcat | grep -E "(MainActivity|Fragmento|AndroidRuntime)"
```

### Instalar APK de debug:
```bash
adb install app/build/outputs/apk/debug/app-debug.apk
```

## 5. Próximos Passos

1. **Execute o app em modo Debug**
2. **Monitore o Logcat** para ver onde está travando
3. **Compartilhe os logs** se ainda houver problemas

Os logs agora estão configurados para mostrar exatamente onde o problema está ocorrendo!

