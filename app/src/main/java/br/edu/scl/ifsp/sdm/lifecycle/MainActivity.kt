package br.edu.scl.ifsp.sdm.lifecycle

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.widget.EditText
import androidx.core.view.children
import androidx.core.widget.doAfterTextChanged
import br.edu.scl.ifsp.sdm.lifecycle.databinding.ActivityMainBinding
import br.edu.scl.ifsp.sdm.lifecycle.databinding.TilePhoneBinding

class MainActivity : AppCompatActivity() {
    private val activityMainBinding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private var filledChars:Int = 0 // Variavel que conta as vezes que modificamos o name edittext

    //Criando (singleton)
    companion object{
        const val FILLED_CHARS = "FILLER_CHARS"
        const val PHONES = "PHONES"
    }



    // onCreate inicializa o ciclo de vida
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activityMainBinding.root)


        activityMainBinding.apply{
            setSupportActionBar(toolbarIn.toolbar)

            // Adicionar um text watcher
            nameEt.doAfterTextChanged {
                "${getString(R.string.filled_chars)} ${++filledChars}".also{
                    filledCharsTv.text = it
                }
            }

            // Eventos de clique
            addPhoneBt.setOnClickListener{
                val tilePhoneBinding = TilePhoneBinding.inflate(layoutInflater)
                phonesLl.addView(tilePhoneBinding.root)
            }

            // Botão que irá para another
            openAnotherActivityBt.setOnClickListener{
                startActivity(Intent(this@MainActivity, AnotherActivity::class.java)) // Ir para outra activity (usando intent)
            }

        }

        supportActionBar?.subtitle = getString(R.string.main)

        Log.v(getString(R.string.app_name), "Main - onCreate(): INÍCIO COMPLETO")

    }

    // Salvar tudo que desejo restaurar no futuro (atentar-se - não funciona se usuário clicar em back)
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        Log.v(getString(R.string.app_name), "Main - onSaveInstanceState(): Salvando dados")

        outState.putInt(FILLED_CHARS, filledChars)

        // Para telefones, sendo view dinâmicas (como não há lista), temos que percorrer nossas views
        val phones = mutableListOf<String>()

        // let trata o valor como it
        activityMainBinding.phonesLl.children.forEachIndexed{index,view ->
            if(index != 0){
                (view as EditText).text.toString().also{
                    phones.add(it)
                }
            }
        }

        // typeArray => retorna um array de string
        outState.putStringArray(PHONES, phones.toTypedArray())
    }

    // Restaurar os dados que estão em outState
    override fun onRestoreInstanceState(savedInstanceState: Bundle){
        super.onRestoreInstanceState(savedInstanceState)

        savedInstanceState.getInt(FILLED_CHARS, 0).let {
            filledChars = it

            "${getString(R.string.filled_chars)} ${filledChars}".also{fc->
                activityMainBinding.filledCharsTv.text = fc
            }

        }

        savedInstanceState.getStringArray(PHONES)?.forEach { phone->

            // Adiciona um objeto inteiro de telefone na view
            TilePhoneBinding.inflate(layoutInflater).apply {
                root.setText(phone)
                activityMainBinding.phonesLl.addView(root)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        Log.v(getString(R.string.app_name), "Main - onStart(): INÍCIO VISÍVEL")
    }

    override fun onResume() {
        super.onResume()
        Log.v(getString(R.string.app_name), "Main - onResume(): INÍCIO PRIMEIRO PLANO")
    }

    override fun onPause() {
        super.onPause()
        Log.v(getString(R.string.app_name), "Main - onPause(): FIM PRIMEIRO PLANO")
    }

    override fun onStop() {
        super.onStop()
        Log.v(getString(R.string.app_name), "Main - onStop(): FIM VISÍVEL")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.v(getString(R.string.app_name), "Main - onDestroy(): FIM COMPLETO")
    }

    override fun onRestart() {
        super.onRestart()
        Log.v(getString(R.string.app_name), "Main - onRestart(): preparando onStart()")
    }

}