package cadastro.model.util;

import cadastrobd.model.PessoaFisica;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;

public class PessoaFisicaDAO {
    private ConectorBD conectorBD;

    public PessoaFisicaDAO() {
        conectorBD = new ConectorBD();
    }

    public List<PessoaFisica> getPessoas() {
        List<PessoaFisica> pessoaFisicas = new ArrayList<>();

        conectorBD.getPrepared("""
                SELECT * FROM PESSOA AS P 
                INNER JOIN Pessoa_fisica F ON P.idPessoa = F.idPessoa
                """);
        ResultSet res = conectorBD.getSelect();
        try {
            while(res.next()) {
                pessoaFisicas.add(new PessoaFisica(
                        res.getInt("idPessoa"),
                        res.getString("nome"),
                        res.getString("logradouro"),
                        res.getString("cidade"),
                        res.getString("estado"),
                        res.getString("telefone"),
                        res.getString("email"),
                        res.getString("cpf")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        this.conectorBD.closeQuerys();
        return pessoaFisicas;


    }

    public PessoaFisica getPessoa (int id) {
        PessoaFisica pessoaFisica;
        try {
            conectorBD.getPrepared("""
                    SELECT * FROM PESSOA AS P 
                    INNER JOIN Pessoa_fisica F ON P.idPessoa = F.idPessoa
                    where P.idPessoa = %d
                    """.formatted(id));

            ResultSet res = conectorBD.getSelect();

            res.next();
            pessoaFisica = new PessoaFisica(
                    res.getInt("idPessoa"),
                    res.getString("nome"),
                    res.getString("logradouro"),
                    res.getString("cidade"),
                    res.getString("estado"),
                    res.getString("telefone"),
                    res.getString("email"),
                    res.getString("cpf"));


        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            this.conectorBD.closeQuerys();

        }
        return pessoaFisica;
    }

    public void incluir (PessoaFisica pessoaFisica) {
            String sqlQuery = String.format("""
                    BEGIN TRANSACTION
                    INSERT INTO Pessoa(nome, logradouro, cidade, estado, telefone, email)
                    VALUES
                    ('%s', '%s', '%s', '%s', '%s', '%s')
                    INSERT INTO Pessoa_fisica(idPessoa, cpf)
                    VALUES
                    ((SELECT TOP(1) SCOPE_IDENTITY() FROM Pessoa), '%s')
                    
                    IF @@ERROR=0
                    COMMIT
                    ELSE
                    ROLLBACK
                    """, pessoaFisica.getNome(),
                    pessoaFisica.getLogradouro(),
                    pessoaFisica.getCidade(),
                    pessoaFisica.getEstado(),
                    pessoaFisica.getCelular(),
                    pessoaFisica.getEmail(),
                    pessoaFisica.getCpf());

            conectorBD.getPrepared(sqlQuery);
            conectorBD.execute();


    }

    public void excluir (int id) {
        conectorBD.getPrepared("""
                    BEGIN TRANSACTION                     
                    DELETE FROM Pessoa_fisica
                    where idPessoa = %d
                    
                    DELETE FROM Pessoa
                    where idPessoa = %d
                    IF @@ERROR=0
                    COMMIT
                    ELSE
                    ROLLBACK
                    
                    """.formatted(id, id));
        conectorBD.execute();
        conectorBD.closeQuerys();
    }

    public void alterar (int id, PessoaFisica pessoaFisica) {
        String sqlQuery = String.format("""
                    BEGIN TRANSACTION
                    
                    UPDATE Pessoa_fisica
                    SET cpf = '%s'
                    WHERE idPessoa = %d
                    
                    UPDATE Pessoa
                    SET
                    nome = '%s',
                    logradouro = '%s',
                    cidade = '%s',
                    estado = '%s',
                    telefone = '%s',
                    email = '%s'
                    WHERE idPessoa = %d
                    
                    
                    IF @@ERROR=0
                    COMMIT
                    ELSE
                    ROLLBACK
                    """,
                pessoaFisica.getCpf(),
                id,
                pessoaFisica.getNome(),
                pessoaFisica.getLogradouro(),
                pessoaFisica.getCidade(),
                pessoaFisica.getEstado(),
                pessoaFisica.getCelular(),
                pessoaFisica.getEmail(),
                id);
        conectorBD.getPrepared(sqlQuery);
        conectorBD.execute();
        conectorBD.closeQuerys();
    }

}
