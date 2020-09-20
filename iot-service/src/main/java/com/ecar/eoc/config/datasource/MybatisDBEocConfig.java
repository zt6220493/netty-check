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
@MapperScan(basePackages = {"com.ecar.eoc.mapper.eocdb"}, sqlSessionFactoryRef = "eocSqlSessionFactory")
public class MybatisDBEocConfig {
	@Autowired
    @Qualifier("eocDS")
    private DataSource eocDS;

    @Bean(name = "eocSqlSessionFactory")
    public SqlSessionFactory eocSqlSessionFactory() throws Exception{
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(eocDS);

        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        try {
            sqlSessionFactoryBean.setMapperLocations(resolver.getResources("classpath:mybatis/mapper/eoc/*.xml"));
            return sqlSessionFactoryBean.getObject();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }

    @Bean(name = "eocSqlSessionTemplate")
    public SqlSessionTemplate eocSqlSessionTemplate() throws Exception{
        SqlSessionTemplate sqlSessionTemplate = new SqlSessionTemplate(eocSqlSessionFactory());
        return sqlSessionTemplate;
    }

    @Bean(name = "eocDataSourceTransactionManager")
    public DataSourceTransactionManager eocDataSourceTransactionManager() throws Exception{
        DataSourceTransactionManager manager = new DataSourceTransactionManager(eocDS);
        return manager;
    }
}
