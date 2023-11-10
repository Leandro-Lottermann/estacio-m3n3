import cadastro.model.util.PessoaFisicaDAO;
import cadastro.model.util.PessoaJuridicaDAO;
import cadastrobd.model.PessoaFisica;
import cadastrobd.model.PessoaJuridica;

public class CadastroBDTeste {
    public static void main(String[] args) {
        PessoaFisicaDAO pessoaFisicaDAO = new PessoaFisicaDAO();
        PessoaJuridicaDAO pessoaJuridicaDAO = new PessoaJuridicaDAO();

        PessoaFisica pessoaFisica = new PessoaFisica(
                00,
                "Leandro",
                "Rua do centro",
                "Harmonia",
                "RS",
                "(51) 99999-9999",
                "leandro@estacio.com",
                "011.123.523-11");

        pessoaFisicaDAO.incluir(pessoaFisica);
        pessoaFisicaDAO.getPessoas().forEach((PessoaFisica p) -> {p.exibir();});
        pessoaFisicaDAO.excluir(1);

        PessoaJuridica pessoaJuridica = new PessoaJuridica(
                00,
                "Calçados SA",
                "Rua do centro",
                "Harmonia",
                "RS",
                "(51) 99999-9999",
                "leandro@estacio.com",
                "12.123.123/0001-13"
        );

        pessoaJuridicaDAO.incluir(pessoaJuridica);
        pessoaJuridicaDAO.getPessoas().forEach((PessoaJuridica p) -> {p.exibir();});
        pessoaJuridicaDAO.excluir(2);
    }
}
