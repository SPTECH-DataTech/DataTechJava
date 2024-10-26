package writer;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

public class ConexaoBanco {

    private final DataSource dataSource;

    public ConexaoBanco() {
        BasicDataSource basicDataSource = new BasicDataSource();
        basicDataSource.setUrl("jdbc:mysql://localhost:3306/datatech");
        basicDataSource.setUsername("root");
        basicDataSource.setPassword("sada1613");

        this.dataSource = basicDataSource;
    }

    public JdbcTemplate getConnection() {
        return new JdbcTemplate(dataSource);
    }

}
