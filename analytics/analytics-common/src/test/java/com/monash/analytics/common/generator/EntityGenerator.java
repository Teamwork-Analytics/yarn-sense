package com.monash.analytics.common.generator;


import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.BeetlTemplateEngine;
import org.junit.jupiter.api.Test;


import java.util.HashMap;
import java.util.Map;


public class EntityGenerator {

    private String outputDir = "/Users/xinyu/developer/IdeaProjects/analytics-parent/analytics-common/src/main/java";
    private String author = "xinyu";
    private String url = "jdbc:mysql://127.0.0.1:3306/for_future_use?autoReconnect=true&useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=CONVERT_TO_NULL&useSSL=false&serverTimezone=UTC";
    private String driverName = "com.mysql.cj.jdbc.Driver";
    private String userName = "root";
    private String userPwd = "5r6t7y8U";
    private String daoPackage = "com.monash.analytics.common.dao";

    private static String[] tableNames;

    static{
        tableNames = new String[]{
                "mooc_backend_user_t"
        };
    }

    @Test
    public void entityGenerator() {
        AutoGenerator mpg = new AutoGenerator();
        mpg.setTemplateEngine(new BeetlTemplateEngine());

        GlobalConfig gc = new GlobalConfig();
        gc.setOutputDir(outputDir);
        gc.setFileOverride(true);
        gc.setActiveRecord(true);
        gc.setEnableCache(false);
        gc.setBaseResultMap(true);
        gc.setBaseColumnList(false);
        gc.setAuthor(author);
        mpg.setGlobalConfig(gc);

        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl(url);
        dsc.setDriverName(driverName);
        dsc.setUsername(userName);
        dsc.setPassword(userPwd);
        mpg.setDataSource(dsc);

        StrategyConfig strategy = new StrategyConfig();
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setInclude(tableNames);
        mpg.setStrategy(strategy);

        PackageConfig pc = new PackageConfig();
        pc.setParent(null);
        pc.setEntity(daoPackage+".entity");
        pc.setMapper(daoPackage+".mapper");
        pc.setXml(daoPackage+".mapper.xml");
        mpg.setPackageInfo(pc);

        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                Map<String, Object> map = new HashMap<>();
                map.put("abc", this.getConfig().getGlobalConfig().getAuthor() + "-mp");
                this.setMap(map);
            }
        };

        mpg.setCfg(cfg);

        mpg.execute();

        System.err.println(mpg.getCfg().getMap().get("abc"));
    }
}
