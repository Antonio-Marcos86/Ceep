package alura.com.br.ceep.ui.activity.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import alura.com.br.ceep.R;
import alura.com.br.ceep.ui.adapter.ListaNotasAdapter;
import alura.com.br.ceep.ui.dao.NotaDAO;
import alura.com.br.ceep.ui.helper.callback.NotaItemTouchHelperCallback;
import alura.com.br.ceep.ui.listener_apapter.OnItemClickListener;
import alura.com.br.ceep.ui.model.Nota;

import static alura.com.br.ceep.ui.activity.activity.NotaActivityConstantes.CHAVE_NOTA;
import static alura.com.br.ceep.ui.activity.activity.NotaActivityConstantes.CHAVE_POSICAO;
import static alura.com.br.ceep.ui.activity.activity.NotaActivityConstantes.CODIGO_REQUISICAO_INSERE_NOTA;
import static alura.com.br.ceep.ui.activity.activity.NotaActivityConstantes.CODIGO_REQUISICAO_ALTERA_NOTA;
import static alura.com.br.ceep.ui.activity.activity.NotaActivityConstantes.POSICAO_INVALIDA;

public class MainActivity extends AppCompatActivity {

    private ListaNotasAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Notas");
        List<Nota> todasNotas = pegaTodasNotas();
        configuraRecyclerView(todasNotas);
        configuraBotaoInsereNota();
    }

    private void configuraBotaoInsereNota() {
        TextView botaoInsereNota = findViewById(R.id.textView);
        botaoInsereNota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vaiParaFormularioNotaActivityInsere();
            }
        });
    }

    private void vaiParaFormularioNotaActivityInsere() {
        Intent iniciaFormularioNota =
                new Intent(MainActivity.this,
                        FormularioNotaActivity.class);
        startActivityForResult(iniciaFormularioNota,
                CODIGO_REQUISICAO_INSERE_NOTA);
    }

    private List<Nota> pegaTodasNotas() {
        NotaDAO dao = new NotaDAO();
        return dao.todos();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (ehResultadoInsereNota(requestCode, data)) {
            if(resultadoOk(resultCode)){
                Nota notaRecebida = (Nota) data.getSerializableExtra(CHAVE_NOTA);
                adiciona(notaRecebida);
            }else if (resultCode == Activity.RESULT_CANCELED){
                message("Cancelado");
            }

        }
        if(ehResultadoParaAlterarNota(requestCode, data)){
            if(resultadoOk(resultCode)){
            Nota notaRecebida = (Nota) data.getSerializableExtra(CHAVE_NOTA);
            int posicaoRecebida = data.getIntExtra(CHAVE_POSICAO, POSICAO_INVALIDA);
            if(posicaoRecebida >POSICAO_INVALIDA){
                altera(notaRecebida, posicaoRecebida);
            }else{
                message("Ocorreu um problema na alteração da nota");
            }
        }
        }
    }

    private void altera(Nota nota, int posicao) {
        new NotaDAO().altera(posicao, nota);
        adapter.altera(posicao, nota);
    }

    private void message(String texto) {
        Toast.makeText(this, texto, Toast.LENGTH_SHORT).show();
    }

    private boolean ehResultadoParaAlterarNota(int requestCode, Intent data) {
        return ehCodigoRequisicaoAlteraNota(requestCode)  && temNota(data);
    }

    private boolean resultadoOk(int resultCode) {
        return resultCode == Activity.RESULT_OK;
    }

    private boolean ehCodigoRequisicaoAlteraNota(int requestCode) {
        return requestCode == CODIGO_REQUISICAO_ALTERA_NOTA;
    }

    private void adiciona(Nota nota) {
        new NotaDAO().insere(nota);
        adapter.adiciona(nota);
    }

    private boolean ehResultadoInsereNota(int requestCode, Intent data) {
        return ehCodigoRequisicaoInsereNota(requestCode) &&
                temNota(data);
    }

    private boolean temNota(Intent data) {
        return data.hasExtra(CHAVE_NOTA);
    }

    private boolean ehCodigoResultadoNotaCriada(int resultCode) {
        return resultadoOk(resultCode);
    }

    private boolean ehCodigoRequisicaoInsereNota(int requestCode) {
        return requestCode == CODIGO_REQUISICAO_INSERE_NOTA;
    }

    private void configuraRecyclerView(List<Nota> todasNotas) {
        RecyclerView listaNotas = findViewById(R.id.listaNotas);
        configuraAdapter(todasNotas, listaNotas);
        configuraItemTouchHelper(listaNotas);
    }

    private void configuraItemTouchHelper(RecyclerView listaNotas) {
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new NotaItemTouchHelperCallback(adapter));
        itemTouchHelper.attachToRecyclerView(listaNotas);
    }

    private void configuraAdapter(List<Nota> todasNotas, RecyclerView listaNotas) {
        adapter = new ListaNotasAdapter(this, todasNotas);
        listaNotas.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(Nota nota,int posicao) {
                vaiParaFormularioActivityAltera(nota, posicao);
            }
        });
    }

    private void vaiParaFormularioActivityAltera(Nota nota, int posicao) {
        Intent abreFormularioComNota = new Intent(MainActivity.this,FormularioNotaActivity.class);
        abreFormularioComNota.putExtra(CHAVE_NOTA, nota);
        abreFormularioComNota.putExtra(CHAVE_POSICAO, posicao);
        startActivityForResult(abreFormularioComNota, CODIGO_REQUISICAO_ALTERA_NOTA);
    }
}