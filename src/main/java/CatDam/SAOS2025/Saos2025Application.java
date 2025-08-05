package CatDam.SAOS2025;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
@SpringBootApplication
@EnableJpaRepositories(basePackages = "CatDam.SAOS2025.repositories")
@EntityScan(basePackages = "CatDam.SAOS2025.entities")
public class Saos2025Application {
	public static void main(String[] args) {
		SpringApplication.run(Saos2025Application.class, args);
	}
}