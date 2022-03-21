package cn.wmyskxz;

import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.MybatisXMLLanguageDriver;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import org.apache.ibatis.logging.stdout.StdOutImpl;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.JdbcType;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;

@Configuration
public class MybatisPlusConfig {
    @Autowired
    private DataSource dataSource;


    @Bean(name = "globalConfig")
    @Primary
    public GlobalConfig globalConfig() {
        GlobalConfig globalConfig = new GlobalConfig();
        return globalConfig;
    }

    @Bean("sqlSessionFactory")
    @Primary
    public SqlSessionFactory dbSqlSessionFactory(@Qualifier("dataSource") DataSource dataSource, GlobalConfig globalConfig) throws Exception {
        // MybatisSqlSessionFactoryBean
        MybatisSqlSessionFactoryBean factoryBean = new MybatisSqlSessionFactoryBean();
        factoryBean.setDataSource(dataSource);
        factoryBean.setGlobalConfig(globalConfig());

        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        // 需要在这里指定xml文件的位置，不然自定义的sql会报Invalid bound statement异常
        factoryBean.setMapperLocations(resolver.getResources("classpath*:cn/wmyskxz/xml/*Mapper.xml"));

        // 导入mybatis配置
        MybatisConfiguration mybatisConfiguration = new MybatisConfiguration();
        // 配置打印sql语句
        mybatisConfiguration.setLogImpl(StdOutImpl.class);
        factoryBean.setConfiguration(mybatisConfiguration);

        return factoryBean.getObject();
    }

//    @Bean
//    public MybatisSqlSessionFactoryBean mybatisSqlSessionFactoryBean() {
//        MybatisSqlSessionFactoryBean mybatisPlus = new MybatisSqlSessionFactoryBean();
//        mybatisPlus.setDataSource(dataSource);
//        return mybatisPlus;
//    }
//    @Bean
//    public MapperScannerConfigurer mapperScannerConfigurer(){
//        MapperScannerConfigurer scannerConfigurer = new MapperScannerConfigurer();
//        //可以通过环境变量获取你的mapper路径,这样mapper扫描可以通过配置文件配置了
//        scannerConfigurer.setBasePackage("com.wzh.db.wzh_dbPractice.mapper");
//        return scannerConfigurer;
//    }
}

