package com.ecar.eoc.config.datasource;

import javax.sql.DataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

@Configuration
@MapperScan(basePackages = {"com.ecar.eoc.mapper.simdb"}, sqlSessionFactoryRef = "simSqlSessionFactory")
public class MybatisDBSimConfig {
	@Autowired
    @Qualifier("simDS")
    private DataSource simDS;

    @Bean(name = "simSqlSessionFactory")
    public SqlSessionFactory simSqlSessionFactory() throws Exception{
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(simDS);

        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        try {
            sqlSessionFactoryBean.setMapperLocations(resolver.getResources("classpath:mybatis/mapper/sim/*.xml"));
            return sqlSessionFactoryBean.getObject();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }

    @Bean(name = "simSqlSessionTemplate")
    public SqlSessionTemplate simSqlSessionTemplate() throws Exception{
        SqlSessionTemplate sqlSessionTemplate = new SqlSessionTemplate(simSqlSessionFactory());
        return sqlSessionTemplate;
    }

    @Bean(name = "simDataSourceTransactionManager")
    public DataSourceTransactionManager simDataSourceTransactionManager() throws Exception{
        DataSourceTransactionManager manager = new DataSourceTransactionManager(simDS);
        return manager;
    }
}
