package alura.com.br.ceep.ui.activity.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.io.Serializable;

import alura.com.br.ceep.R;
import alura.com.br.ceep.ui.model.Nota;

import static alura.com.br.ceep.ui.activity.activity.NotaActivityConstantes.CHAVE_NOTA;
import static alura.com.br.ceep.ui.activity.activity.NotaActivityConstantes.CHAVE_POSICAO;
import static alura.com.br.ceep.ui.activity.activity.NotaActivityConstantes.POSICAO_INVALIDA;

public class FormularioNotaActivity extends AppCompatActivity {


    public static final String ATUALIZA_APPBAR_NOVA_TELA = "Atualiza Dados";
    private int posicaoRecebida = POSICAO_INVALIDA;
    private TextView titulo,descricao;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario_nota);
        setTitle("Insere Dados");
        inicializaComponentes();

        Intent dadosRecebidos = getIntent();
        if(dadosRecebidos.hasExtra(CHAVE_NOTA)){
            setTitle(ATUALIZA_APPBAR_NOVA_TELA);
            Nota notaRecebida = (Nota) dadosRecebidos.getSerializableExtra(CHAVE_NOTA);
            posicaoRecebida = dadosRecebidos.getIntExtra(CHAVE_POSICAO, POSICAO_INVALIDA);
            preencheCampos(notaRecebida);
        }

    }

    private void inicializaComponentes() {
        titulo = findViewById(R.id.formulario_nota_titulo);
        descricao = findViewById(R.id.formulario_nota_descricao);
    }

    private void preencheCampos(Nota notaRecebida) {
        titulo.setText(notaRecebida.getTitulo());
        descricao.setText(notaRecebida.getDescricao());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_formulario_nota_salva,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(ehMenuSalvaNota(item)){
            Nota notaCriada = criaNota();
            retornaNota(notaCriada);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private void retornaNota(Nota nota) {
        Intent resultadoInsercao = new Intent();
        resultadoInsercao.putExtra(CHAVE_NOTA, (Serializable) nota);
        resultadoInsercao.putExtra("posicao",posicaoRecebida);
        setResult(Activity.RESULT_OK,resultadoInsercao);


    }

    private Nota criaNota() {
        return new Nota(titulo.getText().toString(),descricao.getText().toString());
    }
    private boolean ehMenuSalvaNota(MenuItem item) {
        return item.getItemId() == R.id.menu_formulario_nota_ic_salva;
    }
}