package edu.univ.erp.data;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import javax.sql.DataSource;

public class DbPool {
    private static HikariDataSource authDataSource;
    private static HikariDataSource erpDataSource;

    static {
        try {
            Properties props = new Properties();
            // Load database settings from src/main/resources/app.properties
            props.load(new FileInputStream("src/main/resources/app.properties"));

            // ---------- AUTH DB ----------
            HikariConfig authConfig = new HikariConfig();
            authConfig.setJdbcUrl(props.getProperty("auth.jdbc"));
            authConfig.setUsername(props.getProperty("auth.user"));
            authConfig.setPassword(props.getProperty("auth.pass"));
            authConfig.setMaximumPoolSize(5);
            authConfig.setPoolName("AuthDBPool");
            authDataSource = new HikariDataSource(authConfig);

            // ---------- ERP DB ----------
            HikariConfig erpConfig = new HikariConfig();
            erpConfig.setJdbcUrl(props.getProperty("erp.jdbc"));
            erpConfig.setUsername(props.getProperty("erp.user"));
            erpConfig.setPassword(props.getProperty("erp.pass"));
            erpConfig.setMaximumPoolSize(5);
            erpConfig.setPoolName("ErpDBPool");
            erpDataSource = new HikariDataSource(erpConfig);

            System.out.println("✅ Database pools initialized successfully!");
        } catch (IOException e) {
            System.err.println("❌ Could not load app.properties: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("❌ Failed to initialize database pools: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static DataSource getAuthDataSource() {
        return authDataSource;
    }

    public static DataSource getErpDataSource() {
        return erpDataSource;
    }

    public static void close() {
        if (authDataSource != null) authDataSource.close();
        if (erpDataSource != null) erpDataSource.close();
    }
}
