import java.io.*;
import java.nio.file.Files;
import java.util.List;
import java.util.Scanner;

public class Projeto_Guia_Turistico {
    public static void main(String[] args) {
        Scanner ler = new Scanner(System.in);
        int opcao = 0;
        String path = "clientes/";
        String caminhoCadastro = path + "cadastro/";
        String caminhoReserva = path + "reservas/";

        inicializarGeral(path);
        inicializarReservas(path);
        int id = 0;
        int idR = 0;

        while (opcao != 3) {
            menu();
            opcao = ler.nextInt();
            switch (opcao) {
                case 1:
                    menuCadastroUsuario(id, path, caminhoCadastro, caminhoReserva, idR);
                    break;
                case 2:
                    validarAdmin(id, caminhoReserva, idR);
                    break;
                case 3:
                    System.out.println("Encerrando...");
                    break;
                default:
                    System.out.println("Opção Inválida!");
            }
        }
    }

    private static void menuCadastroUsuario(int id, String path, String caminhoCadastro, String caminhoReserva, int idR) {
        id = inicializarGeral(caminhoCadastro);

        Scanner ler = new Scanner(System.in);
        int opcao = 0;

        while (opcao != 3) {
            System.out.println("\n|-------------------------|");
            System.out.println("  Cadastramento ou Login ");
            System.out.println("|---------------------------|\n");
            System.out.println(" 1 - Cadastrar");
            System.out.println(" 2 - Fazer Login ");
            System.out.println(" 3 - Voltar ");
            System.out.print("\n Opção: ");
            opcao = ler.nextInt();

            switch (opcao) {
                case 1:
                    if (cadastrarUsuario(id, caminhoCadastro)) {
                        id++;
                        gravarId(id, "id.txt");
                    }
                    break;
                case 2:
                    //ateção aqui
                    loginUsuario(caminhoCadastro, id, caminhoReserva, idR);
                    break;
                case 3:
                    break;
                default:
                    System.out.println("Opção Inválida!");
            }
        }
    }

    private static void validarAdmin(int id, String caminhoReserva, int idR) {
        Scanner ler = new Scanner(System.in);
        Administrador senhaAdm = new Administrador();
        System.out.println("Informe a senha do Administrador");
        int senha = ler.nextInt();

        if (senha == senhaAdm.senhaAdmin) {
            menuAdmin(id, caminhoReserva, idR, caminhoReserva);
        } else {
            System.out.println("Senha incorreta");
            validarAdmin(id, caminhoReserva, idR);
        }
    }

    private static void menuAdmin(int id, String caminhoCadastro, int idR, String caminhoReserva) {
        id = inicializarGeral(caminhoCadastro);
        idR = inicializarReservas("clientes/reservas/");
        Scanner ler = new Scanner(System.in);
        int opcao = 0;
        while (opcao != 4) {
            System.out.println("\n|----------------------|");
            System.out.println("  Cavernas do Peruaçu  ");
            System.out.println("|----------------------|\n");
            System.out.println(" 1 - Fazer Reserva para Usuario ");
            System.out.println(" 2 - Listar Reservas");
            System.out.println(" 3 - Confirmar Reserva");
            System.out.println(" 4 - Voltar ");
            System.out.print("\n Opção: ");
            opcao = ler.nextInt();

            switch (opcao) {
                case 1:
                    System.out.println("Qual o ID do Usuario: ");
                    id = ler.nextInt();
                    if (reservaPasseio(id, caminhoCadastro, idR)) {
                        idR++;
                        gravarId(idR, "idR.txt");
                    }
                    break;
                case 2:
                    listarReservas();
                    break;
                case 3:
                    System.out.println("Qual o ID do Usuario: ");
                    id = ler.nextInt();
                    confirmarReserva(id, caminhoReserva);
                    break;
                case 4:
                    break;
                default:
                    System.out.println("Opção Inválida!");
                    break;
            }
        }
    }

