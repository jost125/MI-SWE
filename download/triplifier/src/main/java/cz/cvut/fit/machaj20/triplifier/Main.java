package cz.cvut.fit.machaj20.triplifier;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {
    public static void main(String[] args) {
		 if (args.length != 2) {
			 System.err.println("Usage java -jar triplifier.jar <cache_dir> <output_dir>");
			 System.exit(-1);
		 }

		 ApplicationContext context = new ClassPathXmlApplicationContext("spring-config.xml");
		 context.getBean(Application.class).run(args[0], args[1]);
    }
}
