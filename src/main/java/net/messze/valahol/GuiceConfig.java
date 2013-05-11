package net.messze.valahol;


import com.google.inject.Binder;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.name.Names;
import com.google.inject.servlet.GuiceServletContextListener;
import com.sun.jersey.guice.JerseyServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;
import com.wordnik.swagger.jaxrs.JaxrsApiReader;
import net.messze.valahol.service.MongodbPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

public class GuiceConfig extends GuiceServletContextListener {
    private static final Logger LOG = LoggerFactory.getLogger(GuiceConfig.class);

    @Override
    protected Injector getInjector() {
        return Guice.createInjector(new JerseyServletModule() {

            @Override
            protected void configureServlets() {
                loadConfiguration(binder());
                JaxrsApiReader.setFormatString("");
                bind(MongodbPersistence.class);
                bind(JacksonJsonProvider.class);
                bind(JAXBContextResolver.class);
                bind(PuzzleApi.class);
                bind(UserApi.class);
                bind(AuthApi.class);
                bind(MiscApi.class);
                bind(SwaggerApiList.class);
                Names.bindProperties(binder(), new Properties());
                serve("/rest/*").with(GuiceContainer.class);
            }
        });
    }

    public static void loadConfiguration(Binder binder) {
        File configFile = new File(System.getProperty("user.home"), ".valahol");
        if (System.getenv("VALAHOL_CONFIG") != null) {
            configFile = new File(System.getenv("VALAHOL_CONFIG"));
        }
        LOG.info("Trying to load properties from " + configFile.getAbsolutePath());
        Properties defaultProperties = new Properties();
        defaultProperties.put("mongoHost", "localhost");
        defaultProperties.put("mongoPort", "27017");
        defaultProperties.put("mongoDb", "valahol");
        Properties p = new Properties(defaultProperties);
        if (configFile.exists()) {
            try {
                p.load(new FileInputStream(configFile));
            } catch (IOException e) {
                LOG.error("Can't load config file " + configFile.getAbsolutePath(), e);
            }
        }
        Set<String> keys = new TreeSet<String>();
        for (Object o : p.keySet()) {
            keys.add(o.toString());
        }
        for (Object o : defaultProperties.keySet()) {
            keys.add(o.toString());
        }

        for (Object property : keys) {
            LOG.debug("Property " + property.toString() + "=" + p.getProperty(property.toString()) + " " + (p.getProperty(property.toString()).equals(defaultProperties.getProperty(property.toString())) ? "(default)" : ""));
        }
        Names.bindProperties(binder, p);
    }
}