    private static boolean reservaPasseio(int id, String caminhoReserva, int idR) {
        Scanner sc = new Scanner(System.in);
        Reserva c = new Reserva();
        System.out.println("|-------------------|");
        System.out.println(" Reservando Passeio ");
        System.out.println("|-------------------|\n");
        System.out.print("Nome: ");
        c.nome = sc.nextLine();
        System.out.print("Contato: ");
        c.contato = sc.nextLine();
        System.out.print("Quantidade de Adultos: ");
        c.adultos = sc.nextInt();
        System.out.print("Quantidade de Crianças: ");
        c.criancas = sc.nextInt();
        c.confirmada = "Não";
        System.out.println("|-------------------|");
        System.out.println(" Opções de Passeio ");
        System.out.println("|-------------------|\n");
        System.out.println("1 - Todo o Percurso");
        System.out.println("2 - Meio Percurso");
        System.out.print("opção: ");
        c.pacote = sc.nextInt();
        c.id = id;
        c.idR = idR;

        try {
            gravarReserva(c, caminhoReserva, c.id, c.idR); // Chamada atualizada
        } catch (FileNotFoundException e) {
            System.out.println("Não foi possível fazer Reserva: ");
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private static boolean cadastrarUsuario(int id, String path) {
        Scanner sc = new Scanner(System.in);
        Clientes cl = new Clientes();

        System.out.println("|-----------|");
        System.out.println("  Cadastrar  ");
        System.out.println("|-----------|\n");

        System.out.print("E-mail: ");
        String Email = sc.nextLine();

        // verificação de email
        if (emailJaCadastrado(Email, path)) {
            System.out.println("E-mail já está cadastrado. Por favor, use outro.");
            return false;
        }
        cl.email = Email;
        System.out.print("Senha: ");
        cl.senha = sc.nextLine();
        cl.id = id;
        try {
            gravarCadastroCliente(cl, path);
            return true;
        } catch (FileNotFoundException e) {
            System.out.println("Não foi possível cadastrar");
            e.printStackTrace();
            return false;
        }
    }

    // Método para verificar se um determinado e-mail já está cadastrado em arquivos do diretório especificado
    private static boolean emailJaCadastrado(String novoEmail, String path) {
        File diretorio = new File(path); // Cria um objeto de arquivo para o diretório
        File[] arquivos = diretorio.listFiles(); // Obtém a lista de arquivos no diretório

        if (arquivos != null) {  // Verifica se há arquivos no diretório
            for (File arquivo : arquivos) { // Itera sobre cada arquivo no diretório
                try {
                    BufferedReader bf = new BufferedReader(new FileReader(arquivo)); //Cria um leitor para o arquivo atual
                    String linha;

                    while ((linha = bf.readLine()) != null) {    // Lê cada linha do arquivo
                        if (linha.contains("Email do Usuario: ")) {  // Verifica se a linha contém "Email do Usuario:
                            String emailArquivo = linha.replace("Email do Usuario: ", ""); // Extrai o e-mail do arquivo
                            if (novoEmail.equals(emailArquivo)) { // Verifica se o e-mail fornecido é igual ao do arquivo
                                return true; // E-mail já cadastrado
                            }
                        }
                    }
                    bf.close();

                } catch (IOException e) {
                    System.err.println("Erro ao ler o arquivo");
                    e.printStackTrace();
                }
            }
        }

        // Retorna false se o e-mail não estiver cadastrado em nenhum arquivo
        return false;
    }


    private static void gravarId(int id, String arquivo) {
        PrintWriter pw;
        try {
            pw = new PrintWriter(arquivo);
            pw.println(id);
            pw.flush();
            pw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    // Método para listar as reservas
    private static void listarReservas() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Digite o ID do usuário: ");
        int id = scanner.nextInt();

        File dir = new File("clientes/reservas");   // Cria um objeto de arquivo para o diretório de reservas
        File[] arquivos = dir.listFiles();  // Obtém a lista de arquivos no diretório de reservas

        if (arquivos != null) {// Verifica se há arquivos no diretório de reservas
            boolean reserva = false; //indicar se reserva

            for (File arquivo : arquivos) {  // Itera sobre cada arquivo no diretório de reservas

                if (arquivo.getName().contains(id + "_")) {// Verifica se o nome do arquivo contém o ID do usuário como parte do nome
                    if (!reserva) { // Se for a primeira reserva encontrada, exibe mensagem indicando o início das reservas
                        System.out.println("Reservas do usuário com ID: " + id);
                        reserva = true;
                    }
                    exibirConteudoArquivo(arquivo);
                }
            }

            if (!reserva) {
                System.out.println("Nenhuma reserva encontrada para esse ID");
            }
        } else {
            // Se não houver arquivos no diretório de reservas, exibe mensagem apropriada
            System.out.println("Nenhuma reserva encontrada.");
        }
    }


    // Método para confirmar uma reserva com base no ID da reserva fornecido
    public static void confirmarReserva(int id, String caminhoReserva) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Informe o ID da reserva a ser confirmada: ");
        int idReserva = sc.nextInt();

        // Constrói o nome do arquivo a partir do ID do usuário e do ID da reserva
        String nomeArquivo = id + "_" + idReserva + ".txt";
        String caminhoCompleto = caminhoReserva + nomeArquivo; // Constrói o caminho completo para o arquivo de reserva

        // Cria um objeto de arquivo representando a reserva a ser confirmada
        File confirmarReserva = new File(caminhoCompleto);

        // Verifica se o arquivo da reserva existe
        if (confirmarReserva.exists()) {
            Confirmar(confirmarReserva);
            System.out.println("Reserva confirmada com sucesso!");
        } else {
            System.out.println("Reserva não encontrada para o ID informado.");
        }
    }

    // Método para marcar uma reserva como confirmada, alterando o conteúdo do arquivo
    private static void Confirmar(File rConfirmar) {
        try {
            List<String> linhas = Files.readAllLines(rConfirmar.toPath()); // Lê todas as linhas do arquivo

            // Itera sobre cada linha no arquivo
            for (int i = 0; i < linhas.size(); i++) {
                if (linhas.get(i).contains("Confirmada: Não")) { // Verifica se a linha atual contém a indicação "Confirmada: Não"
                    linhas.set(i, "Confirmada: Sim");  //Substitui a linha para marcar a reserva como confirmada
                    break;
                }
            }

            // Escreve as linhas de volta no arquivo, agora com a reserva confirmada
            Files.write(rConfirmar.toPath(), linhas);

        } catch (IOException e) {
            System.err.println("Erro ao marcar a reserva como confirmada");
            e.printStackTrace();
        }
    }


    // Métodos de inicialização
    private static int inicializarGeral(String caminhoReserva) {
        int id = 0;
        File dir = new File(caminhoReserva);
        if (!dir.exists()) {
            dir.mkdir();
        }

        File arquivo = new File("id.txt");
        if (!arquivo.exists()) {
            try {
                arquivo.createNewFile();
            } catch (IOException e) {
                System.out.println("Não foi possível criar o ID");
                e.printStackTrace();
            }
            gravarId(id, "id.txt");
        } else {
            id = lerId("id.txt");
        }
        return id;
    }

    private static int inicializarReservas(String caminhoReserva) {
        int idR = 0;
        File dir = new File(caminhoReserva);
        if (!dir.exists()) {
            dir.mkdir();
        }

        File arquivo = new File("idR.txt");
        if (!arquivo.exists()) {
            try {
                arquivo.createNewFile();
            } catch (IOException e) {
                System.out.println("Não foi possível criar o ID");
                e.printStackTrace();
            }
            gravarId(idR, "idR.txt");
        } else {
            idR = lerId("idR.txt");
        }
        return idR;
    }

    private static int lerId(String arquivo) {
        BufferedReader bf;
        int id = 0;
        try {
            bf = new BufferedReader(new FileReader(arquivo));
            id = Integer.parseInt(bf.readLine());
            bf.close();
        } catch (IOException | NumberFormatException e) {
            System.err.println("Id não encontrado");
            e.printStackTrace();
        }
        return id;
    }

    // Métodos relacionados ao cadastro de usuários
    private static void gravarCadastroCliente(Clientes cl, String path) throws FileNotFoundException {
        PrintWriter pw = new PrintWriter(path + cl.id + ".txt");
        pw.println("|--------------------|");
        pw.println("  Dados do Usuario");
        pw.println("|--------------------|\n");
        pw.println("Id do Usuario: " + cl.id);
        pw.println("Email do Usuario: " + cl.email);
        pw.println("Senha do Usuario: " + cl.senha);
        pw.flush();
        pw.close();
    }

    // Métodos relacionados à reserva de passeios
    private static void gravarReserva(Reserva c, String caminhoReserva, int id, int idR) throws FileNotFoundException {
        PrintWriter pw = new PrintWriter(caminhoReserva + id + "_" + idR + ".txt"); // Ajuste do nome do arquivo
        pw.println("Id do Usuario: " + c.id);
        pw.println("Id da Reserva: " + c.idR);
        pw.println("\nNome: " + c.nome);
        pw.println("Contato: " + c.contato);
        pw.println("Quantidade de Adultos: " + c.adultos);
        pw.println("Quantidade de Crianças: " + c.criancas);
        pw.println("Pacote escolhido: " + c.pacote);
        pw.println("Confirmada: " + c.confirmada);
        pw.flush();
        pw.close();
    }


    // Método para realizar o login do usuário
    private static boolean loginUsuario(String caminhoCadastro, int id, String caminhoReserva, int idR) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Informe o seu e-mail: ");
        String email = sc.nextLine();
        System.out.println("Informe a sua senha: ");
        String senha = sc.nextLine();

        // Cria um objeto de arquivo para o diretório de cadastros
        File diretorio = new File(caminhoCadastro);
        File[] arquivos = diretorio.listFiles();  // Obtém a lista de arquivos no diretório de cadastros

        if (arquivos != null) {// Verifica se há arquivos no diretório de cadastros
            for (File arquivo : arquivos) {  // Itera sobre cada arquivo no diretório de cadastros

                try {
                    BufferedReader bf = new BufferedReader(new FileReader(arquivo));// Cria um leitor para o arquivo atual
                    String linha;
                    String emailArquivo = "";
                    String senhaArquivo = "";
                    int idUsuario = -1;

                    // Lê cada linha do arquivo
                    while ((linha = bf.readLine()) != null) {
                        // Extrai informações relevantes (e-mail, senha, ID) de cada linha
                        if (linha.contains("Email do Usuario: ")) {
                            emailArquivo = linha.replace("Email do Usuario: ", "");
                        }
                        if (linha.contains("Senha do Usuario: ")) {
                            senhaArquivo = linha.replace("Senha do Usuario: ", "");
                        }
                        if (linha.contains("Id do Usuario: ")) {
                            idUsuario = Integer.parseInt(linha.replace("Id do Usuario: ", ""));
                        }
                    }

                    // Verifica se as credenciais fornecidas coincidem com as do arquivo
                    if (email.equals(emailArquivo) && senha.equals(senhaArquivo)) {
                        System.out.println("Login realizado com sucesso!");

                        menuUsuario(idUsuario, caminhoReserva, idR);
                        return true;
                    }
                    bf.close();
                } catch (IOException e) {

                    System.err.println("Erro ao ler o arquivo");
                    e.printStackTrace();
                }
            }
        }
        System.out.println("E-mail ou senha incorretos!");
        return false;
    }


    // Método para cancelar uma reserva com base no ID do usuário e ID da reserva
    private static void cancelarReserva(int id, String caminhoReserva) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Qual o ID da reserva que deseja cancelar?");
        int idReserva = sc.nextInt();
        String arquivoReserva = caminhoReserva + id + "_" + idReserva + ".txt"; // Constrói o caminho completo para o arquivo de reserva
        File CancelarRes = new File(arquivoReserva); // Cria um objeto de arquivo representando a reserva a ser cancelada

        if (CancelarRes.exists()) {
            if (CancelarRes.delete()) {
                System.out.println("Reserva cancelada com sucesso!");
            } else {
                System.out.println("Falha ao cancelar a reserva.");
            }
        } else {
            System.out.println("Reserva não encontrada para o ID informado.");
        }
    }


