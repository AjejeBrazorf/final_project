package utility;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.model.naming.ImplicitNamingStrategy;
import org.hibernate.boot.model.naming.ImplicitNamingStrategyJpaCompliantImpl;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistry;

import com.javasampleapproach.security.model.BusLine;
import com.javasampleapproach.security.model.BusLineStop;
import com.javasampleapproach.security.model.BusStop;

import sun.security.x509.IPAddressName;

public class HibernateUtil {
	
	private static final SessionFactory sessionFactory = buildSessionFactory();
	private static final String ipAddress = "localhost";
	
	private static SessionFactory buildSessionFactory() {
		try {
			
			ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
					.applySetting(Environment.DRIVER,"org.postgresql.Driver")
					//qua sono da cambiare a seconda se JPA o Hibernate
					.applySetting(Environment.USER, "postgres")
			    	.applySetting(Environment.PASS, "ai-user-password")
			    	.applySetting(Environment.URL,"jdbc:postgresql://"+ ipAddress + ":5432/trasporti")
					.applySetting(Environment.DIALECT,"org.hibernate.spatial.dialect.postgis.PostgisDialect")
					.applySetting(Environment.HBM2DDL_AUTO,"validate")
					.applySetting(Environment.CURRENT_SESSION_CONTEXT_CLASS, "thread")
					//.applySetting(Environment.FORMAT_SQL, "true")
					//.applySetting(Environment.SHOW_SQL, "true")
					.build();
			
			
			Metadata metadata= new MetadataSources(serviceRegistry)
					.addAnnotatedClass(BusLine.class) 
					.addAnnotatedClass(BusStop.class) 
					.addAnnotatedClass(BusLineStop.class) 
					.getMetadataBuilder()
					.applyImplicitNamingStrategy(ImplicitNamingStrategyJpaCompliantImpl.INSTANCE)
					.build();	

			return metadata.getSessionFactoryBuilder().build();
		}
		catch (Throwable ex) {
			// Make sure you log the exception, as it might be swallowed
			ex.printStackTrace();
			System.err.println("Initial SessionFactory creation failed." + ex);
			throw new ExceptionInInitializerError(ex);
		}
	}
	
	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}
}
