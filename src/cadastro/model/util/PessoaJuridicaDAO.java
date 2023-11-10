package cadastro.model.util;

import cadastrobd.model.PessoaJuridica;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PessoaJuridicaDAO {
    private ConectorBD conectorBD;

    public PessoaJuridicaDAO() {
        conectorBD = new ConectorBD();
    }

    public List<PessoaJuridica> getPessoas() {
        List<PessoaJuridica> pessoaJuridicas = new ArrayList<>();

        conectorBD.getPrepared("""
                SELECT * FROM PESSOA AS P 
                INNER JOIN Pessoa_juridica F ON P.idPessoa = F.idPessoa
                """);
        ResultSet res = conectorBD.getSelect();
        try {
            while(res.next()) {
                pessoaJuridicas.add(new PessoaJuridica(
                        res.getInt("idPessoa"),
                        res.getString("nome"),
                        res.getString("logradouro"),
                        res.getString("cidade"),
                        res.getString("estado"),
                        res.getString("telefone"),
                        res.getString("email"),
                        res.getString("cnpj")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        this.conectorBD.closeQuerys();
        return pessoaJuridicas;


    }

    public PessoaJuridica getPessoa (int id) {
        PessoaJuridica pessoaJuridica;
        try {
            conectorBD.getPrepared("""
                    SELECT * FROM PESSOA AS P 
                    INNER JOIN Pessoa_juridica F ON P.idPessoa = F.idPessoa
                    where P.idPessoa = %d
                    """.formatted(id));

            ResultSet res = conectorBD.getSelect();

            res.next();
            pessoaJuridica = new PessoaJuridica(
                    res.getInt("idPessoa"),
                    res.getString("nome"),
                    res.getString("logradouro"),
                    res.getString("cidade"),
                    res.getString("estado"),
                    res.getString("telefone"),
                    res.getString("email"),
                    res.getString("cnpj"));


        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            this.conectorBD.closeQuerys();

        }
        return pessoaJuridica;
    }

    public void incluir (PessoaJuridica pessoaJuridica) {
        String sqlQuery = String.format("""
                    BEGIN TRANSACTION
                    INSERT INTO Pessoa(nome, logradouro, cidade, estado, telefone, email)
                    VALUES
                    ('%s', '%s', '%s', '%s', '%s', '%s')
                    INSERT INTO Pessoa_juridica(idPessoa, cnpj)
                    VALUES
                    ((SELECT TOP(1) SCOPE_IDENTITY() FROM Pessoa), '%s')
                    
                    IF @@ERROR=0
                    COMMIT
                    ELSE
                    ROLLBACK
                    """, pessoaJuridica.getNome(),
                pessoaJuridica.getLogradouro(),
                pessoaJuridica.getCidade(),
                pessoaJuridica.getEstado(),
                pessoaJuridica.getCelular(),
                pessoaJuridica.getEmail(),
                pessoaJuridica.getCnpj());

        conectorBD.getPrepared(sqlQuery);
        conectorBD.execute();

    }

    public void excluir (int id) {
        conectorBD.getPrepared("""
                    BEGIN TRANSACTION                     
                    DELETE FROM Pessoa_juridica
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

    public void alterar (int id, PessoaJuridica pessoaJuridica) {
        String sqlQuery = String.format("""
                    BEGIN TRANSACTION
                    
                    UPDATE Pessoa_juridica
                    SET cnpj = '%s'
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
                pessoaJuridica.getCnpj(),
                id,
                pessoaJuridica.getNome(),
                pessoaJuridica.getLogradouro(),
                pessoaJuridica.getCidade(),
                pessoaJuridica.getEstado(),
                pessoaJuridica.getCelular(),
                pessoaJuridica.getEmail(),
                id);
        conectorBD.getPrepared(sqlQuery);
        conectorBD.execute();
        conectorBD.closeQuerys();
    }
}