    private static void informacaoSobreParque() {
        System.out.println("\n|-------------------------|");
        System.out.println("  CAVERNAS DO PERUAÇU");
        System.out.println("|---------------------------|\n");
        System.out.println("O Parque Nacional Cavernas do Peruaçu é uma Unidade de\n" +
                "conservação criada por Decreto s/nº em 21de setembro de 1999 que tem\n" +
                "como principal objetivo proteger o patrimônio geológico e arqueológico existente nesta região.\n" +
                "Endereço: 155, BR-135, s/n - Fabião I, Januária - MG, 39480-000\n" +
                "Estabelecido: 21 de setembro de 1999\n" +
                "Administração: Instituto Chico Mendes de Conservação da Biodiversidade\n" +
                "Telefone: (38) 3623-1038\n");
    }

    // Métodos relacionados aos menus
    private static void menu() {
        System.out.println("\n|----------------------|");
        System.out.println("  Cavernas do Peruaçu  ");
        System.out.println("|----------------------|\n");
        System.out.println(" Entrar como: ");
        System.out.println(" 1 - Usuario ");
        System.out.println(" 2 - Administrador ");
        System.out.println(" 3 - Encerrar\n");
        System.out.print("Opção:");
    }

    private static void menuUsuario(int id, String caminhoReserva, int idR) {
        Scanner ler = new Scanner(System.in);
        idR = inicializarReservas("clientes/reservas/");
        int opcao = 0;
        while (opcao != 5) {
            System.out.println("\n|----------------------|");
            System.out.println("  Cavernas do Peruaçu  ");
            System.out.println("|----------------------|\n");
            System.out.println(" 1 - Ver Informações sobre o Parque");
            System.out.println(" 2 - Fazer Reserva");
            System.out.println(" 3 - ver Reserva");
            System.out.println(" 4 - Cancelar Reserva");
            System.out.println(" 5 - Voltar ");
            System.out.print("\n Opção: ");
            opcao = ler.nextInt();

            switch (opcao) {
                case 1:
                    informacaoSobreParque();
                    break;
                case 2:
                    if (reservaPasseio(id, caminhoReserva, idR)) {
                        idR++;
                        gravarId(idR, "idR.txt");
                    }
                    break;
                case 3:
                    verificarReservas(id, caminhoReserva);
                    break;
                case 4:
                    cancelarReserva(id, caminhoReserva);
                    break;
                case 5:
                    break;
                default:
                    System.out.println("Opção Inválida!");
                    break;
            }
        }
    }

