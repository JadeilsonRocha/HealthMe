package usuario.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import java.util.ArrayList;
import java.util.List;

import usuario.dominio.ContatoEmergencia;

public class ContatoDao {
    private SQLiteDatabase db;
    private DbHelper dataBaseHelper;
    private Context context;
    private SqlScripts script;
    Cursor cursor;

    public ContatoDao(Context context) {
        this.context = context;
        dataBaseHelper = new DbHelper(context);
        script = new SqlScripts();
    }

    public void inserirRegistro(ContatoEmergencia contatoEmergencia) {
        ContentValues valor;
        db = dataBaseHelper.getWritableDatabase();

        valor = new ContentValues();
        valor.put(DbHelper.USUARIO_CONTATO, contatoEmergencia.getUsuario().getLogin());
        valor.put(DbHelper.CONTATO_NOME, contatoEmergencia.getNome());
        valor.put(DbHelper.CONTATO_TELEFONE, contatoEmergencia.getNumero());

        db.insert(DbHelper.TABELA_CONTATO, null, valor);
        db.close();
    }

    public void atualizarRegistro(ContatoEmergencia contatoEmergencia) {
        ContentValues valor;
        String where = DbHelper.ID + "=" + contatoEmergencia.getId();
        db = dataBaseHelper.getWritableDatabase();

        valor = new ContentValues();
        valor.put(DbHelper.USUARIO_CONTATO, contatoEmergencia.getUsuario().getLogin());
        valor.put(DbHelper.CONTATO_NOME, contatoEmergencia.getNome());
        valor.put(DbHelper.CONTATO_TELEFONE, contatoEmergencia.getNumero());

        db.update(DbHelper.TABELA_CONTATO, valor, where, null);
        db.close();
    }

    public ContatoEmergencia buscarContato( String nome) {
        db = dataBaseHelper.getReadableDatabase();

        String[] parametros = { nome};

        Cursor cursor = db.rawQuery(script.cmdWhere(dataBaseHelper.TABELA_CONTATO, dataBaseHelper.CONTATO_NOME),
                parametros);
        ContatoEmergencia contato = null;

        if (cursor.moveToNext()) {
            contato = criarContato(cursor);
        }
        cursor.close();
        db.close();
        return contato;
    }

    public List<ContatoEmergencia> buscarContatos(String usuario){
        db = dataBaseHelper.getReadableDatabase();

        String[] parametros = { usuario};

        Cursor cursor = db.rawQuery(script.cmdWhere(dataBaseHelper.TABELA_CONTATO, dataBaseHelper.USUARIO_CONTATO),
                parametros);
        ContatoEmergencia contato = null;
        List<ContatoEmergencia> contatos = new ArrayList<>();

        while (cursor.moveToNext()) {
            contato = criarContato(cursor);
            contatos.add(contato);
        }
        cursor.close();
        db.close();
        return contatos;
    }

    public ContatoEmergencia criarContato(Cursor cursor) {
        ContatoEmergencia contatoEmergencia = new ContatoEmergencia();
        contatoEmergencia.setId(cursor.getInt(0));
        contatoEmergencia.setNome(cursor.getString(2));
        contatoEmergencia.setNumero(cursor.getString(3));
        return contatoEmergencia;
    }

    public Cursor buscarDados(String usuario){

        db = dataBaseHelper.getWritableDatabase();
        String[] parametros = { usuario};

        cursor = db.rawQuery("SELECT * FROM " + DbHelper.TABELA_CONTATO + " WHERE " + DbHelper.USUARIO_CONTATO +" Like ?",parametros);

        if(cursor!=null){
            cursor.moveToFirst();
        }
        db.close();
        return cursor;
    }
}
