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
@MapperScan(basePackages = {"com.ecar.eoc.mapper.iotdb"}, sqlSessionFactoryRef = "iotSqlSessionFactory")
public class MybatisDBIotConfig {
	@Autowired
    @Qualifier("iotDS")
    private DataSource iotDS;

    @Bean(name = "iotSqlSessionFactory")
    public SqlSessionFactory iotSqlSessionFactory() throws Exception{
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(iotDS);

        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        try {
            sqlSessionFactoryBean.setMapperLocations(resolver.getResources("classpath:mybatis/mapper/iot/*.xml"));
            return sqlSessionFactoryBean.getObject();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }

    @Bean(name = "iotSqlSessionTemplate")
    public SqlSessionTemplate iotSqlSessionTemplate() throws Exception{
        SqlSessionTemplate sqlSessionTemplate = new SqlSessionTemplate(iotSqlSessionFactory());
        return sqlSessionTemplate;
    }

    @Bean(name = "iotDataSourceTransactionManager")
    public DataSourceTransactionManager iotDataSourceTransactionManager() throws Exception{
        DataSourceTransactionManager manager = new DataSourceTransactionManager(iotDS);
        return manager;
    }
}