    private static void verificarReservas(int id, String caminhoReserva) {
        File diretorio = new File(caminhoReserva);
        File[] arquivos = diretorio.listFiles();

        if (arquivos != null) {
            boolean reservas = false;

            for (File arquivo : arquivos) {
                String nomeArquivo = arquivo.getName();

                // Verifica se o arquivo corresponde ao usuário
                if (nomeArquivo.startsWith(id + "_")) {
                    reservas = true;
                    System.out.println("Reserva encontrada:");

                    // Exibir informações do arquivo da reserva
                    exibirConteudoArquivo(arquivo);
                    System.out.println("---------------------");
                }
            }

            if (!reservas) {
                System.out.println("Nenhuma reserva encontrada para este usuário.");
            }
        } else {
            System.out.println("Nenhuma reserva encontrada.");
        }
    }

    // Método para exibir o conteúdo de um arquivo
    private static void exibirConteudoArquivo(File arquivo) {
        try {
            // Cria um leitor para o arquivo
            BufferedReader bf = new BufferedReader(new FileReader(arquivo));
            String linha;
            while ((linha = bf.readLine()) != null) {  // Lê cada linha do arquivo e imprime no console
                System.out.println(linha);
            }

            bf.close();
        } catch (IOException e) {
            System.err.println("Erro ao ler o arquivo da reserva");
            e.printStackTrace();
        }
    }
}
