package sincronizacaoreceita.config;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import sincronizacaoreceita.entities.Account;
import sincronizacaoreceita.services.ReceitaService;

@Configuration
@Profile("test")
public class TestConfig implements CommandLineRunner {

	@Override
	public void run(String... args) throws Exception {
		System.out.println("Started...");
		System.out.println("");
		reader();
		System.out.println("");
		System.out.println("End of processing!");
	}

	// Leitura do arquivo de entrada "in.csv' no diretório "source"
	public void reader() {

		String path = "source\\in.csv";

		List<Account> list = new ArrayList<Account>();

		try (BufferedReader br = new BufferedReader(new FileReader(path))) {

			String line = br.readLine();
			line = br.readLine();

			while (line != null) {
				String[] vect = line.split(";");
				String agencia = vect[0];
				String conta = vect[1].replace("-", "");
				Double saldo = Double.parseDouble(vect[2].replace(",", "."));
				String status = vect[3];

				ReceitaService receitaService = new ReceitaService();
				try {
					receitaService.atualizarConta(agencia, conta, saldo, status);
				} catch (RuntimeException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				Account account = new Account(agencia, conta, saldo, status);
				list.add(account);

				System.out.println(line);
				writer(line);
				line = br.readLine();

			}

		} catch (IOException e) {
			System.out.println("Error: " + e.getMessage());
		}
	}

	// Escrita do arquivo de saída "out.csv" no diretório destiny 
	public void writer(String line) {

		String path = "destiny\\out.csv";

		try (BufferedWriter bw = new BufferedWriter(new FileWriter(path, true))) {
			bw.write(line);
			bw.newLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}